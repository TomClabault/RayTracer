package render;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.stage.Stage;
import util.ImageUtil;

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

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) 
    {
    	ChoiceWindow choiceWindow = new ChoiceWindow();
        choiceWindow.choiceWindowMain();

        StackPane stackPane = new StackPane();

        Scene scene = new Scene(stackPane);
        
        ImageWriter imageWriter = new ImageWriter(scene);
        imageWriter.ImageWriterMain(HEIGHT, WIDTH);
        
        CounterFPS counterFPS = new CounterFPS(imageWriter.getWindowTimer().getfpsLabel());

        URL fpsCounterCSSURL= this.getClass().getResource("styles/fpsCounter.css");
        if(fpsCounterCSSURL == null)//Le fichier n'a pas été trouvé
        	imageWriter.getWindowTimer().getfpsLabel().setStyle("-fx-font-size: 4em; -fx-text-fill: white; -fx-border-color: white;");//On peut avoir une partie du style avec cette ligne. Il manque seulement le contour noir au texte
        else
        {
        	String fpsCounterCSSPath = fpsCounterCSSURL.toExternalForm();
        	scene.getStylesheets().add(fpsCounterCSSPath);//line form https://blog.idrsolutions.com/2014/04/use-external-css-files-javafx/
        }
        stackPane.getChildren().add(imageWriter.getPane());
        stackPane.getChildren().add(counterFPS.getPane());

        stage.setTitle("Rendu");
        stage.setScene(scene);
        stage.show();
    }
}
