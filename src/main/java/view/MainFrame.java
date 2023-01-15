package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static controller.UserController.*;
import static view.SceneController.*;

public class MainFrame{
    @FXML
    private Label myPseudo;
    @FXML
    private Label myIP;
    @FXML
    private ListView<String> UsersList;
    @FXML
    private ImageView imgCat;

    public void initialize() {
       myPseudo.setText(myUser.getPseudo());
       myIP.setText("IP : " + myUser.getIP());
       updateUsersList();
    }

    public void updateUsersList() {
        UsersList.getItems().clear();
        for (User user : listOnline) {
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

        try{
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

}
