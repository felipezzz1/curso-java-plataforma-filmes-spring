package br.com.fezor.screenmatchSpring.main;

import br.com.fezor.screenmatchSpring.models.Episode;
import br.com.fezor.screenmatchSpring.models.EpisodeData;
import br.com.fezor.screenmatchSpring.models.SeasonData;
import br.com.fezor.screenmatchSpring.models.SeriesData;
import br.com.fezor.screenmatchSpring.services.DataConvert;
import br.com.fezor.screenmatchSpring.services.UseAPI;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner read = new Scanner(System.in);
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private UseAPI useApi = new UseAPI();
    private DataConvert dataConvert = new DataConvert();

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY");

    public void showMenu(){
        System.out.println("Please type the serie you want to search: ");
        var serieName = read.nextLine();


        var json = useApi.getData(ADDRESS +
                serieName.replace(" ","+") +
                "&apikey=" + apiKey);

        SeriesData data = dataConvert.getData(json, SeriesData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i <= data.totalSeasons(); i++) {
			json = useApi.getData(ADDRESS +
                    serieName.replace(" ", "+") +
                    "&season=" + i +
                    "&apikey=" + apiKey);
			SeasonData seasonData = dataConvert.getData(json, SeasonData.class);
			seasons.add(seasonData);
		}
		seasons.forEach(System.out::println);

        for (int i = 0; i < data.totalSeasons(); i++) {
            List<EpisodeData> seasonEpisodes = seasons.get(i).episodes();
            for (EpisodeData seasonEpisode : seasonEpisodes) {
                System.out.println(seasonEpisode.title());
            }
        }

//      lambda function
        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

//      Here if you don't want to modify the
//      List you can use .toList() instead of the
//      .collect(Collectors.toList())
        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(t->t.episodes().stream())
                .collect(Collectors.toList());

//        System.out.println("Top 10 episodes: ");
//        episodesData.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("First Filter(N/A)" + e))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .peek(e-> System.out.println("Ordination" + e))
//                .limit(10)
//                .peek(e-> System.out.println("Limit" + e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e-> System.out.println("Map" + e))
//                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.number(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

//        System.out.println("Please type the episode title excerpt: ");
//        var titleExcerpt = read.nextLine();
//
//        Optional<Episode> searchEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toLowerCase().contains(titleExcerpt.toLowerCase()))
//                .findFirst();
//
//        if(searchEpisode.isPresent()){
//            System.out.println("Episode found!");
//            System.out.println("Episode title: " + searchEpisode.get().getTitle());
//            System.out.println("Season: " + searchEpisode.get().getSeason());
//            System.out.println("Episode number: " + searchEpisode.get().getNumber());
//        }else{
//            System.out.println("Episode not found!");
//        }

//        System.out.println("From each year do you want to list the episodes? ");
//        var year  = read.nextInt();
//        read.nextLine();
//
//        LocalDate searchData = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleaseDate()!= null && e.getReleaseDate().isAfter(searchData))
//                .forEach(e-> System.out.println(
//                        "Season" + e.getSeason() +
//                                "Episode" + e.getTitle() +
//                                "ReleaseDate" + e.getReleaseDate().format(formatter)
//                ));

        Map<Integer, Double> ratingBySeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));

        System.out.println(ratingBySeason);

        DoubleSummaryStatistics statistics = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));

        System.out.println("Average: " + statistics.getAverage()
                + "\nBest Episode: " + statistics.getMax()
                + "\nWorst Episode: " + statistics.getMin()
                + "\nNumber of Episodes: " + statistics.getCount());
    }
}
