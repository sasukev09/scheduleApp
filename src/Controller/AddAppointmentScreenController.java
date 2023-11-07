package Controller;

import DAO.DBAppointments;
import Utility.Tiempo;
import DAO.DBContacts;
import DAO.DBCustomers;
import DAO.DBUsers;
import Models.Contact;
import Models.Customer;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class controls the Add Appointment screen.
 * The Add Appointment screen enables the user to enter and upload new customer appointment data to the database.
 */
public class AddAppointmentScreenController implements Initializable {

    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    @FXML
    private DatePicker start_datepick;

    @FXML
    private DatePicker end_datepick;

    @FXML
    private ComboBox<String> starttime_cbox;

    @FXML
    private ComboBox<String> endtime_cbox;

    @FXML
    private ComboBox<String> contact_cbox;

    @FXML
    private ComboBox<String> customerID_cbox;

    @FXML
    private ComboBox<String> userID_cbox;

    @FXML
    private TextField desc_txtfield;

    @FXML
    private TextField location_txtfield;

    @FXML
    private TextField title_txtfield;

    @FXML
    private TextField type_txtfield;

    @FXML
    private TextField customerName_txtfield;

    @FXML
    private TextField userName_txtfield;
    Stage stage;
    Parent scene;

    /**
     * This method takes the user input from customerID_cbox combobox, updates customerName_txtfield with name of customer.
     * A list of all customers in the database is created and searched for a Customer object matching the Customer ID number via a for loop.
     * @param actionEvent Executes when the user selects a Customer ID from the customerID_cbox combobox.
     */
    @FXML
    void onActionCustomerCbox(ActionEvent actionEvent)
    {
        ObservableList<Customer> allExistingCustomers = DBCustomers.getAllCustomers();

        int customerID = Integer.parseInt(customerID_cbox.getSelectionModel().getSelectedItem());
        String customerName = "";

        for (Customer client : allExistingCustomers)
        {
            if (customerID == client.getCustomerID())
            {
                customerName = client.getCustomerName();
            }
        }

        customerName_txtfield.setText(customerName);
    }

    /**
     * This method takes the user input from userID_cbox combobox, updates userName_txtfield with name of user.
     * A list of all users in the database is created and searched for a User object matching the User ID number via a for loop.
     * @param actionEvent Executes when the user selects a User ID from the userID_cbox combobox.
     */
    @FXML
    void onActionUserCbox(ActionEvent actionEvent)
    {
        ObservableList<User> allExistingUsers = DBUsers.getAllUsers();

        int userID = Integer.parseInt(userID_cbox.getSelectionModel().getSelectedItem());
        String userName = "";

        for (User Usuario : allExistingUsers)
        {
            if (userID == Usuario.getUserID())
            {
                userName = Usuario.getUserName();
            }
        }

        userName_txtfield.setText(userName);
    }


    /**
     * This method adds a new customer appointment to the database.
     * After the user inputs valid data into the text fields and comboboxes, a new Appointment object is created and uploaded to the database after a series of conditional statements.
     *
     * @param event Executes when user presses the Add button.
     * @throws SQLException In the event of an SQL error.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionAddButton(ActionEvent event) throws SQLException, IOException {
        System.out.println("New appointment added");

        boolean newAppointment = true;

        DateTimeFormatter hourMinute = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateMonthDay = DateTimeFormatter.ofPattern("yyy-MM-dd");

        String title = title_txtfield.getText();
        String description = desc_txtfield.getText();
        String location = location_txtfield.getText();
        String contact = contact_cbox.getValue();
        String type = type_txtfield.getText();
        int customerID = Integer.parseInt(customerID_cbox.getValue());
        int userID = Integer.parseInt(userID_cbox.getValue());

        //get customer from database
        Customer customer = DBCustomers.getCustomer(customerID);

        //get contact ID from contact string
        ObservableList<Contact> allExistingContacts = DBContacts.getAllContacts();
        int contactID = 0;

        for (Contact contacto : allExistingContacts)
        {
            if (contacto.getContactName().equals(contact))
            {
                contactID = contacto.getContactID();
            }
        }

        //getting start/end dates from combo boxes
        String startTime = starttime_cbox.getValue();
        String endTime = endtime_cbox.getValue();

        //getting start/end dates from date pickers
        String startDate = start_datepick.getValue().format(dateMonthDay);
        String endDate = end_datepick.getValue().format(dateMonthDay);

        //timestamp variables from string time date
        Timestamp startTimeStamp = Tiempo.convertStringTimeDate2TimeStamp(startTime, startDate);
        Timestamp endTimeStamp = Tiempo.convertStringTimeDate2TimeStamp(endTime, endDate);

        //Testing
    //System.out.println(title);
    //System.out.println(description);
    //System.out.println(location);
    //System.out.println(contactID);
    //System.out.println(type);
    //System.out.println(startTime);
    //System.out.println(endTime);
    //System.out.println(startDate);
    //System.out.println(endDate);
    //System.out.println(customerID);
    //System.out.println(userID);


        //convert Timestamp to local date time
        LocalDateTime userRequestedStartDateTime = startTimeStamp.toLocalDateTime();
        LocalDateTime userRequestedEndDateTime = endTimeStamp.toLocalDateTime();

        //attach local time zone to local date time variables
        userRequestedStartDateTime = Tiempo.attachLocalTimeZone(userRequestedStartDateTime);
        userRequestedEndDateTime = Tiempo.attachLocalTimeZone(userRequestedEndDateTime);

        //confirmation that appointment start time is not before present now
        if (startTimeStamp.before(Timestamp.valueOf(LocalDateTime.now())))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID ENTRY");
            alert.setContentText("Chronological error. Appointment cannot be scheduled before current date/time.");
            alert.showAndWait();
            return;
        }

        //confirmation that end time is not before start time
        else if (endTimeStamp.before(startTimeStamp) || endTimeStamp.equals(startTimeStamp))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID ENTRY");
            alert.setContentText("Chronological error. End date/time must be after start date/time.");
            alert.showAndWait();
            return;
        }

        //confirmation that appointment time values are valid and within business hours
        //this should not execute due to combo boxes only allowing valid time entry
        else if (!Tiempo.businessHours(userRequestedStartDateTime, userRequestedEndDateTime))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID ENTRY");
            alert.setContentText("Business hours are 8:00 AM EST to 10:00 PM EST.");
            alert.showAndWait();
            return;
        }

        //confirmation that proposed appointment times do not overlap existing customer appointments
        else if (Tiempo.overlappingTime(newAppointment, customer, userRequestedStartDateTime, userRequestedEndDateTime) == 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Requested appointment start time overlaps existing appointment.");
            alert.showAndWait();
            return;
        }
        else if (Tiempo.overlappingTime(newAppointment, customer, userRequestedStartDateTime, userRequestedEndDateTime) == 2)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ErrorT");
            alert.setContentText("Requested appointment end time overlaps existing appointment.");
            alert.showAndWait();
            return;
        }
        else if (Tiempo.overlappingTime(newAppointment, customer, userRequestedStartDateTime, userRequestedEndDateTime) == 3)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Appointment already scheduled between the requested start/end time.");
            alert.showAndWait();
            return;
        }

        //no overlapping appointments found
        else if (Tiempo.overlappingTime(newAppointment, customer, userRequestedStartDateTime, userRequestedEndDateTime) == 4)
        {
            System.out.println("No chronological errors or scheduling conflicts detected. Adding appointment.");

            //  references in SQL
            //  Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID

            //adding the new appointment to the database
            DBAppointments.addNewAppointment(title, description, location, type, startTimeStamp, endTimeStamp, customerID, userID, contactID);

            //reloading screen after adding new appointment
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/Views/Appointments.fxml"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        }

    }

    /**
     * This method returns the user to the previous screen (Appointments screen).
     *
     * @param event Executes when the user presses the cancel button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionCancelButton(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();                 //  ----------------   Center Screen
        stage.show();
    }


    /**
     * This method initializes the Add Appointment screen. Here the starttime_cbox, endtime_cbox, contact_cbox, customerID_cbox, and userID_cbox comboboxes are populated.
     *
     * lambda 1 populates Observable list allExistingContacts with String values of contact name
     * lambda 2 populates Observable list customerIDs with String values of customer ID numbers.
     * lambda 3 populates Observable list userIDs with String values of user ID numbers.
     *
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {

            //populating contact_cbox
            ObservableList<Contact> allExistingContacts = DBContacts.getAllContacts();
            ObservableList<String> contactNames = FXCollections.observableArrayList();

            //lambda expression 1
            allExistingContacts.forEach(contact -> contactNames.add(contact.getContactName()));

            contact_cbox.setItems(contactNames);
            contact_cbox.setVisibleRowCount(5);


            //populates customerID_cbox
            ObservableList<Customer> allExistingCustomers = DBCustomers.getAllCustomers();
            ObservableList<String> customerIDs = FXCollections.observableArrayList();

            //lambda expression 2
            allExistingCustomers.forEach(customer -> customerIDs.add(String.valueOf(customer.getCustomerID())));

            //setting items in combo box and the row count of the drop down list
            customerID_cbox.setItems(customerIDs);
            customerID_cbox.setVisibleRowCount(5);


            //populates userID_cbox
            ObservableList<User> allUsers = DBUsers.getAllUsers();
            ObservableList<String> userIDs = FXCollections.observableArrayList();

            //lambda expression 3
            allUsers.forEach(user -> userIDs.add(String.valueOf(user.getUserID())));

            //setting items in combo box and the row count of the drop down list
            userID_cbox.setItems(userIDs);
            userID_cbox.setVisibleRowCount(5);


            //populates starttime_cbox
            //the appointment time range is 8am to 10pm EST
            //setting timezones to EST
            ObservableList<String> startTimes = FXCollections.observableArrayList();

            LocalDateTime beginningStartEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
            beginningStartEST = Tiempo.attachESTTimeZone(beginningStartEST);

            // setting variable to local time equivalent
            LocalDateTime earliestStartLocalTime = Tiempo.convertESTToLocalTimeZone(beginningStartEST);

            LocalDateTime lateStartEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 45));
            lateStartEST = Tiempo.attachESTTimeZone(lateStartEST);

            // setting variable to local time equivalent
            LocalDateTime latestStartLocalTime = Tiempo.convertESTToLocalTimeZone(lateStartEST);

            //adding start times to list
            while (earliestStartLocalTime.isBefore(latestStartLocalTime.plusMinutes(1)))
            {
                startTimes.add(earliestStartLocalTime.toLocalTime().toString());
                earliestStartLocalTime = earliestStartLocalTime.plusMinutes(15);
            }

            //setting items in dropdown start time combo box
            starttime_cbox.setItems(startTimes);


            //populating endtime_cbox
            //appointment time range is 8am to 10pm EST
            //setting timezones to EST
            ObservableList<String> endTimes = FXCollections.observableArrayList();

            LocalDateTime earliestEndEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 15));
            earliestEndEST = Tiempo.attachESTTimeZone(earliestEndEST);

            // setting variable to local time equivalent
            LocalDateTime earliestEndLocalTime = Tiempo.convertESTToLocalTimeZone(earliestEndEST);

            LocalDateTime latestEndEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));
            latestEndEST = Tiempo.attachESTTimeZone(latestEndEST);

            // setting variable to local time equivalent
            LocalDateTime latestEndLocalTime = Tiempo.convertESTToLocalTimeZone(latestEndEST);

            //adding end times to list
            while (earliestEndLocalTime.isBefore(latestEndLocalTime.plusMinutes(1)))
            {
                endTimes.add(earliestEndLocalTime.toLocalTime().toString());
                earliestEndLocalTime = earliestEndLocalTime.plusMinutes(15);
            }

            //setting items endtime_cbox combobox
            endtime_cbox.setItems(endTimes);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
