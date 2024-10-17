package br.com.fezor.screenmatch.main;

import br.com.fezor.screenmatch.models.EpisodeData;
import br.com.fezor.screenmatch.models.SeasonData;
import br.com.fezor.screenmatch.models.SeriesData;
import br.com.fezor.screenmatch.services.DataConvert;
import br.com.fezor.screenmatch.services.UseAPI;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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
        List<EpisodeData> episodes = seasons.stream()
                .flatMap(t->t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 episodes: ");
        episodes.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
