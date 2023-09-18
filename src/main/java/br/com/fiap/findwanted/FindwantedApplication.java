package br.com.fiap.findwanted;

import br.com.fiap.findwanted.service.ConsumeAPI;
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
		var jsonFbi = consumeAPI.getData("https://api.fbi.gov/wanted/v1/list");
		System.out.println(jsonFbi);
		var jsonInterpol = consumeAPI.getData("https://ws-public.interpol.int/notices/v1/red");
		System.out.println(jsonInterpol);

	}
}
