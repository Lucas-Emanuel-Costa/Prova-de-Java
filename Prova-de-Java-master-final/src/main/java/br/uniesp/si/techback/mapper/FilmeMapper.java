package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.model.Filme;
import org.springframework.stereotype.Component;

@Component
public class FilmeMapper {

    public Filme toEntity(FilmeDTO dto) {
        if (dto == null) {
            return null;
        }

        Filme filme = new Filme();
        filme.setId(dto.getId());
        filme.setTitulo(dto.getTitulo());
        filme.setSinopse(dto.getSinopse());
        filme.setDataLancamento(dto.getDataLancamento());
        filme.setAno(dto.getAno());
        filme.setGenero(dto.getGenero());
        filme.setDuracaoMinutos(dto.getDuracaoMinutos());
        filme.setRelevancia(dto.getRelevancia());
        filme.setClassificacaoIndicativa(dto.getClassificacaoIndicativa());

        return filme;
    }

    public FilmeDTO toDTO(Filme entity) {
        if (entity == null) {
            return null;
        }

        FilmeDTO dto = new FilmeDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setSinopse(entity.getSinopse());
        dto.setDataLancamento(entity.getDataLancamento());
        dto.setAno(entity.getAno());
        dto.setGenero(entity.getGenero());
        dto.setDuracaoMinutos(entity.getDuracaoMinutos());
        dto.setRelevancia(entity.getRelevancia());
        dto.setClassificacaoIndicativa(entity.getClassificacaoIndicativa());

        return dto;
    }

    public void atualizarEntidade(FilmeDTO dto, Filme entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setTitulo(dto.getTitulo());
        entity.setSinopse(dto.getSinopse());
        entity.setDataLancamento(dto.getDataLancamento());
        entity.setAno(dto.getAno());
        entity.setGenero(dto.getGenero());
        entity.setDuracaoMinutos(dto.getDuracaoMinutos());
        entity.setRelevancia(dto.getRelevancia());
        entity.setClassificacaoIndicativa(dto.getClassificacaoIndicativa());
    }
}
