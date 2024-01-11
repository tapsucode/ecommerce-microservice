package com.phamanh.notificationservice.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private Long id;

    private String firstName;

    private String lastName;

    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;

    private String mobile;

    @JsonIgnore
    private Order order;

}
