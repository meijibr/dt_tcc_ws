package com.ufpr.dt.webservice.dt_tcc_webservice.dto;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.PalavraFrase;

import java.util.List;

public class Jogador {
    private Long idDupla;
    private List<FraseAvaliacao> ordemPalavras;
    private int pontos;
    private boolean minhaVez;
    private Long pin;

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

    public void setPontos(int pontos) {
        this.pontos = pontos;
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
}
