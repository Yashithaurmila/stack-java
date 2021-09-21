package com.staging.stack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staging.stack.controllers.InstanceController;
import com.staging.stack.repository.InstanceRepository;
import com.staging.stack.models.Instance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import  java.util.*;

@WebMvcTest(InstanceController.class)
public class InstanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    InstanceRepository instanceRepository;

    Instance inst_1 = new Instance(100, "Inst1", 123, "Free");
    Instance inst_2 = new Instance(101, "Inst2", 123, "Free");

    @Test
    public void getAllInstances_success() throws Exception{

         List<Instance> instances = new ArrayList<>(Arrays.asList(inst_1, inst_2));

        Mockito.when(instanceRepository.findAll()).thenReturn(instances);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Inst2")))
              

        );
    }


}
