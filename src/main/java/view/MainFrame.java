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
import network.UDPListener;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private Pane mainPane;
    @FXML
    private Pane pane;

    @FXML
    public ClosedChatFrame closedChatController;
    @FXML
    public OpenedChatFrame openedChatController;
    @FXML
    public ParametersFrame parametersController;

    //permet de savoir si le chat est ouvert ou non afin de ne pas initializer le chat deux fois (quand changement de pseudo par exemple)
    private static final AtomicBoolean hasRunAtom = new AtomicBoolean();

    public void initialize() {
        myPseudo.setText(getMyUser().getPseudo());
        myIP.setText("IP : " + getMyUser().getIP());
        updateUsersList();
        UserController.udpListener.setFrame(this);

        if (!hasRunAtom.getAndSet(true)) {
            System.out.println("+++++ Session Control initialize +++++");
            SessionController.initialize();
        }
        SessionController.tcpListener.setFrame(this);
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
        if (pane != null) {
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            if(openedChatController != null) openedChatController.getSession().setOpenDisplay(false);
        }

        if(chatter != null) {
            chatter = null;
            UsersList.getSelectionModel().clearSelection();
        }

        FXMLLoader parameter;

        parameter = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/parametersFrame.fxml")));
        pane = parameter.load();
        mainPane.getChildren().add(pane);
        parametersController = parameter.getController();
        parametersController.setParentController(this);
        pane.setLayoutX(656);
        pane.setLayoutY(142);
    }

    public void easterEgg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setGraphic(new ImageView(new Image("/images/chat.png")));

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
            User user = UserController.getUserByPseudo(pseudo);
            if (user != null) {
                if (user != chatter) {
                    chatter = user;
                    updateChatPane();
                }
            }
        }
    }

    public void updateSelection() {
        UsersList.getSelectionModel().select(chatter.getPseudo());
    }

    public void updateChatPane() throws IOException {
        FXMLLoader chat;

        if (pane != null){
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            pane = null;
        }

        if (SessionController.isSessionWith(chatter)) {
            chat = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/openedChatFrame.fxml")));
            pane = chat.load();
            mainPane.getChildren().add(pane);
            openedChatController = chat.getController();
            openedChatController.setParentController(this);
        }
        else {
            chat = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/closedChatFrame.fxml")));
            pane = chat.load();
            mainPane.getChildren().add(pane);
            closedChatController = chat.getController();
            closedChatController.setParentController(this);
        }

        pane.setLayoutX(407);
        pane.setLayoutY(142);
    }

    public void hidePane() {
        if (pane != null) {
            System.out.println("hide");
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            pane = null;
            UsersList.getSelectionModel().clearSelection();
        }
        chatter = null;
    }



    public void openChatSession() throws IOException {
        createSession(chatter);
        updateChatPane();
    }

    public void closeChatSession(){
        closeSession(chatter);
        hidePane();
    }

    public User getChatter() {
        return chatter;
    }

    public boolean isShowing() {
        return pane != null;
    }
}