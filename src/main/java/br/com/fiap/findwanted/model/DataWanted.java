package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataWanted(@JsonAlias({"name", "title"}) String name,
                         @JsonAlias("forename") String forename,
                         @JsonAlias({"date_of_birth", "dates_of_birth_used"}) String dateOfBirth,
                         @JsonAlias({"gender", "sex"}) String gender,
                         @JsonAlias("thumbnail") String thumbnail
                         ) {


}
