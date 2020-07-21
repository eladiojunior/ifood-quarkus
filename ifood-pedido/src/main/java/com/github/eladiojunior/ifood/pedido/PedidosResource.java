package com.github.eladiojunior.ifood.pedido;

import com.github.eladiojunior.ifood.pedido.dtos.LocalizacaoDTO;
import com.github.eladiojunior.ifood.pedido.entites.Localizacao;
import com.github.eladiojunior.ifood.pedido.entites.Pedido;
import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.bson.types.ObjectId;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidosResource {
    private static final String CLIENTE = "eladiojunior";

    @Inject
    private Vertx vertx;

    @Inject
    private EventBus eventBus;

    @GET
    public List<Pedido> listarPedidos() {
        return Pedido.listAll();
    }

    void startup(@Observes Router router) {
        router.route("/localizacoes*").handler(localizacaoHandler());
    }

    private SockJSHandler localizacaoHandler() {
        SockJSHandler handler = SockJSHandler.create(vertx);
        PermittedOptions permitted = new PermittedOptions();
        permitted.setAddress("novaLocalizacao");
        BridgeOptions bridgerOptions = new BridgeOptions().addOutboundPermitted(permitted);
        handler.bridge(bridgerOptions);
        return handler;
    }

    @POST
    @Path("{idPedido}/localicacao")
    public Pedido atualizarLocalicalizacao(@PathParam("idPedido") String idPedido, LocalizacaoDTO localizacao) {
        Pedido pedido = Pedido.findById(new ObjectId(idPedido));
        if (pedido == null) {
            throw new NotFoundException("Pedido [" + idPedido + "] não encontrado.");
        }
        Localizacao localizacaoAtual = pedido.getLocalizacaoEntregador();
        if (localizacaoAtual == null) {
            localizacaoAtual = new Localizacao();
        }
        localizacaoAtual.setLongitude(localizacao.getLongitude());
        localizacaoAtual.setLatitude(localizacao.getLatitude());
        pedido.setLocalizacaoEntregador(localizacaoAtual);
        pedido.persistOrUpdate();

        String jsonLocalizacao = JsonbBuilder.create().toJson(localizacaoAtual);
        eventBus.sendAndForget("novaLocalizacao", jsonLocalizacao);

        return pedido;
    }
}