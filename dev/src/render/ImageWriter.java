package render;

import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

import povParser.automat.Automat;
import rayTracer.RayTracer;
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
public class ImageWriter {

    private RayTracingScene myGlobalScene;
    
    private WritableImage writableImage;
    private PixelWriter pw;
    private Pane pane;
    private Scene mainAppScene;
    private CameraTimer cameraTimer;
    private WindowTimer windowTimer;

    /**
     *
     * @param mainAppScene la Scene javafx, nécéssite d'être passée en argument pour {@link render.CameraTimer}
    */
    public ImageWriter(Scene mainAppScene)
    {
        this.mainAppScene = mainAppScene;
        //this.myGlobalScene = Automat.parsePov("dev/src/povParser/sphere.pov");
        this.myGlobalScene = addObjectsToScene();
        this.writableImage = new WritableImage(MainApp.WIDTH,MainApp.HEIGHT);

        System.out.println(myGlobalScene);
        
        this.pw = writableImage.getPixelWriter();
        
        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);
        if(MainApp.AUTO_MODE == true) {
        	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			imageView.setFitHeight(primaryScreenBounds.getHeight());
	        imageView.setFitWidth(primaryScreenBounds.getWidth());
        }
        
        Pane pane = new Pane();
        pane.getChildren().add(imageView);
        this.pane = pane;

        WindowTimer windowTimer = new WindowTimer(this.myGlobalScene, this.pw, new RayTracer(MainApp.WIDTH, MainApp.HEIGHT));
        this.windowTimer = windowTimer;

        CameraTimer cameraTimer = new CameraTimer(this.mainAppScene, this.myGlobalScene);
        this.cameraTimer = cameraTimer;
    }

    public void setRayTracingScene(RayTracingScene rayTracingScene) {
        this.myGlobalScene = rayTracingScene;
    }

    public RayTracingScene getRayTracingScene() {
        return this.myGlobalScene;
    }

    public WindowTimer getWindowTimer() {
        return this.windowTimer;
    }

    public Pane getPane() {
    	return pane;
    }

    public void ImageWriterMain(int height, int width) {
        windowTimer.start();
        cameraTimer.start();
        //this.updateCamera.run();
        //this.updateWindow.run();
    }

    public static void doImage(IntBuffer pixelBuffer, PixelWriter pw, WritablePixelFormat<IntBuffer> pixelFormat)
    {
    	pw.setPixels(0, 0, MainApp.WIDTH, MainApp.HEIGHT, pixelFormat, pixelBuffer, MainApp.WIDTH);
    }

    public RayTracingScene addObjectsToScene() {/*utilisé dans le constructeur*/

    	Camera cameraRT = new Camera(new Point(0.000, 0.5, -1.5), 0, 0, 60);//Magic camera
    	//Camera cameraRT = new Camera(new Point(0.75, -0.75, -5.5), 0, 0);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0/2.0))));

        shapeList.add(new Sphere(new Point(-1.25, 0.5, -6), 1, new MirrorMaterial(0.75)));
        shapeList.add(new Sphere(Point.translateMul(new Point(-0.25, 0.5, -0.1), new Vector(1.250, 0.000, -4.500), 1.5625), 0.2, new GlassyMaterial(Color.GREEN)));
        shapeList.add(new Sphere(new Point(-1.25, 1, -6.5), 0.2, new MetallicMaterial(Color.LIGHTSKYBLUE)));
        shapeList.add(new Sphere(new Point(-2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.BLACK, Color.YELLOW, 12))));
        shapeList.add(new Sphere(new Point(2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.RED, Color.DARKRED.darker(), 12))));
        shapeList.add(new Sphere(new Point(0, -0.5, -6), 0.5, new GlassyMaterial(Color.RED)));
        shapeList.add(new Sphere(new Point(-0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));
        shapeList.add(new Sphere(new Point(0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));
        shapeList.add(new Sphere(new Point(1.25, 0.5, -6), 1, new GlassMaterial()));
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

        return  sceneRT;
    }
}
