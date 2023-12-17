package com.example.hospital_management_system;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        FileHandling f = new FileHandling();
        ArrayList a = new ArrayList<>();
        User u = new User();
        u.setID("A-1");
        u.setPassword("1");
        a.add(u);
        a.add(new Patient(  "JohnDoe", "P-1", "password123", 25, "123 Main St"));
        a.add(new Doctor("Man","123","D-1","typ31"));
        f.writeObject(a);

    }
}
