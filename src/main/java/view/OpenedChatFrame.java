package view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Message;
import model.User;
import network.TCPSession;

import java.io.IOException;
import java.util.ArrayList;


import static database.DatabaseManager.findListOfMessage;
import static database.DatabaseManager.getHistory;
import static view.MainFrame.chatter;

public class OpenedChatFrame extends AnchorPane {
    @FXML
    public MainFrame parentController;
    @FXML
    private TextField fieldSearch;
    @FXML
    private ImageView imgCross;
    @FXML
    private Button btnCross;
    @FXML
    private Label labelTest;
    @FXML
    private TextField fieldMessage;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxChat;

    private int messageCount = 0;
    private boolean searchMode = false;

    private ArrayList<Message> listDisplayed = new ArrayList<>();
    @FXML
    public ObservableList<Message> observableHistory;
    public static TCPSession session;

    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }


    public void initialize(){
        session = SessionController.getSessionWithAdress(chatter.getIP());
        session.setOpenedFrame(this);
        session.setOpenDisplay(true);
        //print session information
        System.out.println("Session with " + chatter.getPseudo() + " is open");

        fieldMessage.setPromptText("Send your message to @" + chatter.getPseudo());
        setHistory();
        vboxChat.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
        updateChat();
        fieldMessage.requestFocus();
    }
    public void setHistory() {
        listDisplayed = getHistory(chatter.getIP());
        labelTest.setText("History with " + chatter.getPseudo() + " displayed");
    }

    public TCPSession getSession(){
        return session;
    }

    public void updateChat(){
        vboxChat.getChildren().clear();
        for(Message message : listDisplayed) {
            //messageCount++;
            addMessageToChat(message, message.getSender().equals(UserController.getMyUser()));
        }
        //i want to have my cursor on the text field after the update
        fieldMessage.requestFocus();
    }

    public void addMessageToChat(Message message, Boolean sender){
        Label msg = new Label(message.getData().trim());
        Label time = new Label(message.getTime());
        Label user;

        msg.setWrapText(true);

        Group root = new Group();
        Label label = msg;
        label.setStyle("-fx-padding: 20px; -fx-font-size: 25px;");
        label.setMaxWidth(650);
        root.getChildren().add(label);
        Scene scene = new Scene(root);
        root.applyCss();
        root.layout();

        label.getWidth();

        if (label.getWidth()>=650) msg.setMaxWidth(650);
        else msg.setMaxWidth(USE_COMPUTED_SIZE);

        if(sender){
            user = new Label("You");
            user.setTranslateX(1219);

            msg.setStyle("-fx-background-color: #F88CD7; -fx-background-radius: 30px; -fx-padding: 10 20 10 20; -fx-font-size: 25px;");
            double translate = 1354-label.getWidth()-10;
            msg.setTranslateX(translate);

            if (label.getWidth()<150) time.setTranslateX(1219);
            else time.setTranslateX(translate+20);

            if (label.getWidth()<43) user.setTranslateX(1310);
            else user.setTranslateX(translate+20);

        } else {
            user = new Label(chatter.getPseudo());
            msg.setStyle("-fx-background-color: #ECFB7B; -fx-background-radius: 30px; -fx-padding: 10 20 10 20; -fx-font-size: 25px;");

            msg.setTranslateX(10); time.setTranslateX(20); user.setTranslateX(20);

        }
        vboxChat.getChildren().addAll(user, msg, time);
        scrollPane.setVvalue(1.0);
    }

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

    public void askDeleteConvo(){
        deleteConvo();
    }

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

    public void receiveMessage(Message message){
        if(!searchMode) addMessageToChat(message, false);
    }

    public void hideChatPane() {
        session.setOpenDisplay(false);
        parentController.hidePane();
    }

    public void closeChatSession(){
        parentController.closeChatSession();
    }

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
