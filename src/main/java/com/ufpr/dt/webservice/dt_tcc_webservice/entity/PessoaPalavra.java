package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "PessoaPalavra")
public class PessoaPalavra implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idPessoa")
    private Pessoa pessoa;
    @ManyToOne
    @JoinColumn(name="idPalavra")
    private Palavra palavra;
    @Column(nullable = false, name = "avaliacao")
    private Double avaliacao;
    @Column(nullable = false, name = "quantidade")
    private Integer quantidade;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Palavra getPalavra() {
        return palavra;
    }

    public void setPalavra(Palavra palavra) {
        this.palavra = palavra;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
