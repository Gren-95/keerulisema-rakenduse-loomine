package ee.tak24.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.exception.BookNotAvailableException;
import ee.tak24.library.exception.BookNotFoundException;
import ee.tak24.library.exception.MemberLimitExceededException;
import ee.tak24.library.exception.MemberNotFoundException;
import ee.tak24.library.model.Address;
import ee.tak24.library.model.Book;
import ee.tak24.library.model.BookCategory;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.MembershipType;
import ee.tak24.library.repository.LibraryRepository;
import ee.tak24.library.util.IdGenerator;

/**
 * Implementation of the LibraryService interface.
 * Demonstrates comprehensive business logic, error handling, and logging.
 * 
 * This class follows the Service Layer pattern and implements all business
 * operations for the library management system.
 */
public class LibraryServiceImpl implements LibraryService {
    
    private static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
    
    private final LibraryRepository repository;
    private final IdGenerator idGenerator;

    /**
     * Constructor with dependency injection.
     * 
     * @param repository the data repository
     */
    public LibraryServiceImpl(LibraryRepository repository) {
        this.repository = repository;
        this.idGenerator = new IdGenerator();
    }

    // Member Management
    @Override
    public Member registerMember(String firstName, String lastName, String email, 
                               LocalDate dateOfBirth, String street, String city, 
                               String postalCode, String country, MembershipType membershipType) {
        logger.info("Registering new member: {} {}", firstName, lastName);
        
        validateMemberData(firstName, lastName, email, dateOfBirth, street, city, postalCode, country);
        
        String memberId = idGenerator.generateMemberId();
        Address address = new Address(street, city, postalCode, country);
        Member member = new Member(memberId, firstName, lastName, email, dateOfBirth, address, membershipType);
        
        repository.saveMember(member);
        logger.info("Successfully registered member with ID: {}", memberId);
        
        return member;
    }

    @Override
    public Optional<Member> findMember(String memberId) {
        logger.debug("Finding member with ID: {}", memberId);
        return repository.findMemberById(memberId);
    }

    @Override
    public List<Member> getAllMembers() {
        logger.debug("Retrieving all members");
        return repository.findAllMembers();
    }

    @Override
    public boolean updateMember(String memberId, String firstName, String lastName, 
                              String email, MembershipType membershipType) {
        logger.info("Updating member: {}", memberId);
        
        Optional<Member> memberOpt = repository.findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            logger.warn("Member not found for update: {}", memberId);
            return false;
        }
        
        Member member = memberOpt.get();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setMembershipType(membershipType);
        
        repository.saveMember(member);
        logger.info("Successfully updated member: {}", memberId);
        
        return true;
    }

    @Override
    public boolean deactivateMember(String memberId) {
        logger.info("Deactivating member: {}", memberId);
        
        Optional<Member> memberOpt = repository.findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            logger.warn("Member not found for deactivation: {}", memberId);
            return false;
        }
        
        Member member = memberOpt.get();
        member.setActive(false);
        repository.saveMember(member);
        
        logger.info("Successfully deactivated member: {}", memberId);
        return true;
    }

    // Book Management
    @Override
    public Book addBook(String isbn, String title, String author, String publisher,
                       LocalDate publicationDate, BookCategory category, int totalCopies) {
        logger.info("Adding new book: {} by {}", title, author);
        
        validateBookData(isbn, title, author, publisher, publicationDate, totalCopies);
        
        if (repository.bookExists(isbn)) {
            logger.warn("Book with ISBN {} already exists", isbn);
            throw new IllegalArgumentException("Book with ISBN " + isbn + " already exists");
        }
        
        Book book = new Book(isbn, title, author, publisher, publicationDate, category, totalCopies);
        repository.saveBook(book);
        
        logger.info("Successfully added book with ISBN: {}", isbn);
        return book;
    }

    @Override
    public Optional<Book> findBook(String isbn) {
        logger.debug("Finding book with ISBN: {}", isbn);
        return repository.findBookByIsbn(isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        logger.debug("Retrieving all books");
        return repository.findAllBooks();
    }

    @Override
    public List<Book> searchBooks(String title, String author, BookCategory category, boolean availableOnly) {
        logger.debug("Searching books with criteria - title: {}, author: {}, category: {}, availableOnly: {}", 
                    title, author, category, availableOnly);
        
        List<Book> books = repository.findAllBooks();
        
        return books.stream()
                .filter(book -> title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> category == null || book.getCategory() == category)
                .filter(book -> !availableOnly || book.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateBook(String isbn, String title, String author, String publisher,
                            BookCategory category, int totalCopies) {
        logger.info("Updating book: {}", isbn);
        
        Optional<Book> bookOpt = repository.findBookByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            logger.warn("Book not found for update: {}", isbn);
            return false;
        }
        
        Book book = bookOpt.get();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategory(category);
        book.setTotalCopies(totalCopies);
        
        repository.saveBook(book);
        logger.info("Successfully updated book: {}", isbn);
        
        return true;
    }

    @Override
    public boolean removeBook(String isbn) {
        logger.info("Removing book: {}", isbn);
        
        if (!repository.bookExists(isbn)) {
            logger.warn("Book not found for removal: {}", isbn);
            return false;
        }
        
        // Check if book is currently borrowed
        List<BorrowingRecord> activeBorrowings = repository.findBorrowingRecordsByBook(isbn)
                .stream()
                .filter(record -> record.getReturnDate() == null)
                .collect(Collectors.toList());
        
        if (!activeBorrowings.isEmpty()) {
            logger.warn("Cannot remove book {} - it is currently borrowed", isbn);
            throw new IllegalStateException("Cannot remove book that is currently borrowed");
        }
        
        repository.deleteBook(isbn);
        logger.info("Successfully removed book: {}", isbn);
        
        return true;
    }

    // Borrowing Operations
    @Override
    public BorrowingRecord borrowBook(String memberId, String bookIsbn, LocalDate dueDate) 
            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException, MemberLimitExceededException {
        logger.info("Processing book borrowing - Member: {}, Book: {}", memberId, bookIsbn);
        
        // Validate member exists and is active
        Member member = repository.findMemberById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
        
        if (!member.isActive()) {
            logger.warn("Inactive member attempted to borrow book: {}", memberId);
            throw new MemberLimitExceededException("Member is not active: " + memberId);
        }
        
        // Validate book exists
        Book book = repository.findBookByIsbn(bookIsbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + bookIsbn));
        
        // Check if member can borrow more books
        if (!member.canBorrowMore()) {
            logger.warn("Member {} has reached borrowing limit", memberId);
            throw new MemberLimitExceededException("Member has reached maximum borrowing limit");
        }
        
        // Check if book is available
        if (!book.isAvailable()) {
            logger.warn("Book {} is not available for borrowing", bookIsbn);
            throw new BookNotAvailableException("Book is not available: " + bookIsbn);
        }
        
        // Create borrowing record
        String recordId = idGenerator.generateBorrowingRecordId();
        BorrowingRecord record = new BorrowingRecord(recordId, memberId, bookIsbn, LocalDate.now(), dueDate);
        
        // Update book status
        book.borrow();
        repository.saveBook(book);
        
        // Save borrowing record
        repository.saveBorrowingRecord(record);
        member.addBorrowingRecord(record);
        repository.saveMember(member);
        
        logger.info("Successfully processed borrowing - Record ID: {}", recordId);
        return record;
    }

    @Override
    public boolean returnBook(String memberId, String bookIsbn) {
        logger.info("Processing book return - Member: {}, Book: {}", memberId, bookIsbn);
        
        // Find active borrowing record
        List<BorrowingRecord> activeRecords = repository.findBorrowingRecordsByMember(memberId)
                .stream()
                .filter(record -> record.getBookIsbn().equals(bookIsbn) && record.getReturnDate() == null)
                .collect(Collectors.toList());
        
        if (activeRecords.isEmpty()) {
            logger.warn("No active borrowing record found for member {} and book {}", memberId, bookIsbn);
            return false;
        }
        
        BorrowingRecord record = activeRecords.get(0);
        record.setReturnDate(LocalDate.now());
        repository.saveBorrowingRecord(record);
        
        // Update book status
        Optional<Book> bookOpt = repository.findBookByIsbn(bookIsbn);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.returnBook();
            repository.saveBook(book);
        }
        
        logger.info("Successfully processed return - Record ID: {}", record.getId());
        return true;
    }

    @Override
    public List<BorrowingRecord> getMemberBorrowingHistory(String memberId) {
        logger.debug("Retrieving borrowing history for member: {}", memberId);
        return repository.findBorrowingRecordsByMember(memberId);
    }

    @Override
    public List<BorrowingRecord> getActiveBorrowings() {
        logger.debug("Retrieving all active borrowings");
        return repository.findActiveBorrowingRecords();
    }

    @Override
    public List<BorrowingRecord> getOverdueBorrowings() {
        logger.debug("Retrieving all overdue borrowings");
        return repository.findOverdueBorrowingRecords();
    }

    // Statistics and Reporting
    @Override
    public LibraryStatistics getLibraryStatistics() {
        logger.debug("Generating library statistics");
        
        int totalMembers = repository.getTotalMemberCount();
        int activeMembers = (int) repository.findAllMembers().stream()
                .filter(Member::isActive)
                .count();
        
        int totalBooks = repository.getTotalBookCount();
        int availableBooks = repository.findAvailableBooks().size();
        int borrowedBooks = totalBooks - availableBooks;
        
        int totalBorrowingRecords = repository.getTotalBorrowingRecordCount();
        int activeBorrowings = repository.getActiveBorrowingCount();
        int overdueBorrowings = repository.getOverdueBorrowingCount();
        
        return new LibraryStatistics(totalMembers, activeMembers, totalBooks, 
                                   availableBooks, borrowedBooks, totalBorrowingRecords,
                                   activeBorrowings, overdueBorrowings);
    }

    @Override
    public List<OverdueBookInfo> getOverdueBooks() {
        logger.debug("Retrieving overdue books information");
        
        return repository.findOverdueBorrowingRecords().stream()
                .map(record -> {
                    Book book = repository.findBookByIsbn(record.getBookIsbn()).orElse(null);
                    Member member = repository.findMemberById(record.getMemberId()).orElse(null);
                    return new OverdueBookInfo(book, member, record);
                })
                .filter(info -> info.getBook() != null && info.getMember() != null)
                .collect(Collectors.toList());
    }

    // Private helper methods
    private void validateMemberData(String firstName, String lastName, String email, 
                                  LocalDate dateOfBirth, String street, String city, 
                                  String postalCode, String country) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Valid date of birth is required");
        }
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street address is required");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code is required");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required");
        }
    }

    private void validateBookData(String isbn, String title, String author, String publisher,
                                LocalDate publicationDate, int totalCopies) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher cannot be null or empty");
        }
        if (publicationDate == null || publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Valid publication date is required");
        }
        if (totalCopies <= 0) {
            throw new IllegalArgumentException("Total copies must be positive");
        }
    }
}
