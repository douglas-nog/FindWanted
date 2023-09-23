package br.com.fiap.findwanted.controller;

import br.com.fiap.findwanted.entity.WantedPeopleEntity;
import br.com.fiap.findwanted.model.fbi.FBIWantedList;
import br.com.fiap.findwanted.model.interpol.InterpolWantedList;
import br.com.fiap.findwanted.repository.WantedRepository;
import br.com.fiap.findwanted.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    private WantedRepository repository;

    @GetMapping("iswanted")
    public ResponseEntity<String> getInterpolData(@RequestParam("name") String name) {
        String encodedName = name.replace(" ", "%20").toLowerCase();

        Converter converter = new Converter();
        var consumeAPI = new ConsumeAPI();
        var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=" + encodedName);
        var jsonFbi = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list?title=" + encodedName);
        InterpolWantedList apiResponseInterpol = converter.getData(jsonInterpol, InterpolWantedList.class);
        FBIWantedList apiResponseFbi = converter.getData(jsonFbi, FBIWantedList.class);
        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = repository.findByName(name.toLowerCase());

        List<String> fbiNames = apiResponseFbi.getItems().stream().map(item -> item.getTitle().toLowerCase()).toList();

        if (!wantedPeopleEntityOptional.isEmpty()) {
            WantedPeopleEntity wantedPeopleEntity = wantedPeopleEntityOptional.get();
            return ResponseEntity.ok("Found in the wanted database! Wanted by " + wantedPeopleEntity.getGovernmetnalOrganization() + "\nDate of birth: " + wantedPeopleEntity.getDateOfBirth() +
                    "\nTo see the photo visit: " + wantedPeopleEntity.getThumbnail());
        } else if (apiResponseInterpol.getTotal() > 0) {
            WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
            //wantedPeopleEntity.setId(null);
            wantedPeopleEntity.setName(apiResponseInterpol.get_embedded().getNotices().get(0).getName().toLowerCase());
            wantedPeopleEntity.setForename(apiResponseInterpol.get_embedded().getNotices().get(0).getForename().toLowerCase());
            wantedPeopleEntity.setThumbnail(apiResponseInterpol.get_embedded().getNotices().get(0).get_links().getThumbnail().getHref());
            wantedPeopleEntity.setDateOfBirth(apiResponseInterpol.get_embedded().getNotices().get(0).getDate_of_birth());
            wantedPeopleEntity.setGovernmetnalOrganization("Interpol");
            repository.save(wantedPeopleEntity);

            return ResponseEntity.ok("found in the Interpol database " + name + ". To see the photo visit: " +
                    apiResponseInterpol.get_embedded().getNotices().get(0).get_links().getThumbnail().getHref());
        } else if (fbiNames.contains(name.toLowerCase())) {

            WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
            //wantedPeopleEntity.setId(null);
            wantedPeopleEntity.setName(apiResponseFbi.getItems().get(0).getTitle().toLowerCase());
            wantedPeopleEntity.setForename(apiResponseFbi.getItems().get(0).getAliases().get(0).toLowerCase());
            wantedPeopleEntity.setThumbnail(apiResponseFbi.getItems().get(0).getImages().get(0).getOriginal());
            wantedPeopleEntity.setDateOfBirth(apiResponseFbi.getItems().get(0).getDates_of_birth_used().get(0));
            wantedPeopleEntity.setGovernmetnalOrganization("FBI");
            repository.save(wantedPeopleEntity);

            return ResponseEntity.ok("found in the FBI database " + name + ". To see the photo visit: " +
                    apiResponseFbi.getItems().get(0).getImages().get(0).getOriginal());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
