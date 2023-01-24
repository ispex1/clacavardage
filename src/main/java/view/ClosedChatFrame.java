package view;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;

/**
 * Controller of the ClosedChatFrame FXML
 */
public class ClosedChatFrame extends AnchorPane {
    @FXML
    private Label labelPseudo; // Label of the message of the closed chat
    @FXML
    private MainFrame parentController; // Controller of the MainFrame of the application

    // Sound of the cat
    AudioClip audioClip =
            new AudioClip(Objects.requireNonNull(getClass().getResource("/sound.mp3")).toExternalForm());


    /**
     * Initialize the labelPseudo of the closed chat
     * It is called when the ClosedChatFrame is opened
     */
    public void initialize(){
        labelPseudo.setText("Your are not chatting with " + MainFrame.chatter.getPseudo() + " yet");
    }

    /**
     * It calls the method of the mainFrame to hide the chatPane
     * It is called when the user click on the hide button
     */
    public void hideChatPane() {
        parentController.hidePane();
    }

    /**
     * It calls the method of the mainFrame to open the chat session with the chatter
     * It calls the method of the mainFrame to update the chat pane
     * It is called when the user click on the button "Open chat"
     */
    public void openChatSession() {
         parentController.openChatSession();
         try {
             parentController.updateChatPane();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         System.out.println("Opening chat session with " + MainFrame.chatter.getPseudo());
    }

        /**
     * It play the sound of the cat
     * It is called when the user click on the cat gif
     */
    public void easterEgg() {
        audioClip.play();
    }

    /**
     * Setter of the parentController
     * @param parentController , the controller of the MainFrame
     */
    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

}
