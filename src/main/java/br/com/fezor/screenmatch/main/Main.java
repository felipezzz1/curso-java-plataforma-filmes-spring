package br.com.fezor.screenmatch.main;

import br.com.fezor.screenmatch.models.EpisodeData;
import br.com.fezor.screenmatch.models.SeasonData;
import br.com.fezor.screenmatch.models.SeriesData;
import br.com.fezor.screenmatch.services.DataConvert;
import br.com.fezor.screenmatch.services.UseAPI;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

//        for (int i = 0; i < data.totalSeasons(); i++) {
//            List<EpisodeData> seasonEpisodes = seasons.get(i).episodes();
//            for (EpisodeData seasonEpisode : seasonEpisodes) {
//                System.out.println(seasonEpisode.title());
//            }
//        }

//      lambda function
        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        // using streams to chain operations
        List<String> names = Arrays.asList("Michael", "Lincoln", "Junior");

//      here it is an example using the name list
//      to concatenate the methods sorted, limit and filter
        names.stream()
                .sorted()
                .limit(2)
                .filter(n -> n.startsWith("M"))
                .forEach(System.out::println);
    }
}
