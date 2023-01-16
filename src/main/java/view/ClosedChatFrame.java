package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.User;
import controller.SessionController;

import java.io.IOException;

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
        System.out.println("hide chat pane");
        parentController.hideChatPane();
        System.out.println("test");
    }

    public void openChatSession(){
        //TODO: openSession with the SessionController
         System.out.println("Opening chat session with " + MainFrame.chatter.getPseudo());
        /*
        try{

            //MainFrame.updateChatPane();
        } catch (IOException e) {
            e.printStackTrace();
        }

     */
    }
}
