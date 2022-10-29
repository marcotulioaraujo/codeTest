package com.example.test.controller;

import com.example.test.model.User;
import com.example.test.service.UserRepository;
import com.example.test.util.ApiError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
@Slf4j
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * GET User Details
     * This action provide the information about the user that was requested via ID.
     * If the User is not present in the database (ID NOT FOUND ) throw an NOT_FOUND message
     * @param id
     */
    @GetMapping(value = "/api/userdetails/{id}")
    public ResponseEntity<Object> getUserDetails(@PathVariable(name = "id") Integer id) throws JsonProcessingException {
        Optional<User> userData = userRepository.findById(id.longValue());
        if (userData.isPresent()) {
            log.info("Response: "+ objectMapper.writeValueAsString(userData.get()));
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
           return showMessageUserNotFound(id);
        }


    }

    /**
     * POST USER DETAILS
     * This method is responsible for getting the information posted from the user.
     * Updating in the database and also returning the  updated user with a  HTTPSTTUS_OK MESSAGE.
     * @param userRequest
     * @param id
     * @return
     */
    @PostMapping(value = "/api/userdetails/{id}")
    public ResponseEntity<Object> updateUserDetails(@Valid @RequestBody User userRequest, @PathVariable(name = "id") Integer id) throws JsonProcessingException {
        Optional<User> userData = userRepository.findById(id.longValue());
        if (userData.isPresent()) {
            User userToUpdate = updateUserDetails(userRequest, userData.get());
            userRepository.save(userToUpdate);
            log.info("User has been updated: "+objectMapper.writeValueAsString(userData.get()));
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return showMessageUserNotFound(id);
        }

    }

    /**
     * Method responsible for updating the information of the user from the ID requested.
     * This method receive the REQUEST BODY as the updated information about the user, and also
     * The USER from the DATABASE that needs to be updated.
     * It return the UPDATED USER to be saved.
     * @param userRequest
     * @param userToUpdate
     * @return
     */
    private static User updateUserDetails(User userRequest, User userToUpdate) {
        userToUpdate.setTitle(userRequest.getTitle());
        userToUpdate.setFirstName(userRequest.getFirstName());
        userToUpdate.setLastName(userRequest.getLastName());
        userToUpdate.setGender(userRequest.getGender());
        userToUpdate.getAddress().setCity(userRequest.getAddress().getCity());
        userToUpdate.getAddress().setState(userRequest.getAddress().getState());
        userToUpdate.getAddress().setStreet(userRequest.getAddress().getStreet());
        userToUpdate.getAddress().setPostCode(userRequest.getAddress().getPostCode());
        return userToUpdate;
    }

    /**
     * Extraction of the USER NOT FOUND IN DATABASE.
     * This is a simple extraction, for us to not duplicate the code in our service.
     * This method logs and throw back an HTTPSTATUS_NOT_FOUND
     * @param id
     * @return
     */
    public ResponseEntity<Object> showMessageUserNotFound(Integer id){
        log.info("User has not been found with id "+id);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"User has not been found with id "+id);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
