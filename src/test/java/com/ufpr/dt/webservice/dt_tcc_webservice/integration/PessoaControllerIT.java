package com.ufpr.dt.webservice.dt_tcc_webservice.integration;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Pessoa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application.properties")
public class PessoaControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private List<String> sanitizeIds = new ArrayList<>();

    @Test
    public void contextLoads() {
    }

    @After
    public void sanitize() {
        sanitizeIds.forEach(item ->restTemplate.delete(item));
    }

    @Test
    public void createPessoaTest() {
        Pessoa pessoa = getPessoa();

        String uri =  "/pessoas/";
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, pessoa, Void.class);

        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        sanitizeIds.add(responseEntity.getHeaders().getLocation().getPath());
    }

    @Test
    public void duplicatePessoaTest() {
        Pessoa pessoa = getPessoa();

        String uri =  "/pessoas/";
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, pessoa, Void.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        sanitizeIds.add(responseEntity.getHeaders().getLocation().getPath());

        ResponseEntity<Void> responseEntity2 = restTemplate.postForEntity(uri, pessoa, Void.class);
        Assert.assertEquals(HttpStatus.CONFLICT, responseEntity2.getStatusCode());
    }

    @Test
    public void updatePessoa() {
        Pessoa pessoa = getPessoa();

        String uri =  "/pessoas/";
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, pessoa, Void.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        String patch = responseEntity.getHeaders().getLocation().getPath();
        sanitizeIds.add(patch);

        pessoa.setNome("Fulano");
        restTemplate.put(patch, pessoa);

        HttpEntity<Pessoa> requestEntity = new HttpEntity<>(pessoa);
        ResponseEntity<Pessoa> responseEntity2  = restTemplate.exchange(patch, HttpMethod.PUT, requestEntity, Pessoa.class);
        Assert.assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        Assert.assertEquals("Fulano", responseEntity2.getBody().getNome());
    }

    @Test
    public void duplicatePessoaUpdate() {
        Pessoa pessoa = getPessoa();

        String uri =  "/pessoas/";
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, pessoa, Void.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        String patch = responseEntity.getHeaders().getLocation().getPath();
        sanitizeIds.add(patch);

        Pessoa pessoa2 = getPessoa();
        pessoa2.setEmail("outro_email@teste.com");
        ResponseEntity<Void> responseEntity2 = restTemplate.postForEntity(uri, pessoa2, Void.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity2.getStatusCode());
        patch = responseEntity2.getHeaders().getLocation().getPath();
        sanitizeIds.add(patch);

        pessoa2.setEmail(pessoa.getEmail());
        HttpEntity<Pessoa> requestEntity = new HttpEntity<>(pessoa2);
        ResponseEntity<Pessoa> responseEntity3  = restTemplate.exchange(patch, HttpMethod.PUT, requestEntity, Pessoa.class);
        Assert.assertEquals(HttpStatus.CONFLICT, responseEntity3.getStatusCode());
    }

    private Pessoa getPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Pessoa");
        pessoa.setSobrenome("Teste");
        pessoa.setSenha("teste1234");
        pessoa.setEmail("pessoa@teste.com.br");
        return pessoa;
    }

}
