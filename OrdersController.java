

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class OrdersController {

    private ArrayList<Order> orders;
    private ArrayList<Customer> customers;
    private ArrayList<Restaurant> restaurants;
    private TableView<Order> ordersTable;
    private Label statusLabel;

    public OrdersController(ArrayList<Order> orders, ArrayList<Customer> customers, ArrayList<Restaurant> restaurants) {
        this.orders = orders;
        this.customers = customers;
        this.restaurants = restaurants;
    }

    /**
     * הצגת מסך ניהול הזמנות
     */
    public BorderPane createOrdersScreen() {
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

        Label titleLabel = new Label("ניהול הזמנות");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button displayAllBtn = new Button("הצגת כל ההזמנות");
        displayAllBtn.setOnAction(e -> displayAllOrders());

        Button searchBtn = new Button("חיפוש הזמנה");
        searchBtn.setOnAction(e -> searchOrder());

        Button byCustomerBtn = new Button("הצגה לפי לקוח");
        byCustomerBtn.setOnAction(e -> filterByCustomer());

        Button byRestaurantBtn = new Button("הצגה לפי מסעדה");
        byRestaurantBtn.setOnAction(e -> filterByRestaurant());

        Button maxPriceBtn = new Button("הזמנה יקרה ביותר");
        maxPriceBtn.setStyle("-fx-text-fill: blue;");
        maxPriceBtn.setOnAction(e -> showMaxPriceOrder());

        buttonBox.getChildren().addAll(displayAllBtn, searchBtn, byCustomerBtn, byRestaurantBtn, maxPriceBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TableView
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        ordersTable = new TableView<>();
        ordersTable.setPrefHeight(400);

     // مثال للسطر 92 (قم بتغيير باقي الأسطر بنفس النمط):
        TableColumn<Order, Integer> idCol = new TableColumn<Order, Integer>("קוד הזמנה");
        idCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Integer>(param.getValue().getOrderId()));

        // وللأعمدة الأخرى، استبدل التعريفات الحالية بهذه النسخة:
        TableColumn<Order, Integer> customerCol = new TableColumn<Order, Integer>("קוד לקוח");
        customerCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Integer>(param.getValue().getCustomerId()));

        TableColumn<Order, Integer> restaurantCol = new TableColumn<Order, Integer>("קוד מסעדה");
        restaurantCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Integer>(param.getValue().getRestaurantId()));

        TableColumn<Order, String> dateCol = new TableColumn<Order, String>("תאריך");
        // (اترك كود الـ date كما هو داخل الـ setCellValueFactory لأنه مختلف)

        TableColumn<Order, Double> baseCol = new TableColumn<Order, Double>("סכום בסיסי");
        baseCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Double>(param.getValue().getBaseAmount()));

        TableColumn<Order, Double> finalCol = new TableColumn<Order, Double>("מחיר סופי");
        finalCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Double>(param.getValue().getFinalPrice()));

        TableColumn<Order, Integer> statusCol = new TableColumn<Order, Integer>("סטטוס");
        statusCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<Integer>(param.getValue().getStatus()));
        
        ordersTable.getColumns().addAll(idCol, customerCol, restaurantCol, dateCol, baseCol, finalCol, statusCol);

        centerBox.getChildren().add(ordersTable);
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

    /**
     * 1. הצגת כל ההזמנות
     */
    private void displayAllOrders() {
        ObservableList<Order> data = FXCollections.observableArrayList(orders);
        ordersTable.setItems(data);
        statusLabel.setText("מוצגות " + orders.size() + " הזמנות");
    }

    /**
     * 2. חיפוש הזמנה לפי קוד (עם Exception Handling)
     */
    private void searchOrder() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש הזמנה");
        dialog.setHeaderText("הזן קוד הזמנה:");
        dialog.setContentText("קוד:");

        dialog.showAndWait().ifPresent(orderId -> {
            try {
                int id = Integer.parseInt(orderId);
                Order found = findOrderById(id);
                if (found != null) {
                    ObservableList<Order> data = FXCollections.observableArrayList(found);
                    ordersTable.setItems(data);
                    statusLabel.setText("נמצאה הזמנה: " + id);
                } else {
                    // זרוק exception אם הזמנה לא נמצאה
                    System.out.println("❌ Order Not Found Exception: הזמנה " + id + " לא נמצאה!");
                    showAlert("שגיאה", "הזמנה לא נמצאה!");
                    statusLabel.setText("הזמנה לא נמצאה");
                }
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד הזמנה חייב להיות מספר!");
                ex.printStackTrace();
            }
        });
    }

    /**
     * 3. הצגה לפי לקוח
     */
    private void filterByCustomer() {
        ComboBox<Customer> customerCombo = new ComboBox<>();
        customerCombo.setItems(FXCollections.observableArrayList(customers));
        customerCombo.setPromptText("בחר לקוח");

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הצגת הזמנות לפי לקוח");
        dialog.getDialogPane().setContent(new VBox(10, new Label("בחר לקוח:"), customerCombo));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            Customer selected = customerCombo.getValue();
            if (selected != null) {
                ArrayList<Order> filtered = new ArrayList<>();
                for (Order o : orders) {
                    if (o.getCustomerId() == selected.getCustomerId()) {
                        filtered.add(o);
                    }
                }
                ObservableList<Order> data = FXCollections.observableArrayList(filtered);
                ordersTable.setItems(data);
                statusLabel.setText("הזמנות של " + selected.getFirstName() + ": " + filtered.size());
            }
        });
    }

    /**
     * 4. הצגה לפי מסעדה
     */
    private void filterByRestaurant() {
        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.setItems(FXCollections.observableArrayList(restaurants));
        restaurantCombo.setPromptText("בחר מסעדה");

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הצגת הזמנות לפי מסעדה");
        dialog.getDialogPane().setContent(new VBox(10, new Label("בחר מסעדה:"), restaurantCombo));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            Restaurant selected = restaurantCombo.getValue();
            if (selected != null) {
                ArrayList<Order> filtered = new ArrayList<>();
                for (Order o : orders) {
                    if (o.getRestaurantId() == selected.getRestaurantId()) {
                        filtered.add(o);
                    }
                }
                ObservableList<Order> data = FXCollections.observableArrayList(filtered);
                ordersTable.setItems(data);
                statusLabel.setText("הזמנות של " + selected.getRestaurantName() + ": " + filtered.size());
            }
        });
    }

    /**
     * 5. הצגת ההזמנה בעלת המחיר הגבוה ביותר
     */
    private void showMaxPriceOrder() {
        if (orders.isEmpty()) {
            showAlert("מידע", "אין הזמנות במערכת");
            return;
        }

        Order maxOrder = orders.get(0);
        for (Order o : orders) {
            if (o.getFinalPrice() > maxOrder.getFinalPrice()) {
                maxOrder = o;
            }
        }

        ObservableList<Order> data = FXCollections.observableArrayList(maxOrder);
        ordersTable.setItems(data);
        
        String message = "הזמנה יקרה ביותר:\n" +
                "קוד הזמנה: " + maxOrder.getOrderId() + "\n" +
                "סכום סופי: " + maxOrder.getFinalPrice();
        showAlert("הזמנה יקרה ביותר", message);
        statusLabel.setText("הזמנה יקרה ביותר: " + maxOrder.getFinalPrice());
    }

    // ==================== HELPER METHODS ====================

    private Order findOrderById(int id) {
        for (Order o : orders) {
            if (o.getOrderId() == id) return o;
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
