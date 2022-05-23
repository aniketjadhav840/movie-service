package com.gofore.movie.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.gofore.movie.application.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, QuerydslPredicateExecutor<Person>  {

}
