package com.github.eladiojunior.ifood.marketplace;

import com.github.eladiojunior.ifood.marketplace.daos.PratoDAO;
import com.github.eladiojunior.ifood.marketplace.dtos.PratoDTO;
import com.github.eladiojunior.ifood.marketplace.entites.Prato;
import com.github.eladiojunior.ifood.marketplace.entites.Restaurante;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @Inject
    PgPool client;

    @GET
    @Path("{idRestaurante}/pratos")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PratoDTO.class)))
    public Multi<PratoDTO> listarPratosRestaurante(@PathParam("idRestaurante") Long idRestaurante) {
        return PratoDAO.of(client).findAll(idRestaurante);
    }

}