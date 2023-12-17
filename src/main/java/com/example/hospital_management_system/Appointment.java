package com.example.hospital_management_system;

import java.io.Serializable;
import java.util.Scanner;

public class Appointment implements Serializable {
    transient Scanner input = new Scanner(System.in);
    private String AppointmentID;
    public void setAppointmentID(String appointmentID) {
        AppointmentID = appointmentID;
    }
    public String getAppointmentID() {
        return AppointmentID;
    }
    private Patient AppointmentPatient; // will store patient type object
    private Doctor AppointmentDoctor; // will store Doctor type Object
    private String AppointmentDate; // Google how to use Date Class
    private String Appointmenttime; // String type to be set by user
    private String Status; // can either be Pending, Finished or Cancelled;

//constructors of the class
    public Appointment(Patient appointmentPatient, Doctor appointmentDoctor, String appointmentDate, String appointmenttime) {
        AppointmentPatient = appointmentPatient;
        AppointmentDoctor = appointmentDoctor;
        AppointmentDate = appointmentDate;
        Appointmenttime = appointmenttime;
    }
    //constructor used in the main class
    public Appointment(Patient AppointmentPatient, Doctor AppointmentDoctor) {
        System.out.println("SCHEDULE APPOINTMENT SECTION");
        this.AppointmentPatient = AppointmentPatient;
        this.AppointmentDoctor = AppointmentDoctor;
        System.out.println("Enter the Date you want to take the Appointment from Doctor ");
        AppointmentDate = input.nextLine();
        System.out.println("Enter appointment time : ");
        Appointmenttime= input.nextLine();
        System.out.println("Enter Appointment ID : ");
        this.AppointmentID=input.nextLine();
        Status = "PENDING";
        // set the details of the appointment
    }
    public void updateAppointment() {
        System.out.println("APPOINTMENT UPDATE SECTION");
        System.out.println("Choose what you want to change");
        System.out.println("Press 1 to change the STATUS");
        System.out.println("Press 2 to change the date of Appointment");

        int choice = input.nextInt();
        if (choice == 1) {
            updateStatus();
        } else if (choice == 2) {
            System.out.println("APPOINTMENT DATE CHANGE SECTION\n Enter new date: ");
            AppointmentDate = input.nextLine();
            System.out.println("Appointment date updated to: " + AppointmentDate);
        }
    }
    private void updateStatus() {
        System.out.println("APPOINTMENT STATUS UPDATE SECTION\n");
        System.out.println("Appointment ID: " + AppointmentID);
        System.out.println("Choose the options to update Appointment Status");
        System.out.println("Press 1 to mark the Appointment PENDING");
        System.out.println("Press 2 to mark the Appointment FINISHED");
        System.out.println("Press 3 to mark the Appointment CANCELED");
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                Status = "PENDING";
                break;
            case 2:
                Status = "FINISHED";
                break;
            case 3:
                Status = "CANCELLED";
                break;
            default:
                System.out.println("Invalid choice");
        }
        System.out.println("Status Updated to " + Status);
    }
    public void CancelAppointment() {
        System.out.println("APPOINTMENT CANCELLATION SECTION");
            this.Status = "CANCELLED";
            System.out.println("Status Successfully Updated to be " + Status);
            // Mark the Status of the Appointment as "CANCELLED"
    }
    public void ViewAppointment()
    {   System.out.println(AppointmentID);
        System.out.println(AppointmentPatient);
        System.out.println(AppointmentDoctor);
        System.out.println("Date : " + AppointmentDate + ",Time : "+ Appointmenttime);
    }
    //getters and setters of the class
    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }
    public Patient getAppointmentPatient() {
        return AppointmentPatient;
    }

    public Doctor getAppointmentDoctor() {
        return AppointmentDoctor;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getAppointmenttime() {
        return Appointmenttime;
    }
    public void setStatus(String status) {
        this.Status = status;
    }
    public String getStatus() {return Status;}

}