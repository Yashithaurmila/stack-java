package com.staging.stack.repository;

import com.staging.stack.models.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EngineerRepository extends JpaRepository<Engineer, Long>{

	  List<Engineer> findByEngineerNameContaining(String engineerName);

	  Optional<Engineer> findByEngineerId(Long engineerId);
	  Optional<Engineer> deleteByEngineerId(Long engineerId);
	Optional<Engineer> findByEngineerName(String Name);

	}
