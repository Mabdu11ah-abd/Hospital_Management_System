package com.example.hospital_management_system;

public class User {
    protected String name;
    protected String ID;
    protected String Password;
    // Admin, Doctor ,Patient

    public void RegisterUser(String username, String password,String ID) {
        setname(username);
        setPassword(password);
        setID(ID);        
    }

    public String getname() {
        return name;
    }
    public void setname(String username) {
        this.name = username;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        this.Password = password;
    }
    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    // Method to authenticate the user
    public boolean login(String enteredID, String enteredPassword) {
        if (enteredID.equals(ID) && enteredPassword.equals(Password)) {
            return true;
        } else {
            return false;
        }
    }
    // Method to logout.
    public void logout() {
        System.out.println("Logging out. Goodbye, " + name + "!");
    }
}
