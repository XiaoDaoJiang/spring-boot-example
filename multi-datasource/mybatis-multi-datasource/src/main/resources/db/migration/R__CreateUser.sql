drop table if exists `user`;
create table user
(
    id   bigint           not null auto_increment,
    name varchar(50)      not null,
    age  tinyint unsigned not null,
    primary key (id)
)