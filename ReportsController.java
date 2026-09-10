/**
 * ReportsController - עבודה 4 - חלק ח
 * דוחות ומיונים - שילוב כל המנגנונים מעבודה 3
 * שימוש ב: Comparable, Comparator, Lambda, Method References, Stream
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ReportsController {

    private ArrayList<Customer> customers;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Rider> riders;
    private ArrayList<Order> orders;
    private TextArea resultsArea;
    private Label statusLabel;

    public ReportsController(ArrayList<Customer> customers, ArrayList<Restaurant> restaurants, 
                           ArrayList<Rider> riders, ArrayList<Order> orders) {
        this.customers = customers;
        this.restaurants = restaurants;
        this.riders = riders;
        this.orders = orders;
    }

    /**
     * הצגת מסך דוחות ומיונים
     */
    public BorderPane createReportsScreen() {
        BorderPane root = new BorderPane();

        VBox topSection = createTopSection();
        root.setTop(topSection);

        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        return root;
    }

    /**
     * סעיף עליון - כפתורים למיונים (9 כפתורים!)
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("דוחות ומיונים");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Row 1 - Customer sorting
        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER_LEFT);

        Button customersCreditBtn = new Button("מיון לקוחות לפי יתרה");
        customersCreditBtn.setStyle("-fx-text-fill: darkgreen;");
        customersCreditBtn.setOnAction(e -> sortCustomersByCredit());

        Button customersNameBtn = new Button("מיון לקוחות לפי שם");
        customersNameBtn.setStyle("-fx-text-fill: darkgreen;");
        customersNameBtn.setOnAction(e -> sortCustomersByName());

        // Row 2 - Restaurant and order sorting
        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER_LEFT);

        Button restaurantsRatingBtn = new Button("מיון מסעדות לפי דירוג");
        restaurantsRatingBtn.setStyle("-fx-text-fill: darkblue;");
        restaurantsRatingBtn.setOnAction(e -> sortRestaurantsByRating());

        Button ordersChartBtn = new Button("מיון הזמנות לפי מחיר");
        ordersChartBtn.setStyle("-fx-text-fill: darkblue;");
        ordersChartBtn.setOnAction(e -> sortOrdersByPrice());

        // Row 3 - Rider and special reports
        HBox row3 = new HBox(10);
        row3.setAlignment(Pos.CENTER_LEFT);

        Button ridersDeliveriesBtn = new Button("מיון שליחים לפי משלוחים");
        ridersDeliveriesBtn.setStyle("-fx-text-fill: purple;");
        ridersDeliveriesBtn.setOnAction(e -> sortRidersByDeliveries());

        Button openRestaurantsBtn = new Button("מסעדות פתוחות");
        openRestaurantsBtn.setOnAction(e -> showOpenRestaurants());

        Button premiumRestaurantsBtn = new Button("מסעדות יוקרה");
        premiumRestaurantsBtn.setOnAction(e -> showPremiumRestaurants());

        Button availableRidersBtn = new Button("שליחים זמינים");
        availableRidersBtn.setOnAction(e -> showAvailableRiders());

        Button totalRevenueBtn = new Button("סך כל התשלומים");
        totalRevenueBtn.setStyle("-fx-text-fill: darkred;");
        totalRevenueBtn.setOnAction(e -> calculateTotalRevenue());

        row1.getChildren().addAll(customersCreditBtn, customersNameBtn);
        row2.getChildren().addAll(restaurantsRatingBtn, ordersChartBtn);
        row3.getChildren().addAll(ridersDeliveriesBtn, openRestaurantsBtn, premiumRestaurantsBtn, 
                                 availableRidersBtn, totalRevenueBtn);

        topBox.getChildren().addAll(titleLabel, row1, row2, row3);

        return topBox;
    }

    /**
     * סעיף מרכזי - TextArea להצגת תוצאות
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        Label resultsLabel = new Label("תוצאות:");
        resultsLabel.setStyle("-fx-font-weight: bold;");

        resultsArea = new TextArea();
        resultsArea.setWrapText(true);
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(350);

        centerBox.getChildren().addAll(resultsLabel, resultsArea);
        return centerBox;
    }

    /**
     * סעיף תחתון
     */
    private HBox createBottomSection() {
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");

        statusLabel = new Label("מוכן");
        bottomBox.getChildren().add(statusLabel);

        return bottomBox;
    }

    // ==================== SORTING METHODS (using HW3 mechanisms) ====================

    /**
     * 1. מיון לקוחות לפי יתרת זיכוי (Comparable - עבודה 3)
     */
    private void sortCustomersByCredit() {
        ArrayList<Customer> sorted = new ArrayList<>(customers);
        // Customer implements Comparable<Customer> - sort by creditBalance (HIGH to LOW)
        Collections.sort(sorted);
        Collections.reverse(sorted); // High to Low

        StringBuilder sb = new StringBuilder("מיון לקוחות לפי יתרה (גבוה לנמוך):\n\n");
        for (Customer c : sorted) {
            sb.append(String.format("%d. %s %s - יתרה: %.2f₪\n", 
                    c.getCustomerId(), c.getFirstName(), c.getLastName(), c.getCreditBalance()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מיון לקוחות לפי יתרה - " + sorted.size() + " לקוחות");
    }

    /**
     * 2. מיון לקוחות לפי שם פרטי (Lambda Expression - עבודה 3)
     */
    private void sortCustomersByName() {
        // Using Lambda Expression
        ArrayList<Customer> sorted = customers.stream()
                .sorted((c1, c2) -> c1.getFirstName().compareTo(c2.getFirstName()))
                .collect(Collectors.toCollection(ArrayList::new));

        StringBuilder sb = new StringBuilder("מיון לקוחות לפי שם פרטי (א-י):\n\n");
        for (Customer c : sorted) {
            sb.append(String.format("%d. %s %s\n", c.getCustomerId(), c.getFirstName(), c.getLastName()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מיון לקוחות לפי שם - " + sorted.size() + " לקוחות");
    }

    /**
     * 3. מיון מסעדות לפי דירוג (Comparator - עבודה 3)
     */
    private void sortRestaurantsByRating() {
        // Using Comparator
        ArrayList<Restaurant> sorted = new ArrayList<>(restaurants);
        sorted.sort(new RestaurantComparator()); // HIGH to LOW rating

        StringBuilder sb = new StringBuilder("מיון מסעדות לפי דירוג (גבוה לנמוך):\n\n");
        for (Restaurant r : sorted) {
            sb.append(String.format("%d. %s - דירוג: %.1f ⭐\n", 
                    r.getRestaurantId(), r.getRestaurantName(), r.getRating()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מיון מסעדות לפי דירוג - " + sorted.size() + " מסעדות");
    }

    /**
     * 4. מיון הזמנות לפי מחיר סופי (Comparator - עבודה 3)
     */
    private void sortOrdersByPrice() {
        // Using Comparator
        ArrayList<Order> sorted = new ArrayList<>(orders);
        sorted.sort(new OrderComparator()); // HIGH to LOW price

        StringBuilder sb = new StringBuilder("מיון הזמנות לפי מחיר סופי (גבוה לנמוך):\n\n");
        for (Order o : sorted) {
            sb.append(String.format("הזמנה #%d - מחיר: %.2f₪\n", 
                    o.getOrderId(), o.getFinalPrice()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מיון הזמנות לפי מחיר - " + sorted.size() + " הזמנות");
    }

    /**
     * 5. מיון שליחים לפי מספר משלוחים (Lambda + Stream - עבודה 3)
     */
    private void sortRidersByDeliveries() {
        // Count deliveries per rider using Lambda
        ArrayList<String> riderIds = new ArrayList<>();
        for (Rider r : riders) {
            riderIds.add(r.getIdNumber());
        }

        // Sort by delivery count using Lambda
        riderIds.sort((id1, id2) -> {
            long count1 = orders.stream().filter(o -> o.getRiderId().equals(id1)).count();
            long count2 = orders.stream().filter(o -> o.getRiderId().equals(id2)).count();
            return Long.compare(count2, count1); // HIGH to LOW
        });

        StringBuilder sb = new StringBuilder("מיון שליחים לפי מספר משלוחים (גבוה לנמוך):\n\n");
        for (String riderId : riderIds) {
            Rider r = findRiderById(riderId);
            long count = orders.stream().filter(o -> o.getRiderId().equals(riderId)).count();
            if (r != null) {
                sb.append(String.format("%s %s - משלוחים: %d\n", r.getFirstName(), r.getLastName(), count));
            }
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מיון שליחים לפי משלוחים");
    }

    /**
     * 6. הצגת כל המסעדות הפתוחות (Stream - עבודה 3)
     */
    private void showOpenRestaurants() {
        // Using Stream
        ArrayList<Restaurant> open = restaurants.stream()
                .filter(Restaurant::isOpen)
                .collect(Collectors.toCollection(ArrayList::new));

        StringBuilder sb = new StringBuilder("מסעדות פתוחות:\n\n");
        for (Restaurant r : open) {
            sb.append(String.format("%d. %s - דירוג: %.1f ⭐\n", 
                    r.getRestaurantId(), r.getRestaurantName(), r.getRating()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מסעדות פתוחות - " + open.size() + " מסעדות");
    }

    /**
     * 7. הצגת מסעדות יוקרה (Stream + instanceof - עבודה 3)
     */
    private void showPremiumRestaurants() {
        // Using Stream with filter for PremiumRestaurant
        ArrayList<Restaurant> premium = restaurants.stream()
                .filter(r -> r instanceof PremiumRestaurant)
                .collect(Collectors.toCollection(ArrayList::new));

        StringBuilder sb = new StringBuilder("מסעדות יוקרה:\n\n");
        for (Restaurant r : premium) {
            sb.append(String.format("%d. %s - דירוג: %.1f ⭐ (יוקרה)\n", 
                    r.getRestaurantId(), r.getRestaurantName(), r.getRating()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("מסעדות יוקרה - " + premium.size() + " מסעדות");
    }

    /**
     * 8. הצגת שליחים זמינים (Stream + Predicate - עבודה 3)
     */
    private void showAvailableRiders() {
        // Using Stream with filter
        ArrayList<Rider> available = riders.stream()
                .filter(Rider::isAvailable)
                .collect(Collectors.toCollection(ArrayList::new));

        StringBuilder sb = new StringBuilder("שליחים זמינים:\n\n");
        for (Rider r : available) {
            sb.append(String.format("%s %s - טלפון: %s - רכב: %s\n", 
                    r.getFirstName(), r.getLastName(), r.getPhone(), r.getVehicle()));
        }

        resultsArea.setText(sb.toString());
        statusLabel.setText("שליחים זמינים - " + available.size() + " שליחים");
    }

    /**
     * 9. הצגת סך כל התשלומים במערכת (Stream + sum - עבודה 3)
     */
    private void calculateTotalRevenue() {
        // Using Stream to calculate total
        double totalRevenue = orders.stream()
                .mapToDouble(Order::getFinalPrice)
                .sum();

        double totalBase = orders.stream()
                .mapToDouble(Order::getBaseAmount)
                .sum();

        StringBuilder sb = new StringBuilder("סך כל התשלומים:\n\n");
        sb.append(String.format("סה\"כ סכומים בסיסיים: %.2f₪\n", totalBase));
        sb.append(String.format("סה\"כ סכומים סופיים: %.2f₪\n", totalRevenue));
        sb.append(String.format("סה\"כ עמלות ודמי משלוח: %.2f₪\n", totalRevenue - totalBase));
        sb.append(String.format("\nמספר הזמנות: %d\n", orders.size()));
        sb.append(String.format("ממוצע הזמנה: %.2f₪\n", orders.isEmpty() ? 0 : totalRevenue / orders.size()));

        resultsArea.setText(sb.toString());
        statusLabel.setText("סך כל התשלומים: " + String.format("%.2f₪", totalRevenue));
    }

    // ==================== HELPER METHODS ====================

    private Rider findRiderById(String id) {
        for (Rider r : riders) {
            if (r.getIdNumber().equals(id)) return r;
        }
        return null;
    }
}
