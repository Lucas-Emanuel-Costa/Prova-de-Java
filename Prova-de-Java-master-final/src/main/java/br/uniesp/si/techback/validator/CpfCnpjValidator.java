package br.uniesp.si.techback.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context) {
        if (valor == null || valor.isBlank()) {
            return true;
        }

        String apenasNumeros = valor.replaceAll("\\D", "");

        return apenasNumeros.length() == 11
                ? cpfValido(apenasNumeros)
                : apenasNumeros.length() == 14 && cnpjValido(apenasNumeros);
    }

    private boolean cpfValido(String cpf) {
        if (digitosIguais(cpf)) {
            return false;
        }

        int primeiroDigito = calcularDigito(cpf.substring(0, 9), new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
        int segundoDigito = calcularDigito(cpf.substring(0, 9) + primeiroDigito, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});

        return cpf.equals(cpf.substring(0, 9) + primeiroDigito + segundoDigito);
    }

    private boolean cnpjValido(String cnpj) {
        if (digitosIguais(cnpj)) {
            return false;
        }

        int primeiroDigito = calcularDigito(cnpj.substring(0, 12), new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int segundoDigito = calcularDigito(cnpj.substring(0, 12) + primeiroDigito, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return cnpj.equals(cnpj.substring(0, 12) + primeiroDigito + segundoDigito);
    }

    private int calcularDigito(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private boolean digitosIguais(String valor) {
        return valor.chars().distinct().count() == 1;
    }
}
