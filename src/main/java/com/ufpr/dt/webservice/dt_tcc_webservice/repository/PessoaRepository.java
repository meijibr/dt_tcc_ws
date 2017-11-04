package com.ufpr.dt.webservice.dt_tcc_webservice.repository;



import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Pessoa findByEmail(String email);

    Pessoa findById(Long id);

}
