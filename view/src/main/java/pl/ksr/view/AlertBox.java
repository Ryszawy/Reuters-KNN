package pl.ksr.view;

import javafx.scene.control.Alert;

public class AlertBox {
    public static final void messageBox(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
