package com.example.hospital_management_system;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;

//Testing Push
public class HelloApplication extends Application {
    ArrayList<User> allUsers;
    Bed allBeds[];
    Inventory GUIinv;
    Scene s1, RegisterScene, DoctorMenuScene, PatientMenuScene, AdminMenuScene, s6, s7, s8, s9;
    boolean isClicked = false;

    @Override
    public void start(Stage stage) throws Exception {
        InitializeUsers();
        ObservableList<Patient> patientsList = FXCollections.observableArrayList(addPatientstoObserve());
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
                try {
                    CurrentUser[0] = loginUser(loginField.getText(), loginPass.getText());
                    if (CurrentUser[0] == null) {
                        throw new InvalidLoginException();
                    } else if (CurrentUser[0] instanceof Doctor) {
                        stage.setScene(DoctorMenuScene);
                    } else if (CurrentUser[0] instanceof Patient) {
                        stage.setScene(PatientMenuScene);
                    } else if (CurrentUser[0].getID().equals("A-1")) {
                        stage.setScene(AdminMenuScene);
                    }

                } catch (InvalidLoginException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Attempt");
                    alert.setContentText("Either Password or ID is incorrect");
                    alert.show();
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
        Label Registerl5 = new Label("Age");
        TextField Registername = new TextField();
        TextField password = new TextField();
        TextField id = new TextField();
        TextField address = new TextField();
        TextField patientAge = new TextField();
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
        registerUserPane.add(patientAge, 1, 4);
        registerUserPane.add(Registerb1, 1, 5);
        registerUserPane.add(Registergoback, 1, 6);
        registerUserPane.setAlignment(Pos.BASELINE_CENTER);
        registerUserPane.setVgap(35);
        RegisterScene = new Scene(registerUserPane, 500, 500);
        Registergoback.setOnAction(e -> {
            stage.setScene(s1);
        });

        Registerb1.setOnAction(e -> {
            try {
                if (Registername.getText().isEmpty() ||
                        password.getText().isEmpty() ||
                        id.getText().isEmpty() ||
                        address.getText().isEmpty() ||
                        patientAge.getText().isEmpty()) {
                    throw new InvalidRegistrationException();

                }
                String username = Registername.getText();
                String userID = id.getText();
                String userPassword = password.getText();
                String ageText = patientAge.getText();
                if (!ageText.matches("\\d+")) {
                    throw new InvalidAgeException();
                }
                int age = Integer.parseInt(patientAge.getText()); // Assuming age is an integer
                if (age < 0 || age > 150)
                    throw new InvalidAgeException();
                String userAddress = address.getText();
                Patient p = new Patient(username, userID, userPassword, age, userAddress);
                patientsList.add(p);
                stage.setScene(PatientMenuScene);
            } catch (InvalidRegistrationException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Attempt");
                alert.setContentText("Fields Not Complete");
                alert.show();
            } catch (InvalidAgeException ab) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Attempt");
                alert.setContentText("Age can not be less than 0 or greater than 150 and must be number");
                alert.show();
            }
        });
        //Doctor Menu
        /* Futher Scenes for doctor menu are writing prescriptions 1s, Managing Appointments 3s,Updating Patient 1s*/
        Label Options = new Label("Choose Your Option");
        Button doctorMenu1 = new Button("View Patients");
        Button doctorMenu3 = new Button("Manage Appointments");
        Button doctorMenu4 = new Button("Update Patient Data");
        Button doctorMenu5 = new Button("LogOut");
        GridPane dMenuPane = new GridPane();
        dMenuPane.add(Options, 0, 0);
        dMenuPane.add(doctorMenu1, 0, 1);
        dMenuPane.add(doctorMenu3, 0, 2);
        dMenuPane.add(doctorMenu5, 0, 3);
        dMenuPane.setAlignment(Pos.BASELINE_CENTER);
        dMenuPane.setVgap(30);
        //Button 1
        ObservableList<Patient> docPatientslist = FXCollections.observableArrayList();
        if (CurrentUser[0] != null)
            docPatientslist.addAll(((Doctor) CurrentUser[0]).getPatients());
        else {
            System.out.println("No patients : ");
        }
        TableView<Patient> docPatientsView = new TableView<>();
        TableColumn<Patient, String> docPatientID = new TableColumn<>("ID");
        docPatientID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Patient, String> docPatientName = new TableColumn<>("Name");
        docPatientName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<Patient, String> docPatientAddress = new TableColumn<>("Address");
        docPatientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Patient, Integer> docPatientAge = new TableColumn<>("Age");
        docPatientAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        docPatientsView.getColumns().addAll(docPatientID, docPatientAge, docPatientAddress);
        docPatientsView.setItems(docPatientslist);
        docPatientsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button docPatientsViewb1 = new Button("Back");
        Button docPatientsViewb2 = new Button("Write Prescirption");
        Button docPatientsViewb3 = new Button("Medical Record");
        Button docPatientsViewb4 = new Button("View Medical Record");
        HBox docPatientsViewHbox = new HBox(docPatientsViewb1, docPatientsViewb2, docPatientsViewb3);
        VBox docPatientsViewVbox = new VBox(docPatientsView, docPatientsViewHbox);
        Scene docPatientViewScene = new Scene(docPatientsViewVbox, 500, 500);
        Label docPrescriptionl1 = new Label("Patient:");
        Label docPrescriptionl2 = new Label("");
        Label docPrescriptionl3 = new Label("Date:");
        Button docPrescriptionb1 = new Button("Submit");
        TextField docPrescriptionT1 = new TextField();
        TextField docPrescriptionT2 = new TextField();
        GridPane docPrescriptionpane = new GridPane();
        docPrescriptionpane.add(docPrescriptionl1, 0, 0);
        docPrescriptionpane.add(docPrescriptionl2, 1, 0);
        docPrescriptionpane.add(docPrescriptionl3, 0, 1);
        docPrescriptionpane.add(docPrescriptionT1, 1, 1);
        docPrescriptionpane.add(docPrescriptionT2, 0, 2);
        docPrescriptionpane.add(docPrescriptionb1, 0, 3);
        Scene writePrescriptionScene = new Scene(docPrescriptionpane, 500, 500);
        doctorMenu1.setOnAction(e ->
        {
            updatePatientList(docPatientslist, CurrentUser);
            stage.setScene(docPatientViewScene);
            docPatientsView.refresh();
            docPatientsViewb2.setOnAction(a -> {
                try {
                    if (isClicked)
                        throw new ButtonClickedException();
                    if (docPatientsView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();

                    docPrescriptionl2.setText(docPatientsView.getSelectionModel().getSelectedItem().toString());
                    stage.setScene(writePrescriptionScene);
                    docPrescriptionb1.setOnAction(b -> {try {
                            if(docPrescriptionT1.getText().isEmpty()||docPrescriptionT2.getText().isEmpty())
                                throw new EmptyFieldException();
                        stage.setScene(docPatientViewScene);
                        if (CurrentUser[0] != null && CurrentUser[0] instanceof Doctor) {
                            Doctor doctor = (Doctor) CurrentUser[0];

                                Prescription p = new Prescription(doctor, docPatientsView.getSelectionModel().getSelectedItem(), docPrescriptionT2.getText(), docPrescriptionT1.getText());
                                docPatientsView.getSelectionModel().getSelectedItem().getRecord().addPrescription(p);
                                docPrescriptionT2.clear();
                                docPrescriptionT1.clear();
                                stage.setScene(docPatientViewScene);
                        } else {
                            // Handle the case when CurrentUser[0] is null or not an instance of Doctor
                            System.out.println("Error: Current user is not a Doctor.");
                        }
                    }catch (EmptyFieldException exp1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("error");
                        alert.show();
                    }
                    });
                } catch (ButtonClickedException exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("error");
                    alert.show();
                } catch (NoOptionSelectedException exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("error");
                    alert.show();
                }
            });

        });
        docPatientsViewb1.setOnAction(e -> stage.setScene(DoctorMenuScene));
        TableView<Prescription> prestableView = new TableView<>();
        TableColumn<Prescription, String> prescriptionDate = new TableColumn<>("Date");
        TableColumn<Prescription, String> prescriptionDoc = new TableColumn<>("Doctor");
        prescriptionDoc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPractitioner().getName()));
        prescriptionDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrescriptiondate()));
        prestableView.getColumns().addAll(prescriptionDoc, prescriptionDate);
        prestableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        docPatientsViewb3.setOnAction(e -> {
                    try {
                        if (docPatientsView.getSelectionModel().isEmpty())
                            throw new NoOptionSelectedException();

                        Patient p = docPatientsView.getSelectionModel().getSelectedItem();
                        ObservableList<Prescription> observableList = FXCollections.observableArrayList(
                                p.getRecord().getPrescriptions()
                        );
                        prestableView.setItems(observableList);
                        Label presl1 = new Label("Notes");
                        Label presl2 = new Label(docPatientsView.getSelectionModel().getSelectedItem().getRecord().getNotes());
                        Button presb2 = new Button("Further Details");
                        Button presb = new Button("back");
                        HBox presh = new HBox(presl1, presl2);
                        HBox presh2 = new HBox(presb, presb2);
                        VBox presv = new VBox(presh, prestableView, presh2);
                        Scene press = new Scene(presv, 500, 500);
                        stage.setScene(press);

                        presb.setOnAction(a -> stage.setScene(docPatientViewScene));
                        presb2.setOnAction(a -> {
                            try {
                                if (prestableView.getSelectionModel().isEmpty())
                                    throw new NoOptionSelectedException();

                                Prescription prescription = prestableView.getSelectionModel().getSelectedItem();
                                Label doctorLabel = new Label("Doctor Name: " + prescription.getPractitioner().getName());
                                Label patientLabel = new Label("Patient Name: " + prescription.getPrescribedto().getName());
                                Label prescriptionDateLabel = new Label("Prescription Date: " + prescription.getPrescriptiondate());
                                Label medicinesLabel = new Label("Medicines: " + prescription.getMedicines());
                                Button Db = new Button("Back");
                                // Create a layout to organize labels
                                VBox root = new VBox(doctorLabel, patientLabel, prescriptionDateLabel, medicinesLabel, Db);
                                Db.setOnAction(d -> stage.setScene(press));
                                // Create a scene and set it on the stage
                                Scene scene = new Scene(root, 400, 200);
                                stage.setScene(scene);
                            }
                            catch (NoOptionSelectedException exception){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setContentText("error");
                                alert.show();
                            }
                        });
                    }catch (NoOptionSelectedException exp){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("error");
                        alert.show();
                    }
                }
        );

        doctorMenu3.setOnAction(e -> {
            ObservableList<Appointment> observableList = FXCollections.observableArrayList(
                    ((Doctor) CurrentUser[0]).getAppointments()
            );
            TableView<Appointment> appointmentTableView = new TableView<>();
            TableColumn<Appointment, String> appointmentIDCol = new TableColumn<>("Appointment ID");
            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
            TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
            patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentPatient().getName()));
            TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
            doctorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentDoctor().getName()));
            TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentDate"));
            TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
            timeCol.setCellValueFactory(new PropertyValueFactory<>("Appointmenttime"));
            TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
            appointmentTableView.getColumns().addAll(appointmentIDCol, patientCol, doctorCol, dateCol, timeCol, statusCol);
            appointmentTableView.setItems(observableList);
            appointmentTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            Button b1 = new Button("Go back");
            Button b2 = new Button("Cancel");
            Button b3 = new Button("ChangeTime");
            HBox h = new HBox(b1, b2, b3);
            VBox v = new VBox(appointmentTableView, h);
            b2.setOnAction(a -> {
                try {
                    if (appointmentTableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();

                    appointmentTableView.getSelectionModel().getSelectedItem().setStatus("CANCELLED");
                    appointmentTableView.refresh();
                }catch (NoOptionSelectedException exp){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please select options");
                    alert.show();
                }
            });
            b3.setOnAction(a ->
            {
                try {
                    if (appointmentTableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();

                    GridPane g = new GridPane();
                    Label dateLabel = new Label("Date:");
                    TextField dateTextField = new TextField();
                    Label timeLabel = new Label("Time:");
                    TextField timeTextField = new TextField();
                    g.add(dateLabel, 0, 0);
                    g.add(dateTextField, 1, 0);
                    g.add(timeLabel, 0, 1);
                    g.add(timeTextField, 1, 1);
                    Button button = new Button("Submit");
                    g.add(button, 0, 2);
                    v.getChildren().add(g);

                    button.setOnAction(b -> {
                        try {
                            if (dateTextField.getText().isEmpty() || timeTextField.getText().isEmpty())
                                throw new EmptyFieldException();
                            appointmentTableView.getSelectionModel().getSelectedItem().setAppointmentDate(dateLabel.getText());
                            appointmentTableView.getSelectionModel().getSelectedItem().setAppointmentDate(dateLabel.getText());
                            v.getChildren().remove(g);
                            appointmentTableView.refresh();
                        }catch (EmptyFieldException exception) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("error");
                            alert.show();
                        }
                    });
                }catch (NoOptionSelectedException exp){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please select options");
                    alert.show();
                }
            });

            Scene scene = new Scene(v, 500, 500);
            stage.setScene(scene);
            b1.setOnAction(a -> stage.setScene(DoctorMenuScene));
        });
        doctorMenu5.setOnAction(e -> {
            Alert patientAlert = new Alert(Alert.AlertType.CONFIRMATION);
            patientAlert.setTitle("CONFIRMATION");
            patientAlert.setContentText("DO U WANT TO LOGOUT");
            Optional<ButtonType> result = patientAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.setScene(s1);
            }
        });
        DoctorMenuScene = new Scene(dMenuPane, 500, 500);
                                  ///////////////////////////////////////////
        //Patient Menu
        /*CurrentInfo 1s, MedicalRecord 3s,GenerateInvoice 1s, Medicine 2s, ViewDoctors 1s, AssignDoctor 1s*/
        Label patientOptions = new Label("Choose Your Options");
        Button patientb1 = new Button("View Current Info");
        Button patientb2 = new Button("View Medical Record");
        Button patientb3 = new Button("Buy Medicine");
        Button patientb4 = new Button("Generate Invoice");
        Button patientb5 = new Button("View Doctors");
        Button patientb7 = new Button("Log out");
        GridPane patientMenu = new GridPane();
        patientMenu.add(patientOptions, 0, 0);
        patientMenu.add(patientb1, 0, 1);
        patientMenu.add(patientb2, 0, 2);
        patientMenu.add(patientb3, 0, 3);
        patientMenu.add(patientb4, 0, 4);
        patientMenu.add(patientb5, 0, 5);
        patientMenu.add(patientb7, 0, 6);
        patientMenu.setAlignment(Pos.BASELINE_CENTER);
        patientMenu.setVgap(35);
        PatientMenuScene = new Scene(patientMenu, 500, 500);
        //Buttons for Patient Menu
        patientb1.setOnAction(e -> {//shows patient info
            searchUsers(login1.getText());
            Button patientCurrentInfoBtn = new Button("Back");
            Label patientcurrentinfol1 = new Label("Name of Patient");
            Label patientcurrentinfol2 = new Label("ID of Patient");
            Label patientcurrentinfol3 = new Label("Age of Patient");
            Label patientcurrentinfol4 = new Label("Address of Patient");
            Label patientcurrentinfol5 = new Label(CurrentUser[0].getName());
            Label patientcurrentinfol6 = new Label(CurrentUser[0].getID());
            Label patientcurrentinfol7 = new Label(Integer.toString(((Patient) CurrentUser[0]).getAge()));
            Label patientcurrentinfol8 = new Label(((Patient) CurrentUser[0]).getAddress());
            GridPane patientCurrentInfoGrid = new GridPane();
            patientCurrentInfoGrid.add(patientcurrentinfol1, 3, 5);
            patientCurrentInfoGrid.add(patientcurrentinfol2, 3, 6);
            patientCurrentInfoGrid.add(patientcurrentinfol3, 3, 7);
            patientCurrentInfoGrid.add(patientcurrentinfol4, 3, 8);
            patientCurrentInfoGrid.add(patientcurrentinfol5, 4, 5);
            patientCurrentInfoGrid.add(patientcurrentinfol6, 4, 6);
            patientCurrentInfoGrid.add(patientcurrentinfol7, 4, 7);
            patientCurrentInfoGrid.add(patientcurrentinfol8, 4, 8);
            patientCurrentInfoGrid.add(patientCurrentInfoBtn, 3, 10);
            patientCurrentInfoGrid.setHgap(10);
            patientCurrentInfoGrid.setVgap(10);
            patientCurrentInfoGrid.setAlignment(Pos.CENTER);
            Scene patientCurrentInfoScene = new Scene(patientCurrentInfoGrid, 500, 500);
            patientCurrentInfoBtn.setOnAction(a -> stage.setScene(PatientMenuScene));
            stage.setScene(patientCurrentInfoScene);
        });
        //Button2 medical Record Button
        ObservableList<Prescription> prescriptionsObserve = FXCollections.observableArrayList();

// Check if CurrentUser[0] is not null before accessing methods
        if (CurrentUser[0] != null) {
            prescriptionsObserve.addAll(((Patient) CurrentUser[0]).getRecord().getPrescriptions());
        } else {
            System.out.println("CurrentUser[0] or its record is null. Unable to retrieve prescriptions.");
        }
        TableView<Prescription> prescriptionTableView = createTableView(prescriptionsObserve);
        Button prescriptionBack = new Button("Back");
        VBox prescriptionVBox = new VBox(prescriptionTableView, prescriptionBack);
        Scene viewPrescriptionsScene = new Scene(prescriptionVBox, 500, 500);

        patientb2.setOnAction(e -> {
            prescriptionsObserve.clear();
            if (CurrentUser[0] != null)
                prescriptionsObserve.addAll(((Patient) CurrentUser[0]).getRecord().getPrescriptions());
            prescriptionsObserve.get(0).DisplayPrescription();
            stage.setScene(viewPrescriptionsScene);
            prescriptionTableView.refresh();
            prescriptionBack.setOnAction(a -> stage.setScene(PatientMenuScene));
        });
        //button 4
        Label patientBill = new Label("Due Amount");
        Label dueAmount = new Label();
        HBox patientBillHbox = new HBox();
        Button billGoback = new Button("Go back");
        patientBillHbox.getChildren().addAll(patientBill, dueAmount, billGoback);
        Scene patientBillScene = new Scene(patientBillHbox, 500, 500);
        patientb4.setOnAction(e -> {
            stage.setScene(patientBillScene);
            billGoback.setOnAction(a -> {
                stage.setScene(PatientMenuScene);
                dueAmount.setText("");
            });
            dueAmount.setText(Double.toString(((Patient) CurrentUser[0]).getBill().getDueamount()));
        });

        //button 5 doctors view
        ObservableList<Doctor> allDoctorsObsList = FXCollections.observableArrayList(addDocstoObserve());
        TableView<Doctor> allDoctorsview = new TableView<>();
        TableColumn<Doctor, String> doctorName = new TableColumn<>("Name");
        TableColumn<Doctor, String> doctorID = new TableColumn<>("ID");
        TableColumn<Doctor, String> doctorSpeialization = new TableColumn<>("Specialization");
        doctorName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        doctorID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        doctorSpeialization.setCellValueFactory(new PropertyValueFactory<>("Specialization"));
        allDoctorsview.getColumns().addAll(doctorName, doctorID, doctorSpeialization);
        allDoctorsview.setItems(allDoctorsObsList);
        allDoctorsview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox viewAllDocVbox = new VBox(allDoctorsview);
        Scene viewAllDocScene = new Scene(viewAllDocVbox, 500, 500);
        Button viewAllDocsb1 = new Button("Back");
        Button viewAllDocsb2 = new Button("Assign");
        HBox h = new HBox(viewAllDocsb1, viewAllDocsb2);
        viewAllDocVbox.getChildren().add(h);
        patientb5.setOnAction(e -> {
                viewAllDocsb1.setOnAction(a -> {
                    stage.setScene(PatientMenuScene);
                });
                viewAllDocsb2.setOnAction(a -> {
                    try {
                        if (allDoctorsview.getSelectionModel().isEmpty())
                            throw new NoOptionSelectedException();
                        ((Patient) CurrentUser[0]).setDoctorIncharge(allDoctorsview.getSelectionModel().getSelectedItem());
                        System.out.println(((Patient) CurrentUser[0]).getDoctorIncharge());
                        allDoctorsview.getSelectionModel().getSelectedItem().assignpatient(((Patient) CurrentUser[0]));
                    }catch (NoOptionSelectedException excption) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please select any Doctor");
                        alert.show();
                    }
                });
                stage.setScene(viewAllDocScene);
            });

        patientb7.setOnAction(e -> {
            Alert patientAlert = new Alert(Alert.AlertType.CONFIRMATION);
            patientAlert.setTitle("CONFIRMATION");
            patientAlert.setContentText("DO U WANT TO LOGOUT");
            Optional<ButtonType> result = patientAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.setScene(s1);
            }
        });

        //Patient Menu
        //Admin Menu
        /*adding Doctor 1s, Scheduling Appointment 1s, Managing inventory 6s, view 1s*/
        Label AdminOption = new Label("Choose Your Option");
        Button Adminb1 = new Button("Add Doctor");
        Button Adminb2 = new Button("Schedule Appointment");
        Button Adminb3 = new Button("Manage Inventory");
        Button Adminb5 = new Button("View patients");
        Button Adminb6 = new Button("Logout");
        GridPane AdminMenu = new GridPane();
        AdminMenu.add(AdminOption, 0, 0);  // (element, column, row, column span, row span)
        AdminMenu.add(Adminb1, 0, 1);
        AdminMenu.add(Adminb2, 0, 2);
        AdminMenu.add(Adminb3, 0, 3);
        AdminMenu.add(Adminb5, 0, 4);
        AdminMenu.add(Adminb6, 0, 5);
        AdminMenu.setAlignment(Pos.BASELINE_CENTER);
        AdminMenu.setVgap(35);
        Adminb6.setOnAction(e -> {
            Alert patientAlert = new Alert(Alert.AlertType.CONFIRMATION);
            patientAlert.setTitle("CONFIRMATION");
            patientAlert.setContentText("DO U WANT TO LOGOUT");
            Optional<ButtonType> result = patientAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stage.setScene(s1);
            }
        });
        //Adding Doctor Scene
        Label addDocl1 = new Label("Name");
        Label addDocl2 = new Label("ID");
        Label addDocl3 = new Label("Specialization");
        Label addDocl4 = new Label("Password");
        TextField addDocT1 = new TextField();
        TextField addDocT2 = new TextField();
        TextField addDocT3 = new TextField();
        TextField addDocT4 = new TextField();
        GridPane addDocPane = new GridPane();
        // Add elements to GridPane
        addDocPane.add(addDocl1, 0, 0);
        addDocPane.add(addDocl2, 0, 1);
        addDocPane.add(addDocl3, 0, 2);
        addDocPane.add(addDocl4, 0, 3);
        addDocPane.add(addDocT1, 1, 0);
        addDocPane.add(addDocT2, 1, 1);
        addDocPane.add(addDocT3, 1, 2);
        addDocPane.add(addDocT4, 1, 3);
        Button addDocb1 = new Button("Submit");
        Button addDocb2 = new Button("Go back");
        addDocPane.add(addDocb1, 2, 4);
        addDocPane.add(addDocb2, 0, 4);
        Scene addDocScene = new Scene(addDocPane, 500, 500);
        addDocb1.setOnAction(e -> {
            try {
                if (addDocT1.getText().isEmpty() || addDocT2.getText().isEmpty() || addDocT3.getText().isEmpty() || addDocT4.getText().isEmpty())
                    throw new EmptyFieldException();
                Doctor d = new Doctor(addDocl1.getText(), addDocl4.getText(), addDocl2.getText(), addDocl3.getText());
                allUsers.add(d);
                addDocT1.clear();
                addDocT2.clear();
                addDocT3.clear();
                addDocT4.clear();
            }catch (EmptyFieldException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fields are empty");
                alert.show();
            }
        });
        addDocb2.setOnAction(e -> {
            stage.setScene(AdminMenuScene);
        });
        Adminb1.setOnAction(e -> stage.setScene(addDocScene));
        //Schedule Appointment Scene
        Button ScheduleAppointmentb2 = new Button("back");
        Button ScheduleAppointmentb1 = new Button("Submit");
        Label ScheduleAppointmentl1 = new Label("Doctor ID:");
        Label ScheduleAppointmentl2 = new Label("Patient ID");
        Label ScheduleAppointmentl3 = new Label("Date");
        Label ScheduleAppointmentl4 = new Label("Time");
        TextField ScheduleAppointmentT1 = new TextField();
        TextField ScheduleAppointmentT2 = new TextField();
        TextField ScheduleAppointmentT3 = new TextField();
        TextField ScheduleAppointmentT4 = new TextField();
        GridPane ScheduleAppointmentpane = new GridPane();
        ScheduleAppointmentpane.add(ScheduleAppointmentl1, 0, 0);
        ScheduleAppointmentpane.add(ScheduleAppointmentT1, 1, 0);
        ScheduleAppointmentpane.add(ScheduleAppointmentl2, 0, 1);
        ScheduleAppointmentpane.add(ScheduleAppointmentT2, 1, 1);
        ScheduleAppointmentpane.add(ScheduleAppointmentl3, 0, 2);
        ScheduleAppointmentpane.add(ScheduleAppointmentT3, 1, 2);
        ScheduleAppointmentpane.add(ScheduleAppointmentl4, 0, 3);
        ScheduleAppointmentpane.add(ScheduleAppointmentT4, 1, 3);
        ScheduleAppointmentpane.add(ScheduleAppointmentb1, 2, 4);
        ScheduleAppointmentpane.add(ScheduleAppointmentb2, 0, 4);
        ScheduleAppointmentb2.setOnAction(e -> stage.setScene(AdminMenuScene));
        Scene ScheduleapScene = new Scene(ScheduleAppointmentpane, 500, 500);
        Adminb2.setOnAction(e -> {
            stage.setScene(ScheduleapScene);
        });
        ScheduleAppointmentb1.setOnAction(e -> {
            Patient p = null;
            Doctor d = null;
            if (searchUsers(ScheduleAppointmentT1.getText()) != -1) {
                d = ((Doctor) allUsers.get(searchUsers(ScheduleAppointmentT1.getText())));
            } else {
                System.out.println("incorrect Id");
            }
            if (searchUsers(ScheduleAppointmentT2.getText()) != -1) {
                p = ((Patient) allUsers.get(searchUsers(ScheduleAppointmentT2.getText())));
            } else {
                System.out.println("Incorrect ID");
            }
            Appointment ap = new Appointment(p, d, ScheduleAppointmentT3.getText(), ScheduleAppointmentT4.getText());
            ap.setStatus("PENDING");
            d.addAppointment(ap);
            ScheduleAppointmentT1.clear();
            ScheduleAppointmentT2.clear();
            ScheduleAppointmentT3.clear();
            ScheduleAppointmentT4.clear();
        });
        //schedule Appointment Scene

        //View and Edit Patients Scene

        //Buttons
        Button viewAllPatientsb1 = new Button("Go back");
        Button viewAllPatientsb2 = new Button("Edit Patient");
        Button viewAllPatientsb3 = new Button("Appoint bed");
        Button viewAllPatientsb4 = new Button("Free Bed");
        HBox viewAllPatientsHbox = new HBox(viewAllPatientsb1, viewAllPatientsb2, viewAllPatientsb3, viewAllPatientsb4);
        //Observable arraylist and table view
        TableView<Patient> patientTable = new TableView<>();
        //table Column for each attribute
        TableColumn<Patient, String> patientIdColumn = new TableColumn<>("ID");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Patient, String> patientNameColumn = new TableColumn<>("Name");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<Patient, String> patientAddressColumn = new TableColumn<>("Address");
        patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Patient, Integer> patientAgeColumn = new TableColumn<>("Age");
        patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        TableColumn<Patient, String> patientInchargeColumn = new TableColumn<>("Incharge");
        //for doctor incharge
        patientInchargeColumn.setCellValueFactory(data -> {
            if (data.getValue().getDoctorIncharge() == null)
                return new SimpleStringProperty("NA");
            return new SimpleStringProperty(
                    data.getValue().getDoctorIncharge().getName());
        });
        TableColumn<Patient, String> viewAllPatientsBed = new TableColumn<>("Bed");
        viewAllPatientsBed.setCellValueFactory(data -> {
            if (data.getValue().getBedUsed() != null)
                return new SimpleStringProperty("T");
            return new SimpleStringProperty("F");
        });
        patientTable.getColumns().addAll(patientIdColumn, patientNameColumn, patientAgeColumn, patientAddressColumn, patientInchargeColumn
                , viewAllPatientsBed);
        patientTable.setItems(patientsList);
        patientTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //creating Vbox
        VBox viewAllPatientsVbox = new VBox(patientTable, viewAllPatientsHbox);
        //creating the scene for the table
        Scene viewAllPatient = new Scene(viewAllPatientsVbox, 500, 500);
        System.out.println("Before: " + patientsList.size());
// Add or remove patients here

        //setting actions on button
        Adminb5.setOnAction(e -> {
            System.out.println("After: " + patientsList.size());
            patientTable.refresh();
            stage.setScene(viewAllPatient);
        });
        viewAllPatientsb1.setOnAction(e -> stage.setScene(AdminMenuScene));
        viewAllPatientsb2.setOnAction(e -> {
            try {
                if (patientTable.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();

                Label l1 = new Label("Name");
                Label l3 = new Label("Address");
                Label l4 = new Label("Age");
                TextField t1 = new TextField();
                TextField t3 = new TextField();
                TextField t4 = new TextField();
                GridPane gridPane = new GridPane();
                gridPane.add(l1, 0, 0);
                gridPane.add(t1, 1, 0);
                gridPane.add(l3, 0, 1);
                gridPane.add(t3, 1, 1);
                gridPane.add(l4, 0, 2);
                gridPane.add(t4, 1, 2);
                viewAllPatientsVbox.getChildren().add(gridPane);
                Patient p = patientTable.getSelectionModel().getSelectedItem();
                Button b = new Button("Submit");
                gridPane.add(b, 2, 2);
                b.setOnAction(a -> {
                    try {
                        if(t1.getText().isEmpty()||t3.getText().isEmpty()||t4.getText().isEmpty())
                            throw new EmptyFieldException();
                        if(Integer.parseInt(t4.getText())<0 || Integer.parseInt(t4.getText())>150)
                            throw new InvalidAgeException();
                        p.setGuiPatient(t1.getText(), Integer.parseInt(t4.getText()), t3.getText());
                        viewAllPatientsVbox.getChildren().remove(gridPane);
                        patientTable.refresh();
                    }catch (EmptyFieldException exception) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please fill all fields");
                        alert.show();
                    }catch (InvalidAgeException ab) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Attempt");
                        alert.setContentText("Age can not be less than 0 or greater than 150 and must be number");
                        alert.show();
                    }
                }
                );
            }catch (NoOptionSelectedException excption) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select any patient");
                alert.show();
            }
        });
        viewAllPatientsb3.setOnAction(e -> {
            try {
                if (patientTable.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                Patient p = patientTable.getSelectionModel().getSelectedItem();
                Label l1 = new Label("Bed number");
                Label l2 = new Label("days occupied");
                TextField t1 = new TextField();
                TextField t2 = new TextField();
                Button submit = new Button("Submit");
                GridPane bedGridPane = new GridPane();
                bedGridPane.add(l1, 0, 0);
                bedGridPane.add(t1, 1, 0);
                bedGridPane.add(l2, 0, 1);
                bedGridPane.add(t2, 1, 1);
                bedGridPane.add(submit, 2, 1);
                viewAllPatientsVbox.getChildren().add(bedGridPane);
                submit.setOnAction(a -> {
                    Bed bed = new Bed();
                    Bed[] beds = new Bed[10];
                    try {
                        if(t1.getText().isEmpty()||t2.getText().isEmpty())
                        throw new EmptyFieldException();
//                            if (beds[Integer.parseInt(t1.getText()) - 1].isOccupied()) {  // Assuming bed numbers start from 1
//                                Alert alert = new Alert(Alert.AlertType.ERROR);
//                                alert.setTitle("Error");
//                                alert.setContentText("Bed already occupied");
//                                alert.show();
//                            }
                        p.setBedUsed(allBeds[Integer.parseInt(t1.getText()) - 1]);
                        p.getBedUsed().setDaysOccupied(Integer.parseInt(t2.getText()));
                        viewAllPatientsVbox.getChildren().remove(bedGridPane);
                        patientTable.refresh();
                    }catch (EmptyFieldException exception) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please fill all fields");
                        alert.show();
                    }
                });
            }catch (NoOptionSelectedException excption) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select any patient");
                alert.show();
            }
        });
        viewAllPatientsb4.setOnAction(e -> {
                    try {
                        if (patientTable.getSelectionModel().isEmpty())
                            throw new NoOptionSelectedException();
                        Patient p = patientTable.getSelectionModel().getSelectedItem();
                        if (p.getBedUsed() != null) {
                            p.getBill().finalPayment(p);
                            p.getBedUsed().vacantBed();
                            p.setBedUsed(null);
                        }
                        patientTable.refresh();
                    } catch (NoOptionSelectedException excption) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please select any patient");
                        alert.show();
                    }
                }
        );
        //view and edit Patients Scene


        //Inventory Management Scene //Complete For now
        // buttons for scene
        Button Invob2 = new Button("Update Quantity");
        Button Invob3 = new Button("Update Price");
        Button Invob4 = new Button("Remove");
        Button Invob5 = new Button("Add Item");
        //Creating and setting action for inventory scene
        ObservableList<Item> Invlist1 = FXCollections.observableArrayList(GUIinv.getItemsinInventory());
        TableView<Item> tableView = new TableView<>(Invlist1);
        // Create TableColumn for each attribute
        TableColumn<Item, String> invoidColumn = new TableColumn<>("Item ID");
        invoidColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        TableColumn<Item, String> invonameColumn = new TableColumn<>("Item Name");
        invonameColumn.setCellValueFactory(new PropertyValueFactory<>("Itemname"));
        TableColumn<Item, String> invoMenufacturerColumn = new TableColumn<>("Manufacturer");
        invoMenufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        TableColumn<Item, Integer> invoquantityColumn = new TableColumn<>("Quantity");
        invoquantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        // setting the selection mode of the the table view as single
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Add columns to TableView
        Button GobackFromInvtable = new Button("GO back");
        GobackFromInvtable.setOnAction(e -> stage.setScene(AdminMenuScene));
        tableView.getColumns().addAll(invoidColumn, invonameColumn, invoMenufacturerColumn, invoquantityColumn, priceColumn);
        HBox invoOptions = new HBox(GobackFromInvtable, Invob2, Invob3, Invob4, Invob5);
        VBox InvoVerticalBox = new VBox(tableView, invoOptions);
        Scene InvoScene2 = new Scene(InvoVerticalBox, 500, 500);
        AdminMenuScene = new Scene(AdminMenu, 500, 500);
        //setting functionality on all the buttons of the scene
        Adminb3.setOnAction(e -> stage.setScene(InvoScene2));
        Invob2.setOnAction(e -> {
            try {
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                Label l1 = new Label("New Quantity");
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                HBox h1 = new HBox(l1, T1, b1);
                InvoVerticalBox.getChildren().add(h1);
                b1.setOnAction(a -> {
                    try {
                        if(T1.getText().isEmpty())
                            throw new EmptyFieldException();
                        int i = Invlist1.indexOf(tableView.getSelectionModel().getSelectedItem());
                        Invlist1.get(i).setQuantity(Integer.parseInt(T1.getText()));
                        InvoVerticalBox.getChildren().remove(h1);
                        tableView.refresh();
                    }catch (EmptyFieldException exception) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please fill the textField");
                        alert.show();
                    }
                });
            }catch (NoOptionSelectedException excption) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Select an item");
                alert.show();
            }
        });
        Invob3.setOnAction(e -> {
            try {
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                Label l1 = new Label("New Price");
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                HBox h1 = new HBox(l1, T1, b1);
                InvoVerticalBox.getChildren().add(h1);
                b1.setOnAction(a -> {
                    try {
                        if (T1.getText().isEmpty())
                            throw new EmptyFieldException();
                        if(Integer.parseInt(T1.getText())<0)
                            throw new NegativePriceException();
                        int i = Invlist1.indexOf(tableView.getSelectionModel().getSelectedItem());
                        Invlist1.get(i).setPrice(Double.parseDouble(T1.getText()));
                        InvoVerticalBox.getChildren().remove(h1);
                        tableView.refresh();
                    }catch (EmptyFieldException exception) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please fill the textField");
                        alert.show();
                    }catch(NegativePriceException exp2){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please set price greater than 0");
                        alert.show();
                    }
                });
            }catch (NoOptionSelectedException excption) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Select an item");
                alert.show();
            }
        });
        Invob4.setOnAction(e -> {
            try {
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                Item selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null)
                    // Remove the selected item from the list
                    Invlist1.remove(selected);
                // Refresh the TableView
                tableView.refresh();
            }catch (NoOptionSelectedException excption) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Select an item");
                alert.show();
            }
        });
        Invob5.setOnAction(e -> {
            Label l1 = new Label("ID: ");
            Label l2 = new Label("Name: ");
            Label l3 = new Label("Manufacturer: ");
            Label l4 = new Label("Quantity: ");
            Label l5 = new Label("Price: ");
            // Create TextFields for user input
            TextField t1 = new TextField();
            TextField t2 = new TextField();
            TextField t3 = new TextField();
            TextField t4 = new TextField();
            TextField t5 = new TextField();
            // Assuming you have a GridPane named "gridPane"
            GridPane gridPane = new GridPane();
            gridPane.add(l1, 0, 0); // ID Label
            gridPane.add(t1, 1, 0); // ID TextField
            gridPane.add(l2, 0, 1); // Name Label
            gridPane.add(t2, 1, 1); // Name TextField
            gridPane.add(l3, 0, 2); // Manufacturer Label
            gridPane.add(t3, 1, 2); // Manufacturer TextField
            gridPane.add(l4, 0, 3); // Quantity Label
            gridPane.add(t4, 1, 3); // Quantity TextField
            gridPane.add(l5, 0, 4); // Price Label
            gridPane.add(t5, 1, 4); // Price TextField
            InvoVerticalBox.getChildren().add(gridPane);
            Button b = new Button("Submit");
            gridPane.add(b, 2, 4);
            b.setOnAction(a -> {
                try {
                    if (t1.getText().isEmpty()||t2.getText().isEmpty()||t3.getText().isEmpty()||t4.getText().isEmpty())
                        throw new EmptyFieldException();
                    Item i = new Item(t2.getText(), t3.getText(), Integer.parseInt(t4.getText()), t1.getText(), Double.parseDouble(t5.getText()));
                    Invlist1.add(i);
                    InvoVerticalBox.getChildren().remove(gridPane);
                    tableView.refresh();
                }catch (EmptyFieldException exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please fill the textField");
                    alert.show();
                }
            });
        });
        //Inventory Button from Patient
        patientb3.setOnAction(e -> {
            Button b1 = new Button("Buy");
            Button b2 = new Button("Back");
            GridPane gridPane = new GridPane();
            gridPane.add(b1, 0, 0);
            gridPane.add(b2, 2, 0);
            TextField t1 = new TextField();
            gridPane.add(t1, 1, 0);
            VBox v = new VBox(tableView, gridPane);
            Scene patientInventory = new Scene(v);
            stage.setScene(patientInventory);
            b2.setOnAction(a -> stage.setScene(PatientMenuScene));
            b1.setOnAction(a -> {
                try {
                    if (tableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
//                    if(Integer.parseInt(t1.getText())> ){
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Error");
//                        alert.setContentText("We don't have that much quantity");
//                        alert.show();
//                    }
                    Item i = tableView.getSelectionModel().getSelectedItem();
                    double total = i.getTotal(Integer.parseInt(t1.getText()));
                    ((Patient) CurrentUser[0]).UpdateBilling(total);
                    tableView.refresh();
                    t1.clear();
                }catch (NoOptionSelectedException excption) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please select an item");
                    alert.show();
                }
            }
            );
        });
        //inventory management Scene complete
        //Showing the primary Scene
        stage.setScene(s1);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    private void updatePatientList(ObservableList<Patient> a, User b[]) {
        a.clear(); // Clear existing data
        if (b[0] != null) {
            a.addAll(((Doctor) b[0]).getPatients());
        }
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
        allUsers.add(new Doctor("Doc1", "", "D-1", "TypeA"));
        allUsers.add(new Doctor("Doc2", "Doc456", "D-2", "TypeB"));
        allUsers.add(new Doctor("Doc3", "Doc789", "D-3", "TypeC"));
        allUsers.add(new Doctor("Doc4", "DocABC", "D-4", "TypeD"));
        allUsers.add(new Doctor("Doc5", "DocDEF", "D-5", "TypeE"));
        // adding hardcoded patients
        allUsers.add(new Patient("Patient1", "P-1", "", 25, "Address1"));
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

    private ArrayList<Patient> addPatientstoObserve() {
        ArrayList<Patient> arr = new ArrayList<>();
        for (User user : allUsers) {
            if (user instanceof Patient)
                arr.add((Patient) user);
        }
        return arr;
    }

    private ArrayList<Doctor> addDocstoObserve() {
        ArrayList<Doctor> arr = new ArrayList<>();
        for (User user : allUsers) {
            if (user instanceof Doctor)
                arr.add((Doctor) user);
        }
        return arr;
    }

    private TableView<Prescription> createTableView(ObservableList<Prescription> arr) {
        TableView<Prescription> table = new TableView<>();
        TableColumn<Prescription, String> prescriptionDate = new TableColumn<>("Date");
        TableColumn<Prescription, String> prescriptionDoc = new TableColumn<>("Doctor");
        prescriptionDoc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPractitioner().getName()));
        prescriptionDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrescriptiondate()));
        table.getColumns().addAll(prescriptionDoc, prescriptionDate);
        table.setItems(arr);
        return table;
    }

}
