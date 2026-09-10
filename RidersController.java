/**
 * RidersController - עבודה 4 - חלק ו
 * ניהול שליחים - הצגה, הוספה, חיפוש, עדכון סטטוס
 * שימוש ב-TableView + ObservableList (חלק יא)
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

public class RidersController {

    private ArrayList<Rider> riders;
    private ArrayList<Order> orders;
    private TableView<Rider> ridersTable;
    private Label statusLabel;

    public RidersController(ArrayList<Rider> riders, ArrayList<Order> orders) {
        this.riders = riders;
        this.orders = orders;
    }

    /**
     * הצגת מסך ניהול שליחים
     */
    public BorderPane createRidersScreen() {
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

        Label titleLabel = new Label("ניהול שליחים");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button displayAllBtn = new Button("הצגת כל השליחים");
        displayAllBtn.setOnAction(e -> displayAllRiders());

        Button addBtn = new Button("הוספת שליח חדש");
        addBtn.setStyle("-fx-text-fill: green;");
        addBtn.setOnAction(e -> addNewRider());

        Button searchBtn = new Button("חיפוש שליח");
        searchBtn.setOnAction(e -> searchRider());

        Button ordersBtn = new Button("הזמנות של שליח");
        ordersBtn.setOnAction(e -> showRiderOrders());

        Button statusBtn = new Button("עדכון סטטוס");
        statusBtn.setOnAction(e -> updateRiderStatus());

        Button maxDeliveriesBtn = new Button("שליח עם הכי הרבה משלוחים");
        maxDeliveriesBtn.setStyle("-fx-text-fill: blue;");
        maxDeliveriesBtn.setOnAction(e -> showMaxDeliveries());

        buttonBox.getChildren().addAll(displayAllBtn, addBtn, searchBtn, ordersBtn, statusBtn, maxDeliveriesBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TableView
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        ridersTable = new TableView<>();
        ridersTable.setPrefHeight(400);

        TableColumn<Rider, String> idCol = new TableColumn<>("תעודת זהות");
        idCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getIdNumber()));

        TableColumn<Rider, String> firstNameCol = new TableColumn<>("שם פרטי");
        firstNameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getFirstName()));

        TableColumn<Rider, String> lastNameCol = new TableColumn<>("שם משפחה");
        lastNameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getLastName()));

        TableColumn<Rider, String> phoneCol = new TableColumn<>("טלפון");
        phoneCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getPhone()));

        TableColumn<Rider, String> vehicleCol = new TableColumn<>("רכב");
        vehicleCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getVehicle()));

        TableColumn<Rider, Boolean> availableCol = new TableColumn<>("זמין");
        availableCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().isAvailable()));

        ridersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, phoneCol, vehicleCol, availableCol);

        centerBox.getChildren().add(ridersTable);
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
     * 1. הצגת כל השליחים
     */
    private void displayAllRiders() {
        ObservableList<Rider> data = FXCollections.observableArrayList(riders);
        ridersTable.setItems(data);
        statusLabel.setText("מוצגים " + riders.size() + " שליחים");
    }

    /**
     * 2. הוספת שליח חדש
     */
    private void addNewRider() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הוספת שליח חדש");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("תעודת זהות");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("שם פרטי");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("שם משפחה");

        TextField phoneField = new TextField();
        phoneField.setPromptText("טלפון");

        TextField vehicleField = new TextField();
        vehicleField.setPromptText("סוג רכב");

        content.getChildren().addAll(
                new Label("תעודת זהות:"), idField,
                new Label("שם פרטי:"), firstNameField,
                new Label("שם משפחה:"), lastNameField,
                new Label("טלפון:"), phoneField,
                new Label("סוג רכב:"), vehicleField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            String id = idField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String vehicle = vehicleField.getText().trim();

            if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || vehicle.isEmpty()) {
                showAlert("שגיאה", "אנא מלא את כל השדות!");
                return;
            }

            // Check if rider already exists
            if (findRiderById(id) != null) {
                showAlert("שגיאה", "שליח עם תעודת זהות זו כבר קיים!");
                return;
            }

            Rider newRider = new Rider(id, firstName, lastName, phone, vehicle);
            riders.add(newRider);
            displayAllRiders();
            showAlert("הצלחה", "שליח חדש נוסף בהצלחה!");
            statusLabel.setText("שליח חדש נוסף: " + firstName);
        });
    }

    /**
     * 3. חיפוש שליח
     */
    private void searchRider() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש שליח");
        dialog.setHeaderText("הזן תעודת זהות:");
        dialog.setContentText("תעודת זהות:");

        dialog.showAndWait().ifPresent(riderId -> {
            Rider found = findRiderById(riderId);
            if (found != null) {
                ObservableList<Rider> data = FXCollections.observableArrayList(found);
                ridersTable.setItems(data);
                statusLabel.setText("נמצא שליח: " + found.getFirstName());
            } else {
                showAlert("שגיאה", "שליח לא נמצא!");
            }
        });
    }

    /**
     * 4. הצגת הזמנות של שליח
     */
    private void showRiderOrders() {
        Rider selected = ridersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר שליח!");
            return;
        }

        ArrayList<Order> riderOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getRiderId().equals(selected.getIdNumber())) {
                riderOrders.add(o);
            }
        }

        String message = "הזמנות של " + selected.getFirstName() + ":\n";
        message += "סה\"כ הזמנות: " + riderOrders.size();
        showAlert("הזמנות", message);
        statusLabel.setText("הזמנות של " + selected.getFirstName() + ": " + riderOrders.size());
    }

    /**
     * 5. עדכון סטטוס שליח
     */
    private void updateRiderStatus() {
        Rider selected = ridersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר שליח!");
            return;
        }

        selected.setAvailable(!selected.isAvailable());
        displayAllRiders();
        String status = selected.isAvailable() ? "זמין" : "לא זמין";
        showAlert("הצלחה", "שליח עודכן: " + status);
        statusLabel.setText("שליח " + selected.getFirstName() + " - " + status);
    }

    /**
     * 6. הצגת השליח עם הכי הרבה משלוחים (עם Exception Handling)
     */
    private void showMaxDeliveries() {
        if (riders.isEmpty()) {
            System.out.println("❌ DeliveryPersonUnavailable: אין שליחים במערכת");
            showAlert("מידע", "אין שליחים במערכת");
            return;
        }

        // בדוק אם יש לפחות שליח אחד זמין
        boolean anyAvailable = riders.stream().anyMatch(Rider::isAvailable);
        if (!anyAvailable) {
            try {
                throw new DeliveryPersonUnavailableException("אין שליחים זמינים כרגע!");
            } catch (DeliveryPersonUnavailableException ex) {
                System.out.println("❌ Exception: " + ex.getMessage());
                showAlert("שגיאה - אין שליחים זמינים", ex.getMessage());
                return;
            }
        }

        Rider maxRider = riders.get(0);
        int maxCount = countRiderDeliveries(maxRider.getIdNumber());

        for (Rider r : riders) {
            int count = countRiderDeliveries(r.getIdNumber());
            if (count > maxCount) {
                maxRider = r;
                maxCount = count;
            }
        }

        String message = "שליח עם הכי הרבה משלוחים:\n" +
                "שם: " + maxRider.getFirstName() + " " + maxRider.getLastName() + "\n" +
                "מספר משלוחים: " + maxCount;
        showAlert("שליח מובחר", message);
        statusLabel.setText("שליח עם הכי הרבה משלוחים: " + maxRider.getFirstName() + " (" + maxCount + ")");
    }

    // ==================== HELPER METHODS ====================

    private Rider findRiderById(String id) {
        for (Rider r : riders) {
            if (r.getIdNumber().equals(id)) return r;
        }
        return null;
    }

    private int countRiderDeliveries(String riderId) {
        int count = 0;
        for (Order o : orders) {
            if (o.getRiderId().equals(riderId)) {
                count++;
            }
        }
        return count;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
