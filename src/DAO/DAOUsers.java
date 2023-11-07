package DAO;


import Helper.JDBC;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


/**
 * This class manages all database exchanges associated with User data.
 */
public class DAOUsers {

    /**
     * This method returns a list of all users.
     * By using a Prepared statement, this method executes a database query to get all users stored in the database.
     * Each tuple retrieved is used to create a User object which, added and then returned.
     *
     * @return Returns a list of User objects.
     */
    public static ObservableList<User> getAllUsers()
    {
        ObservableList<User> userList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                User x = new User(userId, userName, password);
                userList.add(x);
            }
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return userList;
    }


    /**
     * This method returns User object from users table in database by user ID.
     * By using a prepared statement, this method queries the database for user data that matches username and password.
     * @param username name of user to return
     * @param userPassword password of user to return
     * @return If user data matching username and password is located, that User object is returned. If not found, returns null.
     */
    public static User getUser(String username, String userPassword)
    {
        try
        {
            System.out.println("Validating User...\n");

            int userID = 0;
            String userName = "";
            String passWord = "";
            String sql = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + userPassword + "'";       // WATCH OUT FOR THE SINGLE QUOTES
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                userID = rs.getInt("User_ID");
                userName = rs.getString("User_Name");
                passWord = rs.getString("Password");
            }

            User x = new User(userID, userName, passWord);

            return x;
        }

        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
    }

}

