package org.homs.lispo.util;

public class ValidationError extends Error {

    public ValidationError(String message) {
        super(message);
    }

    public ValidationError(String message, Throwable cause) {
        super(message, cause);
    }
}
