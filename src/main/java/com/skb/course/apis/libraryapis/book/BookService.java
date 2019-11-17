package com.skb.course.apis.libraryapis.book;

import com.skb.course.apis.libraryapis.author.Author;
import com.skb.course.apis.libraryapis.author.AuthorEntity;
import com.skb.course.apis.libraryapis.author.AuthorRepository;
import com.skb.course.apis.libraryapis.exception.LibraryResourceAlreadyExistException;
import com.skb.course.apis.libraryapis.exception.LibraryResourceNotFoundException;
import com.skb.course.apis.libraryapis.book.Book;
import com.skb.course.apis.libraryapis.book.BookEntity;
import com.skb.course.apis.libraryapis.book.BookRepository;
import com.skb.course.apis.libraryapis.publisher.PublisherEntity;
import com.skb.course.apis.libraryapis.publisher.PublisherRepository;
import com.skb.course.apis.libraryapis.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static Logger logger = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;
    private PublisherRepository publisherRepository;
    private BookStatusRepository bookStatusRepository;
    private AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository,
                       BookStatusRepository bookStatusRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void addBook(Book bookToBeAdded, String traceId)
            throws LibraryResourceAlreadyExistException, LibraryResourceNotFoundException {

        logger.debug("TraceId: {}, Request to add Book: {}", traceId, bookToBeAdded);
        BookEntity bookEntity = new BookEntity(
                bookToBeAdded.getIsbn(),
                bookToBeAdded.getTitle(),
                bookToBeAdded.getYearPublished(),
                bookToBeAdded.getEdition()
        );

        // Get the parent of the Book (Publisher is the parent). If not found throw an exception
        Optional<PublisherEntity> publisherEntity = publisherRepository.findById(bookToBeAdded.getPublisherId());

        if(publisherEntity.isPresent()) {
            bookEntity.setPublisher(publisherEntity.get());
        } else {
            throw new LibraryResourceNotFoundException(traceId, "Publisher mentioned for the book does not exist");
        }

        // Save book to the DB
        BookEntity addedBook = null;
        try {
            addedBook = bookRepository.save(bookEntity);
        } catch (DataIntegrityViolationException e) {
            logger.error("TraceId: {}, Book already exists!!", traceId, e);
            throw new LibraryResourceAlreadyExistException(traceId, "Book already exists!!");
        }

        BookStatusEntity bookStatusEntity = new BookStatusEntity(addedBook.getBookId(),
                bookToBeAdded.getBookStatus().getState(),
                bookToBeAdded.getBookStatus().getTotalNumberOfCopies(), 0);

        //bookStatusEntity.setBookEntity(bookEntity);

        bookStatusRepository.save(bookStatusEntity);

        bookToBeAdded.setBookId(addedBook.getBookId());
        bookToBeAdded.setBookStatus(createBookStatusFromEntity(bookStatusEntity));
        logger.info("TraceId: {}, Book added: {}", traceId, bookToBeAdded);
    }


    public Book getBook(Integer bookId, String traceId) throws LibraryResourceNotFoundException {

        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        if(bookEntity.isPresent()) {

            BookEntity pe = bookEntity.get();
            return createBookFromEntity(pe);
        } else {
            throw new LibraryResourceNotFoundException(traceId, "Book Id: " + bookId + " Not Found");
        }

    }

    public void updateBook(Book bookToBeUpdated, String traceId) throws LibraryResourceNotFoundException {

        Optional<BookEntity> bookEntity = bookRepository.findById(bookToBeUpdated.getBookId());
        Book book = null;

        if(bookEntity.isPresent()) {

            BookEntity pe = bookEntity.get();
            if(LibraryApiUtils.doesStringValueExist(bookToBeUpdated.getEdition())) {
                pe.setEdition(bookToBeUpdated.getEdition());
            }
            if(bookToBeUpdated.getYearPublished() != null) {
                pe.setYearPublished(bookToBeUpdated.getYearPublished());
            }
            bookRepository.save(pe);
            book = createBookFromEntity(pe);
            bookToBeUpdated.setBookStatus(book.getBookStatus());
            bookToBeUpdated.setAuthors(book.getAuthors());
        } else {
            throw new LibraryResourceNotFoundException(traceId, "Book Id: " + bookToBeUpdated.getBookId() + " Not Found");
        }

    }

    public void deleteBook(Integer bookId, String traceId) throws LibraryResourceNotFoundException {

        try {
            bookRepository.deleteById(bookId);
        } catch(EmptyResultDataAccessException e) {
            logger.error("TraceId: {}, Book Id: {} Not Found", traceId, bookId, e);
            throw new LibraryResourceNotFoundException(traceId, "Book Id: " + bookId + " Not Found");
        }
    }

    @Transactional
    public Book addBookAuthors(Integer bookId, Set<Integer> authorIds, String traceId)
            throws LibraryResourceNotFoundException {

        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        if(bookEntity.isPresent()) {
            BookEntity be = bookEntity.get();
            Set<AuthorEntity> authors = authorIds.stream()
                    .map(authorId -> authorRepository.findById(authorId))
                    .collect(Collectors.toSet()).stream()
                    .filter(ae -> ae.isPresent() == true)
                    .map(ae -> ae.get())
                    .collect(Collectors.toSet());

            if(authors.size() == 0) {
                throw new LibraryResourceNotFoundException(traceId, "Book Id: " + bookId + ". None of the authors were found");
            }

            be.setAuthors(authors);
            bookRepository.save(be);
            return createBookFromEntity(be);
        } else {
            logger.error("TraceId: {}, Book Id: {} Not Found", traceId, bookId);
            throw new LibraryResourceNotFoundException(traceId, "Book Id: " + bookId + " Not Found");
        }
    }

    public List<Book> searchBookByTitle(String title, String traceId) {

        List<BookEntity> bookEntities = null;
        if(LibraryApiUtils.doesStringValueExist(title)) {
            bookEntities = bookRepository.findByTitleContaining(title);
        }
        if(bookEntities != null && bookEntities.size() > 0) {
            return createBooksForSearchResponse(bookEntities);
        } else {
            return Collections.emptyList();
        }
    }

    private List<Book> createBooksForSearchResponse(List<BookEntity> bookEntities) {

        return bookEntities.stream()
                .map(pe -> createBookFromEntity(pe))
                .collect(Collectors.toList());
    }

    private Book createBookFromEntity(BookEntity be) {

        Book book = new Book(be.getBookId(), be.getIsbn(), be.getTitle(), be.getPublisher().getPublisherid(),
                be.getYearPublished(), be.getEdition(), createBookStatusFromEntity(be.getBookStatus()));

        if(be.getAuthors() != null && be.getAuthors().size() > 0) {
            Set<Author> authors = be.getAuthors().stream()
                    .map(authorEntity -> {
                        AuthorEntity ae = authorRepository.findById(authorEntity.getAuthorId()).get();
                        return createAuthorFromAuthorEntity(ae);
                    }).collect(Collectors.toSet());

            book.setAuthors(authors);
        }
        return book;
    }

    private Author createAuthorFromAuthorEntity(AuthorEntity ae) {
        return new Author(ae.getAuthorId(), ae.getFirstName(), ae.getLastName());
    }

    private BookStatus createBookStatusFromEntity(BookStatusEntity bse) {
        return new BookStatus(bse.getBookId(), bse.getState(), bse.getTotalNumberOfCopies(), bse.getNumberOfCopiesIssued());
    }
}
