package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.google.gson.Gson;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.FraseAvaliacao;
import com.ufpr.dt.webservice.dt_tcc_webservice.dto.Jogador;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.*;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AtividadeService {

    private static final int MAX_CONT = 4;
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
        atividade.setParear(1);
        atividade.setPareou(0);
        salvar(atividade);
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
            jogadores.get(p.getId()).setMaxCont(MAX_CONT);
            jogadores.get(p.getId()).setCont(MAX_CONT);

        }

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                break;
            case "Tradução":
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
        Pessoa p = pessoaService.findById(idPessoa);

        switch (atividade.getTipoAtividade().getAtividade()) {
            case "Revisão":
                return jogadores.get(p.getId()).getProxima();
            case "Tradução":
                    System.out.printf("\nNome" + p.getNome());
                    System.out.printf(" Cont" + jogadores.get(p.getId()).getCont() + " "+jogadores.get(p.getId()).getMaxCont());
                    System.out.printf(" Dupla" + pessoaService.findById(jogadores.get(p.getId()).getIdDupla()).getNome());
                    System.out.printf(" CDupla" + jogadores.get(jogadores.get(p.getId()).getIdDupla()).getCont() + " " + jogadores.get(jogadores.get(p.getId()).getIdDupla()).getMaxCont() + "\n");
                    if ((jogadores.get(p.getId()).getCont() == jogadores.get(p.getId()).getMaxCont()) && (jogadores.get(jogadores.get(p.getId()).getIdDupla()).getMaxCont() == jogadores.get(jogadores.get(p.getId()).getIdDupla()).getCont())) {
                        if(jogadores.get(p.getId()).isMinhaVez()){
                            atividade.setParear(1);
                            atividade.setPareou(0);
                            salvar(atividade);
                            return null;
                        } else {
                            return jogadores.get(jogadores.get(p.getId()).getIdDupla()).getTraducao();
                        }
                    } else {
                        if (jogadores.get(p.getId()).isMinhaVez()) {
                            if (jogadores.get(idPessoa).getOrdemPalavrasSize() > 0) {
                                return jogadores.get(p.getId()).getProxima();
                            } else {
                                return null;
                            }
                        } else {
                            return jogadores.get(jogadores.get(p.getId()).getIdDupla()).getTraducao();
                        }
                    }

            default:
                System.out.printf("Errou");
                return "Default";
        }
    }

    public String traducaoFrase(Atividade atividade, Long idPessoa){
        Pessoa p = pessoaService.findById(idPessoa);
        if (p == null) {
            return null;
        }
        return jogadores.get(p.getId()).getTraducao();
    }

    public int responder(Atividade atividade, Long pessoa, int resposta) {
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
        List<Long> players = new ArrayList<Long>(jogadores.keySet());
        List<String> cores = getCores();
        Collections.shuffle(cores);
        Collections.shuffle(players);
        while (players.size() != 0){
            System.out.printf("Par = " + players.size() + "\n");
            jogadores.get(players.get(0)).setIdDupla(players.get(1));
            jogadores.get(players.get(0)).setMinhaVez(Boolean.TRUE);
//            System.out.printf("jogador = " + pessoaService.findById(players.get(0)).getNome() + "\n");
//            System.out.printf("dupla = " + pessoaService.findById(players.get(1)).getNome() + "\n");
//            System.out.printf("cor = " + cores.get(0) + "\n");
            jogadores.get(players.get(0)).setCor(cores.get(0));
            jogadores.get(players.get(1)).setIdDupla(players.get(0));
            jogadores.get(players.get(1)).setCor(cores.get(0));
            jogadores.get(players.get(1)).setMinhaVez(Boolean.FALSE);
//            System.out.printf("jogador = " + pessoaService.findById(players.get(0)).getNome() + "\n");
//            System.out.printf("dupla = " + pessoaService.findById(players.get(1)).getNome() + "\n");
//            System.out.printf("cor = " + cores.get(0) + "\n");
            jogadores.get(players.get(0)).setCont(0);
            jogadores.get(players.get(1)).setCont(0);
            cores.remove(0);
            players.remove(1);
            players.remove(0);
        }

        atividade.setParear(0);
        atividade.setPareou(1);
        salvar(atividade);
    }

    public String reemparelhar(Atividade atividade, Long idPessoa){
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

    public int minhaVez(Long idPessoa){
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

    public boolean fimDaLista(Long idPessoa){
        if (jogadores.get(idPessoa).getOrdemPalavrasSize()> 0){
            return false;
        } else{
            return true;
        }
    }
}
