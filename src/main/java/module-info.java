module com.example.hospital_management_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hospital_management_system to javafx.fxml;
    exports com.example.hospital_management_system;
}