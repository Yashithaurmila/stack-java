package com.staging.stack.controllers;

import com.staging.stack.models.Engineer;
import com.staging.stack.models.Instance;
import com.staging.stack.repository.EngineerRepository;
import com.staging.stack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.staging.stack.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class EngineerController {

    @Autowired
    EngineerRepository engineerRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/engineers")
    public ResponseEntity<List<Engineer>> getAllEngineers(@RequestParam(required = false) String engineerName){
        try{

            List<Engineer> engineers= new ArrayList<>();
            if(engineerName == null) {

                engineerRepository.findAll().forEach(engineers::add);
            }

            else
                engineerRepository.findByEngineerNameContaining(engineerName).forEach(engineers::add);



            if(engineers.isEmpty())
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(engineers, HttpStatus.OK);
        }catch (Exception E){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/engineers/{engineerId}")
    public  ResponseEntity<Engineer> getEngineerByEngineerId(@PathVariable("engineerId") long engineerId){
        try{

            Optional<Engineer> engineerOption = engineerRepository.findByEngineerId(engineerId);

            if(engineerOption.isPresent()){
                Engineer _engineer = engineerOption.get();
                return new ResponseEntity<>(_engineer, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/engineers")
    public ResponseEntity<Engineer>  AddEngineer(@RequestBody Engineer engineer){
        try{
            Optional<User> _user = userRepository.findById(engineer.getengineerId());
            if(!_user.isPresent())
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
             Engineer _engineer = engineerRepository.save(new Engineer(
                     engineer.getId(), engineer.getengineerId(), engineer.getEngineerName()
             ));
            return new ResponseEntity<>(_engineer, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/engineers/{engineerId}")
    @Secured("ROLE_USER")
    public ResponseEntity<Engineer> updateEngineer(@PathVariable("engineerId") long engineerId, @RequestBody Engineer engineer) {
        Optional<Engineer> engineerOptional = engineerRepository.findByEngineerId(engineerId);


        if (engineerOptional.isPresent()) {
            Engineer _engineer = engineerOptional.get();
            _engineer.setId(engineer.getId());
            _engineer.setEngineerName(engineer.getEngineerName());
            _engineer.setengineerId(engineer.getengineerId());

            return new ResponseEntity<>(engineerRepository.save(_engineer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/engineers/{engineerId}")
    public ResponseEntity<HttpStatus> deleteEngineer(@PathVariable("engineerId") long engineerId) {
        try {
             engineerRepository.deleteByEngineerId(engineerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/engineers")
    public ResponseEntity<HttpStatus> deleteAllEngineers() {
        try {
            engineerRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
