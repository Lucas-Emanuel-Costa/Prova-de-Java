package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.service.FilmeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmeController.class)
@ActiveProfiles("test")
@DisplayName("Testes do FilmeController")
class FilmeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmeService filmeService;

    @Autowired
    private ObjectMapper objectMapper;

    private FilmeDTO filmeDTO;
    private FilmeDTO filmeSalvoDTO;

    @BeforeEach
    void setUp() {
        filmeDTO = FilmeDTO.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .ano(2023)
                .genero("Ação")
                .duracaoMinutos(120)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("12 anos")
                .build();

        filmeSalvoDTO = FilmeDTO.builder()
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
    void deveListarFilmesComPaginacao() throws Exception {
        var pagina = new PageImpl<>(
                List.of(filmeSalvoDTO),
                PageRequest.of(0, 10),
                1
        );

        when(filmeService.listar(any())).thenReturn(pagina);

        mockMvc.perform(get("/filmes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "titulo,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Filme de Teste"))
                .andExpect(jsonPath("$.content[0].genero").value("Ação"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve listar filmes ordenados")
    void deveListarFilmesOrdenados() throws Exception {
        when(filmeService.listarOrdenado()).thenReturn(List.of(filmeSalvoDTO));

        mockMvc.perform(get("/filmes/ordenado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Filme de Teste"));
    }

    @Test
    @DisplayName("Deve buscar filme por ID quando existir")
    void deveBuscarFilmePorIdQuandoExistir() throws Exception {
        when(filmeService.buscarPorId(1L)).thenReturn(filmeSalvoDTO);

        mockMvc.perform(get("/filmes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Filme de Teste"))
                .andExpect(jsonPath("$.genero").value("Ação"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando buscar filme por ID inexistente")
    void deveRetornar400QuandoBuscarFilmePorIdInexistente() throws Exception {
        when(filmeService.buscarPorId(999L))
                .thenThrow(new CustomBeanException("Filme não encontrado com o ID: 999"));

        mockMvc.perform(get("/filmes/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Filme não encontrado com o ID: 999"));
    }

    @Test
    @DisplayName("Deve criar um novo filme")
    void deveCriarNovoFilme() throws Exception {
        when(filmeService.salvar(any(FilmeDTO.class))).thenReturn(filmeSalvoDTO);

        mockMvc.perform(post("/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmeDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Filme de Teste"))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando criar filme com dados inválidos")
    void deveRetornar400QuandoCriarFilmeComDadosInvalidos() throws Exception {
        FilmeDTO filmeInvalido = FilmeDTO.builder()
                .titulo("")
                .sinopse("Teste")
                .dataLancamento(LocalDate.now().plusDays(1))
                .ano(2023)
                .genero("")
                .duracaoMinutos(0)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("")
                .build();

        mockMvc.perform(post("/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmeInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists())
                .andExpect(jsonPath("$.campos.genero").exists())
                .andExpect(jsonPath("$.campos.duracaoMinutos").exists())
                .andExpect(jsonPath("$.campos.classificacaoIndicativa").exists());
    }

    @Test
    @DisplayName("Deve atualizar um filme existente")
    void deveAtualizarFilmeExistente() throws Exception {
        FilmeDTO filmeAtualizado = FilmeDTO.builder()
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

        when(filmeService.atualizar(eq(1L), any(FilmeDTO.class))).thenReturn(filmeAtualizado);

        mockMvc.perform(put("/filmes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmeAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Filme Atualizado"))
                .andExpect(jsonPath("$.genero").value("Drama"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando tentar atualizar filme inexistente")
    void deveRetornar400QuandoTentarAtualizarFilmeInexistente() throws Exception {
        when(filmeService.atualizar(eq(999L), any(FilmeDTO.class)))
                .thenThrow(new CustomBeanException("Falha ao atualizar: filme não encontrado com o ID: 999"));

        mockMvc.perform(put("/filmes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha ao atualizar: filme não encontrado com o ID: 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando atualizar filme com dados inválidos")
    void deveRetornar400QuandoAtualizarFilmeComDadosInvalidos() throws Exception {
        FilmeDTO filmeInvalido = FilmeDTO.builder()
                .titulo("")
                .sinopse("Teste")
                .dataLancamento(LocalDate.now().plusDays(1))
                .ano(2023)
                .genero("")
                .duracaoMinutos(0)
                .relevancia(new BigDecimal("8.50"))
                .classificacaoIndicativa("")
                .build();

        mockMvc.perform(put("/filmes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmeInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    @DisplayName("Deve excluir um filme existente")
    void deveExcluirFilmeExistente() throws Exception {
        mockMvc.perform(delete("/filmes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 400 quando tentar excluir filme inexistente")
    void deveRetornar400QuandoTentarExcluirFilmeInexistente() throws Exception {
        doThrow(new CustomBeanException("Falha ao excluir: filme não encontrado com o ID: 999"))
                .when(filmeService).excluir(999L);

        mockMvc.perform(delete("/filmes/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha ao excluir: filme não encontrado com o ID: 999"));
    }

    @Test
    @DisplayName("Deve buscar filmes por gênero")
    void deveBuscarFilmesPorGenero() throws Exception {
        when(filmeService.buscarPorGenero("Ação")).thenReturn(List.of(filmeSalvoDTO));

        mockMvc.perform(get("/filmes/genero/Ação"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genero").value("Ação"));
    }

    @Test
    @DisplayName("Deve buscar filmes por termo")
    void deveBuscarFilmesPorTermo() throws Exception {
        when(filmeService.buscarPorTermo("Teste")).thenReturn(List.of(filmeSalvoDTO));

        mockMvc.perform(get("/filmes/buscar")
                        .param("termo", "Teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Filme de Teste"));
    }

    @Test
    @DisplayName("Deve buscar filmes por duração mínima")
    void deveBuscarFilmesPorDuracaoMinima() throws Exception {
        when(filmeService.buscarPorDuracaoMinima(100)).thenReturn(List.of(filmeSalvoDTO));

        mockMvc.perform(get("/filmes/duracao-minima")
                        .param("duracao", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].duracaoMinutos").value(120));
    }

    @Test
    @DisplayName("Deve listar filmes mais recentes")
    void deveListarFilmesMaisRecentes() throws Exception {
        when(filmeService.listarMaisRecentes(5)).thenReturn(List.of(filmeSalvoDTO));

        mockMvc.perform(get("/filmes/recentes")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}