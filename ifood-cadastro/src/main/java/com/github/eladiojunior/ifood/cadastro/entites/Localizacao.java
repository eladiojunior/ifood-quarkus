package com.github.eladiojunior.ifood.cadastro.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "TB_LCLZO")
public class Localizacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LCLZO")
    public Long id;

    @Column(name = "CD_LTTDE")
    public Double latitude;

    @Column(name = "ID_LNGTE")
    public Double longitude;

}
