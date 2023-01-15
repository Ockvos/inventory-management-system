package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Used to initially launch main stage of application.
 */
public class Main extends Application
{
    /**
     * Loads mainForm, gives window size.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        primaryStage.setTitle("Inventory System"); // Window name
        primaryStage.setScene(new Scene(root, 1000, 400));  // Window size (horizontal, vertical)

        primaryStage.show();
    }

    /**
     * main method of application.
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
