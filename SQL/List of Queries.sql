use mms;

-- Selecting Tables

select * from admin;
select * from feedback;
select * from leave_apply;
select * from leftover;
select * from manager;
select * from meal_booking;
select * from meal_owner;
select * from mess;
select * from requests;
select * from responds;
select * from student;

-- Inserting Admin Credentials 
INSERT INTO admin (admin_id,name,password) values (1212,'Rajesh Mishra','admin123');

-- Inserting Mess Details For Application 
INSERT INTO mess (mess_id,mess_name,capacity) VALUES
(101, 'RP', 100),
(102, 'KG', 100),
(103, 'RB', 50),
(104, 'SR', 75),
(105, 'Meera', 100);

-- Assigning Manager to each Mess by inserting Manager Credentials in the manager Table
INSERT INTO manager (manager_id, mess_id, name, password) VALUES
(1011, 101, 'Ajay Gupta','rp1011'),
(1021, 102, 'Nitin Saini','kg1021'),
(1031, 103, 'Manish Nath','rb1031'),
(1041, 104, 'Rajveer Singh','sr1041'),
(1051, 105, 'Radhika Singh','mr1051');

-- Note that '?' in below queries represent placeholders for data input from Java Application

-- LOGIN & SIGNUP Queries
-- Check login credentials query
SELECT * FROM {userType} WHERE {userType}_id = ? AND password = ?;
-- This query is used to check the login credentials entered by the user against the corresponding table (student, manager, or admin) in the database.
-- Sample query:
-- SELECT * FROM student WHERE student_id = 20220057 AND password = '1234';

-- Insert new student record query for signup
INSERT INTO student (student_id, name, hostel, room, mess_id, password) VALUES (?, ?, ?, ?, ?, ?);
-- This query inserts a new student record into the student table when a user signs up.
-- Sample query:
-- INSERT INTO student (student_id, name, hostel, room, mess_id, password) VALUES (20220057, 'kshitiz', 'RP', 4114, 101, '1234');

-- Insert new record into meal_owner table for signup
INSERT INTO meal_owner (meal_id, student_id) VALUES (?, ?);
-- This query inserts a new record into the meal_owner table, mapping the meal ID to the student ID.
-- Sample query:
-- INSERT INTO meal_owner (meal_id, student_id) VALUES (220057, 20220057);
-- The meal_id is generated automatically based on the last six digits of the student_id.

-- Insert initial meal bookings query for signup
INSERT INTO meal_booking (meal_id, time, booked) VALUES (?, ?, 1);
-- This query inserts initial meal bookings for the newly signed-up student for all three meal times (breakfast, lunch, dinner).
-- Sample query:
-- INSERT INTO meal_booking (meal_id, time, booked) VALUES (220057, 'breakfast', 1);

-- Insert initial meal requests query for signup
INSERT INTO requests (student_id, meal_id, time) VALUES (?, ?, ?);
-- This query inserts initial meal requests for the newly signed-up student for all three meal times (breakfast, lunch, dinner).
-- Sample query:
-- INSERT INTO requests (student_id, meal_id, time) VALUES (20220057, 220057, 'breakfast');

-- StudentPanel queries
-- 1.  Query to fetch student credentials including name, hostel, room, mess, and meal ID based on student ID
SELECT s.name, s.hostel, s.room, m.mess_name, mo.meal_id
FROM student s
JOIN mess m ON s.mess_id = m.mess_id
JOIN meal_owner mo ON s.student_id = mo.student_id
WHERE s.student_id = ?;
-- Example: Fetch student credentials for student ID 123
-- SELECT s.name, s.hostel, s.room, m.mess_name, mo.meal_id
-- FROM student s
-- JOIN mess m ON s.mess_id = m.mess_id
-- JOIN meal_owner mo ON s.student_id = mo.student_id
-- WHERE s.student_id = 20220057;

-- 2. Query to fetch meal entries for a specific meal ID from the meal_booking table
SELECT time, booked
FROM meal_booking
WHERE meal_id = ?;

-- Example: Fetch meal entries for meal ID 456
-- SELECT time, booked
-- FROM meal_booking
-- WHERE meal_id = 220057;

--  3. Query to fetch leave applications for a specific student ID from the leave_apply table
SELECT start_date, end_date, message, status
FROM leave_apply
WHERE student_id = ?;

-- Example: Fetch leave applications for student ID 789
-- SELECT start_date, end_date, message, status
-- FROM leave_apply
-- WHERE student_id = 20220057;

-- 4. Query to fetch feedback entries along with remarks for a specific student ID from the feedback and responds tables
SELECT feedback.feedback_id, feedback.message, responds.remark
FROM feedback
LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id
WHERE feedback.student_id = ?;

-- Example: Fetch feedback entries for student ID 456
-- SELECT feedback.feedback_id, feedback.message, responds.remark
-- FROM feedback
-- LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id
-- WHERE feedback.student_id = 20220057;


-- 5. Update statement to toggle the booked status in the meal_booking table based on meal ID and time
UPDATE meal_booking
SET booked = CASE
              WHEN meal_id = ? AND time = ? THEN ?
              ELSE booked
             END
WHERE meal_id = ? AND time = ?;

-- Example: Toggle meal booking status for meal ID 220057 and time 'breakfast' to 1
-- UPDATE meal_booking
-- SET booked = CASE
--               WHEN meal_id = 220057 AND time = 'breakfast' THEN 1
--               ELSE booked
--              END
-- WHERE meal_id = 220057 AND time = 'breakfast'

-- 6.  Insert statement to add a leave application to the leave_apply table
INSERT INTO leave_apply (student_id, start_date, end_date, status, message)
VALUES (?, ?, ?, ?, ?);

-- Example: Submit a leave application for student ID 789 with start date '2024-04-15', end date '2024-04-20', status 1 (pending), and message 'Going home for vacation'
-- INSERT INTO leave_apply (student_id, start_date, end_date, status, message)
-- VALUES (20220057, '2024-04-18', '2024-04-20', 1, 'Going home for vacation');

-- 7. Insert statement to add feedback to the feedback table
INSERT INTO feedback (student_id, message)
VALUES (?, ?);

-- Example: Submit feedback for student ID 456 with message 'The food quality is excellent'
-- INSERT INTO feedback (student_id, message)
-- VALUES (20220057, 'The food quality is excellent')

-- 8.  Update statement to set the addon in the requests table based on student ID, meal ID, and time
UPDATE requests
SET addon = ?
WHERE student_id = ? AND meal_id = ? AND time = ?;

-- Example: Order addon for student ID 789, meal ID 123, and time 'lunch'
-- UPDATE requests
-- SET addon = 1
-- WHERE student_id = 20220057 AND meal_id = 220057 AND time = 'lunch';

-- ManagerPanel queries

-- 1.Retrieve Mess ID Query:sql
-- Explanation: This query retrieves the mess ID, capacity, and name for the mess associated with a specific manager ID.
   SELECT mess_id, mess.capacity, mess_name
   FROM manager
   NATURAL JOIN mess
   WHERE manager_id = ?;
-- Example Query:
-- SELECT mess_id, mess.capacity, mess_name
-- FROM manager
-- NATURAL JOIN mess
-- WHERE manager_id = 101;

-- 2.Food Package Requests Query:
-- Explanation: This query fetches food package requests for a specific mess ID.
   SELECT r.student_id, s.name, r.time
   FROM requests r
   JOIN student s ON r.student_id = s.student_id
   WHERE s.mess_id = ? AND r.food_package = 1;
-- Example Query:
-- SELECT r.student_id, s.name, r.time
-- FROM requests r
-- JOIN student s ON r.student_id = s.student_id
-- WHERE s.mess_id = 101 AND r.food_package = 1
     
-- 3.Add-On Requests Query:
-- Explanation: This query retrieves add-on requests for a specific mess ID.
   SELECT r.student_id, s.name, r.time, r.addon
   FROM requests r
   JOIN student s ON r.student_id = s.student_id
   WHERE s.mess_id = ? AND r.addon != 0;
-- Example Query:
-- SELECT r.student_id, s.name, r.time, r.addon
-- FROM requests r
-- JOIN student s ON r.student_id = s.student_id
-- WHERE s.mess_id = 101 AND r.addon != 0;

-- 4. Acknowledgement Query:
-- Explanation: This query updates the acknowledgment status for food package and add-on requests based on the meal time.
   UPDATE requests r
   JOIN (
       SELECT r1.meal_id
       FROM requests r1
       JOIN meal_owner mo ON r1.meal_id = mo.meal_id
       JOIN student s ON mo.student_id = s.student_id
       WHERE s.mess_id = ? AND r1.time = ?
   ) AS subquery ON r.meal_id = subquery.meal_id
   SET r.food_package = CASE WHEN r.time = ? THEN 0 ELSE r.food_package END,
       r.addon = CASE WHEN r.time = ? THEN 0 ELSE r.addon END;
-- Example Query:
-- 	UPDATE requests r
-- 	JOIN (
-- 	SELECT r1.meal_id
-- 	FROM requests r1
-- 	JOIN meal_owner mo ON r1.meal_id = mo.meal_id
-- 	JOIN student s ON mo.student_id = s.student_id
-- 	WHERE s.mess_id = 101 AND r1.time = 'breakfast'
-- 	) AS subquery ON r.meal_id = subquery.meal_id
-- 	SET r.food_package = CASE WHEN r.time = 'breakfast' THEN 0 ELSE r.food_package END,
-- 	r.addon = CASE WHEN r.time = 'breakfast' THEN 0 ELSE r.addon END;

-- 5. Leftover Fetch Query:
-- Explanation: This query fetches leftover data for a specific mess ID.
   SELECT time, COALESCE(total_leftover, 0) AS total_leftover
   FROM leftover
   WHERE mess_id = ?;
--  Example Query:
-- 	SELECT time, COALESCE(total_leftover, 0) AS total_leftover
-- 	FROM leftover
-- 	WHERE mess_id = 101;

-- 6. Initial State Fetch Query:
-- Explanation: This query fetches the initial state of attendance count, total prepared, and total leftover for a specific meal time and mess ID.
   SELECT total_attended, total_prepared, total_leftover
   FROM leftover
   WHERE time = ? AND mess_id = ?;
-- Example Query:
-- SELECT total_attended, total_prepared, total_leftover
-- FROM leftover
-- WHERE time = 'lunch' AND mess_id = 101;

-- 7. Update Attendance Query:
-- Explanation: This query updates the total attendance count for a specific meal time and mess ID.
   UPDATE leftover
   SET total_attended = ?
   WHERE time = ? AND mess_id = ?;
-- Example Query:
-- UPDATE leftover
-- SET total_attended = 150
-- WHERE time = 'lunch' AND mess_id = 101;

-- 8. Update Prepared Query:
-- Explanation: This query updates the total prepared count based on the remaining capacity for a specific meal time, mess ID, and remaining capacity.
   UPDATE leftover l
   JOIN (
       SELECT m.mess_id,
              (CASE WHEN COUNT(mb.booked) = 0 THEN m.capacity ELSE (m.capacity - COUNT(mb.booked)) END) AS remaining_capacity
       FROM mess m
       JOIN manager mn ON m.mess_id = mn.mess_id
       JOIN student s ON m.mess_id = s.mess_id
       JOIN meal_owner mo ON s.student_id = mo.student_id
       LEFT JOIN meal_booking mb ON mo.meal_id = mb.meal_id AND mb.time = ? AND mb.booked = 0
       GROUP BY m.mess_id, m.capacity
   ) AS subquery ON l.mess_id = subquery.mess_id
   SET l.total_prepared = subquery.remaining_capacity
   WHERE l.mess_id = ? AND l.time = ?;
-- Example Query:
-- UPDATE leftover l
-- JOIN (
-- SELECT m.mess_id,
-- (CASE WHEN COUNT(mb.booked) = 0 THEN m.capacity ELSE (m.capacity - COUNT(mb.booked)) END) AS remaining_capacity
--       FROM mess m
--       JOIN manager mn ON m.mess_id = mn.mess_id
--       JOIN student s ON m.mess_id = s.mess_id
--       JOIN meal_owner mo ON s.student_id = mo.student_id
--       LEFT JOIN meal_booking mb ON mo.meal_id = mb.meal_id AND mb.time = 'lunch' AND mb.booked = 0
--       GROUP BY m.mess_id, m.capacity
--  ) AS subquery ON l.mess_id = subquery.mess_id
--  SET l.total_prepared = subquery.remaining_capacity
--  WHERE l.mess_id = 101 AND l.time = 'lunch';

-- 9. Update Leftover Query:
-- Explanation: This query updates the total leftover count based on the difference between total prepared and total attended for a specific meal time and mess ID.
   UPDATE leftover
   SET total_leftover = (total_prepared - total_attended)
   WHERE time = ? AND mess_id = ?;
   
-- Example Query:
-- UPDATE leftover
-- SET total_leftover = (total_prepared - total_attended)
-- WHERE time = 'lunch' AND mess_id = 101;

-- AdminPanel queries

-- Query to fetch leave applications along with student information
SELECT student.student_id, student.name, leave_apply.start_date, leave_apply.end_date, leave_apply.message
FROM leave_apply
NATURAL JOIN student;

-- Query to fetch feedback along with student information
SELECT student.student_id, student.name, feedback.message
FROM feedback
NATURAL JOIN student;

-- Query to fetch feedback along with student information and admin responses
SELECT feedback.feedback_id, student.student_id, student.name AS student_name, feedback.message, responds.remark
FROM feedback
JOIN student ON student.student_id = feedback.student_id
LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id;

-- Query to update leave application status to 'approved' or 'rejected'
UPDATE leave_apply
SET status = ?
WHERE leave_id = ?;
-- Example query
-- UPDATE leave_apply
-- SET status = 1
-- WHERE leave_id = 2;

-- Query to insert or update admin remarks on feedback responses
INSERT INTO responds (feedback_id, admin_id, remark)
VALUES (?, ?, ?)
ON DUPLICATE KEY UPDATE remark = VALUES(remark);
-- Example query
-- INSERT INTO responds (feedback_id, admin_id, remark)
-- VALUES (6, 1212, 'Your request will be fulfilled as soon as possible.');
-- ON DUPLICATE KEY UPDATE remark = VALUES(remark);

-- Query to fetch leave applications for display in admin dashboard
SELECT leave_apply.leave_id, leave_apply.student_id, student.name,
CONCAT(leave_apply.start_date, ' - ', leave_apply.end_date) AS dates,
leave_apply.message, leave_apply.status
FROM leave_apply
INNER JOIN student ON leave_apply.student_id = student.student_id;

-- Query to fetch feedback for display in admin dashboard
SELECT feedback.feedback_id, student.student_id, student.name AS student_name, feedback.message, responds.remark
FROM feedback
JOIN student ON student.student_id = feedback.student_id
LEFT JOIN responds ON feedback.feedback_id = responds.feedback_id;
