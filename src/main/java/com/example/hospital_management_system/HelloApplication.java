package com.example.hospital_management_system;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class HelloApplication extends Application {
    ArrayList<User> allUsers = new ArrayList<>();
    Bed allBeds[];
    Inventory GUIinv;
    FileHandling fileHandling = new FileHandling();
    Scene s1, RegisterScene, DoctorMenuScene, PatientMenuScene, AdminMenuScene;
    boolean isClicked = false;//Used for making sure same button is not clicked twice
    int []num = new int[3];//Used for assigning ID
    //color and BackGround
    Color customColor = Color.rgb(46, 51, 71);
    BackgroundFill backgroundFill = new BackgroundFill(customColor, null, null);
    Background background = new Background(backgroundFill);

    @Override
    public void start(Stage stage) throws Exception {
        //method intializes arrayList of users will not be needed after file handling
        InitializeUsers();
        ObservableList<Patient> patientsList = FXCollections.observableArrayList(addPatientstoObserve());
        final User[] CurrentUser = {null};                        //Stores the current User of the System
        stage.setTitle("Hospital Management System : ");
        //Login user Section
        //buttons for logging in user
        Button loginB1 = new Button("Login");
        setStyling(loginB1, 90, 25);
        Button loginb1 = new Button("Login");
        setStyling(loginb1, 90, 25);
        Label login1 = new Label("Enter UserName : ");
        login1.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
        Label login2 = new Label("Enter Password : ");
        login2.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
        Button loginb2 = new Button("Register");
        setStyling(loginb2, 90, 25);
        //labels  and text fields
        Label loginl1 = new Label("Enter UserName : ");
        Label loginl2 = new Label("Enter Password : ");
        TextField loginField = new TextField();
        PasswordField loginPass = new PasswordField();
        GridPane loginPane = new GridPane();//GridPane
        loginPane.add(loginl1, 3, 3);
        loginPane.add(loginl2, 3, 4);
        loginPane.setBackground(background);
        loginPane.add(login1, 3, 3);
        loginPane.add(login2, 3, 4);
        loginPane.add(loginField, 4, 3);
        loginPane.add(loginPass, 4, 4);
        loginPane.add(loginB1, 4, 5);
        loginPane.add(loginb2, 3, 5);
        loginPane.setAlignment(Pos.BASELINE_CENTER);//setting alignment
        loginPane.setVgap(35);//setting vertical gap
        s1 = new Scene(loginPane, 900, 650); //creating the first scene of the System
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
        setStyling(Registerb1, 90, 25);
        Button registerBack = new Button("Go back");
        setStyling(registerBack, 90, 25);
        Label Registerl1 = new Label("Username");
        Registerl1.setFont(Font.font("Times New Roman", 12));
        Registerl1.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label Registerl2 = new Label("Password");
        Registerl2.setFont(Font.font("Times New Roman", 12));
        Registerl2.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label Registerl4 = new Label("Address");
        Registerl4.setFont(Font.font("Times New Roman", 12));
        Registerl4.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label Registerl5 = new Label("Age");
        Registerl5.setFont(Font.font("Times New Roman", 12));
        Registerl5.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
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
        registerPane.setBackground(background);
        RegisterScene = new Scene(registerPane, 900, 650);
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
                num[0]++;
                String userID = "P-" + num[0];
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
                allUsers.add(p);
                CurrentUser[0] = p;
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
        setStyling(doctorMenu1, 168, 40);
        Button doctorMenu3 = new Button("Manage Appointments");
        setStyling(doctorMenu3, 168, 40);
        Button doctorMenu4 = new Button("Change PassWord");
        setStyling(doctorMenu4, 168, 40);
        Button doctorMenu5 = new Button("LogOut");
        setStyling(doctorMenu5, 168, 40);
        GridPane dMenuPane = new GridPane();
        dMenuPane.add(doctorMenu1, 4, 3);
        dMenuPane.add(doctorMenu3, 4, 4);
        dMenuPane.add(doctorMenu5, 4, 5);
        dMenuPane.add(doctorMenu4, 4, 6);
        dMenuPane.setBackground(background);
        dMenuPane.setAlignment(Pos.CENTER);
        dMenuPane.setVgap(22);
        //Button 4
        doctorMenu4.setOnAction(e -> changePassword(CurrentUser, stage, DoctorMenuScene));
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
        setStyling(docPatientsViewb1, 90, 25);
        Button docPatientsViewb2 = new Button("Write Prescirption");
        setStyling(docPatientsViewb2, 90, 25);
        Button docPatientsViewb3 = new Button("Medical Record");
        setStyling(docPatientsViewb3, 90, 25);
        HBox docPatientsViewHbox = new HBox(docPatientsViewb1, docPatientsViewb2, docPatientsViewb3);
        docPatientsViewHbox.setBackground(background);
        docPatientsViewHbox.setSpacing(7);
        VBox docPatientsViewVbox = new VBox(docPatientsView, docPatientsViewHbox);
        docPatientsViewVbox.setBackground(background);
        Scene docPatientViewScene = new Scene(docPatientsViewVbox, 900, 650);
        Label docPrescriptionl1 = new Label("Patient:");
        docPrescriptionl1.setFont(Font.font("Times New Roman", 12));
        docPrescriptionl1.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label docPrescriptionl2 = new Label("");
        docPrescriptionl2.setFont(Font.font("Times New Roman", 12));
        docPrescriptionl2.setStyle("-fx-text-fill: white;");
        Label docPrescriptionl3 = new Label("Date:");
        docPrescriptionl3.setFont(Font.font("Times New Roman", 12));
        docPrescriptionl3.setStyle("-fx-text-fill: white; -fx-font-style: italic;");
        Button docPrescriptionb1 = new Button("Submit");
        setStyling(docPrescriptionb1, 90, 25);
        TextField docPrescriptionT1 = new TextField();
        TextField docPrescriptionT2 = new TextField();
        GridPane docPrescriptionpane = new GridPane();
        docPrescriptionpane.add(docPrescriptionl1, 3, 4);
        docPrescriptionpane.add(docPrescriptionl2, 4, 4);
        docPrescriptionpane.add(docPrescriptionl3, 3, 6);
        docPrescriptionpane.add(docPrescriptionT1, 4, 6);
        docPrescriptionpane.add(docPrescriptionT2, 4, 7);
        docPrescriptionpane.add(docPrescriptionb1, 4, 8);
        docPrescriptionpane.setVgap(10);
        docPrescriptionpane.setAlignment(Pos.CENTER);
        docPrescriptionpane.setBackground(background);
        Scene writePrescriptionScene = new Scene(docPrescriptionpane, 900, 650);
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
                    throwAlert("Error");
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
                        Button presb2 = new Button("Further Details");
                        setStyling(presb2, 90, 25);
                        Button presb = new Button("back");
                        setStyling(presb, 90, 25);
                        HBox presh2 = new HBox(presb, presb2);
                        presh2.setBackground(background);
                        presh2.setSpacing(10);
                        VBox presv = new VBox(prestableView, presh2);
                        presv.setBackground(background);
                        Scene press = new Scene(presv, 900, 650);
                        stage.setScene(press);

                        presb.setOnAction(a -> stage.setScene(docPatientViewScene));
                        presb2.setOnAction(a -> {
                            try {
                                if (prestableView.getSelectionModel().isEmpty())
                                    throw new NoOptionSelectedException();

                                Prescription prescription = prestableView.getSelectionModel().getSelectedItem();
                                Label doctorLabel = new Label("Doctor Name: " + prescription.getPractitioner().getName());
                                doctorLabel.setFont(Font.font("New times Roman", 12));
                                doctorLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                                Label patientLabel = new Label("Patient Name: " + prescription.getPrescribedto().getName());
                                patientLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                                patientLabel.setFont(Font.font("New times Roman", 12));
                                Label prescriptionDateLabel = new Label("Prescription Date: " + prescription.getPrescriptiondate());
                                prescriptionDateLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                                prescriptionDateLabel.setFont(Font.font("New times Roman", 12));
                                Label medicinesLabel = new Label("Medicines: " + prescription.getMedicines());
                                medicinesLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                                medicinesLabel.setFont(Font.font("New times Roman", 12));
                                Button Db = new Button("Back");
                                setStyling(Db, 90, 25);
                                // Create a layout to organize labels
                                VBox v = new VBox(doctorLabel, patientLabel, prescriptionDateLabel, medicinesLabel, Db);
                                v.setAlignment(Pos.CENTER);
                                v.setSpacing(25);
                                v.setBackground(background);
                                v.setSpacing(10);
                                Db.setOnAction(d -> stage.setScene(press));
                                // Create a scene and set it on the stage
                                Scene scene = new Scene(v, 900, 650);
                                stage.setScene(scene);
                            } catch (NoOptionSelectedException exception) {
                                throwAlert("Error");
                            }
                        });
                    } catch (NoOptionSelectedException exp) {
                        throwAlert("Error");
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
            setStyling(b1, 90, 25);
            Button b2 = new Button("Cancel");
            setStyling(b2, 90, 25);
            Button b3 = new Button("ChangeTime");
            setStyling(b3, 90, 25);
            Button b4  = new Button("Save File");
            setStyling(b4,90,25);
            HBox h = new HBox(b1, b2, b3, b4);
            h.setBackground(background);
            h.setSpacing(10);
            VBox v = new VBox(appointmentTableView, h);
            v.setBackground(background);
            b4.setOnAction(a->{
                try {
                    if(appointmentTableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
                    fileHandling.writeAppointment(appointmentTableView.getSelectionModel().getSelectedItem());
                }
                catch(NoOptionSelectedException exception)
                {
                    throwAlert("No option selected : ");
                }
                });
            b2.setOnAction(a -> {
                try {
                    if (appointmentTableView.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();

                    appointmentTableView.getSelectionModel().getSelectedItem().setStatus("CANCELLED");
                    appointmentTableView.refresh();
                } catch (NoOptionSelectedException exp) {
                    throwAlert("Error");
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
                    dateLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                    TextField dateTextField = new TextField();
                    Label timeLabel = new Label("Time:");
                    timeLabel.setStyle("-fx-text-fill: white;  -fx-font-weight: bold;");
                    timeLabel.setFont(Font.font("New times Roman", 12));
                    TextField timeTextField = new TextField();
                    GridPane g = new GridPane();
                    g.setBackground(background);
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
            Scene scene = new Scene(v, 900, 650);
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
        DoctorMenuScene = new Scene(dMenuPane, 900, 650);

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
        Button patientb8 = new Button("Change Password");
        setStyling(patientb8, 140, 40);
        GridPane patientMenu = new GridPane();
        patientMenu.add(patientb1, 3, 4);
        patientMenu.add(patientb2, 5, 4);
        patientMenu.add(patientb3, 3, 5);
        patientMenu.add(patientb4, 5, 5);
        patientMenu.add(patientb5, 3, 6);
        patientMenu.add(patientb7, 5, 6);
        patientMenu.add(patientb8, 4, 7);
        patientMenu.setBackground(background);
        patientMenu.setAlignment(Pos.CENTER);
        patientMenu.setVgap(30);
        patientMenu.setHgap(25);
        PatientMenuScene = new Scene(patientMenu, 900, 650);
        //Buttons for Patient Menu
        patientb8.setOnAction(e -> {
            changePassword(CurrentUser, stage, PatientMenuScene);
        });

        patientb1.setOnAction(e -> {//shows patient info
            searchUsers(loginl1.getText());
            Button patientCurrentInfoBtn = new Button("Back");
            setStyling(patientCurrentInfoBtn, 90, 25);
            Label patientcurrentinfol1 = new Label("Name of Patient");
            patientcurrentinfol1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol2 = new Label("ID of Patient");
            patientcurrentinfol2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol3 = new Label("Age of Patient");
            patientcurrentinfol3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol4 = new Label("Address of Patient");
            patientcurrentinfol4.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol5 = new Label(CurrentUser[0].getName());
            patientcurrentinfol5.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol6 = new Label(CurrentUser[0].getID());
            patientcurrentinfol6.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol7 = new Label(Integer.toString(((Patient) CurrentUser[0]).getAge()));
            patientcurrentinfol7.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
            Label patientcurrentinfol8 = new Label(((Patient) CurrentUser[0]).getAddress());
            patientcurrentinfol8.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");

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
            patientCurrentInfoGrid.setBackground(background);
            patientCurrentInfoGrid.setAlignment(Pos.CENTER);
            Scene patientCurrentInfoScene = new Scene(patientCurrentInfoGrid, 900, 650);
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
            setStyling(b, 90, 25);
            Button details = new Button("View Details");
            setStyling(details,90,25);
            Button print = new Button("Save file");
            setStyling(print,90,25);
            HBox h = new HBox(b, details,print);
            VBox v = new VBox(pview, h);
            v.setBackground(background);
            h.setBackground(background);
            pview.setBackground(background);
            pview.refresh();
            Scene scene = new Scene(v, 900, 650);
            stage.setScene(scene);
            print.setOnAction(a->{
                try{
                    if(pview.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
                    fileHandling.writePrescription(pview.getSelectionModel().getSelectedItem());
                }
                catch(NoOptionSelectedException exception)
                {
                    throwAlert("No option has been selected");
                }
            });

            b.setOnAction(a -> stage.setScene(PatientMenuScene));
            details.setOnAction(a -> {
                try {
                    if (pview.getSelectionModel().isEmpty())
                        throw new NoOptionSelectedException();
                    Prescription prescription = pview.getSelectionModel().getSelectedItem();
                    Label doctorLabel = new Label("Doctor Name: " + prescription.getPractitioner().getName());
                    doctorLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
                    Label patientLabel = new Label("Patient Name: " + prescription.getPrescribedto().getName());
                    patientLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
                    Label prescriptionDateLabel = new Label("Prescription Date: " + prescription.getPrescriptiondate());
                    prescriptionDateLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
                    Label medicinesLabel = new Label("Medicines: " + prescription.getMedicines());
                    medicinesLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
                    Button Db = new Button("Back");
                    // Create a layout to organize labels
                    VBox v1 = new VBox(doctorLabel, patientLabel, prescriptionDateLabel, medicinesLabel, Db);
                    v1.setAlignment(Pos.CENTER);
                    v1.setSpacing(25);
                    v1.setBackground(background);
                    v1.setSpacing(10);
                    v1.setBackground(background);
                    Db.setOnAction(d -> stage.setScene(scene));
                    // Create a scene and set it on the stage
                    Scene prescriptionscene = new Scene(v1, 900, 650);
                    stage.setScene(prescriptionscene);
                } catch (NoOptionSelectedException exception) {
                    throwAlert("Select an Option first");
                }
            });

        });
        //button 4
        Label patientBill = new Label("Due Amount");
        patientBill.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
        Label dueAmount = new Label();
        dueAmount.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
        GridPane patientBillpane = new GridPane();
        patientBillpane.setBackground(background);
        Button billGoback = new Button("Go back");
        setStyling(billGoback, 90, 25);
        patientBillpane.add(patientBill, 3, 4);
        patientBillpane.add(dueAmount, 4, 4);
        patientBillpane.add(billGoback, 3, 5);
        patientBillpane.setVgap(10);
        patientBillpane.setHgap(7);
        patientBillpane.setAlignment(Pos.CENTER);
        Scene patientBillScene = new Scene(patientBillpane, 900, 650);
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
        viewAllDocVbox.setBackground(background);
        Scene viewAllDocScene = new Scene(viewAllDocVbox, 900, 650);
        Button viewAllDocsb1 = new Button("Back");
        setStyling(viewAllDocsb1, 90, 25);
        Button viewAllDocsb2 = new Button();
        setStyling(viewAllDocsb2, 90, 25);
        HBox h = new HBox(viewAllDocsb1, viewAllDocsb2);
        h.setBackground(background);
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
        setStyling(Adminb7, 170, 40);
        Button Adminb8 = new Button("Change Password");
        setStyling(Adminb8, 100, 40);
        Adminb8.setOnAction(e -> {
            changePassword(CurrentUser, stage, AdminMenuScene);
        });
        GridPane AdminMenu = new GridPane();
        AdminMenu.setBackground(background);
        // (element, column, row, column span, row span)
        AdminMenu.add(Adminb1, 3, 4);
        AdminMenu.add(Adminb2, 5, 4);
        AdminMenu.add(Adminb3, 3, 5);
        AdminMenu.add(Adminb5, 5, 5);
        AdminMenu.add(Adminb6, 4, 7);
        AdminMenu.add(Adminb7, 5, 6);
        AdminMenu.add(Adminb8, 3, 6);
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
            viewAllDocsb1.setOnAction(a -> stage.setScene(AdminMenuScene));
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
        addDocl1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: white;");
        Label addDocl3 = new Label("Specialization");
        addDocl3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: white;");
        Label addDocl4 = new Label("Password");
        addDocl4.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField addDocT1 = new TextField();
        TextField addDocT3 = new TextField();
        TextField addDocT4 = new TextField();
        GridPane addDocPane = new GridPane();
        addDocPane.setBackground(background);
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
        Scene addDocScene = new Scene(addDocPane, 900, 650);
        addDocb1.setOnAction(e -> {
            try {
                if (addDocT1.getText().isEmpty() || addDocT3.getText().isEmpty() || addDocT4.getText().isEmpty())
                    throw new EmptyFieldException();
                num[1]++;
                Doctor d = new Doctor(addDocT1.getText(), addDocT4.getText(), "D-" + num[1], addDocT3.getText());
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
        ScheduleAppointmentl1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
        Label ScheduleAppointmentl2 = new Label("Patient ID");
        ScheduleAppointmentl2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
        Label ScheduleAppointmentl3 = new Label("Date");
        ScheduleAppointmentl3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
        Label ScheduleAppointmentl4 = new Label("Time");
        ScheduleAppointmentl4.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
        TextField ScheduleAppointmentT1 = new TextField();
        TextField ScheduleAppointmentT2 = new TextField();
        TextField ScheduleAppointmentT3 = new TextField();
        TextField ScheduleAppointmentT4 = new TextField();
        GridPane saPane = new GridPane();
        saPane.setBackground(background);
        saPane.add(ScheduleAppointmentl1, 3, 4);
        saPane.add(ScheduleAppointmentT1, 4, 4);
        saPane.add(ScheduleAppointmentl2, 3, 5);
        saPane.add(ScheduleAppointmentT2, 4, 5);
        saPane.add(ScheduleAppointmentl3, 3, 6);
        saPane.add(ScheduleAppointmentT3, 4, 6);
        saPane.add(ScheduleAppointmentl4, 3, 7);
        saPane.add(ScheduleAppointmentT4, 4, 7);
        saPane.add(ScheduleAppointmentb1, 3, 8);
        saPane.add(ScheduleAppointmentb2, 4, 8);
        saPane.setAlignment(Pos.CENTER);
        saPane.setBackground(background);
        saPane.setVgap(20);
        saPane.setHgap(10);
        ScheduleAppointmentb2.setOnAction(e -> stage.setScene(AdminMenuScene));
        Scene ScheduleapScene = new Scene(saPane, 900, 650);
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
                int doctorIndex = searchUsers(ScheduleAppointmentT1.getText());
                int patientIndex = searchUsers(ScheduleAppointmentT2.getText());

                if (doctorIndex != -1 && allUsers.get(doctorIndex) instanceof Doctor) {
                    d = (Doctor) allUsers.get(doctorIndex);
                } else {
                    System.out.println("Incorrect Doctor ID");
                }
                if (patientIndex != -1 && allUsers.get(patientIndex) instanceof Patient) {
                    p = (Patient) allUsers.get(patientIndex);

                } else {
                    System.out.println("Incorrect Patient ID");
                }

                if (d != null && p != null) {
                    Appointment ap = new Appointment(p, d, ScheduleAppointmentT3.getText(), ScheduleAppointmentT4.getText());
                    ap.setStatus("PENDING");
                    num[2]++;
                    ap.setAppointmentID("A-" + num[2]);
                    d.addAppointment(ap);
                    // Update allUsers list (if needed)
                    // Note: This assumes allUsers is a global variable accessible in this scope
                    System.out.println("Appointment created successfully.");
                    ScheduleAppointmentT1.clear();
                    ScheduleAppointmentT2.clear();
                    ScheduleAppointmentT3.clear();
                    ScheduleAppointmentT4.clear();
                }
            } catch (EmptyFieldException exception) {
                throwAlert("Fields are empty");
            }
        });

        //schedule Appointment Scene
        //View and Edit Patients Scene
        //Buttons
        Button viewAllPatientsb1 = new Button("Go back");
        setStyling(viewAllPatientsb1, 90, 25);
        Button viewAllPatientsb2 = new Button("Edit Patient");
        setStyling(viewAllPatientsb2, 90, 25);
        Button viewAllPatientsb3 = new Button("Appoint bed");
        setStyling(viewAllPatientsb3, 90, 25);
        Button viewAllPatientsb4 = new Button("Free Bed");
        setStyling(viewAllPatientsb4, 90, 25);
        HBox viewAllPatientsHbox = new HBox(viewAllPatientsb1, viewAllPatientsb2, viewAllPatientsb3, viewAllPatientsb4);
        viewAllPatientsHbox.setBackground(background);
        viewAllPatientsHbox.setSpacing(6.6);
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
        viewAllPatientsVbox.setBackground(background);
        //creating the scene for the table
        Scene viewAllPatient = new Scene(viewAllPatientsVbox, 900, 650);
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
                l1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l3 = new Label("Address");
                l3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l4 = new Label("Age");
                l4.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                TextField t1 = new TextField();
                TextField t3 = new TextField();
                TextField t4 = new TextField();
                GridPane gridPane = new GridPane();
                gridPane.setBackground(background);
                gridPane.add(l1, 0, 0);
                gridPane.add(t1, 1, 0);
                gridPane.add(l3, 0, 1);
                gridPane.add(t3, 1, 1);
                gridPane.add(l4, 0, 2);
                gridPane.add(t4, 1, 2);
                viewAllPatientsVbox.getChildren().add(gridPane);
                Patient p = patientTable.getSelectionModel().getSelectedItem();
                Button b = new Button("Submit");
                setStyling(b, 90, 25);
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
                l1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l2 = new Label("days occupied");
                l2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");

                TextField t1 = new TextField();
                TextField t2 = new TextField();
                Button submit = new Button("Submit");
                setStyling(submit, 90, 25);
                GridPane bedGridPane = new GridPane();
                bedGridPane.setBackground(background);
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
                        if (allBeds[Integer.parseInt(t1.getText()) - 1].getisOccupied()) {  // Assuming bed numbers start from 1
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
                        throwAlert("Please Select a patient first");
                    }
                }
        );
        //view and edit Patients Scene


        //Inventory Management Scene //Complete For now
        // buttons for scene
        Button Invob2 = new Button("Update Quantity");
        setStyling(Invob2, 90, 25);
        Button Invob3 = new Button("Update Price");
        setStyling(Invob3, 90, 25);
        Button Invob4 = new Button("Remove");
        setStyling(Invob4, 90, 25);
        Button Invob5 = new Button("Add Item");
        setStyling(Invob5, 90, 25);
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
        setStyling(GobackFromInvtable, 90, 25);
        GobackFromInvtable.setOnAction(e -> {
            ArrayList<Item> Invlist2 = new ArrayList<>(Invlist1);
            stage.setScene(AdminMenuScene);
            GUIinv.setItemsinInventory(Invlist2);
        });
        tableView.getColumns().addAll(invoidColumn, invonameColumn, invoMenufacturerColumn, invoquantityColumn, priceColumn);
        HBox invoOptions = new HBox(GobackFromInvtable, Invob2, Invob3, Invob4, Invob5);
        invoOptions.setBackground(background);
        VBox InvoVerticalBox = new VBox(tableView, invoOptions);
        InvoVerticalBox.setBackground(background);
        invoOptions.setSpacing(10);
        Scene InvoScene2 = new Scene(InvoVerticalBox, 900, 650);
        AdminMenuScene = new Scene(AdminMenu, 900, 650);
        //setting functionality on all the buttons of the scene
        Adminb3.setOnAction(e -> stage.setScene(InvoScene2));
        Invob2.setOnAction(e -> {
            try {
                if (isClicked)
                    throw new ButtonClickedException();
                if (tableView.getSelectionModel().isEmpty())
                    throw new NoOptionSelectedException();
                isClicked = true;
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                setStyling(b1, 90, 26);
                Label l1 = new Label("New Quantity");
                l1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                HBox h1 = new HBox(l1, T1, b1);
                h1.setBackground(background);
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
                l1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                TextField T1 = new TextField();
                Button b1 = new Button("Submit");
                setStyling(b1, 90, 25);
                HBox h1 = new HBox(l1, T1, b1);
                h1.setBackground(background);
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
                l1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l2 = new Label("Name: ");
                l2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l3 = new Label("Manufacturer: ");
                l3.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l4 = new Label("Quantity: ");
                l4.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                Label l5 = new Label("Price: ");
                l5.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-text-fill: white; -fx-font-weight: bold;");
                // Create TextFields for user input
                TextField t1 = new TextField();
                TextField t2 = new TextField();
                TextField t3 = new TextField();
                TextField t4 = new TextField();
                TextField t5 = new TextField();
                // Assuming you have a GridPane named "gridPane"
                GridPane gridPane = new GridPane();
                gridPane.setBackground(background);
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
                setStyling(b, 90, 25);
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
            setStyling(b1, 90, 25);
            Button b2 = new Button("Back");
            setStyling(b2, 90, 25);
            GridPane gridPane = new GridPane();
            gridPane.setBackground(background);
            gridPane.add(b1, 0, 0);
            gridPane.add(b2, 2, 0);
            gridPane.setAlignment(Pos.CENTER);
            TextField t1 = new TextField();
            gridPane.add(t1, 1, 0);
            TableView<Item> tv = new TableView<>();
            tv.getColumns().addAll(invoidColumn, invonameColumn, invoMenufacturerColumn, invoquantityColumn, priceColumn);
            VBox v = new VBox(tv, gridPane);
            tv.setItems(Invlist1);
            v.setBackground(background);
            v.setSpacing(5);
            Scene patientInventory = new Scene(v, 900, 650);
            stage.setScene(patientInventory);
            b2.setOnAction(a -> {
                stage.setScene(PatientMenuScene);
            });
            b1.setOnAction(a -> {
                        try {
                            if (tv.getSelectionModel().isEmpty())
                                throw new NoOptionSelectedException();
                            if (Integer.parseInt(t1.getText()) > tv.getSelectionModel().getSelectedItem().getQuantity()
                                    || Integer.parseInt(t1.getText()) < 0) {
                                throwAlert("We don't have that much quantity");
                            }
                            Item i = tv.getSelectionModel().getSelectedItem();
                            double total = i.getTotal(Integer.parseInt(t1.getText()));
                            ((Patient) CurrentUser[0]).UpdateBilling(total);
                            tv.refresh();

                            tableView.refresh();
                            t1.clear();
                        } catch (NoOptionSelectedException excption) {
                            throwAlert("Please select an item");
                        }
                    }
            );
        });
        //inventory management Scene complete
        stage.setOnCloseRequest(e->
        {
            fileHandling.writeBed(allBeds);
            fileHandling.writeInventory(GUIinv);
            fileHandling.writeObject(allUsers);
            fileHandling.writeNum(num);
        });

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

    private void throwAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.show();
    }

    public void setStyling(Button button, int sizeX, int sizeY) {
        button.setStyle("-fx-background-color: #2497F0 ; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setMinSize(sizeX, sizeY);

        Stream.of(button)
                .forEach(btn -> {
                    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #6A92CC; -fx-text-fill: white;"));
                    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #2497F0 ; -fx-text-fill: white;"));
                });
    }

    //method to intitalize all the users and beds and inventory will not be used after file handling
    public void InitializeUsers() {
      fileHandling.readUsers(allUsers);
      allBeds = fileHandling.readBed();
      GUIinv = fileHandling.readInventory();
      fileHandling.readNum(num);
    }

    public void changePassword(User[] user, Stage stage, Scene s) {
        Label l1 = new Label("Current Password");
        l1.setStyle("-fx" +
                "-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
        Label l2 = new Label("New Password");
        l2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 12;-fx-text-fill: white;  -fx-font-weight: bold;");
        TextField T1 = new TextField();
        TextField T2 = new TextField();
        Button b = new Button("Back");
        setStyling(b, 90, 25);
        GridPane g = new GridPane();
        g.setBackground(background);
        g.add(l1, 0, 0);
        g.add(l2, 0, 1);
        g.add(T1, 1, 0);
        g.add(T2, 1, 1);
        g.add(b, 0, 2);
        b.setOnAction(e -> {
            stage.setScene(s);
            if (T1.getText().equals(user[0].getPassword())) {
                user[0].setPassword(T2.getText());
            }
        });
        g.setAlignment(Pos.CENTER);
        Scene scene = new Scene(g, 900, 650);
        stage.setScene(scene);

    }
}