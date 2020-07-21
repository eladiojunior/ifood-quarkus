package com.github.eladiojunior.ifood.cadastro;

import com.github.eladiojunior.ifood.cadastro.dtos.*;
import com.github.eladiojunior.ifood.cadastro.entites.*;
import com.github.eladiojunior.ifood.cadastro.infra.ConstraintViolationResponse;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows =
@OAuthFlows(password = @OAuthFlow(tokenUrl = "http://192.168.0.18:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth")
public class RestauranteResource {

    @Inject
    IRestauranteMapper restauranteMapper;

    @Inject
    @Channel("restaurantes")
    Emitter<String> emitterRestaurantes;

    @Inject
    @Channel("pratos")
    Emitter<String> emitterPratos;

    @GET
    @APIResponse(responseCode = "200", description = "Lista de restaurante retornada com sucesso.")
    //Metrics - Para registro no Prometheus e apresentação no Grafana.
    @Counted(name = "Quantidade de buscas de restaurantes")
    @SimplyTimed(name = "Tempo simples de resposta da busca dos restaurantes")
    @Timed(name = "Tempo completo de resposta da busca dos restaurantes")
    public List<RestauranteDTO> listarRestaurantes() {
        Stream<Restaurante> restaurantes = Restaurante.streamAll();
        return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    @APIResponse(responseCode = "201", description = "Restaurante registrado com sucesso.")
    @APIResponse(responseCode = "400", description = "Erro de validação no envio das informações.", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public Response adicionarRestaurante(@Valid AdicionarRestauranteDTO dto) {

        Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();

        //Enviar para o AMQ.
        Jsonb create = JsonbBuilder.create();
        String restauranteJson = create.toJson(restaurante);
        emitterRestaurantes.send(restauranteJson);

        return Response.status(Response.Status.CREATED).build();

    }

    @PUT
    @Path("{id}")
    @Transactional
    @APIResponse(responseCode = "201", description = "Restaurante atualizado com sucesso.")
    @APIResponse(responseCode = "400", description = "Erro de validação no envio das informações.", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public void atualizarRestaurante(@PathParam("id") Long id, @Valid AtualizarRestauranteDTO dto) {
        Restaurante restaurante = ObterRestaurante(id);
        restauranteMapper.toRestaurante(dto, restaurante);
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletarRestaurante(@PathParam("id") Long id) {
        Restaurante restaurante = ObterRestaurante(id);
        restaurante.delete();
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<PratoDTO> listarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Restaurante restaurante = ObterRestaurante(idRestaurante);
        Stream<Prato> pratos = Prato.stream("restaurante", restaurante);
        return pratos.map(p -> restauranteMapper.toPratoDTO(p)).collect(Collectors.toList());
    }

    @POST
    @Path("{idRestaurante}/prato")
    @Tag(name = "prato")
    @Transactional
    public Response adicionarPratoRestaurante(
            @PathParam("idRestaurante") Long idRestaurante,
            @Valid AdicionarPratoDTO dto) {

        //Verificar o restaurante
        Restaurante restaurante = ObterRestaurante(idRestaurante);

        Prato entityPrato = restauranteMapper.toPrato(dto);
        entityPrato.restaurante = restaurante;
        entityPrato.persist();

        //Enviar para o AMQ.
        Jsonb create = JsonbBuilder.create();
        String pratoJson = create.toJson(entityPrato);
        emitterPratos.send(pratoJson);

        return Response.status(Response.Status.CREATED).build();

    }

    @PUT
    @Path("{idRestaurante}/prato/{id}")
    @Tag(name = "prato")
    @Transactional
    public void atualizarPratoRestaurante(
            @PathParam("idRestaurante") Long idRestaurante,
            @PathParam("id") Long idPrato,
            @Valid AtualizarPratoDTO dto) {

        //Verificar o restaurante
        ObterRestaurante(idRestaurante);

        Prato prato = ObterPrato(idPrato);
        restauranteMapper.toPrato(dto, prato);
        prato.persist();

    }

    @DELETE
    @Path("{idRestaurante}/prato/{id}")
    @Tag(name = "prato")
    @Transactional
    public void deletarPratoRestaurante(
            @PathParam("idRestaurante") Long idRestaurante,
            @PathParam("id") Long idPrato) {

        //Verificar restaurante...
        ObterRestaurante(idRestaurante);

        Prato prato = ObterPrato(idPrato);
        prato.delete();

    }


    /**
     * Verifica a existencia e retorna um Restaurante pelo Id.
     * @param idRestaurante - Identificador do restaurante.
     * @return
     */
    private Restaurante ObterRestaurante(Long idRestaurante) {
        Optional<Restaurante> entityRestaurante = Restaurante.findByIdOptional(idRestaurante);
        if (!entityRestaurante.isPresent()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        return entityRestaurante.get();
    }

    /**
     * Verifica a existencia e retorna um Prato pelo ID.
     * @param idPrato - Identificador do Prato.
     * @return
     */
    private Prato ObterPrato(Long idPrato) {
        Optional<Prato> entityPrato = Prato.findByIdOptional(idPrato);
        if (!entityPrato.isPresent()) {
            throw new NotFoundException("Prato não encontrado.");
        }
        return entityPrato.get();
    }

}