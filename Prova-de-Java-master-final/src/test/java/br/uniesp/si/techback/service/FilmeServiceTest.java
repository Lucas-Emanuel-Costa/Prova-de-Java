package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.mapper.FilmeMapper;
import br.uniesp.si.techback.model.Filme;
import br.uniesp.si.techback.repository.FilmeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FilmeService")
class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private FilmeMapper filmeMapper;

    @InjectMocks
    private FilmeService filmeService;

    private Filme filme;
    private FilmeDTO filmeDTO;

    @BeforeEach
    void setUp() {
        filme = Filme.builder()
                .id(1L)
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(120)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        filmeDTO = FilmeDTO.builder()
                .id(1L)
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
    @DisplayName("Deve listar filmes com paginação")
    void deveListarFilmesComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("titulo").ascending());
        Page<Filme> paginaFilmes = new PageImpl<>(List.of(filme), pageable, 1);

        when(filmeRepository.findAll(pageable)).thenReturn(paginaFilmes);
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        Page<FilmeDTO> resultado = filmeService.listar(pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Filme de Teste");
        assertThat(resultado.getTotalElements()).isEqualTo(1);

        verify(filmeRepository).findAll(pageable);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve listar filmes ordenados por título")
    void deveListarFilmesOrdenadosPorTitulo() {
        when(filmeRepository.listarFilmesOrdenados()).thenReturn(List.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.listarOrdenado();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme de Teste");

        verify(filmeRepository).listarFilmesOrdenados();
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve buscar filme por ID quando existir")
    void deveBuscarFilmePorIdQuandoExistir() {
        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        FilmeDTO resultado = filmeService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(filmeDTO.getId());
        assertThat(resultado.getTitulo()).isEqualTo(filmeDTO.getTitulo());

        verify(filmeRepository).findById(1L);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve lançar CustomBeanException quando buscar filme por ID inexistente")
    void deveLancarExcecaoQuandoBuscarFilmePorIdInexistente() {
        when(filmeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmeService.buscarPorId(999L))
                .isInstanceOf(CustomBeanException.class)
                .hasMessage("Filme não encontrado com o ID: 999");

        verify(filmeRepository).findById(999L);
        verifyNoInteractions(filmeMapper);
    }

    @Test
    @DisplayName("Deve salvar um novo filme")
    void deveSalvarNovoFilme() {
        FilmeDTO filmeDTOSemId = FilmeDTO.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(120)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        Filme filmeSemId = Filme.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(120)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        when(filmeMapper.toEntity(filmeDTOSemId)).thenReturn(filmeSemId);
        when(filmeRepository.save(filmeSemId)).thenReturn(filme);
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        FilmeDTO resultado = filmeService.salvar(filmeDTOSemId);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(filmeDTO.getId());
        assertThat(resultado.getTitulo()).isEqualTo(filmeDTO.getTitulo());

        verify(filmeMapper).toEntity(filmeDTOSemId);
        verify(filmeRepository).save(filmeSemId);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve atualizar um filme existente")
    void deveAtualizarFilmeExistente() {
        FilmeDTO filmeDTOAtualizado = FilmeDTO.builder()
                .id(1L)
                .titulo("Filme Atualizado")
                .sinopse("Sinopse atualizada")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Drama")
                .duracaoMinutos(150)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("16 anos")
                .build();

        Filme filmeSalvo = Filme.builder()
                .id(1L)
                .titulo("Filme Atualizado")
                .sinopse("Sinopse atualizada")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Drama")
                .duracaoMinutos(150)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("16 anos")
                .build();

        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        doAnswer(invocation -> {
            FilmeDTO dto = invocation.getArgument(0);
            Filme entity = invocation.getArgument(1);
            entity.setTitulo(dto.getTitulo());
            entity.setSinopse(dto.getSinopse());
            entity.setDataLancamento(dto.getDataLancamento());
            entity.setGenero(dto.getGenero());
            entity.setDuracaoMinutos(dto.getDuracaoMinutos());
            entity.setClassificacaoIndicativa(dto.getClassificacaoIndicativa());
            return null;
        }).when(filmeMapper).atualizarEntidade(filmeDTOAtualizado, filme);

        when(filmeRepository.save(filme)).thenReturn(filmeSalvo);
        when(filmeMapper.toDTO(filmeSalvo)).thenReturn(filmeDTOAtualizado);

        FilmeDTO resultado = filmeService.atualizar(1L, filmeDTOAtualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Filme Atualizado");
        assertThat(resultado.getGenero()).isEqualTo("Drama");

        verify(filmeRepository).findById(1L);
        verify(filmeMapper).atualizarEntidade(filmeDTOAtualizado, filme);
        verify(filmeRepository).save(filme);
        verify(filmeMapper).toDTO(filmeSalvo);
    }

    @Test
    @DisplayName("Deve lançar CustomBeanException quando tentar atualizar filme inexistente")
    void deveLancarExcecaoQuandoTentarAtualizarFilmeInexistente() {
        when(filmeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmeService.atualizar(999L, filmeDTO))
                .isInstanceOf(CustomBeanException.class)
                .hasMessage("Falha ao atualizar: filme não encontrado com o ID: 999");

        verify(filmeRepository).findById(999L);
        verify(filmeMapper, never()).atualizarEntidade(any(), any());
    }

    @Test
    @DisplayName("Deve excluir um filme existente")
    void deveExcluirFilmeExistente() {
        when(filmeRepository.existsById(1L)).thenReturn(true);

        filmeService.excluir(1L);

        verify(filmeRepository).existsById(1L);
        verify(filmeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar CustomBeanException quando tentar excluir filme inexistente")
    void deveLancarExcecaoQuandoTentarExcluirFilmeInexistente() {
        when(filmeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> filmeService.excluir(999L))
                .isInstanceOf(CustomBeanException.class)
                .hasMessage("Falha ao excluir: filme não encontrado com o ID: 999");

        verify(filmeRepository).existsById(999L);
        verify(filmeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar filmes por gênero")
    void deveBuscarFilmesPorGenero() {
        when(filmeRepository.buscarPorGenero("Ação")).thenReturn(List.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.buscarPorGenero("Ação");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getGenero()).isEqualTo("Ação");

        verify(filmeRepository).buscarPorGenero("Ação");
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve buscar filmes por termo")
    void deveBuscarFilmesPorTermo() {
        when(filmeRepository.buscarPorTituloOuSinopse("Teste")).thenReturn(List.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.buscarPorTermo("Teste");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme de Teste");

        verify(filmeRepository).buscarPorTituloOuSinopse("Teste");
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve buscar filmes por duração mínima")
    void deveBuscarFilmesPorDuracaoMinima() {
        when(filmeRepository.buscarPorDuracaoMinima(100)).thenReturn(List.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.buscarPorDuracaoMinima(100);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDuracaoMinutos()).isEqualTo(120);

        verify(filmeRepository).buscarPorDuracaoMinima(100);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve listar filmes mais recentes")
    void deveListarFilmesMaisRecentes() {
        when(filmeRepository.listarMaisRecentes(any(Pageable.class))).thenReturn(List.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.listarMaisRecentes(5);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Filme de Teste");

        verify(filmeRepository).listarMaisRecentes(any(Pageable.class));
        verify(filmeMapper).toDTO(filme);
    }
}