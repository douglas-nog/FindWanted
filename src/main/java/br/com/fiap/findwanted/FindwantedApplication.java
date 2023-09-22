package br.com.fiap.findwanted;

import br.com.fiap.findwanted.model.*;
import br.com.fiap.findwanted.controller.ConsumeAPI;
import br.com.fiap.findwanted.util.Converter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FindwantedApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FindwantedApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Converter converter = new Converter();
        var consumeAPI = new ConsumeAPI();
        var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=LEIVA%20RIVERA");

        ApiResponse apiResponse = converter.getData(jsonInterpol, ApiResponse.class);



    }
}

//        Query query = converter.getData(jsonInterpol, Query.class);
//        Embedded embedded = converter.getData(jsonInterpol, Embedded.class);
//        Notice notice = converter.getData(jsonInterpol, Notice.class);
//        Link link = converter.getData(jsonInterpol, Link.class);
//        Links links = converter.getData(jsonInterpol, Links.class);


//        List<Object> notices = new ArrayList<>();
//        notices = (List<Object>) apiResponse.get_embedded().get("notices");
//        Map<String, String> map = new HashMap<>();
//        map = (Map<String, String>) notices.get(0);
//        System.out.println(map.get("date_of_birth"));
//        System.out.println(map.get("entity_id"));
//        System.out.println(map.get("forename"));
//        System.out.println(map.get("name"));

//        var consumeAPI = new ConsumeAPI();
//        var jsonFbi = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list?title=RUJA%20IGNATOVA");
//        //System.out.println(jsonFbi);
//        //var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=TOMKIEL");
//        //System.out.println(jsonInterpol);
//
//        Converter converter = new Converter();
//        DataWanted data = converter.getData(jsonFbi, DataWanted.class);
//        System.out.println(data);




