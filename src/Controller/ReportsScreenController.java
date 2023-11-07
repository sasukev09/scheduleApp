package Controller;


import DAO.DAOAppointments;
import DAO.DAOContacts;
import Models.Appointment;
import Models.Contact;
import Models.ReportByDivision;
import Models.ReportByMonthType;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is controls the Reports Screen.
 * Controls the Reports screen of the app.
 */
public class ReportsScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<ReportByMonthType> monthType_tableview;

    @FXML
    private TableColumn<?, ?> month_col;

    @FXML
    private TableColumn<?, ?> type_Col;

    @FXML
    private TableColumn<?, ?> monthTypeCount_col;

    @FXML
    private TableView<ReportByDivision> customerLoca_tableView;

    @FXML
    private TableColumn<?, ?> divStatProv_col;

    @FXML
    private TableColumn<?, ?> divStatProvCount_col;

    @FXML
    private TableView<Appointment> appointment_tableview;

    @FXML
    private TableColumn<?, ?> appointmentid_col;

    @FXML
    private TableColumn<?, ?> customer_col;

    @FXML
    private TableColumn<?, ?> description_col;

    @FXML
    private TableColumn<?, ?> end_col;

    @FXML
    private TableColumn<?, ?> start_col;

    @FXML
    private TableColumn<?, ?> columnTitle;

    @FXML
    private TableColumn<?, ?> columnType;

    @FXML
    private ComboBox<String> contact_cbox;

    @FXML
    private Button mainMenuButton;

    /**
     * This method filters the appointments table view by contact name, received as input via the combobox dropDownContact.
     * User input is gathered as a string value and a helper function completes the process (DBAppointments.getFilteredAppointments()).
     * @param event Executes when the user selects a value from the combobox dropDownContact.
     */
    @FXML
    void onActionContactComboBox(ActionEvent event) {
        String contactFilter = contact_cbox.getValue();

        if (contactFilter.equals("All Contacts"))
        {
            appointment_tableview.setItems(DAOAppointments.getAllAppointments());
        }

        else
        {
            appointment_tableview.setItems(DAOAppointments.getFilteredAppointments(null, contactFilter));
        }

    }

    /**
     * This method navigates user back to Main Menu screen.
     * @param event Executes when the Back button is pressed.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionBackButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }


    /**
     * This method initializes the Reports screen.
     * The appointment, appointment by month and type, and customer by location tableviews are populated here.
     * Lambda 1 iterates through list of all appointments, adds contact name String type value to list contactNames
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            ObservableList<Contact> allContacts = DAOContacts.getAllContacts();

            ObservableList<String> contactNames = FXCollections.observableArrayList();

            //creating "All Contacts" option for the contact combo box
            contactNames.add("All Contacts");

            //lambda 1
            allContacts.forEach(contact -> contactNames.add(contact.getContactName()));

            contact_cbox.setItems(contactNames);
            contact_cbox.setValue("All Contacts");
            contact_cbox.setVisibleRowCount(5);

            //populating appointment table view and its columns
            appointment_tableview.setItems(DAOAppointments.getAllAppointments());

            appointmentid_col.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            columnTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
            description_col.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            start_col.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
            end_col.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
            customer_col.setCellValueFactory(new PropertyValueFactory<>("customerID"));

            //Populating the month and type table view and its columns
            monthType_tableview.setItems(DAOAppointments.getReportsByMonthType());

            month_col.setCellValueFactory(new PropertyValueFactory<>("month"));
            type_Col.setCellValueFactory(new PropertyValueFactory<>("type"));
            monthTypeCount_col.setCellValueFactory(new PropertyValueFactory<>("count"));

            //Populating the customer locations table view
            customerLoca_tableView.setItems(DAOAppointments.getCustomersByDivision());

            divStatProv_col.setCellValueFactory(new PropertyValueFactory<>("division"));
            divStatProvCount_col.setCellValueFactory(new PropertyValueFactory<>("count"));
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
