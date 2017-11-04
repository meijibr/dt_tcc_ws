package com.ufpr.dt.webservice.dt_tcc_webservice.service;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.repository.TipoAtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoAtividadeService {

    @Autowired
    TipoAtividadeRepository tipoAtividadeRepository;

    public TipoAtividade findById(Long id) {
        return tipoAtividadeRepository.findById(id);
    }
}
