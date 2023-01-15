package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  A class used to generate and interact with products.
 */
public class Product
{
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructs product.
     * @param id Product's ID.
     * @param name Product's name.
     * @param price Product's cost.
     * @param stock Amount of this product in inventory.
     * @param min Minimum amount allowed.
     * @param max Maximum amount allowed.
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id integer ID to be given.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the product ID.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param name product's name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param price product's price.
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * @return the price.
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param stock product's stock quantity.
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     * @return the stock quantity.
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * @param min the minimum amount of products allowed.
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * @return minimum amount of products allowed.
     */
    public int getMin()
    {
        return min;
    }

    /**
     * @param max maximum amount of products allowed.
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * @return the maximum amount of products allowed.
     */
    public int getMax()
    {
        return max;
    }

    /**
     * @param part to be associated with the product.
     */
    public void addAssociatedPart(Part part)
    {
        associatedParts.add(part);
    }


    /**
     * Deletes part from associated parts list by comparing IDs.
     * @param selectedAssociatedPart Part to be deleted.
     * @return returns true if part is deleted, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        for(Part currentPart : associatedParts)
        {
            if(currentPart.getId() == selectedAssociatedPart.getId())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @return all parts associated with product.
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }
}
