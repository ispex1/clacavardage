package view;

import controller.SessionController;
import controller.UserController;
import database.DatabaseManager;
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
import model.User;
import network.TCPSession;

import java.io.IOException;
import java.util.Objects;

import static controller.UserController.*;

public class SceneController extends Application {
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;

    public static void main(String[] args){
        //TODO: remove this line, just for testing
        //if (getListOnline().isEmpty()) testListOnline();
        launch(args);
    }

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

    public static void changePseudo(ActionEvent event, TextField textFieldPseudo, Text textPseudoNotValid) throws IOException {
        String pseudo = textFieldPseudo.getText().trim();

        if(getMyUser().getPseudo().equals(pseudo)) {
            switchToMainScene(event.getSource());
            System.out.println("Pseudo not changed");
        }
        else if (pseudo.isEmpty()) textPseudoNotValid.setText("Please enter a pseudo");
        else if (pseudo.length() > 15) textPseudoNotValid.setText("This pseudo is too long");
        else if (!pseudo.matches("[a-zA-Z0-9]+")) textPseudoNotValid.setText("Only letters and numbers");
        else {
            boolean pseudoValid = true;
            for (User user : getListOnline()) {
                if (user.getPseudo().equals(pseudo)) {
                    pseudoValid = false;
                    break;
                }
            }
            if (pseudoValid) {
                setMyUser(pseudo);
                UserController.updateMyUser();
                UserController.sendPseudo(pseudo);

                //print list online
                System.out.println("List online :");
                for (User user : getListOnline()) {
                    System.out.println(user.getPseudo());
                }
                //print session list
                System.out.println("Session list :");
                for (TCPSession session : SessionController.sessionsList) {
                    System.out.println(session.getUserDist().getPseudo());
                }
                switchToMainScene(event.getSource());
            } else textPseudoNotValid.setText("This pseudo is already taken");
        }
    }

    public static void tryConnect(ActionEvent event, TextField textFieldPseudo, Text textPseudoNotValid) throws IOException {
        String pseudo = textFieldPseudo.getText().trim();

        UserController.askUserList();

        if (pseudo.isEmpty()) textPseudoNotValid.setText("Please enter a pseudo");
        else if (pseudo.length() > 15) textPseudoNotValid.setText("This pseudo is too long");
        else if (!pseudo.matches("[a-zA-Z0-9]+")) textPseudoNotValid.setText("Only letters and numbers");
        else {
            boolean pseudoValid = true;
            for (User user : getListOnline()) {
                if (user.getPseudo().equals(pseudo)) {
                    pseudoValid = false;
                    getListOnline().clear();
                    break;
                }
            }
            if (pseudoValid) {
                setMyUser(pseudo);
                UserController.addMyUser();
                UserController.sendConnect();
                System.out.println("Connected");
                switchToMainScene(event.getSource());
            } else textPseudoNotValid.setText("This pseudo is already taken");
        }
    }

    public static void switchScene(Object eventSource){
        scene = new Scene(root);

        stage = (Stage) ((Node) eventSource).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        setStage();

    }

    public static void setStage() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            if (getMyUser().getPseudo() != null) {
                System.out.println("Disconnected");
                UserController.sendDisconnect();
            }
            SessionController.initialize();
            UserController.close();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void switchToMainScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(MainFrame.class.getResource("/fxml/mainFrame.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Main Page");
    }

    public static void switchToParametersScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(ParametersFrame.class.getResource("/fxml/parametersFrame.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Parameters");
    }

    public static void switchToLoginScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(LoginFrame.class.getResource("/fxml/loginFrame.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Login");
    }
}
