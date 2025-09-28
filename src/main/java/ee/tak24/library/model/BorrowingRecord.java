package ee.tak24.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a borrowing record for tracking book loans.
 * Demonstrates composition and business logic.
 */
public class BorrowingRecord {
    private final String recordId;
    private final String memberId;
    private final String bookIsbn;
    private final LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double lateFee;

    /**
     * Constructor for creating a new borrowing record.
     * 
     * @param recordId unique identifier for the record
     * @param memberId ID of the member who borrowed the book
     * @param bookIsbn ISBN of the borrowed book
     * @param borrowDate date when the book was borrowed
     * @param dueDate date when the book should be returned
     */
    public BorrowingRecord(String recordId, String memberId, String bookIsbn, 
                          LocalDate borrowDate, LocalDate dueDate) {
        this.recordId = Objects.requireNonNull(recordId, "Record ID cannot be null");
        this.memberId = Objects.requireNonNull(memberId, "Member ID cannot be null");
        this.bookIsbn = Objects.requireNonNull(bookIsbn, "Book ISBN cannot be null");
        this.borrowDate = Objects.requireNonNull(borrowDate, "Borrow date cannot be null");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.returnDate = null; // Not returned yet
        this.lateFee = 0.0;
    }

    // Getters
    public String getRecordId() {
        return recordId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    // Setters
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
    }

    /**
     * Sets the return date for the book.
     * 
     * @param returnDate date when the book was returned
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Gets the record ID.
     * 
     * @return the record ID
     */
    public String getId() {
        return recordId;
    }

    /**
     * Marks the book as returned and calculates any late fees.
     * 
     * @param returnDate date when the book was returned
     * @param lateFeePerDay late fee per day for overdue books
     * @return true if successfully returned, false if already returned
     */
    public boolean returnBook(LocalDate returnDate, double lateFeePerDay) {
        if (isReturned()) {
            return false; // Already returned
        }
        
        this.returnDate = Objects.requireNonNull(returnDate, "Return date cannot be null");
        
        // Calculate late fee if overdue
        if (returnDate.isAfter(dueDate)) {
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            this.lateFee = overdueDays * lateFeePerDay;
        }
        
        return true;
    }

    /**
     * Checks if the book has been returned.
     * 
     * @return true if book is returned
     */
    public boolean isReturned() {
        return returnDate != null;
    }

    /**
     * Checks if the book is currently overdue.
     * 
     * @return true if book is overdue and not returned
     */
    public boolean isOverdue() {
        return !isReturned() && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Gets the number of days the book has been borrowed.
     * 
     * @return number of borrowing days
     */
    public long getBorrowingDays() {
        LocalDate endDate = isReturned() ? returnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(borrowDate, endDate);
    }

    /**
     * Gets the number of days overdue (if any).
     * 
     * @return number of overdue days, 0 if not overdue
     */
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Extends the due date by the specified number of days.
     * 
     * @param additionalDays number of days to extend
     * @return true if successfully extended
     */
    public boolean extendDueDate(int additionalDays) {
        if (isReturned() || additionalDays <= 0) {
            return false;
        }
        
        this.dueDate = this.dueDate.plusDays(additionalDays);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BorrowingRecord that = (BorrowingRecord) obj;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return String.format("BorrowingRecord{id='%s', member='%s', book='%s', borrowed=%s, due=%s, returned=%s, overdue=%s}", 
                           recordId, memberId, bookIsbn, borrowDate, dueDate, returnDate, isOverdue());
    }
}

