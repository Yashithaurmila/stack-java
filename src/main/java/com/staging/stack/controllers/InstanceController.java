package com.staging.stack.controllers;

import com.staging.stack.models.Instance;
import com.staging.stack.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")

public class InstanceController {
	
	  @Autowired
	  InstanceRepository instanceRepository;

	@GetMapping("/instances")
	  public ResponseEntity<List<Instance>> getAllInstances(@RequestParam(required = false) String name) {
	    try {
	      List<Instance> instances = new ArrayList<Instance>();

	      if (name == null)
	        instanceRepository.findAll().forEach(instances::add);
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

	  @GetMapping("/instances/{id}")
	  public ResponseEntity<Instance> getInstanceById(@PathVariable("id") long id) {
	    Optional<Instance> instanceData = instanceRepository.findById(id);

	    if (instanceData.isPresent()) {
	      return new ResponseEntity<>(instanceData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @PostMapping("/instances")
	  @Secured("ROLE_ADMIN")
	  public ResponseEntity<Instance> createInstance(@RequestBody Instance instance) {
	    try {
	      Instance _instance = instanceRepository
	          .save(new Instance(instance.getId(), instance.getName(),instance.getTeamId(), instance.getStatus()));
	      return new ResponseEntity<>(_instance, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @PutMapping("/instances/{id}")
	  @Secured("ROLE_USER")
	  public ResponseEntity<Instance> updateTutorial(@PathVariable("id") long id, @RequestBody Instance instance) {
	    Optional<Instance> instanceData = instanceRepository.findById(id);

	    if (instanceData.isPresent()) {
	       Instance _instance = instanceData.get();
	       _instance.setId(instance.getId());
	       _instance.setName(instance.getName());
	       _instance.setTeamId(instance.getTeamId());
	       _instance.setStatus(instance.getStatus());
	      return new ResponseEntity<>(instanceRepository.save(_instance), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/instances/{id}")
	  public ResponseEntity<HttpStatus> deleteInstance(@PathVariable("id") long id) {
	    try {
	      instanceRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @DeleteMapping("/instances")
	  public ResponseEntity<HttpStatus> deleteAllInstances() {
	    try {
	      instanceRepository.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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