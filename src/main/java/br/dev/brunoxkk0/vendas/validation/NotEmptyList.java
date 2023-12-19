package br.dev.brunoxkk0.vendas.validation;

import br.dev.brunoxkk0.vendas.validation.constraintvalidation.NotEmptyListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "A lista não pode ser vazia.";
}
