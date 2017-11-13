package com.ufpr.dt.webservice.dt_tcc_webservice.repository;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Palavra;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.PessoaPalavra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PessoaPalavraRepository extends JpaRepository<PessoaPalavra, Long> {

    PessoaPalavra findById(Long id);

    PessoaPalavra findByPessoaAndPalavra(Pessoa pessoa, Palavra palavra);
}
