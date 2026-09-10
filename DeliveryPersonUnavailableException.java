/**
 * DeliveryPersonUnavailableException - עבודה 3 - חלק ו
 * חריגה מותאמת אישית שתיזרק כאשר שליח אינו זמין
 * 
 * שימוש:
 * - ניסיון שיוך שליח שאינו זמין להזמנה
 */
public class DeliveryPersonUnavailableException extends Exception {
    
    private String deliveryPersonId;
    
    public DeliveryPersonUnavailableException(String deliveryPersonId) {
        super("שליח עם ID " + deliveryPersonId + " אינו זמין במצב זה");
        this.deliveryPersonId = deliveryPersonId;
    }
    
  
    
    public DeliveryPersonUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }
}
