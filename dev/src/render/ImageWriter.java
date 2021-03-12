package render;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

import rayTracer.RayTracer;
import geometry.materials.MirrorMaterial;
import geometry.materials.MetallicMaterial;
import geometry.materials.MatteMaterial;
import geometry.shapes.*;
import geometry.*;
import maths.*;
import scene.*;
import scene.MyScene;
import scene.lights.*;

/**
* Gère le Pane qui contient le rendu
*/
public class ImageWriter {

    private MyScene MyGlobalScene = addObjectsToScene();
    private WritableImage writableImage;
    private PixelWriter pw;
    private Pane pane;
    private Scene mainAppScene;
    private CameraTimer cameraTimer;
    private WindowTimer windowTimer;

    /**
     *
     * @param mainAppScene la Scene javafx, nécéssite d'être passée en argument pour {@link UpdateCamera}
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

        WindowTimer windowTimer = new WindowTimer(this.MyGlobalScene, this.pw, new RayTracer(MainApp.WIDTH, MainApp.HEIGHT));
        this.windowTimer = windowTimer;

        CameraTimer cameraTimer = new CameraTimer(this.mainAppScene, this.MyGlobalScene);
        this.cameraTimer = cameraTimer;
    }

    public void setMyScene(MyScene myScene) {
        this.MyGlobalScene = myScene;
    }

    public MyScene getMyScene() {
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

    public static void doImage(AtomicReferenceArray<Color> colorTab, PixelWriter pw)
    {
        for (int i = 0; i < MainApp.HEIGHT; i++)
            for (int j = 0; j < MainApp.WIDTH; j++)
                pw.setColor(j, i, colorTab.get(i*MainApp.WIDTH + j));
    }

    public MyScene addObjectsToScene() {/*utilisé dans le constructeur*/

    	//(1.820, 1,820, 1.280) (2.720, 0.820, 0.820)
        Camera cameraRT = new Camera(new Point(1, 1, -2), new Point(0, 0, -6));
    	//Camera cameraRT = new Camera(new Point(1.820, 1.820, 1.280), new Point(2.720, 0.820, 0.820));
        cameraRT.setFOV(60);
        Light l = new LightBulb(new Point(-0.5, 0.5, -4), 1.25);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128))));

        shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, new MetallicMaterial(Color.rgb(240, 0, 0))));
        shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, new MetallicMaterial(Color.rgb(255, 211, 0))));
        shapeList.add(new SphereMaths(new Point(-1.25, 1, -6.5), 0.2, new MetallicMaterial(Color.LIGHTSKYBLUE)));
        shapeList.add(new SphereMaths(new Point(-1.5, -0.65, -5.5), 0.35, new MatteMaterial(Color.ORANGERED)));
        shapeList.add(new SphereMaths(new Point(1.5, -0.65, -5), 0.35, new MirrorMaterial(0.75)));

        MyScene sceneRT = new MyScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.55);

        return  sceneRT;
    }
}
