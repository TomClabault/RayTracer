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
import maths.Point;
import maths.Vector;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;

//from www.java2s.com
public class ExempleImageWriter extends Application 
{

	public static void main(String[] args) 
	{
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) 
	{
		int width = 1777;
		int height = 1000;

		WritableImage writableImage = new WritableImage(width, height);

		PixelWriter pw = writableImage.getPixelWriter();

		ImageView imageView = new ImageView();
		imageView.setImage(writableImage);

		Pane root = new Pane();
		root.getChildren().add(imageView);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("");
		stage.show();


		
		RayTracer rayTracerInstance = new RayTracer(width, height);

		Camera cameraRT = new Camera(new Point(-3, 0, -3), new Point(0, 0, -6));
		cameraRT.setFOV(60);
		Light l = new LightBulb(new Point(0, 2, -4), 1);

		ArrayList<Shape> shapeList = new ArrayList<>();
		shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), Color.rgb(125, 125, 125)));
		shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, Color.CHOCOLATE, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(-1.1, 0, -6), 0.1, Color.BLACK, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(1.1, 0, -6), 0.1, Color.WHITE, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(0.5, 1, -5), 0.25, Color.DIMGREY, 80, 1, 0.5));
		shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, Color.DARKCYAN, 80, 1, 0.5));
		shapeList.add(new SphereMaths(new Point(5, -1, 0), 0.2, Color.ORANGERED, 80, 1, 0.5));

		MyScene sceneRT = new MyScene(cameraRT, l, shapeList,Color.LIGHTSKYBLUE, 1);

		doImage(rayTracerInstance.computeImage(sceneRT), pw);
	}

	public void doImage(Color[][] colorTab, PixelWriter pw) 
	{
		int width = colorTab[0].length;
		int height = colorTab.length;

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				pw.setColor(j, i, colorTab[i][j]);
	}
}
