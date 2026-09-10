/**
 * CustomerNotFoundException - עבודה 3 - חלק ו
 * חריגה מותאמת אישית שתיזרק כאשר לקוח לא נמצא
 * 
 * שימושים אפשריים:
 * - כניסת לקוח למערכת
 * - ביצוע הזמנה
 * - חיפוש לקוח
 */
public class CustomerNotFoundException extends Exception {
    
    private int customerId;
    
    public CustomerNotFoundException(int customerId) {
        super("לקוח עם ID " + customerId + " לא נמצא במערכת");
        this.customerId = customerId;
    }
    
    public CustomerNotFoundException(String message) {
        super(message);
    }
    
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public int getCustomerId() {
        return customerId;
    }
}
