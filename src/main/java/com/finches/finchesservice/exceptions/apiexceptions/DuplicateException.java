package com.finches.finchesservice.exceptions.apiexceptions;

import com.finches.finchesservice.models.response.MappedError;

public class DuplicateException extends RuntimeException {
    private final MappedError error;

    public DuplicateException(MappedError error) {
        this.error = error;
    }

    public MappedError getError() {
        return error;
    }
}
