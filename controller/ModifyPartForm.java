package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Tools;

/**
 *  A class that provides GUI controller and the ability to modify an existing part.
 */
public class ModifyPartForm implements Initializable
{
    public RadioButton outsourcedButton;
    public RadioButton inHouseButton;

    public TextField partIdField;
    public TextField partNameField;
    public TextField partInvField;
    public TextField partPriceField;
    public TextField partMaxField;
    public TextField partSource;
    public TextField partMinField;

    public ToggleGroup partToggleGroup;

    public Label machineIdLabel;

    /**
     * Calls a function that fetches all data associated with part.
     */
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
       populateFields();
    }

    /**
     * Returns user to main menu when cancel button is clicked.
     */
    public void onCancelPart(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Cancel parts button triggered");
        returnToMain(actionEvent);
    }

    /**
     * Activated when user clicks save. This method is used to determine which type of part to create, and then
     * return user to main menu.
     */
    public void onSavePart(ActionEvent actionEvent) throws IOException  // Add part to inventory, return to MainForm
    {
        System.out.println("ActionEvent-> Save parts button triggered");

        boolean successfulInput; // Will not return to main menu until true


        if (outsourcedButton.isSelected())
        {
            successfulInput = createOutsourcedPart();
        }
        else  // The inHouseButton is selected by default
        {
            successfulInput = createInHousePart();
        }

        if(successfulInput)  // Part was created, return to main menu
        {
            returnToMain(actionEvent);
        }
    }

    /**
     * Creates a part that is used to overwrite an existing part.
     * Will not create part until user properly fills out text fields.
     * Prints error message to user if data given is incorrect.
     *
     * @return true if part was created, false if data given cannot be used.
     */
    public boolean createOutsourcedPart()
    {
        int partId = Tools.modifyId;
        int partIndex = Inventory.getPartIndex(partId); // Will be needed to update part
        String partName = partNameField.getText();
        String partCompanyName = partSource.getText();
        int partInv, partMax, partMin;
        double partPrice;

        try  // Attempt to parse relevant text box inputs to numbers
        {
            partInv = Integer.parseInt(partInvField.getText());
            partPrice = Double.parseDouble(partPriceField.getText());
            partMax = Integer.parseInt(partMaxField.getText());
            partMin = Integer.parseInt(partMinField.getText());
        }
        catch (Exception failedToParse)
        {
            Tools.errorMessage("Invalid Data", "One or more text boxes contains unexpected inputs");
            return false;
        }

        if(Objects.equals(partName, "") || Objects.equals(partCompanyName, ""))  // Verify name inputs are not blank
        {
            Tools.errorMessage("Blank Name", "Part name, or company name is blank");
            return false;
        }

        if(partInv < 0 || partPrice < 0 || partMax < 0 || partMin < 0)  // Verify user hasn't entered negative numbers
        {
            Tools.errorMessage("Negative Value Entered", "All number values must be positive");
            return false;
        }

        if(partMax <= partMin || partMax < partInv || partMin > partInv)  // Verify all numeric values are logical
        {
            Tools.errorMessage("Invalid Numeric Input",
                    "Minimum must be less than Maximum and \nInventory must be between those two values");
            return false;
        }

        Inventory.updatePart(partIndex, new Outsourced(partId, partName, partPrice, partInv, partMin, partMax, partCompanyName));
        System.out.println("ObjectCreated-> Outsourced Part");

        return true;
    }

    /**
     * Creates a part that is used to overwrite an existing part.
     * Will not create part until user properly fills out text fields.
     * Prints error message to user if data given is incorrect.
     *
     * @return true if part was created, false if data given cannot be used.
     */
    public boolean createInHousePart()
    {
        int partId = Tools.modifyId;
        int partIndex = Inventory.getPartIndex(partId); // Will be needed to update part
        String partName = partNameField.getText();
        int partMachineId, partInv, partMax, partMin;
        double partPrice;


        try  // Attempt to parse relevant text box inputs to numbers
        {
            partInv = Integer.parseUnsignedInt(partInvField.getText());
            partPrice = Double.parseDouble(partPriceField.getText());
            partMax = Integer.parseInt(partMaxField.getText());
            partMin = Integer.parseInt(partMinField.getText());
            partMachineId = Integer.parseInt(partSource.getText());
        }
        catch (Exception failedToParse)
        {
            Tools.errorMessage("Invalid Data", "One or more text boxes contain unexpected inputs");
            return false;
        }

        if(Objects.equals(partName, ""))  // Verify name inputs are not blank
        {
            Tools.errorMessage("Blank Name", "Part name, or company name is blank");
            return false;
        }

        if(partInv < 0 || partPrice < 0 || partMax < 0 || partMin < 0)  // Verify user hasn't entered negative numbers
        {
            Tools.errorMessage("Negative Value Entered", "All number values must be positive");
            return false;
        }

        if(partMax <= partMin || partMax < partInv || partMin > partInv)  // Verify all numeric values are logical
        {
            Tools.errorMessage("Invalid Numeric Input",
                    "Minimum must be less than Maximum and \nInventory must be between those two values");
            return false;
        }

        Inventory.updatePart(partIndex, new InHouse(partId, partName, partPrice, partInv, partMin, partMax, partMachineId));
        System.out.println("ObjectCreated-> In-House Part");

        return true;
    }

    /**
     * Changes final text field name to "Machine ID", clears its existing text.
     */
    public void onInHouse()
    {
        System.out.println("ActionEvent-> In-house radio button triggered");
        machineIdLabel.setText("Machine ID");
        partSource.clear();
    }

    /**
     * Changes final text field name to "Company Name", clears its existing text.
     */
    public void onOutsourced()
    {
        System.out.println("ActionEvent-> Outsourced radio button triggered");
        machineIdLabel.setText("Company Name");
        partSource.clear();

    }

    /**
     * Returns user back to main menu.
     */
    public void returnToMain(ActionEvent actionEvent) throws IOException
    {
        Tools.modifyId = -1;  // Reset the modifyId to default state of -1

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400); // Length, height
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Fetches data of part being modified, fills text fields with the existing values.
     */
    public void populateFields()
    {
        if (Tools.modifyId != -1)  // Checks for a valid object ID
        {
            if(Inventory.lookupPart(Tools.modifyId).getClass() == model.InHouse.class) // Checks if part is InHouse or Outsourced
            {
                InHouse currentPart = (InHouse) Inventory.lookupPart(Tools.modifyId);

                machineIdLabel.setText("Machine ID");

                partIdField.setPromptText(Integer.toString(currentPart.getId()));
                partNameField.setText(currentPart.getName());
                partInvField.setText(Integer.toString(currentPart.getStock()));
                partPriceField.setText(Double.toString(currentPart.getPrice()));
                partMaxField.setText(Integer.toString(currentPart.getMax()));
                partMinField.setText(Integer.toString(currentPart.getMin()));
                partSource.setText(Integer.toString(currentPart.getMachineID()));

            }
            else  // If part is not InHouse, it must be Outsourced
            {
                Outsourced currentPart = (Outsourced) Inventory.lookupPart(Tools.modifyId);

                outsourcedButton.setSelected(true);
                machineIdLabel.setText("Company Name");

                partIdField.setPromptText(Integer.toString(currentPart.getId()));
                partNameField.setText(currentPart.getName());
                partInvField.setText(Integer.toString(currentPart.getStock()));
                partPriceField.setText(Double.toString(currentPart.getPrice()));
                partMaxField.setText(Integer.toString(currentPart.getMax()));
                partMinField.setText(Integer.toString(currentPart.getMin()));
                partSource.setText(currentPart.getCompanyName());
            }
        }
        else
        {
            System.out.println("ERROR: No modify ID was given");
        }
    }
}
