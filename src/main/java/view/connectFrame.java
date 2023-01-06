package view;

import javafx.application.Application;
import javafx.stage.Stage;

// I want to create a javafx frame with a button and a textfield

public class connectFrame extends Application {

    public static void main(String[] args) 
    {
        launch(args);
    }
 
    public void start(Stage theStage) 
    {
        theStage.setTitle("Hello, World!");
        theStage.show();
    }

}
    