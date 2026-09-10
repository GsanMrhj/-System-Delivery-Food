import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * SortingAndReporting - עבודה 3 - חלקים ב, ג, ה, ז
 * 
 * חלק ב - Lambda Expressions (לפחות 3)
 * חלק ג - Method References (לפחות 3)
 * חלק ה - Stream API (בונוס)
 * חלק ז - Predicates (בונוס)
 */
public class SortingAndReporting {
    
    // ═══════════════════════════════════════════════════════════════════════
    // חלק ב - LAMBDA EXPRESSIONS (לפחות 3)
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Lambda Expression 1: מיון שליחים לפי מספר המשלוחים שביצעו
     * ממשלוח הרבה לפחות
     */
    public static ArrayList<Rider> sortRidersByDeliveryCount(ArrayList<Rider> riders) {
        ArrayList<Rider> sorted = new ArrayList<>(riders);
        
        // Lambda Expression: השוואה לפי מספר משלוחים
        sorted.sort((r1, r2) -> Integer.compare(
            r2.getOrders().size(),  // r2 ראשון - מיון יורד
            r1.getOrders().size()
        ));
        
        return sorted;
    }
    
    /**
     * Lambda Expression 2: מיון לקוחות לפי שם פרטי (א"ב)
     */
    public static ArrayList<Customer> sortCustomersByFirstName(ArrayList<Customer> customers) {
        ArrayList<Customer> sorted = new ArrayList<>(customers);
        
        // Lambda Expression: השוואה אלפביתית לפי שם פרטי
        sorted.sort((c1, c2) -> c1.getFirstName().compareTo(c2.getFirstName()));
        
        return sorted;
    }
    
    /**
     * Lambda Expression 3: מיון הזמנות לפי תאריך הזמנה (חדש לישן)
     */
    public static ArrayList<Order> sortOrdersByDate(ArrayList<Order> orders) {
        ArrayList<Order> sorted = new ArrayList<>(orders);
        
        // Lambda Expression: השוואה לפי תאריך (יום בחודש)
        sorted.sort((o1, o2) -> {
            int[] date1 = o1.getOrderDate();
            int[] date2 = o2.getOrderDate();
            
            // שנה -> חודש -> יום
            if (date1[2] != date2[2]) return Integer.compare(date2[2], date1[2]);
            if (date1[1] != date2[1]) return Integer.compare(date2[1], date1[1]);
            return Integer.compare(date2[0], date1[0]);
        });
        
        return sorted;
    }
    
    // ═══════════════════════════════════════════════════════════════════════
    // חלק ג - METHOD REFERENCES (לפחות 3)
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Method Reference 1: System.out::println
     * הדפסת כל הפריטים באוסף
     */
    public static <T> void printAllItems(ArrayList<T> items) {
        System.out.println("=== הדפסת פריטים ===");
        items.forEach(System.out::println);  // Method Reference: System.out::println
    }
    
    /**
     * Method Reference 2: Customer::compareTo (דרך Comparable)
     * השוואה בין לקוחות לפי יתרה
     */
    public static void displayCustomersComparison(Customer c1, Customer c2) {
        System.out.println("\n=== השוואת לקוחות ===");
        System.out.println("לקוח 1: " + c1.getFirstName() + " - יתרה: " + c1.getCreditBalance());
        System.out.println("לקוח 2: " + c2.getFirstName() + " - יתרה: " + c2.getCreditBalance());
        
        int result = c1.compareTo(c2);  // Method Reference: compareTo
        if (result > 0) {
            System.out.println("לקוח 1 בעל יתרה גבוהה יותר");
        } else if (result < 0) {
            System.out.println("לקוח 2 בעל יתרה גבוהה יותר");
        } else {
            System.out.println("היתרות שוות");
        }
    }
    
    /**
     * Method Reference 3: ArrayList::add (דרך Constructor Reference)
     * הוספת פריטים לאוסף
     */
    public static void addItemsUsingMethodReference(ArrayList<String> list, String[] items) {
        System.out.println("\n=== הוספת פריטים ===");
        for (String item : items) {
            list.add(item);  // Method Reference: array add
        }
    }
    
    // ═══════════════════════════════════════════════════════════════════════
    // חלק ה - STREAM API (בונוס 3 נקודות)
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Stream 1: הצגת כל המסעדות הפתוחות
     * שימוש: filter, forEach
     */
    public static void displayOpenRestaurants(ArrayList<Restaurant> restaurants) {
        System.out.println("\n=== מסעדות פתוחות (Stream API) ===");
        restaurants.stream()
                .filter(Restaurant::isOpen)  // filter - רק פתוחות
                .forEach(r -> System.out.println(r.getRestaurantName() + " - דירוג: " + r.getRating()));
    }
    
    /**
     * Stream 2: הצגת כל מסעדות היוקרה
     * שימוש: filter, instanceof, forEach
     */
    public static void displayPremiumRestaurants(ArrayList<Restaurant> restaurants) {
        System.out.println("\n=== מסעדות יוקרה (Stream API) ===");
        restaurants.stream()
                .filter(r -> r instanceof PremiumRestaurant)  // filter - רק יוקרה
                .forEach(r -> System.out.println(r.getRestaurantName() + " - עמלה: " + 
                        ((PremiumRestaurant) r).getCommissionPercent()));
    }
    
    /**
     * Stream 3: הצגת כל השליחים הזמינים
     * שימוש: filter, forEach
     */
    public static void displayAvailableDeliveryPersons(ArrayList<Rider> riders) {
        System.out.println("\n=== שליחים זמינים (Stream API) ===");
        riders.stream()
                .filter(Rider::isAvailable)  // filter - רק זמינים
                .forEach(r -> System.out.println(r.getIdNumber() + " - " + r.getFullName()));
    }
    
    /**
     * Stream 4: חישוב סך כל הכסף ששולם בכל ההזמנות
     * שימוש: mapToDouble, sum
     */
    public static double calculateTotalPayment(ArrayList<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::getFinalPrice)  // map - חילוץ מחיר סופי
                .sum();  // sum - סכום כולל
    }
    
    /**
     * Stream 5: חישוב ממוצע יתרה של לקוחות
     * שימוש: mapToDouble, average
     */
    public static double calculateAverageBalance(ArrayList<Customer> customers) {
        return customers.stream()
                .mapToDouble(Customer::getCreditBalance)  // map - חילוץ יתרה
                .average()  // average - ממוצע
                .orElse(0.0);
    }
    
    /**
     * Stream 6: קבלת רשימה של דירוגי מסעדות
     * שימוש: map, collect
     */
    public static ArrayList<Double> getRatingsAsStream(ArrayList<Restaurant> restaurants) {
        return restaurants.stream()
                .map(Restaurant::getRating)  // map - חילוץ דירוג
                .collect(Collectors.toCollection(ArrayList::new));  // collect - אוסף חדש
    }
    
    // ═══════════════════════════════════════════════════════════════════════
    // חלק ז - PREDICATES (בונוס 2 נקודות)
    // ═══════════════════════════════════════════════════════════════════════
    
    /**
     * Predicate 1: בדיקה אם יתרת לקוח גדולה מ 100
     */
    public static Predicate<Customer> isRichCustomer() {
        return customer -> customer.getCreditBalance() > 100;
    }
    
    /**
     * Predicate 2: בדיקה אם מסעדה בעלת דירוג גבוה
     */
    public static Predicate<Restaurant> isHighRatedRestaurant() {
        return restaurant -> restaurant.getRating() >= 4.0;
    }
    
    /**
     * Predicate 3: בדיקה אם הזמנה יקרה מאוד
     */
    public static Predicate<Order> isExpensiveOrder() {
        return order -> order.getFinalPrice() > 200;
    }
    
    /**
     * שימוש בPredicates
     */
    public static void filterAndDisplayUsingPredicates(
            ArrayList<Customer> customers,
            ArrayList<Restaurant> restaurants) {
        
        System.out.println("\n=== Predicates - סינון ==="  );
        
        // Predicate 1: לקוחות עשירים
        System.out.println("\nלקוחות עם יתרה > 100:");
        Predicate<Customer> richCustomer = isRichCustomer();
        customers.stream()
                .filter(richCustomer)
                .forEach(c -> System.out.println("  " + c.getFirstName() + " - " + c.getCreditBalance()));
        
        // Predicate 2: מסעדות בעלות דירוג גבוה
        System.out.println("\nמסעדות עם דירוג >= 4.0:");
        Predicate<Restaurant> highRated = isHighRatedRestaurant();
        restaurants.stream()
                .filter(highRated)
                .forEach(r -> System.out.println("  " + r.getRestaurantName() + " - " + r.getRating()));
    }
}
