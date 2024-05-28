# Mess Management System

## Birla Institute of Technology and Science
**Course:** CSF212 - Database Systems  
**Project:** Mess Management System  
**Submitted To:** Ramakant Upadhyay  
**Submitted By:** Sarthak Bhagwan Ingle (2022A7PS0037P)

---

## Table of Contents
1. [Problem Statement](#problem-statement)
2. [Features/Requirements](#featuresrequirements)
3. [Normalization Explained](#normalization-explained)
4. [Updated Entity-Relation Model](#updated-entity-relation-model)
5. [Updated Relational Schema](#updated-relational-schema)
6. [Technical Details](#technical-details)
7. [Conclusion](#conclusion)

---

## Problem Statement
Traditional campus mess systems often struggle with several inefficiencies that hinder both operational effectiveness and student satisfaction. These systems rely heavily on manual processes, leading to errors and delays in meal booking, leave applications, and feedback collection. This not only creates frustration among students but also contributes to food wastage due to inaccurate meal planning and preparation. Moreover, the lack of flexibility in accommodating students' dietary preferences and varying schedules further exacerbates the problem.

The Mess Management System aims to revolutionize campus dining by providing a comprehensive solution that automates key processes, promotes sustainability, and meets the evolving needs of the student community.

---

## Features/Requirements
The Mess Management System is designed with a wide array of features that cater to the diverse needs of students while ensuring efficient resource allocation and utilization. Key functionalities include:

- **User Registration:** Seamless access using student ID or email.
- **Meal Booking:** Specify dining plans for each day and receive incentives for advance notice.
- **Mess Leave Applications:** Communicate absence effectively to minimize food waste.
- **Add-on Meal Preparation:** Options like khichdi, and food packing services for travel or busy schedules.
- **Leftover Tracking:** Monitor and manage surplus food effectively.
- **Mess-wise Management:** Localized control for each mess.
- **Feedback System:** Collect valuable input on mess services.
- **Notification System:** Real-time updates on meal timings, menu changes, and more.
- **Admin Panel:** Tools for overseeing operations, managing users, and monitoring feedback.

---

## Normalization Explained
After deriving the attribute closure and functional dependencies of the original Relational Schema, it was determined that most relations (tables) are redundancy-free and in 3NF/BCNF. The `Meal` table, initially in 1NF, had partial dependencies. Decomposition resulted in two new relations:

- `Meal_Booking` (Meal_ID, Time, Booked)
- `Meal_Owner` (Meal_ID, Student_ID)

This decomposition is lossless but not dependency preserving, yet it does not affect the overall efficiency of our Database System.

---

## Updated Entity-Relation Model
![Entity-Relation Model](path/to/er-model.png)

---

## Updated Relational Schema
1. **Student**
   - `Student_ID` INT (Primary)
   - `Name` VARCHAR
   - `Hostel` VARCHAR
   - `Room` INT
   - `Mess_ID` INT (Foreign, References Mess)

2. **Meal_Booking**
   - `Meal_ID` INT (Primary)
   - `Time` ENUM('Breakfast', 'Lunch', 'Dinner') (Primary)
   - `Booked` BOOLEAN

3. **Meal_Owner**
   - `Meal_ID` INT (Primary)
   - `Student_ID` INT (Foreign, References Student)

4. **Mess**
   - `Mess_ID` INT (Primary)
   - `Mess_Name` VARCHAR
   - `Capacity` INT
   - `Attended` INT

5. **Admin**
   - `Admin_ID` INT (Primary)
   - `Name` VARCHAR

6. **Feedback**
   - `Feedback_ID` INT (Primary)
   - `Student_ID` INT (Foreign, References Student)
   - `Message` TEXT

7. **Leave**
   - `Leave_ID` INT (Primary)
   - `Student_ID` INT (Foreign, References Student)
   - `Start_Date` DATE
   - `End_Date` DATE
   - `Status` ENUM('Pending', 'Approved', 'Rejected')

8. **Manager**
   - `Manager_ID` INT (Primary)
   - `Mess_ID` INT (Foreign, References Mess)
   - `Name` VARCHAR

9. **Leftover**
   - `Mess_ID` INT (Primary, Foreign, References Mess)
   - `Total_Prepared` INT
   - `Total_Leftover` INT
   - `Time` ENUM('Breakfast', 'Lunch', 'Dinner') (Primary)

10. **Requests**
    - `Student_ID` INT (Primary, Foreign, References Student)
    - `Manager_ID` INT (Foreign, References Manager)
    - `Addon` BOOLEAN
    - `Food_Package` BOOLEAN

11. **Responds**
    - `Feedback_ID` INT (Primary, Foreign, References Feedback)
    - `Admin_ID` INT (Foreign, References Admin)
    - `Remark` TEXT

---

## Technical Details
### Tech Stack Used
- **Frontend:** JavaFX
- **Backend:** MySQL
- **MySQL Workbench:** MessManagementSystem.mwb
- **MySQL Database Schema:** mmsSchema.sql
- **List of Queries Used:** List of Queries.sql

### Features Implemented
1. **User Registration**
   - Students can register through the Sign-Up page.
   - Initial data insertion queries and snapshots.

2. **Meal Booking**
   - Enable students to specify meal preferences.
   - Opt-out functionality with corresponding database updates.

3. **Mess Leave Application**
   - Apply for mess leave to reduce food waste.
   - Details added to the `leave_apply` table.

4. **Addon Preparation**
   - Option for additional meal preparation like khichdi.
   - Updates in the `requests` table.

5. **Food Packing**
   - Feature for packing meals for students who are traveling or busy.
   - Updates in the `requests` table.

6. **Leftover Tracking**
   - Track leftover food to improve resource efficiency.
   - Attendance and leftover calculations and updates.

7. **Mess-wise Management**
   - Separate management for each mess.
   - Manager dashboard with food package requests, add-on requests, and leftover tracking.

8. **Feedback System**
   - Collect and respond to student feedback.
   - Feedback and response queries.

9. **Admin Panel**
   - Manage user leaves, view feedback, and respond.
   - Admin functionalities and corresponding queries.

### Complex Queries
1. Update total prepared count based on remaining capacity.
2. Update acknowledgement status for food packages and add-on requests.
3. Fetch student credentials based on student ID.
4. Fetch leave applications for admin dashboard.
5. Fetch feedback for admin dashboard.

### Steps to Run Application
1. Install proper versions of JavaFX and Java Connector in the IDE.
2. Create a Java Project and add the JAR files.
3. Import code files into the `src` folder.
4. Open `Mess Management System.mwb` in MySQL.
5. Insert initial data for admin, mess, and manager tables.
6. Run `MainApp.java` as an application.
7. The Mess Management System interface will appear.
8. Detailed explanations are provided in `ReadMe File.docx`.

---

## Conclusion
Traditional campus mess systems are often burdened by inefficiencies, leading to frustration among students and contributing to unnecessary food wastage. These inefficiencies stem from manual processes, such as errors in meal booking, delays in processing leave applications, and challenges in collecting and addressing feedback effectively. As a result, students may encounter difficulties in securing meals according to their preferences and schedules, leading to dissatisfaction with the dining experience.

The Mess Management System represents a significant improvement by automating various processes, such as meal booking and leave applications, streamlining operations, and reducing the likelihood of errors. Efficient feedback collection mechanisms allow for better communication between students and administrators, leading to more responsive and tailored dining services. The system's ability to track and manage leftover food contributes to sustainability and responsible resource utilization. Ensuring a live database connection is crucial for maximizing the system's effectiveness in enhancing the dining experience for students.

---

For more details, please refer to the [ReadMe File.docx](path/to/readme-file.docx).
