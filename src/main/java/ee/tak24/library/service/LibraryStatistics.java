package ee.tak24.library.service;

import java.time.LocalDate;

/**
 * Data class representing library statistics.
 * Demonstrates the Value Object pattern and data encapsulation.
 */
public class LibraryStatistics {
    private final int totalMembers;
    private final int activeMembers;
    private final int totalBooks;
    private final int availableBooks;
    private final int borrowedBooks;
    private final int totalBorrowingRecords;
    private final int activeBorrowings;
    private final int overdueBorrowings;
    private final LocalDate generatedAt;

    /**
     * Constructor for library statistics.
     * 
     * @param totalMembers total number of members
     * @param activeMembers number of active members
     * @param totalBooks total number of books
     * @param availableBooks number of available books
     * @param borrowedBooks number of currently borrowed books
     * @param totalBorrowingRecords total number of borrowing records
     * @param activeBorrowings number of active borrowings
     * @param overdueBorrowings number of overdue borrowings
     */
    public LibraryStatistics(int totalMembers, int activeMembers, int totalBooks, 
                           int availableBooks, int borrowedBooks, int totalBorrowingRecords,
                           int activeBorrowings, int overdueBorrowings) {
        this.totalMembers = totalMembers;
        this.activeMembers = activeMembers;
        this.totalBooks = totalBooks;
        this.availableBooks = availableBooks;
        this.borrowedBooks = borrowedBooks;
        this.totalBorrowingRecords = totalBorrowingRecords;
        this.activeBorrowings = activeBorrowings;
        this.overdueBorrowings = overdueBorrowings;
        this.generatedAt = LocalDate.now();
    }

    // Getters
    public int getTotalMembers() {
        return totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public int getAvailableBooks() {
        return availableBooks;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getTotalBorrowingRecords() {
        return totalBorrowingRecords;
    }

    public int getActiveBorrowings() {
        return activeBorrowings;
    }

    public int getOverdueBorrowings() {
        return overdueBorrowings;
    }

    public LocalDate getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Calculates the utilization rate of books.
     * 
     * @return utilization rate as a percentage (0.0 to 1.0)
     */
    public double getBookUtilizationRate() {
        if (totalBooks == 0) return 0.0;
        return (double) borrowedBooks / totalBooks;
    }

    /**
     * Calculates the member activity rate.
     * 
     * @return activity rate as a percentage (0.0 to 1.0)
     */
    public double getMemberActivityRate() {
        if (totalMembers == 0) return 0.0;
        return (double) activeMembers / totalMembers;
    }

    @Override
    public String toString() {
        return String.format(
            "Library Statistics (Generated: %s)%n" +
            "Members: %d total, %d active (%.1f%% activity rate)%n" +
            "Books: %d total, %d available, %d borrowed (%.1f%% utilization)%n" +
            "Borrowings: %d total, %d active, %d overdue",
            generatedAt,
            totalMembers, activeMembers, getMemberActivityRate() * 100,
            totalBooks, availableBooks, borrowedBooks, getBookUtilizationRate() * 100,
            totalBorrowingRecords, activeBorrowings, overdueBorrowings
        );
    }
}
