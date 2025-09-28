package ee.tak24.library.observer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an event that occurs in the library system.
 * Used by the Observer pattern for event notifications.
 */
public class LibraryEvent {
    private final EventType eventType;
    private final String description;
    private final LocalDateTime timestamp;
    private final Object data;

    /**
     * Constructor for creating a library event.
     * 
     * @param eventType type of the event
     * @param description description of the event
     * @param data additional data associated with the event
     */
    public LibraryEvent(EventType eventType, String description, Object data) {
        this.eventType = Objects.requireNonNull(eventType, "Event type cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    // Getters
    public EventType getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("LibraryEvent{type=%s, description='%s', timestamp=%s}", 
                           eventType, description, timestamp);
    }
}

