package com.ufpr.dt.webservice.dt_tcc_webservice.test.controller;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.service.TipoAtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/test")
@Profile({"integration"})
public class CreateController {

    @Autowired
    TipoAtividadeService tipoAtividadeService;

    //-------------------Create a Atividade--------------------------------------------------------

    @RequestMapping(value = "/tipoatividade", method = RequestMethod.POST)
    public ResponseEntity<TipoAtividade> createTipoAtividade(@RequestBody TipoAtividade tipoAtividade, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Tipo Atividade ");
        TipoAtividade tipoAtividade1 = tipoAtividadeService.save(tipoAtividade);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/pessoa/{id}").buildAndExpand(tipoAtividade1.getId()).toUri());
        return new ResponseEntity<TipoAtividade>(tipoAtividade1, headers, HttpStatus.CREATED);
    }

}
