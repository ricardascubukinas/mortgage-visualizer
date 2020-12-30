package com.controller;

import com.helper.AlertHelper;
import com.helper.InputChecker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class InputController {
    @FXML
    private TextField amountField;

    @FXML
    private TextField interestField;

    @FXML
    private TextField lengthField;

    @FXML
    private TextField defermentFromField;

    @FXML
    private TextField defermentToField;

    @FXML
    private TextField filterFromField;

    @FXML
    private TextField filterToField;

    @FXML
    private Label totalLabel;

    @FXML
    private Label repaymentLabel;

    @FXML
    private RadioButton annuityRadio;

    @FXML
    private RadioButton linearRadio;

    @FXML
    private Button calculateButton;

    @FXML
    private Button defermentButton;

    @FXML
    private Button filterButton;

    @FXML
    protected void handleCalculateButtonAction(ActionEvent event) {
        if (handleErrors(event)) {
        }
    }

    @FXML
    protected void handleRadioButtonAction(ActionEvent event) {

    }

    @FXML
    private boolean handleErrors(ActionEvent event) {
        InputChecker checker = new InputChecker();
        Window owner = calculateButton.getScene().getWindow();
        if (amountField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the loan amount");
            return false;
        }
        if (interestField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enther the interest rate");
            return false;
        }
        if (lengthField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the length");
            return false;
        }
        if (!checker.isFloat(amountField.getText())) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Input Error!",
                    "The loan amount must be a number (float/integer)!");
            return false;
        }
        if (!checker.isFloat(interestField.getText())) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Input Error!",
                    "The interest rate must be a number (float/integer)");
            return false;
        }
        if (!checker.isInteger(lengthField.getText())) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Input Error!", "The length must be a number (integer)!");
            return false;
        }
        AlertHelper.showAlert(AlertType.INFORMATION, owner, "Helpful message", "It worked!");
        return true;
    }
}
