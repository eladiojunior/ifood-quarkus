package com.github.eladiojunior.ifood.pedido.infra;

import com.github.eladiojunior.ifood.pedido.dtos.LocalizacaoDTO;
import com.github.eladiojunior.ifood.pedido.dtos.PedidoClienteDTO;
import com.github.eladiojunior.ifood.pedido.dtos.PratoDTO;
import com.github.eladiojunior.ifood.pedido.dtos.RestauranteDTO;
import com.github.eladiojunior.ifood.pedido.entites.Localizacao;
import com.github.eladiojunior.ifood.pedido.entites.Pedido;
import com.github.eladiojunior.ifood.pedido.entites.Prato;
import com.github.eladiojunior.ifood.pedido.entites.Restaurante;
import com.github.eladiojunior.ifood.pedido.services.ElasticSearchService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PedidoClienteFila {
    private final String ENTREGADOR = "motoboy";

    @Inject
    ElasticSearchService elasticService;

    @Incoming("pedidos")
    public void receberPedidosCliente(Object object) {

        System.out.println("-----------------------------------------------------------------");
        System.out.println("[Pedido Recebido]: " + object.toString());
        System.out.println("-----------------------------------------------------------------");

        PedidoClienteDTO dto = JsonbBuilder.create().fromJson(object.toString(), PedidoClienteDTO.class);
        if (dto == null) {
            System.out.println("Pedido inválido.");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(dto.getCliente());
        List<Prato> pratos = new ArrayList<Prato>();
        dto.getPratos().forEach(prato -> pratos.add(form(prato)));
        pedido.setPratos(pratos);
        pedido.setRestaurante(form(dto.getRestaurante()));
        pedido.setEntregador(ENTREGADOR);

        String jsonPedido = JsonbBuilder.create().toJson(dto);
        System.out.println("----------------------------------------------------------------------");
        System.out.println("[JSON Enviado ElasticSearch]: " + jsonPedido);
        System.out.println("----------------------------------------------------------------------");
        elasticService.index("pedidos", jsonPedido);

        pedido.persist();

    }

    private Restaurante form(RestauranteDTO dto) {
        Restaurante entity = new Restaurante();
        entity.setNome(dto.getNome());
        entity.setLocalizacao(form(dto.getLocalizacao()));
        return entity;
    }

    private Localizacao form(LocalizacaoDTO dto) {
        Localizacao entity = new Localizacao();
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        return entity;
    }

    private Prato form(PratoDTO dto) {
        Prato entity = new Prato();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        return entity;
    }

}
