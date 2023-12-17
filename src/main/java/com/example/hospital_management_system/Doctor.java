package com.example.hospital_management_system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User implements Serializable {
    private String Specialization;
    private ArrayList<Patient> Patients = new ArrayList<>(20);
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    //Constructors of the class
    public Doctor(String username, String password, String id, String sp) {
        ID = id;
        Specialization = sp;
        Name = username;
        Password = password;
    }
    public Doctor() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the Name of the Doctor : ");
        this.Name = input.nextLine();
        System.out.println("Enter the ID of the Doctor : ");
        this.ID = input.nextLine();
        System.out.println("Enter Specialization of the Doctor : ");
        this.Specialization = input.nextLine();
        System.out.println("Enter Password of the new Doctor : ");
        Password = input.nextLine();
    }
//Methods of the class 
    public void WritePrescription() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the ID of the patient whos ID prescription you want to write : ");

        String searchID = input.nextLine();
        if (searchPatients(searchID) != -1) {
            Prescription temPrescription = new Prescription(this, Patients.get(searchPatients(searchID)));
            Patients.get(searchPatients(searchID)).getRecord().addPrescription(temPrescription);
        } else {
            System.out.println("Wrong ID has been entered");
        }
    }

    public void UpdatePatientNotes() {
        Scanner input = new Scanner(System.in);// scanner variable
        // input validation for patient id
        System.out.println("Enter the ID of the patient whos data you want to update : ");
        String searchID = input.nextLine();
        if (searchPatients(searchID) != -1) {
            Patients.get(searchPatients(searchID)).SetPatient();
        } else {
            System.out.println("Patient ID does not exist : ");
        }
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void assignpatient(Patient patient) {
        Patients.add(patient);
    }

    public void ViewPatients() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter 1 to view patient by ID : \nEnter 2 to view all patients : ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            System.out.println("Enter the ID of the patient whos data you want to view : ");
            String searchID = input.nextLine();
            if (searchPatients(searchID) != -1) {
                System.out.println(Patients.get(searchPatients(searchID)));
            } else {
                System.out.println("Invalid ID entered : ");
            }
        } else if (choice == 2) {
            {
                for (Patient patient : Patients) {
                    System.out.println(patient);
                }
            }

        } else {
            System.out.println("Invalid Choice : ");
        }

    }

    public void ManageAppointments() {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 1 to view upcoming appointments");
            System.out.println("Enter 3 to CancelAppointments : ");
            System.out.println("Enter 0 to exits");

            int choice = input.nextInt();
            input.nextLine();
            if (choice == 0) {
                break;
            }
            if (choice == 1) {
                System.out.println("All Appointments:");
                for (Appointment appointment : appointments) {
                    appointment.ViewAppointment();
                }
            } else if (choice == 2) {
                System.out.println("Enter ID of the Appointment you want to mark as Finished : ");
                String cancelAppointmentID = input.nextLine();
                for (Appointment appointment : appointments) {
                    if (appointment.getAppointmentID().equals(cancelAppointmentID)) {
                        appointment.CancelAppointment();
                        System.out.println("Appointment Cancelled");
                        break;
                    }
                }
            } else {
                System.out.println("Invalid Choice");
            }
            // Remove completed or cancelled appointments
            ArrayList<Appointment> appointmentsToRemove = new ArrayList<>();
            for (Appointment appointment : appointments) {
                if (appointment.getStatus().equals("FINISHED") || appointment.getStatus().equals("CANCELLED")) {
                    appointmentsToRemove.add(appointment);
                }
            }
            appointments.removeAll(appointmentsToRemove);
        }
    }
//getter method of class
public String getSpecialization() {
    return Specialization;
}


    // search patients method to be used whenever searching by id is required
    private int searchPatients(String id) {
        for (int index = 0; index < Patients.size(); index++) {
            if (Patients.get(index).getID().equals(id)) {
                return index;
            }
        }
        return -1;
    }
    public String getName()
    {
        return Name;
    }

    public ArrayList<Patient> getPatients() {
        return Patients;
    }

    //to string method of the class
    @Override
    public String toString() {
        return "Doctor [Name= " + Name + ",Specialization=" + Specialization + "ID = " + ID + "]";
    }
}