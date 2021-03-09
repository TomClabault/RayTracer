package render;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static int HEIGHT;/*Définie par choiceWindowMain*/
    public static int WIDTH;

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        ChoiceWindow choiceWindow = new ChoiceWindow();
        choiceWindow.choiceWindowMain();

        StackPane stackPane = new StackPane();

        Scene scene = new Scene(stackPane);
        File f = new File("src/render/style/fpsCounter.css");
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));//line form https://blog.idrsolutions.com/2014/04/use-external-css-files-javafx/

        ImageWriter imageWriter = new ImageWriter(scene);
        imageWriter.ImageWriterMain(HEIGHT, WIDTH);

        CounterFPS counterFPS = new CounterFPS(imageWriter.getUpdateWindow().getWindowTimer().getfpsLabel());

        stackPane.getChildren().add(imageWriter.getPane());
        stackPane.getChildren().add(counterFPS.getPane());

        stage.setTitle("Rendu");
        stage.setScene(scene);
        stage.show();
    }
}
