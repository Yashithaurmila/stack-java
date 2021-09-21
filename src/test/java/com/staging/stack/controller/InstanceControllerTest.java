package com.staging.stack.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.staging.stack.controllers.InstanceController;
import com.staging.stack.models.Instance;
import com.staging.stack.repository.InstanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public  class InstanceControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    InstanceRepository instanceRepository;


    Instance inst_1 = new Instance(100, "Inst1", 123, "Free");
    Instance inst_2 = new Instance(101, "Inst2", 123, "Free");


    public InstanceControllerTest() {

    }

    @Test
    public void getInstanceById_success() throws Exception {
        try {

            Mockito.when(instanceRepository.findById(inst_2.getId())).thenReturn(Optional.of(inst_2));

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/instances/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Inst2")));



        } catch (Exception e) {
               System.out.println(e);
        }
    }
}
