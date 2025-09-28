package ee.tak24.library.util;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs.
 * Demonstrates singleton pattern and thread-safe ID generation.
 */
public class IdGenerator {
    private static final IdGenerator INSTANCE = new IdGenerator();
    private final AtomicLong counter = new AtomicLong(1);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Public constructor for creating new instances.
     */
    public IdGenerator() {
        // This constructor is needed for dependency injection
    }

    /**
     * Gets the singleton instance of IdGenerator.
     * 
     * @return the singleton instance
     */
    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Generates a unique ID with a prefix.
     * 
     * @param prefix the prefix for the ID
     * @return unique ID string
     */
    public String generateId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = "ID";
        }
        
        long timestamp = System.currentTimeMillis();
        long sequence = counter.getAndIncrement();
        
        return String.format("%s_%d_%d", prefix.toUpperCase(), timestamp, sequence);
    }

    /**
     * Generates a unique member ID.
     * 
     * @return unique member ID
     */
    public String generateMemberId() {
        return generateId("MEM");
    }

    /**
     * Generates a unique book ID.
     * 
     * @return unique book ID
     */
    public String generateBookId() {
        return generateId("BOOK");
    }

    /**
     * Generates a unique borrowing record ID.
     * 
     * @return unique borrowing record ID
     */
    public String generateBorrowingRecordId() {
        return generateId("BORROW");
    }

    /**
     * Generates a unique event ID.
     * 
     * @return unique event ID
     */
    public String generateEventId() {
        return generateId("EVT");
    }

    /**
     * Resets the counter (useful for testing).
     */
    public void reset() {
        counter.set(1);
    }
}

