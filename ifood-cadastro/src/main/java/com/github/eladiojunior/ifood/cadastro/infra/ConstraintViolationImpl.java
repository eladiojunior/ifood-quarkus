package com.github.eladiojunior.ifood.cadastro.infra;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstraintViolationImpl implements Serializable {
    private static final long serialVersionUID = 1l;

    @Schema(description = "Local do atributo, ex.: restaurante.cnpj")
    private String atributo;

    @Schema(description = "Mensagem de erro associado ao local do atributo", required = true)
    private String mensagem;

    private ConstraintViolationImpl(ConstraintViolation<?> violation) {
        this.mensagem = violation.getMessage();
        this.atributo = Stream.of(violation.getPropertyPath().toString().split("\\.")).skip(2).collect(Collectors.joining("."));
    }
    private ConstraintViolationImpl(String atributo, String mensagem) {
        this.atributo = atributo;
        this.mensagem = mensagem;
    }

    public static ConstraintViolationImpl of(ConstraintViolation<?> violation) {
        return new ConstraintViolationImpl(violation);
    }

    public static ConstraintViolationImpl of(String violation) {
        return new ConstraintViolationImpl(null, violation);
    }

    public String getAtributo() {
        return atributo;
    }

    public String getMensagem() {
        return mensagem;
    }

}