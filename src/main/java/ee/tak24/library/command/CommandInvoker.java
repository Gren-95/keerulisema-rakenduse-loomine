package ee.tak24.library.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.service.LibraryService;

/**
 * Command invoker that manages command execution and undo functionality.
 * Demonstrates the Command pattern with command history and undo support.
 * 
 * This class acts as the invoker in the Command pattern, managing
 * command execution, history, and undo operations.
 */
public class CommandInvoker {
    
    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);
    
    private final LibraryService libraryService;
    private final List<LibraryCommand> commandHistory;
    private final List<LibraryCommand> undoHistory;

    /**
     * Constructor for the command invoker.
     * 
     * @param libraryService the library service to execute commands against
     */
    public CommandInvoker(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.commandHistory = new ArrayList<>();
        this.undoHistory = new ArrayList<>();
    }

    /**
     * Executes a command and adds it to the history.
     * 
     * @param command the command to execute
     * @return the result of the command execution
     * @throws Exception if the command execution fails
     */
    public Object executeCommand(LibraryCommand command) throws Exception {
        logger.info("Executing command: {}", command.getDescription());
        
        try {
            Object result = command.execute(libraryService);
            commandHistory.add(command);
            undoHistory.clear(); // Clear undo history when new command is executed
            
            logger.info("Command executed successfully: {}", command.getDescription());
            return result;
        } catch (Exception e) {
            logger.error("Command execution failed: {}", command.getDescription(), e);
            throw e;
        }
    }

    /**
     * Undoes the last executed command.
     * 
     * @return true if the undo was successful
     */
    public boolean undoLastCommand() {
        if (commandHistory.isEmpty()) {
            logger.warn("No commands to undo");
            return false;
        }
        
        LibraryCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
        
        if (!lastCommand.supportsUndo()) {
            logger.warn("Command does not support undo: {}", lastCommand.getDescription());
            commandHistory.add(lastCommand); // Put it back
            return false;
        }
        
        try {
            logger.info("Undoing command: {}", lastCommand.getDescription());
            
            boolean success = lastCommand.undo(libraryService);
            
            if (success) {
                undoHistory.add(lastCommand);
                logger.info("Command undone successfully: {}", lastCommand.getDescription());
            } else {
                logger.warn("Failed to undo command: {}", lastCommand.getDescription());
                commandHistory.add(lastCommand); // Put it back if undo failed
            }
            
            return success;
        } catch (Exception e) {
            logger.error("Error undoing command: {}", lastCommand.getDescription(), e);
            commandHistory.add(lastCommand); // Put it back if undo failed
            return false;
        }
    }

    /**
     * Redoes the last undone command.
     * 
     * @return the result of the command execution, or null if no commands to redo
     * @throws Exception if the command execution fails
     */
    public Object redoLastCommand() throws Exception {
        if (undoHistory.isEmpty()) {
            logger.warn("No commands to redo");
            return null;
        }
        
        LibraryCommand commandToRedo = undoHistory.remove(undoHistory.size() - 1);
        
        logger.info("Redoing command: {}", commandToRedo.getDescription());
        
        Object result = commandToRedo.execute(libraryService);
        commandHistory.add(commandToRedo);
        
        logger.info("Command redone successfully: {}", commandToRedo.getDescription());
        return result;
    }

    /**
     * Gets the command history.
     * 
     * @return a copy of the command history
     */
    public List<LibraryCommand> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    /**
     * Gets the undo history.
     * 
     * @return a copy of the undo history
     */
    public List<LibraryCommand> getUndoHistory() {
        return new ArrayList<>(undoHistory);
    }

    /**
     * Clears all command history.
     */
    public void clearHistory() {
        logger.info("Clearing command history");
        commandHistory.clear();
        undoHistory.clear();
    }

    /**
     * Gets the number of commands in history.
     * 
     * @return the number of executed commands
     */
    public int getCommandCount() {
        return commandHistory.size();
    }

    /**
     * Gets the number of commands that can be undone.
     * 
     * @return the number of undoable commands
     */
    public int getUndoableCommandCount() {
        return (int) commandHistory.stream()
                .filter(LibraryCommand::supportsUndo)
                .count();
    }

    /**
     * Gets the number of commands that can be redone.
     * 
     * @return the number of commands in undo history
     */
    public int getRedoableCommandCount() {
        return undoHistory.size();
    }
}
