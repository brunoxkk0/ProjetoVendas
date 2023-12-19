package br.dev.brunoxkk0.vendas.validation.constraintvalidation;

import br.dev.brunoxkk0.vendas.validation.NotEmptyList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {

    @Override
    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
        return list != null && !list.isEmpty();
    }

}
