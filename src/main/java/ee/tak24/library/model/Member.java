package ee.tak24.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library member (customer).
 * Demonstrates inheritance from Person and composition with borrowing history.
 */
public class Member extends Person {
    private final LocalDate membershipDate;
    private final List<BorrowingRecord> borrowingHistory;
    private MembershipType membershipType;
    private boolean isActive;

    /**
     * Constructor for creating a new Member.
     * 
     * @param id unique identifier for the member
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param dateOfBirth member's date of birth
     * @param address member's address
     * @param membershipType type of membership
     */
    public Member(String id, String firstName, String lastName, String email, 
                 LocalDate dateOfBirth, Address address, MembershipType membershipType) {
        super(id, firstName, lastName, email, dateOfBirth, address);
        this.membershipDate = LocalDate.now();
        this.borrowingHistory = new ArrayList<>();
        this.membershipType = membershipType;
        this.isActive = true;
    }

    /**
     * Gets the member's role in the system.
     * 
     * @return "Member" as the role
     */
    @Override
    public String getRole() {
        return "Member";
    }

    // Getters
    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets an unmodifiable view of the borrowing history.
     * 
     * @return unmodifiable list of borrowing records
     */
    public List<BorrowingRecord> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    // Setters
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Adds a borrowing record to the member's history.
     * 
     * @param record the borrowing record to add
     */
    public void addBorrowingRecord(BorrowingRecord record) {
        borrowingHistory.add(record);
    }

    /**
     * Gets the number of books currently borrowed by this member.
     * 
     * @return count of currently borrowed books
     */
    public long getCurrentBorrowedCount() {
        return borrowingHistory.stream()
                .filter(record -> record.getReturnDate() == null)
                .count();
    }

    /**
     * Checks if the member can borrow more books based on their membership type.
     * 
     * @return true if member can borrow more books
     */
    public boolean canBorrowMore() {
        if (!isActive) {
            return false;
        }
        
        long currentBorrowed = getCurrentBorrowedCount();
        return currentBorrowed < membershipType.getMaxBorrowedBooks();
    }

    /**
     * Gets the membership duration in years.
     * 
     * @return years since membership started
     */
    public int getMembershipDuration() {
        return LocalDate.now().getYear() - membershipDate.getYear();
    }

    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s %s', membershipType=%s, active=%s, borrowed=%d}", 
                           getId(), getFirstName(), getLastName(), membershipType, isActive, getCurrentBorrowedCount());
    }
}

