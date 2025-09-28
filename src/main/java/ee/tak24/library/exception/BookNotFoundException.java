package ee.tak24.library.exception;

/**
 * Exception thrown when a book is not found in the system.
 * Demonstrates specific exception types for different error conditions.
 */
public class BookNotFoundException extends LibraryException {
    private static final String ERROR_CODE = "BOOK_NOT_FOUND";

    /**
     * Constructor for book not found exception.
     * 
     * @param bookIsbn ISBN of the book that was not found
     */
    public BookNotFoundException(String bookIsbn) {
        super("Book not found", ERROR_CODE, 
              String.format("Book ISBN: %s", bookIsbn));
    }

    /**
     * Constructor for book not found exception with cause.
     * 
     * @param bookIsbn ISBN of the book that was not found
     * @param cause the cause of this exception
     */
    public BookNotFoundException(String bookIsbn, Throwable cause) {
        super("Book not found", ERROR_CODE, 
              String.format("Book ISBN: %s", bookIsbn), cause);
    }
}

