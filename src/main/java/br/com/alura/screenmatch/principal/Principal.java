package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumerApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    Scanner sc = new Scanner(System.in);
    private ConsumerApi consumo = new ConsumerApi();

    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibiMenu() {
        System.out.println("Digite o nome da séria para busca: ");
        var nomeSerie = sc.nextLine();
        var json = this.consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = this.conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        System.out.println("----------------------");
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            var json2 = this.consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json2, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

    }
}
