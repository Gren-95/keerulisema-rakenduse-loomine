package ee.tak24.library.util;

import java.time.LocalDate;
import java.util.Objects;

import ee.tak24.library.model.Address;
import ee.tak24.library.model.Book;
import ee.tak24.library.model.BookCategory;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.MembershipType;
import ee.tak24.library.strategy.NotificationService;

/**
 * Factory class for creating library objects.
 * Demonstrates the Factory pattern and object creation.
 */
public class LibraryFactory {
    private final IdGenerator idGenerator;
    private final NotificationService notificationService;

    /**
     * Constructor for creating a library factory.
     */
    public LibraryFactory() {
        this.idGenerator = IdGenerator.getInstance();
        this.notificationService = new NotificationService();
    }

    /**
     * Creates a new member with the given details.
     * 
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param dateOfBirth member's date of birth
     * @param address member's address
     * @param membershipType type of membership
     * @return new Member instance
     */
    public Member createMember(String firstName, String lastName, String email, 
                              LocalDate dateOfBirth, Address address, MembershipType membershipType) {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        Objects.requireNonNull(address, "Address cannot be null");
        Objects.requireNonNull(membershipType, "Membership type cannot be null");

        String memberId = idGenerator.generateMemberId();
        return new Member(memberId, firstName, lastName, email, dateOfBirth, address, membershipType);
    }

    /**
     * Creates a new book with the given details.
     * 
     * @param isbn book's ISBN
     * @param title book's title
     * @param author book's author
     * @param publisher book's publisher
     * @param publicationDate book's publication date
     * @param category book's category
     * @param totalCopies total number of copies
     * @return new Book instance
     */
    public Book createBook(String isbn, String title, String author, String publisher, 
                          LocalDate publicationDate, BookCategory category, int totalCopies) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(author, "Author cannot be null");
        Objects.requireNonNull(publisher, "Publisher cannot be null");
        Objects.requireNonNull(publicationDate, "Publication date cannot be null");
        Objects.requireNonNull(category, "Category cannot be null");

        return new Book(isbn, title, author, publisher, publicationDate, category, totalCopies);
    }

    /**
     * Creates a new address with the given details.
     * 
     * @param street street address
     * @param city city name
     * @param postalCode postal code
     * @param country country name
     * @return new Address instance
     */
    public Address createAddress(String street, String city, String postalCode, String country) {
        Objects.requireNonNull(street, "Street cannot be null");
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(postalCode, "Postal code cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");

        return new Address(street, city, postalCode, country);
    }

    /**
     * Creates a new borrowing record.
     * 
     * @param memberId ID of the member
     * @param bookIsbn ISBN of the book
     * @param borrowDate date when borrowed
     * @param dueDate date when due
     * @return new BorrowingRecord instance
     */
    public BorrowingRecord createBorrowingRecord(String memberId, String bookIsbn, 
                                                LocalDate borrowDate, LocalDate dueDate) {
        Objects.requireNonNull(memberId, "Member ID cannot be null");
        Objects.requireNonNull(bookIsbn, "Book ISBN cannot be null");
        Objects.requireNonNull(borrowDate, "Borrow date cannot be null");
        Objects.requireNonNull(dueDate, "Due date cannot be null");

        String recordId = idGenerator.generateBorrowingRecordId();
        return new BorrowingRecord(recordId, memberId, bookIsbn, borrowDate, dueDate);
    }

    /**
     * Creates a sample member for testing purposes.
     * 
     * @return sample Member instance
     */
    public Member createSampleMember() {
        Address address = createAddress("123 Main St", "Tallinn", "10111", "Estonia");
        return createMember("John", "Doe", "john.doe@example.com", 
                           LocalDate.of(1990, 5, 15), address, MembershipType.BASIC);
    }

    /**
     * Creates a sample book for testing purposes.
     * 
     * @return sample Book instance
     */
    public Book createSampleBook() {
        return createBook("978-0-123456-78-9", "Introduction to Java Programming", 
                         "Jane Smith", "Tech Publishers", 
                         LocalDate.of(2023, 1, 15), BookCategory.TECHNOLOGY, 3);
    }

    /**
     * Gets the notification service instance.
     * 
     * @return notification service
     */
    public NotificationService getNotificationService() {
        return notificationService;
    }

    /**
     * Gets the ID generator instance.
     * 
     * @return ID generator
     */
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * Initializes the library service with sample data.
     * 
     * @param libraryService the library service to initialize
     */
    public void initializeWithSampleData(ee.tak24.library.service.LibraryService libraryService) {
        try {
            // Create sample members
            Address address1 = createAddress("123 Main St", "Tallinn", "10111", "Estonia");
            Member member1 = createMember("Alice", "Johnson", "alice.johnson@example.com", 
                                        LocalDate.of(1985, 3, 15), address1, MembershipType.PREMIUM);
            
            Address address2 = createAddress("456 Oak Ave", "Tartu", "51014", "Estonia");
            Member member2 = createMember("Bob", "Smith", "bob.smith@example.com", 
                                        LocalDate.of(1992, 7, 22), address2, MembershipType.STUDENT);
            
            // Create sample books
            Book book1 = createBook("978-0-123456-78-9", "Java Programming Guide", 
                                  "John Author", "Tech Publishers", 
                                  LocalDate.of(2023, 1, 15), BookCategory.TECHNOLOGY, 5);
            
            Book book2 = createBook("978-0-123456-79-6", "Data Structures and Algorithms", 
                                  "Jane Writer", "Academic Press", 
                                  LocalDate.of(2022, 9, 10), BookCategory.TEXTBOOK, 3);
            
            Book book3 = createBook("978-0-123456-80-2", "The Great Adventure", 
                                  "Fiction Author", "Story Books", 
                                  LocalDate.of(2021, 5, 20), BookCategory.FICTION, 2);
            
            // Register members
            libraryService.registerMember(member1.getFirstName(), member1.getLastName(), 
                                        member1.getEmail(), member1.getDateOfBirth(),
                                        address1.getStreet(), address1.getCity(), 
                                        address1.getPostalCode(), address1.getCountry(), 
                                        member1.getMembershipType());
            
            libraryService.registerMember(member2.getFirstName(), member2.getLastName(), 
                                        member2.getEmail(), member2.getDateOfBirth(),
                                        address2.getStreet(), address2.getCity(), 
                                        address2.getPostalCode(), address2.getCountry(), 
                                        member2.getMembershipType());
            
            // Add books
            libraryService.addBook(book1.getIsbn(), book1.getTitle(), book1.getAuthor(), 
                                 book1.getPublisher(), book1.getPublicationDate(), 
                                 book1.getCategory(), book1.getTotalCopies());
            
            libraryService.addBook(book2.getIsbn(), book2.getTitle(), book2.getAuthor(), 
                                 book2.getPublisher(), book2.getPublicationDate(), 
                                 book2.getCategory(), book2.getTotalCopies());
            
            libraryService.addBook(book3.getIsbn(), book3.getTitle(), book3.getAuthor(), 
                                 book3.getPublisher(), book3.getPublicationDate(), 
                                 book3.getCategory(), book3.getTotalCopies());
            
        } catch (Exception e) {
            // Log error but don't fail the application
            System.err.println("Warning: Failed to initialize sample data: " + e.getMessage());
        }
    }
}

