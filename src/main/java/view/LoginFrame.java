package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;


public class LoginFrame {
    @FXML
    private TextField textFieldPseudo;
    @FXML
    private Text textPseudoNotValid;

    public void connectSubmitClick(ActionEvent event) throws IOException {
        SceneController.pseudoValid(event, textFieldPseudo, textPseudoNotValid);
    }

    //TODO: add the method for the disconnect button

}


