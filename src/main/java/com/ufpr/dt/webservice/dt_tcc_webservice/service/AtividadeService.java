package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.google.gson.Gson;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.FraseAvaliacao;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.Jogador;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.*;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.AtividadeRepository;
import jdk.nashorn.internal.scripts.JO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AtividadeService {

    private static final int MAX_CONT = 4;
    private Map<Long, Map<Long, Jogador>> atividades = new HashMap<Long, Map<Long, Jogador>>();


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

    public void criarAtividade(Atividade atividade){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
            atividades.put(atividade.getPin(), jogadores);
        }
    }

    public void adicionarPessoa(Atividade atividade, Long pessoa) {
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }

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
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        List<PalavraFrase> frases;
        List<FraseAvaliacao> box1;
        List<FraseAvaliacao> box2;
        List<FraseAvaliacao> box3;
        List<FraseAvaliacao> fraseAvaliacoes;
        List<Pessoa> pessoas = atividade.getPessoas();
        for (int i = 0; i < pessoas.size(); i++){
            Pessoa p = pessoas.get(i);
            frases = listaPalavraFraseService.getListPalavraFrase(atividade.getLista());
            box1 = new ArrayList<FraseAvaliacao>();
            box2 = new ArrayList<FraseAvaliacao>();
            box3 = new ArrayList<FraseAvaliacao>();
            fraseAvaliacoes = new ArrayList<FraseAvaliacao>();
            for (int j = 0; j < frases.size();j++){
                double ratio = 0;
                FraseAvaliacao f = new FraseAvaliacao();
                f.setAvaliacao(pessoaPalavraService.getAvaliacao(p, frases.get(j).getPalavra()));
                f.setFrase(frases.get(j).getFrase());
                f.setFraseTraduzida(frases.get(j).getTraducao());
                f.setPalavra(frases.get(j).getPalavra());
                if (pessoaPalavraService.getQuantidade(p, f.getPalavra()) != 0){
                    ratio = (double)f.getAvaliacao()/pessoaPalavraService.getQuantidade(p, f.getPalavra());
                }
                if (f.getAvaliacao()<1.5) {
                    if (ratio < 0.025) {
                        box1.add(f);
                    } else if (ratio < 0.075) {
                        box2.add(f);
                    } else {
                        box3.add(f);
                    }
                }
            }

            switch (atividade.getTipoAtividade().getAtividade()) {
                case "Revisão":
                    fraseAvaliacoes.addAll(box1);
                    if ((atividade.getRounds() % 2)==0){
                        fraseAvaliacoes.addAll((box2));
                    }
                    if ((atividade.getRounds() % 5)==0){
                        fraseAvaliacoes.addAll(box3);
                    }
                    break;
                case "Tradução":
                    fraseAvaliacoes.addAll(box1);
                    fraseAvaliacoes.addAll(box2);
                    fraseAvaliacoes.addAll(box3);
                    break;
            }
            switch (atividade.getTipoAtividade().getAtividade()) {
                case "Revisão":
                    Collections.sort(fraseAvaliacoes);
                    break;
                case "Tradução":
                    Collections.shuffle(fraseAvaliacoes);
                    break;
            }
            jogadores.get(p.getId()).setOrdemPalavras(fraseAvaliacoes);
            jogadores.get(p.getId()).setMaxCont(MAX_CONT);
            jogadores.get(p.getId()).setCont(MAX_CONT);
        }

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                atividade.setParear(0);
                atividade.setPareou(0);
                salvar(atividade);
                break;
            case "Tradução":
                atividade.setParear(1);
                atividade.setPareou(0);
                salvar(atividade);
                System.out.printf("size = " + jogadores.size());
                if (jogadores.size() % 2 == 0) {
                    break;
                } else {
                    atividade.setEstado("Impar");
                    salvar(atividade);
                    return;
                }
            default:
                System.out.printf("Errou");
                break;
        }

        atividade.setEstado("Pre-Frase");
        salvar(atividade);
    }

    public void startTraducao(Atividade atividade, int qtd) {
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
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
                f.setPalavra(frases.get(j).getPalavra());
                fraseAvaliacoes.add(f);
            }
            System.out.printf("Antes " + new Gson().toJson(fraseAvaliacoes).toString());
            Collections.sort(fraseAvaliacoes);
            jogadores.get(p.getId()).setOrdemPalavras(fraseAvaliacoes);
            System.out.printf("Depois "+ new Gson().toJson(fraseAvaliacoes).toString());
            jogadores.get(p.getId()).setMaxCont(qtd);
        }

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                break;
            case "Tradução":
                List<Long> ids = new ArrayList<Long>(jogadores.keySet());
                if (ids.size()/2 != 0) {
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
        if (atividade.getEstado().equals("Pre-Frase")){
            return atividade;
        } else {
            return null;
        }
    }

    public String proximaFrase(Atividade atividade, Long idPessoa){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        Pessoa p = pessoaService.findById(idPessoa);

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                return jogadores.get(p.getId()).getProxima();
            case "Tradução":
                Jogador jogador = jogadores.get(p.getId());
                Jogador dupla = jogadores.get(jogador.getIdDupla());
                System.out.printf("\nNome" + p.getNome() + "\n");
                System.out.printf(" Cont" + jogadores.get(p.getId()).getCont() + " " + jogadores.get(p.getId()).getMaxCont());
                System.out.printf(" Dupla" + pessoaService.findById(jogadores.get(p.getId()).getIdDupla()).getNome() + "\n");
                System.out.printf(" CDupla" + jogadores.get(jogadores.get(p.getId()).getIdDupla()).getCont() + " " + jogadores.get(jogadores.get(p.getId()).getIdDupla()).getMaxCont() + "\n");
                if (jogador.isMinhaVez()) {
                    if (jogador.getCont() == jogador.getMaxCont()) {
                        checkParear(atividade);
                        return null;
                    } else if (jogador.getOrdemPalavrasSize() > 0) {
                        return jogador.getProxima();
                    } else {
                        return null;
                    }
                } else {
                    if (dupla.getCont() == dupla.getMaxCont()){
                        checkParear(atividade);
                        return null;
                    } else if (dupla.getOrdemPalavrasSize() > 0){
                            return jogadores.get(jogadores.get(p.getId()).getIdDupla()).getTraducao();
                        } else {
                        return null;
                    }
                    }

            default:
                System.out.printf("Errou");
                return "Default";
        }
    }

    public String traducaoFrase(Atividade atividade, Long idPessoa){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        Pessoa p = pessoaService.findById(idPessoa);
        if (p == null) {
            return null;
        }
        return jogadores.get(p.getId()).getTraducao();
    }

    public int responder(Atividade atividade, Long pessoa, int resposta) {
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        checkPareou(atividade);
        Jogador j = jogadores.get(pessoa);
        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                double avaliacao;
                switch (resposta) {
                    case 1: //Facil
                        avaliacao = 0.1;
                        break;
                    case 2: //Medio
                        avaliacao = 0.01;
                        break;
                    case 3: //Dificil
                        avaliacao = 0.001;
                        break;
                    default:
                        return 0;
                }
                pessoaPalavraService.setAvaliacao(pessoaService.findById(pessoa), j.getPalavra(), avaliacao);
                if (j.removeRespondida() > 0) {
                    return 1; //Ainda tem palavras
                }
                return 2; // Acabou a lista
            case "Tradução":
                Jogador dupla = jogadores.get(j.getIdDupla());
                switch (resposta){
                    case 0:
                        break;
                    case 1:
                        dupla.setPontos();
                        break;
                }
                dupla.setCont();
                dupla.setMinhaVez(Boolean.FALSE);
                j.setMinhaVez(Boolean.TRUE);
                dupla.removeRespondida();
                if ((dupla.getOrdemPalavrasSize() > 0) || (j.getOrdemPalavrasSize() > 0)){
                    return 1;
                }
                return 2;
        }
        return 0;
    }

    public synchronized void emparelhar(Atividade atividade){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        List<Long> players = new ArrayList<Long>(jogadores.keySet());
        List<String> cores = getCores();
        Collections.shuffle(cores);
        Collections.shuffle(players);
        while (players.size() != 0){
            System.out.printf("Par = " + players.size() + "\n");
            jogadores.get(players.get(0)).setIdDupla(players.get(1));
            jogadores.get(players.get(0)).setMinhaVez(Boolean.TRUE);
            jogadores.get(players.get(0)).setCor(cores.get(0));

            jogadores.get(players.get(1)).setIdDupla(players.get(0));
            jogadores.get(players.get(1)).setCor(cores.get(0));
            jogadores.get(players.get(1)).setMinhaVez(Boolean.FALSE);
            cores.remove(0);
            players.remove(1);
            players.remove(0);
        }
        atividade.setParear(0);
        atividade.setPareou(1);
        salvar(atividade);
    }

    public String reemparelhar(Atividade atividade, Long idPessoa){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        int esperando = 0;
        if (atividade.getParear() == 1) {
            List<Long> players = new ArrayList<Long>(jogadores.keySet());
            for (Long id : players) {
                if (jogadores.get(id).getCont() != MAX_CONT) {
                    System.out.printf("esperando = " + id + " \n");
                    esperando = 1;
                }
            }
            if (esperando == 0) {
                if (atividade.getParear() == 1) {
                    System.out.printf("Parear = " + atividade.getParear() + " " + System.currentTimeMillis() + " \n");
                    atividade.setParear(0);
                    salvar(atividade);
                    System.out.printf("Parear = " + atividade.getParear() + " " + System.currentTimeMillis() + " \n");
                    emparelhar(atividade);
                }
            } else {
                return null;
            }
        }
        if (atividade.getPareou() == 1) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jogadores.get(idPessoa).setCont(0);
            System.out.printf("Pareou " + atividade.getPareou());
            return jogadores.get(idPessoa).getCor();
        }
        return null;
    }

    public List<String> getCores(){
        List<String> c = new ArrayList<>();
        c.add("Alpha");
        c.add("Bravo");
        c.add("Charlie");
        c.add("Delta");
        c.add("Echo");
        c.add("Foxtrot");
        c.add("Golf");
        c.add("Hotel");
        c.add("India");
        c.add("Juliet");
        c.add("Kilo");
        c.add("Lima");
        c.add("Mike");
        c.add("November");
        c.add("Oscar");
        c.add("Papa");
        c.add("Quebec");
        c.add("Romeo");
        c.add("Sierra");
        c.add("Tango");
        c.add("Uniform");
        return c;
    }

    public int minhaVez(Atividade atividade, Long idPessoa){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        Jogador j = jogadores.get(idPessoa);
        Jogador dupla = jogadores.get(j.getIdDupla());
        if ((j.getCont() ==j.getMaxCont()) && (dupla.getMaxCont() == dupla.getCont())){
            return 2;
        } else if (jogadores.get(idPessoa).isMinhaVez()){
            return 1;
        } else {
            return 0;
        }
    }

    public boolean fimDaLista(Atividade atividade, Long idPessoa){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        if (jogadores.get(idPessoa).getOrdemPalavrasSize()> 0){
            return false;
        } else{
            return true;
        }
    }

    public void checkParear(Atividade atividade){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        List<Long> players = new ArrayList<Long>(jogadores.keySet());
        boolean esperando = true;
        for (Long id : players) {
            if (jogadores.get(id).getCont() != MAX_CONT) {
                esperando = false;
            }
        }
        if(esperando){
            atividade.setParear(1);
            salvar(atividade);
        }
    }

    public void checkPareou(Atividade atividade){
        Map<Long, Jogador> jogadores;
        if (atividades.get(atividade.getPin())== null) {
            jogadores = new HashMap<Long, Jogador>();
        } else {
            jogadores = atividades.get(atividade.getPin());
        }
        List<Long> players = new ArrayList<Long>(jogadores.keySet());
        boolean esperando = true;
        for (Long id : players) {
            if (jogadores.get(id).getCont() != 0) {
                esperando = false;
            }
        }
        if(esperando){
            atividade.setPareou(0);
            salvar(atividade);
        }
    }
}
