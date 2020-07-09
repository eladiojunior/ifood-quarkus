package com.github.eladiojunior.ifood.cadastro.dtos;

import javax.validation.constraints.Size;

public class AtualizarRestauranteDTO {

    public String proprietario;

    @Size(min = 3, max = 50, message = "Informe o nome do restaurante com no mímino 3 e no máximo 50 caracteres.")
    public String nome;

    public LocalizacaoDTO localizacao;

}
