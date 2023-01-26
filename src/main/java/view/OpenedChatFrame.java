package view;

import java.util.ArrayList;

import controller.SessionController;
import controller.UserController;
import model.Message;
import network.TCPSession;
import database.DatabaseManager;

import static database.DatabaseManager.findListOfMessage;
import static database.DatabaseManager.getHistory;
import static view.MainFrame.chatter;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is the controller of the opened chat frame.
 * It is the frame that contains the chat with the selected user.
 * It is displayed when the user clicks on a user in the list of online users and when the chat session is open.
 */
public class OpenedChatFrame extends AnchorPane {
    @FXML
    public MainFrame parentController; // Controller of the main frame
    @FXML
    private TextField fieldSearch; // Field to search a message
    @FXML
    private ImageView imgCross; // Image of the cross to close the search
    @FXML
    private Button btnCross; // Button of the cross to close the search
    @FXML
    private Label labelTest; // Label of the display status
    @FXML
    private TextField fieldMessage; // Field to write a message
    @FXML
    private ScrollPane scrollPane; // ScrollPane of the chat
    @FXML
    private VBox vboxChat; // VBox of the chat
    private boolean searchMode = false; // Boolean to know if the search mode is activated
    private ArrayList<Message> listDisplayed = new ArrayList<>(); // List of the messages displayed in the chat
    public static TCPSession session; // The session with the user with whom we are currently chatting

    /**
     * Initialize the opened chat frame
     * It is called when the OpenedChatFrame is opened
     * It set the history of the chat
     * It set the label of the pseudo of the chatter
     * It set the label of the display status
     */
    public void initialize(){
        session = SessionController.getSessionWithAddress(chatter.getIP());
        assert session != null;
        session.setOpenedFrame(this);
        session.setOpenDisplay(true);

        fieldMessage.setPromptText("Send your message to @" + chatter.getPseudo());
        setHistory();
        vboxChat.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
        updateChat();
        fieldMessage.requestFocus();
    }

    /**
     * Set the history of the chat
     * It is called when the OpenedChatFrame is opened
     * It get the history of the chat in the database
     */
    public void setHistory() {
        listDisplayed = getHistory(chatter.getIP());
        labelTest.setText("History with " + chatter.getPseudo() + " displayed");
    }

    /**
     * It set the parent controller of the opened chat frame
     * It is called in the main frame
     * @param parentController , The parent controller of the opened chat frame
     */
    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    /**
     * It gets the TCP session with the chatter
     * @return The TCP session with the chatter
     */
    public TCPSession getSession(){
        return session;
    }

    /**
     * It updates the chat displayed
     * It is called when the user clicks on the button "Search"
     * It is called when the user clicks on the button "Cross"
     */
    public void updateChat(){
        vboxChat.getChildren().clear();
        for(Message message : listDisplayed) {
            addMessageToChat(message, message.getSender().equals(UserController.getMyUser()));
        }
        fieldMessage.requestFocus();
    }

    /**
     * It adds a message to the chat
     * It is called when the user clicks on the button "Send"
     * It is called when the user receive a message
     * It changes the design of the message displayed depending on the sender
     * It adapts the position of the position depending on his size
     * @param message , The message to add to the chat
     * @param sender , True if the message is sent by the user, false otherwise
     */
    public void addMessageToChat(Message message, Boolean sender){
        Label msg = new Label(message.getData().trim());
        Label time = new Label(message.getTime());
        Label user;

        msg.setWrapText(true);

        Group root = new Group();
        msg.setStyle("-fx-padding: 20px; -fx-font-size: 25px;");
        msg.setMaxWidth(650);
        root.getChildren().add(msg);
        Scene scene = new Scene(root);
        root.applyCss();
        root.layout();

        msg.getWidth();

        if (msg.getWidth()>=650) msg.setMaxWidth(650);
        else msg.setMaxWidth(USE_COMPUTED_SIZE);

        if(sender){
            user = new Label("You");
            user.setTranslateX(1219);

            msg.setStyle("-fx-background-color: #F88CD7; -fx-background-radius: 30px; -fx-padding: 10 20 10 20; -fx-font-size: 25px;");
            double translate = 1354- msg.getWidth()-10;
            msg.setTranslateX(translate);

            if (msg.getWidth()<150) time.setTranslateX(1219);
            else time.setTranslateX(translate+20);

            if (msg.getWidth()<43) user.setTranslateX(1310);
            else user.setTranslateX(translate+20);

        } else {
            user = new Label(chatter.getPseudo());
            msg.setStyle("-fx-background-color: #ECFB7B; -fx-background-radius: 30px; -fx-padding: 10 20 10 20; -fx-font-size: 25px;");

            msg.setTranslateX(10); time.setTranslateX(20); user.setTranslateX(20);

        }
        vboxChat.getChildren().addAll(user, msg, time);
        scrollPane.setVvalue(1.0);
    }

    /**
     * It sends a message to the chatter
     * It is called when the user clicks on the button "Send"
     * It is called when the user presses the key "Enter"
     */
    public void sendMessage(){
        String message = fieldMessage.getText().trim();

        message = message.replace(";", "");
        if(!message.isEmpty()){
            fieldMessage.clear();
            Message msg = new Message(UserController.getMyUser(), chatter, message);
            SessionController.sendMessage(msg,chatter);
            if(!searchMode){
                addMessageToChat(msg, true);
            }
            else{
                listDisplayed.add(msg);
                searchMessage();
            }
        }
    }

    /**
     * It deletes the history of the chat in the database and in the chat displayed.
     * It is called when the user clicks on the button "Delete history".
     * It deletes the history if the user confirms his choice in a pop-up.
     */
    public void deleteConvo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete conversation");
        alert.setGraphic(new ImageView(new Image("/images/chat.png")));
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this conversation ? You won't be able to recover it.");
        Stage confirm = (Stage) alert.getDialogPane().getScene().getWindow();
        confirm.getIcons().add(new Image("/images/logo_temp.png"));

        if (alert.showAndWait().get() == ButtonType.OK){
            DatabaseManager.deletteAllMessages(chatter.getIP());
            listDisplayed.clear();
            updateChat();
        }
        else {
            alert.close();
        }

    }

    /**
     * It is called when the user receive a message
     * It calls the method addMessageToChat to add the message to the chat
     * It precise that the message is not sent by the user
     * @param message , The message received
     */
    public void receiveMessage(Message message){
        if(!searchMode) addMessageToChat(message, false);
    }

    /**
     * It calls the method of the mainFrame to hide the chatPane
     * It is called when the user click on the hide button or when the chat session is closed
     */
    public void hideChatPane() {
        session.setOpenDisplay(false);
        parentController.hidePane();
    }

    /**
     * It calls the method of the mainFrame to close the chat session
     * It is called when the user clicks on the button "Close"
     */
    public void closeChatSession(){
        parentController.closeChatSession();
    }

    /**
     * It search a message in the chat displayed
     * It is called when the user clicks on the button "Search"
     */
    public void searchMessage() {
        String search = fieldSearch.getText().trim();
        if(search.isEmpty()) {
            searchMode = false;
            imgCross.setVisible(false);
            btnCross.setDisable(true);
            labelTest.setText("No search to display, history with " + chatter.getPseudo() +" displayed");
        } else {
            searchMode = true;
            imgCross.setVisible(true);
            btnCross.setDisable(false);
            listDisplayed = findListOfMessage(chatter.getIP(), search);
            labelTest.setText("List of messages containing " + search + " displayed");
            updateChat();
        }
    }

    /**
     * It cancels the research of a message in the chat displayed
     * It is called when the user clicks on the cross button
     */
    public void cancelSearch(){
        searchMode = false;
        imgCross.setVisible(false);
        btnCross.setDisable(true);
        fieldSearch.clear();
        listDisplayed = getHistory(chatter.getIP());
        labelTest.setText("History with " + chatter.getPseudo() + " displayed");
        updateChat();
    }
}
