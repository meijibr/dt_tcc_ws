package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Lista;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListaService {

    @Autowired
    ListaRepository listaRepository;

    public Lista findById(Long id) {
        return listaRepository.findById(id);
    }
}
