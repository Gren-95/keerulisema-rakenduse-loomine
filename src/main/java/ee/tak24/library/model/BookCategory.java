package ee.tak24.library.model;

/**
 * Enum representing different categories of books.
 * Demonstrates enum usage for categorization.
 */
public enum BookCategory {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    HISTORY("History"),
    BIOGRAPHY("Biography"),
    REFERENCE("Reference"),
    CHILDREN("Children"),
    TEXTBOOK("Textbook"),
    MAGAZINE("Magazine");

    private final String displayName;

    /**
     * Constructor for book category.
     * 
     * @param displayName human-readable name
     */
    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable name of the category.
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

