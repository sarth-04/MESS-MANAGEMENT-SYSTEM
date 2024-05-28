import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDashboard {

    private int managerId;
    private int messId;
    private int messCapacity;
    private String messName;

    public ManagerDashboard(int managerId) {
        this.managerId = managerId;
        retrieveMessIdFromDatabase();
    }

    private void retrieveMessIdFromDatabase() {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            String query = "select mess_id, mess.capacity, mess_name from manager natural join mess where manager_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, managerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                messId = resultSet.getInt("mess_id");
                messCapacity = resultSet.getInt("mess.capacity");
                messName = resultSet.getString("mess_name");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Scene createScene() {
        // Create a VBox for the left column
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(10);
        leftColumn.setPadding(new Insets(20, 0, 20, 0)); // Adding padding at the bottom

        // Create a VBox for the top buttons
        VBox topButtonsBox = new VBox();
        topButtonsBox.setSpacing(10);
        topButtonsBox.setAlignment(Pos.TOP_LEFT); // Aligning top buttons to the top left

        // Create clickable options for dashboard, leave application, feedback, and request
        Button dashboardButton = new Button("Dashboard");
//        Button requestButton = new Button("Request");
        Button leftoverButton = new Button("Leftovers");

        // Add buttons to the top buttons VBox
        topButtonsBox.getChildren().addAll(dashboardButton, leftoverButton);

        // Sign-out button
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            // Handle sign-out button click
            // Redirect to the login page
            LoginSignUpApp loginSignUpApp = new LoginSignUpApp();
            Stage stage = (Stage) signOutButton.getScene().getWindow();
            loginSignUpApp.start(stage);
        });

        // Add the top buttons VBox and sign-out button to the left column VBox
        leftColumn.getChildren().addAll(topButtonsBox, signOutButton);


        // Create a VBox for the right content
        VBox rightContent = new VBox();
        rightContent.setAlignment(Pos.CENTER);

        // Set up layout
        BorderPane root = new BorderPane();
        root.setLeft(leftColumn);
        root.setCenter(rightContent);


        // Set default selection to Dashboard
        dashboardButton.fire();

        // Dashboard heading
        Label dashboardHeading = new Label("Manager Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the heading
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));


        // Mess information
        HBox managerInfoBox = new HBox();
        managerInfoBox.setSpacing(10);

        Label messLabel = new Label("Mess:");
        Label messValueLabel = new Label(messName);
        messValueLabel.setStyle("-fx-font-weight: bold;");

        managerInfoBox.getChildren().addAll(messLabel, messValueLabel);


        managerInfoBox.setAlignment(Pos.CENTER_LEFT);

        // Add space below the mess information
        VBox.setMargin(managerInfoBox, new Insets(0, 0, 20, 0));

     // Initialize TableView for Food Package Requests
     TableView<FoodPackageRequest> FPReqTable = new TableView<>();
     configureFPReqTable(FPReqTable);

     // Initialize TableView for Add On Requests
     TableView<ManagerAOReq> addOnTable = new TableView<>();
     configureAddOnTable(addOnTable);

     // Set up table panes
     TitledPane table1Pane = new TitledPane("Food Package Requests", FPReqTable);
     TitledPane table2Pane = new TitledPane("Add On Requests", addOnTable);

     SplitPane splitPane = new SplitPane();
     splitPane.getItems().addAll(table1Pane, table2Pane);
     splitPane.setDividerPositions(0.5);

     table1Pane.setAlignment(Pos.TOP_LEFT);
     table2Pane.setAlignment(Pos.TOP_LEFT);

     Button acknowledgeBreakfastButton = new Button("Acknowledge Breakfast");
     Button acknowledgeLunchButton = new Button("Acknowledge Lunch");
     Button acknowledgeDinnerButton = new Button("Acknowledge Dinner");

     // Add action event handlers for each button
     acknowledgeBreakfastButton.setOnAction(event -> {
         executeAcknowledgementQuery("breakfast");
         refreshTables(FPReqTable, addOnTable);
     });

     acknowledgeLunchButton.setOnAction(event -> {
         executeAcknowledgementQuery("lunch");
         refreshTables(FPReqTable, addOnTable);
     });

     acknowledgeDinnerButton.setOnAction(event -> {
         executeAcknowledgementQuery("dinner");
         refreshTables(FPReqTable, addOnTable);
     });

     // Add buttons below the tables
     HBox acknowledgeButtonsBox = new HBox();
     acknowledgeButtonsBox.setSpacing(10);
     acknowledgeButtonsBox.setAlignment(Pos.CENTER);

     acknowledgeButtonsBox.getChildren().addAll(
             acknowledgeBreakfastButton,
             acknowledgeLunchButton,
             acknowledgeDinnerButton
     );
     
     dashboardButton.setOnAction(event -> {
    	    rightContent.getChildren().clear();
    	    rightContent.getChildren().addAll(
    	        dashboardHeading, 
    	        managerInfoBox, 
    	        splitPane, 
    	        acknowledgeButtonsBox
    	    );
    	});

        // Create VBox to hold the form elements
//        VBox requestList = new VBox();
//        requestList.setSpacing(10);
//        requestList.setAlignment(Pos.CENTER);
//
//        // Create a heading label for the form
//        Label headingLabel = new Label("Food Requests");
//        headingLabel.setStyle("-fx-font-weight: bold;");
//
//        // Create TableView for the meal booking
//        TableView<ManagerReq> requestTable = new TableView<>();
//
//        // Create TableColumn for meal
//        TableColumn<ManagerReq, String> student3Column = new TableColumn<>("STUDENT");
//        student3Column.setCellValueFactory(new PropertyValueFactory<>("student"));
//
//        // Create TableColumn for time
//        TableColumn<ManagerReq, String> id3Column = new TableColumn<>("ID");
//        id3Column.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        // Create TableColumn for time
//        TableColumn<ManagerReq, String> typeColumn = new TableColumn<>("TYPE");
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//
//        // Create TableColumn for time
//        TableColumn<ManagerReq, String> item2Column = new TableColumn<>("ITEM");
//        item2Column.setCellValueFactory(new PropertyValueFactory<>("item"));
//
//        TableColumn<ManagerReq, Button> status3Column = new TableColumn<>("STATUS");
//        status3Column.setCellValueFactory(new PropertyValueFactory<>("statusButton"));
//
//        // Add columns to the table
//        requestTable.getColumns().addAll(student3Column, id3Column, typeColumn, item2Column, status3Column);
//
//        // Add data to the table
//        requestTable.getItems().addAll(
//                new ManagerReq("Aman Dasgupta", "f20200023", "Food Package", "Lunch", new Button("PENDING")),
//                new ManagerReq("Shreya Singhal", "f20220016", "Add On", "Dal Makhani", new Button("DONE"))
//        );
//
//        // Add form elements to the formBox
//        requestList.getChildren().addAll(headingLabel, requestTable);
//
//        // Set padding for the formBox
//        requestList.setPadding(new Insets(20));
//
//
//        requestButton.setOnAction(event -> {
//            rightContent.getChildren().clear();
//            rightContent.getChildren().add(requestList);
//        });

        VBox leftoverForm = new VBox();
        leftoverForm.setSpacing(10);
        leftoverForm.setPadding(new Insets(20));

        VBox heading = new VBox();
        heading.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label heading2Label = new Label("Leftovers");
        heading2Label.setStyle("-fx-font-weight: bold;");

        heading.getChildren().add(heading2Label);

        VBox.setMargin(heading, new Insets(0, 0, 10, 0));


        Label radioLabel = new Label("Meal");

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton breakfastRadio = new RadioButton("Breakfast");
        breakfastRadio.setToggleGroup(toggleGroup);
        RadioButton lunchRadio = new RadioButton("Lunch");
        lunchRadio.setToggleGroup(toggleGroup);
        RadioButton dinnerRadio = new RadioButton("Dinner");
        dinnerRadio.setToggleGroup(toggleGroup);

        Label mealsLabel = new Label("Attendance Count : ");

        TextField numericalMeals = new TextField();
        numericalMeals.setMaxWidth(100);

        // Add an event handler to allow only numerical input
        numericalMeals.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numericalMeals.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        VBox.setMargin(numericalMeals, new Insets(0, 0, 5, 0));

        VBox button = new VBox();
        button.setAlignment(Pos.CENTER);
        
        // Fetch and display leftover tracking table
        TableView<LeftoverItem> leftoverTable = new TableView<>();
        TableColumn<LeftoverItem, String> mealTimeColumn = new TableColumn<>("Meal Time");
        mealTimeColumn.setCellValueFactory(new PropertyValueFactory<>("mealTime"));
        mealTimeColumn.setPrefWidth(150); // Set preferred width for the meal time column

        TableColumn<LeftoverItem, Integer> leftoverCountColumn = new TableColumn<>("Leftover Count");
        leftoverCountColumn.setCellValueFactory(new PropertyValueFactory<>("leftoverCount"));
        leftoverCountColumn.setPrefWidth(150); // Set preferred width for the leftover count column

        leftoverTable.getColumns().addAll(mealTimeColumn, leftoverCountColumn);
        leftoverTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ensure columns are resized to fit the table width
        // Submit button
        Button submitButton = new Button("UPDATE");

        // Add action event for the submit button
        submitButton.setOnAction(event -> {
            // Get the selected meal time
            String selectedMealTime = "";
            if (breakfastRadio.isSelected()) {
                selectedMealTime = "Breakfast";
            } else if (lunchRadio.isSelected()) {
                selectedMealTime = "Lunch";
            } else if (dinnerRadio.isSelected()) {
                selectedMealTime = "Dinner";
            }

            // Validate if a meal time is selected
            if (selectedMealTime.isEmpty()) {
                showAlert("Error", "Please select a meal time.");
                return;
            }

            // Get the attendance count entered by the manager
            int attendanceCount = 0;
            try {
                attendanceCount = Integer.parseInt(numericalMeals.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid attendance count.");
                return;
            }

            // Validate if attendance count exceeds capacity
            if (attendanceCount > messCapacity) {
                showAlert("Error", "Attendance count cannot exceed mess capacity.");
                return;
            }

            // Save the initial state
            int initialAttendanceCount = 0;
            int initialTotalPrepared = 0;
            int initialTotalLeftover = 0;

            try {
                // Start transaction
                Connection connection = THE_CONNECTION.getTheConnection();
                connection.setAutoCommit(false);

                // Fetch initial state
                String initialStateQuery = "SELECT total_attended, total_prepared, total_leftover FROM leftover WHERE time = ? AND mess_id = ?";
                PreparedStatement initialStateStatement = connection.prepareStatement(initialStateQuery);
                initialStateStatement.setString(1, selectedMealTime);
                initialStateStatement.setInt(2, messId);
                ResultSet initialStateResult = initialStateStatement.executeQuery();

                if (initialStateResult.next()) {
                    initialAttendanceCount = initialStateResult.getInt("total_attended");
                    initialTotalPrepared = initialStateResult.getInt("total_prepared");
                    initialTotalLeftover = initialStateResult.getInt("total_leftover");
                }

                // Update attendance count in total_attended column in leftover table
                String updateAttendanceQuery = "UPDATE leftover SET total_attended = ? WHERE time = ? AND mess_id = ?";
                PreparedStatement updateAttendanceStatement = connection.prepareStatement(updateAttendanceQuery);
                updateAttendanceStatement.setInt(1, attendanceCount);
                updateAttendanceStatement.setString(2, selectedMealTime);
                updateAttendanceStatement.setInt(3, messId);
                updateAttendanceStatement.executeUpdate();

                // Update total_prepared using provided query
                String updatePreparedQuery = "UPDATE leftover l " +
                        "JOIN (" +
                        "    SELECT m.mess_id, " +
                        "           (CASE WHEN COUNT(mb.booked) = 0 THEN m.capacity ELSE (m.capacity - COUNT(mb.booked)) END) AS remaining_capacity " +
                        "    FROM mess m " +
                        "    JOIN manager mn ON m.mess_id = mn.mess_id " +
                        "    JOIN student s ON m.mess_id = s.mess_id " +
                        "    JOIN meal_owner mo ON s.student_id = mo.student_id " +
                        "    LEFT JOIN meal_booking mb ON mo.meal_id = mb.meal_id AND mb.time = ? AND mb.booked = 0 " +
                        "    GROUP BY m.mess_id, m.capacity " +
                        ") AS subquery ON l.mess_id = subquery.mess_id " +
                        "SET l.total_prepared = subquery.remaining_capacity " +
                        "WHERE l.mess_id = ? AND l.time = ?";
                PreparedStatement updatePreparedStatement = connection.prepareStatement(updatePreparedQuery);
                updatePreparedStatement.setString(1, selectedMealTime);
                updatePreparedStatement.setInt(2, messId);
                updatePreparedStatement.setString(3, selectedMealTime);
                updatePreparedStatement.executeUpdate();

                // Update total_leftover as (total_prepared - total_attended)
                String updateLeftoverQuery = "UPDATE leftover SET total_leftover = (total_prepared - total_attended) " +
                        "WHERE time = ? AND mess_id = ?";
                PreparedStatement updateLeftoverStatement = connection.prepareStatement(updateLeftoverQuery);
                updateLeftoverStatement.setString(1, selectedMealTime);
                updateLeftoverStatement.setInt(2, messId);
                updateLeftoverStatement.executeUpdate();

                // Commit the transaction
                connection.commit();

                // Show success message
                showAlert("Success", "Attendance count and leftover count updated successfully.");

                // Close the connection
                connection.close();
                
                fetchAndDisplayLeftoverData(leftoverTable);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while updating the records. Rolling back changes.");
                // Rollback changes
                try {
                    Connection connection = THE_CONNECTION.getTheConnection();
                    connection.rollback();

                    // Restore initial state
                    String restoreQuery = "UPDATE leftover SET total_attended = ?, total_prepared = ?, total_leftover = ? WHERE time = ? AND mess_id = ?";
                    PreparedStatement restoreStatement = connection.prepareStatement(restoreQuery);
                    restoreStatement.setInt(1, initialAttendanceCount);
                    restoreStatement.setInt(2, initialTotalPrepared);
                    restoreStatement.setInt(3, initialTotalLeftover);
                    restoreStatement.setString(4, selectedMealTime);
                    restoreStatement.setInt(5, messId);
                    restoreStatement.executeUpdate();

                    // Close the connection
                    connection.close();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                    showAlert("Error", "Failed to rollback changes.");
                }
            }
        });


        button.getChildren().add(submitButton);

        // Add form elements to the formBox
        leftoverForm.getChildren().addAll(heading, radioLabel, breakfastRadio, lunchRadio, dinnerRadio, mealsLabel, numericalMeals, button);

        // Set padding for the formBox
        leftoverForm.setPadding(new Insets(20));

       

        fetchAndDisplayLeftoverData(leftoverTable);

        leftoverButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            VBox container = new VBox(leftoverForm, new ScrollPane(leftoverTable));
            container.setMaxHeight(3 * (leftoverTable.getFixedCellSize() + 1) + leftoverTable.getFixedCellSize()); // Set maximum height for three rows
            VBox.setVgrow(new ScrollPane(leftoverTable), Priority.ALWAYS); // Allow the table to grow vertically within the scroll pane
            rightContent.getChildren().add(container);
        });

        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }

    private void fetchAndDisplayLeftoverData(TableView<LeftoverItem> table) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            String query = "SELECT time, COALESCE(total_leftover, 0) AS total_leftover FROM leftover WHERE mess_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, messId);
            ResultSet resultSet = statement.executeQuery();
            
            table.getItems().clear();
            
            while (resultSet.next()) {
                String mealTime = resultSet.getString("time");
                int leftoverCount = resultSet.getInt("total_leftover");
                table.getItems().add(new LeftoverItem(mealTime, leftoverCount));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void fetchAndDisplayFoodPackageRequests(TableView<FoodPackageRequest> table) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            String query = "SELECT r.student_id, s.name, r.time " +
                    "FROM requests r " +
                    "JOIN student s ON r.student_id = s.student_id " +
                    "WHERE s.mess_id = ? AND r.food_package = 1";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, messId);
            ResultSet resultSet = statement.executeQuery();
            
            // Clear existing items in the table
            table.getItems().clear();
            
            // Populate the table with the retrieved data
            while (resultSet.next()) {
                String studentId = resultSet.getString("student_id");
                String name = resultSet.getString("name");
                String meal = resultSet.getString("time");
                table.getItems().add(new FoodPackageRequest(studentId, name, meal));
            }
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void configureFPReqTable(TableView<FoodPackageRequest> table) {
        TableColumn<FoodPackageRequest, String> idColumn = new TableColumn<>("STUDENT ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<FoodPackageRequest, String> nameColumn = new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<FoodPackageRequest, String> mealColumn = new TableColumn<>("MEAL");
        mealColumn.setCellValueFactory(new PropertyValueFactory<>("meal"));

        table.getColumns().addAll(idColumn, nameColumn, mealColumn);
        
        fetchAndDisplayFoodPackageRequests(table);
    }

    // Method to configure the Add On Requests table
    private void fetchAndDisplayAddOnRequests(TableView<ManagerAOReq> table) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            String query = "SELECT r.student_id, s.name, r.time, r.addon " +
                           "FROM requests r " +
                           "JOIN student s ON r.student_id = s.student_id " +
                           "WHERE s.mess_id = ? AND r.addon != 0";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, messId);
            ResultSet resultSet = statement.executeQuery();
            
            // Clear existing items in the table
            table.getItems().clear();
            
            // Populate the table with the retrieved data
            while (resultSet.next()) {
                String studentId = resultSet.getString("student_id");
                String name = resultSet.getString("name");
                String meal = resultSet.getString("time");
                String addon = getAddonName(resultSet.getInt("addon")); // Get addon name from addon code
                table.getItems().add(new ManagerAOReq(studentId, name, meal, addon));
            }
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getAddonName(int addonCode) {
        switch (addonCode) {
            case 1:
                return "Sabudana Khichdi";
            case 2:
                return "Dal Khichdi";
            case 3:
                return "Salad";
            default:
                return "";
        }
    }

    private void configureAddOnTable(TableView<ManagerAOReq> table) {
        // Configure table columns
        TableColumn<ManagerAOReq, String> idColumn = new TableColumn<>("STUDENT ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ManagerAOReq, String> nameColumn = new TableColumn<>("NAME");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ManagerAOReq, String> mealColumn = new TableColumn<>("MEAL");
        mealColumn.setCellValueFactory(new PropertyValueFactory<>("meal"));

        TableColumn<ManagerAOReq, String> addonColumn = new TableColumn<>("ADD-ON");
        addonColumn.setCellValueFactory(new PropertyValueFactory<>("addon"));

        // Add columns to the table
        table.getColumns().addAll(idColumn, nameColumn, mealColumn, addonColumn);

        // Fetch and display add-on requests
        fetchAndDisplayAddOnRequests(table);
    }
    
    private void executeAcknowledgementQuery(String mealTime) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            String query = "UPDATE requests r " +
                           "JOIN (" +
                           "    SELECT r1.meal_id " +
                           "    FROM requests r1 " +
                           "    JOIN meal_owner mo ON r1.meal_id = mo.meal_id " +
                           "    JOIN student s ON mo.student_id = s.student_id " +
                           "    WHERE s.mess_id = ? " +
                           "    AND r1.time = ?" +
                           ") AS subquery ON r.meal_id = subquery.meal_id " +
                           "SET r.food_package = CASE WHEN r.time = ? THEN 0 ELSE r.food_package END, " +
                           "    r.addon = CASE WHEN r.time = ? THEN 0 ELSE r.addon END";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, messId);
            statement.setString(2, mealTime);
            statement.setString(3, mealTime);
            statement.setString(4, mealTime);
            statement.executeUpdate();
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTables(TableView<FoodPackageRequest> foodPackageTable, TableView<ManagerAOReq> addOnTable) {
    	foodPackageTable.getItems().clear();
        addOnTable.getItems().clear();
    	fetchAndDisplayFoodPackageRequests(foodPackageTable);
        fetchAndDisplayAddOnRequests(addOnTable);
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    

    public static class LeftoverItem {
        private String mealTime;
        private int leftoverCount;

        public LeftoverItem(String mealTime, int leftoverCount) {
            this.mealTime = mealTime;
            this.leftoverCount = leftoverCount;
        }

        public String getMealTime() {
            return mealTime;
        }

        public int getLeftoverCount() {
            return leftoverCount;
        }
    }
    public static class FoodPackageRequest {
        private String studentId;
        private String name;
        private String meal;

        public FoodPackageRequest(String studentId, String name, String meal) {
            this.studentId = studentId;
            this.name = name;
            this.meal = meal;
        }

        // Add getters for the fields
        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public String getMeal() {
            return meal;
        }
    }
}
