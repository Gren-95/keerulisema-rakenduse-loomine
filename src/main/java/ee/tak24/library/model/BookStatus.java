package ee.tak24.library.model;

/**
 * Enum representing the status of a book.
 * Demonstrates enum usage for state management.
 */
public enum BookStatus {
    AVAILABLE("Available"),
    BORROWED("Borrowed"),
    MAINTENANCE("Under Maintenance"),
    LOST("Lost"),
    DAMAGED("Damaged");

    private final String displayName;

    /**
     * Constructor for book status.
     * 
     * @param displayName human-readable name
     */
    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable name of the status.
     * 
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

