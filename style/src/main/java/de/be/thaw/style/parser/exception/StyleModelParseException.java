package de.be.thaw.style.parser.exception;

/**
 * Exception thrown in case the style model could not be parsed.
 */
public class StyleModelParseException extends Exception {

    public StyleModelParseException(String message) {
        super(message);
    }

    public StyleModelParseException(Throwable cause) {
        super(cause);
    }

    public StyleModelParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
