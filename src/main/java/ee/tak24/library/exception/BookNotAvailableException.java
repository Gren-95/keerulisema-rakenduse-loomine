package ee.tak24.library.exception;

/**
 * Exception thrown when a book is not available for borrowing.
 * Demonstrates specific exception types for different error conditions.
 */
public class BookNotAvailableException extends LibraryException {
    private static final String ERROR_CODE = "BOOK_NOT_AVAILABLE";

    /**
     * Constructor for book not available exception.
     * 
     * @param bookIsbn ISBN of the book that is not available
     * @param reason reason why the book is not available
     */
    public BookNotAvailableException(String bookIsbn, String reason) {
        super("Book is not available for borrowing", ERROR_CODE, 
              String.format("Book ISBN: %s, Reason: %s", bookIsbn, reason));
    }

    /**
     * Constructor for book not available exception with simple message.
     * 
     * @param message the error message
     */
    public BookNotAvailableException(String message) {
        super(message, ERROR_CODE, message);
    }

    /**
     * Constructor for book not available exception with cause.
     * 
     * @param bookIsbn ISBN of the book that is not available
     * @param reason reason why the book is not available
     * @param cause the cause of this exception
     */
    public BookNotAvailableException(String bookIsbn, String reason, Throwable cause) {
        super("Book is not available for borrowing", ERROR_CODE, 
              String.format("Book ISBN: %s, Reason: %s", bookIsbn, reason), cause);
    }
}

