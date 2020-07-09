package com.github.eladiojunior.ifood.cadastro;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:12.3");

    @Override
    public Map<String, String> start() {
        POSTGRES.start();
        Map<String, String> propriedades = new HashMap<String, String>();
        //Variáveis de acesso ao banco de dados.
        propriedades.put("quarkus.datasource.url", POSTGRES.getJdbcUrl());
        propriedades.put("quarkus.datasource.username", POSTGRES.getUsername());
        propriedades.put("quarkus.datasource.password", POSTGRES.getPassword());
        return propriedades;
    }

    @Override
    public void stop() {
        if (POSTGRES != null && POSTGRES.isRunning())
            POSTGRES.stop();
    }
}
