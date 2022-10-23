package com.example.inventoryapplication;

/**
 * a part that was produced by outsourcing
 */
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return the name of the company that produced the part
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the name of the company that produced the part to be set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
