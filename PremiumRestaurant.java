/**
 * מסעדת יוקרה - יורשת מ-Restaurant.
 * חישוב מחיר: סכום + דמי משלוח + עמלה באחוזים.
 * בדיקת מינימום הזמנה.
 *
 */
public class PremiumRestaurant extends Restaurant {

    private double minOrderAmount;    // עלות מינימום להזמנה
    private double commissionPercent; // אחוז עמלה נוסף (0-100)

    //  בנאי 
    public PremiumRestaurant(int restaurantId, String restaurantName,
                              String cuisineType, double rating,
                              boolean isOpen, double baseDeliveryFee,
                              double minOrderAmount, double commissionPercent) {
        super(restaurantId, restaurantName, cuisineType,
              rating, isOpen, baseDeliveryFee);
        this.minOrderAmount    = minOrderAmount;
        this.commissionPercent = commissionPercent;
    }

    //  פולימורפיזם 
    /**
     * חישוב מחיר: סכום + דמי משלוח + עמלה%.
     */
    public double calculateFinalPrice(double baseAmount) {
        return baseAmount + baseDeliveryFee + (baseAmount * commissionPercent / 100.0);
    }

    /**
     * בדיקת מינימום - נדרסת מהבסיס.
     */
    public boolean isMinimumOrderMet(double baseAmount) {
        return baseAmount >= minOrderAmount;
    }

    public String getRestaurantType() {
        return "מסעדת יוקרה";
    }

    // Getters
    public double getMinOrderAmount()    { return minOrderAmount; }
    public double getCommissionPercent() { return commissionPercent; }

    // Setters
    public void setMinOrderAmount(double min) {
        if (min >= 0) this.minOrderAmount = min;
        else System.out.println("שגיאה: מינימום לא יכול להיות שלילי");
    }
    public void setCommissionPercent(double p) {
        if (p >= 0 && p <= 100) this.commissionPercent = p;
        else System.out.println("שגיאה: עמלה חייבת להיות 0-100");
    }

    public String toString() {
        return super.toString() +
               " | מינימום:" + minOrderAmount +
               " | עמלה:" + commissionPercent + "%";
    }
}
