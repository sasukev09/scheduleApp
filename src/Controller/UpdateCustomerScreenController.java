package Controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBDivisions;
import Models.Country;
import Models.Customer;
import Models.Division;
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
public class UpdateCustomerScreenController implements Initializable{
    @FXML
    private Button cancel_button;

    @FXML
    private ComboBox<String> country_cbox;

    @FXML
    private ComboBox<String> division_cbox;

    @FXML
    private Label division_label;

    @FXML
    private TextField address_txtfield;

    @FXML
    private TextField customerid_txtfield;

    @FXML
    private TextField name_txtfield;

    @FXML
    private TextField phone_txtfield;

    @FXML
    private TextField postal_txtfield;

    @FXML
    private Button update_button;

    Stage stage;
    Parent scene;
    Customer selectedCustomer;

    /**
     * This method populates division_cbox combobox with the appropriate list once the user selects a value in country_cbox combo box.
     * The label division_label is also updated to the appropriate term as related to their customs.
     * @param event Executes when user selects country from country_cbox combo box.
     * @throws NullPointerException In the event that a null value is unexpectedly returned.
     */
    @FXML
    void onActionCountryComboBox(ActionEvent event) throws NullPointerException
    {
        division_cbox.setValue("");

        ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
        ObservableList<String> filteredDivisionNames = FXCollections.observableArrayList();

        String countryName = country_cbox.getValue();

        if (countryName.equals("U.S"))
        {
            division_label.setText("State");
        }

        else if (countryName.equals("Canada"))
        {
            division_label.setText("Province/Territory");
        }

        else if (countryName.equals("UK"))
        {
            division_label.setText("Country/Province");
        }

        for (Division d : allDivisions)
        {
            if (d.getCountryName(d.getCountryID()).equals(countryName))
            {
                filteredDivisionNames.add(d.getDivisionName());
            }
        }

        division_cbox.setItems(filteredDivisionNames);

    }

    /**
     * This method returns the user to the previous screen (Customers screen).
     *
     * @param event Executes when the user presses the Cancel button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionCancelButton(ActionEvent event) throws IOException {
        System.out.println("Cancel button pressed");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
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
    void onActionUpdateButton(ActionEvent event) throws SQLException, IOException {
        int customerID = selectedCustomer.getCustomerID();
        String updatedCustomerName = name_txtfield.getText();
        String updatedCustomerAddress = address_txtfield.getText();
        String updatedPostalCode = postal_txtfield.getText();
        String updatedCustomerPhone = phone_txtfield.getText();
        String updatedCustomerDivision = division_cbox.getValue();

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

        //returning to customer screen
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }


    /**
     * This method initializes the Modify Customer screen.
     * Here the country_cbox and division_cbox comboboxes are populated.
     * The label division_label is also updated to its appropiate data
     *
     * Lambda 1 populates ObservableList with String values of division names
     * Lambda 2 populates ObservableList with String values of country names
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ObservableList<Country> allCountries = DBCountries.getAllCountries();
        ObservableList<String> allCountriesInString = FXCollections.observableArrayList();

        ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
        ObservableList<String> allDivisionsInString = FXCollections.observableArrayList();

        //lambda 1
        allCountries.forEach(country -> allCountriesInString.add(country.toString()));

        //lambda 2
        allDivisions.forEach(division -> allDivisionsInString.add(division.toString()));

        selectedCustomer = CustomerScreenController.getSelectedCustomer();
        String countryName = selectedCustomer.getCustomerCountry();

        customerid_txtfield.setText(Integer.toString(selectedCustomer.getCustomerID()));
        name_txtfield.setText(selectedCustomer.getCustomerName());
        address_txtfield.setText(selectedCustomer.getCustomerAddress());
        postal_txtfield.setText(selectedCustomer.getCustomerPostalCode());
        phone_txtfield.setText(selectedCustomer.getCustomerPhone());

        //setting items and selected row counts in the combo boxes
        country_cbox.setItems(allCountriesInString);
        division_cbox.setItems(allDivisionsInString);
        country_cbox.setVisibleRowCount(5);
        division_cbox.setVisibleRowCount(5);

        country_cbox.setValue(countryName);
        division_cbox.setValue(selectedCustomer.getDivisionName());

        if (countryName.equals("U.S"))
        {
            division_label.setText("State");
        }

        else if (countryName.equals("Canada"))
        {
            division_label.setText("Province/Territory");
        }

        else if (countryName.equals("UK"))
        {
            division_label.setText("Country/Province");
        }

    }

}
