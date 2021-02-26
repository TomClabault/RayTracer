package render;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.imageio.ImageIO;

import geometry.shapes.*;
import geometry.*;

import scene.*;
import scene.MyScene;
import scene.lights.*;
import rayTracer.*;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
		int width = 1920;
		int height = 1080;

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

		Camera cameraRT = new Camera(new Point(1, 1, -2), new Point(0, 0, -6));
		cameraRT.setFOV(60);
		Light l = new LightBulb(new Point(0, 1, -4), 1.25);

		ArrayList<Shape> shapeList = new ArrayList<>();
		shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), Color.rgb(64, 64, 64)));
		
		shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, Color.rgb(204, 0, 0), 10, 0.5, 0.3, 0.75, 0.25));
		shapeList.add(new SphereMaths(new Point(0.5, 0, -1), 0.25, Color.BLACK, 3, 1, 0.6, 1, 0));
		shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, Color.RED, 80, 0.75, 1, 0.9, 0.5));
		shapeList.add(new SphereMaths(new Point(-1.5, 0.5, -5.5), 0.2, Color.LIGHTSKYBLUE, 80, 0.5, 0.3, 0.75, 0.25));
		shapeList.add(new SphereMaths(new Point(-1.5, -0.65, -5.5), 0.35, Color.ORANGERED, 1, 0.8, 0.05, 0.8, 0));
		shapeList.add(new SphereMaths(new Point(1.5, -0.65, -5), 0.35, Color.rgb(64, 64, 64), 128, 0.5, 1, 1, 0.65));//Mirror boi

		MyScene sceneRT = new MyScene(cameraRT, l, shapeList, Color.SKYBLUE, 0.55);

		
	
		long startTimer = System.currentTimeMillis();
		rayTracerInstance.renderImage(sceneRT, 8);
		long endTimer = System.currentTimeMillis();
		
		System.out.println(String.format("Render time: %dms", endTimer-startTimer));
		doImage(rayTracerInstance.getRenderedPixels(), height, width, pw);
		
		writeImageToDisk(scene, writableImage);
	}

	public void doImage(AtomicReferenceArray<Color> colorTab, int renderHeight, int renderWidth, PixelWriter pw) 
	{
		for (int i = 0; i < renderHeight; i++)
			for (int j = 0; j < renderWidth; j++)
				pw.setColor(j, i, colorTab.get(i*renderWidth + j));
	}
	
	public void writeImageToDisk(Scene scene, WritableImage writableImage)
	{
		//Source: https://stackoverflow.com/questions/34194427/javafx-2-save-crisp-snapshot-of-scene-to-disk
		
		try 
		{
		  WritableImage screnshot = scene.snapshot(writableImage);
		  
		  File output = new File("RenderOutput.png");
		  ImageIO.write(SwingFXUtils.fromFXImage(screnshot, null), "png", output);
		  
		} catch (IOException ex) 
		{
		  ex.printStackTrace();
		}
	}
}
