package ee.tak24.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.persistence.JsonLibraryRepository;
import ee.tak24.library.service.LibraryService;
import ee.tak24.library.service.LibraryServiceImpl;
import ee.tak24.library.ui.LibraryCLI;
import ee.tak24.library.util.LibraryFactory;

/**
 * Main application class for the Library Management System.
 * Demonstrates clean main method and proper application initialization.
 * 
 * This class follows the Single Responsibility Principle by only handling
 * application startup and dependency injection.
 */
public class LibraryApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(LibraryApplication.class);
    
    /**
     * Main entry point for the Library Management System.
     * 
     * This method is clean and deployable, following best practices:
     * - No business logic in main method
     * - Proper exception handling
     * - Clean dependency injection
     * - Logging for debugging and monitoring
     * 
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        logger.info("Starting Library Management System...");
        
        try {
            // Initialize dependencies using Factory pattern
            LibraryFactory factory = new LibraryFactory();
            
            // Create repository with JSON persistence
            var repository = new JsonLibraryRepository();
            
            // Create service layer with dependency injection
            LibraryService libraryService = new LibraryServiceImpl(repository);
            
            // Initialize with sample data
            factory.initializeWithSampleData(libraryService);
            
            // Create and start CLI interface
            LibraryCLI cli = new LibraryCLI(libraryService);
            cli.start();
            
        } catch (Exception e) {
            logger.error("Failed to start Library Management System", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        
        logger.info("Library Management System shutdown complete.");
    }
}
