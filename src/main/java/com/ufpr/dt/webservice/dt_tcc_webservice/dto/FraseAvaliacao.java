package com.ufpr.dt.webservice.dt_tcc_webservice.dto;

public class FraseAvaliacao implements Comparable<FraseAvaliacao>{
    private Double avaliacao;
    private String frase;
    private String fraseTraduzida;

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getFraseTraduzida() {
        return fraseTraduzida;
    }

    public void setFraseTraduzida(String fraseTraduzida) {
        this.fraseTraduzida = fraseTraduzida;
    }

    @Override
    public int compareTo(FraseAvaliacao o) {
        if (this.getAvaliacao() < o.getAvaliacao()){
            return -1;
        }
        if (this.getAvaliacao() > o.getAvaliacao()){
            return 1;
        }
        return 0;
    }
}
