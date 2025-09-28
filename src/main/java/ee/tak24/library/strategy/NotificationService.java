package ee.tak24.library.strategy;

import ee.tak24.library.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing notifications using different strategies.
 * Demonstrates the Strategy pattern with context.
 */
public class NotificationService {
    private final List<NotificationStrategy> strategies;
    private NotificationStrategy defaultStrategy;

    /**
     * Constructor for creating a notification service.
     */
    public NotificationService() {
        this.strategies = new ArrayList<>();
        this.defaultStrategy = new ConsoleNotificationStrategy();
        
        // Add default strategies
        addStrategy(new ConsoleNotificationStrategy());
        addStrategy(new EmailNotificationStrategy());
    }

    /**
     * Adds a notification strategy.
     * 
     * @param strategy the strategy to add
     */
    public void addStrategy(NotificationStrategy strategy) {
        if (strategy != null && !strategies.contains(strategy)) {
            strategies.add(strategy);
        }
    }

    /**
     * Removes a notification strategy.
     * 
     * @param strategy the strategy to remove
     */
    public void removeStrategy(NotificationStrategy strategy) {
        strategies.remove(strategy);
    }

    /**
     * Sets the default notification strategy.
     * 
     * @param strategy the default strategy
     */
    public void setDefaultStrategy(NotificationStrategy strategy) {
        this.defaultStrategy = strategy;
    }

    /**
     * Sends a notification to a member using the best available strategy.
     * 
     * @param member the member to notify
     * @param message the notification message
     * @return true if notification was sent successfully
     */
    public boolean sendNotification(Member member, String message) {
        if (member == null || message == null || message.trim().isEmpty()) {
            return false;
        }

        // Try each strategy in order until one succeeds
        for (NotificationStrategy strategy : strategies) {
            if (strategy.isAvailable(member)) {
                if (strategy.sendNotification(member, message)) {
                    return true;
                }
            }
        }

        // Fallback to default strategy
        if (defaultStrategy != null && defaultStrategy.isAvailable(member)) {
            return defaultStrategy.sendNotification(member, message);
        }

        return false;
    }

    /**
     * Sends a notification using a specific strategy.
     * 
     * @param member the member to notify
     * @param message the notification message
     * @param strategyName the name of the strategy to use
     * @return true if notification was sent successfully
     */
    public boolean sendNotification(Member member, String message, String strategyName) {
        if (member == null || message == null || message.trim().isEmpty()) {
            return false;
        }

        for (NotificationStrategy strategy : strategies) {
            if (strategy.getStrategyName().equals(strategyName) && strategy.isAvailable(member)) {
                return strategy.sendNotification(member, message);
            }
        }

        return false;
    }

    /**
     * Gets all available strategies for a member.
     * 
     * @param member the member to check
     * @return list of available strategy names
     */
    public List<String> getAvailableStrategies(Member member) {
        List<String> available = new ArrayList<>();
        for (NotificationStrategy strategy : strategies) {
            if (strategy.isAvailable(member)) {
                available.add(strategy.getStrategyName());
            }
        }
        return available;
    }
}

