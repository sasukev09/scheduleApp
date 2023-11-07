package Models;


import DAO.DAOAppointments;
import DAO.DAOCountries;
import DAO.DAODivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class manages the Customer data.
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String customerAddress;
    private int divisionID;
    private String divisionName;
    private String customerCountry;
    private String customerPostalCode;
    private String customerPhone;

    /**
     * Constructor for the Customer object.
     * @param customerID ID of customer
     * @param customerName Name of customer
     * @param customerAddress Address of customer
     * @param divisionID Customer 1st Division ID
     * @param customerPostalCode Postal code of customer
     * @param customerPhone Phone number of customer
     */
    public Customer(int customerID, String customerName, String customerAddress, int divisionID, String customerPostalCode, String customerPhone)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.divisionID = divisionID;
        this.divisionName = getDivisionName();
        this.customerCountry = getCountryName(divisionID);
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
    }

    /**
     * This method gets ID number of a Customer
     * @return Returns the customer ID number.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * This method gets the customer name of a Customer
     * @return Returns the name of customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * This method gets customer address of a Customer
     * @return Returns address of customer.
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * This method gets country name of a Customer
     * @return Returns name of customer country.
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * This method gets customer postal code of a Customer
     * @return Returns postal code of customer.
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * This method gets customer phone number of a Customer
     * @return Returns phone number of customer.
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * This method gets the division ID number of a Customer
     * @return Returns ID number of division.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * This method gets country name of Customer object associated with specified division ID number.
     * @param divisionID division ID number
     * @return Returns the name of customer country.
     */
    private String getCountryName(int divisionID)
    {
        String countryName = "";

        try
        {
            Division d = DAODivisions.getDivision(divisionID);
            int countryID = d.getCountryID();
            Country c = DAOCountries.getCountry(countryID);
            countryName = c.getCountryName();
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return countryName;
    }


    /**
     * This method gets division name of Customer object.
     * @return Returns name of division.
     */
    public String getDivisionName()
    {
        String divisionName = "";

        try
        {
            Division d = DAODivisions.getDivision(divisionID);
            divisionName = d.getDivisionName();
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return divisionName;
    }

    //methods for customer screen and appointment deletion
    /**
     * This method verifies if a customer has any appointments scheduled.
     * A list is created and populated with all appointments in the database.
     * Each Appointment is evaluated if it has a matching customer ID number as this Customer object.
     *
     * @return Returns true if customer ID number is found, false if customer ID is not found in appointments table.
     */
    public boolean hasAppointments()
    {
        ObservableList<Appointment> allAppointments = DAOAppointments.getAllAppointments();
        for (Appointment has : allAppointments)
        {
            if (this.getCustomerID() == has.getCustomerID())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This method compiles a list of all appointments with this customer.
     * Two lists are created. One with all Appointments and one for all appointments associated with this Customer object's customer ID number.
     * @return Returns list of Appointment objects where customer ID matches this customer ID.
     */
    public ObservableList<Appointment> getCustomerAppointmentList()
    {
        ObservableList<Appointment> allAppointments = DAOAppointments.getAllAppointments();
        ObservableList<Appointment> thisCustomersAppointments = FXCollections.observableArrayList();

        for (Appointment customerAppointment : allAppointments)
        {
            if (customerAppointment.getCustomerID() == this.customerID)
            {
                thisCustomersAppointments.add(customerAppointment);
            }
        }
        return thisCustomersAppointments;
    }
}
