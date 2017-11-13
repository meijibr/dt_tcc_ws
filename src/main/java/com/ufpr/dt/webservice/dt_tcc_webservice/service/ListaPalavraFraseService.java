package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Lista;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.ListaPalavraFrase;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.PalavraFrase;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.ListaPalavraFraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListaPalavraFraseService {

    @Autowired
    ListaPalavraFraseRepository listaPalavraFraseRepository;

    public List<PalavraFrase> getListPalavraFrase(Lista lista){
        List<ListaPalavraFrase> listaPalavraFrase = listaPalavraFraseRepository.findByLista(lista);
        List<PalavraFrase> frases = new ArrayList<PalavraFrase>();
        for (int i=0; i < listaPalavraFrase.size(); i++){
            frases.add(listaPalavraFrase.get(i).getPalavraFrase());
        }
        return frases;
    }
}
