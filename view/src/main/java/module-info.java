module com.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires logic;

    opens pl.ksr.view to javafx.fxml;
    exports pl.ksr.view;
}