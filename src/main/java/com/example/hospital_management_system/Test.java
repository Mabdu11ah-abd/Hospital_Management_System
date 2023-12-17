package com.example.hospital_management_system;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        FileHandling f = new FileHandling();
        ArrayList allUsers = new ArrayList<>();
        User u = new User();
        u.setID("A-1");
        u.setPassword("123");
        allUsers.add(u);
        Doctor doctor1 = new Doctor("Abdullah", "123", "D-1", "Specialization1");
        Doctor doctor2 = new Doctor("Doctor2", "123", "D-2", "Specialization2");
        Doctor doctor3 = new Doctor("Doctor3", "123", "D-3", "Specialization3");
        Doctor doctor4 = new Doctor("Doctor4", "123", "D-4", "Specialization4");
        Doctor doctor5 = new Doctor("Doctor5", "123", "D-5", "Specialization5");
        // Creating 5 Patient objects
        Patient patient1 = new Patient("Junaid", "P-1", "123", 25, "Address1");
        Patient patient2 = new Patient("Kanz-Ul-Eman", "P-2", "123", 120, "Address2");
        Patient patient3 = new Patient("Abdullah", "P-3", "123", 11, "Address3");
        Patient patient4 = new Patient("Patient4", "P-4", "123", 40, "Address4");
        Patient patient5 = new Patient("Patient5", "P-5", "123", 45, "Address5");
        // Adding objects to the ArrayList
        allUsers.add(doctor1);
        allUsers.add(doctor2);
        allUsers.add(doctor3);
        allUsers.add(doctor4);
        allUsers.add(doctor5);

        allUsers.add(patient1);
        allUsers.add(patient2);
        allUsers.add(patient3);
        allUsers.add(patient4);
        allUsers.add(patient5);
        f.writeObject(allUsers);
    }
}
