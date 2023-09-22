package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.LinkedHashMap;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private int total;
    private LinkedHashMap query;
    private LinkedHashMap _embedded;

}
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class ApiResponse {
//    private int total;
//    private LinkedHashMap query;
//    private LinkedHashMap _embedded;
//
//}
