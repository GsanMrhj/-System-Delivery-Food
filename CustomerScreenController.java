/**
 * CustomerScreenController - עבודה 4 - חלק יה
 * מסך לקוח אישי - פעולות אישיות של לקוח
 * 10 פעולות: צפייה בפרטים, הזמנה, צפייה בהזמנות, עדכון פרטים, טעינה/משיכת כסף וכו'
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
public class CustomerScreenController {

    private Customer customer;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Order> orders;
    private TextArea infoArea;
    private Label statusLabel;

    public CustomerScreenController(Customer customer, ArrayList<Restaurant> restaurants, ArrayList<Order> orders) {
        this.customer = customer;
        this.restaurants = restaurants;
        this.orders = orders;
    }

    /**
     * הצגת מסך לקוח אישי
     */
    public BorderPane createCustomerScreen() {
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
     * סעיף עליון - כפתורים
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("ברוכים הבאים, " + customer.getFirstName() + "!");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button infoBtn = new Button("צפייה בפרטים אישיים");
        infoBtn.setOnAction(e -> showPersonalInfo());

        Button ordersBtn = new Button("ההזמנות שלי");
        ordersBtn.setOnAction(e -> showMyOrders());

        Button updateBtn = new Button("עדכון פרטים");
        updateBtn.setOnAction(e -> updatePersonalInfo());

        Button restaurantsBtn = new Button("מסעדות שהזמנתי");
        restaurantsBtn.setOnAction(e -> showMyRestaurants());

        Button premiumBtn = new Button("מסעדות יוקרה");
        premiumBtn.setOnAction(e -> showPremiumRestaurants());

        Button creditBtn = new Button("יתרת זיכוי");
        creditBtn.setStyle("-fx-text-fill: green;");
        creditBtn.setOnAction(e -> showCreditBalance());

        Button depositBtn = new Button("טעינת כסף");
        depositBtn.setStyle("-fx-text-fill: green;");
        depositBtn.setOnAction(e -> depositCredit());

        Button withdrawBtn = new Button("משיכת כסף");
        withdrawBtn.setStyle("-fx-text-fill: red;");
        withdrawBtn.setOnAction(e -> withdrawCredit());

        Button searchRestBtn = new Button("חיפוש מסעדה");
        searchRestBtn.setOnAction(e -> searchRestaurant());

        buttonBox.getChildren().addAll(infoBtn, ordersBtn, updateBtn, restaurantsBtn, premiumBtn,
                creditBtn, depositBtn, withdrawBtn, searchRestBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TextArea להצגת מידע
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        infoArea = new TextArea();
        infoArea.setWrapText(true);
        infoArea.setEditable(false);
        infoArea.setPrefHeight(350);

        centerBox.getChildren().add(infoArea);
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

    // ==================== CUSTOMER ACTIONS ====================

    /**
     * 1. צפייה בפרטים אישיים
     */
    private void showPersonalInfo() {
        StringBuilder sb = new StringBuilder("פרטים אישיים:\n\n");
        sb.append(String.format("קוד לקוח: %d\n", customer.getCustomerId()));
        sb.append(String.format("שם: %s %s\n", customer.getFirstName(), customer.getLastName()));
        sb.append(String.format("כתובת: %s, %s\n", customer.getStreet(), customer.getCity()));
        sb.append(String.format("מיקוד: %s\n", customer.getZipCode()));
        sb.append(String.format("טלפון: %s\n", customer.getPhone()));
        sb.append(String.format("אימייל: %s\n", customer.getEmail()));
        sb.append(String.format("יתרת זיכוי: %.2f₪\n", customer.getCreditBalance()));

        infoArea.setText(sb.toString());
        statusLabel.setText("פרטים אישיים");
    }

    /**
     * 2. ביצוע הזמנה חדשה - placeholder
     */
    private void placeOrder() {
        showAlert("מידע", "ביצוע הזמנה - לבנייה בשלב הבא");
    }

    /**
     * 3. צפייה בכל ההזמנות שלו
     */
    private void showMyOrders() {
        ArrayList<Order> myOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getCustomerId() == customer.getCustomerId()) {
                myOrders.add(o);
            }
        }

        StringBuilder sb = new StringBuilder("ההזמנות שלי:\n\n");
        if (myOrders.isEmpty()) {
            sb.append("אין הזמנות עדיין");
        } else {
            for (Order o : myOrders) {
                sb.append(String.format("הזמנה #%d - מחיר: %.2f₪ - מסעדה: %d\n", 
                        o.getOrderId(), o.getFinalPrice(), o.getRestaurantId()));
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("ההזמנות שלי - " + myOrders.size() + " הזמנות");
    }

    /**
     * 4. עדכון פרטים אישיים
     */
    private void updatePersonalInfo() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("עדכון פרטים אישיים");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField phoneField = new TextField(customer.getPhone());
        TextField emailField = new TextField(customer.getEmail());
        TextField streetField = new TextField(customer.getStreet());
        TextField cityField = new TextField(customer.getCity());

        content.getChildren().addAll(
                new Label("טלפון:"), phoneField,
                new Label("אימייל:"), emailField,
                new Label("רחוב:"), streetField,
                new Label("עיר:"), cityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String street = streetField.getText().trim();
            String city = cityField.getText().trim();

            if (phone.isEmpty() || email.isEmpty() || city.isEmpty()) {
                showAlert("שגיאה", "אנא מלא את כל השדות!");
                return;
            }

            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setStreet(street);
            customer.setCity(city);

            showAlert("הצלחה", "הפרטים עודכנו בהצלחה!");
            statusLabel.setText("הפרטים עודכנו");
        });
    }

    /**
     * 5. הצגת כל המסעדות שהלקוח הזמין
     */
    private void showMyRestaurants() {
        ArrayList<Integer> restaurantIds = new ArrayList<>();
        for (Order o : orders) {
            if (o.getCustomerId() == customer.getCustomerId()) {
                if (!restaurantIds.contains(o.getRestaurantId())) {
                    restaurantIds.add(o.getRestaurantId());
                }
            }
        }

        StringBuilder sb = new StringBuilder("המסעדות שהזמנתי:\n\n");
        if (restaurantIds.isEmpty()) {
            sb.append("עדיין לא הזמנתי ממסעדות");
        } else {
            for (int restaurantId : restaurantIds) {
                Restaurant r = findRestaurantById(restaurantId);
                if (r != null) {
                    sb.append(String.format("- %s (דירוג: %.1f⭐)\n", r.getRestaurantName(), r.getRating()));
                }
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("המסעדות שהזמנתי");
    }

    /**
     * 6. הצגת מסעדות יוקרה שהלקוח הזמין
     */
    private void showPremiumRestaurants() {
        ArrayList<Integer> premiumIds = new ArrayList<>();
        for (Order o : orders) {
            if (o.getCustomerId() == customer.getCustomerId()) {
                Restaurant r = findRestaurantById(o.getRestaurantId());
                if (r instanceof PremiumRestaurant && !premiumIds.contains(o.getRestaurantId())) {
                    premiumIds.add(o.getRestaurantId());
                }
            }
        }

        StringBuilder sb = new StringBuilder("מסעדות יוקרה שהזמנתי:\n\n");
        if (premiumIds.isEmpty()) {
            sb.append("עדיין לא הזמנתי ממסעדות יוקרה");
        } else {
            for (int restaurantId : premiumIds) {
                Restaurant r = findRestaurantById(restaurantId);
                if (r != null) {
                    sb.append(String.format("- %s (יוקרה) ⭐⭐⭐\n", r.getRestaurantName()));
                }
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("מסעדות יוקרה שהזמנתי");
    }

    /**
     * 7. הצגת יתרת זיכוי הנוכחית
     */
    private void showCreditBalance() {
        StringBuilder sb = new StringBuilder("יתרת הזיכוי שלי:\n\n");
        sb.append(String.format("יתרה נוכחית: %.2f₪\n\n", customer.getCreditBalance()));

        if (customer.getCreditBalance() > 1000) {
            sb.append("📈 יתרה גבוהה!");
        } else if (customer.getCreditBalance() > 500) {
            sb.append("✓ יתרה סבירה");
        } else if (customer.getCreditBalance() > 100) {
            sb.append("⚠️ יתרה נמוכה - תקבל לטעון כסף");
        } else {
            sb.append("❌ אין יתרה - אנא טען כסף");
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("יתרה: " + String.format("%.2f₪", customer.getCreditBalance()));
    }

    /**
     * 8. טעינת כסף ליתרה
     */
    private void depositCredit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("טעינת כסף");
        dialog.setHeaderText("הזן סכום לטעינה:");
        dialog.setContentText("סכום (₪):");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    showAlert("שגיאה", "הסכום חייב להיות חיובי!");
                    return;
                }

                customer.setCreditBalance(customer.getCreditBalance() + amount);
                showAlert("הצלחה", String.format("טענת בהצלחה %.2f₪\nיתרה חדשה: %.2f₪", 
                        amount, customer.getCreditBalance()));
                statusLabel.setText("טעינה בהצלחה - יתרה חדשה: " + String.format("%.2f₪", customer.getCreditBalance()));
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "הסכום חייב להיות מספר!");
            }
        });
    }

    /**
     * 9. משיכת כסף מהיתרה (עם InsufficientBalanceException)
     */
    private void withdrawCredit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("משיכת כסף");
        dialog.setHeaderText("הזן סכום למשיכה:");
        dialog.setContentText("סכום (₪):");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    showAlert("שגיאה", "הסכום חייב להיות חיובי!");
                    return;
                }

                // בדוק אם יש מספיק יתרה - זרוק InsufficientBalanceException אם לא
                if (amount > customer.getCreditBalance()) {
                    double shortfall = amount - customer.getCreditBalance();
                    try {
                        throw new InsufficientBalanceException("יתרה חסרה: " + String.format("%.2f₪", shortfall));
                    } catch (InsufficientBalanceException ex) {
                        System.out.println("❌ Exception: " + ex.getMessage());
                        showAlert("שגיאה - יתרה חסרה", 
                                "אין מספיק יתרה!\n" +
                                "יתרה נוכחית: " + String.format("%.2f₪\n", customer.getCreditBalance()) +
                                "סכום לביצוע: " + String.format("%.2f₪\n", amount) +
                                "יתרה חסרה: " + String.format("%.2f₪", shortfall));
                        return;
                    }
                }

                customer.setCreditBalance(customer.getCreditBalance() - amount);
                showAlert("הצלחה", String.format("משכת בהצלחה %.2f₪\nיתרה חדשה: %.2f₪", 
                        amount, customer.getCreditBalance()));
                statusLabel.setText("משיכה בהצלחה");
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "הסכום חייב להיות מספר!");
                ex.printStackTrace();
            }
        });
    }

    /**
     * 10. חיפוש מסעדה וצפייה בפרטיה
     */
    private void searchRestaurant() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש מסעדה");
        dialog.setHeaderText("הזן קוד מסעדה:");
        dialog.setContentText("קוד:");

        dialog.showAndWait().ifPresent(restaurantIdStr -> {
            try {
                int id = Integer.parseInt(restaurantIdStr);
                Restaurant r = findRestaurantById(id);
                if (r != null) {
                    StringBuilder sb = new StringBuilder("פרטי המסעדה:\n\n");
                    sb.append(String.format("קוד: %d\n", r.getRestaurantId()));
                    sb.append(String.format("שם: %s\n", r.getRestaurantName()));
                    sb.append(String.format("סוג מטבח: %s\n", r.getCuisineType()));
                    sb.append(String.format("דירוג: %.1f⭐\n", r.getRating()));
                    sb.append(String.format("עמלת משלוח: %.2f₪\n", r.getBaseDeliveryFee()));
                    sb.append(String.format("סטטוס: %s\n", r.isOpen() ? "פתוחה" : "סגורה"));

                    infoArea.setText(sb.toString());
                    statusLabel.setText("מסעדה: " + r.getRestaurantName());
                } else {
                    showAlert("שגיאה", "מסעדה לא נמצאה!");
                }
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד חייב להיות מספר!");
            }
        });
    }

    // ==================== HELPER METHODS ====================

    private Restaurant findRestaurantById(int id) {
        for (Restaurant r : restaurants) {
            if (r.getRestaurantId() == id) return r;
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
