package render;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;

import geometry.shapes.*;
import geometry.*;

import scene.*;
import scene.MyScene;
import scene.lights.*;
import rayTracer.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

import java.awt.event.*;

public class MainApp extends Application {

    public static int HEIGHT;/*Définie par choiceWindowMain*/
    public static int WIDTH;
    public volatile MyScene scene = addObjectsToScene();

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        ChoiceWindow choiceWindow = new ChoiceWindow();
        choiceWindow.choiceWindowMain();

        WritableImage writableImage = new WritableImage(800,600);
        PixelWriter pw = writableImage.getPixelWriter();

        ImageWriter imageWriter = new ImageWriter();
        imageWriter.ImageWriterMain(HEIGHT, WIDTH, writableImage);



        updateWindow(pw, scene);



    }

    public MyScene addObjectsToScene() {

        Camera c = new Camera(); c.setFOV(100);
        Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);
        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new SphereMaths(new Point(0, 0, -4), 1));
        shapeList.add(new Triangle(new Point(-1,-1,-1.5),new Point(1,-1,-1.5),new Point(0,2,-1.5)));
        MyScene s = new MyScene(c, l, shapeList, 0.5);
        return s;
    }


    public void updateWindow(final PixelWriter pw, final MyScene scene) {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    RayTracer r = new RayTracer(MainApp.HEIGHT, MainApp.WIDTH);
                    ImageWriter.doImage(r.computeImage(scene),pw);
                }

            }
        }).start();
    }

    /*public void updateCamera(final PixelWriter pw, final MyScene scene) {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    RayTracer r = new RayTracer(MainApp.HEIGHT, MainApp.WIDTH);
                    ImageWriter.doImage(r.computeImage(scene),pw);
                }

            }
        }).start();
    }*/

}
