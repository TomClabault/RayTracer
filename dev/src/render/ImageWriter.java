package render;

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
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class ImageWriter {

    public static final Double DELTA_MOVE = 0.1;
    public volatile MyScene MyGlobalScene = addObjectsToScene();
    WritableImage writableImage;
    PixelWriter pw;
    Scene scene;


    public ImageWriter(){
        this.writableImage = new WritableImage(800,600);

        this.pw = writableImage.getPixelWriter();

        ImageView imageView = new ImageView();
        imageView.setImage(writableImage);

        Pane root = new Pane();
        root.getChildren().add(imageView);
        this.scene = new Scene(root);
    }

    public void ImageWriterMain(int height, int width) {

        Stage stage = new Stage();



        /*pw.setColor(2,2,Color.web("0x0000FF"));
           pw.setColor(2,3,Color.web("0x0000FF"));
           pw.setColor(3,2,Color.web("0x0000FF"));
           pw.setColor(3,3,Color.web("0x0000FF"));/*Color.rgb(0,0,255)*/
        /*Prend en argument un tableau de couleur*/

        /*ImageView imageView = new ImageView();
        imageView.setImage(writableImage);

        Pane root = new Pane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root);
        stage.setScene(scene);*/
        stage.setTitle("");
        stage.show();

        /*pw.setColor(2,2,Color.web("0xFF0000"));
           pw.setColor(3,2,Color.web("0xFF0000"));
           pw.setColor(2,3,Color.web("0xFF0000"));
           pw.setColor(3,3,Color.web("0xFF0000"));*/
        //RayTracer rayTracer = new RayTracer(800,600);
        //computeImage = rayTracer.computeImage();

        /*RayTracer r = new RayTracer(height, width);

        Camera c = new Camera(); c.setFOV(100);
        Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new SphereMaths(new Point(0, 0, -4), 1));
        shapeList.add(new Triangle(new Point(-1,-1,-1.5),new Point(1,-1,-1.5),new Point(0,2,-1.5)));


        MyScene s = new MyScene(c, l, shapeList, 0.5);


        doImage(r.computeImage(s),pw);*/

    }





    public static void doImage(Color[][] colorTab, PixelWriter pw) {
        System.out.println("colorTab.length = " + colorTab.length);
        System.out.println("colorTab[0].length = " + colorTab[0].length);
        int width = colorTab[0].length;
        int height = colorTab.length;

        for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                        //System.out.println(height);
                        //System.out.println(String.format("%d %d",i,j));
                        pw.setColor(j,i,colorTab[i][j]);
                }
        }
        System.out.println("finish");

    }

    public MyScene addObjectsToScene() {/*utilisé dans le constructeur*/

        Camera c = new Camera(); c.setFOV(100);
        Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);
        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new SphereMaths(new Point(0, 0, -4), 1));
        shapeList.add(new Triangle(new Point(-1,-1,-1.5),new Point(1,-1,-1.5),new Point(0,2,-1.5)));
        MyScene s = new MyScene(c, l, shapeList, 0.5);
        return s;
    }


    public void updateWindow() {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    RayTracer r = new RayTracer(MainApp.HEIGHT, MainApp.WIDTH);
                    ImageWriter.doImage(r.computeImage(MyGlobalScene),pw);
                }

            }
        }).start();
    }

    public void updateCamera() {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true) {
                    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode() == KeyCode.UP) {
                                upGlobalCamera();
                            }else if (event.getCode() == KeyCode.DOWN) {
                                downGlobalCamera();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void upGlobalCamera() {
        MyGlobalScene.getCamera().getPosition().setY(MyGlobalScene.getCamera().getPosition().getY() + DELTA_MOVE);
    }

    public void downGlobalCamera() {
        MyGlobalScene.getCamera().getPosition().setY(MyGlobalScene.getCamera().getPosition().getY() - DELTA_MOVE);
    }

}
