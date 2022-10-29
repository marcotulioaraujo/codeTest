package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@Entity
@Table(name = "user_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "The title must be informed")
    private String title;
    @NotNull(message = "The First Name must be informed")
    private String firstName;
    @NotNull(message = "The Last Name must be informed")
    private String lastName;
    @NotNull(message = "The Gender must be informed")
    private String gender;
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

}
