package br.com.fiap.findwanted.entities.interpol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterpolLinks {

    private InterpolLink self;
    private InterpolLink images;
    private InterpolLink thumbnail;

}

