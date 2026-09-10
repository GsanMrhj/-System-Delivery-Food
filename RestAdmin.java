import java.util.ArrayList;

/**
 * מנהל מסעדה - יורש מ-Admin.
 * עבודה 2: מערך מסעדות הוחלף ב-ArrayList<Restaurant>.
 * מסעדה יכולה להיות תחת יותר ממנהל אחד.
 *
 * חומר נלמד: הורשה, super, ArrayList.
 */
public class RestAdmin extends Admin {

    // עבודה 2: ArrayList במקום מערך
    private ArrayList<Restaurant> restaurants; // מסעדות באחריותו

    // ==================== בנאי ====================
    public RestAdmin(String adminName, String username, String password) {
        super(adminName, username, password);
        this.restaurants = new ArrayList<Restaurant>(); // אתחול רשימה ריקה
    }

    // ==================== מתודות עסקיות ====================

    /**
     * שיוך מסעדה למנהל - בדיקת כפילות
     */
    public boolean addRestaurant(Restaurant restaurant) {
        if (restaurant == null) return false;
        if (isResponsibleFor(restaurant.getRestaurantId())) {
            System.out.println("שגיאה: מסעדה זו כבר משויכת למנהל " + adminName);
            return false;
        }
        restaurants.add(restaurant);
        return true;
    }

    /**
     * האם המנהל אחראי על מסעדה זו?
     */
    public boolean isResponsibleFor(int restaurantId) {
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getRestaurantId() == restaurantId)
                return true;
        }
        return false;
    }

    // ==================== Getters ====================
    public ArrayList<Restaurant> getRestaurants() { return restaurants; }
    public int                   getCount()        { return restaurants.size(); }

    // ==================== toString ====================
    public String toString() {
        String result = "מנהל מסעדה: " + adminName +
                        " | משתמש: " + username +
                        " | מסעדות: " + restaurants.size();
        for (int i = 0; i < restaurants.size(); i++) {
            result += "\n   [" + restaurants.get(i).getRestaurantId() + "] " +
                      restaurants.get(i).getRestaurantName();
        }
        return result;
    }
}
