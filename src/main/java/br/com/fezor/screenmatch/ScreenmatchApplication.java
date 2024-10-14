package br.com.fezor.screenmatch;

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
		var json = useAPI.getData("https://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=" + apiKey);

		System.out.println(json);


	}
}
