package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 *  A class used to manage inventory storage and searching of parts and products.
 *  Class also contains test data which can be loaded by setting loadTestData = true.
 */
public class Inventory
{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static boolean loadTestData = true;  // If true, pre-written test data will display in GUI

    private static final Product largeCar = new Product(109, "Deluxe SUV", 42500.0, 12, 4, 24);
    private static final Product smallCar = new Product(102, "Hatchback", 15499.99, 43, 10, 50);
    private static final Product emptyCar = new Product(113, "Empty Vehicle", 474.99, 2, 0, 2);

    private static final Part tire = new InHouse(32, "A/T Tire", 127.49, 278, 100, 400, 4201);
    private static final Part largeEngine = new InHouse(98, "5.0L Engine", 2499.99, 31, 25, 150, 4321);
    private static final Part smallEngine = new InHouse(87, "2.0L Engine", 1299.99, 47, 45, 175, 7702);
    private static final Part frame = new InHouse(76,"Coated Frame", 1749.25,92, 35,115, 5078);
    private static final Part radio = new Outsourced(41, "Radio", 134.98, 88, 60, 90, "Sonic Sound");
    private static final Part winch = new Outsourced(24,"8000lbs Winch", 589.79,3, 1,20, "PowerPlus");

    /**
     * Adds new part to list containing all parts.
     * @param newPart part to be added.
     */
    static public void addPart(Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * Adds new product to list containing all products.
     * @param newProduct product to be added.
     */
    static public void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
    }

    /**
     * Finds and returns part based on given integer ID.
     * @param partId numeric ID of part in question.
     */
    static public Part lookupPart(int partId)
    {
        for (Part currentPart : allParts) {
            if (currentPart.getId() == partId)
            {
                return currentPart;  // Part is found, object returned
            }
        }
        return null;  // Nothing is found
    }

    /**
     * Finds and returns product based on given integer ID.
     * @param productId numeric ID of product in question.
     * @return null if no product found, product object if found.
     */
    static public Product lookupProduct(int productId)
    {
        for (Product currentProduct : allProducts) {
            if (currentProduct.getId() == productId)
            {
                return currentProduct;  // Product is found, object returned
            }
        }
        return null;
    }


    /**
     * Finds and returns part(s) based on string input. Blank spaces and capitalization will not
     * influence search results.
     * @param partName can be part ID, or name, or both.
     * @return list containing all parts matching input.
     */
    static public ObservableList<Part> lookupPart(String partName)
    {

        partName = partName.toLowerCase().replaceAll("\\s", ""); // Take user input, remove all spaces and convert everything to lowercase

        ObservableList<Part> foundPartList = FXCollections.observableArrayList();  // Stores all parts that match search
        boolean nameFound;
        boolean idFound;
        boolean bothFound;

        for (Part currentPart : allParts)
        {
            String currentPartName = currentPart.getName().toLowerCase().replaceAll("\\s", "");  // Remove uppercase and spaces
            String currentPartId = Integer.toString(currentPart.getId()).toLowerCase().replaceAll("\\s", "");  // Remove uppercase and spaces
            String currentPartBoth = currentPartId + currentPartName;

            nameFound = currentPartId.contains(partName);
            idFound = currentPartName.contains(partName);
            bothFound = currentPartBoth.contains(partName);

            if (nameFound || idFound || bothFound)
            {
                foundPartList.add(currentPart);
            }
        }
            return foundPartList;
    }

    /**
     * Finds and returns product(s) based on string input. Blank spaces and capitalization will not
     * influence search results.
     * @param productName can be product ID, or name, or both.
     * @return list containing all product matching input.
     */
    static public ObservableList<Product> lookupProduct(String productName)
    {
        productName = productName.toLowerCase().replaceAll("\\s", "");  // Take user input, remove all spaces and convert everything to lowercase

        ObservableList<Product> foundProductList = FXCollections.observableArrayList();  // Stores all parts that match search
        boolean nameFound;
        boolean idFound;
        boolean bothFound;

        for (Product currentProduct : allProducts)
        {

            String currentProductName = currentProduct.getName().toLowerCase().replaceAll("\\s", "");  // Remove uppercase and spaces
            String currentProductId = Integer.toString(currentProduct.getId()).toLowerCase().replaceAll("\\s", "");  // Remove uppercase and spaces
            String currentProductBoth = currentProductId + currentProductName;

            nameFound = currentProductId.contains(productName);
            idFound = currentProductName.contains(productName);
            bothFound = currentProductBoth.contains(productName);

            if (nameFound || idFound || bothFound)
            {
                foundProductList.add(currentProduct);
            }
        }
        return foundProductList;
    }

    /**
     * Replaces part with new part object.
     * @param index location of part in list.
     * @param selectedPart new part to be used.
     */
    static public void updatePart(int index, Part selectedPart)
    {
        if (0 <= index && index < allParts.size())  // Verify index is valid
        {
            allParts.set(index, selectedPart);
        }
        else
        {
            System.out.println("Debug Info: Invalid index was given when updating part");
        }
    }

    /**
     * Replaces product with new product object.
     * @param index location of part in list.
     * @param newProduct product to be used.
     */
    static public void updateProduct(int index, Product newProduct)
    {
        if (0 <= index && index < allProducts.size())  // Verify index is valid
        {
            allProducts.set(index, newProduct);
        }
        else
        {
            System.out.println("Debug Info (updateProduct): Invalid index was given when updating part");
        }
    }

    /**
     * Deletes part from list containing all parts.
     * @param selectedPart part to be removed.
     * @return true if part was found and deleted, false if part was not located.
     */
    static public boolean deletePart(Part selectedPart)
    {
        System.out.println(selectedPart.getId());
        for (int i = 0; i < allParts.size(); i++)
        {
            Part currentPart = allParts.get(i);

            if (currentPart.getId() == selectedPart.getId())  // Part ID is a match, remove it
            {
                allParts.remove(i);
                return true;
            }
        }
        System.out.println("Debug Info (deletePart): Failed to delete part");
        return false; // Loop is complete with part never found
    }

    /**
     * Deletes product from list containing all products.
     * @param selectedProduct product to be removed.
     * @return true if product was found and deleted, false if product was not located.
     */
    static public boolean deleteProduct(Product selectedProduct)
    {
        for (int i = 0; i < allProducts.size(); i++)
        {
            Product currentProduct = allProducts.get(i);

            if (currentProduct.getId() == selectedProduct.getId())  // Product ID is a match, remove it
            {
                allProducts.remove(i);
                return true;
            }
        }
        System.out.println("Debug Info (deleteProduct): Failed to delete product");
        return false; // Loop is complete with part never found
    }

    /**
     * Returns list of all parts.
     * @return ObservableList containing all parts.
     */
    static public ObservableList<Part> getAllParts()
    {
        if (loadTestData && Tools.initialLaunch)  // Run checks to make sure test data won't be duplicated when changing scenes
        {
            allParts.add(largeEngine);
            allParts.add(smallEngine);
            allParts.add(tire);
            allParts.add(frame);
            allParts.add(radio);
            allParts.add(winch);
        }
        return allParts;
    }

    /**
     * Returns list of all products.
     * @return ObservableList containing all products.
     */
    static public ObservableList<Product> getAllProducts()
    {
        if (loadTestData && Tools.initialLaunch) // Stops test parts and products from duplicating
        {
            // Add parts to product1
            largeCar.addAssociatedPart(largeEngine);
            largeCar.addAssociatedPart(frame);
            largeCar.addAssociatedPart(tire);
            largeCar.addAssociatedPart(winch);
            allProducts.add(largeCar);

            // Add parts to product2
            smallCar.addAssociatedPart(smallEngine);
            smallCar.addAssociatedPart(frame);
            smallCar.addAssociatedPart(tire);
            allProducts.add(smallCar);

            // Add product3 (Used to show only products with no parts can be deleted)
            allProducts.add(emptyCar);
        }

        return allProducts;
    }

    /**
     * Prints all parts to the console. Used during development prior to GUI existing.
     */
    static public void printAllParts()
    {
        for (Part currentPart : allParts) {
            System.out.println("   ID: (" + currentPart.getId() + ")");
            System.out.println("   Name: (" + currentPart.getName() + ")");
            System.out.println("   Price: (" + currentPart.getPrice() + ")");
            System.out.println("   Stock: (" + currentPart.getStock() + ")");
            System.out.println("   Min: (" + currentPart.getMin() + ")");
            System.out.println("   Max: (" + currentPart.getMax() + ")");
        }
    }

    /**
     * Finds location of part.
     * @param partId numeric ID of part.
     * @return index location of part in list, or -1 if not found.
     */
    static public int getPartIndex(int partId)
    {
        for (int i = 0; i < allParts.size(); i++)
        {
            if(partId == allParts.get(i).getId())
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds location of product.
     * @param productId numeric ID of product.
     * @return index location of product in list, or -1 if not found.
     */
    static public int getProductIndex(int productId)
    {
        for (int i = 0; i < allProducts.size(); i++)
        {
            if(productId == allProducts.get(i).getId())
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Generates unique ID for part or product, based on list length. Different lists are used
     * based on input. Checks to verify ID is not already in use.
     * @param objectType requires the string "Part" or "Product" other inputs won't generate ID.
     * @return Generated ID, or -1 if ID creation failed.
     */
    static public int generateId(String objectType)
    {
        int id;  // ID will never = 0
        boolean idFound = false;

        // Different seed value is used based on object type
        if (Objects.equals(objectType, "Part"))
        {
            id = allParts.size() + 1;
        }
        else if (Objects.equals(objectType, "Product"))
        {
            id = allProducts.size() + 1;
        }
        else  // Programmer gave incorrect input
        {
            System.out.println("Debug Info (generateId): Failed to generate ID, verify function input was \"Part\" OR \"Product\"");
            id = -1;  // Give ID that will notify programmer of error

        }

        while (!idFound && id < Integer.MAX_VALUE)  // Continue searching for an ID until the integer size becomes too large
        {
            if (lookupPart(id) != null) // A part with this ID already exists
            {
                id = id + 1;
            }
            else
            {
                idFound = true;
            }
        }
        return id;
    }
}
