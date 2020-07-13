/*
 * Versão de banco 0.0.1 para contrução da estrutura de dados.
 */
create table tb_lclzo
(
	id_lclzo bigint constraint tb_lclzo_pkey primary key,
	cd_lttde double precision,
	cd_lngte double precision
);
alter table tb_lclzo owner to ifood_marketplace;

create table tb_rstre
(
	id_rstre bigint constraint tb_rstre_pkey primary key,
	nm_rstre varchar(80),
	id_lclzo bigint constraint fk_tb_lclzo references tb_lclzo
);
alter table tb_rstre owner to ifood_marketplace;

create table tb_prato
(
    id_prato bigint constraint tb_prato_pkey primary key,
    ds_prato varchar(500),
    nm_prato varchar(50),
    vl_prato numeric(19,2),
    id_rstre bigint constraint fk_tb_rstre references tb_rstre
);
alter table tb_prato owner to ifood_marketplace;

create table tb_prato_clnte
(
    id_prato bigint,
    nm_clnte varchar(100)
);
alter table tb_prato_clnte owner to ifood_marketplace;