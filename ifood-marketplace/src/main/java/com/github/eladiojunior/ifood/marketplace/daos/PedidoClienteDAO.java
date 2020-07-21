package com.github.eladiojunior.ifood.marketplace.daos;

import com.github.eladiojunior.ifood.marketplace.dtos.PratoDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.stream.StreamSupport;

public class PedidoClienteDAO {
    private PgPool client;

    private PedidoClienteDAO(PgPool client) {
        this.client = client;
    }

    public static PedidoClienteDAO of (PgPool client) {
        return new PedidoClienteDAO(client);
    }

    public Multi<PratoDTO> listarPratos() {
        String sql = "SELECT ID_PRATO, NM_PRATO, DS_PRATO, VL_PRATO FROM TB_PRATO";
        Uni<RowSet<Row>> resultUni = this.client.preparedQuery(sql).execute();
        return uniToMulti(resultUni);
    }

    public Multi<PratoDTO> listarPratosRestaurante(Long idRestaurante) {
        String sql = "SELECT ID_PRATO, NM_PRATO, DS_PRATO, VL_PRATO FROM TB_PRATO WHERE ID_RSTRE = $1 ORDER BY NM_PRATO ASC";
        Uni<RowSet<Row>> resultUni = this.client.preparedQuery(sql).execute(Tuple.of(idRestaurante));
        return uniToMulti(resultUni);
    }

    private Multi<PratoDTO> uniToMulti(Uni<RowSet<Row>> uni) {
        return uni.onItem().produceMulti(rs -> Multi.createFrom().items(() -> {
            return StreamSupport.stream(rs.spliterator(), false);
        })).onItem().apply(PratoDTO::from);
    }

    /**
     * Recupera os pratos de um pedido do cliente.
     * @param cliente - Identificador do cliente.
     * @return
     */
    public Multi<PratoDTO> listarPratosPedidoCliente(String cliente) {
        String sql = "SELECT P.ID_PRATO, P.NM_PRATO, P.DS_PRATO, P.VL_PRATO  FROM TB_PRATO_CLNTE PC " +
                        "INNER JOIN TB_PRATO P ON P.ID_PRATO = PC.ID_PRATO WHERE PC.ID_CLNTE = $1";
        return uniToMulti(this.client.preparedQuery(sql).execute(Tuple.of(cliente)));
    }

    /**
     * Adicionar um prato ao pedido do cliente.
     * @param cliente - identificiador do cliente.
     * @param idPrato - identificador do prato.
     * @return Retorna o identificado do registro de (cliente e prato)
     */
    public Uni<Long> adicionarPratoPedidoCliente(String cliente, Long idPrato) {
        String sql = "INSERT INTO TB_PRATO_CLNTE (ID_CLNTE, ID_PRATO) VALUES ($1, $2) RETURNING ID_PRATO_CLNTE";
        return this.client.preparedQuery(sql).execute(Tuple.of(cliente, idPrato))
                .map(rowset -> rowset.iterator().next().getLong("ID_PRATO_CLNTE"));
    }

    /**
     * Realiza a finalização do pedido, removendo os pedidos do carrinho.
     * Obs: Poderíamos fechar o pedido e manter o histórico.
     * @param cliente - Identificador do cliente.
     * @return
     */
    public Uni<Boolean> finalizarPedidoCliente(String cliente) {
        String sql = "DELETE FROM TB_PRATO_CLNTE WHERE ID_CLNTE = $1";
        return this.client.preparedQuery(sql).execute(Tuple.of(cliente)).onItem().apply(rs -> rs.rowCount() == 1);
    }
}
