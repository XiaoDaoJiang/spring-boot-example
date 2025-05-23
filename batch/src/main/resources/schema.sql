DROP TABLE IF EXISTS t_user;

create table t_user
(
    id          bigint generated by default as identity,
    name        varchar(50) not null,
    age         integer,
    email       varchar(255),
    create_by   bigint,
    create_time timestamp default current_timestamp,
    modify_by   bigint,
    modify_time timestamp default current_timestamp,
    primary key (id)
);
