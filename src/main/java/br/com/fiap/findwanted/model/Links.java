package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {

    private Link self;
    private Link images;
    private Link thumbnail;

}

