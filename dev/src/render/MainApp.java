package render;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static int HEIGHT;
    public static int WIDTH;

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        choiceWindow.choiceWindowMain();
        ImageWriter.ImageWriterMain(HEIGHT, WIDTH);
    }

}
