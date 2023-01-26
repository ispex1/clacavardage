package view;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * This class is the controller of the login frame of the application.
 * It is the first frame that the user sees when he launches the application
 * It is the frame where the user can connect to the server
 */
public class LoginFrame {
    @FXML
    private TextField textFieldPseudo; // TextField to enter the pseudo
    @FXML
    private Text textPseudoNotValid; // Text to display if the pseudo is not valid
    private static final AtomicBoolean hasRunAtom = new AtomicBoolean(); // AtomicBoolean to check if the method has already been run

    /**
     * It is called when the LoginFrame is opened
     * If it is the first time, it will :
     * Initialize the DatabaseManager
     * Initialize the SessionController
     * Initialize the UserController
     */
    public void initialize(){
        if (!hasRunAtom.getAndSet(true)) {
            DatabaseManager.initialize();
            SessionController.initialize();
            UserController.initialize();
        }

    }

    /**
     * Method to connect to the server
     * Its is called when the user click on the button "Connect"
     * @param event , the event that triggered the method
     */
    public void connectClick(ActionEvent event) {
        SceneController.tryConnect(event, textFieldPseudo, textPseudoNotValid);
    }
}


