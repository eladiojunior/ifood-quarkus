package com.github.eladiojunior.ifood.marketplace.dtos;

import io.vertx.mutiny.sqlclient.Row;

public class RestauranteDTO {
    private Long id;
    private String nome;
    private LocalizacaoDTO localizacao;

    public static RestauranteDTO from(Row row) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId(row.getLong("ID_RSTRE"));
        dto.setNome(row.getString("NM_RSTRE"));
        LocalizacaoDTO localDTO = new LocalizacaoDTO();
        localDTO.setLatitude(row.getDouble("CD_LTTDE"));
        localDTO.setLongitude(row.getDouble("CD_LNGTE"));
        dto.setLocalizacao(localDTO);
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

    public LocalizacaoDTO getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoDTO localizacao) {
        this.localizacao = localizacao;
    }

}
