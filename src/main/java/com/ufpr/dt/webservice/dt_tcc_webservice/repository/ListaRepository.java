package com.ufpr.dt.webservice.dt_tcc_webservice.repository;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Lista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaRepository extends JpaRepository<Lista, Long>{

    Lista findById(Long id);
}
