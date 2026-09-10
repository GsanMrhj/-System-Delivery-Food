/**
 * FileIOManager - עבודה 4 - חלק י
 * ניהול שמירה וטעינה של נתונים מקבצי טקסט
 * שימוש ב: File, FileReader, BufferedReader, FileWriter, BufferedWriter
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileIOManager {

    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String RESTAURANTS_FILE = "restaurants.txt";
    private static final String RIDERS_FILE = "riders.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String REST_ADMINS_FILE = "restAdmins.txt";

    // ==================== SAVE METHODS ====================

    /**
     * שמירת כל הלקוחות לקובץ
     */
    public static void saveCustomers(ArrayList<Customer> customers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                String line = c.getCustomerId() + "|" +
                        c.getFirstName() + "|" +
                        c.getLastName() + "|" +
                        c.getStreet() + "|" +
                        c.getCity() + "|" +
                        c.getZipCode() + "|" +
                        c.getPhone() + "|" +
                        c.getEmail() + "|" +
                        c.getCreditBalance();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * שמירת כל המסעדות לקובץ
     * צריך להבחין בין סוגים שונים
     */
    public static void saveRestaurants(ArrayList<Restaurant> restaurants) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESTAURANTS_FILE))) {
            for (Restaurant r : restaurants) {
                if (r instanceof FastFoodRestaurant) {
                    FastFoodRestaurant ffr = (FastFoodRestaurant) r;
                    String line = "FASTFOOD|" +
                            r.getRestaurantId() + "|" +
                            r.getRestaurantName() + "|" +
                            r.getCuisineType() + "|" +
                            r.getRating() + "|" +
                            r.isOpen() + "|" +
                            r.getBaseDeliveryFee() + "|" +
                            ffr.getAvgPrepTimeMinutes() + "|" +
                            ffr.getExpressDeliverySurcharge();
                    writer.write(line);
                    writer.newLine();
                } else if (r instanceof PremiumRestaurant) {
                    PremiumRestaurant pr = (PremiumRestaurant) r;
                    String line = "PREMIUM|" +
                            r.getRestaurantId() + "|" +
                            r.getRestaurantName() + "|" +
                            r.getCuisineType() + "|" +
                            r.getRating() + "|" +
                            r.isOpen() + "|" +
                            r.getBaseDeliveryFee() + "|" +
                            pr.getMinOrderAmount() + "|" +
                            pr.getCommissionPercent();
                    writer.write(line);
                    writer.newLine();
                } else {
                    String line = "REGULAR|" +
                            r.getRestaurantId() + "|" +
                            r.getRestaurantName() + "|" +
                            r.getCuisineType() + "|" +
                            r.getRating() + "|" +
                            r.isOpen() + "|" +
                            r.getBaseDeliveryFee();
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    /**
     * שמירת כל השליחים לקובץ
     */
    public static void saveRiders(ArrayList<Rider> riders) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RIDERS_FILE))) {
            for (Rider r : riders) {
                String line = r.getIdNumber() + "|" +
                        r.getFirstName() + "|" +
                        r.getLastName() + "|" +
                        r.getPhone() + "|" +
                        r.getVehicle() + "|" +
                        r.isAvailable();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * שמירת כל ההזמנות לקובץ
     */
    public static void saveOrders(ArrayList<Order> orders, 
                                 HashMap<Integer, ArrayList<Order>> ordersByCustomer,
                                 HashMap<Integer, ArrayList<Order>> ordersByRestaurant,
                                 HashMap<String, ArrayList<Order>> ordersByRider) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order o : orders) {
                String line = o.getOrderId() + "|" +
                        o.getCustomerId() + "|" +
                        o.getRestaurantId() + "|" +
                        o.getRiderId() + "|" +
                        o.getOrderDay() + "|" +
                        o.getOrderMonth() + "|" +
                        o.getOrderYear() + "|" +
                        o.getBaseAmount() + "|" +
                        o.getFinalPrice() + "|" +
                        o.getStatus();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * שמירת כל מנהלי המסעדות לקובץ
     */
    public static void saveRestAdmins(ArrayList<RestAdmin> restAdmins) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REST_ADMINS_FILE))) {
            for (RestAdmin ra : restAdmins) {
                // כתיבה של פרטי המנהל
                String line = ra.getAdminName() + "|" +
                        ra.getUsername() + "|" +
                        ra.getPassword();
                writer.write(line);
                writer.newLine();
                
                // כתיבה של המסעדות שבאחריותו
                for (Restaurant r : ra.getRestaurants()) {
                    writer.write(String.valueOf(r.getRestaurantId()));
                    writer.newLine();
                }
                
                writer.write("END_ADMIN");
                writer.newLine();
            }
        }
    }

    // ==================== LOAD METHODS ====================

    /**
     * טעינת כל הלקוחות מקובץ
     */
    public static ArrayList<Customer> loadCustomers() throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        
        if (!file.exists()) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length == 9) {
                    Customer c = new Customer(
                            Integer.parseInt(parts[0]),  // customerId
                            parts[1],                     // firstName
                            parts[2],                     // lastName
                            parts[3],                     // street
                            parts[4],                     // city
                            parts[5],                     // zipCode
                            parts[6],                     // phone
                            parts[7],                     // email
                            Double.parseDouble(parts[8]) // creditBalance
                    );
                    customers.add(c);
                }
            }
        }
        return customers;
    }

    /**
     * טעינת כל המסעדות מקובץ
     */
    public static ArrayList<Restaurant> loadRestaurants() throws IOException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        File file = new File(RESTAURANTS_FILE);
        
        if (!file.exists()) {
            return restaurants;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                
                if (parts[0].equals("FASTFOOD") && parts.length == 9) {
                    FastFoodRestaurant r = new FastFoodRestaurant(
                            Integer.parseInt(parts[1]),   // restaurantId
                            parts[2],                      // restaurantName
                            parts[3],                      // cuisineType
                            Double.parseDouble(parts[4]),  // rating
                            Boolean.parseBoolean(parts[5]), // isOpen
                            Double.parseDouble(parts[6]),  // baseDeliveryFee
                            Integer.parseInt(parts[7]),    // avgPrepTimeMinutes
                            Double.parseDouble(parts[8])   // expressDeliverySurcharge
                    );
                    restaurants.add(r);
                } else if (parts[0].equals("PREMIUM") && parts.length == 9) {
                    PremiumRestaurant r = new PremiumRestaurant(
                            Integer.parseInt(parts[1]),   // restaurantId
                            parts[2],                      // restaurantName
                            parts[3],                      // cuisineType
                            Double.parseDouble(parts[4]),  // rating
                            Boolean.parseBoolean(parts[5]), // isOpen
                            Double.parseDouble(parts[6]),  // baseDeliveryFee
                            Double.parseDouble(parts[7]),  // minOrderAmount
                            Double.parseDouble(parts[8])   // commissionPercent
                    );
                    restaurants.add(r);
                } else if (parts[0].equals("REGULAR") && parts.length == 7) {
                    Restaurant r = new Restaurant(
                            Integer.parseInt(parts[1]),   // restaurantId
                            parts[2],                      // restaurantName
                            parts[3],                      // cuisineType
                            Double.parseDouble(parts[4]),  // rating
                            Boolean.parseBoolean(parts[5]), // isOpen
                            Double.parseDouble(parts[6])   // baseDeliveryFee
                    );
                    restaurants.add(r);
                }
            }
        }
        return restaurants;
    }

    /**
     * טעינת כל השליחים מקובץ
     */
    public static ArrayList<Rider> loadRiders() throws IOException {
        ArrayList<Rider> riders = new ArrayList<>();
        File file = new File(RIDERS_FILE);
        
        if (!file.exists()) {
            return riders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Rider r = new Rider(
                            parts[0],                       // idNumber
                            parts[1],                       // firstName
                            parts[2],                       // lastName
                            parts[3],                       // phone
                            parts[4]                        // vehicle
                    );
                    r.setAvailable(Boolean.parseBoolean(parts[5]));
                    riders.add(r);
                }
            }
        }
        return riders;
    }

    /**
     * טעינת כל ההזמנות מקובץ
     */
    public static ArrayList<Order> loadOrders(ArrayList<Restaurant> restaurants,
                                             ArrayList<Customer> customers) throws IOException {
        ArrayList<Order> orders = new ArrayList<>();
        File file = new File(ORDERS_FILE);
        
        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length == 10) {
                    int orderId = Integer.parseInt(parts[0]);
                    int customerId = Integer.parseInt(parts[1]);
                    int restaurantId = Integer.parseInt(parts[2]);
                    
                    // חיפוש המסעדה
                    Restaurant restaurant = null;
                    for (Restaurant r : restaurants) {
                        if (r.getRestaurantId() == restaurantId) {
                            restaurant = r;
                            break;
                        }
                    }
                    
                 // ابدأ النسخ من هنا
                    if (restaurant != null) {
                        Order o = new Order(
                                orderId,
                                customerId,
                                restaurant,
                                Integer.parseInt(parts[4]),  // orderDay
                                Integer.parseInt(parts[5]),  // orderMonth
                                Integer.parseInt(parts[6]),  // orderYear
                                Double.parseDouble(parts[7]) // هذا المعامل السابع - تأكد أنه موجود!
                        );
                        o.setRiderId(parts[3]);
                        o.setStatus(Integer.parseInt(parts[9]));
                        orders.add(o);
                    }
                    // انتهى النسخ هنا
                }
            }
        }
        return orders;
    }

    /**
     * טעינת כל מנהלי המסעדות מקובץ
     */
    public static ArrayList<RestAdmin> loadRestAdmins(ArrayList<Restaurant> restaurants) throws IOException {
        ArrayList<RestAdmin> restAdmins = new ArrayList<>();
        File file = new File(REST_ADMINS_FILE);
        
        if (!file.exists()) {
            return restAdmins;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    RestAdmin ra = new RestAdmin(parts[0], parts[1], parts[2]);
                    
                    // קריאת המסעדות שבאחריותו
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("END_ADMIN")) {
                            break;
                        }
                        int restaurantId = Integer.parseInt(line);
                        for (Restaurant r : restaurants) {
                            if (r.getRestaurantId() == restaurantId) {
                                ra.addRestaurant(r);
                                break;
                            }
                        }
                    }
                    restAdmins.add(ra);
                }
            }
        }
        return restAdmins;
    }
}
