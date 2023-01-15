package controller;

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

import model.Inventory;
import model.Part;
import model.Product;
import model.Tools;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A controller that handles main menu of application.
 */
public class MainForm implements Initializable
{
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partInvCol;
    public TableColumn<Part, Double> partPriceCol;

    public TableColumn<Product, Integer> productIdCol;
    public TableColumn<Product, String> productNameCol;
    public TableColumn<Product, Integer> productInvCol;
    public TableColumn<Product, Double> productPriceCol;

    public TableView<Part> partTable;
    public TableView<Product> productTable;

    public TextField partSearchField;
    public TextField productSearchField;

    /**
     * Populates part and product table fields with information from user generated objects.
     * Additionally, tables can be populated with test data if Inventory.loadTestData = true.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loadAllParts();
        loadAllProducts();

        Tools.initialLaunch = false;  // Used keep test data from loading duplicates
    }

    /**
     * Switches to AddPartForm when user clicks "Add" button underneath part table.
     */
    public void onAddParts(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Add parts button triggered");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addPartForm.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 600); // Length, height
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Switches to modifyPartForm when user clicks "Modify" button underneath part table.
     * Part ID of part being clicked is fetched and assigned to Inventory.modifyId this
     * ID is used in modifyPartForm to know which part to alter.
     */
    public void onPartModify(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Modify parts button triggered");

        ObservableList<Part> selectedPart = partTable.getSelectionModel().getSelectedItems();  // Grabs the part information in list form

        if (selectedPart.size() > 0)  // Checks to verify user has selected a row
        {
            Tools.modifyId = selectedPart.get(0).getId();  // modifyId is used to transfer part ID across scenes

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyPartForm.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 600); // Length, height
            stage.setTitle("Inventory System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes selected part from list. Asks user to confirm deletion.
     * Will notify user if deletion fails to execute.
     */
    public void onPartDelete()
    {
        System.out.println("ActionEvent-> Part delete triggered");

        ObservableList<Part> selection = partTable.getSelectionModel().getSelectedItems();  // Fetches the part information in list form
        boolean successfulDeletion = false;

        if (selection.size() > 0) // Checks to verify user has selected a row
        {
            if(Tools.confirmationMessage("","Permanently Delete Part?"))
            {
                successfulDeletion = Inventory.deletePart(selection.get(0)); // Will return false if deletion fails
            }
        }

        if(!successfulDeletion)  // This will trigger due to user pressing button before selecting part, or when hitting cancel on confirmation message
        {
            Tools.infoMessage("Deletion Not Executed", "");
        }

        onSearchParts();  // If search box is not empty, changes will not be reflected visually, unless this is called

    }

    /**
     * Switches to AddProductForm when user clicks "Add" button underneath product table.
     */
    public void onProductAdd(ActionEvent actionEvent) throws IOException  // Switches to AddProductForm
    {
        System.out.println("ActionEvent-> Add products button triggered");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addProductForm.fxml")));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 500); // Length, height
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches to modifyProductForm when user clicks "Modify" button underneath product table.
     * Product ID of product being clicked is fetched and assigned to Inventory.modifyId this
     * ID is used in modifyProductForm to know which product to alter.
     */
    public void onProductModify(ActionEvent actionEvent) throws IOException
    {
        System.out.println("ActionEvent-> Modify products button triggered");

        ObservableList<Product> selectedProduct;
        selectedProduct = productTable.getSelectionModel().getSelectedItems();

        if (selectedProduct.size() > 0)  // Checks to verify user has selected a row
        {
            Tools.modifyId = selectedProduct.get(0).getId(); // modifyId is used to pass the product ID between scenes

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyProductForm.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 500); // Length, height
            stage.setTitle("Inventory System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes selected product from list. Asks user to confirm deletion.
     * Will notify user if deletion fails to execute.
     * Products with associated parts will not be deleted.
     */
    public void onProductDelete()
    {
        System.out.println("ActionEvent-> Delete products button triggered");

        ObservableList<Product> selection = productTable.getSelectionModel().getSelectedItems();  // Fetches the product information in list form
        boolean successfulDeletion = false;

        if (selection.size() > 0) // Checks to verify user has selected a row
        {
            Product selectedProduct = selection.get(0);

            if(!(selectedProduct.getAllAssociatedParts().size() > 0))  // Checks product has no associated parts
            {
                if (Tools.confirmationMessage(" ", "Permanently Delete Product?"))  // User must confirm deletion
                {
                    successfulDeletion = Inventory.deleteProduct(selectedProduct); // Will equal true if product deleted
                }
            }
            else
            {
                Tools.errorMessage("Cannot Delete This Product", "Products with parts associated cannot be deleted");
            }
        }

        if(!successfulDeletion)  // This will trigger due to user pressing button before selecting product, or when hitting cancel on confirmation message
        {
            Tools.infoMessage("Select A Valid Part To Delete", "");
        }

        onSearchProducts();  // If search box is not empty, changes will not be reflected visually, unless this is called
    }

    /**
     *  Activated when user types in search box. Fills part table with result of search.
     *  If search box is empty, table goes back to original state.
     *  Uses lookup function from the Inventory class.
     */
    public void onSearchParts()
    {
        System.out.println("ActionEvent-> Search parts triggered");


        partTable.setItems(Inventory.lookupPart(partSearchField.getText()));
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setPlaceholder(new Label("Nothing Found, Please Try A Different Input."));

        if(Objects.equals(partSearchField.getText().replaceAll("\\s", ""), ""))  // If input is nothing but spaces, refresh table to original state
        {
            loadAllParts();
        }
    }

    /**
     * Populates product table with results of user's search.
     * Uses lookup function from Inventory class.
     * Triggers when interacting with search box.
     */
    public void onSearchProducts()
    {
        System.out.println("ActionEvent-> Search products triggered");

        productTable.setItems(Inventory.lookupProduct(productSearchField.getText()));  // Set items to search result
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setPlaceholder(new Label("Nothing Found, Please Try A Different Input."));

        if(Objects.equals(productSearchField.getText().replaceAll("\\s", ""), ""))  // If input is nothing but spaces, refresh table to original state
        {
            loadAllProducts();
        }
    }

    /**
     * Fetches all parts in existence, loads them into table.
     * Will also load test data if Inventory.loadTestData = true.
     */
    public void loadAllParts()
    {
        // Fetches data to populate PART table views
        partTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setPlaceholder(new Label("Currently No Parts")); // Will appear if all parts are deleted
    }

    /**
     * Fetches all products in existence, loads them into table.
     * Will also load test data if Inventory.loadTestData = true.
     */
    public void loadAllProducts()
    {
        // Fetches data to populate PRODUCT table views
        productTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setPlaceholder(new Label("Currently No Products")); // Will appear if all parts are deleted
    }

    /**
     * Close application with exit code 0.
     * Activated when user clicks "Exit" button.
     */
    public void onExit()
    {
        System.exit(0);
    }
}