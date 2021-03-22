package render;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import javax.imageio.ImageIO;

import geometry.shapes.*;
import geometry.*;
import geometry.materials.Material;
import geometry.materials.MatteMaterial;
import geometry.materials.MetallicMaterial;
import geometry.materials.MirrorMaterial;
import scene.*;
import scene.RayTracingScene;
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
		int width = 512;
		int height = 512;

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

		Camera cameraRT = new Camera(new Point(-1, 0.8, -3), new Point(0, 0, -6));
		cameraRT.setFOV(60);
		Light l = new LightBulb(new Point(-2, 2, -6), 1);

		ArrayList<Shape> shapeList = new ArrayList<>();
		shapeList.add(new PlaneMaths(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128))));
		
		//shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, new Material(Color.rgb(255, 0, 0), 0.4, 1, 0, 1, 256)));
		//shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, new MetallicMaterial(Color.RED)));
		shapeList.add(new SphereMaths(new Point(1.1, 0.5, -5.5), 0.2, new MatteMaterial(Color.rgb(255, 211, 0))));
		shapeList.add(new SphereMaths(new Point(-1.25, 1, -6.5), 0.2, new MatteMaterial(Color.LIGHTSKYBLUE)));
		shapeList.add(new SphereMaths(new Point(-1.25, -0.65, -5.5), 0.35, new MatteMaterial(Color.ORANGERED)));
		shapeList.add(new SphereMaths(new Point(-1, 1, -2.6), 0.35, new MatteMaterial(Color.rgb(255, 32, 32))));
		shapeList.add(new SphereMaths(new Point(1.5, -0.65, -5.5), 0.35, new MirrorMaterial(0.85)));
		shapeList.add(new SphereMaths(new Point(0, 0, -6), 1, new MirrorMaterial(0.85)));

		RayTracingScene sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 1);

		
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
		  WritableImage screenshot = scene.snapshot(writableImage);
		  
		  File output = new File("RenderOutput.png");
		  ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", output);
		  
		} catch (IOException ex) 
		{
		  ex.printStackTrace();
		}
	}
}
