package com.skb.course.apis.libraryapis.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skb.course.apis.libraryapis.author.Author;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {

    private Integer bookId;

    @NotNull
    @Size(min = 1, max = 50, message = "ISBN name must be between 1 and 50 characters")
    private String isbn;

    @Size(min = 1, max = 50, message = "Title name must be between 1 and 50 characters")
    private String title;

    private Integer publisherId;

    private Integer yearPublished;

    @Size(min = 1, max = 20, message = "Edition name must be between 1 and 50 characters")
    private String edition;

    private BookStatus bookStatus;

    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(Integer bookId,
                String isbn, String title, Integer publisherId, Integer yearPublished, String edition, BookStatus bookStatus, Set<Author> authors) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.bookStatus = bookStatus;
        this.authors = authors;
    }

    public Book(int bookId, String isbn, String title, int publisherid, int yearPublished, String edition,
                BookStatus bookStatusFromEntity) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.bookStatus = bookStatusFromEntity;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
