package br.com.fezor.screenmatch.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;

//not used XD
public class ChatGptService {

    public static String getTranslate(String text){
        Dotenv dotenv = Dotenv.load();
        final String chatgptKey = dotenv.get("CHATGPT_KEY");

        OpenAiService service = new OpenAiService(chatgptKey);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("please translate the text to brazilian portuguese" + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}
