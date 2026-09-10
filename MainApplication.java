/**
 * MainApplication - עבודה 4 - חלק א
 * נקודת כניסה ל-JavaFX Application
 * המערכת כולה עובדת דרך JavaFX - אין Scanner, אין println
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;

public class MainApplication extends Application {

    // Global data structures (from HW2)
    private static ArrayList<Customer> customers;
    private static ArrayList<Restaurant> restaurants;
    private static ArrayList<Rider> riders;
    private static ArrayList<Order> orders;
    private static ArrayList<RestAdmin> restAdmins;
    private static ArrayList<Admin> admins;

    private static HashMap<Integer, ArrayList<Order>> ordersByCustomer;
    private static HashMap<Integer, ArrayList<Order>> ordersByRestaurant;
    private static HashMap<String, ArrayList<Order>> ordersByRider;

    private Stage primaryStage;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        // Initialize all data structures
        initializeData();
        
        // Show login screen
        showLoginScreen();
        
        primaryStage.setTitle("מערכת ניהול הזמנות ומשלוחים - Food Delivery System");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.show();
    }

    /**
     * Initialize all data structures (like initializeData from HW2/3)
     */
    private void initializeData() {
        customers = new ArrayList<>();
        restaurants = new ArrayList<>();
        riders = new ArrayList<>();
        orders = new ArrayList<>();
        restAdmins = new ArrayList<>();
        admins = new ArrayList<>();

        ordersByCustomer = new HashMap<>();
        ordersByRestaurant = new HashMap<>();
        ordersByRider = new HashMap<>();

        // Add sample data (from HW2)
        initializeSampleCustomers();
        initializeSampleRestaurants();
        initializeSampleRiders();
        initializeSampleAdmins();
    }

    /**
     * מסך הלוגין - הנקודה של תחילת המערכת
     */
    private void showLoginScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header
        Label titleLabel = new Label("מערכת ניהול הזמנות ומשלוחים");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        VBox header = new VBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        root.setTop(header);

        // Main content - 3 buttons
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(50));

        Button adminButton = new Button("כניסת מנהל מערכת");
        adminButton.setPrefWidth(250);
        adminButton.setPrefHeight(60);
        adminButton.setStyle("-fx-font-size: 16;");
        adminButton.setOnAction(e -> showAdminLoginScreen());

        Button userButton = new Button("כניסת משתמש");
        userButton.setPrefWidth(250);
        userButton.setPrefHeight(60);
        userButton.setStyle("-fx-font-size: 16;");
        userButton.setOnAction(e -> showUserTypeScreen());

        Button exitButton = new Button("יציאה מהמערכת");
        exitButton.setPrefWidth(250);
        exitButton.setPrefHeight(60);
        exitButton.setStyle("-fx-font-size: 16;");
        exitButton.setOnAction(e -> primaryStage.close());

        centerContent.getChildren().addAll(adminButton, userButton, exitButton);
        root.setCenter(centerContent);

        // Status bar
        statusLabel = new Label("ברוכים הבאים למערכת");
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * מסך כניסת מנהל - שם משתמש וסיסמה
     */
    private void showAdminLoginScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header
        Label titleLabel = new Label("כניסת מנהל מערכת");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        VBox header = new VBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        root.setTop(header);

        // Login form
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(50));
        form.setMaxWidth(300);

        Label usernameLabel = new Label("שם משתמש:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("הזן שם משתמש");

        Label passwordLabel = new Label("סיסמה:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("הזן סיסמה");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("כניסה");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("שגיאה", "אנא הזן שם משתמש וסיסמה");
                return;
            }

            // Validate admin credentials (admin / 12345)
            if (username.equals("admin") && password.equals("12345")) {
                showAdminDashboard();
            } else {
                showAlert("שגיאה", "שם משתמש או סיסמה לא נכונים");
                passwordField.clear();
            }
        });

        Button backButton = new Button("חזור");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> showLoginScreen());

        buttonBox.getChildren().addAll(loginButton, backButton);

        form.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                buttonBox
        );

        // Center the form
        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(form);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * בחירת סוג משתמש - לקוח / שליח / מנהל מסעדה
     */
    private void showUserTypeScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header
        Label titleLabel = new Label("בחר סוג משתמש");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        VBox header = new VBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        root.setTop(header);

        // Buttons for user types
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(50));

        Button customerButton = new Button("כניסת לקוח");
        customerButton.setPrefWidth(250);
        customerButton.setPrefHeight(60);
        customerButton.setStyle("-fx-font-size: 16;");
        customerButton.setOnAction(e -> showCustomerLoginScreen());

        Button riderButton = new Button("כניסת שליח");
        riderButton.setPrefWidth(250);
        riderButton.setPrefHeight(60);
        riderButton.setStyle("-fx-font-size: 16;");
        riderButton.setOnAction(e -> showRiderLoginScreen());

        Button restAdminButton = new Button("כניסת מנהל מסעדה");
        restAdminButton.setPrefWidth(250);
        restAdminButton.setPrefHeight(60);
        restAdminButton.setStyle("-fx-font-size: 16;");
        restAdminButton.setOnAction(e -> showRestAdminLoginScreen());

        Button backButton = new Button("חזור");
        backButton.setPrefWidth(250);
        backButton.setStyle("-fx-font-size: 16;");
        backButton.setOnAction(e -> showLoginScreen());

        centerContent.getChildren().addAll(customerButton, riderButton, restAdminButton, backButton);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * כניסת לקוח
     */
    private void showCustomerLoginScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("כניסת לקוח");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label idLabel = new Label("קוד לקוח:");
        TextField idField = new TextField();
        idField.setPromptText("הזן קוד לקוח");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("כניסה");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                showAlert("שגיאה", "אנא הזן קוד לקוח");
                return;
            }

            try {
                int customerId = Integer.parseInt(idStr);
                Customer customer = findCustomerById(customerId);
                if (customer != null) {
                    showCustomerDashboard(customer);
                } else {
                    showAlert("שגיאה", "לקוח לא נמצא במערכת");
                }
            } catch (NumberFormatException ex) {
                showAlert("שגיאה", "קוד לקוח חייב להיות מספר");
            }
        });

        Button backButton = new Button("חזור");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> showUserTypeScreen());

        buttonBox.getChildren().addAll(loginButton, backButton);
        root.getChildren().addAll(titleLabel, idLabel, idField, buttonBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * כניסת שליח
     */
    private void showRiderLoginScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("כניסת שליח");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label idLabel = new Label("תעודת זהות:");
        TextField idField = new TextField();
        idField.setPromptText("הזן תעודת זהות");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("כניסה");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                showAlert("שגיאה", "אנא הזן תעודת זהות");
                return;
            }

            Rider rider = findRiderById(idStr);
            if (rider != null) {
                showRiderDashboard(rider);
            } else {
                showAlert("שגיאה", "שליח לא נמצא במערכת");
            }
        });

        Button backButton = new Button("חזור");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> showUserTypeScreen());

        buttonBox.getChildren().addAll(loginButton, backButton);
        root.getChildren().addAll(titleLabel, idLabel, idField, buttonBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * כניסת מנהל מסעדה
     */
    private void showRestAdminLoginScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("כניסת מנהל מסעדה");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label usernameLabel = new Label("שם משתמש:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("הזן שם משתמש");

        Label passwordLabel = new Label("סיסמה:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("הזן סיסמה");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("כניסה");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("שגיאה", "אנא הזן שם משתמש וסיסמה");
                return;
            }

            RestAdmin restAdmin = findRestAdminByCredentials(username, password);
            if (restAdmin != null) {
                showRestAdminDashboard(restAdmin);
            } else {
                showAlert("שגיאה", "שם משתמש או סיסמה לא נכונים");
                passwordField.clear();
            }
        });

        Button backButton = new Button("חזור");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> showUserTypeScreen());

        buttonBox.getChildren().addAll(loginButton, backButton);
        root.getChildren().addAll(titleLabel, usernameLabel, usernameField, 
                                 passwordLabel, passwordField, buttonBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
    }

    // ==================== DASHBOARD METHODS ====================

    /**
     * מסך מנהל המערכת - מרכז השליטה (חלק א - ב)
     * 9 אפשרויות: ניהול לקוחות, מסעדות, הזמנות, שליחים, מנהלים, דוחות, שמירה, טעינה, יציאה
     */
    private void showAdminDashboard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // ============== Header ==============
        Label titleLabel = new Label("🔐 מסך מנהל מערכת");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        VBox headerBox = new VBox(titleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        root.setTop(headerBox);

        // ============== Left Menu ==============
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: #f0f0f0;");

        Label menuTitle = new Label("📋 תפריט");
        menuTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // ניהול לקוחות
        Button customersBtn = new Button("👥 ניהול לקוחות");
        customersBtn.setPrefWidth(200);
        customersBtn.setPrefHeight(40);
        customersBtn.setStyle("-fx-font-size: 12;");
        customersBtn.setOnAction(e -> showCustomersManagement());

        // ניהול מסעדות
        Button restaurantsBtn = new Button("🍽️ ניהול מסעדות");
        restaurantsBtn.setPrefWidth(200);
        restaurantsBtn.setPrefHeight(40);
        restaurantsBtn.setStyle("-fx-font-size: 12;");
        restaurantsBtn.setOnAction(e -> showRestaurantsManagement());

        // ניהול הזמנות
        Button ordersBtn = new Button("📦 ניהול הזמנות");
        ordersBtn.setPrefWidth(200);
        ordersBtn.setPrefHeight(40);
        ordersBtn.setStyle("-fx-font-size: 12;");
        ordersBtn.setOnAction(e -> showOrdersManagement());

        // ניהול שליחים
        Button ridersBtn = new Button("🏍️ ניהול שליחים");
        ridersBtn.setPrefWidth(200);
        ridersBtn.setPrefHeight(40);
        ridersBtn.setStyle("-fx-font-size: 12;");
        ridersBtn.setOnAction(e -> showRidersManagement());

        // ניהול מנהלים
        Button restAdminsBtn = new Button("👤 ניהול מנהלים");
        restAdminsBtn.setPrefWidth(200);
        restAdminsBtn.setPrefHeight(40);
        restAdminsBtn.setStyle("-fx-font-size: 12;");
        restAdminsBtn.setOnAction(e -> showRestAdminsManagement());

        // דוחות ומיונים
        Button reportsBtn = new Button("📊 דוחות ומיונים");
        reportsBtn.setPrefWidth(200);
        reportsBtn.setPrefHeight(40);
        reportsBtn.setStyle("-fx-font-size: 12; -fx-text-fill: darkblue;");
        reportsBtn.setOnAction(e -> showReportsScreen());

        Separator sep = new Separator();

        // שמירה נתונים
        Button saveBtn = new Button("💾 שמירה נתונים");
        saveBtn.setPrefWidth(200);
        saveBtn.setPrefHeight(40);
        saveBtn.setStyle("-fx-font-size: 12; -fx-text-fill: green; -fx-font-weight: bold;");
        saveBtn.setOnAction(e -> saveAllData());

        // טעינת נתונים
        Button loadBtn = new Button("📂 טעינת נתונים");
        loadBtn.setPrefWidth(200);
        loadBtn.setPrefHeight(40);
        loadBtn.setStyle("-fx-font-size: 12; -fx-text-fill: blue; -fx-font-weight: bold;");
        loadBtn.setOnAction(e -> loadAllData());

        // יציאה
        Button logoutBtn = new Button("🚪 יציאה מהמערכת");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setPrefHeight(40);
        logoutBtn.setStyle("-fx-font-size: 12; -fx-text-fill: red; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> showLoginScreen());

        menu.getChildren().addAll(
                menuTitle,
                customersBtn,
                restaurantsBtn,
                ordersBtn,
                ridersBtn,
                restAdminsBtn,
                reportsBtn,
                sep,
                saveBtn,
                loadBtn,
                logoutBtn
        );

        root.setLeft(menu);

        // ============== Center Content ==============
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("ברוכים הבאים למערכת ניהול הזמנות ומשלוחים!");
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextArea infoArea = new TextArea();
        infoArea.setWrapText(true);
        infoArea.setEditable(false);
        infoArea.setPrefHeight(400);
        infoArea.setText(
                "📊 סיכום המערכת:\n\n" +
                "👥 לקוחות: " + customers.size() + "\n" +
                "🍽️ מסעדות: " + restaurants.size() + "\n" +
                "📦 הזמנות: " + orders.size() + "\n" +
                "🏍️ שליחים: " + riders.size() + "\n" +
                "👤 מנהלים: " + restAdmins.size() + "\n\n" +
                "בחר אפשרות מהתפריט בצד שמאל כדי להתחיל."
        );

        content.getChildren().addAll(welcomeLabel, infoArea);
        root.setCenter(content);

        // ============== Bottom Status Bar ==============
        HBox footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");
        Label statusBar = new Label("✅ מוכן");
        footer.getChildren().add(statusBar);
        root.setBottom(footer);

        Scene scene = new Scene(root, 1000, 750);
        primaryStage.setScene(scene);
    }

    // ==================== MANAGEMENT SCREENS ====================

    private void showCustomersManagement() {
        CustomersController controller = new CustomersController(customers);
        BorderPane screen = controller.createCustomersScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showRestaurantsManagement() {
        RestaurantsController controller = new RestaurantsController(restaurants);
        BorderPane screen = controller.createRestaurantsScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showOrdersManagement() {
        OrdersController controller = new OrdersController(orders, customers, restaurants);
        BorderPane screen = controller.createOrdersScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showRidersManagement() {
        RidersController controller = new RidersController(riders, orders);
        BorderPane screen = controller.createRidersScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showRestAdminsManagement() {
        RestAdminsController controller = new RestAdminsController(restAdmins, restaurants);
        BorderPane screen = controller.createRestAdminsScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showReportsScreen() {
        ReportsController controller = new ReportsController(customers, restaurants, riders, orders);
        BorderPane screen = controller.createReportsScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    // ==================== PERSONAL USER SCREENS ====================

    private void showCustomerDashboard(Customer customer) {
        CustomerScreenController controller = new CustomerScreenController(customer, restaurants, orders);
        BorderPane screen = controller.createCustomerScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showRiderDashboard(Rider rider) {
        RiderScreenController controller = new RiderScreenController(rider, orders);
        BorderPane screen = controller.createRiderScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    private void showRestAdminDashboard(RestAdmin restAdmin) {
        RestAdminScreenController controller = new RestAdminScreenController(restAdmin, restaurants, customers, orders);
        BorderPane screen = controller.createRestAdminScreen();
        Scene scene = new Scene(screen, 900, 700);
        primaryStage.setScene(scene);
    }

    // ==================== DATA INITIALIZATION ====================

    private void initializeSampleCustomers() {
        for (int i = 1; i <= 10; i++) {
            customers.add(new Customer(i, "שם" + i, "משפחה" + i, 
                    "רחוב " + i, "חיפה", "3303220", "050123456" + i, 
                    "user" + i + "@email.com", 200 + i * 50));
        }
    }

    private void initializeSampleRestaurants() {
        // Regular restaurants
        for (int i = 101; i <= 110; i++) {
            restaurants.add(new Restaurant(i, "מסעדה " + i, "אִיטַלְקִי", 
                    3.5 + (i % 3) * 0.5, true, 15.0));
        }

        // Fast food
        for (int i = 201; i <= 210; i++) {
            restaurants.add(new FastFoodRestaurant(i, "מהיר " + i, "אמריקני", 
                    4.0 + (i % 2) * 0.3, true, 10.0, 15, 5.0));
        }

        // Premium
        for (int i = 301; i <= 310; i++) {
            restaurants.add(new PremiumRestaurant(i, "יוקרה " + i, "צָרְפָתִי", 
                    4.5 + (i % 2) * 0.2, true, 25.0, 100.0, 15.0));
        }
    }

    private void initializeSampleRiders() {
        for (int i = 100; i <= 105; i++) {
            riders.add(new Rider("" + i, "שליח" + i, "משפחה" + i, 
                    "050123456" + i, "אופנוע"));
        }
    }

    private void initializeSampleAdmins() {
        admins.add(new Admin("אדם מנהל", "admin", "12345"));
    }

    // ==================== HELPER METHODS ====================

    private Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) return c;
        }
        return null;
    }

    private Rider findRiderById(String id) {
        for (Rider r : riders) {
            if (r.getIdNumber().equals(id)) return r;
        }
        return null;
    }

    private RestAdmin findRestAdminByCredentials(String username, String password) {
        for (RestAdmin ra : restAdmins) {
            if (ra.validateLogin(username, password)) return ra;
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

    private void saveAllData() {
        try {
            FileIOManager.saveCustomers(customers);
            FileIOManager.saveRestaurants(restaurants);
            FileIOManager.saveRiders(riders);
            FileIOManager.saveOrders(orders, ordersByCustomer, ordersByRestaurant, ordersByRider);
            FileIOManager.saveRestAdmins(restAdmins);
            showAlert("הצלחה", "הנתונים נשמרו בהצלחה!");
        } catch (Exception ex) {
            showAlert("שגיאה", "שגיאה בשמירת הנתונים: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadAllData() {
        try {
            customers = FileIOManager.loadCustomers();
            restaurants = FileIOManager.loadRestaurants();
            riders = FileIOManager.loadRiders();
            orders = FileIOManager.loadOrders(restaurants, customers);
            restAdmins = FileIOManager.loadRestAdmins(restaurants);
            showAlert("הצלחה", "הנתונים נטענו בהצלחה!");
        } catch (Exception ex) {
            showAlert("שגיאה", "שגיאה בטעינת הנתונים: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
