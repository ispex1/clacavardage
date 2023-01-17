package view;

import com.sun.tools.javac.Main;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Message;
import model.User;
import controller.SessionController;
import view.MainFrame.*;

import java.io.IOException;
import java.util.ArrayList;

import static database.DatabaseManager.findListOfMessage;
import static database.DatabaseManager.getHistory;
import static view.MainFrame.chatter;

public class OpenedChatFrame extends AnchorPane {
    @FXML
    private MainFrame parentController;
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
    private Button btnSend;
    @FXML
    private VBox vboxChat;

    private boolean searchMode = false;

    private ArrayList<Message> history = new ArrayList<>();


    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        System.out.println("OpenedChatFrame initialized");
        fieldMessage.requestFocus();
        fieldMessage.setPromptText("Send your message to @" + chatter.getPseudo());
        //history = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
        updateChat();
    }

    public void updateChat(){
        //TODO : add the history of the chat
        for(Message message : history) {
            if (message.getSender().equals(UserController.getMyUser())) {
                addMessageToChat(message, true);
            } else {
                addMessageToChat(message, false);
            }
        }
        vboxChat.getChildren().add(new Label("History of " + chatter.getPseudo() + " generated"));
    }

    public void addMessageToChat(Message message, Boolean sender){
        Label label = new Label(message.toString());
        if(sender){
            label.getStyleClass().add("labelMessageSender");
        } else {
            label.getStyleClass().add("labelMessageReceiver");
        }
        vboxChat.getChildren().add(label);
    }

    public void sendMessage(ActionEvent event){
        String message = fieldMessage.getText().trim();
        if(!message.equals("")){
            Message msg = new Message(UserController.getMyUser(), chatter, message);
            //SessionController.sendMessage(msg,chatter);
            history = getHistory(chatter.getIP());
            fieldMessage.clear();
            fieldMessage.requestFocus();
            updateChat();
        }
    }

    public void hideChatPane() {
        parentController.hideChatPane();
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
            labelTest.setText("No search to display, basic history displayed");
        } else {
            searchMode = true;
            imgCross.setVisible(true);
            btnCross.setDisable(false);
            //history = findListOfMessage(chatter.getIP(), search);
            labelTest.setText("List of message containing " + search + " generated");
            updateChat();
        }
    }

    public void cancelSearch(){
        searchMode = false;
        imgCross.setVisible(false);
        btnCross.setDisable(true);
        fieldSearch.clear();
        //history = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
        updateChat();
    }

}
