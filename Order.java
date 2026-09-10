public class Order {

    //  קבועי סטטוס ]
    public static final int    SENT       = 1;
    public static final int    ON_THE_WAY = 2;
    public static final int    DELIVERED  = 3;
    public static final String NO_RIDER   = "לא משויך";

    // ==================== שדות ====================
    private int        orderId;       // קוד הזמנה
    private int        customerId;    // קוד לקוח
    private Restaurant restaurant;   // מופע מסעדה (פולימורפי!)
    private int        restaurantId;  // קוד מסעדה (שדה נפרד)
    private String     riderId;       // ת"ז שליח
    private int        orderDay;      // תאריך הזמנה
    private int        orderMonth;
    private int        orderYear;
    private int        deliveryDay;   // תאריך מסירה (0=לא נמסר)
    private int        deliveryMonth;
    private int        deliveryYear;
    private double     baseAmount;    // סכום בסיסי
    private double     finalPrice;    // מחיר סופי - מחושב בsetBaseAmount
    private int        status;        // סטטוס: SENT/ON_THE_WAY/DELIVERED

    // בנאי 
    public Order(int orderId, int customerId, Restaurant restaurant,
                 int orderDay, int orderMonth, int orderYear,
                 double baseAmount) {
        this.orderId      = orderId;
        this.customerId   = customerId;
        this.restaurant   = restaurant;
        this.restaurantId = (restaurant != null) ? restaurant.getRestaurantId() : -1;
        this.riderId      = NO_RIDER;
        this.orderDay     = orderDay;
        this.orderMonth   = orderMonth;
        this.orderYear    = orderYear;
        this.deliveryDay   = 0;
        this.deliveryMonth = 0;
        this.deliveryYear  = 0;
        this.status       = SENT;
        setBaseAmount(baseAmount); // חישוב מחיר סופי נעשה כאן!
    }

    //  מתודות עסקיות 

    /**
     * חישוב מחיר סופי בתוך ה-Setter!
     * קריאה פולימורפית - מחשב לפי סוג המסעדה בזמן ריצה.
     */
    public void setBaseAmount(double baseAmount) {
        if (baseAmount < 0) {
            System.out.println("שגיאה: סכום לא יכול להיות שלילי");
            return;
        }
        this.baseAmount = baseAmount;
        if (this.restaurant != null)
            this.finalPrice = this.restaurant.calculateFinalPrice(baseAmount);
        else
            this.finalPrice = baseAmount;
    }

    /** עדכון תאריך מסירה */
    public void setDeliveryDate(int day, int month, int year) {
        this.deliveryDay   = day;
        this.deliveryMonth = month;
        this.deliveryYear  = year;
    }

    /** מחרוזת תאריך הזמנה */
    public String getOrderDateStr() {
        return orderDay + "/" + orderMonth + "/" + orderYear;
    }

    /** מחרוזת תאריך מסירה */
    public String getDeliveryDateStr() {
        if (deliveryDay == 0) return "טרם נמסר";
        return deliveryDay + "/" + deliveryMonth + "/" + deliveryYear;
    }

    /** מחרוזת סטטוס */
    public String getStatusStr() {
        if (status == SENT)       return "נשלח";
        if (status == ON_THE_WAY) return "בדרך";
        if (status == DELIVERED)  return "נמסר";
        return "לא ידוע";
    }

    //  Getters 
    public int        getOrderId()       { return orderId; }
    public int        getCustomerId()    { return customerId; }
    public Restaurant getRestaurant()    { return restaurant; }
    public int        getRestaurantId()  { return restaurantId; }
    public String     getRiderId()       { return riderId; }
    public int        getOrderDay()      { return orderDay; }
    public int        getOrderMonth()    { return orderMonth; }
    public int        getOrderYear()     { return orderYear; }
    public double     getBaseAmount()    { return baseAmount; }
    public double     getFinalPrice()    { return finalPrice; }
    public int        getStatus()        { return status; }
    public int[] getOrderDate() {
        return new int[]{getOrderDay(), getOrderMonth(), getOrderYear()};
    }
    //  Setters 
    public void setRiderId(String rid) { this.riderId = rid; }
    public void setStatus(int status)  { this.status = status; }

    public String toString() {
        String rName = (restaurant != null) ? restaurant.getRestaurantName() : "לא ידוע";
        String rType  = (restaurant != null) ? "[" + restaurant.getRestaurantType() + "]" : "";
        return "הזמנה #" + orderId + "\n" +
               "  לקוח:" + customerId +
               " | מסעדה:" + rName + rType + " (קוד:" + restaurantId + ")\n" +
               "  שליח:" + riderId + "\n" +
               "  הוזמן:" + getOrderDateStr() +
               " | נמסר:" + getDeliveryDateStr() + "\n" +
               "  סכום בסיסי:" + baseAmount +
               " | מחיר סופי:" + String.format("%.2f", finalPrice) +
               " | סטטוס:" + getStatusStr();
    }
}
