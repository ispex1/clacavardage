package view;

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

    private boolean searchMode = false;

    private ArrayList<Message> history = new ArrayList<>();


    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        System.out.println("OpenedChatFrame initialized");
        fieldMessage.requestFocus();
        fieldMessage.setPromptText("Send your message to " + chatter.getPseudo());
        //history = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
        updateChat();
    }

    public void updateChat(){
        //TODO : add the history of the chat
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
        fieldSearch.setText("");
        //history = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
        updateChat();
    }

}
