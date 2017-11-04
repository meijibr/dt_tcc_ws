package com.ufpr.dt.webservice.dt_tcc_webservice.repository;



import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    Atividade findByPin(Long id);

    Atividade findById(Long id);
}
