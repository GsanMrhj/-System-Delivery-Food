import java.util.ArrayList;

/**
 * מחלקת שליח.
 * עבודה 2: מערך הזמנות הוחלף ב-ArrayList<Order>.
 *
 * חומר נלמד: מחלקות, ArrayList, Encapsulation, Getters/Setters.
 */
public class Rider {

    //  שדות 
    private String  idNumber;    // תעודת זהות (9 ספרות)
    private String  firstName;   // שם פרטי
    private String  lastName;    // שם משפחה
    private String  phone;       // טלפון
    private String  vehicle;     // כלי רכב
    private boolean isAvailable; // האם זמין

    // עבודה 2: ArrayList במקום מערך
    private ArrayList<Order> orders; // רשימת הזמנות שביצע

    //  בנאי 
    public Rider(String idNumber, String firstName, String lastName,
                 String phone, String vehicle) {
        this.idNumber    = idNumber;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.phone       = phone;
        this.vehicle     = vehicle;
        this.isAvailable = true;
        this.orders      = new ArrayList<Order>(); // אתחול רשימה ריקה
    }

    //  מתודות עסקיות

    /**
     * הוספת הזמנה לרשימה - בדיקת כפילות לפי orderId
     */
    public boolean addOrder(Order order) {
        if (order == null) return false;
        // בדיקת כפילות
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId() == order.getOrderId()) {
                System.out.println("שגיאה: הזמנה " + order.getOrderId() + " כבר קיימת אצל שליח זה");
                return false;
            }
        }
        orders.add(order);
        return true;
    }

    /**
     * מחזיר את ההזמנה הפעילה של השליח (סטטוס נשלח או בדרך)
     * עבודה 2: הצגת ההזמנה הפעילה של השליח
     */
    public Order getActiveOrder() {
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getStatus() == Order.SENT || o.getStatus() == Order.ON_THE_WAY) {
                return o;
            }
        }
        return null;
    }

    public String getFullName() { return firstName + " " + lastName; }

    // Getters
    public String           getIdNumber()    { return idNumber; }
    public String           getFirstName()   { return firstName; }
    public String           getLastName()    { return lastName; }
    public String           getPhone()       { return phone; }
    public String           getVehicle()     { return vehicle; }
    public boolean          isAvailable()    { return isAvailable; }
    public ArrayList<Order> getOrders()      { return orders; }
    public int              getOrdersCount() { return orders.size(); }

    //  Setters 
    public void setPhone(String phone)           { this.phone = phone; }
    public void setAvailable(boolean avail)      { this.isAvailable = avail; }
    public void setVehicle(String vehicle)       { this.vehicle = vehicle; }

    //  toString 
    public String toString() {
        return "שליח ת.ז:" + idNumber +
               " | " + getFullName() +
               " | טל:" + phone +
               " | רכב:" + vehicle +
               " | זמין:" + (isAvailable ? "כן" : "לא") +
               " | הזמנות:" + orders.size();
    }
}
