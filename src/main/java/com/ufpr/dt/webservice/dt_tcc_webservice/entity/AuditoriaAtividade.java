package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AuditoriaAtividade")
public class AuditoriaAtividade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idPessoa")
    private Pessoa pessoa;
    @ManyToOne
    @JoinColumn(name="idLista")
    private Lista lista;
    @ManyToOne
    @JoinColumn(name="idTipoAtividade")
    private TipoAtividade tipoAtividade;
    @Column(nullable = false, name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public TipoAtividade getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
