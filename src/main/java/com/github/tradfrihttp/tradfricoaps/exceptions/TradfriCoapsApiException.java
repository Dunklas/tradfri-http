package com.github.tradfrihttp.tradfricoaps.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TradfriCoapsApiException extends Exception {

    public TradfriCoapsApiException(String message) {
        super(message);
    }

    public TradfriCoapsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
