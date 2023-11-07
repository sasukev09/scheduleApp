package DAO;


import Helper.JDBC;
import Models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


/**
 * This class manages all database actions related with Country data.
 */
public class DAOCountries {

    /**
     * This method returns a list of all countries in database.
     * By using a Prepared statement, this method executes a database query to retrieve all countries stored in the database.
     * Each tuple retrieved is used to create a Country object which is added to a list and finally returned.
     *
     * @return Returns a list of Country objects.
     */
    public static ObservableList<Country> getAllCountries()
    {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                Country c = new Country(countryId, countryName);
                countryList.add(c);
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return countryList;
    }


    /**
     * This method queries the database for country data associated with a country ID number.
     * By using a Prepared statement, this method executes a database query to retrieve country data stored in the database that matches the country ID number.
     * Each tuple retrieved is used to create an Appointment object which is added to a list and finally returned.
     *
     * @param countryID ID of country
     * @return Returns Country object associated with specified country ID number.
     */
    public static Country getCountry(int countryID)
    {
        try
        {
            System.out.println("Getting Country...");

            String sql = "SELECT * FROM countries WHERE Country_ID = " + countryID;
            String countryName = "";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                countryName = rs.getString("Country");
            }

            Country newCountry = new Country(countryID, countryName);
            return newCountry;
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
    }

}
