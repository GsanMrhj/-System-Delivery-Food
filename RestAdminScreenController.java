/**
 * RestAdminScreenController - עבודה 4 - חלק יה
 * מסך מנהל מסעדה אישי - ניהול המסעדות שבאחריותו
 * 8 פעולות: הצגת מסעדות, הוספת לקוח, הוספת הזמנה, עדכון וכו'
 */

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RestAdminScreenController {

    private RestAdmin restAdmin;
    private ArrayList<Restaurant> allRestaurants;
    private ArrayList<Customer> customers;
    private ArrayList<Order> orders;
    private TextArea infoArea;
    private Label statusLabel;

    public RestAdminScreenController(RestAdmin restAdmin, ArrayList<Restaurant> allRestaurants, 
                                   ArrayList<Customer> customers, ArrayList<Order> orders) {
        this.restAdmin = restAdmin;
        this.allRestaurants = allRestaurants;
        this.customers = customers;
        this.orders = orders;
    }

    /**
     * הצגת מסך מנהל מסעדה אישי
     */
    public BorderPane createRestAdminScreen() {
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

        Label titleLabel = new Label("ברוכים הבאים, " + restAdmin.getAdminName() + "!");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button restaurantsBtn = new Button("המסעדות שלי");
        restaurantsBtn.setOnAction(e -> showMyRestaurants());

        Button addCustomerBtn = new Button("הוספת לקוח");
        addCustomerBtn.setStyle("-fx-text-fill: green;");
        addCustomerBtn.setOnAction(e -> addNewCustomer());

        Button addOrderBtn = new Button("הוספת הזמנה");
        addOrderBtn.setStyle("-fx-text-fill: green;");
        addOrderBtn.setOnAction(e -> addNewOrder());

        Button updateRatingBtn = new Button("עדכון דירוג");
        updateRatingBtn.setOnAction(e -> updateRating());

        Button openCloseBtn = new Button("פתיחה/סגירה");
        openCloseBtn.setOnAction(e -> toggleOpen());

        Button ordersBtn = new Button("הזמנות המסעדה");
        ordersBtn.setOnAction(e -> showRestaurantOrders());

        Button byTypeBtn = new Button("מסעדות לפי סוג");
        byTypeBtn.setOnAction(e -> filterByType());

        Button reportsBtn = new Button("דוחות בסיסיים");
        reportsBtn.setStyle("-fx-text-fill: blue;");
        reportsBtn.setOnAction(e -> showBasicReports());

        buttonBox.getChildren().addAll(restaurantsBtn, addCustomerBtn, addOrderBtn, updateRatingBtn,
                                       openCloseBtn, ordersBtn, byTypeBtn, reportsBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TextArea
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

    // ==================== REST ADMIN ACTIONS ====================

    /**
     * 1. הצגת המסעדות שבאחריותו
     */
    private void showMyRestaurants() {
        StringBuilder sb = new StringBuilder("המסעדות שלי:\n\n");
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();

        if (myRestaurants.isEmpty()) {
            sb.append("אין מסעדות");
        } else {
            for (Restaurant r : myRestaurants) {
                String status = r.isOpen() ? "פתוחה" : "סגורה";
                sb.append(String.format("- %s (קוד: %d) - דירוג: %.1f⭐ - %s\n", 
                        r.getRestaurantName(), r.getRestaurantId(), r.getRating(), status));
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("המסעדות שלי - " + myRestaurants.size() + " מסעדות");
    }

    /**
     * 2. הוספת לקוח חדש למערכת
     */
    private void addNewCustomer() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הוספת לקוח חדש");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("שם פרטי");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("שם משפחה");

        TextField phoneField = new TextField();
        phoneField.setPromptText("טלפון");

        TextField emailField = new TextField();
        emailField.setPromptText("אימייל");

        content.getChildren().addAll(
                new Label("שם פרטי:"), firstNameField,
                new Label("שם משפחה:"), lastNameField,
                new Label("טלפון:"), phoneField,
                new Label("אימייל:"), emailField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showAlert("שגיאה", "אנא מלא את כל השדות!");
                return;
            }

            int newId = generateNewCustomerId();
            Customer newCustomer = new Customer(newId, firstName, lastName, "", "", "", phone, email, 500);
            customers.add(newCustomer);
            showAlert("הצלחה", "לקוח חדש נוסף! קוד: " + newId);
            statusLabel.setText("לקוח חדש נוסף");
        });
    }

    /**
     * 3. הוספת הזמנה חדשה עבור מסעדה שבאחריותו
     */
    private void addNewOrder() {
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();
        if (myRestaurants.isEmpty()) {
            showAlert("שגיאה", "אין לך מסעדות!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הוספת הזמנה");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("קוד לקוח");

        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.setItems(FXCollections.observableArrayList(myRestaurants));
        restaurantCombo.setPromptText("בחר מסעדה");

        TextField amountField = new TextField("100");
        amountField.setPromptText("סכום");

        content.getChildren().addAll(
                new Label("קוד לקוח:"), customerIdField,
                new Label("מסעדה:"), restaurantCombo,
                new Label("סכום בסיסי:"), amountField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                Restaurant restaurant = restaurantCombo.getValue();
                double amount = Double.parseDouble(amountField.getText().trim());

                if (restaurant == null) {
                    showAlert("שגיאה", "אנא בחר מסעדה!");
                    return;
                }

                // Check customer exists
                Customer c = findCustomerById(customerId);
                if (c == null) {
                    showAlert("שגיאה", "לקוח לא קיים!");
                    return;
                }

                int newOrderId = generateNewOrderId();
                Order newOrder = new Order(newOrderId, customerId, restaurant, 1, 1, 2024, amount);
                orders.add(newOrder);

                showAlert("הצלחה", "הזמנה חדשה נוספה! קוד: " + newOrderId);
                statusLabel.setText("הזמנה חדשה נוספה");
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד לקוח וסכום חייבים להיות מספרים!");
            }
        });
    }

    /**
     * 4. עדכון דירוג מסעדה
     */
    private void updateRating() {
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();
        if (myRestaurants.isEmpty()) {
            showAlert("שגיאה", "אין מסעדות!");
            return;
        }

        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.setItems(FXCollections.observableArrayList(myRestaurants));

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("עדכון דירוג");
        dialog.getDialogPane().setContent(new VBox(10, new Label("בחר מסעדה:"), restaurantCombo));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            Restaurant selected = restaurantCombo.getValue();
            if (selected == null) return;

            TextInputDialog ratingDialog = new TextInputDialog(String.valueOf(selected.getRating()));
            ratingDialog.setTitle("עדכון דירוג");
            ratingDialog.setHeaderText("דירוג חדש (0-5):");

            ratingDialog.showAndWait().ifPresent(ratingStr -> {
                try {
                    double rating = Double.parseDouble(ratingStr);
                    if (rating < 0 || rating > 5) {
                        showAlert("שגיאה", "דירוג חייב להיות בין 0 ל-5!");
                        return;
                    }
                    selected.setRating(rating);
                    showAlert("הצלחה", "דירוג עודכן!");
                } catch (NumberFormatException ex) {
                    showAlert("שגיאה", "דירוג חייב להיות מספר!");
                }
            });
        });
    }

    /**
     * 5. פתיחה/סגירה של מסעדה
     */
    private void toggleOpen() {
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();
        if (myRestaurants.isEmpty()) {
            showAlert("שגיאה", "אין מסעדות!");
            return;
        }

        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.setItems(FXCollections.observableArrayList(myRestaurants));

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("פתיחה/סגירה");
        dialog.getDialogPane().setContent(new VBox(10, new Label("בחר מסעדה:"), restaurantCombo));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            Restaurant selected = restaurantCombo.getValue();
            if (selected != null) {
                selected.setOpen(!selected.isOpen());
                String status = selected.isOpen() ? "פתוחה" : "סגורה";
                showAlert("הצלחה", "מסעדה " + status);
                statusLabel.setText("מסעדה " + status);
            }
        });
    }

    /**
     * 6. צפייה בהזמנות של מסעדה
     */
    private void showRestaurantOrders() {
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();
        ArrayList<Order> restaurantOrders = new ArrayList<>();

        for (Restaurant r : myRestaurants) {
            for (Order o : orders) {
                if (o.getRestaurantId() == r.getRestaurantId()) {
                    restaurantOrders.add(o);
                }
            }
        }

        StringBuilder sb = new StringBuilder("הזמנות המסעדות שלי:\n\n");
        if (restaurantOrders.isEmpty()) {
            sb.append("אין הזמנות");
        } else {
            for (Order o : restaurantOrders) {
                sb.append(String.format("הזמנה #%d - לקוח: %d - מחיר: %.2f₪\n", 
                        o.getOrderId(), o.getCustomerId(), o.getFinalPrice()));
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("הזמנות המסעדות - " + restaurantOrders.size() + " הזמנות");
    }

    /**
     * 7. הצגת מסעדות לפי סוג
     */
    private void filterByType() {
        showAlert("מידע", "הצגה לפי סוג - לבנייה בשלב הבא");
    }

    /**
     * 8. דוחות בסיסיים
     */
    private void showBasicReports() {
        ArrayList<Restaurant> myRestaurants = restAdmin.getRestaurants();
        ArrayList<Order> restaurantOrders = new ArrayList<>();
        double totalRevenue = 0;

        for (Restaurant r : myRestaurants) {
            for (Order o : orders) {
                if (o.getRestaurantId() == r.getRestaurantId()) {
                    restaurantOrders.add(o);
                    totalRevenue += o.getFinalPrice();
                }
            }
        }

        StringBuilder sb = new StringBuilder("דוחות בסיסיים:\n\n");
        sb.append(String.format("מספר מסעדות: %d\n", myRestaurants.size()));
        sb.append(String.format("מספר הזמנות: %d\n", restaurantOrders.size()));
        sb.append(String.format("סך כל הכנסות: %.2f₪\n", totalRevenue));

        if (!restaurantOrders.isEmpty()) {
            sb.append(String.format("ממוצע הזמנה: %.2f₪\n", totalRevenue / restaurantOrders.size()));
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("דוחות בסיסיים");
    }

    // ==================== HELPER METHODS ====================

    private Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) return c;
        }
        return null;
    }

    private int generateNewCustomerId() {
        int maxId = 0;
        for (Customer c : customers) {
            if (c.getCustomerId() > maxId) maxId = c.getCustomerId();
        }
        return maxId + 1;
    }

    private int generateNewOrderId() {
        int maxId = 0;
        for (Order o : orders) {
            if (o.getOrderId() > maxId) maxId = o.getOrderId();
        }
        return maxId + 1;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
