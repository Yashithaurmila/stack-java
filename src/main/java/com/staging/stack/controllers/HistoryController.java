package com.staging.stack.controllers;

import com.staging.stack.models.*;
import com.staging.stack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")


public class HistoryController {
	
	@Autowired
	 HistoryRepository historyRepository;
	
	@Autowired
	InstanceRepository instanceRepository;


  /*   @GetMapping("/history")
	 public ResponseEntity<List<History>> getAllHistory() {
	    try {
	      List<History> instances = new ArrayList<History>();

	      historyRepository.findAll().forEach(instances::add);
	      
	      return new ResponseEntity<>(instances, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  } 
     */
	
	  @PostMapping("/history")
	  public ResponseEntity<History> addEngineer(@RequestBody History instance) {
	    try {
	    	 instance.setTime();
	    	 
	    	 Optional<Instance> option = instanceRepository.findById(instance.getInstanceId());
	    	 if(option.get().getStatus().equals("InUse"))
	    		 return new ResponseEntity<>(null, HttpStatus.DESTINATION_LOCKED);
	    	 option.get().setStatus("InUse");
	         History _instance = historyRepository.save(instance);
	      return new ResponseEntity<>(_instance, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	  
	  @GetMapping("/history/{id}")
	  public ResponseEntity<List<History>> getInstanceById(@PathVariable("id") long id) {
	    List<History> instanceData =  historyRepository.findAll();
	    List<History> mappedData = new ArrayList<>();
	    for(History obj: instanceData) {
	    	if(obj.getInstanceId() == id)
	    		mappedData.add(obj);
	    }

	    if (mappedData.size() != 0) {
	      return new ResponseEntity<>(mappedData, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    }
	  }
	  
	  @DeleteMapping("/history/{id}")
	  public ResponseEntity<HttpStatus> deleteHistoryInstance(@PathVariable("id") long id) {
	    try {
	    
	    	Optional<History> historyOption = historyRepository.findById(id);
	    	History history = historyOption.get();
	    	Long instanceId = history.getInstanceId();
	    	Optional<Instance> instanceOption = instanceRepository.findById(instanceId);
	    	Instance instance = instanceOption.get();
	    	instance.setStatus("Free");
	    	
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  
	  
	  @GetMapping("/history")
	  public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
		  @RequestParam(required = false) Long instanceId,
	      @RequestParam(required = false) String time,
	      @RequestParam(defaultValue = "0") int page,
	      @RequestParam(defaultValue = "3") int size,
	      @RequestParam(defaultValue = "time,desc") String[] sort) {

	    try {

	    

	      List<History> tutorials = new ArrayList<>();
	      Pageable pagingSort = PageRequest.of(page, size, Sort.by(sort[0]).descending());
	      Pageable paging = PageRequest.of(page, size);
	      Page<History> pageTuts;
	      if (instanceId != null)
	        pageTuts = historyRepository.findByInstanceIdOrderBytimeDesc(instanceId, pagingSort);
	      else
	        pageTuts = historyRepository.findAll(pagingSort);

	      tutorials = pageTuts.getContent();

	      if (tutorials.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      Map<String, Object> response = new HashMap<>();
	      response.put("historys", tutorials);
	      response.put("currentPage", pageTuts.getNumber());
	      response.put("totalItems", pageTuts.getTotalElements());
	      response.put("totalPages", pageTuts.getTotalPages());

	      return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	  
	  @DeleteMapping("/history")
	  public ResponseEntity<HttpStatus> deleteAllInstances() {
	    try {
	      historyRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	  }
	  
	  
	
}
