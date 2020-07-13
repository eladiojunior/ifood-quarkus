package com.github.eladiojunior.ifood.marketplace.dtos;

import io.vertx.mutiny.sqlclient.Row;

public class LocalizacaoDTO {
    private Long id;
    private Double latitude;
    private Double longitude;

    public static LocalizacaoDTO from(Row row) {
        LocalizacaoDTO dto = new LocalizacaoDTO();
        dto.setId(row.getLong("ID_LCLZO"));
        dto.setLatitude(row.getDouble("CD_LTTDE"));
        dto.setLongitude(row.getDouble("CD_LNGTE"));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
