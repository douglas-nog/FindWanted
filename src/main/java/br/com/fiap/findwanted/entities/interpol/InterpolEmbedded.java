package br.com.fiap.findwanted.entities.interpol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterpolEmbedded {
    private List<InterpolNotice> notices;
}
