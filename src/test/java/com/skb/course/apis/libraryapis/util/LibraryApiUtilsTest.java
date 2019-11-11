package com.skb.course.apis.libraryapis.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryApiUtilsTest {

    @Test
    public void doesStringValueExist() {

        assertTrue(LibraryApiUtils.doesStringValueExist("ValueExist"));
        assertFalse(LibraryApiUtils.doesStringValueExist(""));
        assertFalse(LibraryApiUtils.doesStringValueExist(null));
    }
}