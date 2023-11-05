package Controller;
import DAO.DBAppointments;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class controls the Customer Screen.
 * The Customer screen enables the user to view all customers in database, and add/modify/delete customers from database.
 */
public class CustomerScreenController implements Initializable {

    Stage stage;
    Parent scene;
    static Customer selectedCustomer;

    @FXML
    private ComboBox<String> dropDownCountry;

    @FXML
    private ComboBox<String> dropDownDivision;

    @FXML
    private Button resetFilterButton;

    @FXML
    private TableColumn<?, ?> columnCustomerID;

    @FXML
    private TableColumn<?, ?> columnName;

    @FXML
    private TableColumn<?, ?> columnAddress;

    @FXML
    private TableColumn<?, ?> column1stLevelDivision;

    @FXML
    private TableColumn<?, ?> columnCountry;

    @FXML
    private TableColumn<?, ?> columnPostalCode;

    @FXML
    private TableColumn<?, ?> columnPhone;

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button modifyCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    /**
     * This method gets the user selected customer in the Customer screen.
     * @return Returns Customer object, the customer selected by the user.
     */
    public static Customer getSelectedCustomer()
    {
        return selectedCustomer;
    }

    /**
     * This method reloads the Customer screen, essentially resetting the drop-down comboboxes serving as filters.
     * @param event Executes when the user presses the Reset button.
     * @throws IOException In the event of an IO exception.
     */
    @FXML
    void onActionResetFilter(ActionEvent event) throws IOException
    {
        System.out.println("Reset filter button pressed");

//        dropDownCountry.getSelectionModel().clearSelection();
//        dropDownDivision.getSelectionModel().clearSelection();

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();

    }

    /**
     * This method applies user selected filters to the list displayed in the tableview.
     * User input values are extracted from dropDownCountry and dropDownDivision comboboxes and utilized in following conditional statements to filter what Customers are displayed in the tableview.
     * The table view is updated.
     *
     * Lambda expressions #1 - populates string list of division names from list of division objects
     * Lambda expressions #1 - populates string list of country names from list of country objects
     * @param event Executes when the user presses the Apply button.
     */
    @FXML
    void onActionApplyFilter(ActionEvent event)
    {
        System.out.println("Apply filter button pressed");

        String countryFilter = dropDownCountry.getValue();
        String divisionFilter = dropDownDivision.getValue();

        ObservableList<Customer> customerList = DBCustomers.getAllCustomers();
        ObservableList<Customer> filteredCustomerList = FXCollections.observableArrayList();


        //   -----   if either dropdown has a selection   -----
        if (!countryFilter.equals("Country") || !divisionFilter.equals("Division"))
        {
            //  -----   if only country drop down is selected   -----
            if (!countryFilter.equals("Country") && divisionFilter.equals("Division"))
            {
                System.out.println(countryFilter);          // test

                //  -----   update dropDownDivision to reflect divisions in selected country   -----
                ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
                ObservableList<String> filteredDivisionNames = FXCollections.observableArrayList();

                for (Division division : allDivisions)
                {
                    if (division.getCountryName(division.getCountryID()).equals(countryFilter))
                    {
                        filteredDivisionNames.add(division.getDivisionName());
                    }
                }

                dropDownDivision.setItems(filteredDivisionNames);

                for (Customer customer : customerList)
                {
                    if (customer.getCustomerCountry().equals(countryFilter))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //  -----   if only division drop down is selected   -----
            else if (countryFilter.equals("Country") && !divisionFilter.equals("Division"))
            {
                for (Customer customer : customerList)
                {
                    if (customer.getDivisionName().equals(divisionFilter))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //  -----   if country & division drop down are selected   -----
            else if (!countryFilter.equals("Country") && !divisionFilter.equals("Division"))
            {
                for (Customer customer : customerList)
                {
                    if (customer.getCustomerCountry().equals(countryFilter) && customer.getDivisionName().equals(divisionFilter))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //  ------------------------   VVVVV   update tableview   VVVVV   ------------------------
            //  ------   Creates observable list of string values to populate drop down boxes to filter customer table   ------
            ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();

            ObservableList<Country> allCountries = DBCountries.getAllCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            //  --->   LAMBDA expression #1   <---
            allDivisions.forEach(division -> divisionNames.add(division.getDivisionName()));

            //  --->   LAMBDA expression #2   <---
            allCountries.forEach(country -> countryNames.add(country.getCountryName()));

            customersTableView.setItems(filteredCustomerList);

            columnCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            columnAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            column1stLevelDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            columnCountry.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            columnPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        }

        else
        {
            //   do nothing
            System.out.println("No filter selected");
        }
    }

    /**
     * This method returns the user to the Main Menu.
     * Returns user the Main Menu.
     * @param event Executes when the user presses the Main Menu button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();
    }

    /**
     * This method loads the Add Customer screen.
     * Loads Add Customer screen.
     * @param event Executes when the user presses the Add button.
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        System.out.println("Add Customer Button pressed");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();
    }

    /**
     * This method loads the Modify Customer screen.
     * Loads Modify Customer screen.
     * @param event Executes when the user presses the Modify button.
     */
    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        System.out.println("Modify Customer button pressed");

        selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a Customer");
            alert.showAndWait();
            return;
        }

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/ModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();

    }


    /**
     * This method deletes user selected customer and all appointments associated with the selected customer.
     * Through a series of conditional statements, this method determines if the user selected customer has any appointments.
     * If appointments are found, the appointments are deleted and then the customer is deleted from the database.
     * @param event Executes when the user presses the Delete button.
     */
    @FXML
    void onActionDeleteCustomer(ActionEvent event)
    {
        System.out.println("Delete Customer button pressed");
        selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();

        try
        {
            //  -------------------------------   Check for selection   ---------------------------------------
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please select a Customer");
                alert.showAndWait();
                return;
            }


            //  -------------------------------   Confirm delete   ---------------------------------------

            else
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer Confirmation");
                alert.setContentText("Are you sure you want to delete the customer " + selectedCustomer.getCustomerName() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                    if (!selectedCustomer.hasAppointments()) {
                        System.out.println("Customer has no appointments, deleting customer");

                        DBCustomers.deleteCustomer(selectedCustomer.getCustomerID());

                        customersTableView.setItems(DBCustomers.getAllCustomers());
                    }
                    else if (selectedCustomer.hasAppointments())
                    {
                        System.out.println("Customer has appointments, deleting appointments first, then customer");

                        DBAppointments.deleteCustomerAppointments(selectedCustomer.getCustomerID());
                        DBCustomers.deleteCustomer(selectedCustomer.getCustomerID());

                        customersTableView.setItems(DBCustomers.getAllCustomers());
                    }

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("");
                    alert2.setContentText("Customer ID #" + selectedCustomer.getCustomerID() +
                            " (" + selectedCustomer.getCustomerName() + ") has been deleted.");
                    alert2.showAndWait();
                    return;


                }

                else if (result.isPresent() && result.get() != ButtonType.OK)
                {
                    System.out.println("Deletion cancelled");
                    return;

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * This method initializes the Customer screen.
     * Here the comboboxes dropDownCountry, dropDownDivision, and the tableview are populated.
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
        try
        {
            //  ------   Creates observable list of string values to populate drop down boxes to filter customer table   ------
            ObservableList<Division> allDivisions = DBDivisions.getAllDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();

            ObservableList<Country> allCountries = DBCountries.getAllCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            //  --->   LAMBDA expression #1  <---
            allDivisions.forEach(division -> divisionNames.add(division.getDivisionName()));

            //  --->   LAMBDA expression #2  <---
            allCountries.forEach(country -> countryNames.add(country.getCountryName()));


            customersTableView.setItems(DBCustomers.getAllCustomers());

            columnCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            columnAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            column1stLevelDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            columnCountry.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            columnPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            columnPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));

            dropDownCountry.setValue("Country");                //   Set dropdown box label
            dropDownCountry.setItems(countryNames);             //   Populate dropdown box items
            dropDownCountry.setVisibleRowCount(5);              //   Limit dropdown box row count to 5

            dropDownDivision.setValue("Division");              //   Set dropdown box label
            dropDownDivision.setItems(divisionNames);           //   Populate dropdown box items
            dropDownDivision.setVisibleRowCount(5);              //   Limit dropdown box row count to 5
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
