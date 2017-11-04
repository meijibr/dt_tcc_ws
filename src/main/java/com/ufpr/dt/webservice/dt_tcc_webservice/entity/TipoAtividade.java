package com.ufpr.dt.webservice.dt_tcc_webservice.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "TipoAtividade")
public class TipoAtividade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60, name = "atividade")
    private String atividade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }
}
