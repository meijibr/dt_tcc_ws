package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.google.gson.Gson;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.FraseAvaliacao;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.Jogador;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.*;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.AtividadeRepository;
import jdk.nashorn.internal.scripts.JO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AtividadeService {

    private Map<Long, Jogador> jogadores = new HashMap<Long, Jogador>();

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private ListaService listaService;

    @Autowired
    private TipoAtividadeService tipoAtividadeService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaPalavraService pessoaPalavraService;

    @Autowired
    private ListaPalavraFraseService listaPalavraFraseService;

    @PersistenceContext
    protected EntityManager em;

    public Atividade salvar(Atividade atividade) {
        atividadeRepository.saveAndFlush(atividade);
        return atividade;
    }

    public Atividade findByPin(Long pin) {
        em.clear();
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
            Jogador jogador = new Jogador(atividade.getPin());
            if (jogadores.get(pessoa) == null) {
                jogadores.put(pessoa, jogador);
                System.out.printf("Antes " + new Gson().toJson(jogadores).toString());
            }
            return;
        }
        atividade.addPessoa(p);
        Jogador jogador = new Jogador(atividade.getPin());
        jogadores.put(pessoa, jogador);
        System.out.printf("Antes " + new Gson().toJson(jogadores).toString());
        salvar(atividade);
    }

    public void start(Atividade atividade) {
        List<PalavraFrase> frases;
        List<FraseAvaliacao> fraseAvaliacoes;
        List<Pessoa> pessoas = atividade.getPessoas();
        for (int i = 0; i < pessoas.size(); i++){
            Pessoa p = pessoas.get(i);
            frases = listaPalavraFraseService.getListPalavraFrase(atividade.getLista());
            fraseAvaliacoes = new ArrayList<FraseAvaliacao>();
            for (int j = 0; j < frases.size();j++){
                FraseAvaliacao f = new FraseAvaliacao();
                f.setAvaliacao(pessoaPalavraService.getAvaliacao(pessoas.get(i), frases.get(j).getPalavra()));
                f.setFrase(frases.get(j).getFrase());
                f.setFraseTraduzida(frases.get(j).getTraducao());
                fraseAvaliacoes.add(f);
            }
            System.out.printf("Antes " + new Gson().toJson(fraseAvaliacoes).toString());
            Collections.sort(fraseAvaliacoes);
            jogadores.get(p.getId()).setOrdemPalavras(fraseAvaliacoes);
            System.out.printf("Depois "+ new Gson().toJson(fraseAvaliacoes).toString());
        }

//        switch (atividade.getTipoAtividade().getAtividade()) {
//            case "Revisão":
//                System.out.printf("Revisao");
//            break;
//            case "Tradução":
//                System.out.printf("Tradução");
//                break;
//            default:
//                System.out.printf("Default");
//                break;
//        }

        atividade.setEstado("Pre-Frase");
        salvar(atividade);
    }

    public Atividade aguardarInicio(Atividade atividade) throws InterruptedException {
        if (atividade.getEstado().equals("Aguardando")) {
            Long pin = atividade.getPin();
            while (true) {
                Thread.sleep(1000);
                atividade = findByPin(atividade.getPin());
                System.out.println("Preso aqui " + atividade.getEstado());
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

    public String proximaFrase(Atividade atividade, Long idPessoa){

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                System.out.printf("Revisao");
                return "Revisao";
            case "Tradução":
                System.out.printf("Tradução");
                return "Tradução";
            default:
                System.out.printf("Default");
                return "Default";
        }
    }
}
