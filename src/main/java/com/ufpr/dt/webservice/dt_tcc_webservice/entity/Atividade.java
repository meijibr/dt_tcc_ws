package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Atividade")
public class Atividade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "estado")
    private String estado;
    @ManyToOne
    @JoinColumn(name = "idTipoAtividade")
    private TipoAtividade tipoAtividade;
    @Column(name = "pin")
    private Long pin;
    @ManyToOne
    @JoinColumn(name="idLista")
    private Lista lista;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pessoa> pessoas;

    public Atividade(){

    }

    public Atividade(TipoAtividade tipoAtividade, Lista lista) {
        this.tipoAtividade = tipoAtividade;
        this.lista = lista;
        this.estado = "Aguardando";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoAtividade getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public void addPessoa(Pessoa pessoa){
        pessoas.add(pessoa);
    }
}
