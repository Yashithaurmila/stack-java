package com.staging.stack.repository;

import com.staging.stack.models.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<History, Long>{
	
	 

	  Page<History> findByTimeContaining(String time, Pageable pageable);

	  
	  @Query(value = "SELECT h1 FROM History h1  WHERE h1.instanceId= ?1 order by h1.instanceId desc")
	  Page<History>findByInstanceIdOrderBytimeDesc(long i, Pageable pageable);
	  
	  

}
