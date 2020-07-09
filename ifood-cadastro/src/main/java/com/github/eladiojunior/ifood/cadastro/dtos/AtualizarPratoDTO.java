package com.github.eladiojunior.ifood.cadastro.dtos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class AtualizarPratoDTO {

    @NotEmpty(message = "Informe o nome do prato.")
    public String nome;

    @NotEmpty(message = "Informe a descrição do prato.")
    @Size(min = 50, max = 2000, message = "Informe uma descrição do prato com no mímino 50 e no máximo 2.000 caracteres.")
    public String descricao;

    @Digits(integer = 18, fraction = 2, message = "Informe um valor do prato válido, maior que zero.")
    public BigDecimal preco;
}
