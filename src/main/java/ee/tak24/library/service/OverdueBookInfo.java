package ee.tak24.library.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.Member;

/**
 * Data class representing information about an overdue book.
 * Demonstrates the Value Object pattern and data aggregation.
 */
public class OverdueBookInfo {
    private final Book book;
    private final Member member;
    private final BorrowingRecord borrowingRecord;
    private final long daysOverdue;

    /**
     * Constructor for overdue book information.
     * 
     * @param book the overdue book
     * @param member the member who borrowed it
     * @param borrowingRecord the borrowing record
     */
    public OverdueBookInfo(Book book, Member member, BorrowingRecord borrowingRecord) {
        this.book = book;
        this.member = member;
        this.borrowingRecord = borrowingRecord;
        this.daysOverdue = ChronoUnit.DAYS.between(borrowingRecord.getDueDate(), LocalDate.now());
    }

    // Getters
    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public BorrowingRecord getBorrowingRecord() {
        return borrowingRecord;
    }

    public long getDaysOverdue() {
        return daysOverdue;
    }

    /**
     * Gets a formatted string representation of the overdue information.
     * 
     * @return formatted string with book, member, and overdue details
     */
    public String getFormattedInfo() {
        return String.format(
            "Book: %s by %s (ISBN: %s)%n" +
            "Borrowed by: %s %s (ID: %s)%n" +
            "Due Date: %s%n" +
            "Days Overdue: %d",
            book.getTitle(), book.getAuthor(), book.getIsbn(),
            member.getFirstName(), member.getLastName(), member.getId(),
            borrowingRecord.getDueDate(),
            daysOverdue
        );
    }

    @Override
    public String toString() {
        return String.format("OverdueBookInfo{book='%s', member='%s %s', daysOverdue=%d}",
                           book.getTitle(), member.getFirstName(), member.getLastName(), daysOverdue);
    }
}
