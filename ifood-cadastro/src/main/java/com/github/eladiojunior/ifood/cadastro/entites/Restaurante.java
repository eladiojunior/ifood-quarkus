package com.github.eladiojunior.ifood.cadastro.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_RSTRE")
public class Restaurante extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RSTRE")
    public Long id;

    @Column(name = "NM_PRPRO_RSTRE")
    public String proprietario;

    @Column(name = "NM_RSTRE")
    public String nome;

    @Column(name = "NR_CNPJ_RSTRE")
    public String cnpj;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_LCLZO" )
    public Localizacao localizacao;

    @Column(name = "DH_RGSTO_RSTRE")
    @CreationTimestamp
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public Date dataHoraRegistro;

    @Column(name = "DH_ATLZO_RSTRE")
    @UpdateTimestamp
    @JsonbDateFormat(value = "yyyy-MM-dd")
    public Date dataHoraAtualizacao;

}
