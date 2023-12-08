package com.example.hospital_management_system;

import java.util.Scanner;

public class Patient extends User {
    private int age;
    private String address;
    private Doctor DoctorIncharge;
    private Bed BedUsed;
    private MedicalRecord Record = new MedicalRecord();
    private Billing Bill = new Billing();

    public Patient() {
    }

    public Patient(String username, String userID, String userPassword, int age, String address) {
        name = username;
        ID = userID;
        Password = userPassword;
        this.age = age;
        this.address = address;
    }

    public void SetPatient() {
        // scanner variable for input
        Scanner input = new Scanner(System.in);
        // taking user input for item attributes
        System.out.println("Enter the name of the patient : ");
        this.name = input.nextLine();
        System.out.println("Enter the ID of the patient : ");
        this.ID = input.nextLine();
        System.out.println("Enter the Address of the patient : ");
        this.address = input.nextLine();
        System.out.println("Enter Password : ");
        Password = input.nextLine();
        System.out.println("Enter the age of the patient : ");
        this.age = input.nextInt();
    }


    public void UpdateBilling(double amount) {// meant for user to buy items
        Bill.UpdatePayment(amount);
    }

    public void UpdateBilling()// meant for admin to discharge patient
    {
        Bill.finalPayment(this);
        this.BedUsed.setDaysOccupied(0);// reset bed occupied to zero to show that patient is discharged :
    }

    @Override
    public String toString() {
        return "Patient [name=" + name + ", ID=" + ID + ", age=" + age + ", address=" + address + "]";
    }

    public MedicalRecord getRecord() {
        return Record;
    }

    public void setRecord(MedicalRecord record) {
        Record = record;
    }

    public Bed getBedUsed() {
        return BedUsed;
    }

    public void setBedUsed(Bed bedUsed) {
        BedUsed = bedUsed;
    }

    public Billing getBill() {
        return Bill;
    }

    public void setBill(Billing bill) {
        Bill = bill;
    }

    public Doctor getDoctorIncharge() {
        return DoctorIncharge;
    }

    public void setDoctorIncharge(Doctor doctorIncharge) {
        DoctorIncharge = doctorIncharge;
    }
    public String getname()
    {
        return name;
    }

    public int getage() {
        return age;
    }

    public String getaddress() {
        return address;
    }
}
