package DAO;


import Helper.JDBC;
import Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


/**
 * This class manages all database actions related with Division data.
 */
public class DAODivisions {

    /**
     * This method returns a list of all Divisions.
     * Using a Prepared statement, this method executes a database query to get all divisions stored in the database.
     * Each tuple retrieved is used to create a Division object, added to a list and then returned.
     *
     * @return Returns a list of Division objects.
     */
    public static ObservableList<Division> getAllDivisions()
    {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");

                Division gottenDivision = new Division(divisionID, divisionName, countryId);
                divisionList.add(gottenDivision);
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionList;
    }


    /**
     * This method is a getter, returns Division object from database that matches division ID number.
     * Using a prepared statement, this method queries the database for division data that matches user division ID number.
     * @param divisionID ID number of division.
     * @return Returns Division object identified by divisionID.
     */
    public static Division getDivision(int divisionID)
    {
        try
        {
            System.out.println("Getting Division...");

            String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
            String divisionName = "";
            int countryID = 0;
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                divisionName = rs.getString("Division");
                countryID = rs.getInt("Country_ID");

            }

            Division gottenDivision = new Division(divisionID, divisionName, countryID);

            return gottenDivision;
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
    }

}

