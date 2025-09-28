package ee.tak24.library.observer;

/**
 * Interface for objects that want to listen to library events.
 * Demonstrates the Observer pattern.
 */
public interface LibraryEventListener {
    
    /**
     * Called when a library event occurs.
     * 
     * @param event the library event that occurred
     */
    void onLibraryEvent(LibraryEvent event);
    
    /**
     * Gets the priority of this listener (lower numbers = higher priority).
     * 
     * @return priority level
     */
    default int getPriority() {
        return 0;
    }
    
    /**
     * Checks if this listener is interested in the given event type.
     * 
     * @param eventType the event type to check
     * @return true if interested, false otherwise
     */
    default boolean isInterestedIn(EventType eventType) {
        return true; // By default, interested in all events
    }
}

