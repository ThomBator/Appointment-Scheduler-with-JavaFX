Title:  Desktop Appointment Scheduling Application

Purpose: This application allows users to schedule and update appointments for customers. There is additional functionality
to add new customers and update customer information as needed. The program also includes basic reporting functionality to get
metrics about appointments currently stored in the database.

Date: Tuesday, February 15th 2022.

Author: Thomas Bator

Student ID: #001465097

Contact: tbator1@wgu.edu

Created For: C195 Software Development II

School: Western Governors University

Professor: Juan Ruiz

Java Version: openJDK version 11 2018-09-25

Runtime Environment: openJDK Runtime Environment 18.9 (build 11 + 28)

JavaFX Version: SDK 17.0.1

SQL Driver Used: mysql-connector-java-8.0.27

How to Run Program:
Login Screen:
Enter Username: Test, Password: Test. Click the 'Sign-in' Button.
Main View:
Click the 'Add New Customer' button to add new customer to database.
Select a customer from the existing customers table and click the 'Update Customer' button to update an existing customer.
Select a customer from the existing customers table and click the 'Delete Customer' button to delete a customer and all their associated appointments.
Click 'Exit Program' to exit the appointment scheduler.
Click 'Add Appointment' to create new appointment.
Select an existing appointment from the appointments table and click 'Update Appointment' to update an existing appointment.
Select an existing appointment from the appointments table and click 'Delete Appointment' to delete an existing appointment.,
Add/Update Customer Views:
Fill in all enabled form fields and combo boxes to create/update a customer.
Click 'Add Customer' to add a new customer or 'Update Customer' when you are in the update version of this view to update a customer.
Click 'Cancel' to return to main without saving any new customer info or updatses.
Add/UpdateAppointment Views:
Fill in all enabled form fields and combo boxes to create/update an appointment.
 Click 'Add Appointment' to add a new customer or 'Update Customer' when you are in the update version of this view to update a customer.
 Click 'Cancel' to return to main without saving any new customer info or updates.
 Reports View:
 Select from combo boxes to see reports for specific categories.
 Click 'Return to Main' to return to the Main view.


Description of Report from Section A3F of Rubric: The additional report functionality I created allows users to see the total number of scheduled
appointments for each customer in the database.

Lambda Usage: details on the two required applications of lambdas outlined in part B of the rubric can be found in the javadoc comments for
the methods where they were used. For purposes of simplicity. Lambdas can be found in the following locations:

1. Class: AddModifyCustomerController. Method: setFirstLevel
2. Class: MainViewController. Methods: onSelectMonthRadio, onSelectWeekRadio.


