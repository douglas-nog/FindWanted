package br.com.fiap.findwanted.controller;

import br.com.fiap.findwanted.entities.WantedPeopleEntity;
import br.com.fiap.findwanted.entities.fbi.FBIWantedList;
import br.com.fiap.findwanted.entities.interpol.InterpolWantedList;
import br.com.fiap.findwanted.repository.WantedRepository;
import br.com.fiap.findwanted.services.WantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static br.com.fiap.findwanted.services.WantedService.*;

@RestController
public class Controller {

    @Autowired
    private WantedRepository repository;

    @Autowired
    private WantedService wantedService;

    @GetMapping("iswanted")
    public ResponseEntity<String> getWantedData(@RequestParam("name") String name) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("O nome não pode ser nulo ou vazio.");
            }
            String encodedName = name.replace(" ", "%20").toLowerCase();

            Optional<WantedPeopleEntity> wantedPeopleEntityOptional = repository.findByName(name.toLowerCase());
            if (!wantedPeopleEntityOptional.isEmpty()) {
                WantedPeopleEntity wantedPeopleEntity = wantedPeopleEntityOptional.get();
                return getStringResponseEntityDb(wantedPeopleEntity);
            }

            InterpolWantedList apiResponseInterpol = wantedService.fetchInterpolData(encodedName);
            if (apiResponseInterpol.getTotal() > 0) {
                wantedService.createWantedEntityFromInterpol(apiResponseInterpol);
                return getStringResponseEntityInterpol(name, apiResponseInterpol, "Interpol");
            }

            FBIWantedList apiResponseFbi = wantedService.fetchFbiData(encodedName);
            if (wantedService.isInFbiList(apiResponseFbi, name.toLowerCase())) {
                wantedService.createWantedEntityFromFbi(apiResponseFbi);
                return getStringResponseEntityFbi(name, apiResponseFbi, "FBI");
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde." + e.getMessage());
        }
    }
}

