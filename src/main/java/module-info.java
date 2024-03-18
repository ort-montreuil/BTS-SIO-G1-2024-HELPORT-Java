module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports com.example.demo;
    exports com.example.demo.Entity;

    opens com.example.demo.Entity to javafx.base;
    opens com.example.demo to javafx.fxml;
}