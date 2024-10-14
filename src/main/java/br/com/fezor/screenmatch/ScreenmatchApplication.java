package br.com.fezor.screenmatch;

import br.com.fezor.screenmatch.models.EpisodeData;
import br.com.fezor.screenmatch.models.SeriesData;
import br.com.fezor.screenmatch.services.DataConvert;
import br.com.fezor.screenmatch.services.UseAPI;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Dotenv dotenv = Dotenv.load();
		String apiKey = dotenv.get("API_KEY");

		var useAPI = new UseAPI();
		var json = useAPI.getData("https://www.omdbapi.com/?t=prison+break&apikey=" + apiKey);

		System.out.println(json);

		DataConvert converter = new DataConvert();
		SeriesData data = converter.getData(json, SeriesData.class);
		System.out.println(data);

		json = useAPI.getData("https://www.omdbapi.com/?t=prison+break&season=1&episode=1&apikey=" + apiKey);
		EpisodeData episodeData = converter.getData(json, EpisodeData.class);
		System.out.println(episodeData);
	}
}
