package ee.tak24.library.exception;

/**
 * Exception thrown when a member is not found in the system.
 * Demonstrates specific exception types for different error conditions.
 */
public class MemberNotFoundException extends LibraryException {
    private static final String ERROR_CODE = "MEMBER_NOT_FOUND";

    /**
     * Constructor for member not found exception.
     * 
     * @param memberId ID of the member that was not found
     */
    public MemberNotFoundException(String memberId) {
        super("Member not found", ERROR_CODE, 
              String.format("Member ID: %s", memberId));
    }

    /**
     * Constructor for member not found exception with cause.
     * 
     * @param memberId ID of the member that was not found
     * @param cause the cause of this exception
     */
    public MemberNotFoundException(String memberId, Throwable cause) {
        super("Member not found", ERROR_CODE, 
              String.format("Member ID: %s", memberId), cause);
    }
}

