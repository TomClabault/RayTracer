package render;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import povParser.Automat;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
 * La classe contenant le Main qui gère la totalité de l'application
*/
public class MainApp extends Application {
	
//  {
//	Image skybox = null;
//    URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
//    if(skyboxURL != null)
//    		skybox = new Image(skyboxURL.toExternalForm());
//
//	this.myGlobalScene = Automat.parsePov("dev/src/povParser/roughScene.pov");
//	this.myGlobalScene.setSkybox(skybox);
//}

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

        SetSizeWindow setSizeWindow = new SetSizeWindow();
        setSizeWindow.execute();
        
        RayTracer rayTracer = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);
        RayTracerSettings rayTracerSettings = new RayTracerSettings(8, 4, 9, 4);
        rayTracerSettings.enableAntialiasing(true);
        rayTracerSettings.enableBlurryReflections(true);
        
        RayTracingScene rayTracingScene = new RayTracingScene();

        FileChooser fileChooser = new FileChooser();
	   	fileChooser.setTitle("Selectionner un fichier POV");
	   	ExtensionFilter filter = new ExtensionFilter("POV", "*.pov");
	   	fileChooser.getExtensionFilters().add(filter);
	   	File file = fileChooser.showOpenDialog(stage);
	   	System.out.println("fichier selectionner : " + file);
        
    	RenderWindow renderWindow = new RenderWindow(stage, rayTracer, rayTracingScene, rayTracerSettings);
        renderWindow.execute();

        Toolbox toolbox = new Toolbox(rayTracingScene, renderWindow.getRenderScene(), renderWindow.getStatPane(), rayTracer, rayTracerSettings);
        toolbox.execute();
        
        
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	@Override
        	public void handle(WindowEvent e) {
        		Platform.exit();
        		System.exit(0);
        	}
        });
    }
}
