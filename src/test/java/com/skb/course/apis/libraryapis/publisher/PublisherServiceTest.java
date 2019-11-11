package com.skb.course.apis.libraryapis.publisher;

import com.skb.course.apis.libraryapis.exception.LibraryResourceAlreadyExistException;
import com.skb.course.apis.libraryapis.exception.LibraryResourceNotFoundException;
import com.skb.course.apis.libraryapis.testutils.LibraryApiTestUtil;
import com.skb.course.apis.libraryapis.testutils.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PublisherServiceTest {

    @Mock
    PublisherRepository publisherRepository;

    PublisherService publisherService;

    @Before
    public void setUp() throws Exception {
        publisherService = new PublisherService(publisherRepository);
    }

    @Test
    public void addPublisher_success() throws LibraryResourceAlreadyExistException {

        when(publisherRepository.save(any(PublisherEntity.class)))
                .thenReturn(LibraryApiTestUtil.createPublisherEntity());
        Publisher publisher = LibraryApiTestUtil.createPublisher();
        publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);

        verify(publisherRepository, times(1)).save(any(PublisherEntity.class));
        assertNotNull(publisher.getPublisherId());
        assertTrue(publisher.getName().equals(TestConstants.TEST_PUBLISHER_NAME));
    }

    @Test(expected = LibraryResourceAlreadyExistException.class)
    public void addPublisher_failure() throws LibraryResourceAlreadyExistException {

        doThrow(DataIntegrityViolationException.class).when(publisherRepository).save(any(PublisherEntity.class));
        Publisher publisher = LibraryApiTestUtil.createPublisher();
        publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);

        verify(publisherRepository, times(1)).save(any(PublisherEntity.class));

    }

    @Test
    public void getPublisher_success() throws LibraryResourceNotFoundException {

        when(publisherRepository.findById(anyInt()))
                .thenReturn(LibraryApiTestUtil.createPublisherEntityOptional());
        Publisher publisher = publisherService.getPublisher(123, TestConstants.API_TRACE_ID);

        verify(publisherRepository, times(1)).findById(123);
        assertNotNull(publisher);
        assertNotNull(publisher.getPublisherId());
    }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void getPublisher_failure() throws LibraryResourceNotFoundException {

        when(publisherRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        publisherService.getPublisher(123, TestConstants.API_TRACE_ID);
        verify(publisherRepository, times(1)).findById(123);
    }

    @Test
    public void updatePublisher_success()
            throws LibraryResourceAlreadyExistException, LibraryResourceNotFoundException {

        PublisherEntity publisherEntity = LibraryApiTestUtil.createPublisherEntity();
        when(publisherRepository.save(any(PublisherEntity.class)))
                .thenReturn(publisherEntity);
        Publisher publisher = LibraryApiTestUtil.createPublisher();
        publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);
        verify(publisherRepository, times(1)).save(any(PublisherEntity.class));

        publisher.setEmailId(TestConstants.TEST_PUBLISHER_EMAIL_UPDATED);
        publisher.setPhoneNumber(TestConstants.TEST_PUBLISHER_PHONE_UPDATED);

        when(publisherRepository.findById(anyInt()))
                .thenReturn(LibraryApiTestUtil.createPublisherEntityOptional());
        publisherService.updatePublisher(publisher, TestConstants.API_TRACE_ID);

        verify(publisherRepository, times(1)).findById(publisher.getPublisherId());
        verify(publisherRepository, times(2)).save(any(PublisherEntity.class));

        assertTrue(TestConstants.TEST_PUBLISHER_EMAIL_UPDATED.equals(publisher.getEmailId()));
        assertTrue(TestConstants.TEST_PUBLISHER_PHONE_UPDATED.equals(publisher.getPhoneNumber()));
    }

    @Test
    public void deletePublisher_success() throws LibraryResourceNotFoundException {

        doNothing().when(publisherRepository).deleteById(123);
        publisherService.deletePublisher(123, TestConstants.API_TRACE_ID);
        verify(publisherRepository, times(1)).deleteById(123);
    }

    @Test(expected = LibraryResourceNotFoundException.class)
    public void deletePublisher_failure() throws LibraryResourceNotFoundException {

        doThrow(EmptyResultDataAccessException.class).when(publisherRepository).deleteById(123);
        publisherService.deletePublisher(123, TestConstants.API_TRACE_ID);
        verify(publisherRepository, times(1)).deleteById(123);
    }

    @Test
    public void searchPublisher_success() {

        List<PublisherEntity> publisherEntityList = Arrays.asList(
          new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME + 1,
                  TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE),
          new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME + 2,
                  TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE)
        );

        when(publisherRepository.findByNameContaining(TestConstants.TEST_PUBLISHER_NAME))
                .thenReturn(publisherEntityList);

        List<Publisher> publishers = publisherService.searchPublisher(TestConstants.TEST_PUBLISHER_NAME, TestConstants.API_TRACE_ID);


        verify(publisherRepository, times(1)).findByNameContaining(TestConstants.TEST_PUBLISHER_NAME);
        assertEquals(publisherEntityList.size(), publishers.size());

        assertEquals(publisherEntityList.size(), publishers.stream()
                .filter(publisher -> publisher.getName().contains(TestConstants.TEST_PUBLISHER_NAME))
                .count()
        );
    }

    @Test
    public void searchPublisher_failure() {

        when(publisherRepository.findByNameContaining(TestConstants.TEST_PUBLISHER_NAME))
                .thenReturn(Collections.emptyList());

        List<Publisher> publishers = publisherService.searchPublisher(TestConstants.TEST_PUBLISHER_NAME, TestConstants.API_TRACE_ID);

        verify(publisherRepository, times(1)).findByNameContaining(TestConstants.TEST_PUBLISHER_NAME);
        assertEquals(0, publishers.size());
    }
}