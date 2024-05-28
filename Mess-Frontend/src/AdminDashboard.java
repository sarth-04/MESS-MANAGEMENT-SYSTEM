import java.sql.*;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDashboard {

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
        Button leaveApplicationButton = new Button("Leave Application");
        Button feedbackButton = new Button("Feedback");

        // Add buttons to the top buttons VBox
        topButtonsBox.getChildren().addAll(dashboardButton, leaveApplicationButton, feedbackButton);

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
        Label dashboardHeading = new Label("Admin Dashboard");
        dashboardHeading.setStyle("-fx-font-weight: bold;");

        // Add space below the mess information
        VBox.setMargin(dashboardHeading, new Insets(0, 0, 20, 0));

        // Create TableView for the leave application
        TableView<AdminLeave> adminLeaveTable = new TableView<>();

        // Create TableColumn for ID
        TableColumn<AdminLeave, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Create TableColumn for student
        TableColumn<AdminLeave, String> studentColumn = new TableColumn<>("STUDENT");
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));

        // Create TableColumn for dates
        TableColumn<AdminLeave, String> datesColumn = new TableColumn<>("DATES");
        datesColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));

        // Create TableColumn for comment
        TableColumn<AdminLeave, String> commentColumn = new TableColumn<>("COMMENT");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Add columns to the table
        adminLeaveTable.getColumns().addAll(idColumn, studentColumn, datesColumn, commentColumn);

        // Fetch data and add it to the table
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT student.student_id, student.name, leave_apply.start_date, leave_apply.end_date, leave_apply.message FROM leave_apply NATURAL JOIN student"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("student.student_id");
                String studentName = resultSet.getString("student.name");
                Date startDate = resultSet.getDate("leave_apply.start_date");
                Date endDate = resultSet.getDate("leave_apply.end_date");
                String dates = startDate.toString() + " - " + endDate.toString();
                String comment = resultSet.getString("leave_apply.message");

                adminLeaveTable.getItems().add(new AdminLeave(id, studentName, dates, comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching leave applications. Please try again later.");
        }

        // Create TableView for the feedback
        TableView<AdminFB> adminFeedbackTable = new TableView<>();

        // Create TableColumn for ID
        TableColumn<AdminFB, Integer> feedbackIdColumn = new TableColumn<>("ID");
        feedbackIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Create TableColumn for student
        TableColumn<AdminFB, String> feedbackStudentColumn = new TableColumn<>("STUDENT");
        feedbackStudentColumn.setCellValueFactory(new PropertyValueFactory<>("student"));

        // Create TableColumn for comment
        TableColumn<AdminFB, String> feedbackCommentColumn = new TableColumn<>("COMMENT");
        feedbackCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // Add columns to the table
        adminFeedbackTable.getColumns().addAll(feedbackIdColumn, feedbackStudentColumn, feedbackCommentColumn);

        // Fetch data and add it to the table
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT student.student_id, student.name, feedback.message FROM feedback NATURAL JOIN student"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("student.student_id");
                String studentName = resultSet.getString("student.name");
                String comment = resultSet.getString("feedback.message");

                adminFeedbackTable.getItems().add(new AdminFB(id, studentName, comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching feedback. Please try again later.");
        }

        // Set up table panes
        TitledPane leaveApplicationPane = new TitledPane("Leave Applications", adminLeaveTable);
        TitledPane feedbackPane = new TitledPane("Feedback", adminFeedbackTable);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leaveApplicationPane, feedbackPane);
        splitPane.setDividerPositions(0.5);

        dashboardButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().addAll(dashboardHeading, splitPane);
        });

        
     // Create VBox to hold the form elements
        VBox leaveApproval = new VBox();
        leaveApproval.setSpacing(10);
        leaveApproval.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel = new Label("Leave Applications");
        headingLabel.setStyle("-fx-font-weight: bold;");

     // Create TableView for the meal booking
       

        leaveApplicationButton.setOnAction(event -> {
            // Clear previous content
            rightContent.getChildren().clear();
            // Fetch leave applications
            TableView<LeaveRecord> leaveTable = fetchLeaveApplications(rightContent);
            // Add leave table to the right content
            rightContent.getChildren().add(leaveTable);
        });
        
     // Create VBox to hold the form elements
     // Create VBox to hold the form elements
     // Create VBox to hold the form elements
        VBox feedback = new VBox();
        feedback.setSpacing(10);
        feedback.setAlignment(Pos.CENTER);

        // Create a heading label for the form
        Label headingLabel2 = new Label("Feedback");
        headingLabel2.setStyle("-fx-font-weight: bold;");

        // Create TableView for the feedback
        TableView<AdminFBShort> feedback2Table = new TableView<>();

        // Create TableColumn for feedback id
     // Create TableColumn for feedback id
     // Create TableColumn for feedback id
        TableColumn<AdminFBShort, Integer> feedbackIdColumn2 = new TableColumn<>("FEEDBACK ID");
        feedbackIdColumn2.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));

        // Create TableColumn for student ID
        TableColumn<AdminFBShort, Integer> studentIdColumn = new TableColumn<>("STUDENT ID");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        // Create TableColumn for student name
        TableColumn<AdminFBShort, String> studentNameColumn = new TableColumn<>("STUDENT NAME");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        // Create TableColumn for message
        TableColumn<AdminFBShort, String> messageColumn = new TableColumn<>("MESSAGE");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        // Create TableColumn for remark
        TableColumn<AdminFBShort, String> remarkColumn = new TableColumn<>("REMARK");
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remark"));
        
        remarkColumn.setCellFactory(column -> {
            return new TableCell<AdminFBShort, String>() {
                final TextField textField = new TextField();
                final Button respondButton = new Button("Respond");

                {
                    textField.setOnAction(event -> {
                        AdminFBShort item = getTableView().getItems().get(getIndex());
                        String newRemark = textField.getText();
                        item.setRemark(newRemark);
                        int feedbackId = item.getFeedbackId();

                        // Update the remark in the responds table in MySQL DB
                        try {
                            Connection connection = THE_CONNECTION.getTheConnection();
                            PreparedStatement preparedStatement = connection.prepareStatement(
                                    "INSERT INTO responds (feedback_id, admin_id, remark) VALUES (?, ?, ?) " +
                                            "ON DUPLICATE KEY UPDATE remark = VALUES(remark)"
                            );
                            preparedStatement.setInt(1, feedbackId);
                            preparedStatement.setInt(2, 101); // Set admin_id as 101 by default
                            preparedStatement.setString(3, newRemark);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Error", "Error while updating remark. Please try again later.");
                        }
                    });

                    respondButton.setOnAction(event -> {
                        AdminFBShort item = getTableView().getItems().get(getIndex());
                        String newRemark = textField.getText();
                        item.setRemark(newRemark);
                        int feedbackId = item.getFeedbackId();

                        // Update the remark in the responds table in MySQL DB
                        try {
                            Connection connection = THE_CONNECTION.getTheConnection();
                            PreparedStatement preparedStatement = connection.prepareStatement(
                                    "INSERT INTO responds (feedback_id, admin_id, remark) VALUES (?, ?, ?) " +
                                            "ON DUPLICATE KEY UPDATE remark = VALUES(remark)"
                            );
                            preparedStatement.setInt(1, feedbackId);
                            preparedStatement.setInt(2, 101); // Set admin_id as 101 by default
                            preparedStatement.setString(3, newRemark);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Error", "Error while updating remark. Please try again later.");
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        AdminFBShort adminFBShort = getTableView().getItems().get(getIndex());
                        String remark = adminFBShort.getRemark();
                        if (remark != null) {
                            setGraphic(new Label(remark));
                        } else {
                            HBox hbox = new HBox(textField, respondButton);
                            hbox.setAlignment(Pos.CENTER);
                            setGraphic(hbox);
                        }
                    }
                }
            };
        });


        // Add columns to the table
        feedback2Table.getColumns().addAll(feedbackIdColumn2, studentIdColumn, studentNameColumn, messageColumn, remarkColumn);

        // Fetch data and add it to the table
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT feedback.feedback_id, student.student_id, student.name AS student_name, feedback.message, responds.remark FROM feedback JOIN student ON student.student_id = feedback.student_id LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("student_name");
                String message = resultSet.getString("message");
                String remark = resultSet.getString("remark");
                
            feedback2Table.getItems().add(new AdminFBShort(feedbackId, studentId, studentName, message, remark));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching feedback. Please try again later.");
        }
        
        feedbackButton.setOnAction(event -> {
            rightContent.getChildren().clear();
            rightContent.getChildren().add(feedback2Table);
        });

        
        // Create and return the scene
        Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        return scene;
    }
   
    private TableView<LeaveRecord> fetchLeaveApplications(VBox rightContent) {
        TableView<LeaveRecord> leaveTable = new TableView<>();

        // Columns
        TableColumn<LeaveRecord, Integer> leaveIdColumn = new TableColumn<>("LEAVE ID");
        leaveIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaveId"));

        TableColumn<LeaveRecord, Integer> studentIdColumn = new TableColumn<>("STUDENT ID");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<LeaveRecord, String> studentColumn = new TableColumn<>("STUDENT");
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<LeaveRecord, String> datesColumn = new TableColumn<>("DATES");
        datesColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));

        TableColumn<LeaveRecord, String> commentColumn = new TableColumn<>("COMMENT");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        TableColumn<LeaveRecord, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

     // Create TableColumn for Approve button
        TableColumn<LeaveRecord, Button> approveButtonColumn = new TableColumn<>("APPROVE");
        approveButtonColumn.setCellValueFactory(new PropertyValueFactory<>("approveButton"));

        // Create TableColumn for Reject button
        TableColumn<LeaveRecord, Button> rejectButtonColumn = new TableColumn<>("REJECT");
        rejectButtonColumn.setCellValueFactory(new PropertyValueFactory<>("rejectButton"));

        // Add columns to the table
        leaveTable.getColumns().addAll(leaveIdColumn, studentIdColumn, studentColumn, datesColumn, commentColumn, statusColumn, approveButtonColumn, rejectButtonColumn);
        // Fetch data and add it to the table
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT leave_apply.leave_id, leave_apply.student_id, student.name, " +
                            "CONCAT(leave_apply.start_date, ' - ', leave_apply.end_date) AS dates, " +
                            "leave_apply.message, leave_apply.status " +
                            "FROM leave_apply " +
                            "INNER JOIN student ON leave_apply.student_id = student.student_id"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int leaveId = resultSet.getInt("leave_id");
                int studentId = resultSet.getInt("student_id");
                String studentName = resultSet.getString("name");
                String dates = resultSet.getString("dates");
                String comment = resultSet.getString("message");
                String status = resultSet.getString("status");

                LeaveRecord leaveRecord = new LeaveRecord(leaveId, studentId, studentName, dates, comment, status);
                leaveTable.getItems().add(leaveRecord);

                // If status is pending, add approve and reject buttons
                if ("pending".equals(status)) {
                    addApproveRejectButtons(leaveRecord, leaveTable, rightContent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error while fetching leave applications.");
        }

        return leaveTable;
    }

    private void addApproveRejectButtons(LeaveRecord leaveRecord, TableView<LeaveRecord> leaveTable,VBox rightContent ) {
        // Assuming you have Button objects approveButton and rejectButton
    	Button approveButton = new Button("Approve");
    	Button rejectButton = new Button("Reject");

    	approveButton.setOnAction(event -> {
    	    updateLeaveStatus(leaveRecord.getLeaveId(), "approved", leaveTable);
    	    updateTableView(leaveTable,rightContent); // Update the table view in the UI
    	});

    	rejectButton.setOnAction(event -> {
    	    updateLeaveStatus(leaveRecord.getLeaveId(), "rejected", leaveTable);
    	    updateTableView(leaveTable,rightContent); // Update the table view in the UI
    	});

        leaveRecord.setApproveButton(approveButton);
        leaveRecord.setRejectButton(rejectButton);
    }

    private void updateTableView(TableView<LeaveRecord> leaveTable,VBox rightContent) {
    // Clear previous content
    rightContent.getChildren().clear();

    // Fetch leave applications
    TableView<LeaveRecord> leaveTable2 = fetchLeaveApplications(rightContent);

    // Add leave table to the right content
    rightContent.getChildren().add(leaveTable2);
}



    private void updateLeaveStatus(int leaveId, String status, TableView<LeaveRecord> leaveTable) {
        try {
            Connection connection = THE_CONNECTION.getTheConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE leave_apply SET status = ? WHERE leave_id = ?"
            );
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, leaveId);
            preparedStatement.executeUpdate();

            // Refresh the table after updating the status
            leaveTable.getItems().clear();
//            fetchLeaveApplications(); // Assuming this method updates the TableView
            
            
//            ObservableList<LeaveRecord> newLeaveRecords = leaveTable.getItems();
//            for (LeaveRecord leaveRecord : newLeaveRecords) {
//                if ("pending".equals(leaveRecord.getStatus())) {
//                    addApproveRejectButtons(leaveRecord, leaveTable);
//                }
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error while updating leave status.");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class LeaveRecord {
        private final int leaveId;
        private final int studentId;
        private final String studentName;
        private final String dates;
        private final String comment;
        private final String status;
        private Button approveButton;
        private Button rejectButton;

        public LeaveRecord(int leaveId, int studentId, String studentName, String dates, String comment, String status) {
            this.leaveId = leaveId;
            this.studentId = studentId;
            this.studentName = studentName;
            this.dates = dates;
            this.comment = comment;
            this.status = status;
        }

        public int getLeaveId() {
            return leaveId;
        }

        public int getStudentId() {
            return studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getDates() {
            return dates;
        }

        public String getComment() {
            return comment;
        }

        public String getStatus() {
            return status;
        }

        public Button getApproveButton() {
            return approveButton;
        }

        public void setApproveButton(Button approveButton) {
            this.approveButton = approveButton;
        }

        public Button getRejectButton() {
            return rejectButton;
        }

        public void setRejectButton(Button rejectButton) {
            this.rejectButton = rejectButton;
        }
    }
}