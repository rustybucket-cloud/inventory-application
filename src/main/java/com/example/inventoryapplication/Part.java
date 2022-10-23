package com.example.inventoryapplication;

/**
 * @author jacobpatton
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param new_id the id of the part
     */
    public void setId(int new_id) {
        id = new_id;
    }

    /**
     * @return id the id of the part
     */
    public int getId() {
        return id;
    }

    /**
     * @param new_name the name of the part to set
     */
    public void setName(String new_name) {
        name = new_name;
    }

    /**
     * @return the name of the part
     */
    public String getName() {
        return name;
    }

    /**
     * @param new_price the price of the part to set
     */
    public void setPrice(double new_price) {
        price = new_price;
    }

    /**
     * @return the price of the part
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the current inventory of the part
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the current inventory of the part
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the minimum inventory of the part
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the minimum inventory of the part
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the maximum inventory of the part
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the maximum inventory of the part
     */
    public void setMax(int max) {
        this.max = max;
    }
}
