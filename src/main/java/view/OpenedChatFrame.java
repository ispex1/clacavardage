package view;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Message;

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
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxChat;

    private boolean searchMode = false;

    private ArrayList<Message> listDisplayed = new ArrayList<>();
    @FXML
    public static ObservableList<Message> observableHistory;


    public void setParentController(MainFrame parentController) {
        this.parentController = parentController;
    }

    public void initialize(){
        fieldMessage.setPromptText("Send your message to @" + chatter.getPseudo());
        setHistory();
        initObservableHistory();
        SessionController.getSessionWithUser(chatter).setDisplay(true);
        vboxChat.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
        updateChat();
    }

    public void initObservableHistory(){
        observableHistory = FXCollections.observableArrayList(DatabaseManager.getHistory(chatter.getIP()));
        observableHistory.addListener((ListChangeListener<Message>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Message m : c.getAddedSubList()) {
                        receiveMessage(m);
                    }
                }
            }
        });
    }

    public void setHistory() {
        listDisplayed = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
    }

    public void updateChat(){
        vboxChat.getChildren().clear();
        for(Message message : listDisplayed) {
            if (message.getSender().equals(UserController.getMyUser())) {
                addMessageToChat(message, true);
            } else {
                addMessageToChat(message, false);
            }
        }
    }

    public void addMessageToChat(Message message, Boolean sender){
        Label label = new Label(message.toString());
        if(sender){
            label.getStyleClass().add("labelMessageSender");
        } else {
            label.getStyleClass().add("labelMessageReceiver");
        }
        vboxChat.getChildren().add(label);
        scrollPane.setVvalue(1.0);
    }

    public void sendMessage(){
        String message = fieldMessage.getText().trim();
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

    public void receiveMessage(Message message){
        if(!searchMode) addMessageToChat(message, false);
    }

    public void hideChatPane() {
        parentController.hideChatPane();
        SessionController.getSessionWithUser(chatter).setDisplay(false);
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
            listDisplayed = findListOfMessage(chatter.getIP(), search);
            labelTest.setText("List of message containing " + search + " generated");
            updateChat();
        }
    }

    public void cancelSearch(){
        searchMode = false;
        imgCross.setVisible(false);
        btnCross.setDisable(true);
        fieldSearch.clear();
        listDisplayed = getHistory(chatter.getIP());
        labelTest.setText("History of " + chatter.getPseudo() + " generated");
        updateChat();
    }

}
