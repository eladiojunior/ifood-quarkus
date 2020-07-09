package com.github.eladiojunior.ifood.cadastro.dtos;

import com.github.eladiojunior.ifood.cadastro.entites.Restaurante;
import com.github.eladiojunior.ifood.cadastro.infra.IBaseDTO;
import com.github.eladiojunior.ifood.cadastro.infra.IValidDTO;
import com.github.eladiojunior.ifood.cadastro.infra.UtilValidacao;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@IValidDTO
public class AdicionarRestauranteDTO implements IBaseDTO {

    @NotEmpty(message = "Informe o código do proprietário.")
    public String proprietario;

    @NotEmpty(message = "Informe um CNPJ válido.")
    @Pattern(regexp = "[0-9]{14}", message = "Informe um CNPJ válido de 14 dígitos, somente números.")
    public String cnpj;

    @NotEmpty(message = "Informe o nome do restaurante.")
    @Size(min = 3, max = 50, message = "Informe o nome do restaurante com no mímino 3 e no máximo 50 caracteres.")
    public String nome;

    public LocalizacaoDTO localizacao;

    @Override
    public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        //Verificar se o CNPJ é válido conforme regras de digito verificador.
        if (!UtilValidacao.of().isCnpjValido(cnpj)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ inválido.")
                    .addPropertyNode("cnpj")
                    .addConstraintViolation();
            return false;
        }

        //Verificar se o CNPJ já existe na base
        if (Restaurante.find("cnpj", cnpj).count() > 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ já registrado.")
                    .addPropertyNode("cnpj")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}