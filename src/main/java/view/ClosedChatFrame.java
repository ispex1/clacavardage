package view;

import com.sun.tools.javac.Main;
import controller.SessionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import network.TCPSession;

import java.io.IOException;

import static view.MainFrame.chatter;

public class ClosedChatFrame extends AnchorPane {

    @FXML
    private Label labelPseudo;
    @FXML
    private MainFrame parentController;
    public static TCPSession session = SessionController.getSessionWithUser(chatter);
    AudioClip audioClip = new AudioClip(getClass().getResource("/sound.mp3").toExternalForm());

    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        labelPseudo.setText("Your are not chatting with " + MainFrame.chatter.getPseudo() + " yet");
        session.setClosedFrame(this);
        session.setClosedDisplay(true);
    }

    public void hideChatPane() {
        parentController.hideChatPane();
        session.setClosedDisplay(false);
    }

    public void openChatSession() throws IOException {
         parentController.openChatSession();
         System.out.println("Opening chat session with " + MainFrame.chatter.getPseudo());
    }

    public void updateChatPane() throws IOException {
        parentController.updateChatPane();
    }

    public void easterEgg() {
        audioClip.play();
        System.out.println("Playing sound");
    }
}
