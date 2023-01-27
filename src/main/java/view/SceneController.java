package view;

import java.io.IOException;
import java.util.Objects;

import controller.SessionController;
import controller.UserController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static controller.UserController.*;

/**
 * This class is the controller of the frame switcher of the application.
 * It is the controller that check the uniqueness of the pseudo.
 * It is the controller that switch between the different frames of the application.
 */
public class SceneController extends Application {
    protected static Stage stage; // The stage of the application
    protected static Scene scene; // The scene of the application
    protected static Parent root; // The root of the application

    /**
     * It is called when the application is launched
     * @param args , the argument of the application
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * It is called when the application is launched
     * It will :
     * Initialize the stage
     * Initialize the scene
     * Initialize the root
     * @param stageStart
     */
    public void start(Stage stageStart){
        try {
            stage = stageStart;
            root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/loginFrame.fxml")));

            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();

            stage.getIcons().add(new Image("/images/logo_temp.png"));
            stage.setTitle("Clac Chat - Login");

            setStage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to try to change pseudo
     * It is called when the user click on the button "Change Pseudo" in the parameters frame
     * @param event , the event that triggered the method
     * @param textFieldPseudo , the textField that contains the pseudo
     * @param textPseudoNotValid , the text that display if the pseudo is not valid
     * @throws IOException , if the switch to the main scene failed
     */
    public static void changePseudo(ActionEvent event, TextField textFieldPseudo, Text textPseudoNotValid) throws IOException {
        String pseudo = textFieldPseudo.getText().trim();

        if(getMyUser().getPseudo().equals(pseudo)) {
            switchToMainScene(event.getSource());
            System.out.println("Pseudo not changed");
        }
        else if (pseudo.isEmpty()) textPseudoNotValid.setText("Please enter a pseudo");
        else if (pseudo.length() > 15) textPseudoNotValid.setText("This pseudo is too long");
        else if (!pseudo.matches("[a-zA-Z0-9]+")) textPseudoNotValid.setText("Only letters and numbers");
        else if (UserController.pseudoNotPresent(pseudo)) {
                setMyUser(pseudo);
                UserController.updateMyUser();
                UserController.sendPseudo(pseudo);
                switchToMainScene(event.getSource());
        } else textPseudoNotValid.setText("This pseudo is already taken");
    }

    /**
     * Method to try to connect to the server
     * Its is called when the user click on the button "Connect" in the login frame
     * @param event , the event that triggered the method
     * @param textFieldPseudo , the TextField to enter the pseudo
     * @param textPseudoNotValid , the Text to display if the pseudo is not valid
     */
    public static void tryConnect(ActionEvent event, TextField textFieldPseudo, Text textPseudoNotValid) {
        String pseudo = textFieldPseudo.getText().trim();
        UserController.askUserList();

        if (pseudo.isEmpty()) textPseudoNotValid.setText("Please enter a pseudo");
        else if (pseudo.length() > 15) textPseudoNotValid.setText("This pseudo is too long");
        else if (!pseudo.matches("[a-zA-Z0-9]+")) textPseudoNotValid.setText("Only letters and numbers");
        else if (UserController.pseudoNotPresent(pseudo)) {
                setMyUser(pseudo);
                UserController.addMyUser();
                UserController.sendConnect();
                try {
                    switchToMainScene(event.getSource());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
                getListOnline().clear();
                textPseudoNotValid.setText("This pseudo is already taken");
        }
    }

    /**
     * It will switch to another scene
     * @param eventSource
     */
    public static void switchScene(Object eventSource){
        scene = new Scene(root);

        stage = (Stage) ((Node) eventSource).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        setStage();
    }

    /**
     * It is called everytime the application is showing a new stage
     * It will :
     * Set the stage to the center of the screen
     * Set the stage to be not resizable
     */
    public static void setStage() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            if (getMyUser().getPseudo() != null) {
                UserController.sendDisconnect();
            }
            SessionController.close();
            UserController.close();
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * It will switch to the main scene
     * @param eventSource , the source of the event that triggered the method
     * @throws IOException , if the switch to the main scene failed
     */
    public static void switchToMainScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(MainFrame.class.getResource("/fxml/mainFrame.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Main Page");
    }

    /**
     * It will switch to the login scene
     * @param eventSource , the source of the event that triggered the method
     * @throws IOException , if the switch to the login scene failed
     */
    public static void switchToLoginScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(LoginFrame.class.getResource("/fxml/loginFrame.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Login");
    }
}
