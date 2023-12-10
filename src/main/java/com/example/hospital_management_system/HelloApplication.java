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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class HelloApplication extends Application {
    ArrayList<User> allUsers;
    Bed allBeds[];
    Inventory GUIinv;
    Scene s1, RegisterScene, DoctorMenuScene, PatientMenuScene, AdminMenuScene;
    boolean isClicked = false;
    int patientNum = 0, doctorNum = 0, appointmentNum = 0;


    @Override
    public void start(Stage stage) throws Exception {
        InitializeUsers();
        ObservableList<Patient> patientsList = FXCollections.observableArrayList(addPatientstoObserve());
        final User[] CurrentUser = {null};                        //Stores the current User of the System
        stage.setTitle("Hospital Management System : ");
        //Login user Section

        //buttons for logging in user
        Button loginB1 = new Button("Login");
        setStyling(loginB1, 90, 35);
        Button loginb2 = new Button("Register");
        setStyling(loginb2, 90, 35);
        //labels  and text fields
        Label loginl1 = new Label("Enter UserName : ");
        Label loginl2 = new Label("Enter Password : ");
        TextField loginField = new TextField();
        PasswordField loginPass = new PasswordField();
        GridPane loginPane = new GridPane();//GridPane
        loginPane.add(loginl1, 3, 3);
        loginPane.add(loginl2, 3, 4);
        loginPane.add(loginField, 4, 3);
        loginPane.add(loginPass, 4, 4);
        loginPane.add(loginB1, 4, 5);
        loginPane.add(loginb2, 3, 5);
        loginPane.setAlignment(Pos.BASELINE_CENTER);//setting alignment
        loginPane.setVgap(35);//setting vertical gap
        s1 = new Scene(loginPane, 500, 500); //creating the first scene of the System
        loginB1.setOnAction(new EventHandler<ActionEvent>() {//this button logs in the user
            @Override
            public void handle(ActionEvent event) {
                try {
                    CurrentUser[0] = loginUser(loginField.getText(), loginPass.getText());
                    if (CurrentUser[0] == null) {
                        throw new InvalidLoginException();//exception thrown if the attempt is invalid
                    } else if (CurrentUser[0] instanceof Doctor) {
                        stage.setScene(DoctorMenuScene);
                    } else if (CurrentUser[0] instanceof Patient) {
                        stage.setScene(PatientMenuScene);
                    } else if (CurrentUser[0].getID().equals("A-1")) {
                        stage.setScene(AdminMenuScene);
                    }
                } catch (InvalidLoginException e) {
                    throwAlert("Either Password or ID is incorrect");
                }
            }
        });
        loginb2.setOnAction(e -> stage.setScene(RegisterScene));//b2 sets the scene to RegisterScene
        //Register user Section
        GridPane registerPane = new GridPane();
        Button Registerb1 = new Button("Enter");
        setStyling(Registerb1, 90, 27);
        Button registerBack = new Button("Go back");
        setStyling(registerBack, 90, 27);
        Label Registerl1 = new Label("Name");
        Registerl1.setFont(Font.font("New times Roman", 12));
        Label Registerl2 = new Label("Password");
        Registerl2.setFont(Font.font("New times Roman", 12));
        Label Registerl4 = new Label("Address");
        Registerl4.setFont(Font.font("New times Roman", 12));
        Label Registerl5 = new Label("Age");
        Registerl5.setFont(Font.font("New times Roman", 12));
        TextField RegisterName = new TextField();
        TextField registerPassword = new TextField();
        TextField address = new TextField();
        TextField regiseterAge = new TextField();
        //Add labels and text fields to the GridPane
        registerPane.add(Registerl1, 0, 0);
        registerPane.add(RegisterName, 1, 0);
        registerPane.add(Registerl2, 0, 1);
        registerPane.add(registerPassword, 1, 1);
        registerPane.add(Registerl4, 0, 3);
        registerPane.add(address, 1, 3);
        registerPane.add(Registerl5, 0, 4);
        registerPane.add(regiseterAge, 1, 4);
        registerPane.add(Registerb1, 1, 5);
        registerPane.add(registerBack, 0, 5);
        registerPane.setAlignment(Pos.CENTER);
        registerPane.setVgap(15);
        registerPane.setHgap(7);
        RegisterScene = new Scene(registerPane, 500, 500);
        registerBack.setOnAction(e -> {
            stage.setScene(s1);
        });

        Registerb1.setOnAction(e -> {
            try {
                if (RegisterName.getText().isEmpty() ||
                        registerPassword.getText().isEmpty() ||
                        address.getText().isEmpty() ||
                        regiseterAge.getText().isEmpty()) {
                    throw new InvalidRegistrationException();
                }
                String username = RegisterName.getText();
                String userPassword = registerPassword.getText();
                String ageText = regiseterAge.getText();
                patientNum++;
                String userID = "P-" + patientNum;
                if (!ageText.matches("\\d+")) {
                    throw new InvalidAgeException();
                }
                int age = Integer.parseInt(regiseterAge.getText()); // Assuming age is an integer
                if (age < 0 || age > 150) {
                    throw new InvalidAgeException();
                }
                String userAddress = address.getText();
                Patient p = new Patient(username, userID, userPassword, age, userAddress);
                patientsList.add(p);
                stage.setScene(PatientMenuScene);
            } catch (InvalidRegistrationException exception) {
                throwAlert("Fields Not Complete");
            } catch (InvalidAgeException ab) {
                throwAlert("Age can not be less than 0 or greater than 150 and must be number");
            }
        });
        //Doctor Menu
        /* Futher Scenes for doctor menu are writing prescriptions 1s, Managing Appointments 3s,Updating Patient 1s*/
        Button doctorMenu1 = new Button("View Patients");
        setStyling(doctorMenu1, 140, 40);
        Button doctorMenu3 = new Button("Manage Appointments");
        setStyling(doctorMenu3, 140, 40);
        Button doctorMenu4 = new Button("Update Patient Data");
        Button doctorMenu5 = new Button("LogOut");
        setStyling(doctorMenu5, 140, 40);
        GridPane dMenuPane = new GridPane();
        dMenuPane.add(doctorMenu1, 4, 3);
        dMenuPane.add(doctorMenu3, 4, 4);
        dMenuPane.add(doctorMenu5, 4, 5);
        dMenuPane.setAlignment(Pos.CENTER);
        dMenuPane.setVgap(25);
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
        setStyling(docPatientsViewb1, 90, 26);
        Button docPatientsViewb2 = new Button("Write Prescirption");
        setStyling(docPatientsViewb2, 90, 26);
        Button docPatientsViewb3 = new Button("Medical Record");
        setStyling(docPatientsViewb3, 90, 26);
        HBox docPatientsViewHbox = new HBox(docPatientsViewb1, docPatientsViewb2, docPatientsViewb3);
        docPatientsViewHbox.setSpacing(7);
        VBox docPatientsViewVbox = new VBox(docPatientsView, docPatientsViewHbox);
        Scene docPatientViewScene = new Scene(docPatientsViewVbox, 500, 500);
        Label docPrescriptionl1 = new Label("Patient:");
        docPrescriptionl1.setFont(Font.font("New times Roman", 12));
        Label docPrescriptionl2 = new Label("");
        docPrescriptionl2.setFont(Font.font("New times Roman", 12));
        Label docPrescriptionl3 = new Label("Date:");
        docPrescriptionl3.setFont(Font.font("New times Roman", 12));
        Button docPrescriptionb1 = new Button("Submit");
        setStyling(docPrescriptionb1, 90, 27);
        TextField docPrescriptionT1 = new TextField();
        TextField docPrescriptionT2 = new TextField();
        GridPane docPrescriptionpane = new GridPane();
        docPrescriptionpane.add(docPrescriptionl1, 3, 4);
        docPrescriptionpane.add(docPrescriptionl2, 4, 4);
        docPrescriptionpane.add(docPrescriptionl3, 3, 6);
        docPrescriptionpane.add(docPrescriptionT1, 4, 6);
        docPrescriptionpane.add(docPrescriptionb1, 4, 7);
        docPrescriptionpane.setVgap(10);
        docPrescriptionpane.setAlignment(Pos.CENTER);
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
                    docPrescriptionb1.setOnAction(b -> {
                        try {
                            if (docPrescriptionT1.getText().isEmpty() || docPrescriptionT2.getText().isEmpty())
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
                        } catch (EmptyFieldException exp1) {
                            throwAlert("Error");
                        }
                    });
                } catch (ButtonClickedException exception) {
                    throwAlert("Eror");
                } catch (NoOptionSelectedException exception) {
                    throwAlert("Error ");
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
                        presl1.setFont(Font.font("New times Roman", 12));
                        Label presl2 = new Label(docPatientsView.getSelectionModel().getSelectedItem().getRecord().getNotes());
                        presl2.setFont(Font.font("New times Roman", 12));
                        Button presb2 = new Button("Further Details");
                        setStyling(presb2, 90, 26);
                        Button presb = new Button("back");
                        setStyling(presb, 90, 26);
                        HBox presh = new HBox(presl1, presl2);
                        presh.setSpacing(10);
                        HBox presh2 = new HBox(presb, presb2);
                        presh2.setSpacing(10);
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
                                doctorLabel.setFont(Font.font("New times Roman", 12));
                                Label patientLabel = new Label("Patient Name: " + prescription.getPrescribedto().getName());
                                patientLabel.setFont(Font.font("New times Roman", 12));
                                Label prescriptionDateLabel = new Label("Prescription Date: " + prescription.getPrescriptiondate());
                                prescriptionDateLabel.setFont(Font.font("New times Roman", 12));
                                Label medicinesLabel = new Label("Medicines: " + prescription.getMedicines());
                                medicinesLabel.setFont(Font.font("New times Roman", 12));
                                Button Db = new Button("Back");
                                setStyling(Db, 90, 26);
                                // Create a layout to organize labels
                                VBox root = new VBox(doctorLabel, patientLabel, prescriptionDateLabel, medicinesLabel, Db);
                                root.setSpacing(10);
                                Db.setOnAction(d -> stage.setScene(press));
                                // Create a scene and set it on the stage
                                Scene scene = new Scene(root, 400, 200);
                                stage.setScene(scene);
                            } catch (NoOptionSelectedException exception) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setContentText("error");
                                alert.show();
                            }
                        });
                    } catch (NoOptionSelectedException exp) {
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
            setStyling(b1, 90, 26);
            Button b2 = new Button("Cancel");
            setStyling(b2, 90, 26);
            Button b3 = new Button("ChangeTime");
            setStyling(b3, 90, 26);
            HBox h = new HBox(b1, b2, b3);
            h.setSpacing(10);
            VBox v = new VBox(appointmentTableView, h);
            b2.setOnAction(a -> {
                try {
                    if (appointmentTableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();

                    appointmentTableView.getSelectionModel().getSelectedItem().setStatus("CANCELLED");
                    appointmentTableView.refresh();
                } catch (NoOptionSelectedException exp) {
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
                    if (isClicked)
                        throw new ButtonClickedException();
                    isClicked = true;
                    Label dateLabel = new Label("Date:");
                    TextField dateTextField = new TextField();
                    Label timeLabel = new Label("Time:");
                    timeLabel.setFont(Font.font("New times Roman", 12));
                    TextField timeTextField = new TextField();
                    GridPane g = new GridPane();
                    g.add(dateLabel, 0, 0);
                    g.add(dateTextField, 1, 0);
                    g.add(timeLabel, 0, 1);
                    g.add(timeTextField, 1, 1);
                    Button button = new Button("Submit");
                    button.setFont(Font.font("New times Roman", 12));
                    g.add(button, 0, 2);
                    g.setHgap(7);
                    v.getChildren().add(g);

                    button.setOnAction(b -> {
                        try {
                            if (dateTextField.getText().isEmpty() || timeTextField.getText().isEmpty())
                                throw new EmptyFieldException();
                            appointmentTableView.getSelectionModel().getSelectedItem().setAppointmentDate(dateLabel.getText());
                            appointmentTableView.getSelectionModel().getSelectedItem().setAppointmentDate(dateLabel.getText());
                            v.getChildren().remove(g);
                            appointmentTableView.refresh();
                            isClicked = true;
                        } catch (EmptyFieldException exception) {
                            throwAlert("Error");
                        }
                    });
                } catch (NoOptionSelectedException exp) {
                    throwAlert("Please select options");
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
        Button patientb1 = new Button("View Current Info");
        setStyling(patientb1, 140, 40);
        Button patientb2 = new Button("View Medical Record");
        setStyling(patientb2, 140, 40);
        Button patientb3 = new Button("Buy Medicine");
        setStyling(patientb3, 140, 40);
        Button patientb4 = new Button("Generate Invoice");
        setStyling(patientb4, 160, 40);
        Button patientb5 = new Button("View Doctors");
        setStyling(patientb5, 140, 40);
        Button patientb7 = new Button("Log out");
        setStyling(patientb7, 160, 40);
        GridPane patientMenu = new GridPane();
        patientMenu.add(patientb1, 3, 4);
        patientMenu.add(patientb2, 6, 4);
        patientMenu.add(patientb3, 3, 5);
        patientMenu.add(patientb4, 6, 5);
        patientMenu.add(patientb5, 3, 6);
        patientMenu.add(patientb7, 6, 6);
        patientMenu.setAlignment(Pos.CENTER);
        patientMenu.setVgap(35);
        patientMenu.setHgap(30);
        PatientMenuScene = new Scene(patientMenu, 500, 500);
        //Buttons for Patient Menu
        patientb1.setOnAction(e -> {//shows patient info
            searchUsers(loginl1.getText());
            Button patientCurrentInfoBtn = new Button("Back");
            setStyling(patientCurrentInfoBtn, 90, 26);
            Label patientcurrentinfol1 = new Label("Name of Patient");
            patientcurrentinfol1.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol2 = new Label("ID of Patient");
            patientcurrentinfol2.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol3 = new Label("Age of Patient");
            patientcurrentinfol3.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol4 = new Label("Address of Patient");
            patientcurrentinfol4.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol5 = new Label(CurrentUser[0].getName());
            patientcurrentinfol5.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol6 = new Label(CurrentUser[0].getID());
            patientcurrentinfol6.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol7 = new Label(Integer.toString(((Patient) CurrentUser[0]).getAge()));
            patientcurrentinfol7.setFont(Font.font("New times Roman", 12));
            Label patientcurrentinfol8 = new Label(((Patient) CurrentUser[0]).getAddress());
            patientcurrentinfol8.setFont(Font.font("New times Roman", 12));
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
        patientb2.setOnAction(e -> {

            ObservableList<Prescription> p = FXCollections.observableArrayList(((Patient) CurrentUser[0]).getRecord().getPrescriptions());
            TableView<Prescription> pview = new TableView<>();
            TableColumn<Prescription, String> pdate = new TableColumn<>("Date");
            TableColumn<Prescription, String> pdoc = new TableColumn<>("Doctor");
            pdoc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPractitioner().getName()));
            pdate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrescriptiondate()));
            pview.getColumns().addAll(prescriptionDoc, prescriptionDate);
            pview.setItems(p);
            pview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            Button b = new Button("Go back");
            setStyling(b, 90, 27);
            Button details = new Button("View Details");
            HBox h = new HBox(b, details);
            VBox v = new VBox(pview, h);
            pview.refresh();
            Scene scene = new Scene(v, 500, 500);
            stage.setScene(scene);
            b.setOnAction(a -> stage.setScene(PatientMenuScene));
            details.setOnAction(a -> {
                try {
                    if (pview.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
                    Prescription prescription = pview.getSelectionModel().getSelectedItem();
                    Label doctorLabel = new Label("Doctor Name: " + prescription.getPractitioner().getName());
                    Label patientLabel = new Label("Patient Name: " + prescription.getPrescribedto().getName());
                    Label prescriptionDateLabel = new Label("Prescription Date: " + prescription.getPrescriptiondate());
                    Label medicinesLabel = new Label("Medicines: " + prescription.getMedicines());
                    Button Db = new Button("Back");
                    // Create a layout to organize labels
                    VBox root = new VBox(doctorLabel, patientLabel, prescriptionDateLabel, medicinesLabel, Db);
                    Db.setOnAction(d -> stage.setScene(scene));
                    // Create a scene and set it on the stage
                    Scene prescriptionscene = new Scene(root, 400, 200);
                    stage.setScene(prescriptionscene);
                } catch (NoOptionSelectedException exception) {
                    throwAlert("Select an Option first");
                }
            });

        });
        //button 4
        Label patientBill = new Label("Due Amount");
        Label dueAmount = new Label();
        GridPane patientBillHbox = new GridPane();
        Button billGoback = new Button("Go back");
        setStyling(billGoback, 90, 26);
        patientBillHbox.add(patientBill, 3, 4);
        patientBillHbox.add(dueAmount, 4, 4);
        patientBillHbox.add(billGoback, 3, 5);
        patientBillHbox.setVgap(10);
        patientBillHbox.setHgap(7);
        patientBillHbox.setAlignment(Pos.CENTER);
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
        setStyling(viewAllDocsb1, 90, 27);
        Button viewAllDocsb2 = new Button();
        setStyling(viewAllDocsb2, 90, 27);
        HBox h = new HBox(viewAllDocsb1, viewAllDocsb2);
        h.setSpacing(5);
        viewAllDocVbox.getChildren().add(h);
        patientb5.setOnAction(e -> {
            viewAllDocsb2.setText("Assign");
            viewAllDocsb1.setOnAction(a -> {
                stage.setScene(PatientMenuScene);
            });
            viewAllDocsb2.setOnAction(a -> {
                try {
                    if (allDoctorsview.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
                    if (allDoctorsview.getSelectionModel().getSelectedItem() == ((Patient) CurrentUser[0])
                            .getDoctorIncharge())
                        throw new InvalidInputException();
                    if (((Patient) CurrentUser[0]).getDoctorIncharge() != null)
                        ((Patient) CurrentUser[0]).getDoctorIncharge().getPatients().remove(((Patient) CurrentUser[0]));
                    ((Patient) CurrentUser[0]).setDoctorIncharge(allDoctorsview.getSelectionModel().getSelectedItem());
                    System.out.println(((Patient) CurrentUser[0]).getDoctorIncharge());
                    allDoctorsview.getSelectionModel().getSelectedItem().assignpatient(((Patient) CurrentUser[0]));
                } catch (InvalidInputException exception) {
                    throwAlert("Doctor has already been added");
                } catch (NoOptionSelectedException excption) {
                    throwAlert("Please select any Doctor");
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
        //Admin Menu



        /*adding Doctor 1s, Scheduling Appointment 1s, Managing inventory 6s, view 1s*/
        Button Adminb1 = new Button("Add Doctor");
        setStyling(Adminb1, 140, 40);
        Button Adminb2 = new Button("Schedule Appointment");
        setStyling(Adminb2, 100, 40);
        Button Adminb3 = new Button("Manage Inventory");
        setStyling(Adminb3, 100, 40);
        Button Adminb5 = new Button("View patients");
        setStyling(Adminb5, 170, 40);
        Button Adminb6 = new Button("Logout");
        setStyling(Adminb6, 100, 40);
        Button Adminb7 = new Button("View All Docs");
        setStyling(Adminb7, 100, 40);
        GridPane AdminMenu = new GridPane();
        // (element, column, row, column span, row span)
        AdminMenu.add(Adminb1, 3, 4);
        AdminMenu.add(Adminb2, 5, 4);
        AdminMenu.add(Adminb3, 3, 5);
        AdminMenu.add(Adminb5, 5, 5);
        AdminMenu.add(Adminb6, 4, 6);
        AdminMenu.add(Adminb7, 4, 7);
        AdminMenu.setAlignment(Pos.CENTER);
        AdminMenu.setVgap(35);
        Adminb7.setOnAction(e -> {
            stage.setScene(viewAllDocScene);
            viewAllDocsb2.setText("Remove");
            viewAllDocsb2.setOnAction(a -> {
                Doctor d = allDoctorsview.getSelectionModel().getSelectedItem();
                allUsers.remove(d);
                allDoctorsObsList.remove(d);
                allDoctorsview.refresh();
            });
            viewAllDocsb1.setOnAction(a->stage.setScene(AdminMenuScene));
        });
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
        addDocl1.setFont(Font.font("New times Roman", 15));
        Label addDocl3 = new Label("Specialization");
        addDocl3.setFont(Font.font("New times Roman", 15));
        Label addDocl4 = new Label("Password");
        addDocl4.setFont(Font.font("New times Roman", 15));
        TextField addDocT1 = new TextField();
        TextField addDocT3 = new TextField();
        TextField addDocT4 = new TextField();
        GridPane addDocPane = new GridPane();
        // Add elements to GridPane
        addDocPane.add(addDocl1, 3, 4);
        addDocPane.add(addDocl3, 3, 6);
        addDocPane.add(addDocl4, 3, 7);
        addDocPane.add(addDocT1, 4, 4);
        addDocPane.add(addDocT3, 4, 6);
        addDocPane.add(addDocT4, 4, 7);
        addDocPane.setVgap(20);
        addDocPane.setHgap(10);
        Button addDocb1 = new Button("Submit");
        setStyling(addDocb1, 100, 35);
        Button addDocb2 = new Button("Go back");
        setStyling(addDocb2, 100, 35);
        addDocPane.add(addDocb1, 3, 8);
        addDocPane.add(addDocb2, 4, 8);
        addDocPane.setAlignment(Pos.CENTER);
        Scene addDocScene = new Scene(addDocPane, 500, 500);
        addDocb1.setOnAction(e -> {
            try {
                if (addDocT1.getText().isEmpty() || addDocT3.getText().isEmpty() || addDocT4.getText().isEmpty())
                    throw new EmptyFieldException();
                doctorNum++;
                Doctor d = new Doctor(addDocT1.getText(), addDocT4.getText(), "D-" + doctorNum, addDocT3.getText());
                allUsers.add(d);
                allDoctorsObsList.add(d);
                addDocT1.clear();
                addDocT3.clear();
                addDocT4.clear();
            } catch (EmptyFieldException exception) {
                throwAlert("Fields are empty");
            }
        });
        addDocb2.setOnAction(e -> {
            stage.setScene(AdminMenuScene);
        });
        Adminb1.setOnAction(e -> stage.setScene(addDocScene));
        //Schedule Appointment Scene
        Button ScheduleAppointmentb2 = new Button("back");
        setStyling(ScheduleAppointmentb2, 100, 35);
        Button ScheduleAppointmentb1 = new Button("Submit");
        setStyling(ScheduleAppointmentb1, 100, 35);
        Label ScheduleAppointmentl1 = new Label("Doctor ID:");
        ScheduleAppointmentl1.setFont(Font.font("New times Roman", 15));
        Label ScheduleAppointmentl2 = new Label("Patient ID");
        ScheduleAppointmentl2.setFont(Font.font("New times Roman", 15));
        Label ScheduleAppointmentl3 = new Label("Date");
        ScheduleAppointmentl3.setFont(Font.font("New times Roman", 15));
        Label ScheduleAppointmentl4 = new Label("Time");
        ScheduleAppointmentl4.setFont(Font.font("New times Roman", 15));
        TextField ScheduleAppointmentT1 = new TextField();
        TextField ScheduleAppointmentT2 = new TextField();
        TextField ScheduleAppointmentT3 = new TextField();
        TextField ScheduleAppointmentT4 = new TextField();
        GridPane ScheduleAppointmentpane = new GridPane();
        ScheduleAppointmentpane.add(ScheduleAppointmentl1, 3, 4);
        ScheduleAppointmentpane.add(ScheduleAppointmentT1, 4, 4);
        ScheduleAppointmentpane.add(ScheduleAppointmentl2, 3, 5);
        ScheduleAppointmentpane.add(ScheduleAppointmentT2, 4, 5);
        ScheduleAppointmentpane.add(ScheduleAppointmentl3, 3, 6);
        ScheduleAppointmentpane.add(ScheduleAppointmentT3, 4, 6);
        ScheduleAppointmentpane.add(ScheduleAppointmentl4, 3, 7);
        ScheduleAppointmentpane.add(ScheduleAppointmentT4, 4, 7);
        ScheduleAppointmentpane.add(ScheduleAppointmentb1, 3, 8);
        ScheduleAppointmentpane.add(ScheduleAppointmentb2, 4, 8);
        ScheduleAppointmentpane.setAlignment(Pos.CENTER);
        ScheduleAppointmentpane.setVgap(20);
        ScheduleAppointmentpane.setHgap(10);
        ScheduleAppointmentb2.setOnAction(e -> stage.setScene(AdminMenuScene));
        Scene ScheduleapScene = new Scene(ScheduleAppointmentpane, 500, 500);
        Adminb2.setOnAction(e -> {
            stage.setScene(ScheduleapScene);
        });
        ScheduleAppointmentb1.setOnAction(e -> {
                    Patient p = null;
                    Doctor d = null;
                    try {
                        if (ScheduleAppointmentT1.getText().isEmpty() || ScheduleAppointmentT2.getText().isEmpty() ||
                                ScheduleAppointmentT3.getText().isEmpty() || ScheduleAppointmentT4.getText().isEmpty())
                            throw new EmptyFieldException();

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
                        ap.setAppointmentID("A-" + appointmentNum);
                        d.addAppointment(ap);
                    } catch (EmptyFieldException exception) {
                        throwAlert("fields are Empty");
                    } finally {
                        ScheduleAppointmentT1.clear();
                        ScheduleAppointmentT2.clear();
                        ScheduleAppointmentT3.clear();
                        ScheduleAppointmentT4.clear();
                    }
                }
        );
        //schedule Appointment Scene

        //View and Edit Patients Scene

        //Buttons
        Button viewAllPatientsb1 = new Button("Go back");
        setStyling(viewAllPatientsb1, 90, 27);
        Button viewAllPatientsb2 = new Button("Edit Patient");
        setStyling(viewAllPatientsb2, 90, 27);
        Button viewAllPatientsb3 = new Button("Appoint bed");
        setStyling(viewAllPatientsb3, 90, 27);
        Button viewAllPatientsb4 = new Button("Free Bed");
        setStyling(viewAllPatientsb4, 90, 27);
        HBox viewAllPatientsHbox = new HBox(viewAllPatientsb1, viewAllPatientsb2, viewAllPatientsb3, viewAllPatientsb4);
        viewAllPatientsHbox.setSpacing(6.6);
        //Observable arraylist and table view
        TableView<Patient> patientTable = new TableView<>();
        //table Column for each attribute
        TableColumn<Patient, String> patientIdColumn = new TableColumn<>("ID");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        // patientIdColumn.set(Font.font("New Times Roman", FontWeight.BOLD,12));
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
                if (isClicked)
                    throw new ButtonClickedException();
                isClicked = true;
                Label l1 = new Label("Name");
                l1.setFont(Font.font("New times Roman", 12));
                Label l3 = new Label("Address");
                l3.setFont(Font.font("New times Roman", 12));
                Label l4 = new Label("Age");
                l4.setFont(Font.font("New times Roman", 12));
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
                setStyling(b, 90, 26);
                gridPane.add(b, 2, 2);
                gridPane.setHgap(7);
                gridPane.setVgap(5);
                b.setOnAction(a -> {
                            try {
                                if (t1.getText().isEmpty() || t3.getText().isEmpty() || t4.getText().isEmpty())
                                    throw new EmptyFieldException();
                                if (!(t4.getText().matches("\\d+")))
                                    throw new InvalidAgeException();
                                if ((Integer.parseInt(t4.getText()) < 0 || Integer.parseInt(t4.getText()) > 150)) {
                                    throw new InvalidAgeException();
                                }
                                p.setGuiPatient(t1.getText(), Integer.parseInt(t4.getText()), t3.getText());
                                viewAllPatientsVbox.getChildren().remove(gridPane);
                                patientTable.refresh();
                                isClicked = false;
                            } catch (EmptyFieldException exception) {
                                throwAlert("Please fill all fields");
                            } catch (InvalidAgeException ab) {
                                throwAlert("Age can not be less than 0 or greater than 150 and must be number");
                            }
                        }
                );
            } catch (NoOptionSelectedException excption) {
                throwAlert("Please select any patient");
            } catch (ButtonClickedException exception) {
                throwAlert("Button already Clicked");
            }
        });
        viewAllPatientsb3.setOnAction(e -> {
            try {
                if (patientTable.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                if (isClicked)
                    throw new ButtonClickedException();
                isClicked = true;
                Patient p = patientTable.getSelectionModel().getSelectedItem();
                Label l1 = new Label("Bed number");
                l1.setFont(Font.font("New times Roman", 12));
                Label l2 = new Label("days occupied");
                l1.setFont(Font.font("New times Roman", 12));
                TextField t1 = new TextField();
                TextField t2 = new TextField();
                Button submit = new Button("Submit");
                setStyling(submit, 90, 26);
                GridPane bedGridPane = new GridPane();
                bedGridPane.add(l1, 0, 0);
                bedGridPane.add(t1, 1, 0);
                bedGridPane.add(l2, 0, 1);
                bedGridPane.add(t2, 1, 1);
                bedGridPane.add(submit, 2, 1);
                bedGridPane.setVgap(5);
                bedGridPane.setHgap(5);
                viewAllPatientsVbox.getChildren().add(bedGridPane);
                submit.setOnAction(a -> {
                    try {
                        if (t1.getText().isEmpty() || t2.getText().isEmpty())
                            throw new EmptyFieldException();
                        if (allBeds[Integer.parseInt(t1.getText()) - 1].isOccupied()) {  // Assuming bed numbers start from 1
                            throw new RuntimeException();
                        }
                        if (!t1.getText().matches("\\d+") || !t2.getText().matches("\\d+")) {
                            throw new InvalidInputException();
                        }
                        if (Integer.parseInt(t1.getText()) > allBeds.length || Integer.parseInt(t1.getText()) < 0)
                            throw new InvalidInputException();
                        p.setBedUsed(allBeds[Integer.parseInt(t1.getText()) - 1]);
                        p.getBedUsed().setDaysOccupied(Integer.parseInt(t2.getText()));
                        viewAllPatientsVbox.getChildren().remove(bedGridPane);
                        patientTable.refresh();
                        isClicked = false;
                    } catch (EmptyFieldException exception) {
                        throwAlert("Please fill all fields");
                    } catch (InvalidInputException invalidInputException) {
                        throwAlert("Bed must be integer or bed does not exist");
                    } catch (RuntimeException exception) {
                        throwAlert("Bed is Occupied ");
                    }
                });
            } catch (NoOptionSelectedException excption) {
                throwAlert("Please select any patient");
            } catch (ButtonClickedException exception) {
                throwAlert("Button already Clicked");
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
        setStyling(Invob2, 90, 26);
        Button Invob3 = new Button("Update Price");
        setStyling(Invob3, 90, 26);
        Button Invob4 = new Button("Remove");
        setStyling(Invob4, 90, 26);
        Button Invob5 = new Button("Add Item");
        setStyling(Invob5, 90, 26);
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
        //invonameColumn.setStyle("-fx-border-color: black; -fx-border-width:1px; ");
        // Add columns to TableView
        Button GobackFromInvtable = new Button("GO back");
        setStyling(GobackFromInvtable, 90, 26);
        GobackFromInvtable.setOnAction(e -> stage.setScene(AdminMenuScene));
        tableView.getColumns().addAll(invoidColumn, invonameColumn, invoMenufacturerColumn, invoquantityColumn, priceColumn);
        HBox invoOptions = new HBox(GobackFromInvtable, Invob2, Invob3, Invob4, Invob5);
        VBox InvoVerticalBox = new VBox(tableView, invoOptions);
        invoOptions.setSpacing(10);
        Scene InvoScene2 = new Scene(InvoVerticalBox, 500, 500);
        AdminMenuScene = new Scene(AdminMenu, 500, 500);
        //setting functionality on all the buttons of the scene
        Adminb3.setOnAction(e -> stage.setScene(InvoScene2));
        Invob2.setOnAction(e -> {
            try {
                if (isClicked)
                    throw new ButtonClickedException();
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                isClicked = true;
                Label l1 = new Label("New Quantity");
                l1.setFont(Font.font("New times Roman", 12));
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                setStyling(b1, 90, 26);
                HBox h1 = new HBox(l1, T1, b1);
                h1.setSpacing(10);
                InvoVerticalBox.getChildren().add(h1);
                b1.setOnAction(a -> {
                    try {
                        if (T1.getText().isEmpty())
                            throw new EmptyFieldException();
                        int i = Invlist1.indexOf(tableView.getSelectionModel().getSelectedItem());
                        Invlist1.get(i).setQuantity(Integer.parseInt(T1.getText()));
                        InvoVerticalBox.getChildren().remove(h1);
                        tableView.refresh();
                        isClicked = false;
                    } catch (EmptyFieldException exception) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please fill the textField");
                        alert.show();
                    }
                });
            } catch (NoOptionSelectedException excption) {
                throwAlert("Select an item");
            } catch (ButtonClickedException exception) {
                throwAlert("Button already clicked");
            }
        });
        Invob3.setOnAction(e -> {
            try {
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                if (isClicked)
                    throw new ButtonClickedException();
                isClicked = true;
                Label l1 = new Label("New Price");
                l1.setFont(Font.font("New times Roman", 12));
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                setStyling(b1, 90, 26);
                HBox h1 = new HBox(l1, T1, b1);
                h1.setSpacing(10);
                InvoVerticalBox.getChildren().add(h1);
                b1.setOnAction(a -> {
                    try {
                        if (T1.getText().isEmpty())
                            throw new EmptyFieldException();
                        if (Integer.parseInt(T1.getText()) < 0)
                            throw new NegativePriceException();
                        int i = Invlist1.indexOf(tableView.getSelectionModel().getSelectedItem());
                        Invlist1.get(i).setPrice(Double.parseDouble(T1.getText()));
                        InvoVerticalBox.getChildren().remove(h1);
                        tableView.refresh();
                        isClicked = false;
                    } catch (EmptyFieldException exception) {
                        throwAlert("Please fill the textField");
                    } catch (NegativePriceException exp2) {
                        throwAlert("Please set price greater than 0");
                    }
                });
            } catch (NoOptionSelectedException excption) {
                throwAlert("No options selected");
            } catch (ButtonClickedException exception) {
                throwAlert("Button is already CLicked");
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
            } catch (NoOptionSelectedException excption) {
                throwAlert("Select an item");
            }
        });
        Invob5.setOnAction(e -> {
            try {
                if (isClicked)
                    throw new ButtonClickedException();
                isClicked = true;
                Label l1 = new Label("ID: ");
                l1.setFont(Font.font("New times Roman", 12));
                Label l2 = new Label("Name: ");
                l2.setFont(Font.font("New times Roman", 12));
                Label l3 = new Label("Manufacturer: ");
                l3.setFont(Font.font("New times Roman", 12));
                Label l4 = new Label("Quantity: ");
                l4.setFont(Font.font("New times Roman", 12));
                Label l5 = new Label("Price: ");
                l5.setFont(Font.font("New times Roman", 12));
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
                setStyling(b, 90, 26);
                gridPane.add(b, 2, 4);
                gridPane.setVgap(5);
                b.setOnAction(a -> {
                    try {
                        if (t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty() || t4.getText().isEmpty())
                            throw new EmptyFieldException();
                        if (!t4.getText().matches("\\d+") || !t5.getText().matches("\\d+")) {
                            throw new InvalidInputException();
                        }
                        if (Integer.parseInt(t4.getText()) < 0 || Integer.parseInt(t5.getText()) < 0)
                            throw new InvalidInputException();
                        Item i = new Item(t2.getText(), t3.getText(), Integer.parseInt(t4.getText()), t1.getText(), Double.parseDouble(t5.getText()));
                        Invlist1.add(i);
                        InvoVerticalBox.getChildren().remove(gridPane);
                        tableView.refresh();
                        isClicked = false;
                    } catch (EmptyFieldException exception) {
                        throwAlert("Please fill the textField");
                    } catch (InvalidInputException exception) {
                        throwAlert("Invalid Input");
                    }
                });
            } catch (ButtonClickedException exception) {
                throwAlert("Button is already Clicked");
            }
        });
        //Inventory Button from Patient
        patientb3.setOnAction(e -> {
            Button b1 = new Button("Buy");
            setStyling(b1, 90, 27);
            Button b2 = new Button("Back");
            setStyling(b2, 90, 27);
            GridPane gridPane = new GridPane();
            gridPane.add(b1, 0, 0);
            gridPane.add(b2, 2, 0);
            gridPane.setAlignment(Pos.CENTER);
            TextField t1 = new TextField();
            gridPane.add(t1, 1, 0);
            VBox v = new VBox(tableView, gridPane);
            v.setSpacing(5);
            Scene patientInventory = new Scene(v);
            stage.setScene(patientInventory);
            b2.setOnAction(a -> stage.setScene(PatientMenuScene));
            b1.setOnAction(a -> {
                        try {
                            if (tableView.getSelectionModel().isEmpty())
                                throw new NoOptionSelectedException();
                            if (Integer.parseInt(t1.getText()) > tableView.getSelectionModel().getSelectedItem().getQuantity()
                                    || Integer.parseInt(t1.getText()) < 0) {
                                throwAlert("We don't have that much quantity");
                            }
                            Item i = tableView.getSelectionModel().getSelectedItem();
                            double total = i.getTotal(Integer.parseInt(t1.getText()));
                            ((Patient) CurrentUser[0]).UpdateBilling(total);
                            tableView.refresh();
                            t1.clear();
                        } catch (NoOptionSelectedException excption) {
                            throwAlert("Please select an item");
                        }
                    }
            );
        });
        //inventory management Scene complete


        //admin complete
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
        doctorNum = 5;
        // adding hardcoded patients
        allUsers.add(new Patient("Patient1", "P-1", "", 25, "Address1"));
        allUsers.add(new Patient("Patient2", "P-2", "Patient456", 30, "Address2"));
        allUsers.add(new Patient("Patient3", "P-3", "Patient789", 22, "Address3"));
        allUsers.add(new Patient("Patient4", "P-4", "PatientABC", 40, "Address4"));
        allUsers.add(new Patient("Patient5", "P-5", "PatientDEF", 28, "Address5"));
        patientNum = 5;
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

    private void throwAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.show();
    }

    public void setStyling(Button button, int sizeX, int sizeY) {
        button.setStyle("-fx-background-color: #706FAB; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setMinSize(sizeX, sizeY);

        Stream.of(button)
                .forEach(btn -> {
                    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #6A92CC; -fx-text-fill: white;"));
                    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #706FAB; -fx-text-fill: white;"));
                });
    }
}
