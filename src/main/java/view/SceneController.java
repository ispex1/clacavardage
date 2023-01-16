package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Objects;

import static controller.UserController.*;
import controller.SessionController;

public class SceneController extends Application {
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;

    public static void main(String[] args){
        //TODO: remove this line, just for testing
        if (getListOnline().isEmpty()) testListOnline(); showListOnline();
        launch(args);
    }

    public void start(Stage stage){
        try {
            root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/loginFrame.fxml")));

            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();

            stage.getIcons().add(new Image("/images/logo_temp.png"));
            stage.setTitle("Clac Chat - Login");
            stage.setResizable(false);

            SceneController.stage = stage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pseudoValid(ActionEvent event, TextField textFieldPseudo, Text textPseudoNotValid) throws IOException {
        String pseudo = textFieldPseudo.getText().toUpperCase();

        if (getMyUser()!=null && pseudo.equals(getMyUser().getPseudo())) {
            SceneController.switchToMainScene(event.getSource());
            System.out.println("Pseudo non modifié");
        }
        else {
            if (pseudo.isEmpty()) textPseudoNotValid.setText("Please enter a pseudo");
            else if (pseudo.length() > 19) textPseudoNotValid.setText("This pseudo is too long");
            else if (pseudo.contains(" ")) textPseudoNotValid.setText("Pseudo can't contain spaces");
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
                    SceneController.switchToMainScene(event.getSource());
                } else textPseudoNotValid.setText("This pseudo is already taken");
            }
        }
    }

    public static void switchScene(Object eventSource){
        scene = new Scene(root);

        stage = (Stage) ((Node) eventSource).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
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

    public static void switchToTestScene(Object eventSource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(LoginFrame.class.getResource("/fxml/testBorderPane.fxml")));
        switchScene(eventSource);
        stage.setTitle("Clac Chat - Test");
    }
}
