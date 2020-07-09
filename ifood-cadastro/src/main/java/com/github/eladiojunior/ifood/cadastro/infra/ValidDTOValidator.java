package com.github.eladiojunior.ifood.cadastro.infra;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDTOValidator implements ConstraintValidator<IValidDTO, IBaseDTO> {

    @Override
    public void initialize(IValidDTO constraintAnnotation) {
    }

    @Override
    public boolean isValid(IBaseDTO baseDTO, ConstraintValidatorContext constraintValidatorContext) {
        return baseDTO.isValid(constraintValidatorContext);
    }
}
