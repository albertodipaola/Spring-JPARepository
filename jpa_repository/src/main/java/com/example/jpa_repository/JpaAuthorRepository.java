package com.example.jpa_repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa_repository.models.Author;

public interface JpaAuthorRepository extends JpaRepository<Author, Long> {
    public List<Author> findByLastname(String lastname);

    public List<Author> findByFirstnameAndLastname(String firstname, String lastname);
    
    public List<Author> findByFirstnameNotIgnoreCase(String firstname);

    public List<Author> findByFirstnameEquals(String string);

    public List<Author> findByFirstnameContains(String string);

}
