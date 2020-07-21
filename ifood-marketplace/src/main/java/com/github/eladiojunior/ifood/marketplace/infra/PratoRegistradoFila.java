package com.github.eladiojunior.ifood.marketplace.infra;

import com.github.eladiojunior.ifood.marketplace.daos.PratoDAO;
import com.github.eladiojunior.ifood.marketplace.entites.Prato;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class PratoRegistradoFila {

    @Inject
    private PgPool client;

    @Incoming("pratos")
    public void receberPratoFila(String jsonPrato) {
        Jsonb create = JsonbBuilder.create();
        Prato prato = create.fromJson(jsonPrato, Prato.class);
        PratoDAO.of(client).gravarPrato(prato);
    }
}