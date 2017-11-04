package com.ufpr.dt.webservice.dt_tcc_webservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Pessoa")
public class Pessoa implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60, name = "nome")
    @NotBlank(message = "Nome é uma informação obrigatória.")
    private String nome;
    @Column(nullable = false, length = 60, name = "sobrenome")
    @NotBlank(message = "Sobrenome é uma informação obrigatória.")
    private String sobrenome;
    @Column(name = "senha", nullable = false, length = 60)
    @NotBlank(message = "Senha é uma informação obrigatória.")
    private String senha;
    @Column(name = "email", nullable = false, length = 60)
    @NotBlank(message = "E-mail é uma informação obrigatória.")
    private String email;

    //bi-directional many-to-one association to Lista
    @OneToMany(mappedBy="pessoa")
    @JsonManagedReference
    private List<Lista> listas;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Lista> getListas() {
        return listas;
    }

    public void setListas(List<Lista> listas) {
        this.listas = listas;
    }
}

