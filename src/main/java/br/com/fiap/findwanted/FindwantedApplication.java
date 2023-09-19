package br.com.fiap.findwanted;

import br.com.fiap.findwanted.model.DataWanted;
import br.com.fiap.findwanted.service.ConsumeAPI;
import br.com.fiap.findwanted.service.Converter;
import br.com.fiap.findwanted.service.EmbeddedData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		var consumeAPI = new ConsumeAPI();
		var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=LEIVA%20RIVERA");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonInterpol);

		if (rootNode.has("_embedded")) {
			JsonNode embeddedNode = rootNode.get("_embedded");
			EmbeddedData embeddedData = mapper.convertValue(embeddedNode, EmbeddedData.class);

			if (embeddedData != null && embeddedData.getNotices() != null && !embeddedData.getNotices().isEmpty()) {
				DataWanted data = embeddedData.getNotices().get(0);
				System.out.println(data);
			} else {
				System.out.println("Nenhum dado encontrado.");
			}
		} else {
			System.out.println("Campo '_embedded' não encontrado no JSON.");
		}
	}

//        var consumeAPI = new ConsumeAPI();
//        var jsonFbi = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list?title=RUJA%20IGNATOVA");
//        //System.out.println(jsonFbi);
//        //var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red?name=TOMKIEL");
//        //System.out.println(jsonInterpol);
//
//        Converter converter = new Converter();
//        DataWanted data = converter.getData(jsonFbi, DataWanted.class);
//        System.out.println(data);

    }


