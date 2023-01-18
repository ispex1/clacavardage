package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;

import java.io.IOException;

public class ClosedChatFrame extends AnchorPane {

    @FXML
    private Label labelPseudo;
    @FXML
    private MainFrame parentController;
    AudioClip audioClip = new AudioClip(getClass().getResource("/sound.mp3").toExternalForm());

    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        labelPseudo.setText("Your are not chatting with " + MainFrame.chatter.getPseudo() + " yet");
    }

    public void hideChatPane() {
        parentController.hideChatPane();
    }

    public void openChatSession() throws IOException {
         parentController.openChatSession();
         System.out.println("Opening chat session with " + MainFrame.chatter.getPseudo());
    }

    public void easterEgg() {
        audioClip.play();
        System.out.println("Playing sound");
    }
}
