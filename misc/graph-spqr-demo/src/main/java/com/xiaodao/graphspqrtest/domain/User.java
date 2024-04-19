package com.xiaodao.graphspqrtest.domain;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Accessors(chain = true)
public class User {

    private String name;
    private Integer id;
    private Date registrationDate;



    @GraphQLQuery(name = "name", description = "A person's name")
    public String getName() {
        return name;
    }

    @GraphQLQuery
    public Integer getId() {
        return id;
    }

    @GraphQLQuery(name = "regDate", description = "Date of registration")
    public Date getRegistrationDate() {
        return registrationDate;
    }
}