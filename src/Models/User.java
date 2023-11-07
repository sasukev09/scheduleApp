package Models;


import DAO.DAOAppointments;
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
     * This method gets the user ID of a User
     *
     * @return Returns the ID number of a User
     */
    public int getUserID() {
        return userID;
    }

    /**
     * This method sets user ID of a User
     *
     * @param userID ID of user
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * This method gets the username of a User
     *
     * @return Returns username.
     */
    public String getUserName() {
        return userName;
    }


    /**
     * This method gets a list of all appointments associated with a User
     * Two list are created. The first list consists of all appointments in the database.
     * It is iterated through and all appointments associated with this User is added to a list which is eventually returned.
     *
     * @return Returns a list of Appointment objects that match this User's userID.
     */
    public ObservableList<Appointment> getUserAppointmentList()
    {
        ObservableList<Appointment> allAppointments = DAOAppointments.getAllAppointments();
        ObservableList<Appointment> thisUsersAppointments = FXCollections.observableArrayList();

        for (Appointment userAppointment : allAppointments)
        {
            if (userAppointment.getUserID() == this.userID)
            {
                thisUsersAppointments.add(userAppointment);
            }
        }
        return thisUsersAppointments;

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

        for (Appointment soonAppointment : thisUserAppointments)
        {
            if (soonAppointment.getAppointmentStart().isAfter(LocalDateTime.now()) && soonAppointment.getAppointmentStart().isBefore(LocalDateTime.now().plusMinutes(15)))
            {
                return soonAppointment;
            }
        }
        return null;
    }
}
