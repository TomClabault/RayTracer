package gui.windows;

import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gui.MainApp;
import gui.threads.CameraTimer;
import gui.threads.RenderTask;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
* La classe gerant la fenetre principale contenant le rendu.
*/
public class RenderWindowOld {

    private WritableImage writableImage;
    private PixelWriter pixelWriter;
    private CameraTimer cameraTimer;
    private WindowTimer windowTimer;
    private RenderTask task;
    private RayTracer rayTracer;
    private RayTracingScene rayTracingScene;
    private RayTracerSettings rayTracerSettings;
    private Scene renderScene;
    private Pane statPane;
    private ProgressBar progressBar;

    /**
     * 
     * @param stage Le stage de la fenetre de rendu
     * @param rayTracer L'instance du RayTracer
     * @param rayTracingScene La scene 3d a utiliser
     * @param rayTracerSettings les parametres allant avec l'instance du RayTracer
     */
    public RenderWindowOld(Stage stage, RayTracer rayTracer, RayTracingScene rayTracingScene, RayTracerSettings rayTracerSettings)
    {
        this.rayTracer = rayTracer;
        this.rayTracingScene = rayTracingScene;
        this.rayTracerSettings = rayTracerSettings;
    	
    	
        this.writableImage = new WritableImage(MainApp.WIDTH,MainApp.HEIGHT);
        this.pixelWriter = writableImage.getPixelWriter();
        
        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);
        
        if(MainApp.FULLSCREEN_MODE == true) {
        	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			imageView.setFitHeight(primaryScreenBounds.getHeight());
	        imageView.setFitWidth(primaryScreenBounds.getWidth());
        }

        Pane renderPane = new Pane();
        renderPane.getChildren().add(imageView);
        
        this.statPane = new Pane();
        this.statPane.setVisible(false);//Desactivation des stats par defaut pour ne pas cacher l'affichage
        
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(renderPane);
        stackPane.getChildren().add(statPane);
        
        this.renderScene = new Scene(stackPane);
        
        this.renderScene.getStylesheets().add(RenderWindowOld.class.getResource("../styles/HUDText.css").toExternalForm());
        
        WindowTimer windowTimer = new WindowTimer(this.renderScene, this.rayTracer, this.rayTracerSettings, this.rayTracingScene, this.pixelWriter);
        statPane.getChildren().add(windowTimer.getfpsLabel());
        
        
        this.windowTimer = new WindowTimer(this.renderScene, rayTracer, rayTracerSettings, rayTracingScene, pixelWriter);
        this.progressBar = windowTimer.getProgressBar();
        windowTimer.start();
        this.cameraTimer = new CameraTimer(this.renderScene, rayTracingScene);
        stage.setTitle("Rendu");
        stage.setScene(this.renderScene);
        stage.setMaximized(MainApp.FULLSCREEN_MODE);
        stage.show();
    }
    /**
     * Definie this.rayTracingScene
     * @param rayTracingScene
     */
    public void setRayTracingScene(RayTracingScene rayTracingScene) {
        this.rayTracingScene = rayTracingScene;
    }
    
    /**
     * Retourne la RayTracingScene utilisee dans la classe
     * @return this.rayTracingScene
     */
    public RayTracingScene getRayTracingScene() {
        return this.rayTracingScene;
    }
    
    /**
     * Retourne la writableImage utilise dans la classe
     * @return this.writableImage
     */
    public WritableImage getWritableImage() {
    	return this.writableImage;
    }
    
    /**
     * Retourne la barre de progression de la toolbox
     * @return this.progressBar
     */
    public ProgressBar getProgressBar() {
    	return this.progressBar;
    }
    
    /**
     * Retourne le Pane contenant les informations sur rendu (typiquement les fps visibles sur le rendu)
     * @return this.statPane
     */
    public Pane getStatPane() {
    	return this.statPane;
    }
    
    /**
     * Retourne la scene javafx contenant le rendu
     * @return this.renderScene
     */
    public Scene getRenderScene() {
    	return this.renderScene;
    }

    /**
     * Retourne l'instance de WindowTimer utilisee par la classe
     * @return this.windowTimer
     */
    public WindowTimer getWindowTimer() {
        return this.windowTimer;
    }
    
    /**
     * Retourne la tâche javafx responsable de l'execution du calcul de rendu.
     * @return this.task
     */
    public RenderTask getTask() {
    	return this.task;
    }

    /**
     * Methode executant la classe privee {@link WindowTimer} et {@link gui.threads.CameraTimer}
     */
    public void execute() {
    	windowTimer.start();
        cameraTimer.start();
    }
    
    /**
     * Methode calculant le rendu, executee par la classe {@link WindowTimer}
     * @param pixelBuffer Objet contenant la valeur des pixels.
     * @param pixelWriter Instance de PixelWriter utilise par la classe pour afficher le rendu.
     * @param pixelFormat Objet contenant le format des pixels du pixelBuffer.
     */
    public static void drawImage(IntBuffer pixelBuffer, PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelFormat) {
    	pixelWriter.setPixels(0, 0, MainApp.WIDTH, MainApp.HEIGHT, pixelFormat, pixelBuffer, MainApp.WIDTH);
    }
    
    /**
	 * La classe WindowTimer lance l'execution de la tâche de rendu. Elle effectue aussi le calcul des FPS.
     *
     */
    private class WindowTimer extends AnimationTimer {

        private RayTracingScene rayTracingScene;
        private RayTracerSettings rayTracerSettings;

        private PixelWriter pixelWriter;
        private long oldFrameTime;
        private Label fpsLabel;

        private WritablePixelFormat<IntBuffer> pixelFormat;
        private ExecutorService executorService;
        private RayTracer rayTracer;
        
        private Future<?> futureRenderTask = null;
		private ProgressBar progressBar;

        /**
         * @param scene La Scene javafx de la fenetre du rendu.
         * @param rayTracer L'instance de RayTracer utilise par la classe.
         * @param rayTracerSettings L'instance de RayTracerSettings contenant les parametres de l'instance de RayTracer
         * @param rayTracingScene L'objet contenant les formes 3d
         * @param pixelWriter L'instance de pixelWriter utilise par la classe pour afficher le rendu.
         */
        private WindowTimer(Scene scene, RayTracer rayTracer, RayTracerSettings rayTracerSettings, RayTracingScene rayTracingScene, PixelWriter pixelWriter) {
            this.rayTracingScene = rayTracingScene;

            this.rayTracer = rayTracer;
            this.rayTracerSettings = rayTracerSettings;
            this.pixelWriter = pixelWriter;

            Label fpsLabel = new Label("");
            this.fpsLabel = fpsLabel;
            fpsLabel.setId("fpsLabel");
            this.progressBar = new ProgressBar();
            this.pixelFormat = PixelFormat.getIntArgbPreInstance();
            this.executorService = Executors.newFixedThreadPool(1);

        }
        /**
         * Retourne le Label contenant les statistiques du rendu (typiquement les fps).
         * @return this.fpsLabel
         */
        public Label getfpsLabel() {
        	return this.fpsLabel;
        }
        
        public ProgressBar getProgressBar() {
        	return this.progressBar;
        }

        
        /**
         * Executee a chaque frame, lance les calculs de rendus si les precedents sont termines. Calcul egalement les fps.
         */
        @Override
        public void handle(long actualFrameTime){
        	RenderTask renderTask = new RenderTask(pixelWriter, this.pixelFormat, rayTracer, rayTracingScene, rayTracerSettings);
        	this.progressBar.setProgress(rayTracer.getProgression());
        	if(futureRenderTask == null || futureRenderTask.isDone()){//Si aucune tâche n'a encore ete donnee ou si la tâche est terminee
        		futureRenderTask = executorService.submit(renderTask);//On redonne une autre tâche de rendu a faire
        	}

            renderTask.setOnSucceeded((succeededEvent) -> {
            	IntBuffer pixelBuffer = renderTask.getValue();
            	RenderWindowOld.drawImage(pixelBuffer, pixelWriter, this.pixelFormat);
            	float dif = actualFrameTime - oldFrameTime;
                dif  = 1000000000.0f / dif;
                this.oldFrameTime = actualFrameTime;
                
                String fpsString = String.format("FPS : %.2f", dif);
                if(this.rayTracingScene.getCamera() != null)//On verifie quand meme que la camera n'est pas null
                	fpsString += String.format("\n%s\nH: %.2f°\nV: %.2f°", this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti());
                fpsLabel.setText(fpsString);
            });
        }

    }
}


