package com.controller;

import com.helper.AlertHelper;
import com.helper.InputHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    private TextField defermentLengthField;

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
    private LineChart<String, Number> loanChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

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
        if (handleCalculateErrors(event)) {

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
            // loanTable.setItems(Loan.getPayments());
            updateTable();
            updateGraph();
        }
    }

    @FXML
    protected void handleDefermentButtonAction(ActionEvent event) {
        if (handleDefermentErrors(event)) {
            Loan.setDefermentFrom(Integer.parseInt(defermentFromField.getText()));
            Loan.setDefermentLength(Integer.parseInt(defermentLengthField.getText()));
            Loan.CalculateDefermentPeriod();
            updateTable();
            // loanTable.setItems(Loan.getPayments());
            updateGraph();
        }
    }

    @FXML
    protected void handleFilterButton(ActionEvent event) {
        if (handleFilterErrors(event)) {
            Loan.setFilterFrom(Integer.parseInt(filterFromField.getText()) - 1);
            Loan.setFilterTo(Integer.parseInt(filterToField.getText()));
            updateTable();
            updateGraph();
        }
    }

    @FXML
    protected void updateTable() {
        ObservableList<MonthlyPayment> tempTable = FXCollections.observableArrayList();
        ObservableList<MonthlyPayment> payments = Loan.getPayments();
        for (int i = Loan.getFilterFrom(); i < Loan.getFilterTo(); i++) {
            tempTable.add(payments.get(i));
        }
        loanTable.setItems(tempTable);
    }

    @FXML
    private boolean handleFilterErrors(ActionEvent event) {
        System.out.println("hello");
        InputHelper checker = new InputHelper();
        Window owner = filterButton.getScene().getWindow();
        if (filterFromField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the filter starting month.");
            return false;
        }
        if (filterToField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the filter to month.");
            return false;
        }
        if (!checker.isPositiveInteger(filterFromField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter starting month must be a positive integer");
            return false;
        }
        if (!checker.isPositiveInteger(filterToField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter ending month must be a positive integer");
            return false;
        }
        int filterFrom = Integer.parseInt(filterFromField.getText());
        int filterTo = Integer.parseInt(filterToField.getText());
        if (filterFrom > Loan.getLength()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The starting month can't be more than or equal to loan's length");
            return false;
        }
        if (filterTo > Loan.getLength()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter to month can't be bigger than the loan length");
            return false;
        }
        if (filterFrom > filterTo) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter from month can't be bigger than filter to month");
            return false;
        }
        return true;
    }

    @FXML
    private boolean handleDefermentErrors(ActionEvent event) {
        InputHelper checker = new InputHelper();
        Window owner = defermentButton.getScene().getWindow();
        if (defermentFromField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the deferment starting month.");
            return false;
        }
        if (defermentLengthField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the deferment length.");
            return false;
        }
        if (!checker.isPositiveInteger(defermentFromField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The deferment starting month must be a positive integer");
            return false;
        }
        if (!checker.isPositiveInteger(defermentLengthField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The deferment ending month must be a positive integer");
            return false;
        }
        int defFrom = Integer.parseInt(defermentFromField.getText());
        int defLength = Integer.parseInt(defermentLengthField.getText());
        if (defFrom >= Loan.getLength()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The starting month can't be more than or equal to loan's length");
            return false;
        }
        if (defLength > Loan.getLength()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The deferment length can't be longer than the loan length");
            return false;
        }
        return true;
    }

    @FXML
    protected void handleRadioButtonAction(ActionEvent event) {

    }

    @FXML
    private boolean handleCalculateErrors(ActionEvent event) {
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
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The loan amount must be a number (float/integer)!");
            return false;
        }
        if (!checker.isFloat(interestField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The interest rate must be a number (float/integer)");
            return false;
        }
        if (!checker.isPositiveInteger(lengthField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The length must be a number (integer)!");
            return false;
        }
        return true;
    }

    @FXML
    private void updateGraph() {
        loanChart.getData().clear();
        XYChart.Series<String, Number> seriesInstallment = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> seriesBalance = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> seriesInterest = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> seriesRepayment = new XYChart.Series<String, Number>();
        ObservableList<MonthlyPayment> tempMonths = Loan.getPayments();
        for (int i = Loan.getFilterFrom(); i < Loan.getFilterTo(); i++) {
            MonthlyPayment currentMonth = tempMonths.get(i);
            seriesInstallment.getData().add(new XYChart.Data<String, Number>(String.valueOf(currentMonth.getMonth()),
                    currentMonth.getInstallment()));
            seriesBalance.getData().add(new XYChart.Data<String, Number>(String.valueOf(currentMonth.getMonth()),
                    currentMonth.getBalance()));
            seriesInterest.getData().add(new XYChart.Data<String, Number>(String.valueOf(currentMonth.getMonth()),
                    currentMonth.getInterest()));
            seriesRepayment.getData().add(new XYChart.Data<String, Number>(String.valueOf(currentMonth.getMonth()),
                    currentMonth.getRepayment()));
        }
        seriesInstallment.setName("Installment");
        seriesBalance.setName("Balance");
        seriesInterest.setName("Interest");
        seriesRepayment.setName("Repayment");
        loanChart.getData().add(seriesInstallment);
        loanChart.getData().add(seriesBalance);
        loanChart.getData().add(seriesInterest);
        loanChart.getData().add(seriesRepayment);
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
