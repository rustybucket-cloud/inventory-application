package com.example.inventoryapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores and provides access to the company inventory, includes parts and products.
 *
 * @author jacobpatton
 */
public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    public Inventory() {
        this.allParts = FXCollections.observableArrayList();
        this.allProducts = FXCollections.observableArrayList();
    }

    /**
     * @param newPart part to add to inventory
     */
    void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct product to add to inventory
     */
    void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId the id of the part being searched for
     * @return the part with a matching id
     */
    Part lookupPart(int partId) {
        for (Part part: allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * @param partName the name of the part being searched for
     * @return the part with a matching name
     */
    Part lookupPart(String partName) {
        for (Part part: allParts) {
            if (part.getName().trim().equalsIgnoreCase(partName.trim())) {
                return part;
            }
        }
        return null;
    }

    /**
     * @param productId the id of the product being searched for
     * @return the product with a matching id
     */
    Product lookupProduct(int productId) {
        for (Product product: allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * @param productName the name of the product being searched for
     * @return the product with a matching name
     */
    Product lookupProduct(String productName) {
        for (Product product: allProducts) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    /**
     * @param index the index of the part in the ObservableList
     * @param selectedPart  the part to replace the part at the index
     */
    void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index the index of the part in the ObservableList
     * @param selectedProduct  the product to replace the product at the index
     */
    void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * @param selectedPart the part to be deleted
     * @return if the part was successfully deleted
     */
    boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct the product to be deleted
     * @return if the product was successfully deleted
     */
    boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return all the parts in the inventory
     */
    ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return all the products in the inventory
     */
    ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
