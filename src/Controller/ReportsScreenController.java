package Controller;


import DAO.DBAppointments;
import DAO.DBContacts;
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
    private TableView<ReportByMonthType> appointmentsMonthTypeTableView;

    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colCount;

    @FXML
    private TableView<ReportByDivision> customerLocationsTableView;

    @FXML
    private TableColumn<?, ?> colDSP;

    @FXML
    private TableColumn<?, ?> colDSPCount;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<?, ?> columnAppointmentID;

    @FXML
    private TableColumn<?, ?> columnCustomer;

    @FXML
    private TableColumn<?, ?> columnDescription;

    @FXML
    private TableColumn<?, ?> columnEnd;

    @FXML
    private TableColumn<?, ?> columnStart;

    @FXML
    private TableColumn<?, ?> columnTitle;

    @FXML
    private TableColumn<?, ?> columnType;

    @FXML
    private ComboBox<String> dropDownContact;

    @FXML
    private Button mainMenuButton;

    /**
     * This method filters the appointments table view by contact name, received as input via the combobox dropDownContact.
     * User input is gathered as a string value and a helper function completes the process (DBAppointments.getFilteredAppointments()).
     * @param event Executes when the user selects a value from the combobox dropDownContact.
     */
    @FXML
    void onActionFilterContacts(ActionEvent event) {
        String contactFilter = dropDownContact.getValue();

        if (contactFilter.equals("All Contacts"))
        {
            appointmentTableView.setItems(DBAppointments.getAllAppointments());
        }

        else
        {
            appointmentTableView.setItems(DBAppointments.getFilteredAppointments(null, contactFilter));
        }

    }

    /**
     * This method navigates user back to Main Menu screen.
     * @param event Executes when the Main Menu button is pressed.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();

    }


    /**
     * This method initializes the Reports screen.
     * The appointment, appointment by month and type, and customer by location tableviews are populated here.
     * LAMBDA #1 iterates through list of all appointments, adds contact name String value to list contactNames
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // initialize screen

        try {
            ObservableList<Contact> allContacts = DBContacts.getAllContacts();

            ObservableList<String> contactNames = FXCollections.observableArrayList();

            //   --->   Create All Contacts option in contact filter drop down   <---
            contactNames.add("All Contacts");

            //  --->   LAMBDA expression #1  <---
            allContacts.forEach(contact -> contactNames.add(contact.getContactName()));

            dropDownContact.setItems(contactNames);
            dropDownContact.setValue("All Contacts");
            dropDownContact.setVisibleRowCount(5);

            //  --->   Populate appointmentTableView and columns  <---
            appointmentTableView.setItems(DBAppointments.getAllAppointments());

            columnAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            columnTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
            columnDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            columnStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
            columnEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
            columnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerID"));

            //  --->   Populate appointmentsMonthTypeTableView and columns  <---
            appointmentsMonthTypeTableView.setItems(DBAppointments.getReportsByMonthType());

            colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colCount.setCellValueFactory(new PropertyValueFactory<>("count"));

            //  --->   Populate customerLocationsTableView and columns  <---
            customerLocationsTableView.setItems(DBAppointments.getCustomersByDivision());

            colDSP.setCellValueFactory(new PropertyValueFactory<>("division"));
            colDSPCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
