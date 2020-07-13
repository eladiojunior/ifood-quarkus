package com.github.eladiojunior.ifood.marketplace.dtos;

import com.github.eladiojunior.ifood.marketplace.entites.Restaurante;
import io.vertx.mutiny.sqlclient.Row;

import java.math.BigDecimal;

public class PratoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    public static PratoDTO from(Row row) {
        PratoDTO dto = new PratoDTO();
        dto.setId(row.getLong("ID_PRATO"));
        dto.setNome(row.getString("NM_PRATO"));
        dto.setDescricao(row.getString("DS_PRATO"));
        dto.setPreco(row.getBigDecimal("VL_PRATO"));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
