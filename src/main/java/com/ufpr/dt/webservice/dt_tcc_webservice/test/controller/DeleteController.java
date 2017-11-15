package com.ufpr.dt.webservice.dt_tcc_webservice.test.controller;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.service.TipoAtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@Profile({"integration"})
public class DeleteController {

    @Autowired
    TipoAtividadeService tipoAtividadeService;

    //------------------- Delete a tipo atividade --------------------------------------------------------

    @RequestMapping(value = "/tipoatividade/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TipoAtividade> deleteTipoAtividade(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);

        TipoAtividade tipoAtividade = tipoAtividadeService.findById(id);
        if (tipoAtividade == null) {
            System.out.println("Unable to delete. TipoAtividade with id " + id + " not found");
            return new ResponseEntity<TipoAtividade>(HttpStatus.NOT_FOUND);
        }

        tipoAtividadeService.delete(id);
        return new ResponseEntity<TipoAtividade>(HttpStatus.NO_CONTENT);
    }
}
