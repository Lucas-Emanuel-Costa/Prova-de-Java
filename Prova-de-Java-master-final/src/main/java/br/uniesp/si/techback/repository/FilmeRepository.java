package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Filme;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    @Query("""
           SELECT f FROM Filme f
           ORDER BY f.titulo ASC
           """)
    List<Filme> listarFilmesOrdenados();

    @Query("""
           SELECT f FROM Filme f
           WHERE LOWER(f.genero) = LOWER(:genero)
           ORDER BY f.titulo ASC
           """)
    List<Filme> buscarPorGenero(@Param("genero") String genero);

    @Query("""
           SELECT f FROM Filme f
           WHERE LOWER(f.titulo) LIKE LOWER(CONCAT('%', :termo, '%'))
              OR LOWER(f.sinopse) LIKE LOWER(CONCAT('%', :termo, '%'))
           ORDER BY f.titulo ASC
           """)
    List<Filme> buscarPorTituloOuSinopse(@Param("termo") String termo);

    @Query("""
           SELECT f FROM Filme f
           WHERE f.duracaoMinutos >= :duracao
           ORDER BY f.duracaoMinutos DESC
           """)
    List<Filme> buscarPorDuracaoMinima(@Param("duracao") Integer duracao);

    @Query("""
           SELECT f FROM Filme f
           ORDER BY f.dataLancamento DESC
           """)
    List<Filme> listarMaisRecentes(Pageable pageable);

    @Query("""
           SELECT f FROM Filme f
           WHERE LOWER(f.genero) = LOWER(:genero)
             AND LOWER(f.titulo) = LOWER(:titulo)
           """)
    List<Filme> buscarPorGeneroETitulo(
            @Param("genero") String genero,
            @Param("titulo") String titulo
    );
}
