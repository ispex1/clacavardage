package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.media.AudioClip;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import model.User;
import controller.SessionController;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static com.sun.javafx.reflect.ConstructorUtil.getConstructor;
import static view.MainFrame.chatter;

public class ClosedChatFrame extends AnchorPane {

    @FXML
    private Label labelPseudo;
    @FXML
    private MainFrame parentController;

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

    public void easterEgg(MouseEvent mouseEvent) {
        //print the path of the chaaris.wav file in the resource folder
    //    AudioClip audioClip = new AudioClip(getClass().getResource("resources/chaaris.wav").toString());
    //    audioClip.play(100);
        System.out.println("Playing sound");
    }
}
