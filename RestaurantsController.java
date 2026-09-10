/**
 * RestaurantsController - עבודה 4 - חלק ד
 * ניהול מסעדות - הצגה, חיפוש, הוספה, פתיחה/סגירה
 * שימוש ב-Enum (CuisineType) בComboBox (חלק ט)
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

public class RestaurantsController {

    private ArrayList<Restaurant> restaurants;
    private TableView<Restaurant> restaurantsTable;
    private Label statusLabel;

    public RestaurantsController(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    /**
     * הצגת מסך ניהול מסעדות
     */
    public BorderPane createRestaurantsScreen() {
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

        Label titleLabel = new Label("ניהול מסעדות");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button displayAllBtn = new Button("הצגת כל המסעדות");
        displayAllBtn.setOnAction(e -> displayAllRestaurants());

        Button searchBtn = new Button("חיפוש מסעדה");
        searchBtn.setOnAction(e -> searchRestaurant());

        Button addBtn = new Button("הוספת מסעדה חדשה");
        addBtn.setStyle("-fx-text-fill: green;");
        addBtn.setOnAction(e -> addNewRestaurant());

        Button ratingBtn = new Button("עדכון דירוג");
        ratingBtn.setOnAction(e -> updateRating());

        Button typeBtn = new Button("הצגה לפי סוג");
        typeBtn.setOnAction(e -> filterByType());

        Button openBtn = new Button("הצגת מסעדות פתוחות");
        openBtn.setOnAction(e -> showOpenRestaurants());

        Button toggleBtn = new Button("פתיחה/סגירה");
        toggleBtn.setOnAction(e -> toggleRestaurant());

        buttonBox.getChildren().addAll(displayAllBtn, searchBtn, addBtn, ratingBtn, typeBtn, openBtn, toggleBtn);
        topBox.getChildren().addAll(titleLabel, buttonBox);

        return topBox;
    }

    /**
     * סעיף מרכזי - TableView
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        restaurantsTable = new TableView<>();
        restaurantsTable.setPrefHeight(400);

        TableColumn<Restaurant, Integer> idCol = new TableColumn<>("קוד");
        idCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getRestaurantId()));

        TableColumn<Restaurant, String> nameCol = new TableColumn<>("שם המסעדה");
        nameCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getRestaurantName()));

        TableColumn<Restaurant, String> cuisineCol = new TableColumn<>("סוג מטבח");
        cuisineCol.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getCuisineType()));

        TableColumn<Restaurant, Double> ratingCol = new TableColumn<>("דירוג");
        ratingCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getRating()));

        TableColumn<Restaurant, Boolean> openCol = new TableColumn<>("פתוח");
        openCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().isOpen()));

        TableColumn<Restaurant, Double> feeCol = new TableColumn<>("עמלת משלוח");
        feeCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getBaseDeliveryFee()));

        restaurantsTable.getColumns().addAll(idCol, nameCol, cuisineCol, ratingCol, openCol, feeCol);

        centerBox.getChildren().add(restaurantsTable);
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
     * 1. הצגת כל המסעדות
     */
    private void displayAllRestaurants() {
        ObservableList<Restaurant> data = FXCollections.observableArrayList(restaurants);
        restaurantsTable.setItems(data);
        statusLabel.setText("מוצגות " + restaurants.size() + " מסעדות");
    }

    /**
     * 2. חיפוש מסעדה לפי קוד (עם RestaurantNotFoundException)
     */
    private void searchRestaurant() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("חיפוש מסעדה");
        dialog.setHeaderText("הזן קוד מסעדה:");
        dialog.setContentText("קוד:");

        dialog.showAndWait().ifPresent(restaurantId -> {
            try {
                int id = Integer.parseInt(restaurantId);
                Restaurant found = findRestaurantById(id);
                if (found != null) {
                    ObservableList<Restaurant> data = FXCollections.observableArrayList(found);
                    restaurantsTable.setItems(data);
                    statusLabel.setText("נמצאה מסעדה: " + found.getRestaurantName());
                } else {
                    // זרוק RestaurantNotFoundException אם מסעדה לא נמצאה
                    try {
                        throw new RestaurantNotFoundException("מסעדה עם קוד " + id + " לא נמצאה במערכת!");
                    } catch (RestaurantNotFoundException ex) {
                        System.out.println("❌ Exception: " + ex.getMessage());
                        showAlert("שגיאה - מסעדה לא נמצאה", ex.getMessage());
                        statusLabel.setText("מסעדה לא נמצאה");
                    }
                }
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד מסעדה חייב להיות מספר!");
                ex.printStackTrace();
            }
        });
    }

    /**
     * 3. הוספת מסעדה חדשה
     */
    private void addNewRestaurant() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הוספת מסעדה חדשה");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("שם המסעדה");

        ComboBox<CuisineType> cuisineCombo = new ComboBox<>();
        cuisineCombo.setItems(FXCollections.observableArrayList(CuisineType.values()));
        cuisineCombo.setPromptText("בחר סוג מטבח");

        TextField ratingField = new TextField("3.5");
        ratingField.setPromptText("דירוג (0-5)");

        TextField feeField = new TextField("15");
        feeField.setPromptText("עמלת משלוח");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.setItems(FXCollections.observableArrayList("רגיל", "מזון מהיר", "יוקרה"));
        typeCombo.setPromptText("סוג מסעדה");

        content.getChildren().addAll(
                new Label("שם המסעדה:"), nameField,
                new Label("סוג מטבח (Enum):"), cuisineCombo,
                new Label("דירוג:"), ratingField,
                new Label("עמלת משלוח:"), feeField,
                new Label("סוג מסעדה:"), typeCombo
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            String name = nameField.getText().trim();
            CuisineType cuisine = cuisineCombo.getValue();
            String ratingStr = ratingField.getText().trim();
            String feeStr = feeField.getText().trim();
            String type = typeCombo.getValue();

            if (name.isEmpty() || cuisine == null || type == null) {
                showAlert("שגיאה", "אנא מלא את כל השדות!");
                return;
            }

            try {
                double rating = Double.parseDouble(ratingStr);
                double fee = Double.parseDouble(feeStr);

                if (rating < 0 || rating > 5) {
                    showAlert("שגיאה", "דירוג חייב להיות בין 0 ל-5!");
                    return;
                }

                if (fee < 0) {
                    showAlert("שגיאה", "עמלה לא יכולה להיות שלילית!");
                    return;
                }

                int newId = generateNewRestaurantId();
                Restaurant newRestaurant = new Restaurant(newId, name, cuisine.toString(), rating, true, fee);
                restaurants.add(newRestaurant);
                displayAllRestaurants();
                showAlert("הצלחה", "מסעדה חדשה נוספה בהצלחה! קוד: " + newId);
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "דירוג ועמלה חייבים להיות מספרים!");
            }
        });
    }

    /**
     * 4. עדכון דירוג
     */
    private void updateRating() {
        Restaurant selected = restaurantsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר מסעדה!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getRating()));
        dialog.setTitle("עדכון דירוג");
        dialog.setHeaderText("עדכן דירוג עבור: " + selected.getRestaurantName());
        dialog.setContentText("דירוג (0-5):");

        dialog.showAndWait().ifPresent(ratingStr -> {
            try {
                double rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 5) {
                    showAlert("שגיאה", "דירוג חייב להיות בין 0 ל-5!");
                    return;
                }
                selected.setRating(rating);
                displayAllRestaurants();
                showAlert("הצלחה", "דירוג עודכן!");
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "דירוג חייב להיות מספר!");
            }
        });
    }

    /**
     * 5. הצגה לפי סוג (Enum ComboBox)
     */
    private void filterByType() {
        ComboBox<CuisineType> cuisineCombo = new ComboBox<>();
        cuisineCombo.setItems(FXCollections.observableArrayList(CuisineType.values()));
        cuisineCombo.setPromptText("בחר סוג מטבח");

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("הצגה לפי סוג מטבח");
        dialog.getDialogPane().setContent(new VBox(10, new Label("בחר סוג מטבח (Enum):"), cuisineCombo));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            CuisineType selected = cuisineCombo.getValue();
            if (selected != null) {
                ArrayList<Restaurant> filtered = new ArrayList<>();
                for (Restaurant r : restaurants) {
                    if (r.getCuisineType().equals(selected.toString())) {
                        filtered.add(r);
                    }
                }
                ObservableList<Restaurant> data = FXCollections.observableArrayList(filtered);
                restaurantsTable.setItems(data);
                statusLabel.setText("מוצגות " + filtered.size() + " מסעדות מסוג: " + selected.getHebrewName());
            }
        });
    }

    /**
     * 6. הצגת מסעדות פתוחות בלבד
     */
    private void showOpenRestaurants() {
        ArrayList<Restaurant> openRestaurants = new ArrayList<>();
        for (Restaurant r : restaurants) {
            if (r.isOpen()) {
                openRestaurants.add(r);
            }
        }
        ObservableList<Restaurant> data = FXCollections.observableArrayList(openRestaurants);
        restaurantsTable.setItems(data);
        statusLabel.setText("מוצגות " + openRestaurants.size() + " מסעדות פתוחות");
    }

    /**
     * 7. פתיחה/סגירה של מסעדה
     */
    private void toggleRestaurant() {
        Restaurant selected = restaurantsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("שגיאה", "אנא בחר מסעדה!");
            return;
        }

        selected.setOpen(!selected.isOpen());
        displayAllRestaurants();
        String status = selected.isOpen() ? "פתוחה" : "סגורה";
        showAlert("הצלחה", "מסעדה עודכנה: " + status);
        statusLabel.setText("מסעדה " + selected.getRestaurantName() + " " + status);
    }

    // ==================== HELPER METHODS ====================

    private Restaurant findRestaurantById(int id) {
        for (Restaurant r : restaurants) {
            if (r.getRestaurantId() == id) return r;
        }
        return null;
    }

    private int generateNewRestaurantId() {
        int maxId = 100;
        for (Restaurant r : restaurants) {
            if (r.getRestaurantId() > maxId) maxId = r.getRestaurantId();
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
