package ee.tak24.library.observer;

/**
 * Enum representing different types of library events.
 * Used by the Observer pattern for event categorization.
 */
public enum EventType {
    BOOK_BORROWED("Book Borrowed"),
    BOOK_RETURNED("Book Returned"),
    BOOK_ADDED("Book Added"),
    BOOK_REMOVED("Book Removed"),
    MEMBER_REGISTERED("Member Registered"),
    MEMBER_UPDATED("Member Updated"),
    MEMBER_DEACTIVATED("Member Deactivated"),
    OVERDUE_NOTIFICATION("Overdue Notification"),
    SYSTEM_ERROR("System Error"),
    SYSTEM_INFO("System Information");

    private final String displayName;

    /**
     * Constructor for event type.
     * 
     * @param displayName human-readable name
     */
    EventType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable name of the event type.
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

