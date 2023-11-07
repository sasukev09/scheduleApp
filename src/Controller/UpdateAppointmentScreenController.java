package Controller;


import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomers;
import DAO.DBUsers;
import Models.Appointment;
import Models.Contact;
import Models.Customer;
import Models.User;
import Utility.Tiempo;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class controls the Modify Appointment screen.
 * The Modify Appointment screen enables the user to enter and upload the new customer appointment data to the database.
 */
public class UpdateAppointmentScreenController implements Initializable {
    @FXML
    private Button save_button;

    @FXML
    private Button cancel_button;

    @FXML
    private DatePicker start_datepicker;

    @FXML
    private DatePicker end_datepicker;

    @FXML
    private ComboBox<String> start_cbox;

    @FXML
    private ComboBox<String> end_cbox;

    @FXML
    private ComboBox<String> contact_cbox;

    @FXML
    private ComboBox<String> customer_cbox;

    @FXML
    private ComboBox<String> user_cbox;

    @FXML
    private TextField appointmentID_txtfield;

    @FXML
    private TextField description_txtfield;

    @FXML
    private TextField location_txtfield;

    @FXML
    private TextField title_txtfield;

    @FXML
    private TextField type_txtfield;

    @FXML
    private TextField customer_txtfield;

    @FXML
    private TextField user_txtfield;


    Stage stage;
    Parent scene;
    public static Appointment selectedAppointment;
    /**
     * This method takes the user input from customer_cbox combobox, updates customer_txtfield with name of customer.
     * A list of all customers in the database is created and searched for a Customer object matching the Customer ID number via a for loop.
     * @param actionEvent Executes when the user selects a Customer ID from the dropDownCustomer combobox.
     */
    @FXML
    void onActionCustomerComboBox(ActionEvent actionEvent)
    {
        ObservableList<Customer> allCustomers = DBCustomers.getAllCustomers();

        int customerID = Integer.parseInt(customer_cbox.getSelectionModel().getSelectedItem());
        String customerName = "";

        for (Customer selectedCustomer : allCustomers)
        {
            if (customerID == selectedCustomer.getCustomerID())
            {
                customerName = selectedCustomer.getCustomerName();
            }
        }

        customer_txtfield.setText(customerName);
    }


    /**
     * This method takes the user input from user_cbox combobox, updates user_txtfield with name of user.
     * A list of all users in the database is created and searched for a User object matching the User ID number via a for loop.
     * @param actionEvent Executes when the user selects a User ID from the user_cbox combobox.
     */
    @FXML
    void onActionUserComboBox(ActionEvent actionEvent)
    {
        ObservableList<User> allUsers = DBUsers.getAllUsers();
        int userID = Integer.parseInt(user_cbox.getSelectionModel().getSelectedItem());
        String userName = "";

        for (User u : allUsers)
        {
            if (userID == u.getUserID())
            {
                userName = u.getUserName();
            }
        }
        user_txtfield.setText(userName);
    }

    /**
     * This method modifies an existing customer appointment in the database with user input.
     * After the user inputs valid data into the text fields and comboboxes, that Appointment object is updated in the database after a series of conditional statements.
     * @param event Executes when user presses the Save button.
     * @throws SQLException In the event of an SQL error.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionSaveButton(ActionEvent event) throws SQLException, IOException {
        System.out.println("Save button pressed");

        boolean isNewAppointment = false;

        DateTimeFormatter formatHourMin = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatYearMonthDay = DateTimeFormatter.ofPattern("yyy-MM-dd");

        String title = title_txtfield.getText();
        String description = description_txtfield.getText();
        String location = location_txtfield.getText();
        String contact = contact_cbox.getValue();
        String type = type_txtfield.getText();
        int customerID = Integer.parseInt(customer_cbox.getValue());
        int userID = Integer.parseInt(user_cbox.getValue());

        //getting customer from database
        Customer customer = DBCustomers.getCustomer(customerID);

        //getting contact ID from contact string
        ObservableList<Contact> allContacts = DBContacts.getAllContacts();
        int contactID = 0;

        for (Contact c : allContacts)
        {
            if (c.getContactName().equals(contact))
            {
                contactID = c.getContactID();
            }
        }

        //getting combo boxes values for start and end time String variables
        String startTime = start_cbox.getValue();
        String endTime = end_cbox.getValue();

        //getting date picker values for start and end time date picker variables
        String startDate = start_datepicker.getValue().format(formatYearMonthDay);
        String endDate = end_datepicker.getValue().format(formatYearMonthDay);

        //conerting to timeStamp
        Timestamp startTS = Tiempo.convertStringTimeDate2TimeStamp(startTime, startDate);
        Timestamp endTS = Tiempo.convertStringTimeDate2TimeStamp(endTime, endDate);

        //testing data input
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


        //converting timestamp to local date time
        LocalDateTime userRequestStartDT = startTS.toLocalDateTime();
        LocalDateTime userRequestEndDT = endTS.toLocalDateTime();

        //attaching local time zone to local date time variables
        userRequestStartDT = Tiempo.attachLocalTimeZone(userRequestStartDT);
        userRequestEndDT = Tiempo.attachLocalTimeZone(userRequestEndDT);


        //confirmation that appointment start time is not before present time
        if (startTS.before(Timestamp.valueOf(LocalDateTime.now())))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment cannot be scheduled before current date.");
            alert.showAndWait();
            return;
        }

        //confirmation that end time is not before the starting time
        else if (endTS.before(startTS) || endTS.equals(startTS))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("End time/date must be after start time/date.");
            alert.showAndWait();
            return;
        }

        //error alert mentioning that appointments can only be made within business hours
        else if (!Tiempo.businessHours(userRequestStartDT, userRequestEndDT))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Business hours are 8:00 AM EST to 10:00 PM EST.");
            alert.showAndWait();
            return;
        }

        //error alerts about overlapping appointments
        else if (Tiempo.overlappingTime(isNewAppointment, customer, userRequestStartDT, userRequestEndDT) == 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Requested appointment start time overlaps existing appointment.");
            alert.showAndWait();
            return;
        }
        else if (Tiempo.overlappingTime(isNewAppointment, customer, userRequestStartDT, userRequestEndDT) == 2)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Requested appointment end time overlaps existing appointment.");
            alert.showAndWait();
            return;
        }
        else if (Tiempo.overlappingTime(isNewAppointment, customer, userRequestStartDT, userRequestEndDT) == 3)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment already scheduled between the requested start/end time.");
            alert.showAndWait();
            return;
        }

        //If there are no overlapping appointments
        else if (Tiempo.overlappingTime(isNewAppointment, customer, userRequestStartDT, userRequestEndDT) == 4)
        {
            System.out.println("Appointment added.");

            //adding the new appointment to database
            DBAppointments.modifyAppointment(title, description, location, type, startTS, endTS, customerID, userID, contactID, selectedAppointment.getAppointmentID());

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
     * @param event Executes when the user presses the Cancel button.
     * @throws IOException In the event of an IO error.
     */
    @FXML
    void onActionCancelButton(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }


    /**
     * This method initializes the Add Appointment screen. Here the start_cbox, end_cbox, contact_cbox, customer_cbox, and user_cbox comboboxes are populated.
     *
     * lambda 1 populates Observable list contactNames with String values of contact name
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
            selectedAppointment = AppointmentScreenController.selectedAppointment;

            //populating contact_cbox
            ObservableList<Contact> allContacts = DBContacts.getAllContacts();
            ObservableList<String> contactNames = FXCollections.observableArrayList();

            //lambda 1
            allContacts.forEach(contact -> contactNames.add(contact.getContactName()));

            contact_cbox.setItems(contactNames);
            contact_cbox.setVisibleRowCount(5);


            //populating customer_cbox
            ObservableList<Customer> allCustomers = DBCustomers.getAllCustomers();
            ObservableList<String> customerIDs = FXCollections.observableArrayList();

            //lambda 2
            allCustomers.forEach(customer -> customerIDs.add(String.valueOf(customer.getCustomerID())));

            //setting customer ids inside combo box along with the dropdown row count
            customer_cbox.setItems(customerIDs);
            customer_cbox.setVisibleRowCount(5);


            //populating user_cbox
            ObservableList<User> allUsers = DBUsers.getAllUsers();
            ObservableList<String> userIDs = FXCollections.observableArrayList();

            //lambda 3
            allUsers.forEach(user -> userIDs.add(String.valueOf(user.getUserID())));

            //setting user ids inside combo box along with the dropdown row count
            user_cbox.setItems(userIDs);
            user_cbox.setVisibleRowCount(5);


            //populating start_cbox
            ObservableList<String> startTimes = FXCollections.observableArrayList();

            //early start time
            LocalDateTime earlyStartEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
            //setting timezone to EST
            earlyStartEST = Tiempo.attachESTTimeZone(earlyStartEST);

            //setting variable to local time equivalent
            LocalDateTime earlyStartLocalTime = Tiempo.convertESTToLocalTimeZone(earlyStartEST);

            //late start time
            LocalDateTime lateStartEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 45));
            //setting timezone to EST
            lateStartEST = Tiempo.attachESTTimeZone(lateStartEST);

            //setting variable to local time equivalent
            LocalDateTime latestStartLocalTime = Tiempo.convertESTToLocalTimeZone(lateStartEST);

            //adding all start times to list
            while (earlyStartLocalTime.isBefore(latestStartLocalTime.plusMinutes(1)))
            {
                startTimes.add(earlyStartLocalTime.toLocalTime().toString());
                earlyStartLocalTime = earlyStartLocalTime.plusMinutes(15);
            }

            //setting time items into the start combo box
            start_cbox.setItems(startTimes);


            //populating end_cbox
            ObservableList<String> endTimes = FXCollections.observableArrayList();

            LocalDateTime earlyEndEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 15));
            //setting timezone to EST
            earlyEndEST = Tiempo.attachESTTimeZone(earlyEndEST);
            //setting variable to local time equivalent
            LocalDateTime earliestEndLocalTime = Tiempo.convertESTToLocalTimeZone(earlyEndEST);

            LocalDateTime lateEndEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));
            //setting timezone to EST
            lateEndEST = Tiempo.attachESTTimeZone(lateEndEST);
            //setting variable to local time equivalent
            LocalDateTime latestEndLocalTime = Tiempo.convertESTToLocalTimeZone(lateEndEST);

            //adding end times to list
            while (earliestEndLocalTime.isBefore(latestEndLocalTime.plusMinutes(1)))
            {
                endTimes.add(earliestEndLocalTime.toLocalTime().toString());
                earliestEndLocalTime = earliestEndLocalTime.plusMinutes(15);
            }

            //setting end time items in end combo box
            end_cbox.setItems(endTimes);


            //populating textfields with the selectedAppointment data
            LocalDateTime start = selectedAppointment.getAppointmentStart();
            LocalDateTime end = selectedAppointment.getAppointmentEnd();

            LocalTime startTime = start.toLocalTime();
            LocalTime endTime = end.toLocalTime();
            LocalDate startDate = start.toLocalDate();
            LocalDate endDate = end.toLocalDate();

            appointmentID_txtfield.setText(Integer.toString(selectedAppointment.getAppointmentID()));
            title_txtfield.setText(selectedAppointment.getAppointmentTitle());
            description_txtfield.setText(selectedAppointment.getAppointmentDescription());
            location_txtfield.setText(selectedAppointment.getAppointmentLocation());
            contact_cbox.setValue(selectedAppointment.getContactName(selectedAppointment.getContactID()));
            type_txtfield.setText(selectedAppointment.getAppointmentType());

            start_cbox.setValue(startTime.toString());
            end_cbox.setValue(endTime.toString());
            start_datepicker.setValue(startDate);
            end_datepicker.setValue(endDate);

            customer_cbox.setValue(Integer.toString(selectedAppointment.getCustomerID()));
            user_cbox.setValue(Integer.toString(selectedAppointment.getUserID()));
            customer_txtfield.setText(selectedAppointment.getCustomerName(selectedAppointment.getCustomerID()));
            user_txtfield.setText(selectedAppointment.getUserName(selectedAppointment.getUserID()));

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
