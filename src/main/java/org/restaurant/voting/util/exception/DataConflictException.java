package org.restaurant.voting.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(HttpStatus.CONFLICT, msg, ErrorAttributeOptions.of(MESSAGE));
    }
}
