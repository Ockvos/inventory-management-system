package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import model.Inventory;
import model.Part;
import model.Product;
import model.Tools;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *  A class that provides GUI controller and the ability to generate a new product.
 */
public class AddProductForm implements Initializable
{
    public TextField productIdField;
    public TextField productNameField;
    public TextField productInvField;
    public TextField productPriceField;
    public TextField productMaxField;
    public TextField productMinField;
    public TextField searchPartField;

    public TableView<Part> addPartTable;
    public TableView<Part> removePartTable;

    public TableColumn<Part, Integer> addPartIdCol;
    public TableColumn<Part, String> addPartNameCol;
    public TableColumn<Part, Integer> addPartInvCol;
    public TableColumn<Part, Double> addPartPriceCol;
    public TableColumn<Part, Integer> removePartIdCol;
    public TableColumn<Part, String> removePartNameCol;
    public TableColumn<Part, Integer> removePartInvCol;
    public TableColumn<Part, Double> removePartPriceCol;

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();  // Temporarily holds parts associated with product

    int productId;  // ID of product currently being created

    /**
     * Sets up parts table that user can use to associate parts with product.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) // Populate parts table
    {
        loadAllParts();
    }

    /**
     * Loads all existing parts into the addPartsTable.
     */
    public void loadAllParts()
    {
        // Populates addPartTable TableView
        addPartTable.setItems(Inventory.getAllParts());
        addPartIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Generates and adds product and associated parts to inventory, returns to main menu.
     * Product will not be saved until user properly fills out text fields.
     * Method will print relevant error messages if user enters incorrect data type.
     * Activated when user clicks save.
     */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Save Product button triggered");

        productId = Inventory.generateId("Product");  // Generate unique ID
        String productName = productNameField.getText();
        int productInv, productMax, productMin;
        double productPrice;

        try  // Attempt to parse relevant text box inputs to numbers
        {
            productInv = Integer.parseInt(productInvField.getText());
            productPrice = Double.parseDouble(productPriceField.getText());
            productMax = Integer.parseInt(productMaxField.getText());
            productMin = Integer.parseInt(productMinField.getText());
        }
        catch (Exception failedToParse)  // Print an error message to user if parse fails, return without creating product
        {
            Tools.errorMessage("Invalid Data", "One or more text boxes contains unexpected inputs");
            return;
        }

        if(Objects.equals(productName, ""))  // Verify name inputs are not blank
        {
            Tools.errorMessage("Blank Name", "Part name, or company name is blank");
            return;
        }

        if(productInv < 0 || productPrice < 0 || productMax < 0 || productMin < 0)  // Verify user hasn't entered negative numbers
        {
            Tools.errorMessage("Negative Value Entered", "All number values must be positive");
            return;
        }

        if(productMax <= productMin || productMax < productInv || productMin > productInv)  // Verify all numeric values are logical
        {
            Tools.errorMessage("Invalid Numeric Input",
                    "Minimum must be less than Maximum and \nInventory must be between those two values");
            return;
        }

        Inventory.addProduct(new Product(productId, productName, productPrice, productInv, productMin, productMax));  // Create the product

        for (Part associatedPart : associatedParts)
        {
            Inventory.lookupProduct(productId).addAssociatedPart(associatedPart);  // Fetches current product, and adds associated part
        }

        returnToMain(actionEvent);  // Go back to MainForm
    }

    /**
     * Returns user to main menu when cancel button is clicked.
     */
    public void onCancelProduct(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Cancel Product button triggered");
        returnToMain(actionEvent);
    }

    /**
     * Removes part from associated part table.
     */
    public void onRemoveAssociatedPart()
    {
        System.out.println("ActionEvent-> Remove Associated Part button triggered");

        ObservableList<Part> selectedPart = removePartTable.getSelectionModel().getSelectedItems(); // Grabs the part information in list form

        int selectedPartId = selectedPart.get(0).getId(); // ID of part to be deleted

        for(int i = 0; i < associatedParts.size(); i++)  // Loops through all associated parts
        {
            if(associatedParts.get(i).getId() == selectedPartId)  // ID match found
            {
                associatedParts.remove(i);
                return;
            }
        }
    }

    /**
     * Adds part from table containing all parts, to table containing associated parts.
     */
    public void onAddAssociatedPart()
    {
        System.out.println("ActionEvent-> Add Associated Part button triggered");

        ObservableList<Part> selectedPart = addPartTable.getSelectionModel().getSelectedItems();  // Grabs the part information in list form

        associatedParts.add(selectedPart.get(0));  // Add part to temporary list of associated parts

        // Updates bottom table to now contain new part
        removePartTable.setItems(associatedParts);
        removePartIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        removePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        removePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        removePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Populates AddPartTable with results of user's search. Uses lookup function from Inventory class.
     */
    public void onSearchParts()  // Locates part by ID or name
    {
        System.out.println("ActionEvent-> Search parts triggered");

        addPartTable.setItems(Inventory.lookupPart(searchPartField.getText()));  // Lookup function will return a list of parts
        addPartIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addPartTable.setPlaceholder(new Label("Nothing Found, Please Try A Different Input."));

        if(Objects.equals(searchPartField.getText().replaceAll("\\s", ""), ""))  // If input is nothing but spaces, refresh table to original state
        {
            loadAllParts();
        }
    }

    /**
     * Returns user back to main menu.
     */
    public void returnToMain(ActionEvent actionEvent) throws IOException  // Returns to MainForm
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400); // Length, height
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }
}
