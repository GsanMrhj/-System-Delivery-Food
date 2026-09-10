import java.util.ArrayList;

/**
 * SystemReports - עבודה 3 - חלק ד
 * מחלקה לביצוע דוחות על המערכת בעזרת Wildcards
 * 
 * פונקציות:
 * 1. printRestaurants - ArrayList<? extends Restaurant>
 * 2. sumOrderPrices - ArrayList<? extends Order>
 * 3. addFastFoodRestaurant - ArrayList<? super FastFoodRestaurant>
 * 4. getMax<T> - Generic עבור כל מחלקה Comparable
 */
public class SystemReports {
    
    /**
     * פונקציה 1: הצגת כל המסעדות באוסף
     * מקבלת ArrayList<? extends Restaurant>
     * עובדת עם Restaurant, FastFoodRestaurant, PremiumRestaurant
     */
    public static void printRestaurants(ArrayList<? extends Restaurant> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            System.out.println("אין מסעדות לתצוגה");
            return;
        }
        
        System.out.println("\n=== דוח מסעדות ===");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            System.out.println((i + 1) + ". " + r.getRestaurantName() + 
                             " (ID: " + r.getRestaurantId() + 
                             ", דירוג: " + r.getRating() + 
                             ", סטטוס: " + (r.isOpen() ? "פתוח" : "סגור") + ")");
        }
    }
    
    /**
     * פונקציה 2: חישוב סכום המחירים הסופיים של כל ההזמנות
     * מקבלת ArrayList<? extends Order>
     * מחזירה את סכום המחירים הסופיים
     */
    public static double sumOrderPrices(ArrayList<? extends Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("אין הזמנות לחישוב");
            return 0.0;
        }
        
        double sum = 0.0;
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            sum += o.getFinalPrice();
        }
        
        return sum;
    }
    
    /**
     * פונקציה 3: הוספת מסעדת FastFoodRestaurant לאוסף
     * מקבלת ArrayList<? super FastFoodRestaurant>
     * עובדת עם Restaurant או FastFoodRestaurant (אבא או עצמה)
     */
    public static void addFastFoodRestaurant(ArrayList<? super FastFoodRestaurant> restaurants,
                                            FastFoodRestaurant restaurant) {
        if (restaurants == null) {
            System.out.println("שגיאה: רשימה ריקה");
            return;
        }
        
        if (restaurant == null) {
            System.out.println("שגיאה: מסעדה null");
            return;
        }
        
        restaurants.add(restaurant);
        System.out.println("✓ מסעדה " + restaurant.getRestaurantName() + " נוספה בהצלחה");
    }
    
    /**
     * פונקציה 4: מציאת האובייקט הגדול ביותר מתוך אוסף
     * Generic עבור כל מחלקה שמממשת Comparable
     * מחזירה את האובייקט עם הערך הגבוה ביותר
     */
    public static <T extends Comparable<T>> T getMax(ArrayList<T> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("אין פריטים לחיפוש");
            return null;
        }
        
        T max = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        
        return max;
    }
    
    /**
     * פונקציה עזר: הצגת דוח מפורט על סדר ממויין
     * משמשת להצגה אחידה של תוצאות מיון
     */
    public static <T> void displaySortedList(ArrayList<T> items, String title) {
        if (items == null || items.isEmpty()) {
            System.out.println(title + ": אין פריטים");
            return;
        }
        
        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).toString());
        }
    }
}
