/**
 * RestAdminsController - עבודה 4 - חלק ז
 * ניהול מנהלי מסעדות - הצגה, הוספה, חיפוש, עדכון סטטוס
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

public class RestAdminsController {

    private ArrayList<RestAdmin> restAdmins;
    private ArrayList<Restaurant> restaurants;
    private TableView<RestAdmin> restAdminsTable;
    private Label statusLabel;

    public RestAdminsController(ArrayList<RestAdmin> restAdmins, ArrayList<Restaurant> restaurants) {
        this.restAdmins = restAdmins;
        this.restaurants = restaurants;
    }

    /**
     * הצגת מסך ניהול מנהלי מסעדות
     */
    public BorderPane createRestAdminsScreen() {
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

        Label titleLabel = new Label("ניהול מנהלי מסעדות");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button displayAllBtn = new Button("הצגת כל המנהלים");
        displayAllBtn.setOnAction(e -> displayAllRestAdmins());

        Button addBtn = new Button("הוספת מנהל חדש");
        addBtn.setStyle("-fx-text-fill: green;");
        addBtn.setOnAction(e -> addNewRestAdmin());

        Button searchBtn = new Button("חיפוש מנהל");
        searchBtn.setOnAction(e -> searchRestAdmin());

        Button statusBtn = new Button("עדכון סטטוס");
        statusBtn.setOnAction(e -> updateRestAdminStatus());

        buttonBox.getChildren().addAll(displayAllBtn, addBtn, searchBtn, statusBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TableView
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        restAdminsTable = new TableView<>();
        restAdminsTable.setPrefHeight(400);

        TableColumn<RestAdmin, String> nameCol = new TableColumn<>("שם מנהל");
        nameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getAdminName()));

        TableColumn<RestAdmin, String> usernameCol = new TableColumn<>("שם משתמש");
        usernameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getUsername()));

        TableColumn<RestAdmin, Integer> restaurantsCol = new TableColumn<>("מספר מסעדות");
        restaurantsCol.setCellValueFactory(param -> {
            int count = param.getValue().getRestaurants().size();
            return new javafx.beans.property.SimpleObjectProperty<>(count);
        });

        restAdminsTable.getColumns().addAll(nameCol, usernameCol, restaurantsCol);

        centerBox.getChildren().add(restAdminsTable);
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
     * 1. הצגת כל המנהלים
     */
    private void displayAllRestAdmins() {
        ObservableList<RestAdmin> data = FXCollections.observableArrayList(restAdmins);
        restAdminsTable.setItems(data);
        statusLabel.setText("מוצגים " + restAdmins.size() + " מנהלים");
    }

    /**
     * 2. הוספת מנהל חדש
     */
    private void addNewRestAdmin() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הוספת מנהל מסעדה חדש");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("שם מנהל");

        TextField usernameField = new TextField();
        usernameField.setPromptText("שם משתמש");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("סיסמה");

        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.setItems(FXCollections.observableArrayList(restaurants));
        restaurantCombo.setPromptText("בחר מסעדה");

        content.getChildren().addAll(
                new Label("שם מנהל:"), nameField,
                new Label("שם משתמש:"), usernameField,
                new Label("סיסמה:"), passwordField,
                new Label("מסעדה:"), restaurantCombo
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            Restaurant restaurant = restaurantCombo.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || restaurant == null) {
                showAlert("שגיאה", "אנא מלא את כל השדות!");
                return;
            }

            RestAdmin newAdmin = new RestAdmin(name, username, password);
            newAdmin.addRestaurant(restaurant);
            restAdmins.add(newAdmin);
            displayAllRestAdmins();
            showAlert("הצלחה", "מנהל חדש נוסף בהצלחה!");
            statusLabel.setText("מנהל חדש: " + name);
        });
    }

    /**
     * 3. חיפוש מנהל
     */
    private void searchRestAdmin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש מנהל");
        dialog.setHeaderText("הזן שם מנהל:");
        dialog.setContentText("שם:");

        dialog.showAndWait().ifPresent(adminName -> {
            RestAdmin found = findRestAdminByName(adminName);
            if (found != null) {
                ObservableList<RestAdmin> data = FXCollections.observableArrayList(found);
                restAdminsTable.setItems(data);
                statusLabel.setText("נמצא מנהל: " + found.getAdminName());
            } else {
                showAlert("שגיאה", "מנהל לא נמצא!");
            }
        });
    }

    /**
     * 4. עדכון סטטוס מנהל
     * (במערכת זו אפשר להוסיף מסעדה נוספת או להסיר)
     */
    private void updateRestAdminStatus() {
        RestAdmin selected = restAdminsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר מנהל!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("עדכון מסעדות עבור: " + selected.getAdminName());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        ListView<Restaurant> currentRestaurants = new ListView<>();
        currentRestaurants.setItems(FXCollections.observableArrayList(selected.getRestaurants()));
        currentRestaurants.setPrefHeight(150);

        ComboBox<Restaurant> availableCombo = new ComboBox<>();
        ArrayList<Restaurant> available = new ArrayList<>();
        for (Restaurant r : restaurants) {
            if (!selected.getRestaurants().contains(r)) {
                available.add(r);
            }
        }
        availableCombo.setItems(FXCollections.observableArrayList(available));
        availableCombo.setPromptText("בחר מסעדה להוסיף");

        Button addRestBtn = new Button("הוסף מסעדה");
        addRestBtn.setOnAction(e -> {
            Restaurant selected2 = availableCombo.getValue();
            if (selected2 != null) {
                selected.addRestaurant(selected2);
                currentRestaurants.setItems(FXCollections.observableArrayList(selected.getRestaurants()));
                availableCombo.setItems(FXCollections.observableArrayList(available));
            }
        });

        content.getChildren().addAll(
                new Label("מסעדות נוכחיות:"),
                currentRestaurants,
                new Label("הוספת מסעדה:"),
                availableCombo,
                addRestBtn
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            displayAllRestAdmins();
            showAlert("הצלחה", "מנהל עודכן!");
        });
    }

    // ==================== HELPER METHODS ====================

    private RestAdmin findRestAdminByName(String name) {
        for (RestAdmin ra : restAdmins) {
            if (ra.getAdminName().equalsIgnoreCase(name)) {
                return ra;
            }
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
