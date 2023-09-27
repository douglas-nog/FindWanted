package br.com.fiap.findwanted.services;

import br.com.fiap.findwanted.controller.ConsumeAPI;
import br.com.fiap.findwanted.entities.WantedPeopleEntity;
import br.com.fiap.findwanted.entities.fbi.FBIWantedList;
import br.com.fiap.findwanted.entities.interpol.InterpolWantedList;
import br.com.fiap.findwanted.repository.WantedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class WantedService {

    @Autowired
    private WantedRepository repository;

    private Converter converter = new Converter();
    private ConsumeAPI consumeAPI = new ConsumeAPI();

    DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    public void createWantedEntityFromFbi(FBIWantedList apiResponseFbi) {
        WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
        wantedPeopleEntity.setName(apiResponseFbi.getItems().get(0).getTitle().toLowerCase());
        if (apiResponseFbi.getItems().get(0).getAliases() != null) {
            wantedPeopleEntity.setForename(apiResponseFbi.getItems().get(0).getAliases().get(0).toLowerCase());
        } else {
            wantedPeopleEntity.setForename(null);
        }
        wantedPeopleEntity.setThumbnail(apiResponseFbi.getItems().get(0).getImages().get(0).getOriginal());
        if (apiResponseFbi.getItems().get(0).getDates_of_birth_used() != null) {
            LocalDate localDate = LocalDate.parse(apiResponseFbi.getItems().get(0).getDates_of_birth_used().get(0), formatoEntrada);
            String dataformata = localDate.format(formatoSaida);
            wantedPeopleEntity.setDateOfBirth(LocalDate.parse(dataformata,formatoSaida));
        } else {
            wantedPeopleEntity.setDateOfBirth(null);
        }
        wantedPeopleEntity.setGovernmetnalOrganization("FBI");
        wantedPeopleEntity.setIdFromSource(apiResponseFbi.getItems().get(0).getUid());
        repository.save(wantedPeopleEntity);
    }

    public void createWantedEntityFromInterpol(InterpolWantedList apiResponseInterpol) {
        WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
        wantedPeopleEntity.setName(apiResponseInterpol.get_embedded().getNotices().get(0).getName().toLowerCase());
        wantedPeopleEntity.setForename(apiResponseInterpol.get_embedded().getNotices().get(0).getForename().toLowerCase());
        wantedPeopleEntity.setThumbnail(apiResponseInterpol.get_embedded().getNotices().get(0).get_links().getThumbnail().getHref());
        wantedPeopleEntity.setDateOfBirth(LocalDate.parse(apiResponseInterpol.get_embedded().getNotices().get(0).getDate_of_birth(), formatoSaida));
        wantedPeopleEntity.setGovernmetnalOrganization("Interpol");
        wantedPeopleEntity.setIdFromSource(apiResponseInterpol.get_embedded().getNotices().get(0).getEntity_id());
        repository.save(wantedPeopleEntity);
    }

    public InterpolWantedList fetchInterpolData(String encodedName) {
        var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=" + encodedName);
        InterpolWantedList apiResponseInterpol = converter.getData(jsonInterpol, InterpolWantedList.class);
        return apiResponseInterpol;
    }

    public FBIWantedList fetchFbiData(String encodedName) {
        var jsonFbi = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list?title=" + encodedName);
        FBIWantedList apiResponseFbi = converter.getData(jsonFbi, FBIWantedList.class);
        return apiResponseFbi;
    }

    public boolean isInFbiList(FBIWantedList apiResponse, String name) {
        if (apiResponse.getItems().stream().map(i -> i.getTitle().toLowerCase())
                .collect(Collectors.toList()).contains(name.toLowerCase())) {
            return true;

        } else {
            return false;
        }
    }

    public static ResponseEntity<String> getStringResponseEntityInterpol(String name, InterpolWantedList apiResponseInterpol, String organization) {
        if (apiResponseInterpol.get_embedded().getNotices().get(0).get_links().getThumbnail() != null) {
            String thumbnail = apiResponseInterpol.get_embedded().getNotices().get(0).get_links()
                    .getSelf().getHref();
            return ResponseEntity.ok("Found in the " + organization + " database " + name + ". To see the photo visit: " +
                    thumbnail);
        } else {
            return ResponseEntity.ok("Found in the " + organization + " database " + name + ". Photo not available.");
        }
    }

    public static ResponseEntity<String> getStringResponseEntityFbi(String name, FBIWantedList apiResponseFbi, String organization) {
        if (apiResponseFbi.getItems().get(0).getImages().get(0).getOriginal() != null) {
            String thumbnail = apiResponseFbi.getItems().get(0).getImages().get(0).getOriginal();
            return ResponseEntity.ok("Found in the " + organization + " database " + name + ". To see the photo visit: " +
                    thumbnail);
        } else {
            return ResponseEntity.ok("Found in the " + organization + " database " + name + ". Photo not available.");
        }
    }

    public static ResponseEntity<String> getStringResponseEntityDb(WantedPeopleEntity wantedPeopleEntity) {
        return ResponseEntity.ok("Found in the wanted database! Wanted by " +
                wantedPeopleEntity.getGovernmetnalOrganization() + "\nDate of birth: " +
                wantedPeopleEntity.getDateOfBirth() + "\nTo see the photo visit: " +
                wantedPeopleEntity.getThumbnail());
    }
}
