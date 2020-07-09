package com.github.eladiojunior.ifood.cadastro.infra;

import javax.validation.ConstraintValidatorContext;

public interface IBaseDTO {
    default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
