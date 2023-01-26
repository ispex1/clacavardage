package view;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import model.User;
import controller.SessionController;
import controller.UserController;

import static controller.SessionController.closeSession;
import static controller.SessionController.createSession;
import static controller.UserController.getListOnline;
import static controller.UserController.getMyUser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class is the controller of the main frame of the application.
 * It is the frame that contains the list of online users and the chat with the selected user.
 * It is displayed when the user is connected to the server.
 */
public class MainFrame {
    @FXML
    private Label myPseudo; // Label of the pseudo of the user
    @FXML
    private Label myIP; // Label of the IP of the user
    @FXML
    private ListView<String> UsersList; // List of the users online
    @FXML
    private Pane mainPane; // Pane of the main frame
    protected static User chatter; // The user with whom we are currently chatting
    private Pane pane; // Pane of the opened chat, closed chat or parameters
    public ClosedChatFrame closedChatController; // Controller of the closed chat frame
    public OpenedChatFrame openedChatController; // Controller of the opened chat frame
    public ParametersFrame parametersController; // Controller of the parameters frame


    /**
     * Initialize the main frame
     * It is called when the MainFrame is opened
     * It set the label of the pseudo and the IP of the user
     * It updates the list of the users online
     * It set the mainFrame of the UDP listener
     * It set the mainFrame of the TCP listener
     */
    public void initialize() {
        myPseudo.setText(getMyUser().getPseudo());
        myIP.setText("IP : " + getMyUser().getIP());
        updateUsersList();
        UserController.udpListener.setFrame(this);
        SessionController.tcpListener.setFrame(this);
    }

    /**
     * It updates the list of the users online
     * It is called at the initialization of the frame or everytime the UDP listener receive a message
     */
    public void updateUsersList() {
        UsersList.getItems().clear();
        for (User user : getListOnline()) {
            if (!user.equals(getMyUser())) {
                UsersList.getItems().add(user.getPseudo());
            }
        }
        if (!getListOnline().contains(chatter)) {
            chatter=null;
        }
    }

    /**
     * It opens the parameters pane
     * It is called when the user click on the button "Parameters"
     * It hides the chat pane if there is one displayed
     * It unselect the chatter if there is one selected
     * It opens the parameters pane
     * @throws IOException , if the FXML file of the parameters pane is not found
     */
    public void parametersClick() throws IOException {
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

    /**
     * It opens a pop-up with a joke about cats
     * It is called when the user click on the button "Cat"
     * It call the method findAJoke() to select the joke to display
     */
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

    /**
     * It selects a random joke about cats into the 50 available in the jokes file
     * @return the joke selected
     * @throws Exception , if the jokes file is not found
     */
    private String findAJoke() throws Exception {
        File jokeFile = new File("src/main/resources/jokes.txt");
        Scanner scan = new Scanner(jokeFile);

        int random = (int) (Math.random() * 50);
        int i = 0;

        while (scan.hasNextLine() && i < random) {
            scan.nextLine();
            i++;
        }

        return scan.nextLine();
    }

    /**
     * Update the chatter when the user select a user in the list of the users online
     * It is called when the user click on a user in the UsersList
     * It hides the parameters pane if there is one displayed
     * It opens a chat pane with the selected user
     * @throws IOException , if the FXML file of the chat pane is not found
     */
    public void updateChatter(){

        String pseudo = UsersList.getSelectionModel().getSelectedItem();
        if (pseudo != null) {
            User user = UserController.getUserByPseudo(pseudo);
            if (user != chatter) {
                chatter = user;
            }
        }

        try {
            updateChatPane();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It updates the selection in the UsersList
     * It is called when the chatter selected change his pseudonym
     */
    public void updateSelection() {
        UsersList.getSelectionModel().select(chatter.getPseudo());
    }

    /**
     * It update the chat pane
     * It is called when the chatter selected change
     * It hides the parameters pane if there is one displayed
     * It opens a chat pane with the selected user
     * @throws IOException , if the FXML file of the chat pane is not found
     */
    public void updateChatPane() throws IOException {
        FXMLLoader chat;

        if (pane != null){
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            pane = null;
        }

        if (!getListOnline().contains(chatter)) hidePane();

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

    /**
     * It hides the chat pane if there is one displayed
     * It is called when the chatter selected is not online anymore or
     * it is called when the user click on the button "Hide" or
     * it is called when the user click on the button "Close" or
     * it is called when the user click on the button "Parameters"
     */
    public void hidePane() {
        if (pane != null) {
            mainPane.getChildren().remove(mainPane.getChildren().size() - 1);
            pane = null;
            UsersList.getSelectionModel().clearSelection();
        }
        chatter = null;
    }

    /**
     * It open a chat session with the chatter selected
     * It is called when the user click on the button "Open chat" in the closed chat pane
     */
    public void openChatSession(){
        createSession(chatter);
    }

    /**
     * It closes the chat session with the chatter selected
     * It hide the chat pane
     * It is called when the user click on the button "Close" in the opened chat pane
     */
    public void closeChatSession(){
        closeSession(chatter);
        hidePane();
    }

    /**
     * Getter of the chatter selected
     * @return the chatter selected
     */
    public User getChatter() {
        return chatter;
    }

    /**
     * Tell if the pane is displayed or not
     * @return true if the pane is displayed, false otherwise
     */
    public boolean isShowing() {
        return pane != null;
    }
}