package ee.tak24.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.BookCategory;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.MembershipType;

/**
 * Service interface for library operations.
 * Demonstrates the Service Layer pattern and business logic abstraction.
 * 
 * This interface defines all business operations that can be performed
 * on the library system, providing a clean separation between the UI
 * and data access layers.
 */
public interface LibraryService {
    
    // Member Management
    /**
     * Registers a new member in the library system.
     * 
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param dateOfBirth member's date of birth
     * @param street member's street address
     * @param city member's city
     * @param postalCode member's postal code
     * @param country member's country
     * @param membershipType type of membership
     * @return the created member
     * @throws IllegalArgumentException if any parameter is invalid
     */
    Member registerMember(String firstName, String lastName, String email, 
                         LocalDate dateOfBirth, String street, String city, 
                         String postalCode, String country, MembershipType membershipType);
    
    /**
     * Finds a member by their ID.
     * 
     * @param memberId the member's ID
     * @return Optional containing the member if found
     */
    Optional<Member> findMember(String memberId);
    
    /**
     * Gets all registered members.
     * 
     * @return list of all members
     */
    List<Member> getAllMembers();
    
    /**
     * Updates a member's information.
     * 
     * @param memberId the member's ID
     * @param firstName new first name
     * @param lastName new last name
     * @param email new email
     * @param membershipType new membership type
     * @return true if update was successful
     */
    boolean updateMember(String memberId, String firstName, String lastName, 
                        String email, MembershipType membershipType);
    
    /**
     * Deactivates a member.
     * 
     * @param memberId the member's ID
     * @return true if deactivation was successful
     */
    boolean deactivateMember(String memberId);
    
    // Book Management
    /**
     * Adds a new book to the library.
     * 
     * @param isbn book's ISBN
     * @param title book's title
     * @param author book's author
     * @param publisher book's publisher
     * @param publicationDate book's publication date
     * @param category book's category
     * @param totalCopies number of copies to add
     * @return the created book
     * @throws IllegalArgumentException if any parameter is invalid
     */
    Book addBook(String isbn, String title, String author, String publisher,
                LocalDate publicationDate, BookCategory category, int totalCopies);
    
    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn the book's ISBN
     * @return Optional containing the book if found
     */
    Optional<Book> findBook(String isbn);
    
    /**
     * Gets all books in the library.
     * 
     * @return list of all books
     */
    List<Book> getAllBooks();
    
    /**
     * Searches for books by various criteria.
     * 
     * @param title search by title (partial match)
     * @param author search by author (partial match)
     * @param category search by category
     * @param availableOnly if true, only return available books
     * @return list of matching books
     */
    List<Book> searchBooks(String title, String author, BookCategory category, boolean availableOnly);
    
    /**
     * Updates book information.
     * 
     * @param isbn the book's ISBN
     * @param title new title
     * @param author new author
     * @param publisher new publisher
     * @param category new category
     * @param totalCopies new total copies
     * @return true if update was successful
     */
    boolean updateBook(String isbn, String title, String author, String publisher,
                      BookCategory category, int totalCopies);
    
    /**
     * Removes a book from the library.
     * 
     * @param isbn the book's ISBN
     * @return true if removal was successful
     */
    boolean removeBook(String isbn);
    
    // Borrowing Operations
    /**
     * Borrows a book for a member.
     * 
     * @param memberId the member's ID
     * @param bookIsbn the book's ISBN
     * @param dueDate when the book should be returned
     * @return the borrowing record if successful
     * @throws ee.tak24.library.exception.MemberNotFoundException if member not found
     * @throws ee.tak24.library.exception.BookNotFoundException if book not found
     * @throws ee.tak24.library.exception.BookNotAvailableException if book not available
     * @throws ee.tak24.library.exception.MemberLimitExceededException if member has reached borrowing limit
     */
    BorrowingRecord borrowBook(String memberId, String bookIsbn, LocalDate dueDate) 
            throws ee.tak24.library.exception.MemberNotFoundException, 
                   ee.tak24.library.exception.BookNotFoundException, 
                   ee.tak24.library.exception.BookNotAvailableException, 
                   ee.tak24.library.exception.MemberLimitExceededException;
    
    /**
     * Returns a borrowed book.
     * 
     * @param memberId the member's ID
     * @param bookIsbn the book's ISBN
     * @return true if return was successful
     */
    boolean returnBook(String memberId, String bookIsbn);
    
    /**
     * Gets all borrowing records for a member.
     * 
     * @param memberId the member's ID
     * @return list of borrowing records
     */
    List<BorrowingRecord> getMemberBorrowingHistory(String memberId);
    
    /**
     * Gets all active (not returned) borrowing records.
     * 
     * @return list of active borrowing records
     */
    List<BorrowingRecord> getActiveBorrowings();
    
    /**
     * Gets all overdue borrowing records.
     * 
     * @return list of overdue borrowing records
     */
    List<BorrowingRecord> getOverdueBorrowings();
    
    // Statistics and Reporting
    /**
     * Gets library statistics.
     * 
     * @return LibraryStatistics object with various metrics
     */
    LibraryStatistics getLibraryStatistics();
    
    /**
     * Gets books that are overdue for return.
     * 
     * @return list of overdue books with member information
     */
    List<OverdueBookInfo> getOverdueBooks();
}
