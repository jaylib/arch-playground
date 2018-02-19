package com.breuninger.arch.playground.example.web;

import com.breuninger.arch.playground.example.domain.Example;
import com.breuninger.arch.playground.example.service.ExampleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ExampleRestControllerTests {
  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @SpyBean
  ExampleService exampleService;

  @Test
  public void create() throws Exception {
    createExample(new Example("", "an example", new Date(), new Date()));
    Mockito.verify(exampleService).create(Mockito.any(Example.class));
  }

  @Test
  public void findAll() throws Exception {
    createExample(new Example("", "an example", new Date(), new Date()));

    String result = mvc.perform(
      MockMvcRequestBuilders.get("/examples")
    ).andDo(print()).
      andReturn().getResponse().getContentAsString();

    List<Example> output = mapper.readValue(result, new TypeReference<List<Example>>() {
    });
    assertTrue(output.size() >= 1);
  }

  private void createExample(Example example) throws Exception {
    mvc.perform(
      post("/examples")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(mapper.writeValueAsString(example))
    ).andExpect(status().isCreated());
  }
}
