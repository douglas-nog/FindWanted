package br.com.fiap.findwanted.model.fbi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBIWantedList {

    private Integer total;
    private List<FBIWantedPerson> items;
    private Integer page;
}
