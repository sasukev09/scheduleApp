package Controller;
import DAO.DAOAppointments;
import DAO.DAOCountries;
import DAO.DAOCustomers;
import DAO.DAODivisions;
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

    @FXML
    private ComboBox<String> country_cbox;

    @FXML
    private ComboBox<String> division_cbox;

    @FXML
    private Button reset_button;

    @FXML
    private TableColumn<?, ?> customerID_col;

    @FXML
    private TableColumn<?, ?> name_col;

    @FXML
    private TableColumn<?, ?> address_col;

    @FXML
    private TableColumn<?, ?> division_col;

    @FXML
    private TableColumn<?, ?> country_col;

    @FXML
    private TableColumn<?, ?> postal_col;

    @FXML
    private TableColumn<?, ?> phone_col;

    @FXML
    private TableView<Customer> customer_tableview;

    @FXML
    private Button back_button;

    @FXML
    private Button add_button;

    @FXML
    private Button update_button;

    @FXML
    private Button delete_button;

    Stage stage;
    Parent scene;
    static Customer selectedCustomer;

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
    void onActionResetButton(ActionEvent event) throws IOException
    {
        System.out.println("Reset button pressed");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/CustomersMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * This method applies user selected filters to the list displayed in the tableview.
     * User input values are extracted from country_cbox and division_cbox comboboxes and utilized in following conditional statements to filter what Customers are displayed in the tableview.
     * The table view is updated.
     *
     * Lambda 1 populates string list of division names from list of division objects
     * Lambda 2 populates string list of country names from list of country objects
     * @param event Executes when the user presses the Apply button.
     */
    @FXML
    void onActionApplyCboxes(ActionEvent event)
    {
        System.out.println("Combo boxes pressed");

        String filteredCountry = country_cbox.getValue();
        String filteredDivision = division_cbox.getValue();

        ObservableList<Customer> customerList = DAOCustomers.getAllCustomers();
        ObservableList<Customer> filteredCustomerList = FXCollections.observableArrayList();


        //checks if either combo box has a selection
        if (!filteredCountry.equals("Country") || !filteredDivision.equals("Division"))
        {
            //checks if only country_box is selected
            if (!filteredCountry.equals("Country") && filteredDivision.equals("Division"))
            {
                System.out.println(filteredCountry);

                //updates division_cbox to reflect divisions in selected country
                ObservableList<Division> allDivisions = DAODivisions.getAllDivisions();
                ObservableList<String> filteredDivisionNames = FXCollections.observableArrayList();

                for (Division division : allDivisions)
                {
                    if (division.getCountryName(division.getCountryID()).equals(filteredCountry))
                    {
                        filteredDivisionNames.add(division.getDivisionName());
                    }
                }

                division_cbox.setItems(filteredDivisionNames);

                for (Customer customer : customerList)
                {
                    if (customer.getCustomerCountry().equals(filteredCountry))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //checks if only division_cbox is selected
            else if (filteredCountry.equals("Country") && !filteredDivision.equals("Division"))
            {
                for (Customer customer : customerList)
                {
                    if (customer.getDivisionName().equals(filteredDivision))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //checks if country & division combo boxes are selected
            else if (!filteredCountry.equals("Country") && !filteredDivision.equals("Division"))
            {
                for (Customer customer : customerList)
                {
                    if (customer.getCustomerCountry().equals(filteredCountry) && customer.getDivisionName().equals(filteredDivision))
                    {
                        filteredCustomerList.add(customer);
                    }
                }
            }

            //updating the tableview
            //creates observable list of string values to populate combo  boxes to filter customer table
            ObservableList<Division> allDivisions = DAODivisions.getAllDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();

            ObservableList<Country> allCountries = DAOCountries.getAllCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            //lambda 1
            allDivisions.forEach(division -> divisionNames.add(division.getDivisionName()));

            //lambda 2
            allCountries.forEach(country -> countryNames.add(country.getCountryName()));

            customer_tableview.setItems(filteredCustomerList);

            customerID_col.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            name_col.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address_col.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            division_col.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            country_col.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            postal_col.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            phone_col.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        }

        //if no action is done
        else
        {
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
    void onActionBackButton(ActionEvent event) throws IOException
    {
        System.out.println("Back button pressed");
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
    void onActionAddButton(ActionEvent event) throws IOException {
        System.out.println("Add button pressed");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/AddCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method loads the Modify Customer screen.
     * Loads Modify Customer screen.
     * @param event Executes when the user presses the Modify button.
     */
    @FXML
    void onActionUpdateButton(ActionEvent event) throws IOException {
        System.out.println("update button pressed");
        selectedCustomer = customer_tableview.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a Customer");
            alert.showAndWait();
            return;
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/UpdateCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * This method deletes user selected customer and all appointments associated with the selected customer.
     * Through a series of conditional statements, this method determines if the user selected customer has any appointments.
     * If appointments are found, the appointments are deleted and then the customer is deleted from the database.
     * @param event Executes when the user presses the Delete button.
     */
    @FXML
    void onActionDeleteButton(ActionEvent event)
    {
        System.out.println("Delete Customer button pressed");
        selectedCustomer = customer_tableview.getSelectionModel().getSelectedItem();

        try
        {
            //checking for customer selection
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please select a Customer");
                alert.showAndWait();
                return;
            }

            //confirmation alert for delete
            else
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Are you sure you want to delete customer " + selectedCustomer.getCustomerName() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                    if (!selectedCustomer.hasAppointments()) {
                        System.out.println("This customer has no appointments, deleting customer");

                        DAOCustomers.deleteCustomer(selectedCustomer.getCustomerID());

                        customer_tableview.setItems(DAOCustomers.getAllCustomers());
                    }
                    else if (selectedCustomer.hasAppointments())
                    {
                        System.out.println("This customer has appointments, deleting appointments first, then customer");

                        DAOAppointments.deleteCustomerAppointments(selectedCustomer.getCustomerID());
                        DAOCustomers.deleteCustomer(selectedCustomer.getCustomerID());

                        customer_tableview.setItems(DAOCustomers.getAllCustomers());
                    }

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("INFORMATION");
                    alert2.setContentText("Customer ID " + selectedCustomer.getCustomerID() +
                            " (" + selectedCustomer.getCustomerName() + ") has been deleted.");
                    alert2.showAndWait();
                    return;
                }
                //if action is cancelled
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
     * Here the comboboxes country_cbox, division_cbox, and the customer_tableview are populated.
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
        try
        {
            //Creating observable lists of string values to populate combo boxes to filter customer table
            ObservableList<Division> allDivisions = DAODivisions.getAllDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();

            ObservableList<Country> allCountries = DAOCountries.getAllCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            //lambda 1
            allDivisions.forEach(division -> divisionNames.add(division.getDivisionName()));

            //lambda 2
            allCountries.forEach(country -> countryNames.add(country.getCountryName()));


            customer_tableview.setItems(DAOCustomers.getAllCustomers());

            customerID_col.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            name_col.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address_col.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            division_col.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            country_col.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            postal_col.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            phone_col.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));

            //setting labels and row counts for the combo boxes
            country_cbox.setValue("Country");
            country_cbox.setItems(countryNames);
            country_cbox.setVisibleRowCount(5);

            division_cbox.setValue("Division");
            division_cbox.setItems(divisionNames);
            division_cbox.setVisibleRowCount(5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
