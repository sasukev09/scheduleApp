package DAO;


import Controller.LoginScreenController;
import Helper.JDBC;
import Models.Appointment;
import Models.ReportByDivision;
import Models.ReportByMonthType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;

/**
 * This class manages all database actions related with Appointment data.
 */
public class DAOAppointments {

    /**
     * This method returns a list of all appointments in database.
     * By using a Prepared statement, this method executes a database query to retrieve all appointments stored in the database.
     * Each tuple retrieved is used to create an Appointment object which is added to a list and finally returned.
     *
     * @return Returns a list of Appointment objects.
     */
    public static ObservableList<Appointment> getAllAppointments()
    {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");

                //Localdatetime variables for the timestamps of appointment creation
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();

                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment newAppointment = new Appointment(apptID, title, description, location, type, start, end, customerID, userID, contactID);

                appointmentsList.add(newAppointment);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentsList;
    }


    /**
     * This method queries the database for number of customers by division.
     * With the use of a prepared statement, this method creates ReportsByDivision object with the data retrieved.
     * An Observable list containing a compilation of ReportsByDivision objects is created and returned.
     * @return Returns a list of ReportsByDivision objects.
     * @throws SQLException In the event of an SQL error.
     */
    public static ObservableList<ReportByDivision> getCustomersByDivision() throws SQLException {
        ObservableList<ReportByDivision> customersByDivision = FXCollections.observableArrayList();

        String sql = "SELECT first_level_divisions.Division, count(*) AS Number_of_Customers FROM customers " +
                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "WHERE  customers.Division_ID = first_level_divisions.Division_ID " +
                "GROUP BY first_level_divisions.Division_ID ORDER BY first_level_divisions.Division";
        try
        {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String division = rs.getString(1);
                int customerCount = rs.getInt(2);

                ReportByDivision ReportDiv = new ReportByDivision(division, customerCount);
                customersByDivision.add(ReportDiv);
            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return customersByDivision;

    }

    /**
     * This method queries the database for appointment data used in the Reports screen.
     * Data is returned and utilized to create new ReportByMonthType objects which are added to a list.
     *
     * @return Returns a list of ReportByMonthType objects.
     */
    public static ObservableList<ReportByMonthType> getReportsByMonthType()
    {
        ObservableList<ReportByMonthType> reportMonthTypeList = FXCollections.observableArrayList();

        String sql = "SELECT MONTHNAME(Start) AS Month_Name, Type, count(*) AS Type_Count, month(Start) AS Month " +
                "FROM appointments GROUP BY Month, Month_Name, Type ORDER BY Month";
        try
        {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultS = ps.executeQuery();

            while (resultS.next())
            {
                String month = resultS.getString(1);
                String type = resultS.getString(2);
                int count = resultS.getInt(3);
                //used for sorting purpose in query
                int monthNum = resultS.getInt(4);

                ReportByMonthType finalReport = new ReportByMonthType(month, type, count);

                reportMonthTypeList.add(finalReport);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return reportMonthTypeList;
    }


    /**
     * This method filters the appointments tableview in the Appointments screen.
     * User input is gathered from combo boxes for time and contact.
     * A series of conditional statements selects the desired data indicated by user selection(s).
     * @param time Time specification - All, Current Week, or Current Month.
     * @param contact Name of contact.
     * @return Returns an observable list of filtered appointments based on user input from drop down boxes.
     * @throws NullPointerException In the event of a null value.
     */
    public static ObservableList<Appointment> getFilteredAppointments(String time, String contact) throws NullPointerException
    {
        ObservableList<Appointment> allAppointments = DAOAppointments.getAllAppointments();
        ObservableList<Appointment> temporaryAppointments = FXCollections.observableArrayList();
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        //Get current month to get current week number of the year
        Month thisMonth = LocalDateTime.now().getMonth();

        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentWeekOfYear = currentDateTime.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        //testing
        //System.out.println(currentWeekOfYear);

        //Get current month start and end
        LocalDateTime startCurrentMonth = LocalDateTime.of(LocalDateTime.now().getYear(), thisMonth, 1, 0, 0);
        LocalDateTime endCurrentMonth = LocalDateTime.of(LocalDate.now().getYear(), thisMonth, thisMonth.maxLength(), 23, 59);

        //If no filters get selected
        if (time == null && contact == null)
        {
            return allAppointments;
        }

        //If only the contact filter box is selected
        else if (time == null && contact != null)
        {
            for (Appointment contactAppointment : allAppointments)
            {
                if (contactAppointment.getContactName(contactAppointment.getContactID()).equals(contact))
                {
                    filteredAppointments.add(contactAppointment);
                }
            }

            return filteredAppointments;
        }

        //If only the week/moth filter is selected
        else if (time != null && contact == null)
        {
            if (time.equals("Current Week"))
            {
                for (Appointment a : allAppointments)
                {
                    if (a.getAppointmentStart().get(ChronoField.ALIGNED_WEEK_OF_YEAR) == currentWeekOfYear)
                    {
                        filteredAppointments.add(a);
                    }
                }

                return filteredAppointments;
            }

            else if (time.equals("Current Month"))
            {
                for (Appointment a : allAppointments)
                {
                    if (a.getAppointmentStart().isAfter(startCurrentMonth) && a.getAppointmentStart().isBefore(endCurrentMonth))
                    {
                        filteredAppointments.add(a);
                    }
                }
                return filteredAppointments;
            }

            else
            {
                return allAppointments;
            }
        }

        //if both filers are selected
        else if (time != null && contact != null)
        {
            temporaryAppointments.clear();

            //adding appointments with contact names to the temporaryList
            for (Appointment appointmentWcontact : allAppointments)
            {
                if (appointmentWcontact.getContactName(appointmentWcontact.getContactID()).equals(contact))
                {
                    temporaryAppointments.add(appointmentWcontact);
                }
            }
            //test
           // System.out.println("temporaryAppointments size = " + temporaryAppointments.size());

            //filter the temporaryAppointments list for current week appointments
            if (time.equals("Current Week"))
            {
                for (Appointment a : temporaryAppointments)
                {
                    if (a.getAppointmentStart().get(ChronoField.ALIGNED_WEEK_OF_YEAR) == currentWeekOfYear)
                    {
                        filteredAppointments.add(a);
                    }
                }

                return filteredAppointments;
            }

            //filter temporaryAppointments list for current month
            else if (time.equals("Current Month"))
            {
                for (Appointment currentMonthAppointment : temporaryAppointments)
                {
                    if (currentMonthAppointment.getAppointmentStart().isAfter(startCurrentMonth) && currentMonthAppointment.getAppointmentStart().isBefore(endCurrentMonth))
                    {
                        filteredAppointments.add(currentMonthAppointment);
                    }
                }
                return filteredAppointments;
            }

        }

        return filteredAppointments;
    }


    /**
     * This method inserts new appointment into appointments table in database.
     * A prepared statement is utilized to insert a new appointment into the database.
     *
     * @param title title of appointment
     * @param description description of appointment
     * @param location location of appointment
     * @param type type of appointment
     * @param start starting time of appointment
     * @param end ending time of appointment
     * @param customer_ID customer ID number
     * @param user_ID user ID number
     * @param contact_ID contact ID number
     *
     * @throws SQLException In the event of an SQL error.
     */
    public static void addNewAppointment(String title, String description, String location,
                                         String type, Timestamp start, Timestamp end, int customer_ID, int user_ID,
                                         int contact_ID) throws SQLException
    {
        //reference to sql
        // Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID
        String sql = "INSERT INTO appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //reference to sql
        // 1 Appointment_ID - NULL
        // 2 Title
        // 3 Description
        // 4 Location
        // 5 Type
        // 6 Start
        // 7 End
        // 8 Create_Date
        // 9 Created_By
        // 10 Last_Update
        // 11 Last_Updated_By
        // 12 Customer_ID
        // 13 User_ID
        // 14 Contact_ID

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, LoginScreenController.authorizedUser.getUserName());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(10, LoginScreenController.authorizedUser.getUserName());
        ps.setInt(11, customer_ID);
        ps.setInt(12, user_ID);
        ps.setInt(13, contact_ID);
    //test
    // System.out.println(sql);
        ps.execute();

    }

    /**
     * This method modifies existing Appointment data in the database.
     * By using a prepared statement, existing appointment data in the database's appointments table is updated with user input.
     *
     * @param title title of appointment
     * @param description description of appointment
     * @param location location of appointment
     * @param type type of appointment
     * @param startTS start date/time of appointment
     * @param endTS end date/time of appointment
     * @param customerID customer ID number
     * @param userID user ID number
     * @param contactID contact ID number
     * @param appointmentID appointment ID number (unique)
     * @throws SQLException In the event of an SQL error.
     */
    public static void modifyAppointment(String title, String description, String location, String type, Timestamp startTS, Timestamp endTS, int customerID, int userID, int contactID, int appointmentID) throws SQLException
    {
        //sql reference
        //Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID

        String sql = ("UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?");

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, startTS);
        ps.setTimestamp(6, endTS);
        ps.setString(7, LoginScreenController.authorizedUser.getUserName());
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.setInt(10, contactID);
        ps.setInt(11, appointmentID);

        //test
        //System.out.println(sql);
        ps.execute();

    }

    /**
     * This method deletes an appointment from the database.
     * By using a prepared statement with the appointment ID number, appointment data is deleted from appointments table in database.
     * @param appointmentID unique ID number of appointment
     * @throws SQLException In the event of an SQL error.
     */
    public static void deleteAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, appointmentID);

        ps.execute();
    }

    /**
     * This method deletes appointment data from the database associated with a specified customer ID number.
     * By using a prepared statement with the customer ID number, appointment data is deleted from appointments table in database.
     * @param customerID unique ID number of customer.
     * @throws SQLException In the event of an SQL error.
     */
    public static void deleteCustomerAppointments(int customerID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, customerID);

        ps.execute();
    }

}
