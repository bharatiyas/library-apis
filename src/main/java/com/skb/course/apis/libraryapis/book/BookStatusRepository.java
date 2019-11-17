package com.skb.course.apis.libraryapis.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatusRepository extends CrudRepository<BookStatusEntity, Integer> {
}
