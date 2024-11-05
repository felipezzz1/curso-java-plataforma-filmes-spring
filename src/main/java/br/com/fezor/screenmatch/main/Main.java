package br.com.fezor.screenmatch.main;

import br.com.fezor.screenmatch.models.Episode;
import br.com.fezor.screenmatch.models.EpisodeData;
import br.com.fezor.screenmatch.models.SeasonData;
import br.com.fezor.screenmatch.models.SeriesData;
import br.com.fezor.screenmatch.services.DataConvert;
import br.com.fezor.screenmatch.services.UseAPI;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner read = new Scanner(System.in);
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private UseAPI useApi = new UseAPI();
    private DataConvert dataConvert = new DataConvert();

    Dotenv dotenv = Dotenv.load();
    private final String apiKey = dotenv.get("API_KEY");

    public void showMenu(){
        var menu = """
                1 - Search Series
                2 - Search Episodes
                
                0 - Exit
                """;

        System.out.println(menu);
        var option = read.nextInt();
        read.nextLine();

        switch (option){
            case 1:
                searchSerieWeb();
                break;
            case 2:
                searchEpisodeBySerie();
                break;
            case 0:
                System.out.println("Exiting...");
            default:
                System.out.println("Invalid Option");
        }
    }

    private void searchSerieWeb(){
        SeriesData data = getSeriesData();
        System.out.println(data);
    }

    private SeriesData getSeriesData(){
        System.out.println("Type the serie you want to search: ");
        var seriesName = read.nextLine();
        var json = useApi.getData(ADDRESS + seriesName.replace(" ", "+") + "&apikey=" + apiKey);
        return dataConvert.getData(json, SeriesData.class);
    }

    private void searchEpisodeBySerie(){
        SeriesData seriesData = getSeriesData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 0; i < seriesData.totalSeasons(); i++) {
            var json = useApi.getData(ADDRESS + seriesData.title().replace(" ", "+")+ "&season=" + i + "&apikey=" + apiKey);
            SeasonData seasonData = dataConvert.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }
}
