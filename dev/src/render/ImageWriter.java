package render;

import rayTracer.RayTracer;
import geometry.materials.MirrorMaterial;
import geometry.materials.MetallicMaterial;
import geometry.materials.MatteMaterial;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.ArrayList;

import geometry.shapes.*;
import geometry.*;
import maths.*;

import scene.*;
import scene.MyScene;
import scene.lights.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

public class ImageWriter {

    public volatile MyScene MyGlobalScene = addObjectsToScene();
    WritableImage writableImage;
    PixelWriter pw;
    Scene scene;


    public ImageWriter(){
        this.writableImage = new WritableImage(MainApp.WIDTH,MainApp.HEIGHT);

        this.pw = writableImage.getPixelWriter();

        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);

        Pane root = new Pane();
        root.getChildren().add(imageView);
        this.scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    public void ImageWriterMain(int height, int width) {

        //UpdateCamera updateCamera = new UpdateCamera(MyGlobalScene, scene);
        //updateCamera.run();
        /*UpdateWindow updateWindow = new UpdateWindow(new RayTracer(MainApp.WIDTH, MainApp.HEIGHT), this.MyGlobalScene, this.pw);
        updateWindow.run();*/
        //while(true){
            RayTracer r = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);
            ImageWriter.doImage(r.renderImage(MyGlobalScene,8),this.pw);
        //}

    }





    public static void doImage(AtomicReferenceArray<Color> colorTab, PixelWriter pw)
    {
        for (int i = 0; i < MainApp.HEIGHT; i++)
            for (int j = 0; j < MainApp.WIDTH; j++)
                pw.setColor(j, i, colorTab.get(i*MainApp.WIDTH + j));
    }

    public MyScene addObjectsToScene() {/*utilisé dans le constructeur*/

        Camera cameraRT = new Camera(new Point(1, 1, -2), new Point(0, 0, -6));
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
