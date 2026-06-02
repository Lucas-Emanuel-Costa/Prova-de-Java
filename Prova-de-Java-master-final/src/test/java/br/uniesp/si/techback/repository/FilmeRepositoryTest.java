package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Filme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Testes do FilmeRepository")
class FilmeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilmeRepository filmeRepository;

    private Filme filmeTeste;

    @BeforeEach
    void setUp() {
        filmeTeste = Filme.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(120)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();
    }

    @Test
    @DisplayName("Deve salvar um filme com sucesso")
    void deveSalvarFilme() {
        Filme filmeSalvo = filmeRepository.save(filmeTeste);

        assertThat(filmeSalvo).isNotNull();
        assertThat(filmeSalvo.getId()).isNotNull();
        assertThat(filmeSalvo.getTitulo()).isEqualTo(filmeTeste.getTitulo());
        assertThat(filmeSalvo.getSinopse()).isEqualTo(filmeTeste.getSinopse());
        assertThat(filmeSalvo.getDataLancamento()).isEqualTo(filmeTeste.getDataLancamento());
        assertThat(filmeSalvo.getGenero()).isEqualTo(filmeTeste.getGenero());
        assertThat(filmeSalvo.getDuracaoMinutos()).isEqualTo(filmeTeste.getDuracaoMinutos());
        assertThat(filmeSalvo.getClassificacaoIndicativa()).isEqualTo(filmeTeste.getClassificacaoIndicativa());
    }

    @Test
    @DisplayName("Deve encontrar filme por ID quando existir")
    void deveEncontrarFilmePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        Optional<Filme> filmeEncontrado = filmeRepository.findById(filmeSalvo.getId());

        assertThat(filmeEncontrado).isPresent();
        assertThat(filmeEncontrado.get().getId()).isEqualTo(filmeSalvo.getId());
        assertThat(filmeEncontrado.get().getTitulo()).isEqualTo(filmeTeste.getTitulo());
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar por ID inexistente")
    void deveRetornarVazioQuandoBuscarPorIdInexistente() {
        Optional<Filme> filmeEncontrado = filmeRepository.findById(999L);

        assertThat(filmeEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    void deveListarTodosOsFilmes() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filme2 = Filme.builder()
                .titulo("Filme de Teste 2")
                .sinopse("Outra sinopse")
                .dataLancamento(LocalDate.of(2023, 2, 1))
                .ano(2023)
                .genero("Comédia")
                .duracaoMinutos(90)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("Livre")
                .build();

        entityManager.persistAndFlush(filme2);

        List<Filme> filmes = filmeRepository.findAll();

        assertThat(filmes).hasSize(2);
        assertThat(filmes).extracting(Filme::getTitulo)
                .containsExactlyInAnyOrder("Filme de Teste", "Filme de Teste 2");
    }

    @Test
    @DisplayName("Deve verificar se filme existe por ID")
    void deveVerificarSeFilmeExistePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        boolean existe = filmeRepository.existsById(filmeSalvo.getId());
        boolean naoExiste = filmeRepository.existsById(999L);

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    @DisplayName("Deve deletar filme por ID")
    void deveDeletarFilmePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        filmeRepository.deleteById(filmeSalvo.getId());

        Optional<Filme> filmeDeletado = filmeRepository.findById(filmeSalvo.getId());
        assertThat(filmeDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve contar total de filmes")
    void deveContarTotalDeFilmes() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filme2 = Filme.builder()
                .titulo("Filme de Teste 2")
                .sinopse("Outra sinopse")
                .dataLancamento(LocalDate.of(2023, 2, 1))
                .ano(2023)
                .genero("Comédia")
                .duracaoMinutos(90)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("Livre")
                .build();

        entityManager.persistAndFlush(filme2);

        long totalFilmes = filmeRepository.count();

        assertThat(totalFilmes).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve listar filmes ordenados por título usando JPQL")
    void deveListarFilmesOrdenadosPorTitulo() {
        Filme filmeB = Filme.builder()
                .titulo("Zorro")
                .sinopse("Sinopse Zorro")
                .dataLancamento(LocalDate.of(2020, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(100)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        Filme filmeA = Filme.builder()
                .titulo("Avatar")
                .sinopse("Sinopse Avatar")
                .dataLancamento(LocalDate.of(2022, 1, 1))
                .ano(2023)
                .genero("Ficção")
                .duracaoMinutos(160)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        entityManager.persist(filmeB);
        entityManager.persist(filmeA);
        entityManager.flush();

        List<Filme> resultado = filmeRepository.listarFilmesOrdenados();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Avatar");
        assertThat(resultado.get(1).getTitulo()).isEqualTo("Zorro");
    }

    @Test
    @DisplayName("Deve buscar filmes por gênero ignorando maiúsculas e minúsculas")
    void deveBuscarFilmesPorGenero() {
        entityManager.persistAndFlush(filmeTeste);

        List<Filme> resultado = filmeRepository.buscarPorGenero("ação");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme de Teste");
    }

    @Test
    @DisplayName("Deve buscar filmes por termo no título ou sinopse")
    void deveBuscarFilmesPorTituloOuSinopse() {
        entityManager.persistAndFlush(filmeTeste);

        List<Filme> resultadoPorTitulo = filmeRepository.buscarPorTituloOuSinopse("Teste");
        List<Filme> resultadoPorSinopse = filmeRepository.buscarPorTituloOuSinopse("sinopse");

        assertThat(resultadoPorTitulo).hasSize(1);
        assertThat(resultadoPorSinopse).hasSize(1);
        assertThat(resultadoPorTitulo.get(0).getTitulo()).isEqualTo("Filme de Teste");
    }

    @Test
    @DisplayName("Deve buscar filmes por duração mínima")
    void deveBuscarFilmesPorDuracaoMinima() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filmeCurto = Filme.builder()
                .titulo("Filme Curto")
                .sinopse("Sinopse curta")
                .dataLancamento(LocalDate.of(2021, 1, 1))
                .ano(2023)
                .genero("Drama")
                .duracaoMinutos(80)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("Livre")
                .build();

        entityManager.persistAndFlush(filmeCurto);

        List<Filme> resultado = filmeRepository.buscarPorDuracaoMinima(100);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDuracaoMinutos()).isGreaterThanOrEqualTo(100);
    }

    @Test
    @DisplayName("Deve listar filmes mais recentes com limite")
    void deveListarFilmesMaisRecentes() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filmeRecente = Filme.builder()
                .titulo("Filme Recente")
                .sinopse("Sinopse recente")
                .dataLancamento(LocalDate.of(2024, 1, 1))
                .ano(2023)
                .genero("Drama")
                .duracaoMinutos(130)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("14 anos")
                .build();

        entityManager.persistAndFlush(filmeRecente);

        List<Filme> resultado = filmeRepository.listarMaisRecentes(PageRequest.of(0, 1));

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme Recente");
    }

    @Test
    @DisplayName("Deve buscar filmes por gênero e título")
    void deveBuscarFilmesPorGeneroETitulo() {
        entityManager.persistAndFlush(filmeTeste);

        List<Filme> resultado = filmeRepository.buscarPorGeneroETitulo("ação", "filme de teste");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme de Teste");
    }
}
