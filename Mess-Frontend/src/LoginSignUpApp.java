import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.sql.*;

public class LoginSignUpApp extends Application {

    private Stage primaryStage;
    private TextField loginIdField;
    private PasswordField passwordField;
    private double defaultWidth = 300;
    private double defaultHeight = 250;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");

        Label headingLabel = new Label("Mess Management System");
        headingLabel.setStyle("-fx-font-weight: bold;");

        Label loginIdLabel = new Label("Student ID:");
        loginIdField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Switch to the signup page
            showSignUpPage();
        });
        
     // Create a ChoiceBox for selecting login type
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Student", "Manager", "Admin");
        choiceBox.setValue("Student"); // Default selection
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Show login fields based on selected option
            if (newValue.equals("Manager") || newValue.equals("Admin")) {
                loginIdLabel.setText(newValue + " ID:");
                // Hide signup button for Manager and Admin
                signUpButton.setVisible(false);
            } else {
                loginIdLabel.setText("Student ID:");
                // Show signup button for Student
                signUpButton.setVisible(true);
            }
        });
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            // Check if ID or password fields are empty
            if (loginIdField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                // Show dialog box for empty fields
                showAlert("Error", "Please enter ID and password.");
            } else {
                try {
                    // Attempt to parse the ID
                    int id = Integer.parseInt(loginIdField.getText());
                    String password = passwordField.getText();
                    
                    // Fetch user data from the database based on the selected login type
                    String loginType = choiceBox.getValue();
                    boolean isValidUser = false;
                    switch (loginType) {
                        case "Student":
                            isValidUser = checkLogin(id, password, "student");
                            break;
                        case "Manager":
                            isValidUser = checkLogin(id, password, "manager");
                            break;
                        case "Admin":
                            isValidUser = checkLogin(id, password, "admin");
                            break;
                    }
                    
                    // Check if any matching user is found
                    if (isValidUser) {
                        // Login successful, switch to the corresponding dashboard
                        switchToDashboard(loginType, id);
                    } else {
                        // Show dialog box for invalid credentials
                        showAlert("Error", "Invalid ID or password. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    // Show dialog box for invalid ID format
                    showAlert("Error", "Please enter a valid ID.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database error
                    showAlert("Error", "An error occurred while attempting to log in.");
                }
            }
        });

        // Define signUpButton here so it can be accessed by choiceBox listener
        

        VBox loginFieldsBox = new VBox(10);
        loginFieldsBox.setAlignment(Pos.CENTER);
        loginFieldsBox.getChildren().addAll(
                new HBox(10, loginIdLabel, loginIdField),
                new HBox(10, passwordLabel, passwordField),
                new HBox(10, loginButton, signUpButton)
        );

        // Layout for login fields and choice box
        VBox loginLayout = new VBox(20);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(
                headingLabel,
                choiceBox,
                loginFieldsBox
        );

        // Set window size
        setWindowSize(primaryStage);

     // Create a GridPane as the root node of the scene
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER); // This will center the loginLayout in the scene

        // Add loginLayout to the root GridPane
        root.getChildren().add(loginLayout);

        // Create the scene with the root GridPane
        Scene scene = new Scene(root, defaultWidth, defaultHeight);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private boolean checkLogin(int id, String password, String userType) throws SQLException {
        Connection connection = THE_CONNECTION.getTheConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM " + userType + " WHERE " + userType + "_id = ? AND password = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next(); // Returns true if there is a match
    }

    private void switchToDashboard(String loginType, int id) {
        switch (loginType) {
            case "Student":
                StudentDashboard studentDashboard = new StudentDashboard(id);
                primaryStage.setScene(studentDashboard.createScene());
                break;
            case "Manager":
                ManagerDashboard managerDashboard = new ManagerDashboard(id);
                primaryStage.setScene(managerDashboard.createScene());
                break;
            case "Admin":
                AdminDashboard adminDashboard = new AdminDashboard();
                primaryStage.setScene(adminDashboard.createScene());
                break;
        }
    }

    private void showSignUpPage() {
        primaryStage.setTitle("Sign Up");

        Label headingLabel = new Label("Mess Management System");
        headingLabel.setStyle("-fx-font-weight: bold;");

        Label studentIdLabel = new Label("Student ID:");
        TextField studentIdField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label hostelLabel = new Label("Hostel:");
        ComboBox<String> hostelComboBox = new ComboBox<>();
        hostelComboBox.getItems().addAll("Krishna", "Gandhi", "Rana Pratap", "VK Bhawan", "Ram", "Budh", "Meera", "Shankar", "Vyas", "SR Bhawan");

        Label roomLabel = new Label("Room:");
        TextField roomField = new TextField();

        Label messLabel = new Label("Mess:");
        ComboBox<String> messComboBox = new ComboBox<>();
        messComboBox.getItems().addAll("RP - 101", "Meera - 102", "RB - 103", "KG - 104", "SR - 105");

        Label passwordLabel = new Label("Password:");
        PasswordField signUpPasswordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(event -> {
            // Add signup logic here
            String studentIdText = studentIdField.getText();
            String roomText = roomField.getText();
            
            if (studentIdText.length() < 8) {
                showAlert("Error", "Student ID must be at least 8 digits.");
                return;
            }
            
            if (!isNumeric(studentIdText)) {
                showAlert("Error", "Student ID must be a number.");
                return;
            }
            
            int studentId = Integer.parseInt(studentIdText);
            
            if (!isNumeric(roomText)) {
                showAlert("Error", "Room number must be a number.");
                return;
            }
            
            int room = Integer.parseInt(roomText);
            
            if (room < 2000 || room > 5000) {
                showAlert("Error", "Room number must be between 2000 and 5000.");
                return;
            }
            
            String name = nameField.getText();
            String hostel = hostelComboBox.getValue();
            String mess = messComboBox.getValue();
            // Extract the Mess ID from the Mess ComboBox selection
            int messId = Integer.parseInt(mess.split(" - ")[1]);
            String password = signUpPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (password.equals(confirmPassword)) {
                try {
                    Connection connection = THE_CONNECTION.getTheConnection();
                    // Insert student details into the student table
                    PreparedStatement studentStatement = connection.prepareStatement(
                            "INSERT INTO student (student_id, name, hostel, room, mess_id, password) VALUES (?, ?, ?, ?, ?, ?)"
                    );
                    studentStatement.setInt(1, studentId);
                    studentStatement.setString(2, name);
                    studentStatement.setString(3, hostel);
                    studentStatement.setInt(4, room);
                    studentStatement.setInt(5, messId);
                    studentStatement.setString(6, password);
                    studentStatement.executeUpdate();

                    // Insert student meal owner details into meal_owner table
                    String mealOwnerId = studentIdText.substring(Math.max(0, studentIdText.length() - 6));
                    PreparedStatement mealOwnerStatement = connection.prepareStatement(
                            "INSERT INTO meal_owner (meal_id, student_id) VALUES (?, ?)"
                    );
                    mealOwnerStatement.setString(1, mealOwnerId);
                    mealOwnerStatement.setInt(2, studentId);
                    mealOwnerStatement.executeUpdate();

                    // Insert student meal bookings into meal_booking table
                    String[] meals = {"breakfast", "lunch", "dinner"};
                    PreparedStatement mealBookingStatement = connection.prepareStatement(
                            "INSERT INTO meal_booking (meal_id, time, booked) VALUES (?, ?, 1)"
                    );
                    for (String meal : meals) {
                        mealBookingStatement.setString(1, mealOwnerId);
                        mealBookingStatement.setString(2, meal);
                        mealBookingStatement.executeUpdate();
                    }
                    
                    // Insert student requests into requests table
                    PreparedStatement requestStatement = connection.prepareStatement(
                            "INSERT INTO requests (student_id, meal_id, time) VALUES (?, ?, ?)"
                    );
                    for (String meal : meals) {
                        requestStatement.setInt(1, studentId);
                        requestStatement.setString(2, mealOwnerId);
                        requestStatement.setString(3, meal);
                        requestStatement.executeUpdate();
                    }
                    // If signup is successful, switch back to the login page
                    start(primaryStage);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database error
                    // You can show an error message to the user if signup fails
                }
            } else {
                // Show error message if passwords don't match
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match. Please re-enter your password.");
                alert.showAndWait();
                // Clear password and confirm password fields
                signUpPasswordField.clear();
                confirmPasswordField.clear();
            }
        });

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setOnAction(event -> {
            // Switch back to the login page
            start(primaryStage);
        });

        GridPane signUpLayout = new GridPane();
        signUpLayout.setPadding(new Insets(20));
        signUpLayout.setHgap(10);
        signUpLayout.setVgap(10);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(100));
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(200));
        signUpLayout.add(headingLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headingLabel, HPos.CENTER);
        signUpLayout.add(studentIdLabel, 0, 1);
        signUpLayout.add(studentIdField, 1, 1);
        signUpLayout.add(nameLabel, 0, 2);
        signUpLayout.add(nameField, 1, 2);
        signUpLayout.add(hostelLabel, 0, 3);
        signUpLayout.add(hostelComboBox, 1, 3);
        signUpLayout.add(roomLabel, 0, 4);
        signUpLayout.add(roomField, 1, 4);
        signUpLayout.add(messLabel, 0, 5);
        signUpLayout.add(messComboBox, 1, 5);
        signUpLayout.add(passwordLabel, 0, 6);
        signUpLayout.add(signUpPasswordField, 1, 6);
        signUpLayout.add(confirmPasswordLabel, 0, 7);
        signUpLayout.add(confirmPasswordField, 1, 7);
        signUpLayout.add(signUpButton, 0, 8, 2, 1);
        signUpLayout.add(backToLoginButton, 0, 9, 2, 1);

        // Set window size
        setWindowSize(primaryStage);

        Scene scene = new Scene(signUpLayout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
    }



    private void setWindowSize(Stage stage) {
        if (stage.isMaximized()) {
            // If window is maximized, set default size to screen dimensions
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
            defaultWidth = screenWidth;
            defaultHeight = screenHeight;
        } else {
            // If window is not maximized, set default size to 300 width and 250 height
            defaultWidth = 500;
            defaultHeight = 500;
        }
    }
    
    private boolean checkLogin(int studentId, String password) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM student WHERE student_id = ? AND password = ?"
            );
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if there is a match
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any exception
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
