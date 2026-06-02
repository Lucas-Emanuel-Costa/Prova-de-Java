package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.service.FilmeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/filmes")
@RequiredArgsConstructor
@Slf4j
public class FilmeController {

    private final FilmeService filmeService;

    @GetMapping
    public ResponseEntity<Page<FilmeDTO>> listar(Pageable pageable) {
        log.info("Recebida requisição para listar filmes com paginação");
        return ResponseEntity.ok(filmeService.listar(pageable));
    }

    @GetMapping("/ordenado")
    public ResponseEntity<List<FilmeDTO>> listarOrdenado() {
        log.info("Recebida requisição para listar filmes ordenados por título");
        return ResponseEntity.ok(filmeService.listarOrdenado());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmeDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar filme por ID: {}", id);
        return ResponseEntity.ok(filmeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FilmeDTO> criar(@Valid @RequestBody FilmeDTO filmeDTO) {
        log.info("Recebida requisição para criar novo filme: {}", filmeDTO.getTitulo());

        FilmeDTO filmeSalvo = filmeService.salvar(filmeDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(filmeSalvo.getId())
                .toUri();

        log.info("Filme criado com sucesso. ID: {}", filmeSalvo.getId());

        return ResponseEntity.created(location).body(filmeSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmeDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilmeDTO filmeDTO
    ) {
        log.info("Recebida requisição para atualizar filme ID: {}", id);
        return ResponseEntity.ok(filmeService.atualizar(id, filmeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Recebida requisição para excluir filme ID: {}", id);
        filmeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<FilmeDTO>> buscarPorGenero(@PathVariable String genero) {
        log.info("Recebida requisição para buscar filmes por gênero: {}", genero);
        return ResponseEntity.ok(filmeService.buscarPorGenero(genero));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<FilmeDTO>> buscarPorTermo(@RequestParam String termo) {
        log.info("Recebida requisição para buscar filmes pelo termo: {}", termo);
        return ResponseEntity.ok(filmeService.buscarPorTermo(termo));
    }

    @GetMapping("/duracao-minima")
    public ResponseEntity<List<FilmeDTO>> buscarPorDuracaoMinima(@RequestParam Integer duracao) {
        log.info("Recebida requisição para buscar filmes com duração mínima: {}", duracao);
        return ResponseEntity.ok(filmeService.buscarPorDuracaoMinima(duracao));
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<FilmeDTO>> listarMaisRecentes(
            @RequestParam(defaultValue = "5") int limit
    ) {
        log.info("Recebida requisição para listar os {} filmes mais recentes", limit);
        return ResponseEntity.ok(filmeService.listarMaisRecentes(limit));
    }
}
