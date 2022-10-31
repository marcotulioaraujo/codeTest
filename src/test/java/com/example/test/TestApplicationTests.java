package com.example.test;

import com.example.test.controller.UserController;
import com.example.test.model.Address;
import com.example.test.model.User;
import com.example.test.service.UserRepository;
import com.example.test.util.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class TestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserDetailsStringParam() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/userdetails/as").accept(
                MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.status",
                        is("BAD_REQUEST")));

    }

    @Test
    public void testPostUserDetailsStringParam() throws Exception {

        Address address = Address.builder()
                .city("Sydney")
                .state("NSW")
                .street("Epping Park Drive")
                .postCode("2121")
                .build();
        User userUpdate = User.builder()
                .firstName("Marco")
                .lastName("Araujo")
                .title("Mr")
                .gender("Male")
                .address(address)
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/userdetails/as")
                .content(this.objectMapper.writeValueAsString(userUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.status",
                        is("BAD_REQUEST")));

    }

    @Test
    public void testPostUserDetailsMissingFieldLastName() throws Exception {

        Address address = Address.builder()
                .city("Sydney")
                .state("NSW")
                .street("Epping Park Drive")
                .postCode("2121")
                .build();
        User userUpdate = User.builder()
                .firstName("Marco")
                .title("Mr")
                .gender("Male")
                .address(address)
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/userdetails/1")
                .content(this.objectMapper.writeValueAsString(userUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",
                        is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        is("The Last Name must be informed")));
    }


    @Test
    public void testPostUserDetailsMissingFieldState() throws Exception {

        Address address = Address.builder()
                .city("Sydney")
                .street("Epping Park Drive")
                .postCode("2121")
                .build();
        User userUpdate = User.builder()
                .firstName("Marco")
                .lastName("Araujo")
                .title("Mr")
                .gender("Male")
                .address(address)
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/userdetails/1")
                .content(this.objectMapper.writeValueAsString(userUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",
                        is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        is("The State must be informed")));
    }

    @Test
    public void getUserDetailsSuccess() throws Exception {
        Address address = Address.builder()
                .id(1L)
                .city("Sydney")
                .state("NSW")
                .street("Epping Park Drive")
                .postCode("2121")
                .build();
        User userUpdate = User.builder()
                .id(1L)
                .firstName("Marco")
                .lastName("Araujo")
                .title("Mr")
                .gender("Male")
                .address(address)
                .build();
        Mockito.when(userController.getUserDetails(1)).thenReturn(new ResponseEntity<>(userUpdate, HttpStatus.OK));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/userdetails/1");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is("Marco")))
                .andExpect(jsonPath("$.lastName",
                        is("Araujo")));
    }


    @Test
    public void testGetUserDetailsNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/userdetails/20").accept(
                MediaType.APPLICATION_JSON);

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"User has not been found with id 20");
        Mockito.when(userController.getUserDetails(20)).thenReturn(new ResponseEntity<>(apiError, apiError.getStatus()));
        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.status",
                        is("NOT_FOUND")))
                .andExpect(jsonPath("$.message",
                        is("User has not been found with id 20")));

    }


    @Test
    public void testUpdateUserDetailsNotFound() throws Exception {

        Address address = Address.builder()
                .city("Sydney")
                .state("NSW")
                .street("Epping Park Drive")
                .postCode("2121")
                .build();
        User userUpdate = User.builder()
                .firstName("Marco")
                .lastName("Araujo")
                .title("Mr")
                .gender("Male")
                .address(address)
                .build();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/userdetails/20")
                .content(this.objectMapper.writeValueAsString(userUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"User has not been found with id 20");
        Mockito.when(userController.updateUserDetails(userUpdate,20)).thenReturn(new ResponseEntity<>(apiError, apiError.getStatus()));
        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.status",
                        is("NOT_FOUND")))
                .andExpect(jsonPath("$.message",
                        is("User has not been found with id 20")));

    }

}
