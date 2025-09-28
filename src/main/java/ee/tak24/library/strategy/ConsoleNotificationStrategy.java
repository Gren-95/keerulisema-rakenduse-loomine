package ee.tak24.library.strategy;

import ee.tak24.library.model.Member;

/**
 * Console notification strategy implementation.
 * Demonstrates the Strategy pattern with console output.
 */
public class ConsoleNotificationStrategy implements NotificationStrategy {
    
    @Override
    public boolean sendNotification(Member member, String message) {
        if (!isAvailable(member)) {
            return false;
        }
        
        System.out.println("=== CONSOLE NOTIFICATION ===");
        System.out.println("Member: " + member.getFullName() + " (ID: " + member.getId() + ")");
        System.out.println("Message: " + message);
        System.out.println("=============================");
        
        return true;
    }
    
    @Override
    public String getStrategyName() {
        return "Console";
    }
    
    @Override
    public boolean isAvailable(Member member) {
        return member != null;
    }
}

