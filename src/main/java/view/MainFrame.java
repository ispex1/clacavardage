package view;

import controller.SessionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import model.User;
import controller.UserController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static controller.UserController.*;
import static view.SceneController.*;

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
    private Pane closedPane;

    @FXML
    public ClosedChatFrame closedChatController;

    public void initialize() {
        myPseudo.setText(getMyUser().getPseudo());
        myIP.setText("IP : " + getMyUser().getIP());
        updateUsersList();
    }

    public void updateUsersList() {
        UsersList.getItems().clear();
        for (User user : getListOnline()) {
            UsersList.getItems().add(user.getPseudo());
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
                    //updatePseudo(pseudo);
                    //updateChatPane();
                    initializeChatPane();
                }
            }
        }
    }

    public void initializeChatPane() throws IOException {
        FXMLLoader close = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/closedChatFrame.fxml")));
        closedPane = close.load();
        mainPane.getChildren().setAll(closedPane);
        closedPane.setLayoutX(407);
        closedPane.setLayoutY(142);
        closedChatController = close.getController();
        closedChatController.setParentController(this);
    }

    public void updateChatPane() throws IOException {
        String fxmlPath;

        if (SessionController.isSessionWith(chatter)) fxmlPath = "/fxml/openedChatFrame.fxml";
        else fxmlPath = "/fxml/closedChatFrame.fxml";

        if (chatPane != null) mainPane.getChildren().remove(chatPane);

        chatPane =  FXMLLoader.load(Objects.requireNonNull(MainFrame.class.getResource(fxmlPath)));
        chatPane.setLayoutX(407);
        chatPane.setLayoutY(142);
        chatPane.setId("closedChatPane");

        mainPane.getChildren().add(chatPane);
    }

    public void hideChatPane() {
        mainPane.getChildren().remove("closedChatFrame");
        //mainPane.getChildren().remove(chatPane);
        System.out.println("ChatPane removed");
        chatter = null;
    }

    public void openChatSession(){
        //TODO: openSession with the SessionController

        try{
            updateChatPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}