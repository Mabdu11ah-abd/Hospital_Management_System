package com.example.hospital_management_system;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    ArrayList<User> allUsers;
    Bed allBeds[];
    Inventory GUIinv;
    Scene s1, RegisterScene, DoctorMenuScene, PatientMenuScene, AdminMenuScene, s6, s7, s8, s9;

    @Override
    public void start(Stage stage) throws IOException {
        InitializeUsers();
        final User[] CurrentUser = {null};                   //Stores the current User of the System
        stage.setTitle("Hospital Management System : ");

        //Login user Section
        Button loginb1 = new Button("Enter");
        Label login1 = new Label("Enter your Name : ");
        Label login2 = new Label("Enter Your Password : ");
        Button loginb2 = new Button("Register");
        TextField loginField = new TextField();
        PasswordField loginPass = new PasswordField();
        GridPane loginPane = new GridPane();
        loginPane.add(login1, 3, 3);
        loginPane.add(login2, 3, 4);
        loginPane.add(loginField, 4, 3);
        loginPane.add(loginPass, 4, 4);
        loginPane.add(loginb1, 4, 5);
        loginPane.add(loginb2, 3, 5);
        loginPane.setAlignment(Pos.BASELINE_CENTER);
        loginPane.setVgap(35);
        s1 = new Scene(loginPane, 500, 500);
        loginb1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CurrentUser[0] = loginUser(loginField.getText(), loginPass.getText());
                if (CurrentUser[0] == null) {
                    loginPane.add(new Label("Testing"), 9, 9);
                } else if (CurrentUser[0] instanceof Doctor) {
                    stage.setScene(DoctorMenuScene);
                } else if (CurrentUser[0] instanceof Patient) {
                    stage.setScene(PatientMenuScene);
                } else if (CurrentUser[0].getID().equals("A-1")) {
                    stage.setScene(AdminMenuScene);
                }
            }
        });
        loginb2.setOnAction(e -> stage.setScene(RegisterScene));
        //Register user Section
        GridPane registerUserPane = new GridPane();
        Button Registerb1 = new Button("Enter");
        Button Registergoback = new Button("Go back");
        Label Registerl1 = new Label("Name");
        Label Registerl2 = new Label("Password");
        Label Registerl3 = new Label("ID");
        Label Registerl4 = new Label("Address");
        Label Registerl5 = new Label("Patient");
        TextField Registername = new TextField();
        TextField password = new TextField();
        TextField id = new TextField();
        TextField address = new TextField();
        TextField patient = new TextField();

        //Add labels and text fields to the GridPane
        registerUserPane.add(Registerl1, 0, 0);
        registerUserPane.add(Registername, 1, 0);
        registerUserPane.add(Registerl2, 0, 1);
        registerUserPane.add(password, 1, 1);
        registerUserPane.add(Registerl3, 0, 2);
        registerUserPane.add(id, 1, 2);
        registerUserPane.add(Registerl4, 0, 3);
        registerUserPane.add(address, 1, 3);
        registerUserPane.add(Registerl5, 0, 4);
        registerUserPane.add(patient, 1, 4);
        registerUserPane.add(Registerb1, 1, 5);
        registerUserPane.add(Registergoback, 1, 6);
        registerUserPane.setAlignment(Pos.BASELINE_CENTER);
        registerUserPane.setVgap(35);
        RegisterScene = new Scene(registerUserPane, 500, 500);
        Registergoback.setOnAction(e -> {
            stage.setScene(s1);
        });
        Registerb1.setOnAction(e->{stage.setScene(PatientMenuScene);});
        //Doctor Menu
        /* Further Scenes for doctor menu are writing prescriptions 1s, Managing Appointments 3s,Updating Patient 1s*/
        Label Options = new Label("Choose Your Option ");
        Button doctorMenu1 = new Button("View Patients");
        Button doctorMenu2 = new Button("write Prescriptions");
        Button doctorMenu3 = new Button("Manage Appointments");
        Button doctorMenu4 = new Button("Update Patient Data");
        Button doctorMenu5 = new Button("LogOut");
        GridPane dMenuPane = new GridPane();
        dMenuPane.add(Options, 2, 1);
        dMenuPane.add(doctorMenu1, 0, 1);
        dMenuPane.add(doctorMenu2, 0, 2);
        dMenuPane.add(doctorMenu3, 0, 3);
        dMenuPane.add(doctorMenu4, 0, 4);
        dMenuPane.add(doctorMenu5,0,5);
        dMenuPane.setAlignment(Pos.BASELINE_CENTER);
        dMenuPane.setVgap(30);
        doctorMenu5.setOnAction(e->stage.setScene(s1));
        DoctorMenuScene = new Scene(dMenuPane, 500, 500);
        //Patient Menu
        /*CurrentInfo 1s, MedicalRecord 3s,GenerateInvoice 1s, Medicine 2s, ViewDoctors 1s, AssignDoctor 1s*/
        Label patientOptions = new Label("Choose Your Options");
        Button patientb1 = new Button("View Current Info");
        Button patientb2 = new Button("View Medical Record");
        Button patientb3 = new Button("Buy Medicine");
        Button patientb4 = new Button("Generate Invoice");
        Button patientb5 = new Button("View Doctors");
        Button patientb6 = new Button("Assign Doctors");
        Button patientb7 = new Button("Log out");
        GridPane patientMenu = new GridPane();
        patientMenu.add(patientOptions, 0, 0);
        patientMenu.add(patientb1, 0, 1);
        patientMenu.add(patientb2, 0, 2);
        patientMenu.add(patientb3, 0, 3);
        patientMenu.add(patientb4, 0, 4);
        patientMenu.add(patientb5, 0, 5);
        patientMenu.add(patientb6, 0, 6);
        patientMenu.add(patientb7,0,7);
        patientMenu.setAlignment(Pos.BASELINE_CENTER);
        patientMenu.setVgap(35);
        patientb7.setOnAction(e->{stage.setScene(s1);});
        PatientMenuScene = new Scene(patientMenu, 500, 500);
        //Admin Menu
        /*adding Doctor 1s, Scheduling Appointment 1s, Managing inventory 6s,Edit 3s, view 2s*/
        Label AdminOption = new Label("Choose Your Option");
        Button Adminb1 = new Button("Add Doctor");
        Button Adminb2 = new Button("Schedule Appointment");
        Button Adminb3 = new Button("Manage Inventory");
        Button Adminb4 = new Button("Edit Patient");
        Button Adminb5 = new Button("View patients");
        Button Adminb6 = new Button("Logout");
        GridPane AdminMenu = new GridPane();
        AdminMenu.add(AdminOption, 0, 0);  // (element, column, row, column span, row span)
        AdminMenu.add(Adminb1, 0, 1);
        AdminMenu.add(Adminb2, 0, 2);
        AdminMenu.add(Adminb3, 0, 3);
        AdminMenu.add(Adminb4, 0, 4);
        AdminMenu.add(Adminb5, 0, 5);
        AdminMenu.add(Adminb6, 0, 6);

        AdminMenu.setAlignment(Pos.BASELINE_CENTER);
        AdminMenu.setVgap(35);
        Adminb6.setOnAction(e->{stage.setScene(s1);});
        AdminMenuScene = new Scene(AdminMenu, 500, 500);


        //Showing the primary Scene
        stage.setScene(s1);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }

    //method to intitalize all the users and beds and inventory will not be used after file handling
    public void InitializeUsers() {
        allUsers = new ArrayList<>(100);
        allBeds = new Bed[50];
        // initializing all beds with bed number
        for (int i = 0; i < allBeds.length; i++) {
            allBeds[i] = new Bed(i + 1);
        }
        // creating an admin user
        User Admin = new User();
        Admin.RegisterUser("Administrator", "Admin234", "A-1");
        allUsers.add(Admin);
        // adding hardcoded doctors to arraylist
        allUsers.add(new Doctor("Doc1", "Doc123", "D-1", "TypeA"));
        allUsers.add(new Doctor("Doc2", "Doc456", "D-2", "TypeB"));
        allUsers.add(new Doctor("Doc3", "Doc789", "D-3", "TypeC"));
        allUsers.add(new Doctor("Doc4", "DocABC", "D-4", "TypeD"));
        allUsers.add(new Doctor("Doc5", "DocDEF", "D-5", "TypeE"));
        // adding hardcoded patients
        allUsers.add(new Patient("Patient1", "P-1", "Patient123", 25, "Address1"));
        allUsers.add(new Patient("Patient2", "P-2", "Patient456", 30, "Address2"));
        allUsers.add(new Patient("Patient3", "P-3", "Patient789", 22, "Address3"));
        allUsers.add(new Patient("Patient4", "P-4", "PatientABC", 40, "Address4"));
        allUsers.add(new Patient("Patient5", "P-5", "PatientDEF", 28, "Address5"));

        GUIinv = new Inventory();
        // hardcoded items
        GUIinv.addHardcoded(new Item("Item1", "Manufacturer1", 10, "I-1", 200));
        GUIinv.addHardcoded(new Item("Item2", "Manufacturer2", 20, "I-2", 300));
        GUIinv.addHardcoded(new Item("Item3", "Manufacturer3", 30, "I-3", 350));
    }

    private int searchUsers(String ID) {
        for (int index = 0; index < allUsers.size(); index++) {
            User currentUser = allUsers.get(index);
            String currentUserID = currentUser.getID();

            if (ID.equals(currentUserID)) {
                return index;
            }
        }
        return -1;
    }

    public User loginUser(String ID, String PassWord) {
        for (User user : allUsers) {
            if (user.login(ID, PassWord)) {
                System.out.println("Logged in Successfully: ");
                return user;
            }
        }
        return null;
    }
}
