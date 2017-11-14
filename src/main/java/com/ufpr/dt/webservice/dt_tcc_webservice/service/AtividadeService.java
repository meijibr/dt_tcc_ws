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

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                break;
            case "Tradução":
                List<Long> ids = new ArrayList<Long>(jogadores.keySet());
                if (ids.size()/2 == 0) {
                    Collections.shuffle(ids);
                    while (ids.size() != 0){
                        jogadores.get(ids.get(0)).setIdDupla(ids.get(1));
                        jogadores.get(ids.get(1)).setIdDupla(ids.get(0));
                        ids.remove(1);
                        ids.remove(0);
                    }
                } else {
                    atividade.setEstado("Impar");
                    salvar(atividade);
                    return;
                }
                break;
            default:
                System.out.printf("Errou");
                break;
        }

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
        Pessoa p = pessoaService.findById(idPessoa);
        if (p == null) {
            return null;
        }
        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                return jogadores.get(p.getId()).getProxima();
            case "Tradução":
                if (jogadores.get(p.getId()).isMinhaVez()){
                    return jogadores.get(p.getId()).getProxima();
                } else {
                    return jogadores.get(jogadores.get(p.getId()).getIdDupla()).getTraducao();
                }
            default:
                System.out.printf("Errou");
                return "Default";
        }
    }
}
