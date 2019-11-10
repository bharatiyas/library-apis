package com.skb.course.apis.libraryapis.exception;

import com.skb.course.apis.libraryapis.model.common.LibraryApiError;
import com.skb.course.apis.libraryapis.util.LibraryApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class LibraryControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(LibraryControllerExceptionHandler.class);

    @ExceptionHandler(LibraryResourceNotFoundException.class)
    public final ResponseEntity<LibraryApiError> handleLibraryResourceNotFoundException(
            LibraryResourceNotFoundException e, WebRequest webRequest) {

        return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibraryResourceAlreadyExistException.class)
    public final ResponseEntity<LibraryApiError> handleLibraryResourceAlreadyExistException(
            LibraryResourceAlreadyExistException e, WebRequest webRequest) {

        return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibraryResourceBadRequestException.class)
    public final ResponseEntity<LibraryApiError> handleLibraryResourceBadRequestException(
            LibraryResourceBadRequestException e, WebRequest webRequest) {

        return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<LibraryApiError> handleAllException(
            Exception e, WebRequest webRequest) {

        String traceId = getTraceId(webRequest);
        logger.error(traceId, e);
        return new ResponseEntity<>(new LibraryApiError(traceId, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private String getTraceId(WebRequest webRequest) {
        String traceId = webRequest.getHeader("Trace-Id");
        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        return traceId;
    }
}
