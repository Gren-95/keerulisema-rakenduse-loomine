package ee.tak24.library.observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Publisher for library events using the Observer pattern.
 * Demonstrates the Observer pattern implementation.
 */
public class LibraryEventPublisher {
    private final List<LibraryEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Adds a listener to receive library events.
     * 
     * @param listener the listener to add
     */
    public void addListener(LibraryEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            // Sort by priority (lower number = higher priority)
            listeners.sort(Comparator.comparingInt(LibraryEventListener::getPriority));
        }
    }

    /**
     * Removes a listener from receiving library events.
     * 
     * @param listener the listener to remove
     */
    public void removeListener(LibraryEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Publishes a library event to all interested listeners.
     * 
     * @param event the event to publish
     */
    public void publishEvent(LibraryEvent event) {
        if (event == null) {
            return;
        }

        // Notify all interested listeners
        for (LibraryEventListener listener : listeners) {
            try {
                if (listener.isInterestedIn(event.getEventType())) {
                    listener.onLibraryEvent(event);
                }
            } catch (Exception e) {
                // Log error but don't stop other listeners
                System.err.println("Error notifying listener " + listener.getClass().getSimpleName() + 
                                 ": " + e.getMessage());
            }
        }
    }

    /**
     * Gets the number of registered listeners.
     * 
     * @return number of listeners
     */
    public int getListenerCount() {
        return listeners.size();
    }

    /**
     * Clears all listeners.
     */
    public void clearListeners() {
        listeners.clear();
    }
}

