package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Palavra;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.PessoaPalavra;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.PessoaPalavraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaPalavraService {

    @Autowired
    PessoaPalavraRepository pessoaPalavraRepository;

    public Double getAvaliacao(Pessoa pessoa, Palavra palavra){
        PessoaPalavra p = pessoaPalavraRepository.findByPessoaAndPalavra(pessoa, palavra);
        if (p != null) {
            if (p.getAvaliacao() != null) {
                return p.getAvaliacao();
            }
        }
            return 0.0;

    }
}
