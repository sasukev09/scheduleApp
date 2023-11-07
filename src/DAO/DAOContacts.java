package DAO;


import Helper.JDBC;
import Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class manages all database actions related with Contact data.
 */
public class DAOContacts {

    /**
     * This method returns a list of all contacts in the database.
     * By using a prepared statement, this method executes a database query to retrieve all contacts stored in the database.
     * Each tuple is retrieved and used to create a Contact object which is added to a list and finally returned.
     * @return Returns a list of Contact objects.
     */
    public static ObservableList<Contact> getAllContacts()
    {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        try
        {
            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int iD = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contact newContact = new Contact(iD, name, email);
                allContacts.add(newContact);
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return allContacts;
    }

}
