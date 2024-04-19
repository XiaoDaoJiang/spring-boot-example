package com.xiaodao.graphspqrtest.service;

import com.xiaodao.graphspqrtest.domain.User;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Component;

@Component
@GraphQLApi
public class UserService {

    @GraphQLQuery(name = "user")
    public User getById(@GraphQLArgument(name = "id") Integer id) {
        return new User().setId(id);
    }
}