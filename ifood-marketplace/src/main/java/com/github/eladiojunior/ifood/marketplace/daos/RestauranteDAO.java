package com.github.eladiojunior.ifood.marketplace.daos;

import com.github.eladiojunior.ifood.marketplace.dtos.RestauranteDTO;
import com.github.eladiojunior.ifood.marketplace.entites.Localizacao;
import com.github.eladiojunior.ifood.marketplace.entites.Restaurante;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.*;

import java.util.stream.StreamSupport;

public class RestauranteDAO {
    private PgPool client;

    private RestauranteDAO(PgPool client) {
        this.client = client;
    }

    public static RestauranteDAO of (PgPool client) {
        return new RestauranteDAO(client);
    }

    public Multi<RestauranteDTO> listar() {
        String sql = "SELECT R.ID_RSTRE, R.NM_RSTRE, L.CD_LTTDE, L.CD_LNGTE FROM TB_RSTRE R INNER JOIN TB_LCLZO L ON R.ID_LCLZO = L.ID_LCLZO";
        Uni<RowSet<Row>> resultUni = this.client.preparedQuery(sql).execute();
        return uniToMulti(resultUni);
    }

    public Uni<RestauranteDTO> obter(Long idRestaurante) {
        String sql = "SELECT R.ID_RSTRE, R.NM_RSTRE, L.CD_LTTDE, L.CD_LNGTE FROM TB_RSTRE R INNER JOIN TB_LCLZO L ON R.ID_LCLZO = L.ID_LCLZO WHERE R.ID_RSTRE = $1";
        return this.client.preparedQuery(sql).execute(Tuple.of(idRestaurante))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? RestauranteDTO.from(iterator.next()) : null);
    }

    private Multi<RestauranteDTO> uniToMulti(Uni<RowSet<Row>> uni) {
        return uni.onItem().produceMulti(rs -> Multi.createFrom().items(() -> {
            return StreamSupport.stream(rs.spliterator(), false);
        })).onItem().apply(RestauranteDTO::from);
    }

    /**
     * Registrar ou alterar o restaurante com a sua licalização.
     * @param restaurante - Informações do restaurante.
     */
    public void persist(Restaurante restaurante) {

        try {

            Localizacao lclzo = restaurante.getLocalizacao();
            Long idLocalidade = null;
            if (lclzo != null)
            {
                String sql = "INSERT INTO TB_LCLZO (ID_LCLZO, CD_LTTDE, CD_LNGTE) VALUES ($1, $2, $3)";
                this.client.preparedQuery(sql).execute(Tuple.of(lclzo.getId(), lclzo.getLatitude(), lclzo.getLongitude())).await().indefinitely();
                idLocalidade = lclzo.getId();
            }
            /*

            //Verificar existencia.
            Uni<RestauranteDTO> dto = obter(restaurante.getId());
            if (dto == null)
            {//Não existe, incluir.
                String sql = "INSERT INTO TB_RSTRE (ID_RSTRE, NM_RSTRE, ID_LCLZO) VALUES ($1, $2, $3)";
                transaction.preparedQuery(sql).execute(Tuple.of(restaurante.getId(), restaurante.getNome(), idLocalidade)).await().indefinitely();
            }
            else
            {//Alterar informações.
                String sql = "UPDATE TB_RSTRE SET NM_RSTRE = $1 WHERE ID_RSTRE = $2";
                transaction.preparedQuery(sql).execute(Tuple.of(restaurante.getNome(), restaurante.getId())).await().indefinitely();
            }
            */

            String sql = "INSERT INTO TB_RSTRE (ID_RSTRE, NM_RSTRE, ID_LCLZO) VALUES ($1, $2, $3)";
            this.client.preparedQuery(sql).execute(Tuple.of(restaurante.getId(), restaurante.getNome(), idLocalidade)).await().indefinitely();

        } catch (Exception erro) {
            throw erro;
        }

    }

}
