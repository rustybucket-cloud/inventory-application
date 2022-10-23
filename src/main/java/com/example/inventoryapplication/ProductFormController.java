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

public class ProductFormController extends FormController {
    private ObservableList<Part> associatedParts;
    private TableView.TableViewSelectionModel<Part> allPartsTableSelectionModel;
    private TableView.TableViewSelectionModel<Part> curPartsTableSelectionModel;
    public HBox productFormContent;
    public Text heading;
    public TextField productFormPartSearch;
    public VBox productFormLeft;
    public TextField productId;
    public TextField productName;
    public TextField productInv;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;
    public TableView allPartsTable;
    public TableColumn allPartsIdCol;
    public TableColumn allPartsNameCol;
    public TableColumn allPartsInvCol;
    public TableColumn allPartsPriceCol;
    public TableView curPartsTable;
    public TableColumn curPartsIdCol;
    public TableColumn curPartsNameCol;
    public TableColumn curPartsInvCol;
    public TableColumn curPartsPriceCol;

    public void initialize() {
        associatedParts = FXCollections.observableArrayList();
        allPartsTableSelectionModel = allPartsTable.getSelectionModel();
        curPartsTableSelectionModel = curPartsTable.getSelectionModel();

        setupErrorMessage();

        String productFormContentStyles = "-fx-border-color: black;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-radius: 10;\n" +
                "-fx-padding: 20 20 20 20;\n";
        productFormContent.setStyle(productFormContentStyles);
        productFormContent.setPrefWidth(760);

        heading.setFont(new Font(15));

        productId.setDisable(true);
        String formMode = InventoryApplication.getFormMode();
        if (formMode == "CREATE") productId.setText("Auto Gen- Disabled");
        else if (formMode == "MODIFY") {
            String idInString;
            Product selectedProduct = InventoryApplication.getSelectedProduct();
            try {
                idInString = Integer.toString(selectedProduct.getId());
            } catch (Exception e) {
                setErrorMessageText("Product has an invalid ID");
                return;
            }
            productId.setText(idInString);
            productName.setText(selectedProduct.getName());
            productInv.setText(Integer.toString(selectedProduct.getStock()));
            productPrice.setText(Double.toString(selectedProduct.getPrice()));
            productMax.setText(Integer.toString(selectedProduct.getMax()));
            productMin.setText(Integer.toString(selectedProduct.getMin()));
        }

        allPartsIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
        allPartsTable.setItems(InventoryApplication.inventory.getAllParts());

        if (InventoryApplication.getFormMode() == "MODIFY") {
            associatedParts.addAll(InventoryApplication.getSelectedProduct().getAllAssociatedParts());
            System.out.println(associatedParts.size());
        }
        curPartsIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        curPartsNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        curPartsInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        curPartsPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
        curPartsTable.setItems(associatedParts);
    }

    /**
     * adds an associated part based on user input
     */
    @FXML
    public void addPart() {
        Part selectedPart = allPartsTableSelectionModel.getSelectedItem();
        if (selectedPart == null) return;
        associatedParts.add(selectedPart);
    }

    /**
     * removes an associated part
     */
    @FXML
    public void removePart() {
        Part selectedPart = curPartsTableSelectionModel.getSelectedItem();
        if (selectedPart == null) return;

        Stage newStage = new Stage();
        VBox container = new VBox();

        container.setPadding(new Insets(10, 10, 10, 10));
        container.setSpacing(20);
        Text confirmText = new Text("Are you sure you want to remove associated part?");
        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Remove");

        cancelButton.setOnAction(e -> newStage.close());
        deleteButton.setOnAction(e -> {
            for (int i = 0; i < associatedParts.size(); i++) {
                if (associatedParts.get(i).getId() == selectedPart.getId()) {
                    associatedParts.remove(i);
                    break;
                }
            }
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
     * determines the save function to run
     * @throws IOException
     */
    @FXML
    public void handleSave() throws IOException {
        String mode = InventoryApplication.getFormMode();
        if (mode == "CREATE") addProduct();
        else if (mode == "MODIFY") modifyProduct();
        else throw new IOException("Invalid form mode.");
    }

    /**
     * creates a new product
     */
    public void addProduct() {
        int id;
        String name;
        int inv;
        double price;
        int max;
        int min;

        try {
            id = InventoryApplication.inventory.getAllProducts().size();
            name = productName.getText();
            inv = Integer.parseInt(productInv.getText());
            price = Double.parseDouble(productPrice.getText());
            max = Integer.parseInt(productMax.getText());
            min = Integer.parseInt(productMin.getText());
        } catch (Exception e) {
            setErrorMessageText("Invalid product details");
            return;
        }

        if (!validateInputs(min, max, inv, price)) return;

        Product newProduct = new Product(id, name, price, inv, min, max);
        for (Part part: associatedParts) newProduct.addAssociatedPart(part);
        InventoryApplication.inventory.addProduct(newProduct);
        closeForm();
    }

    /**
     * modifies an existing product
     */
    public void modifyProduct() {
        String name;
        int inv;
        double price;
        int max;
        int min;
        Product selectedProduct = InventoryApplication.getSelectedProduct();

        try {
            name = productName.getText();
            inv = Integer.parseInt(productInv.getText());
            price = Double.parseDouble(productPrice.getText());
            max = Integer.parseInt(productMax.getText());
            min = Integer.parseInt(productMin.getText());
        } catch (Exception e) {
            setErrorMessageText("Invalid product details");
            return;
        }

        if (!validateInputs(min, max, inv, price)) return;

        selectedProduct.setName(name);
        selectedProduct.setStock(inv);
        selectedProduct.setPrice(price);
        selectedProduct.setMax(max);
        selectedProduct.setMin(min);

        for (int i = selectedProduct.getAllAssociatedParts().size(); i > 0; i--) {
            selectedProduct.deleteAssociatedPart(selectedProduct.getAllAssociatedParts().get(i - 1));
        }
        for (Part part: associatedParts) {
            selectedProduct.addAssociatedPart(part);
        }

        int count = InventoryApplication.inventory.getAllProducts().size();
        for (int i = 0; i < count; i++) {
            if (InventoryApplication.inventory.getAllProducts().get(i).getId() == selectedProduct.getId()) {
                InventoryApplication.inventory.updateProduct(i, selectedProduct);
            }
        }
        closeForm();
    }

    /**
     * performs a search for a part and changes the table output
     */
    @FXML
    public void doPartSearch() {
        ObservableList<Part> result = FXCollections.observableArrayList();
        Part searchResult = null;
        String input = productFormPartSearch.getText();
        if (input == null || input.equals("")) {
            allPartsTable.setItems(InventoryApplication.inventory.getAllParts());
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

        if (result.size() == 0) setErrorMessageText("No matching parts found.");

        allPartsTable.setItems(result);
    }
}
