package ee.tak24.library.repository;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.BookCategory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of LibraryRepository.
 * Demonstrates the Repository pattern with in-memory storage.
 */
public class InMemoryLibraryRepository implements LibraryRepository {
    private final Map<String, Member> members = new ConcurrentHashMap<>();
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private final Map<String, BorrowingRecord> borrowingRecords = new ConcurrentHashMap<>();

    // Member operations
    @Override
    public void saveMember(Member member) {
        if (member != null) {
            members.put(member.getId(), member);
        }
    }

    @Override
    public Optional<Member> findMemberById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(members.values());
    }

    @Override
    public void deleteMember(String memberId) {
        members.remove(memberId);
    }

    @Override
    public boolean memberExists(String memberId) {
        return members.containsKey(memberId);
    }

    // Book operations
    @Override
    public void saveBook(Book book) {
        if (book != null) {
            books.put(book.getIsbn(), book);
        }
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> findBooksByCategory(BookCategory category) {
        return books.values().stream()
                .filter(book -> book.getCategory() == category)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBook(String isbn) {
        books.remove(isbn);
    }

    @Override
    public boolean bookExists(String isbn) {
        return books.containsKey(isbn);
    }

    // Borrowing record operations
    @Override
    public void saveBorrowingRecord(BorrowingRecord record) {
        if (record != null) {
            borrowingRecords.put(record.getRecordId(), record);
        }
    }

    @Override
    public Optional<BorrowingRecord> findBorrowingRecordById(String recordId) {
        return Optional.ofNullable(borrowingRecords.get(recordId));
    }

    @Override
    public List<BorrowingRecord> findBorrowingRecordsByMember(String memberId) {
        return borrowingRecords.values().stream()
                .filter(record -> record.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findBorrowingRecordsByBook(String bookIsbn) {
        return borrowingRecords.values().stream()
                .filter(record -> record.getBookIsbn().equals(bookIsbn))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findActiveBorrowingRecords() {
        return borrowingRecords.values().stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findOverdueBorrowingRecords() {
        return borrowingRecords.values().stream()
                .filter(BorrowingRecord::isOverdue)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBorrowingRecord(String recordId) {
        borrowingRecords.remove(recordId);
    }

    @Override
    public boolean borrowingRecordExists(String recordId) {
        return borrowingRecords.containsKey(recordId);
    }

    // Statistics
    @Override
    public int getTotalMemberCount() {
        return members.size();
    }

    @Override
    public int getTotalBookCount() {
        return books.size();
    }

    @Override
    public int getTotalBorrowingRecordCount() {
        return borrowingRecords.size();
    }

    @Override
    public int getActiveBorrowingCount() {
        return (int) borrowingRecords.values().stream()
                .filter(record -> !record.isReturned())
                .count();
    }

    @Override
    public int getOverdueBorrowingCount() {
        return (int) borrowingRecords.values().stream()
                .filter(BorrowingRecord::isOverdue)
                .count();
    }
}

