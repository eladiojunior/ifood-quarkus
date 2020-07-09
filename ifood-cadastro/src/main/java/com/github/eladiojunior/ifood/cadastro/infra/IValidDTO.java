package com.github.eladiojunior.ifood.cadastro.infra;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidDTOValidator.class })
@Documented
public @interface IValidDTO {

    String message() default "{com.github.eladiojunior.ifood.cadastro.infra.IValidDTO.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
