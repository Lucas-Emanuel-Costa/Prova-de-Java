package br.uniesp.si.techback.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CpfCnpjValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfCnpj {

    String message() default "CPF/CNPJ inválido. Informe 11 dígitos para CPF ou 14 dígitos para CNPJ.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}