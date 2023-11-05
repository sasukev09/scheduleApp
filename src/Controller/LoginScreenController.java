package Controller;

import DAO.DBUsers;
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
 * This method controls the Login screen of the app.
 * This method determines the system default time zone setting, verifies username and password during login, and creates/updates a login activity file.
 */
public class LoginScreenController implements Initializable {

    Stage stage;
    Parent scene;
    public static User authorizedUser;          //   -------  static var to use in createdby updatedby cols


    /**
     * "Login" label at top of window
     */
    @FXML
    private Label loginScreenTitle;

    /**
     * Label for Password text field
     */
    @FXML
    private Label passwordLabel;

    /**
     * Label for Username text field
     */
    @FXML
    private Label userNameLabel;

    /**
     * The Cancel button on the Login Screen
     */
    @FXML
    private Button cancelButton;

    /**
     * The Login button on the Login screen
     */
    @FXML
    private Button loginButton;

    /**
     * The password text field in the Login Screen
     */
    @FXML
    private PasswordField passwordField;

    /**
     * The Username text field in the Login Screen
     */
    @FXML
    private TextField userNameField;

    /**
     * Label for "User Location"
     */
    @FXML
    private Label userLocationLabel;

    /**
     * relays user location per system Zone ID
     */
    @FXML
    private Label userLocationText;

    /**
     * This method closes the program.
     * @param event Executes when Cancel button is pressed.
     */
    @FXML
    void onActionCancel(ActionEvent event) {
        System.out.println("Cancel Button Pressed");
        System.exit(0);
    }

    /**
     * This method enables the user to submit username and password to login.
     * After gathering user input for username and password, this method verifies if there is data matching those submissions in the database.
     * User login attempts are documented in login_activity.txt in the root directory.
     *
     * @param event validates login credentials, progresses to either Main Menu or Error Message
     */
    @FXML
    void onActionLogin(ActionEvent event)
    {
        try
        {
            System.out.println("Login Button pressed!\n");                                    // test print

            String user_name = userNameField.getText();
            String password = passwordField.getText();

            ZoneId timeZone = ZoneId.systemDefault();
            String tz = timeZone.toString();

            String loginFileName = "login_activity.txt";
            FileWriter fwLoginTracker = new FileWriter(loginFileName, true);
            PrintWriter pwLoginTracker = new PrintWriter(fwLoginTracker);

            ResourceBundle rb = ResourceBundle.getBundle("language");

            User newUser = DBUsers.getUser(user_name, password);

//        System.out.println(user_name + " - " + password);                           // test user_name and password field input
//        System.out.println("User ID: " + newUser.getUserID());               //
//        System.out.println("User Name: " + newUser.getUserName());           //
//        System.out.println("Password: " + newUser.getPassword());            //

            if (newUser.getUserID() == 0)               // -----------------   If invalid login
            {
                //   >>---------->   update login_activity.txt   <----------<<
                String loginUpdate = ("   >>----->   Login attempt by Username '" + user_name + "' DENIED at " + Timestamp.valueOf(LocalDateTime.now()) + " " + tz);
                pwLoginTracker.println(loginUpdate);
                pwLoginTracker.close();

                System.out.println(loginUpdate);
                System.out.println();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("error"));
                alert.setContentText(rb.getString("error_message"));
                alert.show();
            }

            else                                        //  ----------------   else Valid login
            {
                authorizedUser = newUser;                     //   -------  static var to use in createdby updatedby cols

                //   >>---------->   update login_activity.txt   <----------<<
                String loginUpdate = ("+ Username '" + user_name + "' SUCCESSFULLY logged in at " + Timestamp.valueOf(LocalDateTime.now()) + " " + tz);
                pwLoginTracker.println(loginUpdate);
                pwLoginTracker.close();

                System.out.println(loginUpdate);
                System.out.println();


                //   >>---------->   15 minute appointment check   <----------<<
                if (authorizedUser.hasAppointmentSoon() != null)
                {
                    Appointment in15 = authorizedUser.hasAppointmentSoon();

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText("Appointment ID #" + in15.getAppointmentID() + " with "
                            + in15.getCustomerName(in15.getCustomerID()) + " scheduled to start at "
                            + in15.getAppointmentStart().toLocalTime() + " on "
                            + in15.getAppointmentStart().toLocalDate() + ".");
                    alert.showAndWait();
                }

                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Schedule Update");
                    alert.setContentText("You have no appointments in the next 15 minutes. You are scheduled for "
                            + authorizedUser.getUserAppointmentList().size() + " appointment(s) total.");
                    alert.showAndWait();
                }


                //   >>---------->   proceed to Main Menu   <----------<<
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();                 //  ----------------   Center Screen
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

        //   VVVVVV  -----------------   TEST LOGIN TRANSLATION   -----------------------   VVVVVV
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
        //   ^^^^^^  ------------------   TEST LOGIN TRANSLATION   -----------------------   ^^^^^^


        //   --------------------   Detects timezone and updates User Location Text   -------------
        ZoneId timeZone = ZoneId.systemDefault();
        userLocationText.setText(timeZone.toString());

        //   ----------------   Get Resource bundle and confirm language w "welcome"   -------------
        ResourceBundle rb = ResourceBundle.getBundle("language", Locale.getDefault());
        System.out.println(rb.getString("welcome"));

        //   --------------------   set GUI labels/buttons to french here   --------------------------
        if (Locale.getDefault().getLanguage().equals("fr"))
        {
            System.out.println("Language set to French");
            loginScreenTitle.setText(rb.getString("login"));
            userNameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            cancelButton.setText(rb.getString("cancel"));
            userLocationLabel.setText(rb.getString("user_location"));
            userLocationText.setText(timeZone.toString());
        }

        if ((Locale.getDefault().getLanguage().equals("fr")) && testing)
        {
            userLocationText.setText("!France (Test)");
        }

        //   -------------------------------------------------------------------------------


    }
}
