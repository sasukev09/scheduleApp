package Models;


import DAO.DBAppointments;
import DAO.DBCountries;
import DAO.DBDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class manages Customer data.
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
     * Constructor for Customer class
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
     * This method gets ID number of Customer object.
     * @return Returns the customer ID number.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * This method gets the customer name of Customer object.
     * @return Returns the name of customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * This method gets customer address of Customer object.
     * @return Returns address of customer.
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * This method gets country name of Customer object.
     * @return Returns name of customer country.
     */
    public String getCustomerCountry() {
        return customerCountry;
    }

    /**
     * This method gets customer postal code of Customer object.
     * @return Returns postal code of customer.
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * This method gets customer phone number of Customer object.
     * @return Returns phone number of customer.
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * This method gets the division ID number of Customer object.
     * @return Returns ID number of division.
     */
    public int getDivisionID() {
        return divisionID;
    }

//    /**
//     * This method sets value of customerID of Customer object.
//     * @param customerID ID number of customer
//     */
//    public void setCustomerID(int customerID) {
//        this.customerID = customerID;
//    }
//
//    /**
//     * sets customerName
//     * @param customerName name of customer
//     */
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }
//
//    /**
//     * sets customer address
//     * @param customerAddress address of customer
//     */
//    public void setCustomerAddress(String customerAddress) {
//        this.customerAddress = customerAddress;
//    }
//
//    /**
//     * sets division ID number
//     * @param divisionID ID number of division
//     */
//    public void setDivisionID(int divisionID) {
//        this.divisionID = divisionID;
//    }
//
//    /**
//     * sets customer country name
//     * @param customerCountry name of customer country
//     */
//    public void setCustomerCountry(String customerCountry) {
//        this.customerCountry = customerCountry;
//    }
//
//    /**
//     * sets customer postal code
//     * @param customerPostalCode postal code of customer
//     */
//    public void setCustomerPostalCode(String customerPostalCode) {
//        this.customerPostalCode = customerPostalCode;
//    }
//
//    /**
//     * sets customer phone number
//     * @param customerPhone phone number of customer
//     */
//    public void setCustomerPhone(String customerPhone) {
//        this.customerPhone = customerPhone;
//    }

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
            Division d = DBDivisions.getDivision(divisionID);
            int countryID = d.getCountryID();
            Country c = DBCountries.getCountry(countryID);
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
            Division d = DBDivisions.getDivision(divisionID);
            divisionName = d.getDivisionName();
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return divisionName;
    }

    //   ------------------------------------    FOR DELETE APPOINTMENT on CUSTOMER SCREEN   ---------------------------------
    /**
     * This method verifies if customer has any appointments scheduled.
     * A list is created and populated with all appointments in the database.
     * Each Appointment is evaluated if it has a matching customer ID number as this Customer object.
     *
     * @return Returns true if customer ID number is found, false if customer ID is not found in appointments table.
     */
    public boolean hasAppointments()
    {
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        for (Appointment a : allAppointments)
        {
            if (this.getCustomerID() == a.getCustomerID())
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
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<Appointment> thisCustomersAppointments = FXCollections.observableArrayList();

        for (Appointment a : allAppointments)
        {
            if (a.getCustomerID() == this.customerID)
            {
                thisCustomersAppointments.add(a);
            }
        }
        return thisCustomersAppointments;
    }


}
