module asteroid.com.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens asteroid.com.example to javafx.fxml;
    exports asteroid.com.example;
}
