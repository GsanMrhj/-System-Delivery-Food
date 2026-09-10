/**
 * CustomersController - עבודה 4 - חלק ג
 * ניהול לקוחות - הצגה, חיפוש, הוספה, עדכון
 * שימוש ב-TableView + ObservableList (חלק יא)
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CustomersController {

    private ArrayList<Customer> customers;
    private TableView<Customer> customersTable;
    private Label statusLabel;

    public CustomersController(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    /**
     * הצגת מסך ניהול לקוחות
     */
    public BorderPane createCustomersScreen() {
        BorderPane root = new BorderPane();

        // Top - Title and action buttons
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Center - TableView with customers
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        // Bottom - Status and back button
        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        return root;
    }

    /**
     * סעיף עליון - כפתורים ופעולות
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("ניהול לקוחות");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button displayAllBtn = new Button("הצגת כל הלקוחות");
        displayAllBtn.setOnAction(e -> displayAllCustomers());

        Button searchBtn = new Button("חיפוש לקוח");
        searchBtn.setOnAction(e -> searchCustomer());

        Button addBtn = new Button("הוספת לקוח חדש");
        addBtn.setStyle("-fx-text-fill: green;");
        addBtn.setOnAction(e -> addNewCustomer());

        Button updateBtn = new Button("עדכון פרטי לקוח");
        updateBtn.setOnAction(e -> updateCustomer());

        Button ordersBtn = new Button("הצגת הזמנות של לקוח");
        ordersBtn.setOnAction(e -> showCustomerOrders());

        Button restaurantsBtn = new Button("מסעדות שהלקוח הזמין");
        restaurantsBtn.setOnAction(e -> showCustomerRestaurants());

        buttonBox.getChildren().addAll(displayAllBtn, searchBtn, addBtn, updateBtn, ordersBtn, restaurantsBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TableView
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        // Create TableView
        customersTable = new TableView<>();
        customersTable.setPrefHeight(400);

        // Create columns
        TableColumn<Customer, Integer> idCol = new TableColumn<>("קוד לקוח");
        idCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getCustomerId()));

        TableColumn<Customer, String> firstNameCol = new TableColumn<>("שם פרטי");
        firstNameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getFirstName()));

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("שם משפחה");
        lastNameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getLastName()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("טלפון");
        phoneCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getPhone()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("אימייל");
        emailCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getEmail()));

        TableColumn<Customer, Double> balanceCol = new TableColumn<>("יתרת זיכוי");
        balanceCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getCreditBalance()));

        customersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, phoneCol, emailCol, balanceCol);

        centerBox.getChildren().add(customersTable);
        return centerBox;
    }

    /**
     * סעיף תחתון - סטטוס וחזרה
     */
    private HBox createBottomSection() {
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");

        statusLabel = new Label("מוכן");
        statusLabel.setStyle("-fx-font-size: 12;");

        bottomBox.getChildren().add(statusLabel);
        return bottomBox;
    }

    /**
     * 1. הצגת כל הלקוחות
     */
    private void displayAllCustomers() {
        ObservableList<Customer> data = FXCollections.observableArrayList(customers);
        customersTable.setItems(data);
        statusLabel.setText("מוצגים " + customers.size() + " לקוחות");
    }

    /**
     * 2. חיפוש לקוח לפי קוד (עם CustomerNotFoundException)
     */
    private void searchCustomer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש לקוח");
        dialog.setHeaderText("הזן קוד לקוח:");
        dialog.setContentText("קוד:");

        dialog.showAndWait().ifPresent(customerId -> {
            try {
                int id = Integer.parseInt(customerId);
                Customer found = findCustomerById(id);
                if (found != null) {
                    ObservableList<Customer> data = FXCollections.observableArrayList(found);
                    customersTable.setItems(data);
                    statusLabel.setText("נמצא לקוח: " + found.getFirstName() + " " + found.getLastName());
                } else {
                    // זרוק CustomerNotFoundException אם לקוח לא נמצא
                    try {
                        throw new CustomerNotFoundException("לקוח עם קוד " + id + " לא נמצא במערכת!");
                    } catch (CustomerNotFoundException ex) {
                        System.out.println("❌ Exception: " + ex.getMessage());
                        showAlert("שגיאה - לקוח לא נמצא", ex.getMessage());
                        statusLabel.setText("לקוח לא נמצא");
                    }
                }
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד לקוח חייב להיות מספר!");
                ex.printStackTrace();
            }
        });
    }

    /**
     * 3. הוספת לקוח חדש
     */
    private void addNewCustomer() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("הוספת לקוח חדש");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("שם פרטי");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("שם משפחה");

        TextField streetField = new TextField();
        streetField.setPromptText("רחוב");

        TextField cityField = new TextField();
        cityField.setPromptText("עיר");

        TextField zipField = new TextField();
        zipField.setPromptText("מיקוד");

        TextField phoneField = new TextField();
        phoneField.setPromptText("טלפון");

        TextField emailField = new TextField();
        emailField.setPromptText("אימייל");

        TextField balanceField = new TextField();
        balanceField.setPromptText("יתרת זיכוי");

        content.getChildren().addAll(
                new Label("שם פרטי:"), firstNameField,
                new Label("שם משפחה:"), lastNameField,
                new Label("רחוב:"), streetField,
                new Label("עיר:"), cityField,
                new Label("מיקוד:"), zipField,
                new Label("טלפון:"), phoneField,
                new Label("אימייל:"), emailField,
                new Label("יתרת זיכוי:"), balanceField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (dialog.getResult() != null) {
                // Validation
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String street = streetField.getText().trim();
                String city = cityField.getText().trim();
                String zip = zipField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String balanceStr = balanceField.getText().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    showAlert("שגיאה", "אנא מלא את כל השדות!");
                    return;
                }

                try {
                    double balance = Double.parseDouble(balanceStr);
                    if (balance < 0) {
                        showAlert("שגיאה", "יתרה לא יכולה להיות שלילית!");
                        return;
                    }

                    int newId = generateNewCustomerId();
                    Customer newCustomer = new Customer(newId, firstName, lastName, street, city, zip, phone, email, balance);
                    customers.add(newCustomer);
                    displayAllCustomers();
                    showAlert("הצלחה", "לקוח חדש נוסף בהצלחה! קוד: " + newId);
                    statusLabel.setText("לקוח חדש נוסף: " + firstName);
                } catch (NumberFormatException ex) {
                    showAlert("שגיאה", "יתרה חייבת להיות מספר!");
                }
            }
        });
    }

    /**
     * 4. עדכון פרטי לקוח
     */
    private void updateCustomer() {
        Customer selected = customersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר לקוח לעדכון!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("עדכון פרטי לקוח: " + selected.getFirstName());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField phoneField = new TextField(selected.getPhone());
        TextField emailField = new TextField(selected.getEmail());
        TextField streetField = new TextField(selected.getStreet());
        TextField cityField = new TextField(selected.getCity());

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

            selected.setPhone(phone);
            selected.setEmail(email);
            selected.setStreet(street);
            selected.setCity(city);

            displayAllCustomers();
            showAlert("הצלחה", "הלקוח עודכן בהצלחה!");
            statusLabel.setText("לקוח עודכן: " + selected.getFirstName());
        });
    }

    /**
     * 5. הצגת הזמנות של לקוח
     */
    private void showCustomerOrders() {
        showAlert("מידע", "הצגת הזמנות - לבנייה בשלב הבא");
    }

    /**
     * 6. הצגת מסעדות שהלקוח הזמין
     */
    private void showCustomerRestaurants() {
        showAlert("מידע", "הצגת מסעדות - לבנייה בשלב הבא");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
