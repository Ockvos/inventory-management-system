package model;

/**
 * A class that extends the Part class, allowing the creation of Outsourced parts which have a
 * company name of the company that created the part associated.
 */
public class Outsourced extends Part
{
    private String companyName;

    /**
     * Constructs InHouse part. A part not outsourced to a different company.
     * @param id The ID of part.
     * @param name The name given to part.
     * @param price The cost of product.
     * @param stock Total inventory amount.
     * @param min Minimum allowed amount.
     * @param max Minimum allowed amount.
     * @param companyName Name of company that created part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Set name of company.
     * @param companyName the name of the company that created the part.
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * Get name of company
     * @return the name of company that created part.
     */
    public String getCompanyName()
    {
        return companyName;
    }
}
