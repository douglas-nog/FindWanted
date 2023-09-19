package br.com.fiap.findwanted.service;

import br.com.fiap.findwanted.model.DataWanted;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedData {
    private List<DataWanted> notices;

    public List<DataWanted> getNotices() {
        return notices;
    }

    public void setNotices(List<DataWanted> notices) {
        this.notices = notices;
    }
}
