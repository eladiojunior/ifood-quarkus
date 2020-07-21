package com.github.eladiojunior.ifood.pedido.dtos;

import java.util.List;

public class PedidoClienteDTO {
    private String cliente;
    private RestauranteDTO restaurante;
    private List<PratoDTO> pratos;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public RestauranteDTO getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(RestauranteDTO restaurante) {
        this.restaurante = restaurante;
    }

    public List<PratoDTO> getPratos() {
        return pratos;
    }

    public void setPratos(List<PratoDTO> pratos) {
        this.pratos = pratos;
    }

}
