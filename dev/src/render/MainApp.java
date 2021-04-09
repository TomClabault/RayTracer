package render;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;
import scene.RayTracingScene;
import javafx.concurrent.*;

/**
 * La classe contenant le Main qui gère la totalité de l'application
*/
public class MainApp extends Application {

    /**
     * Définie par la fenètre du choix de taille de rendu
    */
    public static int HEIGHT;
    /**
     * Définie par la fenètre du choix de taille de rendu
    */
    public static int WIDTH;

    public static boolean AUTO_MODE;

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        //ChoiceWindow choiceWindow = new ChoiceWindow();
        //choiceWindow.choiceWindowMain();
        SetSizeWindow setSizeWindow = new SetSizeWindow();

        setSizeWindow.execute();

        StackPane stackPane = new StackPane();

        Scene scene = new Scene(stackPane);

        scene.getStylesheets().add(MainApp.class.getResource("style/fpsCounter.css").toExternalForm());
        try {
        	ImageWriter imageWriter = new ImageWriter(scene);
            imageWriter.ImageWriterMain(HEIGHT, WIDTH);

            //CounterFPS counterFPS = new CounterFPS(imageWriter.getUpdateWindow().getWindowTimer().getfpsLabel());
            CounterFPS counterFPS = new CounterFPS(imageWriter.getWindowTimer().getfpsLabel());

            stackPane.getChildren().add(imageWriter.getPane());
            stackPane.getChildren().add(counterFPS.getPane());

            stage.setTitle("Rendu");
            stage.setScene(scene);
            stage.setMaximized(AUTO_MODE); // si AUTO_MODE alors on maximize la fenêtre
            stage.show();

            Toolbox toolbox = new Toolbox(imageWriter.getRayTracingScene());
            toolbox.execute();

            //Toolbox toolboxTask = new Toolbox(null);
            //ExecutorService es = Executors.newFixedThreadPool(1);
            //.execute(toolboxTask);
		} catch (IllegalArgumentException e)
        {
			System.out.println("Vous avez annulé");
			e.printStackTrace();

			System.exit(0);
		}

        System.out.println();
    }
}
