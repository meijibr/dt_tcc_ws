package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AuditoriaLista")
public class AuditoriaLista implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idLista")
    private Lista lista;
    @ManyToOne
    @JoinColumn(name="idPessoa")
    private Pessoa pessoa;
    @Column(nullable = false, name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(nullable = false, name = "registro")
    private String registro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
