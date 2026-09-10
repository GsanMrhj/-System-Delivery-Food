/**
 * RiderScreenController - עבודה 4 - חלק יה
 * מסך שליח אישי - פעולות אישיות של שליח
 * 6 פעולות: צפייה בהזמנות, עדכון סטטוס, היסטוריה וכו'
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RiderScreenController {

    private Rider rider;
    private ArrayList<Order> orders;
    private TextArea infoArea;
    private Label statusLabel;

    public RiderScreenController(Rider rider, ArrayList<Order> orders) {
        this.rider = rider;
        this.orders = orders;
    }

    /**
     * הצגת מסך שליח אישי
     */
    public BorderPane createRiderScreen() {
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

        Label titleLabel = new Label("ברוכים הבאים, " + rider.getFirstName() + "!");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button infoBtn = new Button("פרטים אישיים");
        infoBtn.setOnAction(e -> showPersonalInfo());

        Button ordersBtn = new Button("ההזמנות שלי");
        ordersBtn.setOnAction(e -> showMyOrders());

        Button activeBtn = new Button("הזמנה פעילה");
        activeBtn.setStyle("-fx-text-fill: blue;");
        activeBtn.setOnAction(e -> showActiveOrder());

        Button onWayBtn = new Button("עדכון ל'בדרך'");
        onWayBtn.setOnAction(e -> updateStatusOnWay());

        Button deliveredBtn = new Button("עדכון ל'נמסר'");
        deliveredBtn.setStyle("-fx-text-fill: green;");
        deliveredBtn.setOnAction(e -> updateStatusDelivered());

        Button historyBtn = new Button("היסטוריית משלוחים");
        historyBtn.setOnAction(e -> showDeliveryHistory());

        Button countBtn = new Button("סה\"כ משלוחים");
        countBtn.setStyle("-fx-text-fill: purple;");
        countBtn.setOnAction(e -> showTotalDeliveries());

        buttonBox.getChildren().addAll(infoBtn, ordersBtn, activeBtn, onWayBtn, 
                                       deliveredBtn, historyBtn, countBtn);
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

    // ==================== RIDER ACTIONS ====================

    /**
     * 1. פרטים אישיים
     */
    private void showPersonalInfo() {
        StringBuilder sb = new StringBuilder("פרטים אישיים:\n\n");
        sb.append(String.format("תעודת זהות: %s\n", rider.getIdNumber()));
        sb.append(String.format("שם: %s %s\n", rider.getFirstName(), rider.getLastName()));
        sb.append(String.format("טלפון: %s\n", rider.getPhone()));
        sb.append(String.format("סוג רכב: %s\n", rider.getVehicle()));
        sb.append(String.format("סטטוס: %s\n", rider.isAvailable() ? "זמין ✓" : "לא זמין ✗"));

        int deliveryCount = countMyDeliveries();
        sb.append(String.format("סה\"כ משלוחים: %d\n", deliveryCount));

        infoArea.setText(sb.toString());
        statusLabel.setText("פרטים אישיים");
    }

    /**
     * 2. צפייה בכל ההזמנות שלי
     */
    private void showMyOrders() {
        ArrayList<Order> myOrders = getMyOrders();

        StringBuilder sb = new StringBuilder("ההזמנות שלי:\n\n");
        if (myOrders.isEmpty()) {
            sb.append("אין הזמנות משויכות");
        } else {
            for (Order o : myOrders) {
                String statusStr = getStatusString(o.getStatus());
                sb.append(String.format("הזמנה #%d - מחיר: %.2f₪ - סטטוס: %s\n", 
                        o.getOrderId(), o.getFinalPrice(), statusStr));
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("ההזמנות שלי - " + myOrders.size() + " הזמנות");
    }

    /**
     * 3. צפייה בהזמנה הפעילה הנוכחית
     */
    private void showActiveOrder() {
        ArrayList<Order> myOrders = getMyOrders();
        
        // Find the first "in progress" order
        Order activeOrder = null;
        for (Order o : myOrders) {
            if (o.getStatus() == 1 || o.getStatus() == 2) { // 1 = in progress, 2 = on way
                activeOrder = o;
                break;
            }
        }

        StringBuilder sb = new StringBuilder("הזמנה פעילה:\n\n");
        if (activeOrder == null) {
            sb.append("אין הזמנה פעילה כרגע");
        } else {
            String statusStr = getStatusString(activeOrder.getStatus());
            sb.append(String.format("קוד הזמנה: %d\n", activeOrder.getOrderId()));
            sb.append(String.format("לקוח: %d\n", activeOrder.getCustomerId()));
            sb.append(String.format("מסעדה: %d\n", activeOrder.getRestaurantId()));
            sb.append(String.format("מחיר: %.2f₪\n", activeOrder.getFinalPrice()));
            sb.append(String.format("סטטוס: %s\n", statusStr));
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("הזמנה פעילה");
    }

    /**
     * 4. עדכון סטטוס ל"בדרך"
     */
    private void updateStatusOnWay() {
        ArrayList<Order> myOrders = getMyOrders();
        
        // Find pending order
        Order order = null;
        for (Order o : myOrders) {
            if (o.getStatus() == 1) { // 1 = pending
                order = o;
                break;
            }
        }

        if (order == null) {
            showAlert("מידע", "אין הזמנה בהמתנה");
            statusLabel.setText("אין הזמנה בהמתנה");
            return;
        }

        order.setStatus(2); // 2 = on way
        showAlert("הצלחה", "סטטוס עודכן ל'בדרך'");
        statusLabel.setText("סטטוס עודכן - בדרך");
    }

    /**
     * 5. עדכון סטטוס ל"נמסר"
     */
    private void updateStatusDelivered() {
        ArrayList<Order> myOrders = getMyOrders();
        
        // Find "on way" order
        Order order = null;
        for (Order o : myOrders) {
            if (o.getStatus() == 2) { // 2 = on way
                order = o;
                break;
            }
        }

        if (order == null) {
            showAlert("מידע", "אין הזמנה בדרך");
            statusLabel.setText("אין הזמנה בדרך");
            return;
        }

        order.setStatus(3); // 3 = delivered
        showAlert("הצלחה", "סטטוס עודכן ל'נמסר'");
        statusLabel.setText("סטטוס עודכן - נמסר");
    }

    /**
     * 6. היסטוריית משלוחים
     */
    private void showDeliveryHistory() {
        ArrayList<Order> myDelivered = new ArrayList<>();
        for (Order o : orders) {
            if (o.getRiderId().equals(rider.getIdNumber()) && o.getStatus() == 3) {
                myDelivered.add(o);
            }
        }

        StringBuilder sb = new StringBuilder("היסטוריית משלוחים שהושלמו:\n\n");
        if (myDelivered.isEmpty()) {
            sb.append("אין משלוחים שהושלמו");
        } else {
            for (Order o : myDelivered) {
                sb.append(String.format("הזמנה #%d - מחיר: %.2f₪ - תאריך: %d/%d/%d\n", 
                        o.getOrderId(), o.getFinalPrice(), o.getOrderDay(), 
                        o.getOrderMonth(), o.getOrderYear()));
            }
        }

        infoArea.setText(sb.toString());
        statusLabel.setText("היסטוריית משלוחים - " + myDelivered.size() + " משלוחים");
    }

    /**
     * 7. סה"כ משלוחים
     */
    private void showTotalDeliveries() {
        int total = countMyDeliveries();
        int completed = 0;
        int inProgress = 0;

        for (Order o : orders) {
            if (o.getRiderId().equals(rider.getIdNumber())) {
                if (o.getStatus() == 3) {
                    completed++;
                } else if (o.getStatus() == 1 || o.getStatus() == 2) {
                    inProgress++;
                }
            }
        }

        StringBuilder sb = new StringBuilder("סיכום משלוחים:\n\n");
        sb.append(String.format("סה\"כ משלוחים: %d\n", total));
        sb.append(String.format("משלוחים שהושלמו: %d\n", completed));
        sb.append(String.format("משלוחים בתהליך: %d\n", inProgress));

        infoArea.setText(sb.toString());
        statusLabel.setText("סה\"כ משלוחים: " + total);
    }

    // ==================== HELPER METHODS ====================

    private ArrayList<Order> getMyOrders() {
        ArrayList<Order> myOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getRiderId().equals(rider.getIdNumber())) {
                myOrders.add(o);
            }
        }
        return myOrders;
    }

    private int countMyDeliveries() {
        int count = 0;
        for (Order o : orders) {
            if (o.getRiderId().equals(rider.getIdNumber())) {
                count++;
            }
        }
        return count;
    }

    private String getStatusString(int status) {
        switch (status) {
            case 1: return "בהמתנה";
            case 2: return "בדרך";
            case 3: return "נמסר";
            default: return "לא ידוע";
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
