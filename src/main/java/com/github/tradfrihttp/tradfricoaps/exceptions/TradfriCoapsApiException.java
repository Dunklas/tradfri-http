package com.github.tradfrihttp.tradfricoaps.exceptions;

import org.springframework.http.HttpStatus;

public class TradfriCoapsApiException extends Exception {

    private HttpStatus httpStatus;

    public TradfriCoapsApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public TradfriCoapsApiException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
