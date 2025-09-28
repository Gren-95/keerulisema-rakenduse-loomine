package ee.tak24.library.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a book in the library system.
 * Demonstrates encapsulation and business logic.
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private BookCategory category;
    private BookStatus status;
    private int totalCopies;
    private int availableCopies;

    /**
     * Constructor for creating a new Book.
     * 
     * @param isbn unique ISBN identifier
     * @param title book title
     * @param author book author
     * @param publisher book publisher
     * @param publicationDate date of publication
     * @param category book category
     * @param totalCopies total number of copies
     */
    public Book(String isbn, String title, String author, String publisher, 
               LocalDate publicationDate, BookCategory category, int totalCopies) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
        this.publisher = Objects.requireNonNull(publisher, "Publisher cannot be null");
        this.publicationDate = Objects.requireNonNull(publicationDate, "Publication date cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.totalCopies = Math.max(1, totalCopies); // Ensure at least 1 copy
        this.availableCopies = this.totalCopies;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public BookCategory getCategory() {
        return category;
    }

    public BookStatus getStatus() {
        return status;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // Setters with validation
    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public void setAuthor(String author) {
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    public void setPublisher(String publisher) {
        this.publisher = Objects.requireNonNull(publisher, "Publisher cannot be null");
    }

    public void setCategory(BookCategory category) {
        this.category = Objects.requireNonNull(category, "Category cannot be null");
    }

    /**
     * Sets the book status and updates available copies accordingly.
     * 
     * @param status new book status
     */
    public void setStatus(BookStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        updateAvailableCopies();
    }

    /**
     * Updates the total number of copies and recalculates available copies.
     * 
     * @param totalCopies new total number of copies
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = Math.max(1, totalCopies);
        updateAvailableCopies();
    }

    /**
     * Updates the available copies based on current status and total copies.
     */
    private void updateAvailableCopies() {
        if (status == BookStatus.AVAILABLE) {
            this.availableCopies = totalCopies;
        } else if (status == BookStatus.BORROWED) {
            this.availableCopies = Math.max(0, totalCopies - 1);
        } else {
            this.availableCopies = 0;
        }
    }

    /**
     * Checks if the book is available for borrowing.
     * 
     * @return true if book can be borrowed
     */
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE && availableCopies > 0;
    }

    /**
     * Marks the book as borrowed (decrements available copies).
     * 
     * @return true if successfully borrowed, false if not available
     */
    public boolean borrow() {
        if (!isAvailable()) {
            return false;
        }
        
        availableCopies--;
        if (availableCopies == 0) {
            status = BookStatus.BORROWED;
        }
        return true;
    }

    /**
     * Marks the book as returned (increments available copies).
     * 
     * @return true if successfully returned
     */
    public boolean returnBook() {
        if (availableCopies >= totalCopies) {
            return false; // Already all copies are available
        }
        
        availableCopies++;
        if (availableCopies == totalCopies) {
            status = BookStatus.AVAILABLE;
        }
        return true;
    }

    /**
     * Gets the book's age in years since publication.
     * 
     * @return age in years
     */
    public int getAge() {
        return LocalDate.now().getYear() - publicationDate.getYear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s', author='%s', status=%s, available=%d/%d}", 
                           isbn, title, author, status, availableCopies, totalCopies);
    }
}

