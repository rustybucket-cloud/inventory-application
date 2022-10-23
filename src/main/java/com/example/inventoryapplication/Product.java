package com.example.inventoryapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains data about products and their associated parts
 * @author jacobpatton
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    /**
     * @return the id of the product
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id of the product to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the product to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price of the product to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the inventory of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the inventory of the product to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the minimum inventory of the product
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the minimum inventory of the product to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the minimum inventory of the product
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the maximum inventory of the product to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param part a part to associate with the product
     */
    void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart a part to remove association with the product
     */
    boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return all parts associated with the product
     */
    ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
