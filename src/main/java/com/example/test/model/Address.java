package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "The Street must be informed")
    private String street;
    @NotNull(message = "The City must be informed")
    private String city;
    @NotNull(message = "The State must be informed")
    private String state;
    @NotNull(message = "The PostCode must be informed")
    private String postCode;
}
