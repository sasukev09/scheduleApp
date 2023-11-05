package Models;


import DAO.DBAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * This class manages all User objects.
 */
public class User {

    /**
     * The unique user ID number.
     */
    private int userID;

    /**
     * The username.
     */
    private String userName;

    /**
     * The password.
     */
    private String password;

    /**
     * This is the constructor for new User object.
     *
     * @param userId ID of user
     * @param userName Username
     * @param password User's password
     */
    public User(int userId, String userName, String password)
    {
        this.userID = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * This method gets the user ID of the User object.
     *
     * @return Returns the ID number of the User object.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * This method sets user ID of the User object.
     *
     * @param userID ID of user
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * This method gets the username of the User object.
     *
     * @return Returns username.
     */
    public String getUserName() {
        return userName;
    }

    //   ------------------------------------   not used   ----------------------------------
//    /**
//     * This method sets username of the User object.
//     *
//     * @param userName Returns username (used for login)
//     */
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }


    //   ------------------------------------   passwords....private?   ----------------------------------

//    /**
//     * Returns user password
//     *
//     * @return password of user
//     */
//    public String getPassword() {
//        return password;
//    }
//
//    /**
//     * Sets user password
//     *
//     * @param password password of user
//     */
//    public void setPassword(String password) {
//        this.password = password;
//    }


    /**
     * This method gets a list of all appointments associated with this User object.
     * Two list are created. The first list consists of all appointments in the database.
     * It is iterated through and all appointments associated with this User is added to a list which is eventually returned.
     *
     * @return Returns a list of Appointment objects that match this User object's userID.
     */
    public ObservableList<Appointment> getUserAppointmentList()
    {
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<Appointment> thisUserAppointments = FXCollections.observableArrayList();

        for (Appointment a : allAppointments)
        {
            if (a.getUserID() == this.userID)
            {
                thisUserAppointments.add(a);
            }
        }

        return thisUserAppointments;

    }

    /**
     * This method determines if this user has an appointment scheduled to start in the next 15 min from current time.
     * A list of all appointments is created and iterated through with conditional statements to determine if any are scheduled in a 15-minute window from the current time.
     * @return Returns an Appointment object with start time in 15 min or less from current time, null if next Appointment start
     * time is greater than 15 min away
     */
    public Appointment hasAppointmentSoon()
    {
        ObservableList<Appointment> thisUserAppointments = getUserAppointmentList();

        for (Appointment a : thisUserAppointments)
        {
            if (a.getAppointmentStart().isAfter(LocalDateTime.now()) && a.getAppointmentStart().isBefore(LocalDateTime.now().plusMinutes(15)))
            {
                return a;
            }
        }

        return null;
    }

}
