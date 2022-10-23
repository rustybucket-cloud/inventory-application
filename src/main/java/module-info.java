module com.example.inventoryapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventoryapplication to javafx.fxml;
    exports com.example.inventoryapplication;
}