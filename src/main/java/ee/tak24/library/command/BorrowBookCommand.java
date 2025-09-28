package ee.tak24.library.command;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.model.BorrowingRecord;
import ee.tak24.library.service.LibraryService;

/**
 * Command implementation for borrowing a book.
 * Demonstrates the Command pattern with undo functionality.
 */
public class BorrowBookCommand implements LibraryCommand {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowBookCommand.class);
    
    private final String memberId;
    private final String bookIsbn;
    private final LocalDate dueDate;
    private BorrowingRecord createdRecord;

    /**
     * Constructor for the borrow book command.
     * 
     * @param memberId the member's ID
     * @param bookIsbn the book's ISBN
     * @param dueDate the due date for the book
     */
    public BorrowBookCommand(String memberId, String bookIsbn, LocalDate dueDate) {
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.dueDate = dueDate;
    }

    @Override
    public Object execute(LibraryService libraryService) throws Exception {
        logger.info("Executing borrow book command for member {} and book {}", memberId, bookIsbn);
        
        this.createdRecord = libraryService.borrowBook(memberId, bookIsbn, dueDate);
        
        logger.info("Borrow book command executed successfully, record ID: {}", 
                   createdRecord != null ? createdRecord.getId() : "null");
        
        return createdRecord;
    }

    @Override
    public boolean undo(LibraryService libraryService) {
        if (createdRecord == null) {
            logger.warn("Cannot undo borrow book command - no record was created");
            return false;
        }
        
        try {
            logger.info("Undoing borrow book command for record {}", createdRecord.getId());
            
            boolean success = libraryService.returnBook(memberId, bookIsbn);
            
            if (success) {
                logger.info("Borrow book command undone successfully");
            } else {
                logger.warn("Failed to undo borrow book command");
            }
            
            return success;
        } catch (Exception e) {
            logger.error("Error undoing borrow book command", e);
            return false;
        }
    }

    @Override
    public String getDescription() {
        return String.format("Borrow book %s for member %s (due: %s)", 
                           bookIsbn, memberId, dueDate);
    }

    @Override
    public boolean supportsUndo() {
        return true;
    }

    /**
     * Gets the created borrowing record.
     * 
     * @return the borrowing record created by this command
     */
    public BorrowingRecord getCreatedRecord() {
        return createdRecord;
    }
}
