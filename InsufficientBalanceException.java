/**
 * InsufficientBalanceException - עבודה 3 - חלק ו
 * חריגה מותאמת אישית שתיזרק כאשר לקוח אין מספיק כסף ביתרה
 * 
 * שימוש:
 * - ביצוע הזמנה כאשר היתרה אינה מספיקה
 */
public class InsufficientBalanceException extends Exception {
    
    private double requiredAmount;
    private double availableBalance;
    
    public InsufficientBalanceException(double requiredAmount, double availableBalance) {
        super("יתרה לא מספיקה. נדרש: " + requiredAmount + 
              ", זמין: " + availableBalance);
        this.requiredAmount = requiredAmount;
        this.availableBalance = availableBalance;
    }
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
    
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public double getRequiredAmount() {
        return requiredAmount;
    }
    
    public double getAvailableBalance() {
        return availableBalance;
    }
    
    public double getShortfall() {
        return requiredAmount - availableBalance;
    }
}
