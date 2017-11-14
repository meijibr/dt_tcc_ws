package com.ufpr.dt.webservice.dt_tcc_webservice.integration;

import com.ufpr.dt.webservice.dt_tcc_webservice.entity.Atividade;
import com.ufpr.dt.webservice.dt_tcc_webservice.entity.TipoAtividade;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application.properties")
@ActiveProfiles("integration")
public class AtividadeControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private List<String> sanitizeIds = new ArrayList<>();

    @Before
    public void init() {
        TipoAtividade tipoAtividade = new TipoAtividade();
        tipoAtividade.setAtividade("Aula Teste");
        String tipoAtividadeURI = "/test/tipoatividade";

        ResponseEntity<TipoAtividade> responseEntity = restTemplate.postForEntity(tipoAtividadeURI, tipoAtividade, TipoAtividade.class);
        Assert.assertEquals( HttpStatus.CREATED , responseEntity.getStatusCode());
        sanitizeIds.add(responseEntity.getHeaders().getLocation().getPath());


    }

    @After
    public void sanitize() {
        sanitizeIds.forEach(item ->restTemplate.delete(item));
    }

    @Test
    public void createAtividade() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("lista", "1");
        map.add("tipoAtividade", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String uri =  "/atividade/criar";
        ResponseEntity<Atividade> responseEntity = restTemplate.postForEntity( uri, request , Atividade.class);
        Assert.assertEquals( HttpStatus.CREATED , responseEntity.getStatusCode());
        sanitizeIds.add(responseEntity.getHeaders().getLocation().getPath());

    }


}
