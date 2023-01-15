package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *  Class allowing user to alter values of existing products.
 */
public class ModifyProductForm implements Initializable
{
    public TextField productIdField;
    public TextField productNameField;
    public TextField productInvField;
    public TextField productPriceField;
    public TextField productMaxField;
    public TextField productMinField;
    public TextField searchPartField;

    public TableColumn<Part, Integer> addPartIdCol;
    public TableColumn<Part, String> addPartNameCol;
    public TableColumn<Part, Integer> addPartInvCol;
    public TableColumn<Part, Double> addPartPriceCol;
    public TableColumn<Part, Integer> removePartIdCol;
    public TableColumn<Part, String> removePartNameCol;
    public TableColumn<Part, Integer> removePartInvCol;
    public TableColumn<Part, Double> removePartPriceCol;

    public TableView<Part> addPartTable;  // Table displaying parts that have not yet been added
    public TableView<Part> removePartTable;  // Table displaying parts that have already been added

    public Button saveProductButton;

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Since part is being modified, all its existing data is filled into the text boxes.
     * The user can then decide which fields need modification.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateFields();
    }

    /**
     *  Overwrite existing product with new modified product. Will print error message to users
     *  if data entered is not acceptable.
     */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException
    {
        int productId, productInv, productMax, productMin, productIndex;
        double productPrice;
        String productName = productNameField.getText();

        productId = Tools.modifyId;  // Product ID for modified product is determined on the MainForm
        productIndex = Inventory.getProductIndex(Tools.modifyId);  // Index of product is needed in order to update it

        try  // Attempt to parse relevant text box inputs to numbers
        {
            productInv = Integer.parseInt(productInvField.getText());
            productMin = Integer.parseInt(productMinField.getText());
            productMax = Integer.parseInt(productMaxField.getText());
            productPrice = Double.parseDouble(productPriceField.getText());
        }
        catch (Exception failedToParse)
        {
            Tools.errorMessage("Invalid Data", "One or more text boxes contains unexpected inputs");
            return;
        }

        if(Objects.equals(productName, ""))  // Verify name inputs are not blank
        {
            Tools.errorMessage("Blank Name", "Product must have valid name");
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

        if(productIndex >= 0)  // Index will be negative if part not found
        {
            Product newProduct = new Product(productId, productName, productPrice, productInv, productMin, productMax);


            for (Part part : associatedParts) // Add all associated parts
            {
                newProduct.addAssociatedPart(part);
            }

            Inventory.updateProduct(productIndex, newProduct);  // Overwrite old product object with new product object
        }
        else
        {
            System.out.println("Dev Info: Could not result ID @ onSaveProduct");  // This will only trigger if there is an unknown bug
        }

        returnToMain(actionEvent);
    }

    /**
     *  Returns user to main menu.
     */
    public void onCancelProduct(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Cancel Product button triggered");
        returnToMain(actionEvent);
    }

    /**
     *  Removes associated part from visual table, no part is actually removed from the product object until user saves changes to product.
     *  User is asked to press confirm before removing.
     */
    public void onRemoveAssociatedPart()
    {
        System.out.println("ActionEvent-> Remove Associated Part button triggered");

        ObservableList<Part> selectedPart;
        selectedPart = removePartTable.getSelectionModel().getSelectedItems(); // Grabs the part information in list form

        int selectedPartId = selectedPart.get(0).getId(); // ID of part to be deleted

        for(int i = 0; i < associatedParts.size(); i++)
        {
            if(associatedParts.get(i).getId() == selectedPartId)
            {
                if(Tools.confirmationMessage("", "Remove Associated Part?"))
                {
                    associatedParts.remove(i);
                }
                return;
            }
        }
    }

    /**
     *  Adds associated part to the visual table, no part is added to the product object until user saves product.
     */
    public void onAddAssociatedPart()
    {
        System.out.println("ActionEvent-> Add Associated Part button triggered");

        ObservableList<Part> selectedPart;
        selectedPart = addPartTable.getSelectionModel().getSelectedItems();  // Grabs the part information in list form

        associatedParts.add(selectedPart.get(0));

        removePartTable.setItems(associatedParts);
        removePartIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        removePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        removePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        removePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *  Activated when user types in search box. Fills addPartTable with result of search.
     *  If search box is empty, table goes back to original state.
     *  Uses lookup function from the Inventory class.
     */
    public void onSearchParts()
    {
        System.out.println("ActionEvent-> Search parts triggered");

        addPartTable.setItems(Inventory.lookupPart(searchPartField.getText()));
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
     *  Returns to main menu.
     */
    public void returnToMain(ActionEvent actionEvent) throws IOException  // Returns to main screen
    {
        Tools.modifyId = -1; // Reset modify ID to default value

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/mainForm.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400); // Length, height
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Adds associated part to the visual table, no part is added to the product object until user saves product.
     */
    public void populateFields()  // Fetches data of part, fills text fields
    {
        if (Tools.modifyId != -1)  // Checks for a valid object ID, modifyId has default state of -1
        {
            Product currentProduct = Inventory.lookupProduct(Tools.modifyId);

            // Populates the text field
            productIdField.setPromptText(Integer.toString(currentProduct.getId()));  // ID has greyed out prompt text
            productNameField.setText(currentProduct.getName());
            productInvField.setText(Integer.toString(currentProduct.getStock()));
            productPriceField.setText(Double.toString(currentProduct.getPrice()));
            productMaxField.setText(Integer.toString(currentProduct.getMax()));
            productMinField.setText(Integer.toString(currentProduct.getMin()));

            // Populates addPartTable (done within a function, so it can be called easily after user searches for parts)
            loadAllParts();

            // // Populates removePartTable TableView (parts associated with product)
            associatedParts.addAll(Inventory.lookupProduct(Tools.modifyId).getAllAssociatedParts());
            removePartTable.setItems(associatedParts);
            removePartIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            removePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            removePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            removePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
        else
        {
            System.out.println("Dev Info: No modify ID was given @ populateFields");
        }
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
}
