package com.github.eladiojunior.ifood.marketplace.dtos;

import io.vertx.mutiny.sqlclient.Row;

import java.math.BigDecimal;

public class PratoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    public static PratoDTO from(Row row) {
        PratoDTO dto = new PratoDTO();
        dto.setId(row.getLong(0));
        dto.setNome(row.getString(1));
        dto.setDescricao(row.getString(2));
        dto.setPreco(row.getBigDecimal(3));
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

    @Override
    public String toString() {
        return "PratoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                '}';
    }
}
