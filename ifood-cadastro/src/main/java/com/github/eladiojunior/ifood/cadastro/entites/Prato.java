package com.github.eladiojunior.ifood.cadastro.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_PRATO")
public class Prato extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRATO")
    public Long id;

    @Column(name = "NM_PRATO")
    public String nome;

    @Column(name = "DS_PRATO")
    public String descricao;

    @Column(name = "VL_PRATO")
    public BigDecimal preco;

    @ManyToOne
    public Restaurante restaurante;

}
