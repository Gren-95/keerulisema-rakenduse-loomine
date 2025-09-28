package ee.tak24.library.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.BookCategory;
import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.MembershipType;
import ee.tak24.library.service.LibraryService;
import ee.tak24.library.service.LibraryStatistics;
import ee.tak24.library.service.OverdueBookInfo;

/**
 * Command Line Interface for the Library Management System.
 * Demonstrates user interaction patterns and input validation.
 * 
 * This class provides a clean, user-friendly interface for all library operations
 * and follows the Command pattern for handling user actions.
 */
public class LibraryCLI {
    
    private static final Logger logger = LoggerFactory.getLogger(LibraryCLI.class);
    
    private final LibraryService libraryService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean running = true;

    /**
     * Constructor for the CLI.
     * 
     * @param libraryService the library service to use
     */
    public LibraryCLI(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the CLI application.
     */
    public void start() {
        logger.info("Starting Library CLI interface");
        
        printWelcomeMessage();
        
        while (running) {
            try {
                printMainMenu();
                int choice = getIntInput("Enter your choice: ");
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                logger.error("Error in CLI main loop", e);
                System.err.println("An error occurred: " + e.getMessage());
                // Add a small delay to prevent rapid error loops
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        scanner.close();
        logger.info("Library CLI interface stopped");
    }

    private void printWelcomeMessage() {
        System.out.println("=".repeat(60));
        System.out.println("    Welcome to the Library Management System");
        System.out.println("    Developed for TAK24 - Complex Application Development");
        System.out.println("=".repeat(60));
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1.  Member Management");
        System.out.println("2.  Book Management");
        System.out.println("3.  Borrowing Operations");
        System.out.println("4.  Reports & Statistics");
        System.out.println("5.  Search Books");
        System.out.println("0.  Exit");
        System.out.println("q.  Quit (same as 0)");
        System.out.println("=".repeat(40));
        System.out.println("💡 Tip: Press 'q' at any time to go back or exit!");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleMemberManagement();
            case 2 -> handleBookManagement();
            case 3 -> handleBorrowingOperations();
            case 4 -> handleReportsAndStatistics();
            case 5 -> handleSearchBooks();
            case 0 -> {
                System.out.println("Thank you for using the Library Management System!");
                running = false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleMemberManagement() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    MEMBER MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Register New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Find Member by ID");
            System.out.println("4. Update Member");
            System.out.println("5. Deactivate Member");
            System.out.println("0. Back to Main Menu");
            System.out.println("q. Quit (same as 0)");
            System.out.println("-".repeat(30));

            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> registerNewMember();
                case 2 -> viewAllMembers();
                case 3 -> findMemberById();
                case 4 -> updateMember();
                case 5 -> deactivateMember();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerNewMember() {
        System.out.println("\n--- Register New Member ---");
        System.out.println("💡 Tip: Press 'q' at any time to cancel and go back");
        
        try {
            String firstName = getStringInput("First Name: ");
            if (firstName.equals("QUIT")) return;
            
            String lastName = getStringInput("Last Name: ");
            if (lastName.equals("QUIT")) return;
            
            String email = getStringInput("Email: ");
            if (email.equals("QUIT")) return;
            
            LocalDate dateOfBirth = getDateInput("Date of Birth (yyyy-MM-dd): ");
            if (dateOfBirth == null) return;
            
            System.out.println("\nAddress Information:");
            String street = getStringInput("Street: ");
            if (street.equals("QUIT")) return;
            
            String city = getStringInput("City: ");
            if (city.equals("QUIT")) return;
            
            String postalCode = getStringInput("Postal Code: ");
            if (postalCode.equals("QUIT")) return;
            
            String country = getStringInput("Country: ");
            if (country.equals("QUIT")) return;
            
            System.out.println("\nMembership Types:");
            for (MembershipType type : MembershipType.values()) {
            System.out.printf("%d. %s (Max %d books, %d days)%n", 
                type.ordinal() + 1, type.getDisplayName(), 
                type.getMaxBorrowedBooks(), type.getMaxBorrowingDays());
            }
            
            int typeChoice = getIntInput("Select membership type (1-" + MembershipType.values().length + "): ");
            MembershipType membershipType = MembershipType.values()[typeChoice - 1];
            
            Member member = libraryService.registerMember(firstName, lastName, email, dateOfBirth,
                                                        street, city, postalCode, country, membershipType);
            
            System.out.println("\n✓ Member registered successfully!");
            System.out.println("Member ID: " + member.getId());
            System.out.println("Membership Type: " + member.getMembershipType().getDisplayName());
            
        } catch (Exception e) {
            System.out.println("✗ Error registering member: " + e.getMessage());
        }
    }

    private void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        
        List<Member> members = libraryService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-20s %-15s %-10s%n", 
                         "ID", "Name", "Email", "Membership", "Status");
        System.out.println("-".repeat(80));
        
        for (Member member : members) {
            System.out.printf("%-10s %-20s %-20s %-15s %-10s%n",
                             member.getId(),
                             member.getFirstName() + " " + member.getLastName(),
                             member.getEmail(),
                             member.getMembershipType().getDisplayName(),
                             member.isActive() ? "Active" : "Inactive");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Press 'q' to go back to main menu");
        System.out.println("=".repeat(50));
        
        String input = getStringInput("");
        // No need to check for 'q' here since we're going back anyway
    }

    private void findMemberById() {
        System.out.println("\n--- Find Member by ID ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        var memberOpt = libraryService.findMember(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            System.out.println("\nMember Details:");
            System.out.println("ID: " + member.getId());
            System.out.println("Name: " + member.getFirstName() + " " + member.getLastName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Membership Type: " + member.getMembershipType().getDisplayName());
            System.out.println("Status: " + (member.isActive() ? "Active" : "Inactive"));
            System.out.println("Currently Borrowed: " + member.getCurrentBorrowedCount());
        } else {
            System.out.println("Member not found.");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Press 'q' to go back to main menu");
        System.out.println("=".repeat(50));
        
        String input = getStringInput("");
        // No need to check for 'q' here since we're going back anyway
    }

    private void updateMember() {
        System.out.println("\n--- Update Member ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        var memberOpt = libraryService.findMember(memberId);
        if (memberOpt.isEmpty()) {
            System.out.println("Member not found.");
            return;
        }
        
        try {
            String firstName = getStringInput("New First Name: ");
            String lastName = getStringInput("New Last Name: ");
            String email = getStringInput("New Email: ");
            
            System.out.println("\nMembership Types:");
            for (MembershipType type : MembershipType.values()) {
                System.out.printf("%d. %s%n", type.ordinal() + 1, type.getDisplayName());
            }
            
            int typeChoice = getIntInput("Select new membership type (1-" + MembershipType.values().length + "): ");
            MembershipType membershipType = MembershipType.values()[typeChoice - 1];
            
            boolean success = libraryService.updateMember(memberId, firstName, lastName, email, membershipType);
            
            if (success) {
                System.out.println("✓ Member updated successfully!");
            } else {
                System.out.println("✗ Failed to update member.");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error updating member: " + e.getMessage());
        }
    }

    private void deactivateMember() {
        System.out.println("\n--- Deactivate Member ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        boolean success = libraryService.deactivateMember(memberId);
        if (success) {
            System.out.println("✓ Member deactivated successfully!");
        } else {
            System.out.println("✗ Member not found or already inactive.");
        }
    }

    private void handleBookManagement() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    BOOK MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Find Book by ISBN");
            System.out.println("4. Update Book");
            System.out.println("5. Remove Book");
            System.out.println("0. Back to Main Menu");
            System.out.println("q. Quit (same as 0)");
            System.out.println("-".repeat(30));

            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addNewBook();
                case 2 -> viewAllBooks();
                case 3 -> findBookByIsbn();
                case 4 -> updateBook();
                case 5 -> removeBook();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addNewBook() {
        System.out.println("\n--- Add New Book ---");
        
        try {
            String isbn = getStringInput("ISBN: ");
            String title = getStringInput("Title: ");
            String author = getStringInput("Author: ");
            String publisher = getStringInput("Publisher: ");
            LocalDate publicationDate = getDateInput("Publication Date (yyyy-MM-dd): ");
            
            System.out.println("\nBook Categories:");
            for (BookCategory category : BookCategory.values()) {
                System.out.printf("%d. %s%n", category.ordinal() + 1, category.getDisplayName());
            }
            
            int categoryChoice = getIntInput("Select category (1-" + BookCategory.values().length + "): ");
            BookCategory category = BookCategory.values()[categoryChoice - 1];
            
            int totalCopies = getIntInput("Total Copies: ");
            
            Book book = libraryService.addBook(isbn, title, author, publisher, 
                                             publicationDate, category, totalCopies);
            
            System.out.println("\n✓ Book added successfully!");
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Available Copies: " + book.getAvailableCopies() + "/" + book.getTotalCopies());
            
        } catch (Exception e) {
            System.out.println("✗ Error adding book: " + e.getMessage());
        }
    }

    private void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        
        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        
        System.out.printf("%-15s %-30s %-20s %-15s %-10s%n", 
                         "ISBN", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(100));
        
        for (Book book : books) {
            System.out.printf("%-15s %-30s %-20s %-15s %-10s%n",
                             book.getIsbn(),
                             book.getTitle().length() > 30 ? book.getTitle().substring(0, 27) + "..." : book.getTitle(),
                             book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor(),
                             book.getCategory().getDisplayName(),
                             book.getStatus().getDisplayName());
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Press 'q' to go back to main menu");
        System.out.println("=".repeat(50));
        
        String input = getStringInput("");
        // No need to check for 'q' here since we're going back anyway
    }

    private void findBookByIsbn() {
        System.out.println("\n--- Find Book by ISBN ---");
        String isbn = getStringInput("Enter ISBN: ");
        
        var bookOpt = libraryService.findBook(isbn);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            System.out.println("\nBook Details:");
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publisher: " + book.getPublisher());
            System.out.println("Category: " + book.getCategory().getDisplayName());
            System.out.println("Status: " + book.getStatus().getDisplayName());
            System.out.println("Available: " + book.getAvailableCopies() + "/" + book.getTotalCopies());
            System.out.println("Age: " + book.getAge() + " years");
        } else {
            System.out.println("Book not found.");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Press 'q' to go back to main menu");
        System.out.println("=".repeat(50));
        
        String input = getStringInput("");
        // No need to check for 'q' here since we're going back anyway
    }

    private void updateBook() {
        System.out.println("\n--- Update Book ---");
        String isbn = getStringInput("Enter ISBN: ");
        
        var bookOpt = libraryService.findBook(isbn);
        if (bookOpt.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }
        
        try {
            String title = getStringInput("New Title: ");
            String author = getStringInput("New Author: ");
            String publisher = getStringInput("New Publisher: ");
            
            System.out.println("\nBook Categories:");
            for (BookCategory category : BookCategory.values()) {
                System.out.printf("%d. %s%n", category.ordinal() + 1, category.getDisplayName());
            }
            
            int categoryChoice = getIntInput("Select new category (1-" + BookCategory.values().length + "): ");
            BookCategory category = BookCategory.values()[categoryChoice - 1];
            
            int totalCopies = getIntInput("New Total Copies: ");
            
            boolean success = libraryService.updateBook(isbn, title, author, publisher, category, totalCopies);
            
            if (success) {
                System.out.println("✓ Book updated successfully!");
            } else {
                System.out.println("✗ Failed to update book.");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error updating book: " + e.getMessage());
        }
    }

    private void removeBook() {
        System.out.println("\n--- Remove Book ---");
        String isbn = getStringInput("Enter ISBN: ");
        
        try {
            boolean success = libraryService.removeBook(isbn);
            if (success) {
                System.out.println("✓ Book removed successfully!");
            } else {
                System.out.println("✗ Book not found or cannot be removed.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error removing book: " + e.getMessage());
        }
    }

    private void handleBorrowingOperations() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("  BORROWING OPERATIONS");
            System.out.println("-".repeat(30));
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Member Borrowing History");
            System.out.println("4. View Active Borrowings");
            System.out.println("5. View Overdue Books");
            System.out.println("0. Back to Main Menu");
            System.out.println("q. Quit (same as 0)");
            System.out.println("-".repeat(30));

            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> viewMemberBorrowingHistory();
                case 4 -> viewActiveBorrowings();
                case 5 -> viewOverdueBooks();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        
        try {
            String memberId = getStringInput("Member ID: ");
            String bookIsbn = getStringInput("Book ISBN: ");
            LocalDate dueDate = getDateInput("Due Date (yyyy-MM-dd): ");
            
            var borrowingRecord = libraryService.borrowBook(memberId, bookIsbn, dueDate);
            
            System.out.println("\n✓ Book borrowed successfully!");
            System.out.println("Borrowing Record ID: " + borrowingRecord.getId());
            System.out.println("Due Date: " + borrowingRecord.getDueDate());
            
        } catch (Exception e) {
            System.out.println("✗ Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook() {
        System.out.println("\n--- Return Book ---");
        
        String memberId = getStringInput("Member ID: ");
        String bookIsbn = getStringInput("Book ISBN: ");
        
        boolean success = libraryService.returnBook(memberId, bookIsbn);
        if (success) {
            System.out.println("✓ Book returned successfully!");
        } else {
            System.out.println("✗ No active borrowing found for this member and book.");
        }
    }

    private void viewMemberBorrowingHistory() {
        System.out.println("\n--- Member Borrowing History ---");
        String memberId = getStringInput("Enter Member ID: ");
        
        List<BorrowingRecord> history = libraryService.getMemberBorrowingHistory(memberId);
        if (history.isEmpty()) {
            System.out.println("No borrowing history found for this member.");
            return;
        }
        
        System.out.printf("%-15s %-15s %-12s %-12s %-12s%n", 
                         "Record ID", "Book ISBN", "Borrow Date", "Due Date", "Return Date");
        System.out.println("-".repeat(80));
        
        for (BorrowingRecord record : history) {
            System.out.printf("%-15s %-15s %-12s %-12s %-12s%n",
                             record.getId(),
                             record.getBookIsbn(),
                             record.getBorrowDate(),
                             record.getDueDate(),
                             record.getReturnDate() != null ? record.getReturnDate() : "Not returned");
        }
    }

    private void viewActiveBorrowings() {
        System.out.println("\n--- Active Borrowings ---");
        
        List<BorrowingRecord> activeBorrowings = libraryService.getActiveBorrowings();
        if (activeBorrowings.isEmpty()) {
            System.out.println("No active borrowings found.");
            return;
        }
        
        System.out.printf("%-15s %-15s %-12s %-12s%n", 
                         "Record ID", "Book ISBN", "Borrow Date", "Due Date");
        System.out.println("-".repeat(60));
        
        for (BorrowingRecord record : activeBorrowings) {
            System.out.printf("%-15s %-15s %-12s %-12s%n",
                             record.getId(),
                             record.getBookIsbn(),
                             record.getBorrowDate(),
                             record.getDueDate());
        }
    }

    private void viewOverdueBooks() {
        System.out.println("\n--- Overdue Books ---");
        
        List<OverdueBookInfo> overdueBooks = libraryService.getOverdueBooks();
        if (overdueBooks.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }
        
        for (OverdueBookInfo info : overdueBooks) {
            System.out.println("\n" + info.getFormattedInfo());
            System.out.println("-".repeat(50));
        }
    }

    private void handleReportsAndStatistics() {
        System.out.println("\n--- Library Statistics ---");
        
        LibraryStatistics stats = libraryService.getLibraryStatistics();
        System.out.println(stats);
    }

    private void handleSearchBooks() {
        System.out.println("\n--- Search Books ---");
        
        String title = getStringInput("Title (leave empty to skip): ");
        String author = getStringInput("Author (leave empty to skip): ");
        
        System.out.println("\nBook Categories (leave empty to skip):");
        for (BookCategory category : BookCategory.values()) {
            System.out.printf("%d. %s%n", category.ordinal() + 1, category.getDisplayName());
        }
        
        BookCategory category = null;
        String categoryInput = getStringInput("Category number (leave empty to skip): ");
        if (!categoryInput.trim().isEmpty()) {
            try {
                int categoryChoice = Integer.parseInt(categoryInput);
                category = BookCategory.values()[categoryChoice - 1];
            } catch (Exception e) {
                System.out.println("Invalid category choice, searching all categories.");
            }
        }
        
        boolean availableOnly = getBooleanInput("Available books only? (y/n): ");
        
        List<Book> results = libraryService.searchBooks(
            title.isEmpty() ? null : title,
            author.isEmpty() ? null : author,
            category,
            availableOnly
        );
        
        if (results.isEmpty()) {
            System.out.println("No books found matching your criteria.");
            return;
        }
        
        System.out.println("\nSearch Results (" + results.size() + " books found):");
        System.out.printf("%-15s %-30s %-20s %-15s %-10s%n", 
                         "ISBN", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(100));
        
        for (Book book : results) {
            System.out.printf("%-15s %-30s %-20s %-15s %-10s%n",
                             book.getIsbn(),
                             book.getTitle().length() > 30 ? book.getTitle().substring(0, 27) + "..." : book.getTitle(),
                             book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor(),
                             book.getCategory().getDisplayName(),
                             book.getStatus().getDisplayName());
        }
    }

    // Helper methods for input handling
    private String getStringInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("q")) {
                        return "QUIT"; // Special marker for quit
                    }
                    return input;
                } else {
                    System.out.println("No input available. Please try again.");
                    return "";
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // consume the problematic line
                }
            }
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("q")) {
                        return 0; // Treat 'q' as exit
                    }
                    return Integer.parseInt(input);
                } else {
                    System.out.println("No input available. Please try again.");
                    return 0;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'q' to quit.");
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // consume the problematic line
                }
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("q")) {
                        return null; // Special marker for quit
                    }
                    return LocalDate.parse(input, dateFormatter);
                } else {
                    System.out.println("No input available. Please try again.");
                    return LocalDate.now();
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd (e.g., 2024-01-15) or 'q' to quit.");
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // consume the problematic line
                }
            }
        }
    }

    private boolean getBooleanInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("y") || input.equals("yes")) {
                        return true;
                    } else if (input.equals("n") || input.equals("no")) {
                        return false;
                    } else {
                        System.out.println("Please enter 'y' for yes or 'n' for no.");
                    }
                } else {
                    System.out.println("No input available. Please try again.");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // consume the problematic line
                }
            }
        }
    }
}
