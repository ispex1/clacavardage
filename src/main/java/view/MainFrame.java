package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static controller.UserController.*;
import static view.SceneController.*;

public class MainFrame{
    @FXML
    private Label myPseudo;
    @FXML
    private Label myID;

    public void initialize() {
       myPseudo.setText(myUser.getPseudo());
       myID.setText("ID : " + myUser.getID());
    }

    public void testClick(MouseEvent event) throws IOException {
        switchToParametersScene(event.getSource());
    }

}
