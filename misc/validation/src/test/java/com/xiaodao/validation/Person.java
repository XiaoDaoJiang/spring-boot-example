package com.xiaodao.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Person {
    private final String firstName;

    @JsonProperty("last_name")
    private final String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @NotNull
    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }
}