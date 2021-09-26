package com.staging.stack.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staging.stack.models.User;
import com.staging.stack.repository.EngineerRepository;
import com.staging.stack.repository.UserRepository;
import com.staging.stack.security.WebSecurityConfig;
import com.staging.stack.models.Engineer;
import static  org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.junit.Before;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
@EnableAutoConfiguration( exclude = {WebSecurityConfig.class})
class EngineerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Mock
    private UserRepository userRepository;
     private EngineerRepository engineerRepository = Mockito.mock(EngineerRepository.class);

    @InjectMocks
    private EngineerController engineerController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(engineerController).build();

        this.objectMapper = new ObjectMapper();
    }



    @Test
    void getAllEngineers_Success() throws Exception{

        Engineer e1 = new Engineer(1, 1, "Eng1");
        Engineer e2 = new Engineer(2, 2, "Eng2");

        List<Engineer> engineerList = new ArrayList<>(Arrays.asList(e1, e2));

        Mockito.when(engineerRepository.findAll()).thenReturn(engineerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/engineers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void getAllEngineers_Failure() throws Exception{



        List<Engineer> engineerList = new ArrayList<>();

        Mockito.when(engineerRepository.findAll()).thenReturn(engineerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/engineers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
    @Test
    void getEngineerByEngineerId_Success() throws Exception{
        Engineer e1 = new Engineer(1, 1, "Eng1");

        Mockito.when(engineerRepository.findByEngineerId(e1.getengineerId())).thenReturn(Optional.of(e1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/engineers/{engineerId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.engineerName").value("Eng1"))
                .andDo(print());


    }

    @Test
    void getEngineerByEngineerId_Failure() throws Exception{
        Engineer e1 = new Engineer(1, 1, "Eng1");

        Mockito.when(engineerRepository.findByEngineerId(e1.getengineerId())).thenReturn(Optional.of(e1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/engineers/{engineerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());



    }

    @Test
    void addEngineerTest_success() throws Exception{

        User u1 = new User("Eng1", "eng@gmail.com", "yashu2090");
        Engineer e1 = new Engineer(1, 1, "Eng1");

        Mockito.when(userRepository.findById(e1.getengineerId())).thenReturn(Optional.of(u1));
        Mockito.when(engineerRepository.save(e1)).thenReturn(e1);


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/engineers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(e1));

        mockMvc.perform(mockRequest).andExpect(status().isCreated());

    }


    @Test
    void addEngineerTest_Failure() throws Exception{


        Engineer e1 = new Engineer(2, 2, "Eng2");


        Mockito.when(engineerRepository.save(e1)).thenReturn(e1);


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/engineers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(e1));

        mockMvc.perform(mockRequest).andExpect(status().isNotAcceptable());

    }

    @Test
    void updateEngineer() {
    }

    @Test
    void deleteEngineerTest_success() throws Exception{
        Engineer e1 = new Engineer(2, 2, "Eng2");

        Mockito.when(engineerRepository.findByEngineerId(e1.getengineerId())).thenReturn(Optional.of(e1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/test/engineers/{engineerId}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void deleteEngineerTest_Failure() throws Exception{
        Engineer e1 = new Engineer(2, 2, "Eng2");

        Mockito.when(engineerRepository.findByEngineerId(e1.getengineerId())).thenReturn(Optional.of(e1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/test/engineers/{engineerId}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    void deleteAllEngineersTest_success() throws Exception{


        Engineer e1 = new Engineer(1, 1, "Eng1");
        Engineer e2 = new Engineer(2, 2, "Eng2");

        List<Engineer> engineerList = new ArrayList<>(Arrays.asList(e1, e2));

        Mockito.when(engineerRepository.findAll()).thenReturn(engineerList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/test/engineers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }
}