package com.example.hospital_management_system;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        FileHandling f = new FileHandling();
        Bed[]a = f.readBed();
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
        }
    }
