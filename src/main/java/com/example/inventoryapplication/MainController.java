package com.example.inventoryapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MainController {
    public Text mainHeading;
    public Text errorMessage;
    public VBox partContainer;
    public VBox productContainer;
    public TextField partSearch;
    public TextField productSearch;
    public TableView partsTable;
    public TableView productsTable;
    public TableColumn<Part,String> partIdCol;
    public TableColumn<Part,String> partNameCol;
    public TableColumn<Part,String> partInvCol;
    public TableColumn<Part,String> partCostCol;
    public TableView.TableViewSelectionModel<Part> partTableSelectionModel;
    public TableColumn<Product,String> productIdCol;
    public TableColumn<Product,String> productNameCol;
    public TableColumn<Product,String> productInvCol;
    public TableColumn<Product,String> productCostCol;
    public TableView.TableViewSelectionModel<Product> productTableSelectionModel;


    public void initialize() {
        partTableSelectionModel = partsTable.getSelectionModel();
        productTableSelectionModel = productsTable.getSelectionModel();

        mainHeading.setFont(new Font(20));
        errorMessage.setFill(Paint.valueOf("RED"));

        String tableCss = "-fx-border-color: black;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-radius: 5;\n" +
                "-fx-width: 500;\n" +
                "-fx-padding: 10 10 10 10;\n" +
                "-fx-margin: 20 20 20 20;\n";
        partSearch.setPrefWidth(200);
        productSearch.setPrefWidth(200);
        partIdCol.setPrefWidth(80);
        partNameCol.setPrefWidth(80);
        partInvCol.setPrefWidth(90);
        partCostCol.setPrefWidth(125);

        productIdCol.setPrefWidth(80);
        productNameCol.setPrefWidth(90);
        productInvCol.setPrefWidth(90);
        productCostCol.setPrefWidth(115);

        partContainer.setStyle(tableCss);
        productContainer.setStyle(tableCss);

        partIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory("price"));
        partsTable.setItems(InventoryApplication.inventory.getAllParts());

        productIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        productCostCol.setCellValueFactory(new PropertyValueFactory("price"));
        productsTable.setItems(InventoryApplication.inventory.getAllProducts());
    }

    /**
     * opens the form to create a part
     */
    @FXML
    public void openAddPartForm() throws IOException {
        resetErrorMessage();
        partsTable.setItems(InventoryApplication.inventory.getAllParts());
        InventoryApplication.openPartForm("CREATE");
    }

    /**
     * opens the form to modify a part
     */
    @FXML
    public void openModifyPartForm() throws IOException {
        partsTable.setItems(InventoryApplication.inventory.getAllParts());
        Part selectedPart = partTableSelectionModel.getSelectedItem();
        if (selectedPart == null) {
            setErrorMessageText("No part selected.");
            return;
        }
        resetErrorMessage();
        InventoryApplication.setSelectedPart(selectedPart);
        InventoryApplication.openPartForm("MODIFY");
    }

    /**
     * opens the form to create a product
     */
    @FXML
    public void openAddProductForm() throws IOException {
        resetErrorMessage();
        productsTable.setItems(InventoryApplication.inventory.getAllProducts());
        InventoryApplication.openProductForm("CREATE");
    }

    /**
     * opens the form to modify a product
     */
    @FXML
    public void openModifyProductForm() throws IOException {
        productsTable.setItems(InventoryApplication.inventory.getAllProducts());
        Product selectedProduct = productTableSelectionModel.getSelectedItem();
        if (selectedProduct == null) {
            setErrorMessageText("No product selected.");
            return;
        }
        resetErrorMessage();
        InventoryApplication.setSelectedProduct(selectedProduct);
        InventoryApplication.openProductForm("MODIFY");
    }

    /**
     * deletes the selected part
     */
    @FXML
    public void deletePart() {
        Part selectedPart = partTableSelectionModel.getSelectedItem();
        if (selectedPart == null) return;

        Stage newStage = new Stage();
        VBox container = new VBox();

        container.setPadding(new Insets(10, 10, 10, 10));
        container.setSpacing(20);
        Text confirmText = new Text("Are you sure you want to delete this part?");
        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Delete");

        cancelButton.setOnAction(e -> newStage.close());
        deleteButton.setOnAction(e -> {
            InventoryApplication.inventory.deletePart(selectedPart);
            newStage.close();
        });
        HBox actions = new HBox();
        actions.setSpacing(20);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(cancelButton, deleteButton);
        container.getChildren().addAll(confirmText, actions);

        Scene confirm = new Scene(container, 250, 250);
        newStage.setScene(confirm);
        newStage.show();
    }

    /**
     * deletes the selected product
     */
    @FXML
    public void deleteProduct() {
        Product selectedProduct = productTableSelectionModel.getSelectedItem();
        if (selectedProduct == null) return;
        if (InventoryApplication.inventory.getAllProducts().size() > 0) {
            errorMessage.setText("Unable to delete products with associated parts.");
            return;
        }

        Stage newStage = new Stage();
        VBox container = new VBox();

        container.setPadding(new Insets(10, 10, 10, 10));
        container.setSpacing(20);
        Text confirmText = new Text("Are you sure you want to delete this product?");
        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Delete");

        cancelButton.setOnAction(e -> newStage.close());
        deleteButton.setOnAction(e -> {
            InventoryApplication.inventory.deleteProduct(selectedProduct);
            newStage.close();
        });
        HBox actions = new HBox();
        actions.setSpacing(20);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(cancelButton, deleteButton);
        container.getChildren().addAll(confirmText, actions);

        Scene confirm = new Scene(container, 300, 250);
        newStage.setScene(confirm);
        newStage.show();
    }

    /**
     * does a search for a part and adjust table output
     */
    @FXML
    public void doPartSearch() {
        ObservableList<Part> result = FXCollections.observableArrayList();
        Part searchResult = null;
        String input = partSearch.getText();
        if (input == null || input.equals("")) {
            partsTable.setItems(InventoryApplication.inventory.getAllParts());
            return;
        }
        Part nameSearch = InventoryApplication.inventory.lookupPart(input);
        if (nameSearch == null) {
            try {
                Part idSearch = InventoryApplication.inventory.lookupPart(Integer.parseInt(input));
                searchResult = idSearch;
            } catch (Exception e) {}
        } else {
            searchResult = nameSearch;
        }
        if (searchResult != null) {
            result.add(searchResult);
        }

        for (Part part: InventoryApplication.inventory.getAllParts()) {
            if (part.getName().contains(input) && (searchResult == null || part.getId() != searchResult.getId())) {
                result.add(part);
            }
        }

        if (result.size() == 0) setErrorMessageText("Part not found.");
        else resetErrorMessage();

        partsTable.setItems(result);
    }

    /**
     * does a search for a product and adjust table output
     */
    @FXML
    public void doProductSearch() {
        ObservableList<Product> result = FXCollections.observableArrayList();
        Product searchResult = null;
        String input = productSearch.getText();
        if (input == null || input.equals("")) {
            productsTable.setItems(InventoryApplication.inventory.getAllProducts());
            return;
        }
        Product nameSearch = InventoryApplication.inventory.lookupProduct(input);
        if (nameSearch == null) {
            try {
                Product idSearch = InventoryApplication.inventory.lookupProduct(Integer.parseInt(input));
                searchResult = idSearch;
            } catch (Exception e) {}
        } else {
            searchResult = nameSearch;
        }
        if (searchResult != null) {
            result.add(searchResult);
        }

        for (Product product: InventoryApplication.inventory.getAllProducts()) {
            if (product.getName().contains(input) && (searchResult == null || product.getId() != searchResult.getId())) {
                result.add(product);
            }
        }

        if (result.size() == 0) setErrorMessageText("Product not found.");
        else resetErrorMessage();

        productsTable.setItems(result);
    }

    /**
     * closes the application
     */
    @FXML
    public void closeApplication() {
        InventoryApplication.closeApplication();
    }

    /**
     * displays an error message
     * @param error the error message
     */
    private void setErrorMessageText(String error) {
        errorMessage.setText(error);
        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            if (errorMessage.getText().equals(error)) errorMessage.setText("");
        });
    }

    private void resetErrorMessage() {
        errorMessage.setText("");
    }
}
