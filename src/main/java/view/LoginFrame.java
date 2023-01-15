package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

import static controller.UserController.*;


public class LoginFrame {
    @FXML
    private TextField textFieldPseudo;
    @FXML
    private Text textPseudoNotValid;

    public void connectClick(ActionEvent event) throws IOException {
        SceneController.pseudoValid(event, textFieldPseudo, textPseudoNotValid);
    }

    //TODO: add the method for the disconnect button

}


