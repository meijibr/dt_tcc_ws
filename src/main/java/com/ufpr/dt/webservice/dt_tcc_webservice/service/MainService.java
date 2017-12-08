package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    @Autowired
    PessoaService pessoaService;

    public Pessoa findByEmail(String email) {
        return pessoaService.findByEmail(email);
    }

    public boolean login(Pessoa p, String senha) {
        if (p.getSenha().equals(senha)){
            System.out.printf("P-> " + p.getSenha() + " Senha->" + senha + "\n");
            return true;
        }
        return false;
    }
}
