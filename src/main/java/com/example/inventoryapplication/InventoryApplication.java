package com.example.inventoryapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Application that manages company inventory
 * Javadocs located in InventoryApplication/Javadocs
 * FUTURE ENHANCEMENT the application could be extended to provide information about sales and production costs
 * of parts and products
 */
public class InventoryApplication extends Application {
    private static Stage app;
    private static Scene main;
    private static String formMode;
    public static Inventory inventory;
    private static FXMLLoader loader = new FXMLLoader();
    private static Part selectedPart;
    private static Product selectedProduct;
    private static Stage openForm;

    /**
     * @return if the current open form is a creating or modifying
     */
    public static String getFormMode() {
        return formMode;
    }

    /**
     * @param formMode if the open form is creating or modifying
     * @throws IOException
     */
    public static void setFormMode(String formMode) throws IOException {
        if (formMode != "CREATE" && formMode != "MODIFY") throw new IOException("Invalid form mode selected");
        InventoryApplication.formMode = formMode;
    }

    /**
     * @return the part that is being modified
     */
    public static Part getSelectedPart() {
        return selectedPart;
    }

    /**
     * @param selectedPart the part that is being modified
     */
    public static void setSelectedPart(Part selectedPart) {
        InventoryApplication.selectedPart = selectedPart;
    }

    /**
     * @return the product that is being modified
     */
    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the product that is being modified
     */
    public static void setSelectedProduct(Product selectedProduct) {
        InventoryApplication.selectedProduct = selectedProduct;
    }

    /**
     * closes the form that is open
     */
    public static void closeOpenForm() {
        openForm.close();
        openForm = null;
    }

    /**
     * @param openForm the form that is being opened
     */
    public static void setOpenForm(Stage openForm) {
        InventoryApplication.openForm = openForm;
    }

    /**
     * @param formMode if the form is creating or modifying
     * @throws IOException
     */
    public static void openPartForm(String formMode) throws IOException {
        setFormMode(formMode);
        Scene partFormScene = new Scene(loader.load(InventoryApplication.class.getResource("part-form.fxml")), 600, 500);
        openForm(partFormScene);
    }

    /**
     * @param formMode if the form is creating or modifying
     * @throws IOException
     */
    public static void openProductForm(String formMode) throws IOException {
        setFormMode(formMode);
        Scene productFormScene = new Scene(loader.load(InventoryApplication.class.getResource("product-form.fxml")), 800, 650);
        openForm(productFormScene);
    }

    private static void openForm(Scene form) {
        if (openForm != null) return;
        Stage newStage = new Stage();
        setOpenForm(newStage);
        newStage.setScene(form);
        newStage.show();
        newStage.setOnCloseRequest(e -> closeOpenForm());
    }

    /**
     * closes the application
     */
    public static void closeApplication() {
        app.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        app = stage;

        inventory = new Inventory();

        main = new Scene(loader.load(InventoryApplication.class.getResource("main-view.fxml")), 1000, 400);

        stage.setScene(main);
        stage.show();
    }

    public static void main(String[] args) {
        inventory = new Inventory();
        launch();
    }
}