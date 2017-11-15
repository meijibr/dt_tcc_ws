package com.ufpr.dt.webservice.dt_tcc_webservice.controller;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Atividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.service.AtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class AtividadeController {


    @Autowired
    AtividadeService atividadeService;  //Service which will do all data retrieval/manipulation work


    //-------------------Retrieve All Atividades--------------------------------------------------------

    @RequestMapping(value = "/atividade/", method = RequestMethod.GET)
    public ResponseEntity<List<Atividade>> listarAtividades() {
        List<Atividade> atividades = atividadeService.mostrarTodos();
        if(atividades.isEmpty()){
            return new ResponseEntity<List<Atividade>>(HttpStatus.NOT_FOUND);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Atividade>>(atividades, HttpStatus.OK);
    }


    //-------------------Retrieve Single Atividade--------------------------------------------------------

    @RequestMapping(value = "/atividade/{pin}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Atividade> getAtividade(@PathVariable("pin") Long pin) {
        System.out.println("Fetching Atividade with pin " + pin);
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null) {
            System.out.println("Atividade with pin " + pin + " not found");
            return new ResponseEntity<Atividade>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Atividade>(atividade, HttpStatus.OK);
    }



    //-------------------Create a Atividade--------------------------------------------------------

    @RequestMapping(value = "/atividade/criar", method = RequestMethod.POST)
    public ResponseEntity<Atividade> createAtividade(@RequestParam("lista") Long idLista, @RequestParam("tipoAtividade") Long idTipoAtividade) {
        System.out.println("Creating Atividade ");
        Atividade atividade = atividadeService.iniciarAtividade(idLista,idTipoAtividade);
        atividadeService.salvar(atividade);

        return new ResponseEntity<Atividade>(atividade, HttpStatus.CREATED);
    }

    //------------------- Inserindo Pessoa na Atividade -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/entrar", method = RequestMethod.POST)
    public ResponseEntity<String> createAtividade(@RequestParam("pessoa") Long idPessoa, @PathVariable("pin") long pin) {
        System.out.println("Entrando na Atividade ");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        if (atividade.getEstado().equals("Aguardando")) {
            atividadeService.adicionarPessoa(atividade, idPessoa);
            return new ResponseEntity<String>(atividade.getTipoAtividade().getAtividade(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
    }

    //------------------- Iniciar a Atividade -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/iniciar", method = RequestMethod.POST)
    public ResponseEntity<String> startAtividade(@PathVariable("pin") long pin) {
        System.out.println("Iniciando a Atividade ");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if ((atividade.getEstado().equals("Aguardando"))||(atividade.getEstado().equals("Impar"))) {
            atividadeService.start(atividade);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //------------------- Esperar Inicio -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/pronto", method = RequestMethod.POST)
    public ResponseEntity<Atividade> esperarAtividade(@PathVariable("pin") long pin) throws InterruptedException {
        System.out.println("Iniciando a Atividade ");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<Atividade>(HttpStatus.NOT_FOUND);
        }
        atividade = atividadeService.aguardarInicio(atividade);
        if (atividade == null){
            return new ResponseEntity<Atividade>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Atividade>(atividade, HttpStatus.OK);
    }

    //------------------- Proxima Frase -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/proxima", method = RequestMethod.POST)
    public ResponseEntity<String> proximaFrase(@RequestParam Long pessoa, @PathVariable("pin") long pin) throws InterruptedException {
        System.out.println("Pegar proxima frase");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String frase = atividadeService.proximaFrase(atividade, pessoa);
        if (frase == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(frase, HttpStatus.OK);
        }
    }
    //------------------- Traducao Frase -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/traducao", method = RequestMethod.POST)
    public ResponseEntity<String> traducaoFrase(@RequestParam Long pessoa, @PathVariable("pin") long pin) throws InterruptedException {
        System.out.println("Pegar traducao da frase atual");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String frase = atividadeService.traducaoFrase(atividade, pessoa);
        if (frase == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(frase, HttpStatus.OK);
        }
    }

    //------------------- Responder frase -------------------------------------------

    @RequestMapping(value = "/atividade/{pin}/responder", method = RequestMethod.POST)
    public ResponseEntity<String> respostaFrase(@RequestParam Long pessoa, @RequestParam int resposta, @PathVariable("pin") long pin) throws InterruptedException {
        System.out.println("Resgistrar resposta");
        Atividade atividade = atividadeService.findByPin(pin);
        if (atividade == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        switch (atividadeService.responder(atividade, pessoa,resposta)){
            case 0:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            case 1:
                return new ResponseEntity<>(HttpStatus.OK);
            case 2:
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            default:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    //------------------- Update a User --------------------------------------------------------

//    @RequestMapping(value = "/atividade/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Atividade> updateUser(@PathVariable("id") long id, @RequestBody Atividade pessoa) {
//        System.out.println("Updating User " + id);
//
//        Atividade atividadeAtual = atividadeService.findById(id);
//
//        if (atividadeAtual==null) {
//            System.out.println("User with id " + id + " not found");
//            return new ResponseEntity<Atividade>(HttpStatus.NOT_FOUND);
//        }
//
//
//        atividadeAtual.setNome(pessoa.getNome());
//        atividadeAtual.setSobrenome(pessoa.getSobrenome());
//        atividadeAtual.setSenha(pessoa.getSenha());
//        atividadeAtual.setEmail(pessoa.getEmail());
//
//        atividadeService.salvar(atividadeAtual);
//        return new ResponseEntity<Pessoa>(atividadeAtual, HttpStatus.OK);
//    }

    //------------------- Delete a User --------------------------------------------------------
//
//    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Pessoa> deleteUser(@PathVariable("id") long id) {
//        System.out.println("Fetching & Deleting User with id " + id);
//
//        Pessoa user = atividadeService.findById(id);
//        if (user == null) {
//            System.out.println("Unable to delete. User with id " + id + " not found");
//            return new ResponseEntity<Pessoa>(HttpStatus.NOT_FOUND);
//        }
//
//        atividadeService.delete(id);
//        return new ResponseEntity<Pessoa>(HttpStatus.NO_CONTENT);
//    }
}
