/**
 * RestaurantNotFoundException - עבודה 3 - חלק ו
 * חריגה מותאמת אישית שתיזרק כאשר מסעדה לא נמצאת
 * 
 * שימושים אפשריים:
 * - ביצוע הזמנה
 * - הצגת מסעדה לפי קוד
 * - שיוך מסעדה למנהל
 */
public class RestaurantNotFoundException extends Exception {
    
    private int restaurantId;
    
    public RestaurantNotFoundException(int restaurantId) {
        super("מסעדה עם ID " + restaurantId + " לא נמצאה במערכת");
        this.restaurantId = restaurantId;
    }
    
    public RestaurantNotFoundException(String message) {
        super(message);
    }
    
    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public int getRestaurantId() {
        return restaurantId;
    }
}
