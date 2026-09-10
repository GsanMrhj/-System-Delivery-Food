# -System-Delivery-Food

# Food Delivery & Restaurant Management System 🍔🛵

## Overview
This is a comprehensive, desktop-based Object-Oriented application built with **Java** and **JavaFX**. It simulates a complete ecosystem for a food delivery service, featuring distinct interfaces and functionalities for different types of users: System Administrators, Customers, Delivery Riders, and Restaurant Managers.

## Key Features
*   **Role-Based Access Control (RBAC):** Secure login system directing users to their specific dashboards based on their role (Admin, Customer, Rider, Manager).
*   **Graphical User Interface (GUI):** Fully interactive and user-friendly interface built with **JavaFX**, replacing traditional console inputs. Real-time data is displayed using dynamic `TableView` components.
*   **Data Persistence (File I/O):** Implemented a custom saving and loading mechanism using `FileReader`/`FileWriter` and `BufferedReader`/`BufferedWriter`. All system data (Customers, Restaurants, Orders, Riders) is securely saved to and retrieved from `.txt` files.
*   **Advanced Data Manipulation:** Utilized **Lambda Expressions**, **Method References**, and **Comparators** to implement complex sorting and filtering features (e.g., sorting orders by price, filtering restaurants by type or rating).
*   **Robust Error Handling:** Comprehensive implementation of custom Exceptions to ensure the system handles invalid inputs and edge cases gracefully without crashing.
*   **OOP Principles:** Strictly follows Encapsulation, Inheritance, and Polymorphism, ensuring a scalable and maintainable codebase structured across modular files.

## Technologies Used
*   **Language:** Java (JDK 17+)
*   **UI Framework:** JavaFX
*   **Concepts:** Object-Oriented Programming (OOP), Data Structures, File I/O, Exception Handling, Lambda Expressions.
*   **IDE:** Eclipse

## How to Run
1. Clone the repository.
2. Open the project in your preferred Java IDE (Eclipse / IntelliJ).
3. Ensure the JavaFX SDK is properly configured in your build path.
4. Run the `Main.java` file to launch the application.
