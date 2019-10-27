package com.wipro.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(@Nullable String message) {
        super(message);
    }

    public CustomerNotFoundException(@Nullable String message, @Nullable  Throwable cause) {
        super(message, cause);
    }
}
