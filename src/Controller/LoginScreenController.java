package Controller;

import DAO.DAOUsers;
import Models.Appointment;
import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * This method controls the Login Screen of the app.
 * This method determines the system default time zone setting, verifies username and password during login, and creates/updates a login activity file.
 */
public class LoginScreenController implements Initializable {

    /**
     * "Login" label at top of window
     */
    @FXML
    private Label login_label;

    /**
     * Label for Password text field
     */
    @FXML
    private Label password_label;

    /**
     * Label for Username text field
     */
    @FXML
    private Label username_label;

    /**
     * The Cancel button on the Login Screen
     */
    @FXML
    private Button cancel_button;

    /**
     * The Login button on the Login screen
     */
    @FXML
    private Button login_button;

    /**
     * The password text field in the Login Screen
     */
    @FXML
    private PasswordField password_txtfield;

    /**
     * The Username text field in the Login Screen
     */
    @FXML
    private TextField username_txtfield;

    /**
     * Label for "User Location"
     */
    @FXML
    private Label userloc_label;

    /**
     * Label for location per system Zone ID
     */
    @FXML
    private Label location_label;


    Stage stage;
    Parent scene;
    public static User authorizedUser;

    /**
     * This method closes the program.
     * @param event Executes when Cancel button is pressed.
     */
    @FXML
    void onActionCancelButton(ActionEvent event) {
        System.out.println("Cancel Button Pressed");
        System.exit(0);
    }

    /**
     * This method enables the user to submit username and password to login.
     * After gathering user input for username and password, this method verifies if there is data matching those submissions in the database.
     * User login attempts are documented in login_activity.txt.
     *
     * @param event validates login credentials, progresses to either Main Menu or Error Message
     */
    @FXML
    void onActionLoginButton(ActionEvent event)
    {
        try
        {
            System.out.println("Login Button pressed!");

            String user_name = username_txtfield.getText();
            String password = password_txtfield.getText();

            ZoneId timeZone = ZoneId.systemDefault();
            String tz = timeZone.toString();

            String loginFileName = "login_activity.txt";
            FileWriter fwLoginTracker = new FileWriter(loginFileName, true);
            PrintWriter pwLoginTracker = new PrintWriter(fwLoginTracker);

            ResourceBundle rb = ResourceBundle.getBundle("language");

            User newUser = DAOUsers.getUser(user_name, password);
            //testing input
//        System.out.println(user_name + " - " + password);
//        System.out.println("User ID: " + newUser.getUserID());
//        System.out.println("User Name: " + newUser.getUserName());
//        System.out.println("Password: " + newUser.getPassword());

            //if login is invalid
            if (newUser.getUserID() == 0)
            {
                // updating login_activity.txt file
                String loginUpdate = ("Login attempt by the username '" + user_name + "' DENIED at " + Timestamp.valueOf(LocalDateTime.now()) + " " + tz);
                pwLoginTracker.println(loginUpdate);
                pwLoginTracker.close();

                System.out.println(loginUpdate);
                System.out.println();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("error"));
                alert.setContentText(rb.getString("error_message"));
                alert.show();
            }

            //valid login if not denied
            else
            {
                authorizedUser = newUser;
                //updating login_activity.txt
                String loginUpdate = ("+ Username '" + user_name + "' SUCCESSFULLY logged in at " + Timestamp.valueOf(LocalDateTime.now()) + " " + tz);
                pwLoginTracker.println(loginUpdate);
                pwLoginTracker.close();

                System.out.println(loginUpdate);
                System.out.println();


                //checking if there is an appointment within 15 mins
                if (authorizedUser.hasAppointmentSoon() != null)
                {
                    Appointment within15 = authorizedUser.hasAppointmentSoon();

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setContentText("Appointment ID " + within15.getAppointmentID() + " with "
                            + within15.getCustomerName(within15.getCustomerID()) + " scheduled to start at "
                            + within15.getAppointmentStart().toLocalTime() + " on "
                            + within15.getAppointmentStart().toLocalDate() + ".");
                    alert.showAndWait();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMATION");
                    alert.setContentText("No upcoming appointments in the next 15 minutes.");
//                            " You are scheduled for "
//                            + authorizedUser.getUserAppointmentList().size() + " appointment(s) total."
                    alert.showAndWait();
                }

                //continuing to main menu
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * This method initializes the Login screen.
     * If the user's system language setting is French, this method also changes the content of the Login screen to French.
     * The user's system default timezone and displays that with a dynamic label.
     *
     * @param url            the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        boolean testing = false;

        //testing for the translation
//        Locale fr = new Locale("fr", "FR");
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter 'F' for French or any other key for English: ");
//        String setLangToFrench = scanner.nextLine().toLowerCase();
//
//        if (setLangToFrench.equals("f"))
//        {
//            Locale.setDefault(fr);
//            testing = true;
//        }

        //detecting time zone and updates the label that contains the timezone
        ZoneId timeZone = ZoneId.systemDefault();
        userloc_label.setText(timeZone.toString());

        //getting resource bundle and testing
        ResourceBundle rb = ResourceBundle.getBundle("language", Locale.getDefault());
        System.out.println(rb.getString("welcome"));

        //labels from screen get turned into french
        if (Locale.getDefault().getLanguage().equals("fr"))
        {
            System.out.println("Language set to French");
            login_label.setText(rb.getString("login"));
            username_label.setText(rb.getString("username"));
            password_label.setText(rb.getString("password"));
            login_button.setText(rb.getString("login"));
            cancel_button.setText(rb.getString("cancel"));
            userloc_label.setText(rb.getString("user_location"));
            location_label.setText(timeZone.toString());
        }

        if ((Locale.getDefault().getLanguage().equals("fr")) && testing)
        {
            userloc_label.setText("!France (Test)");
        }
    }
}
