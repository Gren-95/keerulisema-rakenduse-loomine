package ee.tak24.library.exception;

/**
 * Base exception class for all library-related exceptions.
 * Demonstrates exception hierarchy and custom exception handling.
 */
public abstract class LibraryException extends Exception {
    private final String errorCode;
    private final String details;

    /**
     * Constructor for creating a library exception.
     * 
     * @param message error message
     * @param errorCode unique error code
     * @param details additional error details
     */
    protected LibraryException(String message, String errorCode, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Constructor for creating a library exception with cause.
     * 
     * @param message error message
     * @param errorCode unique error code
     * @param details additional error details
     * @param cause the cause of this exception
     */
    protected LibraryException(String message, String errorCode, String details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Gets the error code for this exception.
     * 
     * @return error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets additional error details.
     * 
     * @return error details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Gets a formatted error message including code and details.
     * 
     * @return formatted error message
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(errorCode).append("] ").append(getMessage());
        if (details != null && !details.isEmpty()) {
            sb.append(" - ").append(details);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}

