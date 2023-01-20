package view;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginFrame {
    @FXML
    private TextField textFieldPseudo;
    @FXML
    private Text textPseudoNotValid;
    private static final AtomicBoolean hasRunAtom = new AtomicBoolean();

    public void initialize(){
        System.out.println("TEST");
        if (!hasRunAtom.getAndSet(true)) {
            DatabaseManager.initialize();
            SessionController.initialize();
            UserController.initialize();
        }

    }

    public void connectClick(ActionEvent event) throws IOException {
        SceneController.tryConnect(event, textFieldPseudo, textPseudoNotValid);
    }
}


