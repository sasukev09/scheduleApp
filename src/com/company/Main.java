package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import Helper.DBConnection;
import java.io.IOException;

//   >>------------------------------>   Author: Steven Bennett   <------------------------------<<

/**
 * This is the Main class of the app.
 * The application is designed to enable the user to add, modify, and delete customer appointments for contacts.
 */
public class Main extends Application {

    /**
     * This method loads the Login screen.
     *
     * @param primaryStage The primaryStage to be set.
     * @throws IOException The exception that is thrown if there's an error.
     */
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        //  >>---------->   TEMP BYPASS TO SKIP LOGIN SCREEN   <----------<<
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Views/MainMenu.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Views/Login.fxml"));


        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Client Scheduler");          //   --------------------   Client Scheduler?  ----------------
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();                      //   center on screen
        primaryStage.show();
    }


    /**
     * This is the main method of the application.
     * This method opens the database connection, launches arguments, then closes database connection.
     *
     * @param args The arguments
     */
    public static void main (String[] args) throws Exception
    {
        DBConnection.openConnection();      //  ------------------   start

        launch(args);

        DBConnection.closeConnection();     //  ------------------   end
    }

}
