package com.controller;

import com.helper.AlertHelper;
import com.helper.InputHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

import com.dataStructure.AdvancedLoan;
import com.dataStructure.MonthlyPayment;

public class InputController implements Initializable {
    private AdvancedLoan Loan;

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
    private RadioButton annualRadio;

    @FXML
    private RadioButton monthlyRadio;

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
    private TableView<MonthlyPayment> loanTable;

    @FXML
    private TableColumn<MonthlyPayment, Integer> monthColumn;

    @FXML
    private TableColumn<MonthlyPayment, Float> balanceColumn;

    @FXML
    private TableColumn<MonthlyPayment, Float> installmentColumn;

    @FXML
    private TableColumn<MonthlyPayment, Float> interestColumn;

    @FXML
    private TableColumn<MonthlyPayment, Float> repaymentColumn;

    @FXML
    protected void handleCalculateButtonAction(ActionEvent event) {
        if (handleErrors(event)) {

            defermentButton.setDisable(false);
            filterButton.setDisable(false);
            float balance = Float.parseFloat(amountField.getText());
            float interestRate = Float.parseFloat(interestField.getText());
            int length = Integer.parseInt(lengthField.getText());
            boolean loanType;
            if (annuityRadio.isSelected())
                loanType = false;
            else
                loanType = true;
            float interestType;
            if (annualRadio.isSelected()) {
                interestType = 1.0f / 12.0f;
            } else
                interestType = 1.0f;
            Loan = new AdvancedLoan(0, 0, 0, length, balance, interestRate, interestType, length, loanType);
            totalLabel.setText("Total amount: " + String.valueOf(Loan.getBalance()));
            repaymentLabel.setText("Total repayment amount: " + String.valueOf(Loan.getTotalRepayment()));
            totalLabel.setVisible(true);
            repaymentLabel.setVisible(true);
            loanTable.setItems(Loan.getPayments());

        }
    }

    @FXML
    protected void handleRadioButtonAction(ActionEvent event) {

    }

    @FXML
    private boolean handleErrors(ActionEvent event) {
        InputHelper checker = new InputHelper();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Integer>("Month"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Balance"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Installment"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Interest"));
        repaymentColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Repayment"));
    }
}
