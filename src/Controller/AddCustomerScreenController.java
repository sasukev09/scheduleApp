package Controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBDivisions;
import Models.Country;
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
 * This class controls the Add Customer screen.
 * The Add Customer screen enables the user to enter and upload new customer data to the database.
 */
public class AddCustomerScreenController implements Initializable {

    @FXML
    private ComboBox<String> country_cbox;

    @FXML
    private ComboBox<String> division_cbox;

    @FXML
    private Label division_label;

    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    @FXML
    private TextField CustomerID_textfield;

    @FXML
    private TextField name_txtfield;

    @FXML
    private TextField address_txtfield;

    @FXML
    private TextField postal_txfield;

    @FXML
    private TextField phone_txtfield;
    Stage stage;
    Parent scene;

    /**
     * This method adds a new customer to the database.
     * After the user inputs valid new customer data, a new Customer object is created and uploaded to the database.
     *
     * @param event Executes when Add button is pressed.
     * @throws SQLException In the event of an SQL error.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionAddButton(ActionEvent event) throws SQLException, IOException
    {
        String customerName = name_txtfield.getText();
        String customerAddress = address_txtfield.getText();
        String postalCode = postal_txfield.getText();
        String phoneNumber = phone_txtfield.getText();

        String countryName = country_cbox.getValue();
        String divisionName = division_cbox.getValue();

        if (!customerName.isEmpty() || !customerAddress.isEmpty() || !postalCode.isEmpty() || !phoneNumber.isEmpty() || !countryName.isEmpty() || !divisionName.isEmpty())
        {
            System.out.println("Adding " + customerName);

            //adding a new customer through sql
            DBCustomers.addNewCustomer(customerName, customerAddress, postalCode, phoneNumber, divisionName);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();

        }
    }

    /**
     * This method populates division_cbox combobox with the appropriate list once the user selects a value in dropDownCountry combobox.
     * The label division_label is also updated to the appropriate term as related to their customs.
     *
     * @param event Executes when user selects country from dropDownCountry combobox.
     */
    @FXML
    void onActionCountry(ActionEvent event)
    {
        ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
        ObservableList<String> filteredDivisionNames = FXCollections.observableArrayList();

        String countryName = country_cbox.getValue().toString();

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

        for (Division selectedDivision : allDivisions)
        {
            if (selectedDivision.getCountryName(selectedDivision.getCountryID()).equals(countryName))
            {
                filteredDivisionNames.add(selectedDivision.getDivisionName());
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
    void onActionCancelButton(ActionEvent event) throws IOException
    {
        System.out.println("Cancel button pressed");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method initializes the Add Customer screen.
     * Here country_cbox and dropDownDivision division_cbox are populated.
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //initialize screen, populate dropDownCountry box w string values
        ObservableList<Country> allCountries = DBCountries.getAllCountries();
        ObservableList<String> allCountriesInString = FXCollections.observableArrayList();

        allCountries.forEach(country -> allCountriesInString.add(country.toString()));

        country_cbox.setItems(allCountriesInString);
        country_cbox.setVisibleRowCount(5);

        division_cbox.getSelectionModel().clearSelection();
        division_cbox.setVisibleRowCount(5);

        CustomerID_textfield.setPromptText("Auto-Gen");
    }
}