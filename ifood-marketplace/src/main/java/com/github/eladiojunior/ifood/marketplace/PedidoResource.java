package com.github.eladiojunior.ifood.marketplace;

import com.github.eladiojunior.ifood.marketplace.daos.PedidoClienteDAO;
import com.github.eladiojunior.ifood.marketplace.daos.RestauranteDAO;
import com.github.eladiojunior.ifood.marketplace.dtos.PedidoClienteDTO;
import com.github.eladiojunior.ifood.marketplace.dtos.PratoDTO;
import com.github.eladiojunior.ifood.marketplace.dtos.RestauranteDTO;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/pedido")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "pedido")
public class PedidoResource {
    private static final String CLIENTE = "eladiojunior";

    @Inject
    private PgPool client;

    @Inject
    @Channel("pedidos")
    private Emitter<PedidoClienteDTO> emitterPedido;

    @GET
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(type = SchemaType.OBJECT, implementation = PedidoClienteDTO.class)))
    public Uni<PedidoClienteDTO> obterPedidoCliente() {
        PedidoClienteDTO pedido = new PedidoClienteDTO();
        pedido.setCliente(CLIENTE);
        pedido.setPratos(PedidoClienteDAO.of(client).listarPratosPedidoCliente(CLIENTE).collectItems().asList().await().indefinitely());
        return Uni.createFrom().item(pedido);
    }

    @POST
    @Path("/prato/{idPrato}")
    public Uni<Long> adicionarPratoPedidoCliente(@PathParam("idPrato") Long idPrato) {
        return PedidoClienteDAO.of(client).adicionarPratoPedidoCliente(CLIENTE, idPrato);
    }

    @POST
    @Path("/finalizar")
    public Uni<Boolean> finalizarPedidoCliente() {

        PedidoClienteDTO pedidoDTO = new PedidoClienteDTO();
        pedidoDTO.setCliente(CLIENTE);
        List<PratoDTO> listPratos = PedidoClienteDAO.of(client).listarPratosPedidoCliente(CLIENTE)
                .collectItems().asList().await().indefinitely();
        pedidoDTO.setPratos(listPratos);

        //Buscar restaurante.
        Uni<RestauranteDTO> restaurante = RestauranteDAO.of(client).obter(1L);
        if (restaurante != null)
            pedidoDTO.setRestaurante(restaurante.await().indefinitely());

        /**/
        Jsonb create = JsonbBuilder.create();
        String jsonPedidoCarrinho = create.toJson(pedidoDTO);
        System.out.println("----------------------------------------------------");
        System.out.println("[AMQ]: " + jsonPedidoCarrinho);
        /**/
        //Enviar pedido para fila de mensageria.
        emitterPedido.send(pedidoDTO);
        System.out.println("[Enviado]");
        System.out.println("----------------------------------------------------");

        //Remover o pedido do carrinho.
        return PedidoClienteDAO.of(client).finalizarPedidoCliente(CLIENTE);

    }


}