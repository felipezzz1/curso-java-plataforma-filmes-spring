package br.com.fezor.screenmatch.services.translate;

import br.com.fezor.screenmatch.services.UseAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class MyMemoryService {
    public static String getTranslation(String text){
        ObjectMapper mapper = new ObjectMapper();

        UseAPI useApi = new UseAPI();

        String text1 = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + text1 + "&langpair=" + langpair;

        String json = useApi.getData(url);

        DataTranslate translate;
        try{
            translate = mapper.readValue(json, DataTranslate.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }

        return translate.dataResponse().translatedText();
    }
}
