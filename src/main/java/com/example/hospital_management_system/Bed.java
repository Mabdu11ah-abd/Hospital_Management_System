package com.example.hospital_management_system;

import java.io.Serializable;

public class Bed implements Serializable {
    private int DaysOccupied;
    private int bedNumber;
    private boolean isOccupied=false;
//constructors of class
    public Bed() {}
    public Bed(int bedNumber) {
        this.bedNumber=bedNumber;
    }
    // methods of bed
    // Method to vacate the bed
    public void vacantBed() {
        if (isOccupied) {
            isOccupied = false;
            System.out.println("Bed " + bedNumber + " is now vacant.");
        } else {
            System.out.println("Bed " + bedNumber + " is already vacant.");
        }
    }
    // Getters and Setters for all fields
    public void setOccupied() {
        isOccupied = true;
    }
    public boolean getisOccupied() {
        return isOccupied;
    }
    public int getDaysOccupied() {
        return DaysOccupied;
    }
    public void setDaysOccupied(int daysOccupied) {
        DaysOccupied = daysOccupied; isOccupied=true;
    }
}
