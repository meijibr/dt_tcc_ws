package com.ufpr.dt.webservice.dt_tcc_webservice.dto;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Palavra;

import java.util.List;

public class Jogador {
    private Long idDupla;
    private List<FraseAvaliacao> ordemPalavras;
    private int pontos;
    private boolean minhaVez;
    private Long pin;
    private int cont;
    private int maxCont;
    private volatile String cor;

    public Jogador(Long pin) {
        this.pin = pin;
        this.pontos = 0;
    }

    public Long getIdDupla() {
        return idDupla;
    }

    public void setIdDupla(Long idDupla) {
        this.idDupla = idDupla;
    }

    public List<FraseAvaliacao> getOrdemPalavras() {
        return ordemPalavras;
    }

    public void setOrdemPalavras(List<FraseAvaliacao> ordemPalavras) {
        this.ordemPalavras = ordemPalavras;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos() {
        this.pontos++;
    }

    public boolean isMinhaVez() {
        return minhaVez;
    }

    public void setMinhaVez(boolean minhaVez) {
        this.minhaVez = minhaVez;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }

    public String getProxima() {
        return this.ordemPalavras.get(0).getFrase();
    }

    public Palavra getPalavra(){
        return this.ordemPalavras.get(0).getPalavra();
    }

    public String getTraducao() {
        return this.ordemPalavras.get(0).getFraseTraduzida();
    }

    public int removeRespondida() {
        this.ordemPalavras.remove(0);
        return this.ordemPalavras.size();
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public void setCont() {
        this.cont++;
    }

    public int getMaxCont() {
        return maxCont;
    }

    public void setMaxCont(int maxCont) {
        this.maxCont = maxCont;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getOrdemPalavrasSize(){
        return this.ordemPalavras.size();
    }
}
