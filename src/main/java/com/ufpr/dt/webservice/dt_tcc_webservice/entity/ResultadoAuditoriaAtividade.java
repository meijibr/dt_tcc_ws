package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "ResultadoAuditoriaAtividade")
public class ResultadoAuditoriaAtividade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="idPalavraFrase")
    private PalavraFrase palavraFrase;
    @ManyToOne
    @JoinColumn(name="idAuditoriaAtividade")
    private AuditoriaAtividade auditoriaAtividade;
    @ManyToOne
    @JoinColumn(name="idPessoa")
    private Pessoa pessoa;
    @Column(nullable = false, name = "resultado")
    private String resultado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PalavraFrase getPalavraFrase() {
        return palavraFrase;
    }

    public void setPalavraFrase(PalavraFrase palavraFrase) {
        this.palavraFrase = palavraFrase;
    }

    public AuditoriaAtividade getAuditoriaAtividade() {
        return auditoriaAtividade;
    }

    public void setAuditoriaAtividade(AuditoriaAtividade auditoriaAtividade) {
        this.auditoriaAtividade = auditoriaAtividade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
