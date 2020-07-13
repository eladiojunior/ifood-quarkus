package com.github.eladiojunior.ifood.marketplace.daos;

import com.github.eladiojunior.ifood.marketplace.dtos.LocalizacaoDTO;
import com.github.eladiojunior.ifood.marketplace.dtos.RestauranteDTO;
import com.github.eladiojunior.ifood.marketplace.entites.Localizacao;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.stream.StreamSupport;

public class LocalizacaoDAO {
    private PgPool client;

    private LocalizacaoDAO(PgPool client) {
        this.client = client;
    }

    public static LocalizacaoDAO of (PgPool client) {
        return new LocalizacaoDAO(client);
    }

    public Uni<LocalizacaoDTO> obter(Long idLocalizacao) {
        String sql = "SELECT ID_LCLZO, CD_LTTDE, CD_LNGTE FROM TB_LCLZO WHERE ID_LCLZO = $1";
        return this.client.preparedQuery(sql).execute(Tuple.of(idLocalizacao))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? LocalizacaoDTO.from(iterator.next()) : null);
    }

    private Multi<RestauranteDTO> uniToMulti(Uni<RowSet<Row>> uni) {
        return uni.onItem().produceMulti(rs -> Multi.createFrom().items(() -> {
            return StreamSupport.stream(rs.spliterator(), false);
        })).onItem().apply(RestauranteDTO::from);
    }

    /**
     * Registrar ou alterar a licalização.
     * @param localizacao - Informações do localicação.
     */
    public void persist(Localizacao localizacao) {

        /*
        //Verificar existencia.
        Uni<LocalizacaoDTO> dtoUni = LocalizacaoDAO.of(this.client).obter(localizacao.getId());
        if (dtoUni == null)
        {//Não existe, incluir.
            String sql = "INSERT INTO TB_LCLZO (ID_LCLZO, CD_LTTDE, CD_LNGTE) VALUES ($1, $2, $3)";
            this.client.preparedQuery(sql).execute(Tuple.of(localizacao.getId(), localizacao.getLatitude(), localizacao.getLongitude())).await().indefinitely();
        }
        else
        {//Alterar informações.
            String sql = "UPDATE TB_LCLZO SET CD_LTTDE = $1, CD_LNGTE = $2 WHERE ID_LCLZO = $3";
            this.client.preparedQuery(sql).execute(Tuple.of(localizacao.getLatitude(), localizacao.getLongitude(), localizacao.getId())).await().indefinitely();
        }
        */

        String sql = "INSERT INTO TB_LCLZO (ID_LCLZO, CD_LTTDE, CD_LNGTE) VALUES ($1, $2, $3)";
        this.client.preparedQuery(sql).execute(Tuple.of(localizacao.getId(), localizacao.getLatitude(), localizacao.getLongitude())).await().indefinitely();

    }

}
