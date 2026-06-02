package br.uniesp.si.techback.dto;

import br.uniesp.si.techback.validator.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
    private String email;

    @CpfCnpj
    private String cpfCnpj;

    @NotBlank(message = "O cargo é obrigatório")
    @Size(max = 80, message = "O cargo deve ter no máximo 80 caracteres")
    private String cargo;

    @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres")
    private String cep;

    private String logradouro;

    private String bairro;

    private String localidade;

    private String uf;
}
