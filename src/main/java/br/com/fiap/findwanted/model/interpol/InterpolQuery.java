package br.com.fiap.findwanted.model.interpol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterpolQuery {
    private int page;
    private int resultPerPage;

}