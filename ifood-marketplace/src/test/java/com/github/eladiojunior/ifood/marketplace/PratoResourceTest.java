package com.github.eladiojunior.ifood.marketplace;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PratoResourceTest {

    @Test
    public void testlistarPratos() {
        String body = given()
          .when().get("/pratos")
          .then()
             .statusCode(200).extract().asString();
        System.out.println(body);
    }

}