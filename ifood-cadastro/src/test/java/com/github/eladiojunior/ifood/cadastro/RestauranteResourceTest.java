package com.github.eladiojunior.ifood.cadastro;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.eladiojunior.ifood.cadastro.entites.Restaurante;
import com.github.eladiojunior.ifood.cadastro.util.TokenUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
public class RestauranteResourceTest {

    private String token;

    @BeforeEach
    public void gerarToken() {
        try {
            token = TokenUtils.generateTokenString("/JWTProprietarioClaims.json", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DataSet("restaurantes-cenario-01.yml")
    public void testListarRestaurantes() {
        String resultado = given()
          .when().get("/restaurantes")
          .then()
             .statusCode(200)
             .extract().asString();
        Approvals.verifyJson(resultado);
    }

    /**
     * Especificar o tipo do dados (JSON) utilizado nas requisições de teste.
     * @return
     */
    private RequestSpecification given() {
        return RestAssured.given().contentType(ContentType.JSON).header(new Header("Authorization", "Bearer " + token));
    }

    @Test
    @DataSet("restaurantes-cenario-01.yml")
    public void testAtualizarRestaurantes() {
        Restaurante dto = new Restaurante();
        dto.nome = "Alteração no nome do restaurante";
        Long idParamRestaurante = 1L;

        given()
                .with().pathParam("id", idParamRestaurante)
                .body(dto)
                .when().put("/restaurantes/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode())
                .extract().asString();
        Restaurante findById = Restaurante.findById(idParamRestaurante);
        Assert.assertEquals(dto.nome, findById.nome);
    }

}