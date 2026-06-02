package br.uniesp.si.techback.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class FilmeDTOValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveRecusarFaixasInvalidas() {
        FilmeDTO dto = FilmeDTO.builder()
                .titulo("Filme")
                .sinopse("Sinopse")
                .dataLancamento(LocalDate.now())
                .ano(1800)
                .genero("Drama")
                .duracaoMinutos(1000)
                .relevancia(new BigDecimal("10.01"))
                .classificacaoIndicativa("Livre")
                .build();

        assertThat(validator.validate(dto))
                .extracting(violacao -> violacao.getPropertyPath().toString())
                .contains("ano", "duracaoMinutos", "relevancia");
    }
}
