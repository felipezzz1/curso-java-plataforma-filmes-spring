package br.com.fezor.screenmatch.services.translate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataTranslate(@JsonAlias(value = "responseData") DataResponse dataResponse) {
}