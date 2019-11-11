package com.skb.course.apis.libraryapis.author;

import com.skb.course.apis.libraryapis.publisher.PublisherEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer> {
    List<AuthorEntity> findByFirstNameContaining(String firstName);

    List<AuthorEntity> findByLastNameContaining(String lastName);

    List<AuthorEntity> findByFirstNameAndLastNameContaining(String firstName, String lastName);
}
