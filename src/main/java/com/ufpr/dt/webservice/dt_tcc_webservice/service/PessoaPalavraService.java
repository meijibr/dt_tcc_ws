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


    public int getQuantidade(Pessoa pessoa,Palavra palavra){
        PessoaPalavra p = pessoaPalavraRepository.findByPessoaAndPalavra(pessoa, palavra);
        if (p != null) {
            if (p.getQuantidade() != null){
                return p.getQuantidade();
            }
        }
        return 0;
    }

    public Double getAvaliacao(Pessoa pessoa, Palavra palavra){
        PessoaPalavra p = pessoaPalavraRepository.findByPessoaAndPalavra(pessoa, palavra);
        if (p != null) {
            if (p.getAvaliacao() != null) {
                return p.getAvaliacao();
            }
        }
            return 0.0;
    }

    public void setAvaliacao(Pessoa pessoa, Palavra palavra, Double avaliacao) {
        PessoaPalavra p = pessoaPalavraRepository.findByPessoaAndPalavra(pessoa, palavra);
        if (p == null) {
            p = new PessoaPalavra(pessoa, palavra,avaliacao);
            pessoaPalavraRepository.save(p);
        } else {
            p.updateAvaliacao(avaliacao);
            pessoaPalavraRepository.save(p);
        }
    }
}
