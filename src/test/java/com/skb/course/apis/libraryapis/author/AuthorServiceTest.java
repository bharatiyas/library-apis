package com.skb.course.apis.libraryapis.author;

import com.skb.course.apis.libraryapis.exception.LibraryResourceAlreadyExistException;
import com.skb.course.apis.libraryapis.exception.LibraryResourceNotFoundException;
import com.skb.course.apis.libraryapis.model.common.Gender;
import com.skb.course.apis.libraryapis.testutils.LibraryApiTestUtil;
import com.skb.course.apis.libraryapis.testutils.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    AuthorService authorService;

    @Before
    public void setUp() throws Exception {
        authorService = new AuthorService(authorRepository);
    }

    @Test
    public void addAuthor_success() throws LibraryResourceAlreadyExistException {

        when(authorRepository.save(any(AuthorEntity.class)))
                .thenReturn(LibraryApiTestUtil.createAuthorEntity());
        Author author = LibraryApiTestUtil.createAuthor();
        authorService.addAuthor(author, TestConstants.API_TRACE_ID);

        verify(authorRepository, times(1)).save(any(AuthorEntity.class));
        assertNotNull(author.getAuthorId());
        assertTrue(author.getFirstName().equals(TestConstants.TEST_AUTHOR_FIRST_NAME));
    }

    @Test(expected = LibraryResourceAlreadyExistException.class)
    public void addAuthor_failure() throws LibraryResourceAlreadyExistException {

        doThrow(DataIntegrityViolationException.class).when(authorRepository).save(any(AuthorEntity.class));
        Author author = LibraryApiTestUtil.createAuthor();
        authorService.addAuthor(author, TestConstants.API_TRACE_ID);

        verify(authorRepository, times(1)).save(any(AuthorEntity.class));

    }

    @Test
    public void getAuthor_success() throws LibraryResourceNotFoundException {

        when(authorRepository.findById(anyInt()))
                .thenReturn(LibraryApiTestUtil.createAuthorEntityOptional());
        Author author = authorService.getAuthor(123, TestConstants.API_TRACE_ID);

        verify(authorRepository, times(1)).findById(123);
        assertNotNull(author);
        assertNotNull(author.getAuthorId());
    }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void getAuthor_failure() throws LibraryResourceNotFoundException {

        when(authorRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        authorService.getAuthor(123, TestConstants.API_TRACE_ID);
        verify(authorRepository, times(1)).findById(123);
    }

    @Test
    public void updateAuthor_success()
            throws LibraryResourceAlreadyExistException, LibraryResourceNotFoundException {

        AuthorEntity authorEntity = LibraryApiTestUtil.createAuthorEntity();
        when(authorRepository.save(any(AuthorEntity.class)))
                .thenReturn(authorEntity);
        Author author = LibraryApiTestUtil.createAuthor();
        authorService.addAuthor(author, TestConstants.API_TRACE_ID);
        verify(authorRepository, times(1)).save(any(AuthorEntity.class));

        LocalDate updatedDob = author.getDateOfBirth().minusMonths(5);
        author.setDateOfBirth(updatedDob);

        when(authorRepository.findById(anyInt()))
                .thenReturn(LibraryApiTestUtil.createAuthorEntityOptional());
        authorService.updateAuthor(author, TestConstants.API_TRACE_ID);

        verify(authorRepository, times(1)).findById(author.getAuthorId());
        verify(authorRepository, times(2)).save(any(AuthorEntity.class));

        assertTrue(author.getDateOfBirth().isEqual(updatedDob));
    }

    @Test
    public void deleteAuthor_success() throws LibraryResourceNotFoundException {

        doNothing().when(authorRepository).deleteById(123);
        authorService.deleteAuthor(123, TestConstants.API_TRACE_ID);
        verify(authorRepository, times(1)).deleteById(123);
    }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void deleteAuthor_failure() throws LibraryResourceNotFoundException {

        doThrow(EmptyResultDataAccessException.class).when(authorRepository).deleteById(123);
        authorService.deleteAuthor(123, TestConstants.API_TRACE_ID);
        verify(authorRepository, times(1)).deleteById(123);
    }

    @Test
    public void searchAuthor_success_firstname_lastname() {

        List<AuthorEntity> authorEntityList = Arrays.asList(
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32), Gender.Male)
        );

        when(authorRepository.findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME))
                .thenReturn(authorEntityList);

        List<Author> authors = authorService.searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME, TestConstants.API_TRACE_ID);


        verify(authorRepository, times(1))
                .findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME);
        assertEquals(authorEntityList.size(), authors.size());

        assertEquals(authorEntityList.size(), authors.stream()
                .filter(author -> author.getFirstName().contains(TestConstants.TEST_AUTHOR_FIRST_NAME))
                .count()
        );
    }

    @Test
    public void searchAuthor_success_firstname() {

        List<AuthorEntity> authorEntityList = Arrays.asList(
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32), Gender.Male),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "c", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male)
        );

        when(authorRepository.findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME))
                .thenReturn(authorEntityList);

        List<Author> authors = authorService.searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, "", TestConstants.API_TRACE_ID);


        verify(authorRepository, times(1))
                .findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME);
        assertEquals(authorEntityList.size(), authors.size());

        assertEquals(authorEntityList.size(), authors.stream()
                .filter(author -> author.getFirstName().contains(TestConstants.TEST_AUTHOR_FIRST_NAME))
                .count()
        );
    }

    @Test
    public void searchAuthor_success_lastname() {

        List<AuthorEntity> authorEntityList = Arrays.asList(
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32), Gender.Male),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "c", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male),
                new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "d", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male)
        );

        when(authorRepository.findByLastNameContaining(TestConstants.TEST_AUTHOR_LAST_NAME))
                .thenReturn(authorEntityList);

        List<Author> authors = authorService.searchAuthor("", TestConstants.TEST_AUTHOR_LAST_NAME, TestConstants.API_TRACE_ID);


        verify(authorRepository, times(1))
                .findByLastNameContaining(TestConstants.TEST_AUTHOR_LAST_NAME);
        assertEquals(authorEntityList.size(), authors.size());

        assertEquals(authorEntityList.size(), authors.stream()
                .filter(author -> author.getLastName().contains(TestConstants.TEST_AUTHOR_LAST_NAME))
                .count()
        );
    }

    @Test
    public void searchAuthor_failure() {

        when(authorRepository
                .findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME))
                .thenReturn(Collections.emptyList());

        List<Author> authors = authorService
                .searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME, TestConstants.API_TRACE_ID);

        verify(authorRepository, times(1))
                .findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME);
        assertEquals(0, authors.size());
    }
}