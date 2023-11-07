package Models;

import DAO.DAOContacts;
import DAO.DAOCustomers;
import DAO.DAOUsers;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

/**
 * This class manages Appointment data.
 */
public class Appointment {

    /**
     * The unique ID number of the Appointment object.
     */
    private int appointmentID;

    /**
     * The title of the Appointment object.
     */
    private String appointmentTitle;

    /**
     * The description of the Appointment object.
     */
    private String appointmentDescription;

    /**
     * The location of the Appointment object.
     */
    private String appointmentLocation;

    /**
     * The type of the Appointment object.
     */
    private String appointmentType;

    /**
     * The start date/time of the Appointment object.
     */
    private LocalDateTime appointmentStart;

    /**
     * The end date/time of the Appointment object.
     */
    private LocalDateTime appointmentEnd;

    /**
     * The customer ID of the Appointment object.
     */
    private int customerID;

    /**
     * The user ID of the Appointment object.
     */
    private int userID;

    /**
     * The contact ID of the Appointment object.
     */
    private int contactID;

    /**
     * The name of the customer fetched by getCustomerName().
     * This value is assigned in the constructor.
     */
    private String customerName;

    /**
     * The name of the user fetched by getUserName().
     * This value is assigned in the constructor.
     */
    private String userName;

    /**
     * The name of the contact fetched by getContactName().
     * This value is assigned in the constructor.
     */
    private String contactName;

    /**
     * This is the contructor for Appointment objects.
     *
     * @param appointmentID Appointment ID number
     * @param appointmentTitle Title of appointment
     * @param appointmentDescription Description of appointment
     * @param appointmentLocation Location of appointment
     * @param appointmentType Type of appointment
     * @param appointmentStart Start time of appointment
     * @param appointmentEnd End time of appointment
     * @param customerID Customer ID number
     * @param userID User ID number
     * @param contactID Contact ID number
     */
    public Appointment(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentType, LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int customerID, int userID, int contactID)
    {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;

        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;

        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;

        this.customerName = getCustomerName(customerID);
        this.userName = getUserName(userID);
        this.contactName = getContactName(contactID);
    }

    /**
     * This method gets the appointment ID number of the Appointment object.
     * @return Returns appointmentID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * This method gets the appointment title of the Appointment object.
     * @return Returns appointmentTitle.
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * This method gets the appointment description of the Appointment object.
     * @return Returns appointmentDescription.
     */
    public String getAppointmentDescription() {

        return appointmentDescription;
    }

    /**
     * This method gets the appointment location of the Appointment object.
     * @return Returns appointmentLocation.
     */
    public String getAppointmentLocation() {

        return appointmentLocation;
    }

    /**
     * This method gets the appointment type of the Appointment object.
     * @return Returns appointmentType.
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * This method gets the appointment start date/time of the Appointment object.
     * @return Returns appointmentStart.
     */
    public LocalDateTime getAppointmentStart() {

        return appointmentStart;
    }

    /**
     * This method gets the appointment end date/time of the Appointment object.
     * @return Returns appointmentEnd.
     */
    public LocalDateTime getAppointmentEnd() {

        return appointmentEnd;
    }

    /**
     * This method gets the customer ID of the Appointment object.
     * @return Returns customer ID.
     */
    public int getCustomerID() {

        return customerID;
    }

    /**
     * This method gets the user ID of the Appointment object.
     * @return Returns userID.
     */
    public int getUserID() {

        return userID;
    }

    /**
     * This method gets the contact ID of the Appointment object.
     * @return Returns contactID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * This method gets name of customer assigned to customerID.
     * @param customerID ID number of customer.
     * @return String value name of customer.
     */
    public String getCustomerName(int customerID)
    {
        ObservableList<Customer> allCustomers = DAOCustomers.getAllCustomers();
        String customerName = "";

        try
        {
            for (Customer assignedCustomer : allCustomers)
            {
                if (assignedCustomer.getCustomerID() == customerID)
                {
                    customerName = assignedCustomer.getCustomerName();
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return customerName;
    }

    /**
     * This method gets name of user assigned to userID.
     * @param userID ID number of user.
     * @return Returns String value name of user.
     */
    public String getUserName(int userID)
    {
        ObservableList<User> allUsers = DAOUsers.getAllUsers();
        String user_Name = "";

        try
        {
            for (User assignedUser : allUsers)
            {
                if (assignedUser.getUserID() == userID)
                {
                    user_Name = assignedUser.getUserName();
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return user_Name;
    }

    /**
     * This method gets name of contact assigned to contactID.
     * @param contactID ID number of contact.
     * @return Returns String value of contact.
     */
    public String getContactName(int contactID)
    {
        ObservableList<Contact> allContacts = DAOContacts.getAllContacts();
        String contact_Name = "";

        for (Contact assignedContact : allContacts)
        {
            if (assignedContact.getContactID() == contactID)
            {
                contact_Name = assignedContact.getContactName();
            }
        }

        return contact_Name;
    }
}
