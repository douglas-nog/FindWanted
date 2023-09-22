package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private int total;
    private Query query;
    private Embedded _embedded;
}

