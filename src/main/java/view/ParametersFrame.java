package view;

import java.io.IOException;

import controller.UserController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * This class is the controller of the login frame of the application.
 * It is the frame that allows the user to change his pseudo or to disconnect.
 * It is display in the main frame of the application.
 */
public class ParametersFrame {
    @FXML
    private MainFrame parentController; // The controller of the parent frame
    @FXML
    private TextField textFieldPseudo; // TextField to enter the pseudo
    @FXML
    private Text textPseudoNotValid; // Text to display if the pseudo is not valid

    /**
     * It set the parent controller of the opened chat frame
     * It is called in the main frame
     * @param parentController , The parent controller of the opened chat frame
     */
    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    /**
     * Method to change the pseudo
     * Its is called when the user click on the button "Change pseudo"
     * @param event , the event that triggered the method
     */
    public void changePseudoClick(ActionEvent event) throws IOException {
        SceneController.changePseudo(event, textFieldPseudo, textPseudoNotValid);
    }

    /**
     * It calls the method of the mainFrame to hide the parameters pane
     * It is called when the user click on the hide button
     */
    public void hideParametersPane() {
        parentController.hidePane();
    }

    /**
     * Method to disconnect from the server
     * Its is called when the user click on the button "Disconnect"
     * @param event , the event that triggered the method
     */
    public void disconnectClick(ActionEvent event) throws IOException {
        SceneController.switchToLoginScene(event.getSource());
        UserController.sendDisconnect();
    }
}
