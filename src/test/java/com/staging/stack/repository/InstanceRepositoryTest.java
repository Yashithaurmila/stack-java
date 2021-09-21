package com.staging.stack.repository;

import com.staging.stack.models.Instance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class InstanceRepositoryTest {

    @Autowired
  InstanceRepository instanceRepository;

    @Test
    void findByStatus_success() {

        Instance inst1 = new Instance(1, "Inst1", 123, "Free");

        instanceRepository.save(inst1);

        List<Instance> instances= instanceRepository.findByStatus("Free");
        Instance instance = instances.get(0);

        System.out.println(instance.getStatus());
        assertEquals(instance.getStatus(), inst1.getStatus());




    }

    @Test
    void testGetAllInstancesSuccess() throws Exception{

    }
}