package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import model.User;
import controller.SessionController;

import java.io.IOException;

public class OpenedChatFrame extends Node {

    public OpenedChatFrame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/openedChatFrame.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public OpenedChatFrame(User user) throws IOException {
        System.out.println("OpenedChatFrame with user");
    }

}
