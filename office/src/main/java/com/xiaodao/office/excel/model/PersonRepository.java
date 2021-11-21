package com.xiaodao.office.excel.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p where p.idCard in ?1 order by p.id desc ")
    List<Person> findByIdCardInOrderByIdDesc(Collection<String> idCards);

    List<Person> findByIdCard(String idCard);

}