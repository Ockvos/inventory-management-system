package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * A class containing tools that are needed across multiple classes.
 */
public class Tools
{
    public static boolean initialLaunch = true;  // True if it's first time user encounters main menu, used to determine how to load test data

    public static int modifyId = -1;  // Used across scenes to determine which object was selected for modification (reset to -1 when done)

    /**
     * Print error message with OK button.
     * @param messageTitle A title given to the error.
     * @param messageDesc Provide a description explaining the error.
     */
    public static void errorMessage(String messageTitle, String messageDesc)  // Print error message with OK button
    {
        // Title is on top, description is below next to OK button
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        errorAlert.setTitle("Issue");
        errorAlert.setHeaderText(messageTitle);
        errorAlert.setContentText(messageDesc);
        errorAlert.showAndWait();
    }

    /**
     * Prints confirmation message with OK and CANCEL button.
     * @param messageTitle A title given to message.
     * @param messageDesc Provide a description about the buttons, appears below messageTitle.
     * @return returns true if user presses OK, false if user selects CANCEL.
     */
    public static boolean confirmationMessage(String messageTitle, String messageDesc)
    {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(messageTitle);
        confirmationAlert.setContentText(messageDesc);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);


        if (button == ButtonType.OK)
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * Prints informative message, can be used to tell the user an event has occurred.
     * @param messageTitle A title given to message.
     * @param messageDesc Provide a description, appears below messageTitle.
     */
    public static void infoMessage(String messageTitle, String messageDesc)
    {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

        infoAlert.setTitle("Notification");
        infoAlert.setHeaderText(messageTitle);
        infoAlert.setContentText(messageDesc);
        infoAlert.showAndWait();
    }
}
