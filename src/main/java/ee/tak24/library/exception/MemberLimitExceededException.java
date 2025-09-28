package ee.tak24.library.exception;

/**
 * Exception thrown when a member tries to borrow more books than allowed.
 * Demonstrates specific exception types for business rule violations.
 */
public class MemberLimitExceededException extends LibraryException {
    private static final String ERROR_CODE = "MEMBER_LIMIT_EXCEEDED";

    /**
     * Constructor for member limit exceeded exception.
     * 
     * @param memberId ID of the member who exceeded the limit
     * @param currentBorrowed current number of borrowed books
     * @param maxAllowed maximum number of books allowed
     */
    public MemberLimitExceededException(String memberId, int currentBorrowed, int maxAllowed) {
        super("Member borrowing limit exceeded", ERROR_CODE, 
              String.format("Member ID: %s, Current borrowed: %d, Max allowed: %d", 
                           memberId, currentBorrowed, maxAllowed));
    }

    /**
     * Constructor for member limit exceeded exception with simple message.
     * 
     * @param message the error message
     */
    public MemberLimitExceededException(String message) {
        super(message, ERROR_CODE, message);
    }

    /**
     * Constructor for member limit exceeded exception with cause.
     * 
     * @param memberId ID of the member who exceeded the limit
     * @param currentBorrowed current number of borrowed books
     * @param maxAllowed maximum number of books allowed
     * @param cause the cause of this exception
     */
    public MemberLimitExceededException(String memberId, int currentBorrowed, int maxAllowed, Throwable cause) {
        super("Member borrowing limit exceeded", ERROR_CODE, 
              String.format("Member ID: %s, Current borrowed: %d, Max allowed: %d", 
                           memberId, currentBorrowed, maxAllowed), cause);
    }
}

