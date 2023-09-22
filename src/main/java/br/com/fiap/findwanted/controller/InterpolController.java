package br.com.fiap.findwanted.controller;

import br.com.fiap.findwanted.entity.WantedPeopleEntity;
import br.com.fiap.findwanted.model.ApiResponse;
import br.com.fiap.findwanted.repository.InterpolRepository;
import br.com.fiap.findwanted.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

@RestController
public class InterpolController {

    @Autowired
    private InterpolRepository repository;

    @GetMapping("iswanted")
    public ResponseEntity<String> getInterpolData(@RequestParam("name") String name) {
        Optional<WantedPeopleEntity> wantedPeopleEntityOptional = repository.findByName(name);

        if (!wantedPeopleEntityOptional.isEmpty()) {
            WantedPeopleEntity wantedPeopleEntity = wantedPeopleEntityOptional.get();
            return ResponseEntity.ok("found in the wanted database! Date of birth: " + wantedPeopleEntity.getDateOfBirth() +
                    " To see the photo visit: " + wantedPeopleEntity.getThumbnail());
        } else {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

            Converter converter = new Converter();
            var consumeAPI = new ConsumeAPI();
            var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=" + encodedName);

            ApiResponse apiResponse = converter.getData(jsonInterpol, ApiResponse.class);

            if (!jsonInterpol.isEmpty()) {

                WantedPeopleEntity wantedPeopleEntity = new WantedPeopleEntity();
                wantedPeopleEntity.setId(null);
                wantedPeopleEntity.setName(apiResponse.get_embedded().getNotices().get(0).getName().toLowerCase());
                wantedPeopleEntity.setForename(apiResponse.get_embedded().getNotices().get(0).getForename().toLowerCase());
                wantedPeopleEntity.setThumbnail(apiResponse.get_embedded().getNotices().get(0).get_links().getThumbnail().getHref());
                wantedPeopleEntity.setDateOfBirth(apiResponse.get_embedded().getNotices().get(0).getDate_of_birth());
                repository.save(wantedPeopleEntity);

                return ResponseEntity.ok("found in the Interpol database " + name + ". To see the photo visit: " +
                        apiResponse.get_embedded().getNotices().get(0).get_links().getThumbnail().getHref());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
