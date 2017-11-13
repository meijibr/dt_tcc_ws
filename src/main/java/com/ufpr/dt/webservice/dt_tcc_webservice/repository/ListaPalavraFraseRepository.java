package com.ufpr.dt.webservice.dt_tcc_webservice.repository;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Lista;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.ListaPalavraFrase;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.PalavraFrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaPalavraFraseRepository extends JpaRepository<ListaPalavraFrase, Long> {

    List<ListaPalavraFrase> findByLista(Lista lista);
}
