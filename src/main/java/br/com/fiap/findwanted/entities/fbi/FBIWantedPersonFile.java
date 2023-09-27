package br.com.fiap.findwanted.entities.fbi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBIWantedPersonFile {
    private String name;
    private String url;
}
