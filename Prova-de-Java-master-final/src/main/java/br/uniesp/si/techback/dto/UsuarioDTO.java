package br.uniesp.si.techback.dto;

import br.uniesp.si.techback.validator.CpfCnpj;
import br.uniesp.si.techback.validator.SenhaForte;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome completo é obrigatório")
    @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres")
    private String nomeCompleto;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @SenhaForte
    private String senha;

    @CpfCnpj
    private String cpfCnpj;

    @NotBlank(message = "O perfil é obrigatório")
    private String perfil;
}
