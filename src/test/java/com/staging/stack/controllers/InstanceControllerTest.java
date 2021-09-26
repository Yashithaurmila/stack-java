package com.staging.stack.controllers;

import com.staging.stack.repository.UserRepository;
import com.staging.stack.security.WebSecurityConfig;


import com.staging.stack.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.staging.stack.models.Instance;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.staging.stack.repository.InstanceRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.runner.RunWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import org.springframework.test.context.TestContext;
import org.mortbay.jetty.webapp.WebAppContext;
import static  org.hamcrest.Matchers.hasSize;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
@EnableAutoConfiguration( exclude = {WebSecurityConfig.class})
class InstanceControllerTest {


    private MockMvc mockMvc;
    private ObjectMapper mapper;



    @Autowired
    private WebApplicationContext context;
    //InstanceRepository instanceRepository = Mockito.mock(InstanceRepository.class);

    @Mock
    InstanceRepository instanceRepository;

    @Mock
    UserRepository userRepository;



    UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private InstanceController instanceController;

    Instance inst1 = new Instance(1l, "inst1", 123, "Free");
    Instance inst2 = new Instance(2l, "Inst2", 123, "Free");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(instanceController)
                .build();

        this.mapper = new ObjectMapper();

    }



    @Test
    void getAllInstances_success() throws Exception{
        List<Instance> instances = new ArrayList<>(Arrays.asList(inst1, inst2));
        Mockito.when(instanceRepository.findAll()).thenReturn(instances);
        mockMvc.perform(get("/api/test/instances").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name").value("Inst2"));

    }
    @Test
    void getAllInstances_Failure() throws Exception{
        List<Instance> instances = new ArrayList<>();
        Mockito.when(instanceRepository.findAll()).thenReturn(instances);
        mockMvc.perform(get("/api/test/instances").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void getInstanceByIdTest() throws Exception {


        Mockito.when(instanceRepository.findById(inst1.getId())).thenReturn(Optional.of(inst1));

       mockMvc.perform(MockMvcRequestBuilders
               .get("/api/test/instances/{id}", 1).accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("inst1"));


       // failure cases

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/test/instances/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void changeInstanceTest() {


    }

    @Test
    void createInstanceTest() throws Exception {

        Instance instance = new Instance(3, "Inst3", 123, "Free");
        Mockito.when(instanceRepository.save(instance)).thenReturn(instance);


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(instance));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andDo(print());


        //failure case
        MockHttpServletRequestBuilder mockRequestFailure = MockMvcRequestBuilders.post("/api/test/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(null));

        mockMvc.perform(mockRequestFailure)
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateInstanceTest_success() throws Exception {


        Instance instance = new Instance(3, "Inst3", 123, "Free");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/test/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(this.mapper.writeValueAsString(instance));

      mockMvc.perform(MockMvcRequestBuilders.put("/api/test/instances/{id}", 3)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


    }

    @Test
    void deleteInstanceTest_sucess() throws Exception{

        Mockito.when(instanceRepository.findById(inst1.getId())).thenReturn(Optional.of(inst1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/test/instances/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    void deleteInstanceTest_Failure() throws Exception {

        Mockito.when(instanceRepository.findById(inst1.getId())).thenReturn(Optional.of(inst1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/test/instances/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.when(instanceRepository.findById(inst1.getId())).thenReturn(null);

    }


        @Test
    void deleteAllInstances_success() throws Exception{


        Mockito.when(instanceRepository.findAll()).thenReturn(Arrays.asList(inst1, inst2));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/test/instances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void deleteAllInstances_failure() throws Exception{


        //Mockito.when(instanceRepository.findAll()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/test/instances")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}