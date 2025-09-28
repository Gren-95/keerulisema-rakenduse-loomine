package ee.tak24.library.strategy;

import ee.tak24.library.model.Member;

/**
 * Email notification strategy implementation.
 * Demonstrates the Strategy pattern with email notifications.
 */
public class EmailNotificationStrategy implements NotificationStrategy {
    
    @Override
    public boolean sendNotification(Member member, String message) {
        if (!isAvailable(member)) {
            return false;
        }
        
        // Simulate email sending
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + member.getEmail());
        System.out.println("Subject: Library Notification");
        System.out.println("Message: " + message);
        System.out.println("==========================");
        
        // In a real implementation, this would send an actual email
        return true;
    }
    
    @Override
    public String getStrategyName() {
        return "Email";
    }
    
    @Override
    public boolean isAvailable(Member member) {
        return member != null && 
               member.getEmail() != null && 
               !member.getEmail().trim().isEmpty() &&
               member.getEmail().contains("@");
    }
}

