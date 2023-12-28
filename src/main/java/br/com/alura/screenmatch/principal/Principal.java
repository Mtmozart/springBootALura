package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumerApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        System.out.println("----------------------");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                //gera uma lista editável
                .collect(Collectors.toList());
        //gera uma lista imutável
        //.toList();
//        System.out.println("----------------");
//        System.out.println("\nTop 10 epíosdios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primeiro filtro(N/A): " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e-> System.out.println("Ordenação: " + e))
//                .limit(10)
//                .peek(e-> System.out.println("Limite: " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Mapeamento: " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Deseja procurar algum episódio específico ? ");
//        var trechoTitulo = sc.nextLine();
//        var episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isEmpty()) {
//            System.out.println("Desculpe, o episódio não foi encontrato");
//        } else {
//            System.out.println("Episodio encontrado");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        }
//        System.out.println("Deseja ver os episódios a partir de que ano ?");
//        var ano = sc.nextInt();
//        sc.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "temporada: " + e.getTemporada() + ", episodio: " + e.getTitulo() + " Data de lançamento: " + e.getDataLancamento().format(dtf) + "."
//                ));
//

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.printf("Media: %.2f\n ", est.getAverage());
        System.out.println("Menor nota: " + est.getMin());
        System.out.println("Maior nota: " + est.getMax());
        System.out.println("Total de epsódios utilizados para média: " + est.getSum());
    }


}

