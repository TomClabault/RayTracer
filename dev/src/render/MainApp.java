package render;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
            imageWriter.execute();

            //CounterFPS counterFPS = new CounterFPS(imageWriter.getUpdateWindow().getWindowTimer().getfpsLabel());
            CounterFPS counterFPS = new CounterFPS(imageWriter.getWindowTimer().getfpsLabel());

            stackPane.getChildren().add(imageWriter.getPane());
            stackPane.getChildren().add(counterFPS.getPane());

            stage.setTitle("Rendu");
            stage.setScene(scene);
            stage.setMaximized(AUTO_MODE); // si AUTO_MODE alors on maximize la fenêtre
            stage.show();

            Toolbox toolbox = new Toolbox(imageWriter.getRayTracingScene(),scene, counterFPS.getPane());
            toolbox.execute();
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            	@Override
            	public void handle(WindowEvent e) {
            		Platform.exit();
            		System.exit(0);
            	}
            });
		} catch (IllegalArgumentException e)
        {
			System.out.println("Vous avez annulé");
			e.printStackTrace();
			
			Platform.exit();
			System.exit(0);
		}

        System.out.println();
    }
}
