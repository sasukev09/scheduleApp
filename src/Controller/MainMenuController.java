package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This method controls the Main Menu screen of the app.
 * The Main Menu enables the user to open the Appointments, Customers, or Reports screen as well as exit the application.
 */
public class MainMenuController implements Initializable {

    /**
     * Main Menu label (title)
     */
    @FXML
    private Label mainMenuTitle;

    /**
     * Button to open Appointments screen
     */
    @FXML
    private Button appointments_button;

    /**
     * Button to open Customers screen
     */
    @FXML
    private Button customers_button;

    /**
     * Button to open Reports Screen
     */
    @FXML
    private Button reports_button;

    /**
     * Button to exit app
     */
    @FXML
    private Button exit_button;

    Stage stage;
    Parent scene;

    /**
     * This method opens the Appointments screen.
     * @param event Executes when Appointments button is pressed
     */
    @FXML
    void onActionAppointmentButton(ActionEvent event) throws IOException {
        System.out.println("Appointments button pressed");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method opens the Customers screen.
     * @param event Executes when the Customers button is pressed.
     */
    @FXML
    void onActionCustomersButton(ActionEvent event) throws IOException {
        System.out.println("Customers button pressed");

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method opens the Reports screen.
     * @param event Executes when Reports button is pressed.
     */
    @FXML
    void onActionReportsButton(ActionEvent event) throws IOException {
        System.out.println("Reports button pressed");
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Views/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * This method exits the application.
     * @param event Executes when the Exit button is pressed.
     */
    @FXML
    void onActionExitButton(ActionEvent event)
    {
        System.out.println("Exit Button pressed");
        System.exit(0);
    }

    /**
     * This method initializes the Main Menu screen.
     * @param url the location
     * @param resourceBundle the resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

}
