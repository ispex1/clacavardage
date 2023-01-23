package view;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class ParametersFrame {
    @FXML
    private MainFrame parentController;
    @FXML
    private TextField textFieldPseudo;
    @FXML
    private Text textPseudoNotValid;

    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void changePseudoClick(ActionEvent event) throws IOException {
        SceneController.changePseudo(event, textFieldPseudo, textPseudoNotValid);
    }

    public void hideParametersPane() {
        parentController.hidePane();
    }

    public void disconnectClick(ActionEvent event) throws IOException {
        SceneController.switchToLoginScene(event.getSource());
        UserController.sendDisconnect();
    }
}
