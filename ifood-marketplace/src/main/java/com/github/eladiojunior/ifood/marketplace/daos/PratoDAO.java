package com.github.eladiojunior.ifood.marketplace.daos;

import com.github.eladiojunior.ifood.marketplace.dtos.PratoDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import java.util.stream.StreamSupport;

public class PratoDAO {
    private PgPool client;

    private PratoDAO(PgPool client) {
        this.client = client;
    }

    public static PratoDAO of (PgPool client) {
        return new PratoDAO(client);
    }

    public Multi<PratoDTO> findAll() {
        String sql = "SELECT ID_PRATO, NM_PRATO, DS_PRATO, VL_PRATO FROM TB_PRATO";
        Uni<RowSet<Row>> resultUni = this.client.preparedQuery(sql).execute();
        return uniToMulti(resultUni);
    }

    public Multi<PratoDTO> findAll(Long idRestaurante) {
        String sql = "SELECT ID_PRATO, NM_PRATO, DS_PRATO, VL_PRATO FROM TB_PRATO WHERE ID_RSTRE = $1 ORDER BY NM_PRATO ASC";
        Uni<RowSet<Row>> resultUni = this.client.preparedQuery(sql).execute(Tuple.of(idRestaurante));
        return uniToMulti(resultUni);
    }

    private Multi<PratoDTO> uniToMulti(Uni<RowSet<Row>> uni) {
        return uni.onItem().produceMulti(rs -> Multi.createFrom().items(() -> {
            return StreamSupport.stream(rs.spliterator(), false);
        })).onItem().apply(PratoDTO::from);
    }
}
