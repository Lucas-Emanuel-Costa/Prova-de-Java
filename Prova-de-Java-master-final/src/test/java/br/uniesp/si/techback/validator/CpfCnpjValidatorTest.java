package br.uniesp.si.techback.validator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfCnpjValidatorTest {

    private final CpfCnpjValidator validator = new CpfCnpjValidator();

    @Test
    void deveAceitarCpfECnpjValidos() {
        assertThat(validator.isValid("529.982.247-25", null)).isTrue();
        assertThat(validator.isValid("11.222.333/0001-81", null)).isTrue();
    }

    @Test
    void deveRecusarCpfECnpjComDigitosVerificadoresInvalidos() {
        assertThat(validator.isValid("529.982.247-24", null)).isFalse();
        assertThat(validator.isValid("11.222.333/0001-80", null)).isFalse();
        assertThat(validator.isValid("111.111.111-11", null)).isFalse();
    }

    @Test
    void devePermitirValorAusentePorSerCampoOpcional() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid("", null)).isTrue();
    }
}
