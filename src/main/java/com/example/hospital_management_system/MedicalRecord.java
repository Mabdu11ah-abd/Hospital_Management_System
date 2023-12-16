package com.example.hospital_management_system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MedicalRecord implements Serializable {
    private String AdmissionDate;
    private String DischargeDate;
    private ArrayList<Prescription> Prescriptions = new ArrayList<>(); // As Prescription is another class


    public ArrayList<Prescription> getPrescriptions() {
        return Prescriptions;
    }

    public void editRecord() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter admission Date : ");
        AdmissionDate = input.nextLine();
        System.out.println("Enter discharge Date, Enter Admitted if not discharged : ");
        DischargeDate = input.nextLine();
    }

    // Method to add a prescription to the medical record
    public void addPrescription(Prescription prescription) {
        if (!Prescriptions.contains(prescription)) {
            Prescriptions.add(prescription);
            System.out.println("Prescription added"); // getter of details present in prescript
        } else {
            System.out.println("Cannot add prescription. The patient is either discharged or not admitted.");
        }
    }

    // Method to get data of all or latest prescription of the patient.
    public void viewMedicalRecord() {
        System.out.println(
                "Admission Date : " + AdmissionDate + "\nDischarge Date : " + DischargeDate);
        System.out.println("Enter 1 to see all prescriptions \nEnter 2 to see latest prescription");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 1) {
            if(Prescriptions.isEmpty())
            {
                System.out.println("There are No prescriptions : ");
                return;
            }
            for (int i = 0; i < Prescriptions.size(); i++) {
                System.out.println(Prescriptions.get(i));
            }
        } else if (choice == 2) {
             if(Prescriptions.isEmpty())
            {
                System.out.println("There are prescriptions : ");
                return;
            }
            int latestPrescription = Prescriptions.size() - 1;
            System.out.println(Prescriptions.get(latestPrescription));
        } else {
            System.out.println("Invalid Choice");
        }
    }
}