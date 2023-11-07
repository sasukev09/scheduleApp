package DAO;


import Helper.JDBC;
import Models.Customer;
import Models.Division;
import Controller.LoginScreenController;        //   -------  static var authorizedUser to use in Created_by Last_updated_by cols
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class manages all database exchanges associated with Customer data.
 */
public class DBCustomers {

    /**
     * This method returns a list of all customers in database.
     * By using a Prepared statement, this method executes a database query to retrieve all customers stored in the database.
     * Each tuple retrieved is used to create a Customer object which is added to a list and finally returned.
     *
     * @return Returns a list of Customer objects.
     */
    public static ObservableList<Customer> getAllCustomers()
    {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                int divisionID = rs.getInt("Division_ID");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");

                Customer newCustomer = new Customer(customerID, customerName, customerAddress, divisionID, customerPostalCode, customerPhone);
                customerList.add(newCustomer);
            }

        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return customerList;
    }

    /**
     * This method is a getter, returns Customer object from database that matches specified customer ID number.
     * By using a prepared statement, this method queries the database for customer data that matches user specified customer ID number.
     * @param customer_ID ID number of customer
     * @return Returns customer object identified by customer_ID.
     */
    public static Customer getCustomer(int customer_ID)
    {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        Customer newCustomer = null;
        try
        {
            String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customer_ID);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                int divisionID = rs.getInt("Division_ID");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");

                newCustomer = new Customer(customerID, customerName, customerAddress, divisionID, customerPostalCode, customerPhone);

            }

        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return newCustomer;

    }


    /**
     * This method inserts new customer into customers table in the database.
     * A prepared statement is used to insert a new customer into the customers table of the database.
     *
     * @param customerName name of customer
     * @param customerAddress address of customer
     * @param postalCode postal code of customer
     * @param phone phone number of customer
     * @param divisionName name of division
     * @throws SQLException In the event of an SQL error.
     */
    public static void addNewCustomer(String customerName, String customerAddress, String postalCode, String phone, String divisionName) throws SQLException
    {
        ObservableList<Division> divisionList = DBDivisions.getAllDivisions();

        int divisionID = 0;

        for (Division d : divisionList)
        {
            if (d.getDivisionName().equals(divisionName))
            {
                divisionID = d.getDivisionID();
            }
        }

//        String sql = "INSERT INTO customers VALUES (NULL, '" + customerName + "', '" + customerAddress + "', '" + postalCode + "', '" + phone + "', NOW(), 'TEST', NOW(), 'TEST', " + divisionID + ")";
        String sql = "INSERT INTO customers VALUES (NULL, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, LoginScreenController.authorizedUser.getUserName());
        ps.setString(6, LoginScreenController.authorizedUser.getUserName());
        ps.setInt(7, divisionID);

        System.out.println(sql);        //  TEST PRINT

        ps.execute();

    }


    /**
     * This method updates existing customer data in the database.
     * By using a prepared statement, customer data in the customers table is updated.
     *
     * @param customerID customer ID number
     * @param name customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone
     * @param divisionID customer division ID number
     *
     * @throws SQLException In the event of an SQL error.
     */
    public static void modifyCustomer(int customerID, String name, String address, String postalCode, String phone, int divisionID) throws SQLException
    {
        ObservableList<Division> divisionList = DBDivisions.getAllDivisions();

        String sql = ("UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?");

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, LoginScreenController.authorizedUser.getUserName());
        ps.setInt(6, divisionID);
        ps.setInt(7, customerID);

        System.out.println(sql);        //  TEST PRINT

        ps.execute();

    }


    /**
     * This method deletes customer data from the database.
     * By using a prepared statement, customer data specified by customerID is deleted from the customers table of the database.
     * @param customerID ID number of customer
     * @throws SQLException In the event of an SQL error.
     */
    //      ------------------------------------------------------------------
    public static void deleteCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, customerID);

        ps.execute();

    }



}
