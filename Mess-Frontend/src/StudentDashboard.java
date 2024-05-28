import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;

public class StudentDashboard {
	private int studentId; // Student ID field
    private String name;
    private String hostel;
    private int roomNo;
    private String mess;
    private int mealId;

    public StudentDashboard(int studentId) {
        this.studentId = studentId;
        fetchStudentCredentials();
    }
    
    public Scene createScene() {
        // Create left column
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(10);
        leftColumn.setPadding(new Insets(20, 0, 20, 0));

        // Create top buttons box
        VBox topButtonsBox = new VBox();
        topButtonsBox.setSpacing(10);
        topButtonsBox.setAlignment(Pos.TOP_LEFT);

        // Buttons
        Button dashboardButton = new Button("Dashboard");
        Button leaveApplicationButton = new Button("Leave Application");
        Button feedbackButton = new Button("Feedback");
        Button requestButton = new Button("Request");

        // Add buttons to top buttons box
        topButtonsBox.getChildren().addAll(dashboardButton, leaveApplicationButton, feedbackButton, requestButton);

        // Sign-out button
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            LoginSignUpApp loginSignUpApp = new LoginSignUpApp();
            Stage stage = (Stage) signOutButton.getScene().getWindow();
            loginSignUpApp.start(stage);
        });

        // Add top buttons box and sign-out button to left column
        leftColumn.getChildren().addAll(topButtonsBox, signOutButton);

        // Right content
        VBox rightContent = new VBox();
        rightContent.setAlignment(Pos.CENTER);

        // Root layout
        BorderPane root = new BorderPane();
        root.setLeft(leftColumn);
        root.setCenter(rightContent);

        // Dashboard heading
        Label dashboardHeading = new Label("Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 10, 0));

        // Student credentials
        Label studentInfoLabel = new Label("Student ID: " + studentId + "\n" +
                                            "Name: " + name + "\n" +
                                            "Hostel: " + hostel + "\n" +
                                            "Room No.: " + roomNo + "\n" +
                                            "Mess: " + mess + "\n" +
                                            "Meal ID: " + mealId);
        studentInfoLabel.setStyle("-fx-font-weight: bold;");
        VBox.setMargin(studentInfoLabel, new Insets(0, 0, 20, 0));
        // Mess information
      
        VBox messInfoBox = new VBox(studentInfoLabel);
        messInfoBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(messInfoBox, new Insets(0, 0, 20, 0));

        // Create TableView for meal booking
     // Inside the section where mealBookingTable is created
     // Create TableView for meal booking
     TableView<MealEntry> mealBookingTable = new TableView<>();
     TableColumn<MealEntry, String> mealColumn = new TableColumn<>("MEAL");
     mealColumn.setCellValueFactory(new PropertyValueFactory<>("meal"));

     TableColumn<MealEntry, String> timeColumn = new TableColumn<>("TIME");
     timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

     TableColumn<MealEntry, Button> optColumn = new TableColumn<>("OPT");
     optColumn.setCellValueFactory(new PropertyValueFactory<>("optButton"));

     mealBookingTable.getColumns().addAll(mealColumn, timeColumn, optColumn);

     // Add meal entries dynamically based on database records
     ObservableList<MealEntry> mealEntries = FXCollections.observableArrayList();
     try {
         Connection connection = THE_CONNECTION.getTheConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT time, booked FROM meal_booking WHERE meal_id = ?"
         );
         preparedStatement.setInt(1, mealId);
         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
        	 String meal = resultSet.getString("time");

        	// Setting time based on meal type
        	String time;
        	switch (meal) {
        	    case "breakfast":
        	        time = "7:30am - 9:30am";
        	        break;
        	    case "lunch":
        	        time = "12pm - 2pm";
        	        break;
        	    case "dinner":
        	        time = "7:30pm - 9:30pm";
        	        break;
        	    default:
        	        time = ""; // Set default value if meal type is unknown
        	}

//        	int mealId = resultSet.getInt("meal_id");
        	Button optButton = new Button(); // Empty button for now, will be updated later
        	mealEntries.add(new MealEntry(meal, time, optButton, mealId));
         }
     } catch (SQLException e) {
         e.printStackTrace();
         showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching meal entries.");
     }

     mealBookingTable.setItems(mealEntries);


        // Create TableView for leave application
        TableView<LeaveApplication> leaveAppTable = new TableView<>();
        TableColumn<LeaveApplication, String> datesColumn = new TableColumn<>("DATES");
        datesColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));

        TableColumn<LeaveApplication, String> commentsColumn = new TableColumn<>("COMMENT");
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        TableColumn<LeaveApplication, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

//        leaveAppTable.getColumns().addAll(datesColumn, commentsColumn, statusColumn);
        
        leaveAppTable.getColumns().add(datesColumn);
        leaveAppTable.getColumns().add(commentsColumn);
        leaveAppTable.getColumns().add(statusColumn);

        
        // Add tables to titled panes
        TitledPane table1Pane = new TitledPane("Meal Booking", mealBookingTable);
        TitledPane table2Pane = new TitledPane("Leave Application", leaveAppTable);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(table1Pane, table2Pane);
        splitPane.setDividerPositions(0.5);

        table1Pane.setAlignment(Pos.TOP_LEFT);
        table2Pane.setAlignment(Pos.TOP_LEFT);

        // Handle dashboard button action
        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(dashboardHeading, messInfoBox, splitPane);
            
            leaveAppTable.getItems().clear();
            try {
                // Fetch leave applications
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT start_date, end_date, message, status FROM leave_apply WHERE student_id = ?"
                );
                preparedStatement.setInt(1, studentId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    java.sql.Date startDate = resultSet.getDate("start_date");
                    java.sql.Date endDate = resultSet.getDate("end_date");
                    String dates = startDate.toString() + " - " + endDate.toString();
                    String comments = resultSet.getString("message");
                    String status = resultSet.getString("status");

                    leaveAppTable.getItems().add(new LeaveApplication(dates, comments, status));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching leave applications.");
            }
        });


        // Create VBox to hold the form elements
        VBox leaveForm = new VBox();
        leaveForm.setSpacing(10);
        leaveForm.setAlignment(Pos.CENTER);

        Label headingLabel = new Label("Leave Application");
        headingLabel.setStyle("-fx-font-weight: bold;");

        // Create a VBox for the date inputs
        VBox dateInputBox = new VBox();
        dateInputBox.setSpacing(10);

        // Left side - Start Date input
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker(); // Use DatePicker for input

        // Right side - End Date input
        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker(); // Use DatePicker for input

        // Add nodes to the date input VBox
        dateInputBox.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker);

        // Create a label and text area for comments input
        Label commentsLabel = new Label("Comments:");
        TextArea commentsTextArea = new TextArea();
        commentsTextArea.setPrefWidth(300); // Set preferred width
        commentsTextArea.setPrefHeight(100); // Set preferred height

        // Create a submit button
        Button submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.CENTER);

        // Add form elements to the form VBox
        leaveForm.getChildren().addAll(headingLabel, dateInputBox, commentsLabel, commentsTextArea, submitButton);

        // Set padding for the form VBox
        leaveForm.setPadding(new Insets(20));

        leaveApplicationButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(leaveForm);
        });

        submitButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String comments = commentsTextArea.getText();

            // Check for null values in start date, end date, and comments
            if (startDate == null || endDate == null || comments.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid values for start date, end date, and comments.");
                return; // Exit the method
            }

            // Check for valid dates (start date should be before end date and not in the past)
            if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid dates (start date should be before end date and not in the past).");
                return; // Exit the method
            }

            try {
                // Prepare the INSERT statement
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO leave_apply (student_id, start_date, end_date, status, message) VALUES (?, ?, ?, ?, ?)"
                );

                // Set parameters for the INSERT statement
                preparedStatement.setInt(1, studentId); // Use the stored student ID
                preparedStatement.setDate(2, java.sql.Date.valueOf(startDate));
                preparedStatement.setDate(3, java.sql.Date.valueOf(endDate));
                preparedStatement.setInt(4, 1); // Default status (e.g., Pending)
                preparedStatement.setString(5, comments);

                // Execute the INSERT statement
                preparedStatement.executeUpdate();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Leave application submitted successfully.");

                // Clear all fields
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                commentsTextArea.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
                showAlert(Alert.AlertType.ERROR, "Error", "Error while submitting leave application. Please try again later.");
            }
        });

        VBox feedbackForm = new VBox();
        feedbackForm.setAlignment(Pos.TOP_CENTER);
        feedbackForm.setSpacing(10);
        feedbackForm.setPadding(new Insets(20));

        // Feedback heading
        Label feedbackHeading = new Label("Feedback");
        feedbackHeading.setStyle("-fx-font-weight: bold;");
        feedbackHeading.setAlignment(Pos.CENTER);

        // Add space below the heading
        VBox.setMargin(feedbackHeading, new Insets(0, 0, 10, 0));

        // Provide Feedback label
        Label provideFeedbackLabel = new Label("Provide Feedback:");

        // Text box for feedback input
        TextArea feedbackInput = new TextArea();
        feedbackInput.setWrapText(true);
        feedbackInput.setPrefWidth(300); // Adjust width as needed
        feedbackInput.setPrefHeight(100); // Adjust height as needed

        // Submit button
        Button submitFBButton = new Button("Submit");
        submitFBButton.setAlignment(Pos.CENTER);

        // Add nodes to feedback form
        feedbackForm.getChildren().addAll(feedbackHeading, provideFeedbackLabel, feedbackInput, submitFBButton);

        TableView<FeedbackEntry> feedbackTable = new TableView<>();
        TableColumn<FeedbackEntry, Integer> feedbackIdColumn = new TableColumn<>("Feedback ID");
        feedbackIdColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));

        TableColumn<FeedbackEntry, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<FeedbackEntry, String> remarkColumn = new TableColumn<>("Remark");
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remark"));
        
        
        feedbackTable.getColumns().addAll(feedbackIdColumn, messageColumn, remarkColumn);

        feedbackButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(feedbackForm, feedbackTable);
            
            feedbackTable.getItems().clear();
         // Fetch data and add it to the table
            try {
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT feedback.feedback_id, feedback.message, responds.remark " +
                                "FROM feedback " +
                                "LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id " +
                                "WHERE feedback.student_id = ?"
                );
                preparedStatement.setInt(1, studentId); // Bind the student ID parameter
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int feedbackId = resultSet.getInt("feedback.feedback_id");
                    String message = resultSet.getString("feedback.message");
                    String remark = resultSet.getString("responds.remark");

                    feedbackTable.getItems().add(new FeedbackEntry(feedbackId, message, remark));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching feedback. Please try again later.");
            }
        });

        // Action event for feedback submission
        submitFBButton.setOnAction(event -> {
            // Retrieve feedback message
            String feedbackMessage = feedbackInput.getText();

            // Check if the feedback message is empty
            if (feedbackMessage.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter your feedback message.");
                return; // Exit the method
            }

            try {
                // Prepare the INSERT statement
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO feedback (student_id, message) VALUES (?, ?)"
                );

                // Set parameters for the INSERT statement
                preparedStatement.setInt(1, studentId); // Use the stored student ID
                preparedStatement.setString(2, feedbackMessage);

                // Execute the INSERT statement
                preparedStatement.executeUpdate();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback submitted successfully.");

                // Clear the feedback input
                feedbackInput.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
                showAlert(Alert.AlertType.ERROR, "Error", "Error while submitting feedback. Please try again later.");
            }
        });

        // VBox for request form
        VBox requestForm = new VBox();
        requestForm.setAlignment(Pos.TOP_CENTER);
        requestForm.setSpacing(10);
        requestForm.setPadding(new Insets(20));

        // Feedback heading
        Label requestHeading = new Label("Food Requests");
        requestHeading.setStyle("-fx-font-weight: bold;");
        requestHeading.setAlignment(Pos.CENTER);

        // Left pane for food package requests
        VBox foodPackagePane = new VBox();
        foodPackagePane.setSpacing(10);

        // Food package heading
        Label foodPackageHeading = new Label("Food Package");
        foodPackageHeading.setStyle("-fx-font-weight: bold;");
        foodPackagePane.getChildren().add(foodPackageHeading);

        // Checkboxes for food package options
        CheckBox breakfastCheckBox = new CheckBox("Breakfast");
        CheckBox lunchCheckBox = new CheckBox("Lunch");
        CheckBox dinnerCheckBox = new CheckBox("Dinner");

        // Button for ordering food package
        Button orderFoodPackageButton = new Button("Order");

        // Add nodes to food package pane
        foodPackagePane.getChildren().addAll(breakfastCheckBox, lunchCheckBox, dinnerCheckBox, orderFoodPackageButton);
        
        orderFoodPackageButton.setOnAction(event -> {
//            String studentId = ""; // Get the student's ID

            // Initialize a StringBuilder to construct the SQL query
            StringBuilder updateQuery = new StringBuilder("UPDATE requests SET food_package = CASE ");

            try {
                Connection connection = THE_CONNECTION.getTheConnection();

                // Count the number of selected checkboxes
                int selectedCount = 0;
                if (breakfastCheckBox.isSelected()) selectedCount++;
                if (lunchCheckBox.isSelected()) selectedCount++;
                if (dinnerCheckBox.isSelected()) selectedCount++;

                // Check if at least one checkbox is selected
                if (selectedCount == 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please select at least one meal option.");
                    return;
                }

                // Checkboxes for food package options
                CheckBox[] checkboxes = {breakfastCheckBox, lunchCheckBox, dinnerCheckBox};
                String[] mealLabels = {"breakfast", "lunch", "dinner"};

                // Iterate over the checkboxes
                for (int i = 0; i < checkboxes.length; i++) {
                    if (checkboxes[i].isSelected()) {
                        // Add a CASE statement for selected meal option
                        updateQuery.append("WHEN student_id = ? AND meal_id = ? AND time = ? THEN 1 ");
                    } else {
                        // Add a CASE statement to set food_package to NULL if meal option is not selected
                        updateQuery.append("WHEN student_id = ? AND meal_id = ? AND time = ? THEN 0 ");
                    }
                }

                // Close the CASE statement and set the ELSE clause to not alter the food_package
                updateQuery.append("ELSE food_package END");

                // Prepare the PreparedStatement with the fully constructed SQL query
                PreparedStatement statement = connection.prepareStatement(updateQuery.toString());

                // Initialize parameters index
                int parameterIndex = 1;

                // Set the parameters for the PreparedStatement
                for (int i = 0; i < checkboxes.length; i++) {
                    if (checkboxes[i].isSelected() || !checkboxes[i].isSelected()) {
                        statement.setInt(parameterIndex++, studentId);
                        statement.setInt(parameterIndex++, mealId);
                        statement.setString(parameterIndex++, mealLabels[i]);
                    }
                }

                // Execute the constructed SQL query
                int rowsAffected = statement.executeUpdate();

                // Check if any rows were affected
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Food package ordered successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to order food package.");
                }

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
            }
        });



        // Right pane for add-ons requests
        VBox addOnsPane = new VBox();
        addOnsPane.setSpacing(10);

        // Meal time selection heading
        Label mealTimeHeading = new Label("Select Meal Time");
        mealTimeHeading.setStyle("-fx-font-weight: bold;");
        addOnsPane.getChildren().add(mealTimeHeading);

        // Radio buttons for selecting meal time
        ToggleGroup mealTimeToggleGroup = new ToggleGroup();
        RadioButton breakfastRadioButton = new RadioButton("Breakfast");
        breakfastRadioButton.setToggleGroup(mealTimeToggleGroup);
        RadioButton lunchRadioButton = new RadioButton("Lunch");
        lunchRadioButton.setToggleGroup(mealTimeToggleGroup);
        RadioButton dinnerRadioButton = new RadioButton("Dinner");
        dinnerRadioButton.setToggleGroup(mealTimeToggleGroup);

        // Add meal time components to the pane
        addOnsPane.getChildren().addAll(breakfastRadioButton, lunchRadioButton, dinnerRadioButton);

        // Add-ons heading
        Label addOnsHeading = new Label("Add Ons");
        addOnsHeading.setStyle("-fx-font-weight: bold;");
        addOnsPane.getChildren().add(addOnsHeading);

        // Radio buttons for selecting addon options
        ToggleGroup addonToggleGroup = new ToggleGroup();
        RadioButton sabudanaKhichdiRadioButton = new RadioButton("Sabudana Khichdi");
        sabudanaKhichdiRadioButton.setToggleGroup(addonToggleGroup);
        RadioButton dalKhichdiRadioButton = new RadioButton("Dal Khichdi");
        dalKhichdiRadioButton.setToggleGroup(addonToggleGroup);
        RadioButton saladRadioButton = new RadioButton("Salad");
        saladRadioButton.setToggleGroup(addonToggleGroup);

        // Add addon components to the pane
        addOnsPane.getChildren().addAll(sabudanaKhichdiRadioButton, dalKhichdiRadioButton, saladRadioButton);

        // Button for ordering add-ons
        Button orderAddOnsButton = new Button("Order");
        addOnsPane.getChildren().add(orderAddOnsButton);

        // Action event for ordering add-ons
        orderAddOnsButton.setOnAction(event -> {
            // Get the selected meal time
            String selectedTime = "";
            if (breakfastRadioButton.isSelected()) {
                selectedTime = "breakfast";
            } else if (lunchRadioButton.isSelected()) {
                selectedTime = "lunch";
            } else if (dinnerRadioButton.isSelected()) {
                selectedTime = "dinner";
            }

            // Get the selected addon option
            int selectedAddon = 0;
            if (sabudanaKhichdiRadioButton.isSelected()) {
                selectedAddon = 1;
            } else if (dalKhichdiRadioButton.isSelected()) {
                selectedAddon = 2;
            } else if (saladRadioButton.isSelected()) {
                selectedAddon = 3;
            }

            try {
                // Prepare the INSERT statement
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE requests SET addon = ? WHERE student_id = ? AND meal_id = ? AND time = ?"
                );

                // Set parameters for the UPDATE statement
                preparedStatement.setInt(1, selectedAddon);
                preparedStatement.setInt(2, studentId); // Use the stored student ID
                preparedStatement.setInt(3, mealId); // Use the stored meal ID
                preparedStatement.setString(4, selectedTime);
                preparedStatement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Addon request submitted successfully.");

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error while submitting addon request. Please try again later.");
            }
        });

     // Add the food package and add-ons panes to the request form
        requestForm.getChildren().addAll(foodPackagePane, addOnsPane);

        // Handle request button action
        requestButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(requestForm);
        });

        // Initially, only display the left column
        rightContent.getChildren().add(dashboardHeading);
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }

    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static class LeaveApplication {
        private final String dates;
        private final String comments;
        private final String status;

        public LeaveApplication(String dates, String comments, String status) {
            this.dates = dates;
            this.comments = comments;
            this.status = status;
        }

        public String getDates() {
            return dates;
        }

        public String getComments() {
            return comments;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class MealEntry {
        private final String meal;
        private final String time;
        private final Button optButton;
        private final int mealId; // Added mealId

        public MealEntry(String meal, String time, Button optButton, int mealId) { // Updated constructor
            this.meal = meal;
            this.time = time;
            this.optButton = optButton;
            this.mealId = mealId; // Set mealId
            updateButtonText(); // Set initial button text based on database value
            this.optButton.setOnAction(event -> toggleBooking());
        }

        public String getMeal() {
            return meal;
        }

        public String getTime() {
            return time;
        }

        public Button getOptButton() {
            return optButton;
        }

        private void toggleBooking() {
            String buttonText = optButton.getText();
            String newButtonText = buttonText.equals("OPT") ? "OPT OUT" : "OPT";
            optButton.setText(newButtonText);
            // Update database here based on meal ID and time
            updateMealBooking(mealId, meal, newButtonText.equals("OPT OUT") ? 1 : 0);
        }

        private void updateButtonText() {
            try {
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT booked FROM meal_booking WHERE meal_id = ? AND time = ?"
                );
                preparedStatement.setInt(1, mealId);
                preparedStatement.setString(2, meal);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int booked = resultSet.getInt("booked");
                    optButton.setText(booked == 1 ? "OPT OUT" : "OPT");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching meal booking status.");
            }
        }

        private void updateMealBooking(int mealId, String time, int booked) {
            try {
                Connection connection = THE_CONNECTION.getTheConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE meal_booking SET booked = ? WHERE meal_id = ? AND time = ?"
                );
                preparedStatement.setInt(1, booked);
                preparedStatement.setInt(2, mealId);
                preparedStatement.setString(3, time);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error while updating meal booking.");
            }
        }

        private void showAlert(Alert.AlertType alertType, String title, String content) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
    }

    public static class FeedbackEntry {
        private final int feedbackId;
        private final String message;
        private final String remark;

        public FeedbackEntry(int feedbackId, String message, String remark) {
            this.feedbackId = feedbackId;
            this.message = message;
            this.remark = remark;
        }

        public int getFeedbackId() {
            return feedbackId;
        }

        public String getMessage() {
            return message;
        }

        public String getRemark() {
            return (remark != null) ? remark : "Remark Pending";
        }
    }
    private void fetchStudentCredentials() {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT s.name, s.hostel, s.room, m.mess_name, mo.meal_id " +
                            "FROM student s " +
                            "JOIN mess m ON s.mess_id = m.mess_id " +
                            "JOIN meal_owner mo ON s.student_id = mo.student_id " +
                            "WHERE s.student_id = ?"
            );
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString("name");
                hostel = resultSet.getString("hostel");
                roomNo = resultSet.getInt("room");
                mess = resultSet.getString("mess_name");
                mealId = resultSet.getInt("meal_id");
            } else {
                // Show error message if student details not found
                showAlert(Alert.AlertType.ERROR, "Error", "Student details not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching student details.");
        }
    }
    
}

