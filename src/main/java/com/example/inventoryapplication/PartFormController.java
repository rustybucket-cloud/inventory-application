package com.example.inventoryapplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * controller for the part form
 */
public class PartFormController extends FormController {
    public Text formTitle;
    public TextField id;
    public TextField name;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public TextField inv;
    public TextField priceCost;
    public TextField max;
    public TextField min;
    public Label changAbleLabel;
    public TextField chanAbleInput;
    String type;

    public void initialize() {
        type = "IN_HOUSE";
        id.setEditable(false);
        id.setDisable(true);
        formTitle.setStyle("-fx-weight: bold;");
        if (InventoryApplication.getFormMode() == "CREATE") {
            formTitle.setText("Add Part");
            id.setText("Auto Gen- Disabled");
            inHouse.setSelected(true);
        } else if (InventoryApplication.getFormMode() == "MODIFY") {
            formTitle.setText("Modify Part");
            id.setText(Integer.toString(InventoryApplication.getSelectedPart().getId()));

            Part selectedPart;
            Outsourced outsourcedPart = null;
            InHouse inHousePart = null;

            selectedPart = InventoryApplication.getSelectedPart();
            if (selectedPart == null) return;
            if (selectedPart instanceof Outsourced) {
                outsourcedPart = (Outsourced) selectedPart;
                outsourced.setSelected(true);
                setOutsourced();
            }
            else if (selectedPart instanceof InHouse) {
                inHousePart = (InHouse) selectedPart;
                inHouse.setSelected(true);
                setInHouse();
            }

            id.setText(Integer.toString(selectedPart.getId()));
            name.setText(selectedPart.getName());
            priceCost.setText(Double.toString(selectedPart.getPrice()));
            inv.setText(Integer.toString(selectedPart.getStock()));
            min.setText(Integer.toString(selectedPart.getMin()));
            max.setText(Integer.toString(selectedPart.getMax()));
            if (inHousePart != null) chanAbleInput.setText(Integer.toString(inHousePart.getMachineId()));
            else if (outsourcedPart != null) chanAbleInput.setText(outsourcedPart.getCompanyName());
        }

        setupErrorMessage();
    }

    /**
     * sets the product as in house
     */
    @FXML
    public void setInHouse() {
        type = "IN_HOUSE";
        changAbleLabel.setText("Machine ID");
    }

    /**
     * sets the product as outsourced
     */
    @FXML
    public void setOutsourced() {
        type = "OUTSOURCED";
        changAbleLabel.setText("Company Name");
    }

    /**
     * creates a new part and adds it to the inventory
     */
    private void createPart() {
        String newName;
        Double newPrice;
        int newStock;
        int newMin;
        int newMax;
        int newID;

        try {
            newName = name.getText();
            newPrice = Double.parseDouble(priceCost.getText());
            newStock = Integer.parseInt(inv.getText());
            newMin = Integer.parseInt(min.getText());
            newMax = Integer.parseInt(max.getText());
            newID = InventoryApplication.inventory.getAllParts().size();
        } catch(Exception e) {
            setErrorMessageText("Invalid input values");
            return;
        }

        if (!validateInputs(newMin, newMax, newStock, newPrice)) return;

        if (type.equals("IN_HOUSE")) {
            int machineId = Integer.parseInt(chanAbleInput.getText());
            InHouse newPart = new InHouse(machineId, newID, newName, newPrice, newStock, newMin, newMax);
            InventoryApplication.inventory.addPart(newPart);
        }
        else {
            String companyName = chanAbleInput.getText();
            Outsourced newPart = new Outsourced(newID, newName, newPrice, newStock, newMin, newMax, companyName);
            InventoryApplication.inventory.addPart(newPart);
        }

        closeForm();
    }

    /**
     * modifies an existing part
     *
     * RUNTIME ERROR There was a runtime error that occurred when adding the machine id or company name to the modified
     * part. This was fixed by creating new inHouse and outsourced variables that can contain the correct property
     */
    private void modifyPart() {
        String newName;
        String newPrice;
        int newStock;
        int newMin;
        int newMax;
        Part selectedPart;
        InHouse inHouse;
        Outsourced outsourced;

        try {
            selectedPart = InventoryApplication.getSelectedPart();
            newName = name.getText();
            selectedPart.setName(newName);
            newPrice = priceCost.getText();
            selectedPart.setPrice(Double.parseDouble(newPrice));
            newStock = Integer.parseInt(inv.getText());
            selectedPart.setStock(newStock);
            newMin = Integer.parseInt(min.getText());
            selectedPart.setMin(newMin);
            newMax = Integer.parseInt(max.getText());
            selectedPart.setMax(newMax);
            if (type.equals("IN_HOUSE")) {
                int newMachineId = Integer.parseInt(chanAbleInput.getText());
                if (selectedPart instanceof InHouse) {
                    inHouse = (InHouse) selectedPart;
                    inHouse.setMachineId(newMachineId);
                } else {
                    inHouse = new InHouse(
                            newMachineId,
                            selectedPart.getId(),
                            selectedPart.getName(),
                            selectedPart.getPrice(),
                            selectedPart.getStock(),
                            selectedPart.getMin(),
                            selectedPart.getMax()
                    );
                }
                selectedPart = inHouse;
            }
            else if (type.equals("OUTSOURCED")) {
                String newCompanyName = chanAbleInput.getText();
                if (selectedPart instanceof Outsourced) {
                    outsourced = (Outsourced) selectedPart;
                    outsourced.setCompanyName(newCompanyName);
                }
                else {
                    outsourced = new Outsourced(
                            selectedPart.getId(),
                            selectedPart.getName(),
                            selectedPart.getPrice(),
                            selectedPart.getStock(),
                            selectedPart.getMin(),
                            selectedPart.getMax(),
                            newCompanyName
                    );
                }
                selectedPart = outsourced;
            }
        } catch (Exception e) {
            System.out.println(e);
            setErrorMessageText("Invalid input values");
            return;
        }

        if (!validateInputs(newMin, newMax, newStock, Double.parseDouble(newPrice))) return;

        for (int i = 0; i < InventoryApplication.inventory.getAllParts().size(); i++) {
            if (selectedPart.getId() == InventoryApplication.inventory.getAllParts().get(i).getId()) {
                InventoryApplication.inventory.updatePart(i, selectedPart);
                break;
            }
        }

        closeForm();
    }

    /**
     * chooses the correct method to handle save
     */
    @FXML
    public void handleClick() {
        String formMode = InventoryApplication.getFormMode();
        if (formMode == "CREATE") createPart();
        else if (formMode == "MODIFY") modifyPart();
    }
}
