package br.com.fiap.findwanted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Notice {

    private LinkedHashMap date_of_birth;
    private LinkedHashMap nationalities;
    private LinkedHashMap entity_id;
    private LinkedHashMap forename;
    private LinkedHashMap name;
    private LinkedHashMap _links;

}
/* forma que esta funcionando
    private HashMap date_of_birth;
    private LinkedHashMap nationalities;
    private HashMap entity_id;
    private HashMap forename;
    private HashMap name;
    private LinkedHashMap _links;*/

