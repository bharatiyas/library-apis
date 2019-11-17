package com.skb.course.apis.libraryapis.book;


import com.skb.course.apis.libraryapis.author.AuthorEntity;
import com.skb.course.apis.libraryapis.publisher.PublisherEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BOOK")
public class BookEntity {

    @Column(name = "Book_Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookId_generator")
    @SequenceGenerator(name = "bookId_generator", sequenceName = "book_sequence", allocationSize = 50)
    private int bookId;

    @Column(name = "Isbn")
    private String isbn;

    @Column(name = "Title")
    private String title;

    @Column(name = "Year_Published")
    private int yearPublished;

    @Column(name = "Edition")
    private String edition;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    @JoinColumn(name = "Publisher_Id",
                nullable = false)
    private PublisherEntity publisher;

    @OneToOne(fetch = FetchType.LAZY,
              cascade = CascadeType.ALL,
                mappedBy = "bookEntity")
    private BookStatusEntity bookStatus;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<AuthorEntity> authors = new HashSet<>();

    public BookEntity() {
    }

    public BookEntity(String isbn, String title, int yearPublished, String edition, PublisherEntity publisher, BookStatusEntity bookStatus, Set<AuthorEntity> authors) {
        this.isbn = isbn;
        this.title = title;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.publisher = publisher;
        this.bookStatus = bookStatus;
        this.authors = authors;
    }

    public BookEntity(String isbn, String title, Integer yearPublished, String edition) {
        this.isbn = isbn;
        this.title = title;
        this.yearPublished = yearPublished;
        this.edition = edition;
    }

    public int getBookId() {
        return bookId;
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

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public PublisherEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }

    public BookStatusEntity getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatusEntity bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
    }
}
