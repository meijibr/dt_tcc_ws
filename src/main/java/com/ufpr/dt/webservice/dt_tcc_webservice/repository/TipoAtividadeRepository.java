package com.ufpr.dt.webservice.dt_tcc_webservice.repository;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Lista;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoAtividadeRepository extends JpaRepository<TipoAtividade, Long>{

    TipoAtividade findById(Long id);
}
