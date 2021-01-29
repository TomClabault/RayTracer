package tests;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class testJavaFX extends Application {
 
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.RED);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}