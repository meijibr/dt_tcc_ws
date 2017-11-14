package com.ufpr.dt.webservice.dt_tcc_webservice.service;



import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa salvar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
        return pessoa;
    }

    public Pessoa findByEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }

    public Pessoa findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public List<Pessoa> mostrarTodos() {
        return pessoaRepository.findAll();
    }

    public Pessoa mostrarUm(Long id) {
        return pessoaRepository.findOne(id);
    }

    public void delete(Long id){
        pessoaRepository.delete(id);
    }

    public boolean repetido(Pessoa pessoa) {
        Pessoa p = findByEmail(pessoa.getEmail());
        if (p != null){
            return true;
        } else {
            return false;
        }
    }
    public boolean repetidoUpdate(Pessoa atual, Pessoa atualizada) {
        Pessoa p = findByEmail(atualizada.getEmail());
        if (atual.getEmail().equals(atualizada.getEmail())) {
            return false;
        } else {
            if (p != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
