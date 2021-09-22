package com.staging.stack.controllers;

import com.staging.stack.models.History;
import com.staging.stack.models.Instance;
import com.staging.stack.models.User;
import com.staging.stack.repository.EngineerRepository;
import com.staging.stack.repository.HistoryRepository;
import com.staging.stack.repository.InstanceRepository;
import com.staging.stack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ImageBanner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@TestConfiguration
public class InstanceController {

	@Autowired
	InstanceRepository instanceRepository;
	@Autowired
	HistoryRepository historyRepository;

	@Autowired
	UserRepository userRepository;


	@RequestMapping(value = "/instances", method = RequestMethod.GET)
	  public ResponseEntity<List<Instance>> getAllInstances(@RequestParam(required = false) String name) {
	    try {
	      List<Instance> instances = new ArrayList<Instance>();

	      if (name == null) {

			  instanceRepository.findAll().forEach(instances::add);
		  }

	      else
	        instanceRepository.findByNameContaining(name).forEach(instances::add);

	      if (instances.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }


	      return new ResponseEntity<>(instances, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @GetMapping(value = "/instances/{id}")
	  public ResponseEntity<Instance> getInstanceById(@PathVariable("id") long id) {
	    Optional<Instance> instanceData = instanceRepository.findById(id);

	    if (instanceData.isPresent()) {
	      return new ResponseEntity<>(instanceData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @GetMapping("/instances/{id}/change")
	  public ResponseEntity<Instance> changeInstance(@PathVariable("id") long id, @RequestParam(required = false) String passcode){

		Optional<History> optional = historyRepository.findByInstanceId(id);
		  History _history = optional.get();
		Optional<User> optionalUser = userRepository.findByUsername(_history.getEngineerName());
		User _user = optionalUser.get();
		Optional<Instance> optionalInstance = instanceRepository.findById(id);
		Instance _instance = optionalInstance.get();

		if(passcode != _user.getPassword())
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

		_instance.setStatus("Free");
		_history.setStopTime();

		return  new ResponseEntity(_instance, HttpStatus.OK);

	  }

	  @PostMapping("/instances")
	  @Secured("ROLE_ADMIN")
	  public ResponseEntity<Instance> createInstance(@RequestBody Instance instance) {
	    try {
	    	System.out.println("I am in instnace post");
	      Instance _instance = instanceRepository
	          .save(new Instance(instance.getId(), instance.getName(),instance.getTeamId(), instance.getStatus()));
	      return new ResponseEntity<>(_instance, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @PutMapping("/instances/{id}")
	  @Secured("ROLE_USER")
	  public ResponseEntity<Instance> updateInstance(@PathVariable("id") long id, @RequestBody Instance instance) throws Exception{
	    Optional<Instance> instanceData = instanceRepository.findById(id);

	    if (instanceData.isPresent()) {

	       Instance _instance = instanceData.get();
	       _instance.setId(instance.getId());
	       _instance.setName(instance.getName());
	       _instance.setTeamId(instance.getTeamId());
	       _instance.setStatus(instance.getStatus());

			System.out.println(_instance.getStatus());
			String str = "Free";
	       if(str.equals(_instance.getStatus())){
	       	 List<History> histories = new ArrayList<>();
	       	 historyRepository.findAllByInstanceId(_instance.getId()).forEach(histories::add);
	       	 for(History h: histories){
                if(h.getLifeTime() == 0){
                	h.setStopTime();
                	h.setLifeTime();
				}
			 }
		   }
	      return new ResponseEntity<>(instanceRepository.save(_instance), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/instances/{id}")
	  public ResponseEntity<HttpStatus> deleteInstance(@PathVariable("id") long id) {
	    try {
	    	Optional<Instance> instance = instanceRepository.findById(id);
	    	if(!instance.isPresent())
	    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      instanceRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @DeleteMapping("/instances")
	  public ResponseEntity<HttpStatus> deleteAllInstances() {
	    try {
	      instanceRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	  }

	  @GetMapping("/instances/status")
	  public ResponseEntity<List<Instance>> findByPublished() {
	    try {
	      List<Instance> instances = instanceRepository.findByStatus("Free");

	      if (instances.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(instances, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	

	}