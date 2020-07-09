package com.github.eladiojunior.ifood.cadastro.infra;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintViolationResponse {
    private final List<ConstraintViolationImpl> violations = new ArrayList<ConstraintViolationImpl>();

    private ConstraintViolationResponse(ConstraintViolationException exception) {
        exception.getConstraintViolations().forEach(violation -> violations.add(ConstraintViolationImpl.of(violation)));
    }
    public static ConstraintViolationResponse of(ConstraintViolationException exception) {
        return new ConstraintViolationResponse(exception);
    }
    public List<ConstraintViolationImpl> getViolations() {
        return violations;
    }
}
