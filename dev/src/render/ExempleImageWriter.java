package render;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

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
import multithreading.ThreadsTaskList;
import multithreading.TileThread;
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
		int width = 1280;
		int height = 720;

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

		Camera cameraRT = new Camera(new Point(-2, 0, -3), new Point(0, 0, -6));
		cameraRT.setFOV(60);
		Light l = new LightBulb(new Point(0, 0, -4), 1);

		ArrayList<Shape> shapeList = new ArrayList<>();
		shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), Color.rgb(125, 125, 125)));
		shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, Color.CHOCOLATE, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(-1.1, 0, -6), 0.1, Color.BLACK, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(1.1, 0, -6), 0.1, Color.WHITE, 128, 0.5, 0.5));
		shapeList.add(new SphereMaths(new Point(0.5, 1, -5), 0.25, Color.DIMGREY, 80, 1, 0.5));
		shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, Color.DARKCYAN, 80, 1, 0.5));
		shapeList.add(new SphereMaths(new Point(5, -1, 0), 0.2, Color.ORANGERED, 80, 1, 0.5));

		MyScene sceneRT = new MyScene(cameraRT, l, shapeList,Color.LIGHTSKYBLUE, 1);

		
		
		
		
		
		
		int nbCore = 16;
		
		long start = System.currentTimeMillis();
		for(int j = 0; j < 1; j++)
		{
			ThreadsTaskList threadTaskList = new ThreadsTaskList();
			threadTaskList.initTaskList(nbCore, width, height);
			
			for(int i = 1; i < nbCore; i++)
			{
				new Thread(new TileThread(threadTaskList, rayTracerInstance, sceneRT), String.format("Test boi %d", i)).start();
			}
				
			while(threadTaskList.getTotalTaskFinished() < threadTaskList.getTotalTaskCount())
				rayTracerInstance.computeTask(sceneRT, threadTaskList);
			assert threadTaskList.getTotalTaskFinished() == threadTaskList.getTotalTaskCount() : "totalTaskFinished != totalTaskCount";
			
		}
		long end = System.currentTimeMillis();
		System.out.println("Compute time: " + Long.toString(end - start) + "ms");

		doImage(rayTracerInstance.getRenderedPixels(), height, width, pw);
	}

	public void doImage(AtomicReferenceArray<Color> colorTab, int renderHeight, int renderWidth, PixelWriter pw) 
	{
		System.out.println("drawing");
		for (int i = 0; i < renderHeight; i++)
			for (int j = 0; j < renderWidth; j++)
				pw.setColor(j, i, colorTab.get(i*renderWidth + j));
	}
}
