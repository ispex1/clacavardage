package view;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

import static controller.UserController.myUser;
import static view.SceneController.pseudoValid;

public class ParametersFrame {

    @FXML
    private TextField textFieldPseudo;
    @FXML
    private Text textPseudoNotValid;

    public void initialize() {
        textFieldPseudo.setPromptText(myUser.getPseudo());
    }

    public void changePseudoClick(ActionEvent event) throws IOException {
        pseudoValid(event, textFieldPseudo, textPseudoNotValid);
    }

    public void disconnectClick(ActionEvent event) throws IOException {
        SceneController.switchToTestScene(event.getSource());
        //TODO: remove the commentaries when the tests will be over
        /*
        UserController.sendDisconnect();
        SceneController.switchToLoginScene(event.getSource());
        */
    }


}
