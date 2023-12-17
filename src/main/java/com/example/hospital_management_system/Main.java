package com.example.hospital_management_system;

import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        FileHandling f = new FileHandling();
        // array lists holding the total number of doctors, beds, and patients
        ArrayList<User> allUsers = new ArrayList<>(100);
        f.readUsers(allUsers);
        Bed[] allBeds = f.readBed();
        // inventory object
        Inventory myInventory =  f.readInventory();
        // Scanner variable for user input
        Scanner input = new Scanner(System.in);
        // primary program loop exiting this loop exits the program
        while (true) {
            User CurrentUser = null; // variable that stores the current user
            int Choice;// Choice variable to be used for choices throughout the program
            // first Loop for Login menu
            while (CurrentUser == null) {
                System.out.println("Enter 1 to login, enter 2 to login as new patient : "); // taking user's
                                                                                            // choice
                Choice = input.nextInt();
                input.nextLine();// clearing input buffer
                if (Choice == 1) {
                    CurrentUser = loginUser(allUsers, input);// login user method returns User type variable
                } else if (Choice == 2) {
                    System.out.println("Do you want to register as a new User or Login, Enter 1 or 2: ");
                    Choice = input.nextInt();// giving only Patient the option to register doctor will be added by admin
                    input.nextLine(); // only
                    Patient newPatient = new Patient();
                    newPatient.SetPatient();
                    allUsers.add(newPatient);// adding new patient to users after setting
                    CurrentUser = newPatient;// assigning patient to current user

                } else {
                    System.out.println("Invalid Choice. Give input again: ");// input validation for login
                }
            }
            // Doctor portion of program
            if (CurrentUser instanceof Doctor) {
                do {
                    System.out.println(
                            "Enter 1 to view Patient: \nEnter 2 to Write Prescription\n Enter 3 to Update Patient Data\nEnter 4 to Manage Appointments\n0 to exit ");
                    Choice = input.nextInt();
                    input.nextLine();// clearing input buffer
                    if (Choice == 1) {
                        ((Doctor) CurrentUser).ViewPatients();// allow doctor to view either one patient or all patients
                    } else if (Choice == 2) {
                        ((Doctor) CurrentUser).WritePrescription();// allow doctor to write prescription for patient of
                                                                   // his choice
                    } else if (Choice == 3) {
                        System.out.println("Enter 1 to update Medical Record Dates\nEnter 2 to Update patient Notes");
                        ((Doctor) CurrentUser).UpdatePatientNotes();// allow doctor to update patient
                    } else if (Choice == 4) {
                        ((Doctor) CurrentUser).ManageAppointments();// allow doctor to view and manage appointments
                    } else if (Choice == 0) {
                        System.out.println("Exited Successfully: ");
                    } else {
                        System.out.println("Invalid input. Enter again: ");
                    }
                } while (Choice != 0);// Loop runs till not equal to zero

                // Patient Portion of program
            } else if (CurrentUser instanceof Patient) {
                while (true) {// Loop runs till not equal to zero

                    System.out.println(
                        "Enter 1 To view Current Information \nEnter 2 to view Medical Record:\nEnter 3 to Buy Medicine from Inventory:\nEnter 4 to generate Invoice :  \nEnter 5 to view Available Doctors:\nEnter 6 to Book Doctor:\nEnter 0 to Exit: ");
                    int patientChoice = input.nextInt();
                    input.nextLine();// Consuming new line Character
                    if (patientChoice == 0) {
                        System.out.println("Exited : ");
                        break;// breaks the loop
                    } else {
                        if (patientChoice == 1) {
                            System.out.println(CurrentUser);// printing the current information of the Character
                        } else if (patientChoice == 2) {
                            ((Patient) CurrentUser).getRecord().viewMedicalRecord(); // to view the current
                                                                                     // Medicalrecord of the user
                        } else if (patientChoice == 3) {
                            System.out.println("Enter 1 to view Available items\n Enter 2 to Buy items:\n 0 to exit: ");
                            myInventory.Displayinventory();// display all current available items to the user
                            double bill = myInventory.BuyItems();// Bill of the items that user has bought are stored
                            ((Patient) CurrentUser).UpdateBilling(bill);// amount added to bill of patient
                        } else if (patientChoice == 4) {
                            ((Patient) CurrentUser).getBill().GenerateInvoice((Patient) CurrentUser);
                            // prints bill based on items bought and daysofbedoccupied bed occupied
                            // controlled by admin
                        } else if (patientChoice == 5) {
                            System.out.println(// shows patient all available doctors based on the specialization they
                                               // chose
                                    "What type of Doctor do you want to visit:\nTypeA\nTypeB\nTypeC\nTypeD\nTypeE");
                            int docChoice = input.nextInt();
                            input.nextLine();// consume newline character
                            if (docChoice == 1) {
                                ViewSpecialist("TypeA", allUsers);
                            } else if (docChoice == 2) {
                                ViewSpecialist("TypeB", allUsers);
                            } else if (docChoice == 3) {
                                ViewSpecialist("TypeC", allUsers);
                            } else if (docChoice == 4) {
                                ViewSpecialist("TypeD", allUsers);
                            } else if (docChoice == 5) {
                                ViewSpecialist("TypeE", allUsers);
                            } else {
                                System.out.println("Invalid Output");
                            }
                        } else if (patientChoice == 6) // assigns patient to doctor of their choice
                        {
                            System.out.println("Enter the id of doctor you want to be assigned to: ");
                            String tempID = input.nextLine();
                            if (searchUsers(tempID, allUsers) != -1) {// condition checks if doctor id entered is real
                                ((Patient) CurrentUser)
                                        .setDoctorIncharge((Doctor) allUsers.get(searchUsers(tempID, allUsers)));// sets
                                                                                                                 // doctor
                                Doctor temp = (Doctor) allUsers.get(searchUsers(tempID, allUsers));
                                temp.assignpatient((Patient) CurrentUser);// assign corresponding patient to doctor
                            } else {
                                System.out.println("Incorrect Input: ");// input validation
                            }
                        } else {
                            System.out.println("Wrong choice: ");
                        }
                    }
                }
            } else if (CurrentUser.getID().equals("A-1")) {// admin manages doctors,beds ,patient data ,inventory
                while (true) {
                    System.out.println(
                            "1 to add Doctor:\n2 to schedule Appointment:\n3 Manage inventory:\n5 Edit patients\n6 to view all patients\n0 to exit ");
                    Choice = input.nextInt();
                    input.nextLine();// consume newline
                    if (Choice == 0) {
                        System.out.println("Exited");
                        break;// break and exit the loop
                    }
                    if (Choice == 1) {
                        Doctor newdoc = new Doctor();
                        System.out.println("How many patients do you want to add to the doctor ");
                        int num = input.nextInt();
                        input.nextLine();
                        for (int i = 0; i < num; i++) {
                            System.out.println("Enter the ID of the Patient you want to add: ");
                            String searchPatientsID = input.nextLine();
                            if (searchUsers(searchPatientsID, allUsers) != -1) {
                                newdoc.assignpatient((Patient) allUsers.get(searchUsers(searchPatientsID, allUsers)));
                            }
                        }
                        allUsers.add(newdoc); // adds new doctor to the array list of users
                    } else if (Choice == 2) {
                        // to schedule Appointments :
                        // method searches and makes temporary objects that point to same memory
                        // location as alluser obj
                        System.out.println("Enter Id of the Doctor for whom you want to schedule Appointment: ");
                        String tempID = input.nextLine();
                        Doctor dtemp = null;
                        if (searchUsers(tempID, allUsers) != -1) {
                            dtemp = (Doctor) allUsers.get(searchUsers(tempID, allUsers));
                        } else {
                            System.out.println("Doctor does not Exist: ");
                        }
                        System.out.println("Enter ID of Patient : ");
                        Patient ptemp = null;
                        tempID = input.nextLine();
                        if (searchUsers(tempID, allUsers) != -1) {
                            ptemp = (Patient) allUsers.get(searchUsers(tempID, allUsers));
                        } else {
                            System.out.println("Patient Does not Exist");
                        }
                        Appointment tempAppointment = new Appointment(ptemp, dtemp);
                        dtemp.addAppointment(tempAppointment);
                    } else if (Choice == 3) {
                        // Complete
                        System.out.println("1 to add item, 2 to update item, 3 to view Inventory: ");
                        Choice = input.nextInt();
                        if (Choice == 1) {
                            myInventory.Addinventory();
                        } else if (Choice == 2) {
                            myInventory.updateinventory();
                        } else if (Choice == 3) {
                            myInventory.Displayinventory();
                        }
                    } else if (Choice == 5) {
                        System.out.println("Enter 1 to assign bed to patient : ");
                        System.out.println("Enter 2 to update Patient Information : ");
                        System.out.println("Enter 3 to vacate bed from Patient : ");
                        Choice = input.nextInt();
                        input.nextLine();
                        if (Choice == 1) {
                            System.out.println("Enter Bed number to assign to patient : ");
                            int bednumber = input.nextInt();
                            if (!allBeds[bednumber-1].getisOccupied()) {
                                System.out.println("enter Number of days bed has been Occupied  : ");
                                allBeds[bednumber-1].setDaysOccupied(input.nextInt());
                                input.nextLine();
                                System.out.println("Enter the ID of patient Occupying Bed : ");
                                String tempID = input.nextLine();
                                if (searchUsers(tempID, allUsers) != 1) {
                                    allBeds[bednumber-1].setOccupied();    
                                    ((Patient) allUsers.get(searchUsers(tempID, allUsers)))
                                            .setBedUsed(allBeds[bednumber-1]);
                                } else {
                                    System.out.println("Incorrect ID has been Entered : ");
                                }
                                System.out.println("Bed has been Assigned : ");
                            } else {
                                System.out.println("Bed is already Occupied ");
                            }
                        } else if (Choice == 2) {
                            System.out.println("Enter Patient ID that you want to update : ");
                            String searchID = input.nextLine();
                            if (searchUsers(searchID, allUsers) != 1) {
                                ((Patient) allUsers.get(searchUsers(searchID, allUsers))).SetPatient();
                            }
                        } else if (Choice == 3) {
                            System.out.println("Vacating Bed : ");
                            System.out.println("Enter the ID of patient Occupying Bed : ");
                            String tempID = input.nextLine();
                            if (searchUsers(tempID, allUsers) != 1) {
                                Patient temp = (Patient) allUsers.get(searchUsers(tempID, allUsers));
                                temp.getBill().finalPayment(temp);
                                temp.getBedUsed().vacantBed();
                                temp.setBedUsed(null);
                            }
                        }
                        else {
                            System.out.println("Invalid Choice : ");
                        }
                    } else if (Choice == 6) {
                        System.out.println("List of Patients : ");
                        for (User user : allUsers) {
                            if (user instanceof Patient) {
                                Patient patient = (Patient) user;
                                System.out.println(patient);
                            }
                        }
                    } else {
                        System.out.println("An error has occurred: ");
                    }
                }
            }
        f.writeObject(allUsers);
        f.writeBed(allBeds);
        f.writeInventory(myInventory);
        }
    }

    private static User loginUser(ArrayList<User> allUsers, Scanner input) {
        System.out.println("Enter Your ID and Password: ");
        String tempID = input.nextLine();
        String tempPassword = input.nextLine();
        for (User user : allUsers) {
            if (user.login(tempID, tempPassword)) {
                System.out.println("Logged in Successfully: ");
                return user;
            }
        }
        return null;
    }

    public static void ViewSpecialist(String type, ArrayList<User> allUsers) {
        for (User user : allUsers) {
            if (user instanceof Doctor) {
                Doctor d = (Doctor) user;
                if (d.getSpecialization().equals(type))
                    System.out.println(d);
            }
        }
    }

    private static int searchUsers(String id, ArrayList<User> users) {
        for (int index = 0; index < users.size(); index++) {
            User currentUser = users.get(index);
            String currentUserID = currentUser.getID();

            if (id.equals(currentUserID)) {
                return index;
            }

        }

        System.out.println("ID not found");
        return -1;
    }
}
