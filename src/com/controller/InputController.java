package com.controller;

import com.helper.InputHelper;
import com.main.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import com.dataStructure.AdvancedLoan;
import com.dataStructure.MonthlyPayment;

public class InputController implements Initializable {
    private AdvancedLoan Loan;
    private App mainApp;

    @FXML
    private TextField amountField, interestField, lengthField, defermentFromField, defermentLengthField,
            filterFromField, filterToField;

    @FXML
    private Label totalLabel, repaymentLabel, lengthLabel;

    @FXML
    private RadioButton annualRadio, monthlyRadio, annuityRadio, linearRadio;

    @FXML
    private Button calculateButton, defermentButton, filterButton;

    @FXML
    Alert alert;

    @FXML
    private LineChart<String, Number> loanChart;

    @FXML
    private TableView<MonthlyPayment> loanTable;

    @FXML
    private TableColumn<MonthlyPayment, Integer> monthColumn;

    @FXML
    private TableColumn<MonthlyPayment, Float> balanceColumn, installmentColumn, interestColumn, repaymentColumn;

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
    protected void handleAbout() {
        Window owner = calculateButton.getScene().getWindow();
        showAlert(Alert.AlertType.INFORMATION, owner, "About!", "This app was created by R.Č");
    }

    @FXML
    private void handleSave() throws IOException {
        File paymentFile = getPaymentFilePath();
        if (paymentFile != null) {
            savePaymentDataToFile(paymentFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     * 
     * @throws IOException
     */
    @FXML
    private void handleSaveAs() throws IOException {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(this.mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            savePaymentDataToFile(file);
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
        totalLabel.setText("Total amount: " + String.valueOf(Loan.getBalance()));
        repaymentLabel.setText("Total repayment amount: " + String.valueOf(Loan.getTotalRepayment()));
        lengthLabel.setText("Total Length: " + String.valueOf(Loan.getLength()) + " Months");
    }

    @FXML
    private boolean handleFilterErrors(ActionEvent event) {
        InputHelper checker = new InputHelper();
        Window owner = filterButton.getScene().getWindow();
        if (filterFromField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the filter starting month.");
            return false;
        }
        if (filterToField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the filter to month.");
            return false;
        }
        if (!checker.isPositiveInteger(filterFromField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter starting month must be a positive integer");
            return false;
        }
        if (!checker.isPositiveInteger(filterToField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter ending month must be a positive integer");
            return false;
        }
        int filterFrom = Integer.parseInt(filterFromField.getText());
        int filterTo = Integer.parseInt(filterToField.getText());
        if (filterFrom > Loan.getLength()) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The starting month can't be more than or equal to loan's length");
            return false;
        }
        if (filterTo > Loan.getLength()) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The filter to month can't be bigger than the loan length");
            return false;
        }
        if (filterFrom > filterTo) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
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
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the deferment starting month.");
            return false;
        }
        if (defermentLengthField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the deferment length.");
            return false;
        }
        if (!checker.isPositiveInteger(defermentFromField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The deferment starting month must be a positive integer");
            return false;
        }
        if (!checker.isPositiveInteger(defermentLengthField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The deferment ending month must be a positive integer");
            return false;
        }
        int defFrom = Integer.parseInt(defermentFromField.getText());
        int defLength = Integer.parseInt(defermentLengthField.getText());
        if (defFrom >= Loan.getLength()) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The starting month can't be more than or equal to loan's length");
            return false;
        }
        if (defLength > Loan.getLength()) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
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
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the loan amount");
            return false;
        }
        if (interestField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enther the interest rate");
            return false;
        }
        if (lengthField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter the length");
            return false;
        }
        if (!checker.isFloat(amountField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The loan amount must be a number (float/integer)!");
            return false;
        }
        if (!checker.isFloat(interestField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!",
                    "The interest rate must be a number (float/integer)");
            return false;
        }
        if (!checker.isPositiveInteger(lengthField.getText())) {
            showAlert(Alert.AlertType.ERROR, owner, "Input Error!", "The length must be a number (integer)!");
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

    @FXML
    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {

        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        if (alertType == Alert.AlertType.ERROR) {
            alert.setGraphic(new ImageView(this.getClass().getResource("../gui/images/remove64.png").toString()));
        } else if (alertType == Alert.AlertType.INFORMATION) {
            alert.setGraphic(new ImageView(this.getClass().getResource("../gui/images/about64.png").toString()));
        }

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("../gui/styling/DarkDialog.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.show();

    }

    public File getPaymentFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in the
     * OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setPaymentFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            // this.mainApp.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            // this.mainApp.setTitle("AddressApp");
        }
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    public void loadPaymentDataFromFile(File file) {
        try {

            // Reading XML from the file and unmarshalling.

            Loan.getPayments().clear();

            // Save the file path to the registry.
            setPaymentFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     * 
     * @param file
     * @throws IOException
     */
    public void savePaymentDataToFile(File file) throws IOException {
        FileWriter fw = new FileWriter("test.txt");
        PrintWriter pw = new PrintWriter(fw);

        pw.println("Month Balance     Installment    Interest   Repayment");
        List<MonthlyPayment> tempList = Loan.getPayments();
        for (int i = Loan.getFilterFrom(); i < Loan.getFilterTo(); i++) {
            MonthlyPayment cMonth = tempList.get(i);
            pw.println(String.valueOf(cMonth.getMonth()) + "     " + String.valueOf(cMonth.getBalance() + "     ")
                    + String.valueOf(cMonth.getInstallment()) + "        "
                    + String.valueOf(cMonth.getInterest() + "    ") + String.valueOf(cMonth.getRepayment()));
        }

        pw.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Integer>("Month"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Balance"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Installment"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Interest"));
        repaymentColumn.setCellValueFactory(new PropertyValueFactory<MonthlyPayment, Float>("Repayment"));
        File file = getPaymentFilePath();
        if (file != null) {
            loadPaymentDataFromFile(file);
        }
    }
}
