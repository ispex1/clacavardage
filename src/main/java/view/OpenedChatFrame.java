package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.User;
import controller.SessionController;

import java.io.IOException;

public class OpenedChatFrame extends AnchorPane {

    @FXML
    private Label labelPseudo;
    @FXML
    private MainFrame parentController;

    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        //labelPseudo.setText(MainFrame.chatter.getPseudo());
    }

    public void hideChatPane() {
        parentController.hideChatPane();
    }

}
