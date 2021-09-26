package com.staging.stack.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staging.stack.models.Engineer;
import com.staging.stack.repository.EngineerRepository;
import org.junit.jupiter.api.Test;

import com.staging.stack.models.History;
import com.staging.stack.repository.HistoryRepository;
import com.staging.stack.controllers.HistoryController;

import com.staging.stack.repository.UserRepository;
import com.staging.stack.security.WebSecurityConfig;


import com.staging.stack.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.staging.stack.models.Instance;


import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.staging.stack.repository.InstanceRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.TestContext;
import org.mortbay.jetty.webapp.WebAppContext;
import static  org.hamcrest.Matchers.hasSize;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
@EnableAutoConfiguration( exclude = {WebSecurityConfig.class})
class HistoryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private InstanceRepository instanceRepository;

    @Mock
    private EngineerRepository engineerRepository;

    @InjectMocks
    private HistoryController historyController;

    History h1 = new History(1, 1, "Eng1", "I am using", 2L);
    History h2 = new History(2, 1, "Eng1", "I am using", 2L);
    History h3 = new History(3, 2, "Eng2", "I am using", 3L);


    HistoryControllerTest() throws Exception {
    }


    @BeforeEach
    public  void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(historyController).build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void addEngineer_success() throws Exception{

        History history = new History(1, 1, "Eng1", "I am using", 1L);
        Instance ins1 = new Instance(1, "Inst1", 123, "Free");
        Engineer e1 = new Engineer(1, 1,  "Eng1");
        Mockito.when(engineerRepository.findByEngineerId(history.getEngineerId())).thenReturn(Optional.of(e1));
        Mockito.when(instanceRepository.findById(history.getInstanceId())).thenReturn(Optional.of(ins1));
        Mockito.when(historyRepository.save(history)).thenReturn(history);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/instances/{id}/assign", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(this.objectMapper.writeValueAsString(h1));

        mockMvc.perform(mockRequest).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addEngineer_Failure() throws Exception{

        History history = new History(1, 1, "Eng1", "I am using", 1L);
        Instance ins1 = new Instance(1, "Inst1", 123, "InUse");
        Engineer e1 = new Engineer(1, 1,  "Eng1");
        Engineer e2 = new Engineer(2, 2,  "Eng2");
        Mockito.when(engineerRepository.findByEngineerId(history.getEngineerId())).thenReturn(Optional.of(e1));
        Mockito.when(instanceRepository.findById(history.getInstanceId())).thenReturn(Optional.of(ins1));
        Mockito.when(historyRepository.save(history)).thenReturn(history);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/instances/{id}/assign", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(this.objectMapper.writeValueAsString(h1));

        //InUse State
        mockMvc.perform(mockRequest).andDo(print())
                .andExpect(status().isDestinationLocked());


    }

    @Test
    void addEngineer_EngineerNotAuthorized() throws Exception{

        History history = new History(1, 1, "Eng1", "I am using", 1L);
        Instance ins1 = new Instance(1, "Inst1", 123, "InUse");
        //Engineer is not Authorized

        Mockito.when(instanceRepository.findById(history.getInstanceId())).thenReturn(Optional.of(ins1));
        Mockito.when(historyRepository.save(history)).thenReturn(history);
        MockHttpServletRequestBuilder mockRequest1 = MockMvcRequestBuilders.post("/api/test/instances/{id}/assign", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(this.objectMapper.writeValueAsString(h1));
        mockMvc.perform(mockRequest1).andExpect(status().isInternalServerError());

    }

    @Test
    void getAllByEngineerId_success() throws Exception{

      List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));

      Mockito.when(historyRepository.findAllByEngineerIdOrderByLifeTimeDesc(h1.getEngineerId())).thenReturn(histories);

      mockMvc.perform(MockMvcRequestBuilders
              .get("/api/test/profile/{engineerId}", 1)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andDo(print())
              .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void getAllByEngineerId_Failure() throws Exception{

        List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));

        Mockito.when(historyRepository.findAllByEngineerIdOrderByLifeTimeDesc(h1.getEngineerId())).thenReturn(histories);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/test/profile/{engineerId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void getInstanceById_success() throws Exception{

        List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));
     Mockito.when(historyRepository.findAll()).thenReturn(histories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/history/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void getInstanceById_Failure() throws Exception{

        List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));
        Mockito.when(historyRepository.findAll()).thenReturn(histories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/history/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    void deleteHistoryInstance_Success() throws Exception{

        Instance ins1 = new Instance(1, "Inst1", 123, "InUse");

        Mockito.when(historyRepository.findById(h1.getId())).thenReturn(Optional.of(h1));
        Mockito.when(instanceRepository.findById(h1.getInstanceId())).thenReturn(Optional.of(ins1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/test/history/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteHistoryInstance_Failure() throws Exception{

        Instance ins1 = new Instance(1, "Inst1", 123, "InUse");

        Mockito.when(historyRepository.findById(h1.getId())).thenReturn(Optional.of(h1));
        Mockito.when(instanceRepository.findById(h1.getInstanceId())).thenReturn(Optional.of(ins1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/test/history/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteAllInstances_success() throws  Exception{
        List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));

        Mockito.when(historyRepository.findAll()).thenReturn(histories);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/test/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
   void getAllHistoryPage_success() throws Exception
    {

        List<History> histories = new ArrayList<>(Arrays.asList(h1, h2));

        Page<History> historyPage = new PageImpl<>(histories);



  Mockito.when(historyRepository.findAll(org.mockito.Matchers.isA(Pageable.class))).thenReturn(historyPage);


        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/test/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalItems").value(2));
    }
}