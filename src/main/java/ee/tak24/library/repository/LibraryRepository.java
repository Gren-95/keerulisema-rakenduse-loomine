package ee.tak24.library.repository;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.BorrowingRecord;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for library data access.
 * Demonstrates the Repository pattern and data abstraction.
 */
public interface LibraryRepository {
    
    // Member operations
    void saveMember(Member member);
    Optional<Member> findMemberById(String memberId);
    List<Member> findAllMembers();
    void deleteMember(String memberId);
    boolean memberExists(String memberId);
    
    // Book operations
    void saveBook(Book book);
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findAllBooks();
    List<Book> findBooksByCategory(ee.tak24.library.model.BookCategory category);
    List<Book> findBooksByAuthor(String author);
    List<Book> findAvailableBooks();
    void deleteBook(String isbn);
    boolean bookExists(String isbn);
    
    // Borrowing record operations
    void saveBorrowingRecord(BorrowingRecord record);
    Optional<BorrowingRecord> findBorrowingRecordById(String recordId);
    List<BorrowingRecord> findBorrowingRecordsByMember(String memberId);
    List<BorrowingRecord> findBorrowingRecordsByBook(String bookIsbn);
    List<BorrowingRecord> findActiveBorrowingRecords();
    List<BorrowingRecord> findOverdueBorrowingRecords();
    void deleteBorrowingRecord(String recordId);
    boolean borrowingRecordExists(String recordId);
    
    // Statistics
    int getTotalMemberCount();
    int getTotalBookCount();
    int getTotalBorrowingRecordCount();
    int getActiveBorrowingCount();
    int getOverdueBorrowingCount();
}

