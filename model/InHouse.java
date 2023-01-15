package model;

/**
 * A class that extends the Part class, allowing the creation of InHouse parts which have a
 * Machine ID associated.
 */
public class InHouse extends Part
{
    private int machineID;

    /**
     * Constructs InHouse part. A part not outsourced to a different company.
     * @param id The ID of part.
     * @param name The name given to part.
     * @param price The cost of product.
     * @param stock Total inventory amount.
     * @param min Minimum allowed amount.
     * @param max Minimum allowed amount.
     * @param machineID ID of machine that created part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID)
    {
        super(id, name, price, stock, min, max); // Transfers constructor data to the parent constructor (Part)
        this.machineID = machineID;
    }

    /**
     * Sets machine ID
     * @param machineID the ID of the machine that created the part.
     */
    public void setMachineID(int machineID)
    {
        this.machineID = machineID;
    }

    /**
     * Returns machine ID
     * @return machine ID of product.
     */
    public int getMachineID()
    {
        return machineID;
    }
}
