package com.skb.course.apis.libraryapis.testutils;

import com.skb.course.apis.libraryapis.publisher.Publisher;
import com.skb.course.apis.libraryapis.publisher.PublisherEntity;

import java.util.Optional;

public class LibraryApiTestUtil {

    public static Publisher createPublisher() {
        return new Publisher(null, TestConstants.TEST_PUBLISHER_NAME,
                TestConstants.TEST_PUBLISHER_EMAIL,
                TestConstants.TEST_PUBLISHER_PHONE);
    }

    public static PublisherEntity createPublisherEntity() {
        return new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME,
                TestConstants.TEST_PUBLISHER_EMAIL,
                TestConstants.TEST_PUBLISHER_PHONE);
    }

    public static Optional<PublisherEntity> createPublisherEntityOptional() {
        return Optional.of(createPublisherEntity());
    }
}
