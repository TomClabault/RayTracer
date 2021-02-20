package render;

import java.util.ArrayList;

import geometry.shapes.*;
import geometry.*;

import scene.*;
import scene.MyScene;
import scene.lights.*;
import rayTracer.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
//from www.java2s.com
public class ExempleImageWriter extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) 
  {
	  	Shape sphere = new SphereMaths(new Point(0, 0, -2), 1);
		Ray rayon = new Ray(new Point(0,0 ,0), new Vector(0, 0, -1));
		Point intersection = sphere.intersect(rayon);
		System.out.println(intersection);
		return;
	
//	  int height = 1080;
//	  int width = 1920;
//	  
//    WritableImage writableImage = new WritableImage(width,height);
//
//    PixelWriter pw = writableImage.getPixelWriter();
//
//    /*pw.setColor(2,2,Color.web("0x0000FF"));
//    pw.setColor(2,3,Color.web("0x0000FF"));
//    pw.setColor(3,2,Color.web("0x0000FF"));
//    pw.setColor(3,3,Color.web("0x0000FF"));/*Color.rgb(0,0,255)*/
//    /*Prend en argument un tableau de couleur*/
//
//    ImageView imageView = new ImageView();
//    imageView.setImage(writableImage);
//
//    Pane root = new Pane();
//    root.getChildren().add(imageView);
//    Scene scene = new Scene(root);
//    stage.setScene(scene);
//    stage.setTitle("");
//    stage.show();
//
//    /*pw.setColor(2,2,Color.web("0xFF0000"));
//    pw.setColor(3,2,Color.web("0xFF0000"));
//    pw.setColor(2,3,Color.web("0xFF0000"));
//    pw.setColor(3,3,Color.web("0xFF0000"));*/
//    //RayTracer rayTracer = new RayTracer(800,600);
//    //computeImage = rayTracer.computeImage();
//    //doImage(colorTab, pw);/*TODO trouver un moyen d'executer cette foction en récupérant le tableau ici*/
//
//    RayTracer r = new RayTracer(width, height);
//
//    Camera c = new Camera(); c.setFOV(40);
//    Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);
//
//    ArrayList<Shape> shapeList = new ArrayList<>();
//    shapeList.add(new SphereMaths(new Point(-1, 0, -14), 1));
//
//
//    MyScene s = new MyScene(c, l, shapeList, 0.5);
//
//
//    doImage(r.computeImage(s),pw);

  }

  public void doImage(Color[][] colorTab, PixelWriter pw) {
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
}
