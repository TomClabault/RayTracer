package render;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import povParser.automat.Automat;
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

        URL url = Automat.class.getResource("scenes/roughScene.pov");
        if (url != null) {
			String pathToPov = url.toExternalForm();
			rayTracingScene = Automat.parsePov(pathToPov);
		} else {
			System.out.println("Impossible de trouver le fichier de scene POV par défaut");
			
		}
        
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
