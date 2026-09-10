import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * מחלקת מסד נתונים מרכזית - עבודה 2.
 * מחליפה את DeliverySystem - משתמשת באוספים במקום מערכים.
 *
 * חומר נלמד: ArrayList, HashMap, Hashtable, פולימורפיזם,
 *             Encapsulation, Getters/Setters.
 */
public class DeliveryDataBase {

    // ==================== שדות ====================

    // מנהל מערכת ראשי
    private Admin systemAdministrator;

    // אוספים ראשיים
    private ArrayList<RestAdmin>  restAdmins;   // מנהלי מסעדות
    private ArrayList<Restaurant> restaurants;  // כל המסעדות (פולימורפי!)
    private ArrayList<Customer>   customers;    // לקוחות
    private ArrayList<Rider>      riders;        // שליחים
    private ArrayList<Order>      orders;        // כל ההזמנות

    // מפות נתונים לפי לקוח
    private HashMap<Integer, ArrayList<Order>>      ordersByCustomer;   // הזמנות לפי קוד לקוח
    private Hashtable<Integer, ArrayList<Restaurant>> restaurantsByCustomer; // מסעדות לפי קוד לקוח
    private HashMap<Integer, Double>                 totalPayments;     // סכום תשלומים לפי קוד לקוח

    // מונה הזמנות
    private int nextOrderId;

    // ==================== בנאי ====================
    /**
     * בנאי - מאתחל את כל האוספים כריקים.
     * יוצר מנהל מערכת ראשי עם username: admin, password: 12345.
     */
    public DeliveryDataBase() {
        systemAdministrator      = new Admin("System Admin", "admin", "12345");
        restAdmins               = new ArrayList<RestAdmin>();
        restaurants              = new ArrayList<Restaurant>();
        customers                = new ArrayList<Customer>();
        riders                   = new ArrayList<Rider>();
        orders                   = new ArrayList<Order>();
        ordersByCustomer         = new HashMap<Integer, ArrayList<Order>>();
        restaurantsByCustomer    = new Hashtable<Integer, ArrayList<Restaurant>>();
        totalPayments            = new HashMap<Integer, Double>();
        nextOrderId              = 1001;
    }

    // ==================== פונקציות חובה ====================

    /**
     * א. הוספת הזמנה ל-HashMap לפי לקוח.
     * אם ההזמנה כבר קיימת - לא מבצע כלום.
     * אם הלקוח לא קיים - יוצר רשימה חדשה.
     */
    public void addOrderToCustomerMap(int customerId, Order order) {
        if (order == null) return;

        // בדיקה אם הלקוח כבר קיים במפה
        if (!ordersByCustomer.containsKey(customerId)) {
            ordersByCustomer.put(customerId, new ArrayList<Order>());
        }

        // בדיקת כפילות
        ArrayList<Order> customerOrders = ordersByCustomer.get(customerId);
        for (int i = 0; i < customerOrders.size(); i++) {
            if (customerOrders.get(i).getOrderId() == order.getOrderId()) {
                return; // כבר קיים - לא מוסיף
            }
        }
        customerOrders.add(order);
    }

    /**
     * עדכון Hashtable של מסעדות לפי לקוח.
     * אם הלקוח לא קיים - יוצר רשימה חדשה.
     * אם המסעדה כבר קיימת ברשימה - לא מוסיף כפילות.
     */
    public void addRestaurantToCustomerMap(int customerId, Restaurant restaurant) {
        if (restaurant == null) return;

        if (!restaurantsByCustomer.containsKey(customerId)) {
            restaurantsByCustomer.put(customerId, new ArrayList<Restaurant>());
        }

        ArrayList<Restaurant> custRests = restaurantsByCustomer.get(customerId);
        for (int i = 0; i < custRests.size(); i++) {
            if (custRests.get(i).getRestaurantId() == restaurant.getRestaurantId()) {
                return; // כבר קיים
            }
        }
        custRests.add(restaurant);
    }

    /**
     * עדכון HashMap של סכומי תשלומים לפי לקוח.
     */
    public void updateTotalPayments(int customerId, double amount) {
        if (!totalPayments.containsKey(customerId)) {
            totalPayments.put(customerId, 0.0);
        }
        totalPayments.put(customerId, totalPayments.get(customerId) + amount);
    }

    /**
     * ב. מחזירה הזמנות של שליח בסטטוס "נשלח" או "בדרך" בלבד.
     */
    public ArrayList<Order> getActiveOrdersByRider(String riderId) {
        ArrayList<Order> result = new ArrayList<Order>();
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getRiderId().equals(riderId) &&
                (o.getStatus() == Order.SENT || o.getStatus() == Order.ON_THE_WAY)) {
                result.add(o);
            }
        }
        return result;
    }

    /**
     * ג. מחזירה מסעדות יוקרה שלקוח הזמין מהן.
     */
    public ArrayList<Restaurant> getPremiumRestaurantsByCustomer(Customer customer) {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        if (customer == null) return result;

        ArrayList<Restaurant> custRests = restaurantsByCustomer.get(customer.getCustomerId());
        if (custRests == null) return result;

        for (int i = 0; i < custRests.size(); i++) {
            if (custRests.get(i) instanceof PremiumRestaurant) {
                result.add(custRests.get(i));
            }
        }
        return result;
    }

    /**
     * ד. מחזירה הלקוח עם הכי הרבה הזמנות (מניחים שיש רק אחד כזה).
     */
    public Customer getTopCustomer() {
        Customer topCustomer = null;
        int      maxOrders   = -1;

        for (int i = 0; i < customers.size(); i++) {
            int custId = customers.get(i).getCustomerId();
            ArrayList<Order> custOrders = ordersByCustomer.get(custId);
            int count = (custOrders != null) ? custOrders.size() : 0;
            if (count > maxOrders) {
                maxOrders   = count;
                topCustomer = customers.get(i);
            }
        }
        return topCustomer;
    }

    /**
     * ה. מחזירה השליח עם הכי הרבה משלוחים (מניחים שיש רק אחד כזה).
     */
    public Rider getTopRider() {
        Rider topRider  = null;
        int   maxOrders = -1;

        for (int i = 0; i < riders.size(); i++) {
            int count = riders.get(i).getOrdersCount();
            if (count > maxOrders) {
                maxOrders = count;
                topRider  = riders.get(i);
            }
        }
        return topRider;
    }

    /**
     * ו. מחזירה מסעדות פתוחות לפי סוג מטבח.
     * לשים לב: cuisineType הוגדר בעבודה 1 כ-String.
     */
    public ArrayList<Restaurant> getOpenRestaurantsByCuisine(String cuisineType) {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        if (cuisineType == null || cuisineType.isEmpty()) return result;

        // הסרת רווחים ידנית (trim לא ברשימת הסיכום)
        String cleaned = myTrim(cuisineType);
        if (cleaned.isEmpty()) return result;

        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            if (r.isOpen() && r.getCuisineType().equalsIgnoreCase(cleaned)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * מימוש ידני של trim() - מסיר רווחים מהתחלה ומהסוף.
     * משתמש ב-charAt() ו-substring() שמופיעים בסיכום הקורס.
     */
    private String myTrim(String s) {
        if (s == null || s.isEmpty()) return s;
        int start = 0;
        int end   = s.length() - 1;
        while (start <= end && s.charAt(start) == ' ') start++;
        while (end >= start && s.charAt(end)   == ' ') end--;
        if (start > end) return "";
        return s.substring(start, end + 1);
    }

    // ==================== הוספה ====================

    public boolean addCustomer(Customer c) {
        if (c == null) return false;
        if (findCustomerById(c.getCustomerId()) != null) {
            System.out.println("שגיאה: לקוח " + c.getCustomerId() + " כבר קיים");
            return false;
        }
        customers.add(c);
        return true;
    }

    public boolean addRestaurant(Restaurant r) {
        if (r == null) return false;
        if (findRestaurantById(r.getRestaurantId()) != null) {
            System.out.println("שגיאה: מסעדה " + r.getRestaurantId() + " כבר קיימת");
            return false;
        }
        restaurants.add(r);
        return true;
    }

    public boolean addRider(Rider r) {
        if (r == null) return false;
        if (findRiderById(r.getIdNumber()) != null) {
            System.out.println("שגיאה: שליח " + r.getIdNumber() + " כבר קיים");
            return false;
        }
        riders.add(r);
        return true;
    }

    /**
     * הוספת הזמנה - מוסיפה לאוסף הכללי ומעדכנת את כל המפות.
     */
    public boolean addOrder(Order o) {
        if (o == null) return false;
        if (findOrderById(o.getOrderId()) != null) {
            System.out.println("שגיאה: הזמנה " + o.getOrderId() + " כבר קיימת");
            return false;
        }
        orders.add(o);
        // עדכון כל המפות
        addOrderToCustomerMap(o.getCustomerId(), o);
        if (o.getRestaurant() != null) {
            addRestaurantToCustomerMap(o.getCustomerId(), o.getRestaurant());
        }
        updateTotalPayments(o.getCustomerId(), o.getFinalPrice());
        return true;
    }

    public boolean addRestAdmin(RestAdmin ra) {
        if (ra == null) return false;
        if (findRestAdminByUsername(ra.getUsername()) != null) {
            System.out.println("שגיאה: מנהל '" + ra.getUsername() + "' כבר קיים");
            return false;
        }
        restAdmins.add(ra);
        return true;
    }

    // ==================== חיפוש ====================

    public Customer findCustomerById(int id) {
        for (int i = 0; i < customers.size(); i++)
            if (customers.get(i).getCustomerId() == id) return customers.get(i);
        return null;
    }

    public Restaurant findRestaurantById(int id) {
        for (int i = 0; i < restaurants.size(); i++)
            if (restaurants.get(i).getRestaurantId() == id) return restaurants.get(i);
        return null;
    }

    public Rider findRiderById(String id) {
        if (id == null) return null;
        for (int i = 0; i < riders.size(); i++)
            if (riders.get(i).getIdNumber().equals(id)) return riders.get(i);
        return null;
    }

    public Order findOrderById(int id) {
        for (int i = 0; i < orders.size(); i++)
            if (orders.get(i).getOrderId() == id) return orders.get(i);
        return null;
    }

    public RestAdmin findRestAdminByUsername(String u) {
        if (u == null) return null;
        for (int i = 0; i < restAdmins.size(); i++)
            if (restAdmins.get(i).getUsername().equals(u)) return restAdmins.get(i);
        return null;
    }

    // ==================== לוגיקה עסקית ====================

    /**
     * שיוך שליח להזמנה.
     * בדיקות: שליח קיים, שליח זמין, הזמנה קיימת, הזמנה ללא שליח.
     * לאחר שיוך: עדכון שליח בהזמנה + שליח לא זמין + סטטוס "נשלח".
     */
    public boolean assignRiderToOrder(String riderId, int orderId) {
        Rider rider = findRiderById(riderId);
        if (rider == null) {
            System.out.println("שגיאה: שליח " + riderId + " לא נמצא");
            return false;
        }
        if (!rider.isAvailable()) {
            System.out.println("שגיאה: שליח " + rider.getFullName() + " אינו זמין");
            return false;
        }
        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("שגיאה: הזמנה " + orderId + " לא נמצאה");
            return false;
        }
        // בדיקה שההזמנה עדיין ללא שליח
        if (!order.getRiderId().equals(Order.NO_RIDER)) {
            System.out.println("שגיאה: להזמנה " + orderId + " כבר יש שליח משויך");
            return false;
        }
        // שיוך
        order.setRiderId(riderId);
        rider.addOrder(order);
        rider.setAvailable(false);
        order.setStatus(Order.SENT);
        System.out.println("שליח " + rider.getFullName() + " שויך להזמנה #" + orderId);
        return true;
    }

    public boolean assignAdminToRestaurant(String username, int restaurantId) {
        RestAdmin admin = findRestAdminByUsername(username);
        if (admin == null) {
            System.out.println("שגיאה: מנהל '" + username + "' לא נמצא");
            return false;
        }
        Restaurant r = findRestaurantById(restaurantId);
        if (r == null) {
            System.out.println("שגיאה: מסעדה " + restaurantId + " לא נמצאה");
            return false;
        }
        return admin.addRestaurant(r);
    }

    /** מחיקת כסף מיתרת לקוח */
    public boolean deductFromBalance(Customer customer, double amount) {
        if (customer.getCreditBalance() < amount) {
            System.out.println("שגיאה: יתרה לא מספיקה. יתרה נוכחית: " +
                               customer.getCreditBalance());
            return false;
        }
        customer.setCreditBalance(customer.getCreditBalance() - amount);
        return true;
    }

    public int generateOrderId() { return nextOrderId++; }

    // ==================== בדיקות התחברות ====================

    public boolean validateSystemAdmin(String u, String p) {
        return systemAdministrator != null &&
               systemAdministrator.validateLogin(u, p);
    }

    public RestAdmin validateRestAdminLogin(String u, String p) {
        for (int i = 0; i < restAdmins.size(); i++)
            if (restAdmins.get(i).validateLogin(u, p)) return restAdmins.get(i);
        return null;
    }

    // ==================== הצגה ====================

    public void displayAllRestaurants() {
        System.out.println("\n=== מסעדות (" + restaurants.size() + ") ===");
        for (int i = 0; i < restaurants.size(); i++)
            System.out.println((i + 1) + ". " + restaurants.get(i));
    }

    public void displayOpenRestaurants() {
        System.out.println("\n=== מסעדות פתוחות ===");
        boolean found = false;
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).isOpen()) {
                System.out.println(restaurants.get(i));
                found = true;
            }
        }
        if (!found) System.out.println("אין מסעדות פתוחות כרגע");
    }

    public void displayAllCustomers() {
        System.out.println("\n=== לקוחות (" + customers.size() + ") ===");
        for (int i = 0; i < customers.size(); i++)
            System.out.println((i + 1) + ". " + customers.get(i));
    }

    public void displayAllRiders() {
        System.out.println("\n=== שליחים (" + riders.size() + ") ===");
        for (int i = 0; i < riders.size(); i++)
            System.out.println((i + 1) + ". " + riders.get(i));
    }

    public void displayAllOrders() {
        System.out.println("\n=== הזמנות (" + orders.size() + ") ===");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i));
            System.out.println("----------------------------");
        }
    }

    public void displayAllRestAdmins() {
        System.out.println("\n=== מנהלי מסעדות (" + restAdmins.size() + ") ===");
        for (int i = 0; i < restAdmins.size(); i++)
            System.out.println((i + 1) + ". " + restAdmins.get(i));
    }

    /**
     * הצגת כל ההזמנות של מסעדה מסוימת
     */
    public void displayOrdersByRestaurant(int restaurantId) {
        System.out.println("\n=== הזמנות של מסעדה " + restaurantId + " ===");
        boolean found = false;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getRestaurantId() == restaurantId) {
                System.out.println(orders.get(i));
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) System.out.println("לא נמצאו הזמנות למסעדה זו");
    }

    // ==================== Getters ====================
    public Admin                         getSystemAdministrator()   { return systemAdministrator; }
    public ArrayList<Customer>           getCustomers()             { return customers; }
    public ArrayList<Restaurant>         getRestaurants()           { return restaurants; }
    public ArrayList<Rider>              getRiders()                { return riders; }
    public ArrayList<Order>              getOrders()                { return orders; }
    public ArrayList<RestAdmin>          getRestAdmins()            { return restAdmins; }
    public HashMap<Integer,ArrayList<Order>> getOrdersByCustomer()  { return ordersByCustomer; }
    public Hashtable<Integer,ArrayList<Restaurant>> getRestaurantsByCustomer() { return restaurantsByCustomer; }
    public HashMap<Integer,Double>       getTotalPayments()         { return totalPayments; }

    /**
     * מחזיר את רשימת ההזמנות של לקוח לפי קוד לקוח מה-HashMap
     */
    public ArrayList<Order> getOrdersByCustomer(int customerId) {
        return ordersByCustomer.get(customerId);
    }

    /**
     * מחזיר את רשימת המסעדות של לקוח לפי קוד לקוח מה-Hashtable
     */
    public ArrayList<Restaurant> getRestaurantsByCustomer(int customerId) {
        return restaurantsByCustomer.get(customerId);
    }
}
