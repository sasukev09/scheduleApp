package Controller;

import DAO.DAOAppointments;
import DAO.DAOContacts;
import Models.Appointment;
import Models.Contact;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class controls the Appointment screen.
 * It allows the user to view and filter all appointments, add, update or delete appointments.
 */
public class AppointmentScreenController implements Initializable {

    @FXML
    private TableView<Appointment> appointment_TableView;

    @FXML
    private TableColumn<?, ?> AppointmentID_col;

    @FXML
    private TableColumn<?, ?> contact_Column;

    @FXML
    private TableColumn<?, ?> customer_Column;

    @FXML
    private TableColumn<?, ?> description_col;

    @FXML
    private TableColumn<?, ?> end_Col;

    @FXML
    private TableColumn<?, ?> location_col;

    @FXML
    private TableColumn<?, ?> start_column;

    @FXML
    private TableColumn<?, ?> title_col;

    @FXML
    private TableColumn<?, ?> type_col;

    @FXML
    private TableColumn<?, ?> user_Col;

    @FXML
    private ComboBox<String> contact_cbox;

    @FXML
    private ComboBox<String> time_cbox;

    @FXML
    private Button apply_Button;

    @FXML
    private Button back_button;

    @FXML
    private Button add_Button;

    @FXML
    private Button update_button;

    @FXML
    private Button delete_button;
    Stage stage;
    Parent scene;
    static Appointment selectedAppointment;

    /**
     * This method applies user selected filters to data displayed in tableview.
     * User input is gathered from time_cbox and contact_cbox comboboxes and used as arguments (filters) when getting appointments from the database.
     * The tableview is automatically updated.
     * @param event Executes when the user presses the Apply button.
     */
    @FXML
    void onActionApplyButton(ActionEvent event) {
        String filteredTime = time_cbox.getValue();
        String filteredContact = contact_cbox.getValue();

        //testing inputs
    //System.out.println(timeFilter);
    //System.out.println(contactFilter);

        appointment_TableView.setItems(DAOAppointments.getFilteredAppointments(filteredTime, filteredContact));
    }

    /**
     * The method resets the time_cbox and contact_cbox comboboxes to their respective original prompts and null values.
     * The tableview is updated automatically.
     *
     * @param event Executes when the user presses the reset button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionResetButton(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/AppointmentsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method returns the user to the Main Menu.
     * returns user the Main Menu.
     * @param event Executes when the user presses the Main Menu button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionBackButton(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * This method loads the Add Appointment screen.
     * Loads the Add Appointment screen.
     * @param event Executes when the user presses the Add button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionAddButton(ActionEvent event) throws IOException
    {
        System.out.println("Add button selected");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/AddAppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method loads the Modify Appointment screen.
     * Loads the Modify Appointment screen.
     * @param event Executes when the user presses the Modify button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionUpdateButton (ActionEvent event) throws IOException
    {
        System.out.println("Update button selected");

        selectedAppointment = appointment_TableView.getSelectionModel().getSelectedItem();

        //confirmation that there is an appointment selected
        if (selectedAppointment == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select an Appointment");
            alert.showAndWait();
            return;
        }

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/UpdateAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * This method deletes appointment data from the database.
     * After user confirms deletion in a dialog box, this method deletes appointment data from the database, then provides confirmation of deletion in another dialog box.
     * @param event Executes when the user presses the Delete button.
     * @throws IOException In the event of an IO error.
     * @throws SQLException In the event of an SQL error.
     */
    @FXML
    void onActionDeleteButton(ActionEvent event) throws IOException, SQLException {
        System.out.println("Delete appointment selected");
        selectedAppointment = appointment_TableView.getSelectionModel().getSelectedItem();


        try {
            //confirmation that there is an appointment selected
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please select an Appointment");
                alert.showAndWait();
                return;
            }

            //confirmation for deletion
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("Are you sure you want to delete?");
                Optional<ButtonType> confirmation = alert.showAndWait();

                if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                    DAOAppointments.deleteAppointment(selectedAppointment.getAppointmentID());

                    ObservableList<Appointment> allAppointments = DAOAppointments.getAllAppointments();
                    appointment_TableView.setItems(allAppointments);

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("INFORMATION");
                    alert2.setContentText("Appointment ID " + selectedAppointment.getAppointmentID() +
                            " (" + selectedAppointment.getAppointmentType() + ") with " +
                            selectedAppointment.getCustomerName(selectedAppointment.getCustomerID()) + " has been deleted.");
                    alert2.showAndWait();
                    return;

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Deleting appointment with " + selectedAppointment.getCustomerName(selectedAppointment.getCustomerID()));
    }

    /**
     * This method initializes the Appointment screen.
     * Here the appointment_TableView, time_cbox and contact_cbox comboboxes get populated.
     * Lambda 1 populates list of Contact names
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //initializing screen
        try {
            ObservableList<Contact> allContacts = DAOContacts.getAllContacts();
            ObservableList<String> contactNames = FXCollections.observableArrayList();

            //lambda 1
            allContacts.forEach(contact -> contactNames.add(contact.getContactName()));
            contact_cbox.setItems(contactNames);
            contact_cbox.setVisibleRowCount(5);

            //populating time_cbox
            ObservableList<String> timeFilters = FXCollections.observableArrayList();
//            timeFilters.add("All");
            timeFilters.add("Current Week");
            timeFilters.add("Current Month");
            time_cbox.setItems(timeFilters);

            //populating appointment_TableView and its columns
            appointment_TableView.setItems(DAOAppointments.getAllAppointments());

            AppointmentID_col.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title_col.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
            description_col.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
            location_col.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
            type_col.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            start_column.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
            end_Col.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
            customer_Column.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            user_Col.setCellValueFactory(new PropertyValueFactory<>("userID"));
            contact_Column.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
