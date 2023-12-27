package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumerApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumerApi api = new ConsumerApi();
		var json  = api.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=993cfc8d");
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados =	conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);


	}
}
