module org.example {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires gson;

    opens org.example to javafx.fxml;
    exports org.example;
}