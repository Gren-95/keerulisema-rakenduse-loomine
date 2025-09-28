package ee.tak24.library.strategy;

import ee.tak24.library.model.Member;

/**
 * Strategy interface for different notification methods.
 * Demonstrates the Strategy pattern.
 */
public interface NotificationStrategy {
    
    /**
     * Sends a notification to a member.
     * 
     * @param member the member to notify
     * @param message the notification message
     * @return true if notification was sent successfully
     */
    boolean sendNotification(Member member, String message);
    
    /**
     * Gets the name of this notification strategy.
     * 
     * @return strategy name
     */
    String getStrategyName();
    
    /**
     * Checks if this strategy is available for the given member.
     * 
     * @param member the member to check
     * @return true if strategy is available
     */
    boolean isAvailable(Member member);
}

