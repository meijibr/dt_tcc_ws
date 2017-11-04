package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Atividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private ListaService listaService;

    @Autowired
    private TipoAtividadeService tipoAtividadeService;

    @Autowired
    private PessoaService pessoaService;

    public Atividade salvar(Atividade atividade) {
        atividadeRepository.saveAndFlush(atividade);
        return atividade;
    }

    public Atividade findByPin(Long pin) {
        return atividadeRepository.findByPin(pin);
    }

    public Atividade findById(Long id) {
        return atividadeRepository.findById(id);
    }

    public List<Atividade> mostrarTodos() {
        return atividadeRepository.findAll();
    }

    public Atividade mostrarUm(Long id) {
        return atividadeRepository.findOne(id);
    }

    public void delete(Long id){
        atividadeRepository.delete(id);
    }

    public boolean repetido(Atividade atividade) {
        Atividade a = findByPin(atividade.getPin());
        if (a != null){
            return true;
        } else {
            return false;
        }
    }

    public Atividade iniciarAtividade(Long tipoAtividade, Long lista){
        Atividade atividade = new Atividade( tipoAtividadeService.findById(tipoAtividade), listaService.findById(lista));
        int min, max;
        min = 10000;
        max = 99999;
        do {
            Long randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);
            atividade.setPin(randomNum);
            if (repetido(atividade)) {
                System.out.println("A Atividade with pin " + atividade.getPin() + " already exist");
            }
        } while (repetido(atividade));
        return atividade;
    }

    public void adicionarPessoa(Atividade atividade, Long pessoa) {
        Pessoa p = pessoaService.findById(pessoa);
        if (atividade.getPessoas().contains(p)){
            return;
        }
        atividade.addPessoa(p);
        salvar(atividade);
    }

    public void start(Atividade atividade) {
        atividade.setEstado("Pre-Frase");
        salvar(atividade);
    }

    public Atividade aguardarInicio(Atividade atividade) throws InterruptedException {
        if (atividade.getEstado().equals("Aguardando")) {
            Long pin = atividade.getPin();
            while (true) {
                Atividade ativ = findByPin(pin);
                Thread.sleep(1000);
                atividade = findByPin(atividade.getPin());
                atividadeRepository.flush();
                System.out.println("Preso aqui " + atividade.getEstado() + ativ.getEstado());
                if (atividade.getEstado().equals("Pre-Frase")){
                    break;
                }
            }
        }
        if (atividade.getEstado().equals("Pre-Frase")){
            return atividade;
        } else {
            return null;
        }
    }
}
