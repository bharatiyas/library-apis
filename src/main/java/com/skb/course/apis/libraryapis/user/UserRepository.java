package com.skb.course.apis.libraryapis.user;

import com.skb.course.apis.libraryapis.author.AuthorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);

    List<UserEntity> findByFirstNameAndLastNameContaining(String firstName, String lastName);

    List<UserEntity> findByFirstNameContaining(String firstName);

    List<UserEntity> findByLastNameContaining(String lastName);
}
