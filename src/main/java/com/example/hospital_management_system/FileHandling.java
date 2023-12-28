package com.example.hospital_management_system;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileHandling implements Serializable {

    public void writeInventory(Inventory a) {
        try {
            FileOutputStream f = new FileOutputStream("src/main/java/com/example/hospital_management_system/inventoryFile.txt", false);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(a);
            o.close();
        } catch (IOException exception) {
            System.out.println("file did not open properly ");
        }
    }

    public void writeObject(ArrayList<User> a) {
        try {
            FileOutputStream f = new FileOutputStream("src/main/java/com/example/hospital_management_system/userFile.txt", false);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(a);
            o.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void readUsers(ArrayList<User> a) {
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream("src/main/java/com/example/hospital_management_system/userFile.txt"));
            Object obj = i.readObject();
            if (obj instanceof ArrayList) {
                a.addAll((ArrayList<User>) obj);
            }
            i.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading from the file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("No such element: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Inventory readInventory() {
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream("src/main/java/com/example/hospital_management_system/inventoryFile.txt"));
            Object obj = i.readObject();
            i.close();
            return (Inventory) obj;

        } catch (IOException | NoSuchElementException | ClassNotFoundException exception) {
            System.out.println("File did not close properly : ");
        }
        return null;
    }

    public void writeBed(Bed[] b) {
        try {
            FileOutputStream f = new FileOutputStream("src/main/java/com/example/hospital_management_system/bedFile.txt", false);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(b);
            o.close();
        } catch (IOException exception) {
            System.out.println("Error opening file");
        }
    }

    public Bed[] readBed() {
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream("src/main/java/com/example/hospital_management_system/bedFile.txt"));
            Object obj = i.readObject();
            i.close();
            return ((Bed[]) obj);
        } catch (IOException | NoSuchElementException | ClassNotFoundException exception) {
            System.out.println("File did not Open properly : ");
        }
        return null;
    }

    public void writeNum(int[] a) {
        try {
            Formatter f = new Formatter("src/main/java/com/example/hospital_management_system/numFile.txt");
            f.format("%d %d %d", a[0], a[1], a[2]);
            f.close();
        } catch (IOException exception) {
        }
    }

    public void readNum(int[] a) {
        try {
            Scanner scanner = new Scanner(Paths.get("src/main/java/com/example/hospital_management_system/numFile.txt"));
            a[0] = scanner.nextInt();
            a[1] = scanner.nextInt();
            a[2] = scanner.nextInt();
        } catch (IOException exception) {
        }
    }


    public void writePrescription(Prescription p) {
        try {
            Formatter f = new Formatter("src/main/java/com/example/hospital_management_system/prescriptionFile.txt");
            f.format("Patient:%s \nDoctor: %s \nDate: %s \nMedicines: %s", p.getPrescribedto(),
                    p.getPrescribedto(), p.getPrescriptiondate(), p.getMedicines());
            f.close();
        } catch (IOException exception) {
            System.out.println("file did not open properly : ");
        }
    }

    public void writeAppointment(Appointment a) {
        try {
            Formatter f = new Formatter("src/main/java/com/example/hospital_management_system/appointmentFile.txt");
            f.format("Doctor: %s\nPatient: %s\nID: %s\nDate: %s\nTime: %s", a.getAppointmentDoctor(), a.getAppointmentPatient(), a.getAppointmentID()
                    , a.getAppointmentDate(), a.getAppointmenttime());
            f.close();
        } catch (IOException exception) {

        }
    }

}
