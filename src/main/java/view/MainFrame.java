package view;

import controller.SessionController;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static controller.SessionController.closeSession;
import static controller.SessionController.createSession;
import static controller.UserController.getListOnline;
import static controller.UserController.getMyUser;
import static view.SceneController.switchToParametersScene;

public class MainFrame {

    protected static User chatter;
    @FXML
    private Label myPseudo;
    @FXML
    private Label myIP;
    @FXML
    private ListView<String> UsersList;
    @FXML
    private ImageView imgCat;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane chatPane;

    @FXML
    public ClosedChatFrame closedChatController;
    @FXML OpenedChatFrame openedChatController;

    public void initialize() {
        myPseudo.setText(getMyUser().getPseudo());
        myIP.setText("IP : " + getMyUser().getIP());
        updateUsersList();
        SessionController.initialize();
    }

    public void updateUsersList() {
        UsersList.getItems().clear();

        for (User user : getListOnline()) {
            if (!user.equals(getMyUser())) {
                UsersList.getItems().add(user.getPseudo());
            }
        }
    }

    public void parametersClick(ActionEvent event) throws IOException {
        switchToParametersScene(event.getSource());
    }

    public void easterEgg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setGraphic(imgCat);

        alert.setTitle("The Clac Jokey");
        alert.setHeaderText(null);

        try {
            alert.setContentText(findAJoke());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Stage joke = (Stage) alert.getDialogPane().getScene().getWindow();
        joke.getIcons().add(new Image("/images/logo_temp.png"));

        alert.showAndWait();
    }

    private String findAJoke() throws Exception {
        File jokeFile = new File("src/main/resources/jokes.txt");
        Scanner scan = new Scanner(jokeFile);

        int random = (int) (Math.random() * 10);
        int i = 0;

        while (scan.hasNextLine() && i < random) {
            scan.nextLine();
            i++;
        }

        return scan.nextLine();
    }

    /**
     * Select a user in the UsersList and open a chat with him
     *
     */
    public void updateChatter() throws IOException {
        String pseudo = UsersList.getSelectionModel().getSelectedItem();
        if (pseudo != null) {
            User user = UserController.findUser(pseudo);
            if (user != null) {
                if (user != chatter) {
                    chatter = user;
                    updateChatPane();
                }
            }
        }
    }

    public void updateChatPane() throws IOException {
        FXMLLoader chat;
        if (chatPane != null) mainPane.getChildren().remove(mainPane.getChildren().size() - 1);

        if (SessionController.isSessionWith(chatter)) {
            chat = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/openedChatFrame.fxml")));
            chatPane = chat.load();
            chatPane.setDisable(false);
            mainPane.getChildren().add(chatPane);
            openedChatController = chat.getController();
            openedChatController.setParentController(this);
        }
        else {
            chat = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/closedChatFrame.fxml")));
            chatPane = chat.load();
            chatPane.setDisable(false);
            mainPane.getChildren().add(chatPane);
            closedChatController = chat.getController();
            closedChatController.setParentController(this);
        }

        chatPane.setLayoutX(407);
        chatPane.setLayoutY(142);
    }

    public void hideChatPane() {
        if (chatPane != null) {
            System.out.println("hide");
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            chatPane = null;
        }
        chatter = null;
    }

    public void openChatSession() throws IOException {
        createSession(chatter);
        updateChatPane();
    }

    public void closeChatSession(){
        closeSession(chatter);
        hideChatPane();
    }

}