package br.com.fiap.findwanted.controller;

import br.com.fiap.findwanted.model.WantedPeopleEntity;
import br.com.fiap.findwanted.model.fbi.FBIWantedList;
import br.com.fiap.findwanted.model.interpol.InterpolWantedList;
import br.com.fiap.findwanted.repository.WantedRepository;
import br.com.fiap.findwanted.service.WantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static br.com.fiap.findwanted.service.WantedService.*;

@RestController
public class Controller {

    @Autowired
    private WantedRepository repository;

    @Autowired
    private WantedService wantedService;

    @GetMapping("iswanted")
    public ResponseEntity<String> getWantedData(@RequestParam("name") String name) {
        String encodedName = name.replace(" ", "%20").toLowerCase();

        InterpolWantedList apiResponseInterpol = wantedService.fetchInterpolData(encodedName);

        FBIWantedList apiResponseFbi = wantedService.fetchFbiData(encodedName);

        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = repository.findByName(name.toLowerCase());


        if (!wantedPeopleEntityOptional.isEmpty()) {
            WantedPeopleEntity wantedPeopleEntity = wantedPeopleEntityOptional.get();
            return getStringResponseEntityDb(wantedPeopleEntity);

        } else if (apiResponseInterpol.getTotal() > 0) {
            wantedService.createWantedEntityFromInterpol(apiResponseInterpol);
            return getStringResponseEntityInterpol(name, apiResponseInterpol, "Interpol");

        } else if (wantedService.isInFbiList(apiResponseFbi, name.toLowerCase())) {
            wantedService.createWantedEntityFromFbi(apiResponseFbi);
            return getStringResponseEntityFbi(name, apiResponseFbi, "FBI");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
