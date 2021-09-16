package com.staging.stack.repository;

import com.staging.stack.models.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
	  List<Instance> findByStatus(String Status);
	  List<Instance> findByNameContaining(String name);
	}
