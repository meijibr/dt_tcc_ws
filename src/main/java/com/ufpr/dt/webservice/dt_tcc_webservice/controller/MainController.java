package com.ufpr.dt.webservice.dt_tcc_webservice.controller;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import com.ufpr.dt.webservice.dt_tcc_webservice.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    MainService mainService;

    //------------------- Login a User --------------------------------------------------------

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public ResponseEntity<String> loginUser(@RequestParam("emailPessoa") String email, @RequestParam("passPessoa") String senha) {
        System.out.println("Login in " + email + " " + senha);

        Pessoa p = mainService.findByEmail(email);
        if (p == null) {
            System.out.println("User with login: " + email + " not found");
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        if (mainService.login(p, senha)) {
            return new ResponseEntity<String>(p.getId().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
    }
}
