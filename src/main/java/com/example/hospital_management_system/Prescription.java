package com.example.hospital_management_system;

import java.io.Serializable;
import java.util.Scanner;

public class Prescription implements Serializable {
    Doctor Practitioner;
    Patient prescribedto;

    public Patient getPrescribedto() {
        return prescribedto;
    }

    private String Medicines;
    private String Prescriptiondate;

    public Prescription(Doctor practitioner, Patient prescribedto, String medicines, String prescriptiondate) {
        Practitioner = practitioner;
        this.prescribedto = prescribedto;
        Medicines = medicines;
        Prescriptiondate = prescriptiondate;
    }

    public Prescription(Doctor doctor, Patient patient) {
        Scanner input = new Scanner(System.in);

        // Set Doctor
        this.Practitioner = doctor;

        // Set Patient
        this.prescribedto = patient;

        // Set Medicines
        System.out.println("Enter Prescribed Medicines:");
        input.nextLine(); // consume the newline character
        this.Medicines = input.nextLine();

        // Set Prescription Date
         // Current date and time

        System.out.println("Prescription has been written successfully.");
    }

    public void DisplayPrescription() {
        System.out.println("Doctor Appointed :" + Practitioner.getName() + "\n" + Practitioner.getID());
        System.out.println("Patient INFO");
        System.out.println(prescribedto);
        System.out.println("Doctor INFO ");
        System.out.println("Prescribed Medicines : \n" + Medicines);
        System.out.println("Prescribed Date to patient :" + Prescriptiondate);

    }
    public Doctor getPractitioner() {
        return Practitioner;
    }

    public String getMedicines() {
        return Medicines;
    }

    public String getPrescriptiondate() {
        return Prescriptiondate;
    }
}