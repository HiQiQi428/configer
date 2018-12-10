package org.luncert.configer;

public class ConfigureException extends RuntimeException {

    private static final long serialVersionUID = -4870142172534439500L;

    public ConfigureException() {
        super();
    }

    public ConfigureException(String message) {
        super(message);
    }

    public ConfigureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigureException(Throwable cause) {
        super(cause);
    }

    protected ConfigureException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}