package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmeDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    private String titulo;

    @Size(max = 1000, message = "A sinopse deve ter no máximo 1000 caracteres")
    private String sinopse;

    @NotNull(message = "A data de lançamento é obrigatória")
    @PastOrPresent(message = "A data de lançamento não pode ser futura")
    private LocalDate dataLancamento;

    @NotNull(message = "O ano e obrigatorio")
    @Min(value = 1888, message = "O ano deve ser no minimo 1888")
    @Max(value = 2100, message = "O ano deve ser no maximo 2100")
    private Integer ano;

    @NotBlank(message = "O gênero é obrigatório")
    @Size(max = 50, message = "O gênero deve ter no máximo 50 caracteres")
    private String genero;

    @NotNull(message = "A duração é obrigatória")
    @Min(value = 1, message = "A duração deve ser de no mínimo 1 minuto")
    @Max(value = 999, message = "A duração deve ser de no máximo 999 minutos")
    private Integer duracaoMinutos;

    @NotNull(message = "A relevancia e obrigatoria")
    @DecimalMin(value = "0.00", message = "A relevancia deve ser no minimo 0.00")
    @DecimalMax(value = "10.00", message = "A relevancia deve ser no maximo 10.00")
    private BigDecimal relevancia;

    @NotBlank(message = "A classificação indicativa é obrigatória")
    @Size(max = 20, message = "A classificação indicativa deve ter no máximo 20 caracteres")
    private String classificacaoIndicativa;
}
