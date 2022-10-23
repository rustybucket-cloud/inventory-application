package com.example.inventoryapplication;

import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class FormController {
    public Text errorMessage;
    private int errorMessageId;

    /**
     * closes the part form
     */
    @FXML
    public void closeForm() {
        InventoryApplication.closeOpenForm();
    }

    /**
     * validates the user input
     * @param min
     * @param max
     * @param inv
     * @param price
     * @return if the input is valid
     */
    protected boolean validateInputs(int min, int max, int inv, double price) {
        String cantBeNeg = " can't be negative.";
        String mustBePosString = " must be positive.";
        String errorMessage;

        if (max < 0) errorMessage = "Maximum" + cantBeNeg;
        else if (min < 0) errorMessage ="Minimum" + cantBeNeg;
        else if (inv < 0) errorMessage = "Inventory" + cantBeNeg;
        else if (price <= 0) errorMessage = "Price" + mustBePosString;
        else if (min > max) errorMessage = "Minimum must be greater than maximum.";
        else if (inv > max || inv < min) errorMessage = "Inventory value must be between the minimum and maximum.";
        else errorMessage = null;

        if (errorMessage != null) {
            setErrorMessageText(errorMessage);
        }
        return errorMessage == null;
    }

    /**
     * displays an error message to the user
     * @param error the error message to display
     */
    protected void setErrorMessageText(String error) {
        errorMessage.setText(error);
        errorMessageId++;
        int currentMessageId = errorMessageId;
        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            if (currentMessageId == errorMessageId) errorMessage.setText("");
        });
    }

    /**
     * initializes error message
     */
    protected void setupErrorMessage() {
        errorMessage.setFill(Paint.valueOf("RED"));
        errorMessageId = 0;
    }
}
