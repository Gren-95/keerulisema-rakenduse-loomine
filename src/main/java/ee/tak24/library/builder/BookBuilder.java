package ee.tak24.library.builder;

import java.time.LocalDate;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.BookCategory;
import ee.tak24.library.model.BookStatus;

/**
 * Builder pattern implementation for creating Book objects.
 * Demonstrates the Builder pattern for complex object construction.
 * 
 * This builder provides a fluent interface for creating Book instances
 * with optional parameters and validation.
 */
public class BookBuilder {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private BookCategory category;
    private BookStatus status;
    private int totalCopies;
    private int availableCopies;

    /**
     * Creates a new BookBuilder instance.
     */
    public BookBuilder() {
        // Initialize with default values
        this.status = BookStatus.AVAILABLE;
        this.totalCopies = 1;
        this.availableCopies = 1;
    }

    /**
     * Sets the ISBN of the book.
     * 
     * @param isbn the book's ISBN
     * @return this builder instance
     */
    public BookBuilder withIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    /**
     * Sets the title of the book.
     * 
     * @param title the book's title
     * @return this builder instance
     */
    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the author of the book.
     * 
     * @param author the book's author
     * @return this builder instance
     */
    public BookBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Sets the publisher of the book.
     * 
     * @param publisher the book's publisher
     * @return this builder instance
     */
    public BookBuilder withPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    /**
     * Sets the publication date of the book.
     * 
     * @param publicationDate the book's publication date
     * @return this builder instance
     */
    public BookBuilder withPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    /**
     * Sets the category of the book.
     * 
     * @param category the book's category
     * @return this builder instance
     */
    public BookBuilder withCategory(BookCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Sets the status of the book.
     * 
     * @param status the book's status
     * @return this builder instance
     */
    public BookBuilder withStatus(BookStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Sets the total number of copies.
     * 
     * @param totalCopies the total number of copies
     * @return this builder instance
     */
    public BookBuilder withTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        return this;
    }

    /**
     * Sets the available number of copies.
     * 
     * @param availableCopies the available number of copies
     * @return this builder instance
     */
    public BookBuilder withAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
        return this;
    }

    /**
     * Builds the Book object with the configured parameters.
     * 
     * @return a new Book instance
     * @throws IllegalStateException if required fields are not set
     */
    public Book build() {
        validateRequiredFields();
        
        Book book = new Book(isbn, title, author, publisher, publicationDate, category, totalCopies);
        
        // Set additional properties if they differ from defaults
        if (status != BookStatus.AVAILABLE) {
            book.setStatus(status);
        }
        
        return book;
    }

    /**
     * Validates that all required fields are set.
     * 
     * @throws IllegalStateException if any required field is null or empty
     */
    private void validateRequiredFields() {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalStateException("ISBN is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalStateException("Title is required");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalStateException("Author is required");
        }
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalStateException("Publisher is required");
        }
        if (publicationDate == null) {
            throw new IllegalStateException("Publication date is required");
        }
        if (category == null) {
            throw new IllegalStateException("Category is required");
        }
        if (totalCopies <= 0) {
            throw new IllegalStateException("Total copies must be positive");
        }
    }

    /**
     * Creates a new BookBuilder instance.
     * 
     * @return a new BookBuilder instance
     */
    public static BookBuilder builder() {
        return new BookBuilder();
    }

    /**
     * Creates a BookBuilder with common book data pre-filled.
     * 
     * @param isbn the book's ISBN
     * @param title the book's title
     * @param author the book's author
     * @return a BookBuilder with basic information set
     */
    public static BookBuilder builder(String isbn, String title, String author) {
        return new BookBuilder()
                .withIsbn(isbn)
                .withTitle(title)
                .withAuthor(author);
    }
}
