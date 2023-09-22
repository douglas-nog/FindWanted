package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notice {

    private String date_of_birth;
    private List<String> nationalities;
    private String entity_id;
    private String forename;
    private String name;
    private Links _links;
}
