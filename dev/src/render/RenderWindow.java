package render;

import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

import povParser.Automat;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import geometry.shapes.*;
import geometry.*;
import materials.*;
import materials.textures.*;
import maths.*;
import scene.*;
import scene.RayTracingScene;
import scene.lights.*;

/**
* Gère le Pane qui contient le rendu
*/
public class RenderWindow {

    
    private WritableImage writableImage;
    private PixelWriter pixelWriter;
    private Pane renderPane;
    private CameraTimer cameraTimer;
    private WindowTimer windowTimer;
    private DoImageTask task;
    private Stage stage;
    private RayTracer rayTracer;
    private RayTracingScene rayTracingScene;
    private RayTracerSettings rayTracerSettings;
    private Scene renderScene;
    private Pane statPane;

    /**
     *
     * @param mainAppScene la Scene javafx, nécéssite d'être passée en argument pour {@link render.CameraTimer}
    */
    public RenderWindow(Stage stage, RayTracer rayTracer, RayTracingScene rayTracingScene, RayTracerSettings rayTracerSettings)
    {
        this.stage = stage;
        this.rayTracer = rayTracer;
        this.rayTracingScene = rayTracingScene;
        this.rayTracerSettings = rayTracerSettings;
    	
    	
        this.writableImage = new WritableImage(MainApp.WIDTH,MainApp.HEIGHT);
        this.pixelWriter = writableImage.getPixelWriter();
        
        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);
        
        if(MainApp.AUTO_MODE == true) {
        	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			imageView.setFitHeight(primaryScreenBounds.getHeight());
	        imageView.setFitWidth(primaryScreenBounds.getWidth());
        }

        Pane renderPane = new Pane();
        renderPane.getChildren().add(imageView);
        this.renderPane = renderPane;
        
        this.statPane = new Pane();
        this.statPane.setVisible(false);//Désactivation des stats par défaut pour ne pas cacher l'affichage
        
        
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(renderPane);
        stackPane.getChildren().add(statPane);
        
        this.renderScene = new Scene(stackPane);
        
        this.renderScene.getStylesheets().add(MainApp.class.getResource("style/fpsCounter.css").toExternalForm());
        
        WindowTimer windowTimer = new WindowTimer(this.renderScene, this.rayTracer, this.rayTracerSettings, this.rayTracingScene, this.pixelWriter);
        statPane.getChildren().add(windowTimer.getfpsLabel());
        
        this.windowTimer = new WindowTimer(this.renderScene, rayTracer, rayTracerSettings, rayTracingScene, pixelWriter);
        windowTimer.start();
        this.cameraTimer = new CameraTimer(this.renderScene, rayTracingScene);
        stage.setTitle("Rendu");
        stage.setScene(this.renderScene);
        stage.setMaximized(MainApp.AUTO_MODE);
        stage.show();
    }

    public void setRayTracingScene(RayTracingScene rayTracingScene) {
        this.rayTracingScene = rayTracingScene;
    }

    public RayTracingScene getRayTracingScene() {
        return this.rayTracingScene;
    }
    
    public Pane getStatPane() {
    	return this.statPane;
    }
    
    public Scene getRenderScene() {
    	return this.renderScene;
    }

    public WindowTimer getWindowTimer() {
        return this.windowTimer;
    }

    public DoImageTask getTask() {
    	return this.task;
    }

    public void execute() {
    	windowTimer.start();
        cameraTimer.start();
    }

    public static void doImage(IntBuffer pixelBuffer, PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelFormat)
    {
    	pixelWriter.setPixels(0, 0, MainApp.WIDTH, MainApp.HEIGHT, pixelFormat, pixelBuffer, MainApp.WIDTH);
    }
    
    private class WindowTimer extends AnimationTimer {

        private RayTracingScene rayTracingScene;
        private RayTracerSettings rayTracerSettings;

        private PixelWriter pixelWriter;
        private long oldFrameTime;
        private Label fpsLabel;

        private WritablePixelFormat<IntBuffer> pixelFormat;
        private ExecutorService executortService;
        private Scene mainAppScene;
        private RayTracer rayTracer;
        
        private Future<?> futureRenderTask = null;

        private WindowTimer(Scene scene, RayTracer rayTracer, RayTracerSettings rayTracerSettings, RayTracingScene rayTracingScene, PixelWriter pixelWriter) {
            this.rayTracingScene = rayTracingScene;

            this.rayTracer = rayTracer;
            this.rayTracerSettings = rayTracerSettings;
            this.pixelWriter = pixelWriter;

            Label fpsLabel = new Label("");
            this.fpsLabel = fpsLabel;
            fpsLabel.setId("fpsLabel");
            this.mainAppScene = scene;
            this.pixelFormat = PixelFormat.getIntArgbPreInstance();
            this.executortService = Executors.newFixedThreadPool(1);

        }

        public Label getfpsLabel() {
        	return fpsLabel;
        }
        
        public RayTracerSettings getRayTracerSettings() {
        	return this.rayTracerSettings;
        }


        public void handle(long actualFrameTime){
        	DoImageTask renderTask = new DoImageTask(mainAppScene, pixelWriter, PixelFormat.getIntArgbPreInstance(), rayTracer, rayTracingScene, rayTracerSettings);
        	if(futureRenderTask == null || futureRenderTask.isDone()){//Si aucune tâche n'a encore été donnée ou si la tâche est terminée
        		futureRenderTask = executortService.submit(renderTask);//On redonne une autre tâche de rendu à faire
        	}

            renderTask.setOnSucceeded((succeededEvent) -> {
            	IntBuffer pixelBuffer = renderTask.getValue();
            	RenderWindow.doImage(pixelBuffer, pixelWriter, pixelFormat);
            	long dif = actualFrameTime - oldFrameTime;
                dif  = (long)1000000000.0 / dif;
                this.oldFrameTime = actualFrameTime;
                
                String fpsString = String.format("FPS : %d", dif);
                if(this.rayTracingScene.getCamera() != null)//On vérifie quand même que la caméra n'est pas null
                	fpsString += String.format("\n%s\nH: %.2f°\nV: %.2f°", this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti());
                fpsLabel.setText(fpsString);
            });
        }

    }
}


