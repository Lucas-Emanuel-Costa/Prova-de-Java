package br.uniesp.si.techback.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioDTOValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveAceitarUsuarioValido() {
        UsuarioDTO dto = UsuarioDTO.builder()
                .nomeCompleto("Lucas Emanuel De Oliveira Costa")
                .email("lucas@example.com")
                .senha("Senha123")
                .cpfCnpj("529.982.247-25")
                .perfil("USER")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void deveRecusarEmailSenhaECpfInvalidos() {
        UsuarioDTO dto = UsuarioDTO.builder()
                .nomeCompleto("Lucas Emanuel De Oliveira Costa")
                .email("email-invalido")
                .senha("fraca")
                .cpfCnpj("529.982.247-24")
                .perfil("USER")
                .build();

        assertThat(validator.validate(dto))
                .extracting(violacao -> violacao.getPropertyPath().toString())
                .contains("email", "senha", "cpfCnpj");
    }
}
