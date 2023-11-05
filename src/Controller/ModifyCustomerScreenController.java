package Controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBDivisions;
import Models.Country;
import Models.Customer;
import Models.Division;
import Controller.LoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class controls the Modify Customer screen.
 * The Modify Customer screen enables the user to modify and update existing customer data in the database.
 */
public class ModifyCustomerScreenController implements Initializable{

    Stage stage;
    Parent scene;
    Customer selectedCustomer;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> dropDownCountry;

    @FXML
    private ComboBox<String> dropDownDivision;

    @FXML
    private Label labelDivision;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private TextField textFieldCustomerID;

    @FXML
    private TextField textFieldCustomerName;

    @FXML
    private TextField textFieldPhone;

    @FXML
    private TextField textFieldPostalCode;

    @FXML
    private Button updateButton;

    /**
     * This method populates dropDownDivision combobox with the appropriate list once the user selects a value in dropDownCountry combobox.
     * The label labelDivision is also updated to the appropriate term as related to their customs.
     * @param event Executes when user selects country from dropDownCountry combobox.
     * @throws NullPointerException In the event that a null value is unexpectedly returned.
     */
    @FXML
    void onActionCountrySelect(ActionEvent event) throws NullPointerException
    {
        dropDownDivision.setValue("");

        ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
        ObservableList<String> filteredDivisionNames = FXCollections.observableArrayList();

        String countryName = dropDownCountry.getValue();

        if (countryName.equals("U.S"))
        {
            labelDivision.setText("State");
        }

        else if (countryName.equals("Canada"))
        {
            labelDivision.setText("Province/Territory");
        }

        else if (countryName.equals("UK"))
        {
            labelDivision.setText("Country/Province");
        }

        for (Division d : allDivisions)
        {
            if (d.getCountryName(d.getCountryID()).equals(countryName))
            {
                filteredDivisionNames.add(d.getDivisionName());
            }
        }

        dropDownDivision.setItems(filteredDivisionNames);

    }

    /**
     * This method returns the user to the previous screen (Customers screen).
     *
     * @param event Executes when the user presses the Cancel button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionReturnPreviousScreen(ActionEvent event) throws IOException {
        System.out.println("Cancel button pressed");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();

    }


    /**
     * This method updates existing customer data in the database.
     * After the user updates existing customer data with valid inputs, the existing customer data is updated in the database.
     *
     * @param event Executes when Update button is pressed.
     * @throws SQLException In the event of an SQL error.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws SQLException, IOException {
        int customerID = selectedCustomer.getCustomerID();
        String updatedCustomerName = textFieldCustomerName.getText();
        String updatedCustomerAddress = textFieldAddress.getText();
        String updatedPostalCode = textFieldPostalCode.getText();
        String updatedCustomerPhone = textFieldPhone.getText();
        String updatedCustomerDivision = dropDownDivision.getValue();

        int updatedDivisionID = 0;

        ObservableList<Division> divisionList = DBDivisions.getAllDivisions();

        for (Division division : divisionList)
        {
            if (division.getDivisionName().equals(updatedCustomerDivision))
            {
                updatedDivisionID = division.getDivisionID();
            }
        }

        DBCustomers.modifyCustomer(customerID, updatedCustomerName, updatedCustomerAddress, updatedPostalCode,
                updatedCustomerPhone, updatedDivisionID);

        //      --------------------   RETURN TO CUSTOMER SCREEN   ------------------------------------
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();

    }


    /**
     * This method initializes the Modify Customer screen.
     * Here the dropDownCountry and dropDownDivision comboboxes are populated.
     * The label labelDivision is also updated to appropriate terminology.
     *
     * Lambda expression #1 - populates ObservableList with String values of division names
     * Lambda expression #2 - populates ObservableList with String values of country names
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ObservableList<Country> allCountries = DBCountries.getAllCountries();
        ObservableList<String> allCountriesString = FXCollections.observableArrayList();

        ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
        ObservableList<String> allDivisionsString = FXCollections.observableArrayList();

        //  --->   LAMBDA expression #1  <---
        allCountries.forEach(country -> allCountriesString.add(country.toString()));

        //  --->   LAMBDA expression #2  <---
        allDivisions.forEach(division -> allDivisionsString.add(division.toString()));

        selectedCustomer = CustomerScreenController.getSelectedCustomer();
        String countryName = selectedCustomer.getCustomerCountry();

        textFieldCustomerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
        textFieldCustomerName.setText(selectedCustomer.getCustomerName());
        textFieldAddress.setText(selectedCustomer.getCustomerAddress());
        textFieldPostalCode.setText(selectedCustomer.getCustomerPostalCode());
        textFieldPhone.setText(selectedCustomer.getCustomerPhone());

        dropDownCountry.setItems(allCountriesString);
        dropDownDivision.setItems(allDivisionsString);

        dropDownCountry.setVisibleRowCount(5);              //   Limit dropdown box row count to 5
        dropDownDivision.setVisibleRowCount(5);              //   Limit dropdown box row count to 5


        dropDownCountry.setValue(countryName);
        dropDownDivision.setValue(selectedCustomer.getDivisionName());


        if (countryName.equals("U.S"))
        {
            labelDivision.setText("State");
        }

        else if (countryName.equals("Canada"))
        {
            labelDivision.setText("Province/Territory");
        }

        else if (countryName.equals("UK"))
        {
            labelDivision.setText("Country/Province");
        }

    }

}
