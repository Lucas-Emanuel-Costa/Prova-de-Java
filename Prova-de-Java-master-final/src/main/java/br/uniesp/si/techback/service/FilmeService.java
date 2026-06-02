package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.mapper.FilmeMapper;
import br.uniesp.si.techback.model.Filme;
import br.uniesp.si.techback.repository.FilmeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final FilmeMapper filmeMapper;

    public Page<FilmeDTO> listar(Pageable pageable) {
        log.info("Buscando filmes com paginação: página={}, tamanho={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<FilmeDTO> filmes = filmeRepository.findAll(pageable)
                .map(filmeMapper::toDTO);

        log.debug("Total de filmes encontrados: {}", filmes.getTotalElements());

        return filmes;
    }

    public List<FilmeDTO> listarOrdenado() {
        log.info("Listando filmes ordenados por título");

        return filmeRepository.listarFilmesOrdenados()
                .stream()
                .map(filmeMapper::toDTO)
                .toList();
    }

    public FilmeDTO buscarPorId(Long id) {
        log.info("Buscando filme pelo ID: {}", id);

        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> {
                    String mensagem = String.format("Filme não encontrado com o ID: %d", id);
                    log.warn(mensagem);
                    return new CustomBeanException(mensagem);
                });

        log.debug("Filme encontrado: ID={}, Título={}", filme.getId(), filme.getTitulo());

        return filmeMapper.toDTO(filme);
    }

    @Transactional
    public FilmeDTO salvar(FilmeDTO filmeDTO) {
        log.info("Salvando novo filme: {}", filmeDTO.getTitulo());

        Filme filme = filmeMapper.toEntity(filmeDTO);
        filme.setId(null);

        Filme filmeSalvo = filmeRepository.save(filme);

        log.info("Filme salvo com sucesso. ID: {}, Título: {}",
                filmeSalvo.getId(), filmeSalvo.getTitulo());

        return filmeMapper.toDTO(filmeSalvo);
    }

    @Transactional
    public FilmeDTO atualizar(Long id, FilmeDTO filmeDTO) {
        log.info("Atualizando filme ID: {}", id);

        Filme filmeExistente = filmeRepository.findById(id)
                .orElseThrow(() -> {
                    String mensagem = String.format("Falha ao atualizar: filme não encontrado com o ID: %d", id);
                    log.warn(mensagem);
                    return new CustomBeanException(mensagem);
                });

        filmeMapper.atualizarEntidade(filmeDTO, filmeExistente);

        Filme filmeSalvo = filmeRepository.save(filmeExistente);

        log.info("Filme ID: {} atualizado com sucesso. Novo título: {}",
                id, filmeSalvo.getTitulo());

        return filmeMapper.toDTO(filmeSalvo);
    }

    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo filme ID: {}", id);

        if (!filmeRepository.existsById(id)) {
            String mensagem = String.format("Falha ao excluir: filme não encontrado com o ID: %d", id);
            log.warn(mensagem);
            throw new CustomBeanException(mensagem);
        }

        filmeRepository.deleteById(id);

        log.info("Filme ID: {} excluído com sucesso", id);
    }

    public List<FilmeDTO> buscarPorGenero(String genero) {
        log.info("Buscando filmes por gênero: {}", genero);

        return filmeRepository.buscarPorGenero(genero)
                .stream()
                .map(filmeMapper::toDTO)
                .toList();
    }

    public List<FilmeDTO> buscarPorTermo(String termo) {
        log.info("Buscando filmes pelo termo: {}", termo);

        return filmeRepository.buscarPorTituloOuSinopse(termo)
                .stream()
                .map(filmeMapper::toDTO)
                .toList();
    }

    public List<FilmeDTO> buscarPorDuracaoMinima(Integer duracao) {
        log.info("Buscando filmes com duração mínima de {} minutos", duracao);

        return filmeRepository.buscarPorDuracaoMinima(duracao)
                .stream()
                .map(filmeMapper::toDTO)
                .toList();
    }

    public List<FilmeDTO> listarMaisRecentes(int limit) {
        log.info("Listando os {} filmes mais recentes", limit);

        Pageable pageable = PageRequest.of(0, limit);

        return filmeRepository.listarMaisRecentes(pageable)
                .stream()
                .map(filmeMapper::toDTO)
                .toList();
    }
}