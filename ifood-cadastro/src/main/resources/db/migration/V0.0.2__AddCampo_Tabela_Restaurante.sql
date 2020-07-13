/*
 * Versão de banco 0.0.2 - Inclusão do campo NR_CNPJ_RSTRE que falta.
 */

alter table tb_rstre
	add nr_cnpj_rstre varchar(14);