package br.uniesp.si.techback.validator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SenhaForteValidatorTest {

    private final SenhaForteValidator validator = new SenhaForteValidator();

    @Test
    void deveAceitarSenhaForte() {
        assertThat(validator.isValid("Senha123", null)).isTrue();
    }

    @Test
    void deveRecusarSenhaFracaOuAusente() {
        assertThat(validator.isValid("senha", null)).isFalse();
        assertThat(validator.isValid("SENHA123", null)).isFalse();
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
