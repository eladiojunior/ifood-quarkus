package com.github.eladiojunior.ifood.marketplace.infra;

import com.github.eladiojunior.ifood.marketplace.daos.RestauranteDAO;
import com.github.eladiojunior.ifood.marketplace.entites.Restaurante;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class RestauranteRegistradoFila {

    @Inject
    PgPool client;

    @Incoming("restaurantes")
    public void receberRestauranteFila(String jsonRestaurante) {
        Jsonb create = JsonbBuilder.create();
        Restaurante restaurante = create.fromJson(jsonRestaurante, Restaurante.class);
        System.out.println("-------------------------------------------------------------");
        System.out.println("JSON: " + jsonRestaurante);
        RestauranteDAO.of(client).persist(restaurante);
    }
}