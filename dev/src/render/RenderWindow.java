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
        
        
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(renderPane);
        stackPane.getChildren().add(statPane);
        
        this.renderScene = new Scene(stackPane);
        
        this.renderScene.getStylesheets().add(MainApp.class.getResource("style/fpsCounter.css").toExternalForm());
        
        WindowTimer windowTimer = new WindowTimer(this.renderScene, this.rayTracer, this.rayTracerSettings, this.rayTracingScene, this.pixelWriter);
        statPane.getChildren().add(windowTimer.getfpsLabel());
        
        this.windowTimer = new WindowTimer(this.renderScene, rayTracer, rayTracerSettings, rayTracingScene, pixelWriter);
        windowTimer.start();
        System.out.println("EXECUTION");
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

    public RayTracingScene generateUsualScene() 
    {

    	Camera cameraRT = new Camera(new Point(0.000, 0.5, 0.320), 0, 0, 40);//Magic camera
    	//Camera cameraRT = new Camera(new Point(0.75, -0.75, -5.5), 0, 0);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));
        //shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new RoughMaterial(Color.rgb(48, 48, 48), 0.75, new ProceduralTextureCheckerboard(Color.rgb(16, 16, 16), Color.rgb(75, 75, 75), 1.0))));

        //shapeList.add(new Sphere(new Point(0, 0.5, -3.5), 1, new GlossyMaterial(Color.GOLD, 0.92)));
        shapeList.add(new Sphere(new Point(-1.25, 0.5, -6), 1, new MirrorMaterial(0.75)));
        shapeList.add(new Sphere(new Point(0, 1.5, -6), 0.5, new RoughMaterial(ColorOperations.sRGBGamma2_2ToLinear(Color.web("D4AF37")), 0.75)));
        shapeList.add(new Sphere(new Point(1.25, 0.5, -6), 1, new GlassMaterial()));
        
        shapeList.add(new Sphere(Point.translateMul(new Point(-0.3, 0.5, -0.1), new Vector(1.250, 0.000, -4.500), 1.5625), 0.2, new GlassyMaterial(Color.GREEN)));
        shapeList.add(new Sphere(new Point(-2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.BLACK, Color.YELLOW, 12))));
        shapeList.add(new Sphere(new Point(2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.RED, Color.DARKRED.darker(), 12))));
        
        shapeList.add(new Sphere(new Point(0, -0.5, -6), 0.5, new GlassyMaterial(Color.RED)));
        shapeList.add(new Sphere(new Point(-0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));
        shapeList.add(new Sphere(new Point(0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));
        //shapeList.add(new Icosphere(new Point(0, 2, -6), 1, 2, new GlassyMaterial(Color.rgb(0, 128, 255))));
        //shapeList.add(new Rectangle(new Point(-1.25, 1.5, -6), new Point(-0.25, 2.5, -7), new GlassyMaterial(Color.RED)));
        
        
        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());
        
        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargée
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisée.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        sceneRT.addLight(new LightBulb(new Point(-2, 2.5, 1.440), 1));
        return  sceneRT;
    }
    
    public RayTracingScene generateRoughnessDemoScene() 
    {
    	Camera cameraRT = new Camera(new Point(-2.000, 4, -1), new Point(-2, 0, -8), 40);
        PositionnalLight l = new LightBulb(new Point(-2, 6, 0), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));

        double roughnessTab[] = new double[] {0.5, 0.75, 0.9, 1};
        for(int y = 0; y < 4; y++)
        {
        	for(int x = 0; x < 4; x++)
        	{
        		Color sphereColor = ColorOperations.sRGBGamma2_2ToLinear(Color.web("D4AF37").interpolate(Color.rgb(32, 32, 32), 1.0/4.0*x));
        		
//        		System.out.println(
//        				"Position:" + new Point(-5 + x * 2, -0.5, -15 + y * 3) 
//        				+ String.format("Color: [%.3f, %.3f, %.3f]", sphereColor.getRed(), sphereColor.getGreen(), sphereColor.getBlue()) 
//        				+ " Roughness: " + roughnessTab[y]
//        				+ String.format(" Specular Size/Intensity: %d/%.3f", RoughMaterial.computeSpecularSize(roughnessTab[y]), RoughMaterial.computeSpecularIntensity(roughnessTab[y])) );
        		
                shapeList.add(new Sphere(new Point(-5 + x * 2, -0.5, -15 + y * 3), 0.5, new RoughMaterial(sphereColor, roughnessTab[y])));
        	}
        }
        
        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());
        
        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargée
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisée.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        return  sceneRT;
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
                fpsLabel.setText(String.format("FPS : %d\n%s\nH: %.2f°\nV: %.2f°", dif, this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti()));
            });
        }

    }
}


