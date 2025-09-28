package ee.tak24.library.model;

/**
 * Enum representing different types of library memberships.
 * Demonstrates enum usage and encapsulation of business rules.
 */
public enum MembershipType {
    BASIC("Basic", 3, 14, 0.0),
    PREMIUM("Premium", 5, 21, 0.0),
    STUDENT("Student", 4, 28, 0.0),
    FACULTY("Faculty", 10, 30, 0.0);

    private final String displayName;
    private final int maxBorrowedBooks;
    private final int maxBorrowingDays;
    private final double lateFeePerDay;

    /**
     * Constructor for membership type.
     * 
     * @param displayName human-readable name
     * @param maxBorrowedBooks maximum number of books that can be borrowed
     * @param maxBorrowingDays maximum number of days a book can be borrowed
     * @param lateFeePerDay late fee per day for overdue books
     */
    MembershipType(String displayName, int maxBorrowedBooks, int maxBorrowingDays, double lateFeePerDay) {
        this.displayName = displayName;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.maxBorrowingDays = maxBorrowingDays;
        this.lateFeePerDay = lateFeePerDay;
    }

    /**
     * Gets the human-readable name of the membership type.
     * 
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the maximum number of books that can be borrowed.
     * 
     * @return maximum borrowed books
     */
    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    /**
     * Gets the maximum number of days a book can be borrowed.
     * 
     * @return maximum borrowing days
     */
    public int getMaxBorrowingDays() {
        return maxBorrowingDays;
    }

    /**
     * Gets the late fee per day for overdue books.
     * 
     * @return late fee per day
     */
    public double getLateFeePerDay() {
        return lateFeePerDay;
    }

    /**
     * Calculates the late fee for a given number of overdue days.
     * 
     * @param overdueDays number of days overdue
     * @return calculated late fee
     */
    public double calculateLateFee(int overdueDays) {
        return Math.max(0, overdueDays) * lateFeePerDay;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

