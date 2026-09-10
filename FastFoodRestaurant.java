
public class FastFoodRestaurant extends Restaurant {

    private int    avgPrepTimeMinutes;       // זמן הכנה ממוצע בדקות
    private double expressDeliverySurcharge; // תוספת עלות משלוח מהיר

    //  בנאי 
    public FastFoodRestaurant(int restaurantId, String restaurantName,
                               String cuisineType, double rating,
                               boolean isOpen, double baseDeliveryFee,
                               int avgPrepTimeMinutes,
                               double expressDeliverySurcharge) {
        super(restaurantId, restaurantName, cuisineType,
              rating, isOpen, baseDeliveryFee);
        this.avgPrepTimeMinutes       = avgPrepTimeMinutes;
        this.expressDeliverySurcharge = expressDeliverySurcharge;
    }

    //  פולימורפיזם 
    /**
     * חישוב מחיר: סכום + דמי משלוח + תוספת מהיר.
     */
    public double calculateFinalPrice(double baseAmount) {
        return baseAmount + baseDeliveryFee + expressDeliverySurcharge;
    }

    public String getRestaurantType() {
        return "מסעדה מהירה";
    }

    //  Getters 
    public int    getAvgPrepTimeMinutes()       { return avgPrepTimeMinutes; }
    public double getExpressDeliverySurcharge() { return expressDeliverySurcharge; }

    //  Setters 
    public void setAvgPrepTimeMinutes(int minutes) {
        if (minutes > 0) this.avgPrepTimeMinutes = minutes;
        else System.out.println("שגיאה: זמן הכנה חייב להיות חיובי");
    }
    public void setExpressDeliverySurcharge(double s) {
        if (s >= 0) this.expressDeliverySurcharge = s;
        else System.out.println("שגיאה: תוספת לא יכולה להיות שלילית");
    }

    public String toString() {
        return super.toString() +
               " | זמן הכנה:" + avgPrepTimeMinutes +
               "ד' | תוספת מהיר:" + expressDeliverySurcharge;
    }
}
