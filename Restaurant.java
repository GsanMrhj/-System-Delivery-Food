public class Restaurant {

    //  שדות (private - כימוס) 
    protected int     restaurantId;
    protected String  restaurantName;
    protected String  cuisineType;
    protected double  rating;
    protected boolean isOpen;
    protected double  baseDeliveryFee;

    //  בנאי 
    public Restaurant(int restaurantId, String restaurantName,
                      String cuisineType, double rating,
                      boolean isOpen, double baseDeliveryFee) {
        this.restaurantId   = restaurantId;
        this.restaurantName = restaurantName;
        this.cuisineType    = cuisineType;
        this.rating         = rating;
        this.isOpen         = isOpen;
        this.baseDeliveryFee = baseDeliveryFee;
    }

    // מתודות עסקיות 

    /**
     * חישוב מחיר סופי - מסעדה רגילה: סכום + דמי משלוח.
     * מתודה זו נדרסת בתת-מחלקות לפולימורפיזם.
     */
    public double calculateFinalPrice(double baseAmount) {
        return baseAmount + baseDeliveryFee;
    }

    /**
     * בדיקת מינימום הזמנה.
     * תמיד true במסעדה רגילה, נדרסת ב-PremiumRestaurant.
     */
    public boolean isMinimumOrderMet(double baseAmount) {
        return true;
    }

    /** מחזיר סוג מסעדה - נדרס בתת-מחלקות */
    public String getRestaurantType() {
        return "מסעדה רגילה";
    }

    // Getters 
    public int     getRestaurantId()    { return restaurantId; }
    public String  getRestaurantName()  { return restaurantName; }
    public String  getCuisineType()     { return cuisineType; }
    public double  getRating()          { return rating; }
    public boolean isOpen()             { return isOpen; }
    public double  getBaseDeliveryFee() { return baseDeliveryFee; }

    //  Setters 
    public void setRestaurantName(String name) { this.restaurantName = name; }
    public void setCuisineType(String type)    { this.cuisineType = type; }
    public void setOpen(boolean open)          { this.isOpen = open; }

    public void setRating(double rating) {
        if (rating >= 0 && rating <= 5)
            this.rating = rating;
        else
            System.out.println("שגיאה: דירוג חייב להיות 0-5");
    }

    public void setBaseDeliveryFee(double fee) {
        if (fee >= 0)
            this.baseDeliveryFee = fee;
        else
            System.out.println("שגיאה: דמי משלוח לא יכולים להיות שליליים");
    }

    //  toString 
    public String toString() {
        return "[" + getRestaurantType() + "]" +
               " קוד:" + restaurantId +
               " | " + restaurantName +
               " | מטבח:" + cuisineType +
               " | דירוג:" + rating +
               " | פתוח:" + (isOpen ? "כן" : "לא") +
               " | משלוח:" + baseDeliveryFee;
    }
}
