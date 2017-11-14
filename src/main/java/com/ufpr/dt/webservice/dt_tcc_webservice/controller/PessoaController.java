package com.ufpr.dt.webservice.dt_tcc_webservice.controller;
import java.util.List;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PessoaController {

    @Autowired
    PessoaService pessoaService;  //Service which will do all data retrieval/manipulation work


    //-------------------Retrieve All Users--------------------------------------------------------

    @RequestMapping(value = "/pessoa/", method = RequestMethod.GET)
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaService.mostrarTodos();
        if(pessoas.isEmpty()){
            return new ResponseEntity<List<Pessoa>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Pessoa>>(pessoas, HttpStatus.OK);
    }


    //-------------------Retrieve Single User--------------------------------------------------------

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pessoa> getPessoa(@PathVariable("id") long id) {
        System.out.println("Fetching User with id " + id);
        Pessoa pessoa = pessoaService.findById(id);
        if (pessoa == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Pessoa>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
    }



    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(value = "/pessoas/", method = RequestMethod.POST)
    public ResponseEntity<Void> createPessoa(@RequestBody Pessoa pessoa, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + pessoa.getNome());

        if (pessoaService.repetido(pessoa)) {
            System.out.println("A User with name " + pessoa.getNome() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        pessoaService.salvar(pessoa);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/pessoa/{id}").buildAndExpand(pessoa.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Pessoa> updateUser(@PathVariable("id") long id, @RequestBody Pessoa pessoaAtualizada) {
        System.out.println("Updating User " + id);

        Pessoa pessoaAtual = pessoaService.findById(id);

        if (pessoaAtual==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Pessoa>(HttpStatus.NOT_FOUND);
        }
        if (pessoaService.repetidoUpdate(pessoaAtual, pessoaAtualizada)) {
            System.out.println("\nA User with email " + pessoaAtualizada.getEmail() + " already exist");
            return new ResponseEntity<Pessoa>(HttpStatus.CONFLICT);
        }
        pessoaAtual.setNome(pessoaAtualizada.getNome());
        pessoaAtual.setSobrenome(pessoaAtualizada.getSobrenome());
        pessoaAtual.setSenha(pessoaAtualizada.getSenha());
        pessoaAtual.setEmail(pessoaAtualizada.getEmail());

        pessoaService.salvar(pessoaAtual);
        return new ResponseEntity<Pessoa>(pessoaAtual, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Pessoa> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);

        Pessoa user = pessoaService.findById(id);
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<Pessoa>(HttpStatus.NOT_FOUND);
        }

        pessoaService.delete(id);
        return new ResponseEntity<Pessoa>(HttpStatus.NO_CONTENT);
    }
}