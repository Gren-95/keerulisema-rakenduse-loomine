package ee.tak24.library.command;

import ee.tak24.library.service.LibraryService;

/**
 * Command interface for library operations.
 * Demonstrates the Command pattern for encapsulating operations.
 * 
 * This interface allows operations to be encapsulated as objects,
 * enabling undo functionality, queuing, and logging of operations.
 */
public interface LibraryCommand {
    
    /**
     * Executes the command.
     * 
     * @param libraryService the library service to execute the command against
     * @return the result of the command execution
     * @throws Exception if the command execution fails
     */
    Object execute(LibraryService libraryService) throws Exception;
    
    /**
     * Undoes the command (if supported).
     * 
     * @param libraryService the library service to undo the command against
     * @return true if the undo was successful
     * @throws UnsupportedOperationException if undo is not supported
     */
    default boolean undo(LibraryService libraryService) {
        throw new UnsupportedOperationException("Undo not supported for this command");
    }
    
    /**
     * Gets a description of the command.
     * 
     * @return command description
     */
    String getDescription();
    
    /**
     * Checks if the command supports undo operation.
     * 
     * @return true if undo is supported
     */
    default boolean supportsUndo() {
        return false;
    }
}
