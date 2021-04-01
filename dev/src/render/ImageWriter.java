package render;

import java.nio.IntBuffer;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import materials.Material;
import materials.MatteMaterial;
import materials.MirrorMaterial;
import materials.MetallicMaterial;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

import rayTracer.RayTracer;
import geometry.shapes.*;
import geometry.*;
import maths.*;
import scene.*;
import scene.RayTracingScene;
import scene.lights.*;
import textures.ProceduralTextureCheckerboard;

/**
* Gère le Pane qui contient le rendu
*/
public class ImageWriter {

    private RayTracingScene MyGlobalScene = addObjectsToScene();
    private WritableImage writableImage;
    private PixelWriter pw;
    private Pane pane;
    private Scene mainAppScene;
    private CameraTimer cameraTimer;
    private WindowTimer windowTimer;

    /*
     * @param mainAppScene la Scene javafx, nécéssite d'être passée en argument pour @link{CameraTimer}
     */
    public ImageWriter(Scene mainAppScene){
        this.mainAppScene = mainAppScene;
        this.writableImage = new WritableImage(MainApp.WIDTH,MainApp.HEIGHT);

        this.pw = writableImage.getPixelWriter();

        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);

        Pane pane = new Pane();
        pane.getChildren().add(imageView);
        this.pane = pane;

        WindowTimer windowTimer = new WindowTimer(this.MyGlobalScene, this.pw, new RayTracer(MainApp.WIDTH, MainApp.HEIGHT, 6));
        this.windowTimer = windowTimer;

        CameraTimer cameraTimer = new CameraTimer(this.mainAppScene, this.MyGlobalScene);
        this.cameraTimer = cameraTimer;
    }

    public void setRayTracingScene(RayTracingScene rayTracingScene) {
        this.MyGlobalScene = rayTracingScene;
    }

    public RayTracingScene getRayTracingScene() {
        return this.MyGlobalScene;
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

    	//Camera cameraRT = new Camera(new Point(-2.790, 3.562, -4.463), -60.0, -15.0);
    	Camera cameraRT = new Camera(new Point(0, 1, -2), 0.0, 0.0);
        
        cameraRT.setFOV(60);
        //Light l = new LightBulb(Point.scalarMul(1000000, new Point(1, 2, 1)), 1);
        Light l = new LightBulb(new Point(1, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(100, 100, 100), Color.WHITE, 1.0/2.0))));

        double difuse = 1.0;
        double refle = 0.0;
        double spec = 1.0;
        int shini = 256;
        //shapeList.add(new SphereMaths(new Point(0, 3, -6), 1, new Material(Color.rgb(255, 0, 0), difuse, refle, spec, shini)));
        //shapeList.add(new Rectangle(new Point(-2, -2, -4), new Point(2, 2, -8), new MatteMaterial(Color.rgb(64, 64, 64))));
        shapeList.add(new SphereMaths(new Point(0, 0.5, -6), 1, new MirrorMaterial(0.75)));
        shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, new MetallicMaterial(Color.rgb(255, 211, 0))));
        shapeList.add(new SphereMaths(new Point(-1.25, 1, -6.5), 0.2, new MetallicMaterial(Color.LIGHTSKYBLUE)));
        shapeList.add(new SphereMaths(new Point(-1.5, -0.65, -5.5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.ORANGERED, Color.ORANGERED.darker(), 12))));
        shapeList.add(new SphereMaths(new Point(1.5, -0.65, -5), 0.35, new MirrorMaterial(0.75)));
        
        Image skybox = new Image("file:oberer_kuhberg.jpg");
        RayTracingScene sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);

        return  sceneRT;
    }
}
