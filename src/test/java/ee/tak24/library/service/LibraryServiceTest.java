package ee.tak24.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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

/**
 * Comprehensive unit tests for LibraryService.
 * Demonstrates testing best practices and comprehensive coverage.
 */
@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private LibraryRepository repository;

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryServiceImpl(repository);
    }

    // Member Management Tests
    @Test
    void registerMember_ValidData_ShouldCreateMember() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        String street = "123 Main St";
        String city = "Tallinn";
        String postalCode = "10111";
        String country = "Estonia";
        MembershipType membershipType = MembershipType.BASIC;

        // Act
        Member result = libraryService.registerMember(firstName, lastName, email, dateOfBirth,
                                                   street, city, postalCode, country, membershipType);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(membershipType, result.getMembershipType());
        assertTrue(result.isActive());
        verify(repository).saveMember(any(Member.class));
    }

    @Test
    void registerMember_InvalidEmail_ShouldThrowException() {
        // Arrange
        String invalidEmail = "invalid-email";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            libraryService.registerMember("John", "Doe", invalidEmail, LocalDate.of(1990, 1, 1),
                                        "123 Main St", "Tallinn", "10111", "Estonia", MembershipType.BASIC)
        );

        assertEquals("Valid email is required", exception.getMessage());
    }

    @Test
    void findMember_ExistingMember_ShouldReturnMember() {
        // Arrange
        String memberId = "M001";
        Member expectedMember = createSampleMember(memberId);
        when(repository.findMemberById(memberId)).thenReturn(Optional.of(expectedMember));

        // Act
        Optional<Member> result = libraryService.findMember(memberId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedMember, result.get());
    }

    @Test
    void findMember_NonExistingMember_ShouldReturnEmpty() {
        // Arrange
        String memberId = "M999";
        when(repository.findMemberById(memberId)).thenReturn(Optional.empty());

        // Act
        Optional<Member> result = libraryService.findMember(memberId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void updateMember_ExistingMember_ShouldUpdateSuccessfully() {
        // Arrange
        String memberId = "M001";
        Member existingMember = createSampleMember(memberId);
        when(repository.findMemberById(memberId)).thenReturn(Optional.of(existingMember));

        // Act
        boolean result = libraryService.updateMember(memberId, "Jane", "Smith", 
                                                   "jane.smith@example.com", MembershipType.PREMIUM);

        // Assert
        assertTrue(result);
        verify(repository).saveMember(existingMember);
    }

    @Test
    void deactivateMember_ExistingMember_ShouldDeactivateSuccessfully() {
        // Arrange
        String memberId = "M001";
        Member existingMember = createSampleMember(memberId);
        when(repository.findMemberById(memberId)).thenReturn(Optional.of(existingMember));

        // Act
        boolean result = libraryService.deactivateMember(memberId);

        // Assert
        assertTrue(result);
        assertFalse(existingMember.isActive());
        verify(repository).saveMember(existingMember);
    }

    // Book Management Tests
    @Test
    void addBook_ValidData_ShouldCreateBook() {
        // Arrange
        String isbn = "978-0-123456-78-9";
        String title = "Test Book";
        String author = "Test Author";
        String publisher = "Test Publisher";
        LocalDate publicationDate = LocalDate.of(2020, 1, 1);
        BookCategory category = BookCategory.FICTION;
        int totalCopies = 5;

        when(repository.bookExists(isbn)).thenReturn(false);

        // Act
        Book result = libraryService.addBook(isbn, title, author, publisher, 
                                           publicationDate, category, totalCopies);

        // Assert
        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(category, result.getCategory());
        assertEquals(totalCopies, result.getTotalCopies());
        verify(repository).saveBook(any(Book.class));
    }

    @Test
    void addBook_DuplicateIsbn_ShouldThrowException() {
        // Arrange
        String isbn = "978-0-123456-78-9";
        when(repository.bookExists(isbn)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            libraryService.addBook(isbn, "Test Book", "Test Author", "Test Publisher",
                                 LocalDate.of(2020, 1, 1), BookCategory.FICTION, 5)
        );

        assertEquals("Book with ISBN " + isbn + " already exists", exception.getMessage());
    }

    @Test
    void findBook_ExistingBook_ShouldReturnBook() {
        // Arrange
        String isbn = "978-0-123456-78-9";
        Book expectedBook = createSampleBook(isbn);
        when(repository.findBookByIsbn(isbn)).thenReturn(Optional.of(expectedBook));

        // Act
        Optional<Book> result = libraryService.findBook(isbn);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedBook, result.get());
    }

    @Test
    void searchBooks_WithCriteria_ShouldReturnFilteredResults() {
        // Arrange
        Book book1 = createSampleBook("978-0-123456-78-9", "Java Programming", "John Author", BookCategory.TECHNOLOGY);
        Book book2 = createSampleBook("978-0-123456-79-6", "Python Basics", "Jane Author", BookCategory.TECHNOLOGY);
        Book book3 = createSampleBook("978-0-123456-80-2", "Fiction Story", "John Author", BookCategory.FICTION);

        when(repository.findAllBooks()).thenReturn(List.of(book1, book2, book3));

        // Act
        List<Book> result = libraryService.searchBooks("Java", "John", BookCategory.TECHNOLOGY, false);

        // Assert
        assertEquals(1, result.size());
        assertEquals(book1, result.get(0));
    }

    @Test
    void removeBook_BookNotBorrowed_ShouldRemoveSuccessfully() {
        // Arrange
        String isbn = "978-0-123456-78-9";
        when(repository.bookExists(isbn)).thenReturn(true);
        when(repository.findBorrowingRecordsByBook(isbn)).thenReturn(List.of());

        // Act
        boolean result = libraryService.removeBook(isbn);

        // Assert
        assertTrue(result);
        verify(repository).deleteBook(isbn);
    }

    @Test
    void removeBook_BookCurrentlyBorrowed_ShouldThrowException() {
        // Arrange
        String isbn = "978-0-123456-78-9";
        BorrowingRecord activeRecord = new BorrowingRecord("R001", "M001", isbn, 
                                                          LocalDate.now(), LocalDate.now().plusDays(14));
        
        when(repository.bookExists(isbn)).thenReturn(true);
        when(repository.findBorrowingRecordsByBook(isbn)).thenReturn(List.of(activeRecord));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            libraryService.removeBook(isbn)
        );

        assertEquals("Cannot remove book that is currently borrowed", exception.getMessage());
    }

    // Borrowing Operations Tests
    @Test
    void borrowBook_ValidRequest_ShouldCreateBorrowingRecord() throws Exception {
        // Arrange
        String memberId = "M001";
        String bookIsbn = "978-0-123456-78-9";
        LocalDate dueDate = LocalDate.now().plusDays(14);

        Member member = createSampleMember(memberId);
        Book book = createSampleBook(bookIsbn);

        when(repository.findMemberById(memberId)).thenReturn(Optional.of(member));
        when(repository.findBookByIsbn(bookIsbn)).thenReturn(Optional.of(book));

        // Act
        BorrowingRecord result = libraryService.borrowBook(memberId, bookIsbn, dueDate);

        // Assert
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals(bookIsbn, result.getBookIsbn());
        assertEquals(dueDate, result.getDueDate());
        verify(repository).saveBorrowingRecord(any(BorrowingRecord.class));
        verify(repository).saveBook(book);
        verify(repository).saveMember(member);
    }

    @Test
    void borrowBook_MemberNotFound_ShouldThrowException() {
        // Arrange
        String memberId = "M999";
        String bookIsbn = "978-0-123456-78-9";
        LocalDate dueDate = LocalDate.now().plusDays(14);

        when(repository.findMemberById(memberId)).thenReturn(Optional.empty());

        // Act & Assert
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () ->
            libraryService.borrowBook(memberId, bookIsbn, dueDate)
        );

        assertEquals("Member not found", exception.getMessage());
    }

    @Test
    void borrowBook_BookNotFound_ShouldThrowException() {
        // Arrange
        String memberId = "M001";
        String bookIsbn = "978-0-123456-78-9";
        LocalDate dueDate = LocalDate.now().plusDays(14);

        Member member = createSampleMember(memberId);
        when(repository.findMemberById(memberId)).thenReturn(Optional.of(member));
        when(repository.findBookByIsbn(bookIsbn)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () ->
            libraryService.borrowBook(memberId, bookIsbn, dueDate)
        );

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void borrowBook_MemberLimitExceeded_ShouldThrowException() {
        // Arrange
        String memberId = "M001";
        String bookIsbn = "978-0-123456-78-9";
        LocalDate dueDate = LocalDate.now().plusDays(14);

        Member member = createSampleMember(memberId);
        member.setMembershipType(MembershipType.BASIC); // Max 3 books
        // Add borrowing records to reach limit
        for (int i = 0; i < 3; i++) {
            BorrowingRecord record = new BorrowingRecord("R" + i, memberId, "ISBN" + i, 
                                                       LocalDate.now(), LocalDate.now().plusDays(14));
            member.addBorrowingRecord(record);
        }

        Book book = createSampleBook(bookIsbn);

        when(repository.findMemberById(memberId)).thenReturn(Optional.of(member));
        when(repository.findBookByIsbn(bookIsbn)).thenReturn(Optional.of(book));

        // Act & Assert
        MemberLimitExceededException exception = assertThrows(MemberLimitExceededException.class, () ->
            libraryService.borrowBook(memberId, bookIsbn, dueDate)
        );

        assertEquals("Member has reached maximum borrowing limit", exception.getMessage());
    }

    @Test
    void returnBook_ValidRequest_ShouldReturnSuccessfully() {
        // Arrange
        String memberId = "M001";
        String bookIsbn = "978-0-123456-78-9";

        BorrowingRecord activeRecord = new BorrowingRecord("R001", memberId, bookIsbn, 
                                                          LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        Book book = createSampleBook(bookIsbn);

        when(repository.findBorrowingRecordsByMember(memberId)).thenReturn(List.of(activeRecord));
        when(repository.findBookByIsbn(bookIsbn)).thenReturn(Optional.of(book));

        // Act
        boolean result = libraryService.returnBook(memberId, bookIsbn);

        // Assert
        assertTrue(result);
        assertNotNull(activeRecord.getReturnDate());
        verify(repository).saveBorrowingRecord(activeRecord);
        verify(repository).saveBook(book);
    }

    @Test
    void getLibraryStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        when(repository.getTotalMemberCount()).thenReturn(10);
        when(repository.findAllMembers()).thenReturn(createSampleMembers());
        when(repository.getTotalBookCount()).thenReturn(50);
        when(repository.findAvailableBooks()).thenReturn(createSampleBooks());
        when(repository.getTotalBorrowingRecordCount()).thenReturn(25);
        when(repository.getActiveBorrowingCount()).thenReturn(15);
        when(repository.getOverdueBorrowingCount()).thenReturn(3);

        // Act
        LibraryStatistics stats = libraryService.getLibraryStatistics();

        // Assert
        assertNotNull(stats);
        assertEquals(10, stats.getTotalMembers());
        assertEquals(50, stats.getTotalBooks());
        assertEquals(25, stats.getTotalBorrowingRecords());
        assertEquals(15, stats.getActiveBorrowings());
        assertEquals(3, stats.getOverdueBorrowings());
    }

    // Helper methods
    private Member createSampleMember(String memberId) {
        Address address = new Address("123 Main St", "Tallinn", "10111", "Estonia");
        return new Member(memberId, "John", "Doe", "john.doe@example.com", 
                         LocalDate.of(1990, 1, 1), address, MembershipType.BASIC);
    }

    private Book createSampleBook(String isbn) {
        return new Book(isbn, "Test Book", "Test Author", "Test Publisher", 
                       LocalDate.of(2020, 1, 1), BookCategory.FICTION, 5);
    }

    private Book createSampleBook(String isbn, String title, String author, BookCategory category) {
        return new Book(isbn, title, author, "Test Publisher", 
                       LocalDate.of(2020, 1, 1), category, 5);
    }

    private List<Member> createSampleMembers() {
        Address address = new Address("123 Main St", "Tallinn", "10111", "Estonia");
        Member member1 = new Member("M001", "John", "Doe", "john@example.com", 
                                   LocalDate.of(1990, 1, 1), address, MembershipType.BASIC);
        Member member2 = new Member("M002", "Jane", "Smith", "jane@example.com", 
                                   LocalDate.of(1985, 5, 15), address, MembershipType.PREMIUM);
        return List.of(member1, member2);
    }

    private List<Book> createSampleBooks() {
        Book book1 = new Book("978-0-123456-78-9", "Java Programming", "John Author", "Tech Publisher", 
                             LocalDate.of(2020, 1, 1), BookCategory.TECHNOLOGY, 3);
        Book book2 = new Book("978-0-123456-79-6", "Python Basics", "Jane Author", "Tech Publisher", 
                             LocalDate.of(2021, 3, 15), BookCategory.TECHNOLOGY, 2);
        return List.of(book1, book2);
    }
}
