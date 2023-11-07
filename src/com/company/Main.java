package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import Helper.JDBC;
import java.io.IOException;

/**
 * This is the Main class of the app.
 * The application is designed to enable the user to add, modify, and delete customer appointments for contacts.
 */
public class Main extends Application {

    /**
     * This method loads the Login menu.
     *
     * @param primaryStage The primaryStage to be set.
     * @throws IOException The exception that is thrown if there's an error.
     */
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Views/LoginMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    /**
     * This is the main method of the application.
     * This method executes the database connection, launches it, and closes database connection.
     *
     * @param args The arguments
     */
    public static void main (String[] args) throws Exception
    {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }

}
