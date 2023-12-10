package com.example.hospital_management_system;

public class Bed {
    private int DaysOccupied;
    private int bedNumber;
    private boolean isOccupied=false;

    public Bed() {
    }

    public Bed(int bedNumber) {
        this.bedNumber=bedNumber;
    }

    // Getters and Setters for all fields
    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied() {
        isOccupied = true;
    }
    // Method to occupy the bed
    public void occupyBed() {
        if (!isOccupied) {
            isOccupied = true;
            System.out.println("Bed " + bedNumber + " is now occupied.");
        } else {
            System.out.println("Bed " + bedNumber + " is already occupied.");
        }
    }

    // Method to vacate the bed
    public void vacantBed() {
        if (isOccupied) {
            isOccupied = false;
            System.out.println("Bed " + bedNumber + " is now vacant.");
        } else {
            System.out.println("Bed " + bedNumber + " is already vacant.");
        }

    }
    public int getDaysOccupied() {
        return DaysOccupied;
    }

    public void setDaysOccupied(int daysOccupied) {
        DaysOccupied = daysOccupied; isOccupied=true;
    }

    // Other relevant methods
}
