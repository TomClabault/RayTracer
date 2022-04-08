package raytracer.gui.materialChooser;

import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.concurrent.WorkerStateEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import raytracer.accelerationStructures.NoAccelerationStructure;
import raytracer.geometry.Shape;
import raytracer.geometry.shapes.Plane;
import raytracer.geometry.shapes.Sphere;
import raytracer.gui.threads.RenderTask;
import raytracer.materials.MatteMaterial;
import raytracer.materials.observer.ObservableConcreteMaterial;
import raytracer.materials.textures.ProceduralTextureCheckerboard;
import raytracer.maths.Point;
import raytracer.maths.Vector;
import raytracer.rayTracer.RayTracer;
import raytracer.rayTracer.RayTracerSettings;
import raytracer.scene.Camera;
import raytracer.scene.RayTracingScene;
import raytracer.scene.lights.LightBulb;
import raytracer.scene.lights.PositionnalLight;

public class MaterialChooserPreview extends GridPane
{
	private RayTracer rayTracer;
	private RayTracingScene rtScene;
	private RayTracerSettings settings;
	
	private WritableImage writablePreviewImage;
	
	private ObservableConcreteMaterial observableMaterial;
	
	private ExecutorService executorService;
	private Future<?> renderTaskFuture;
	
	private boolean previewPushed;
	
	private static final int PREVIEW_WIDTH = 192;
	private static final int PREVIEW_HEIGHT = 192;
	
	public MaterialChooserPreview(ObservableConcreteMaterial material)
	{
		super();
		
		this.rayTracer = new RayTracer(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		this.rtScene = createPreviewScene();
		this.rtScene.setAccelerationStructure(new NoAccelerationStructure(this.rtScene.getSceneObjects()));
		this.settings = new RayTracerSettings();
		this.settings.setRecursionDepth(10);
		this.settings.setBlurryReflectionsSampleCount(5);
		this.settings.setTileSize(16);

		this.writablePreviewImage = new WritableImage(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		this.executorService = Executors.newFixedThreadPool(1);
		this.renderTaskFuture = null;
		this.previewPushed = true;
		
		this.observableMaterial = material;
		
		ImageView imageView = new ImageView(this.writablePreviewImage);
		
		this.getChildren().add(imageView);
//		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private void drawImage(IntBuffer pixelBuffer, WritablePixelFormat<IntBuffer> pixelFormat) 
	{
    	this.writablePreviewImage.getPixelWriter().setPixels(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, pixelFormat, pixelBuffer, PREVIEW_WIDTH);
    }
	
	private RayTracingScene createPreviewScene()
    {
    	Camera cameraRT = new Camera(new Point(0, 0, 2), 0, 0, 40);
        PositionnalLight l = new LightBulb(new Point(0, 4, 4), 1);
    	
        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));
        
        shapeList.add(new Sphere(new Point(0, 0, -1), 1, new MatteMaterial(Color.RED)));
        
        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());

        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargee
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisee.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        return sceneRT;
    }
	
	public void updatePreview()
	{
		//On selectionne la sphere et on change son materiau
		this.rtScene.getSceneObjects().get(1).setMaterial(this.observableMaterial.getMaterial());
		
		RenderTask renderTask = new RenderTask(this.writablePreviewImage.getPixelWriter(), WritablePixelFormat.getIntArgbInstance(), rayTracer, rtScene, settings);
		renderTask.setOnSucceeded(this::pushPreview);
		
		if(this.renderTaskFuture == null || this.renderTaskFuture.isDone() && previewPushed)
		{
			previewPushed = false;
			
			this.renderTaskFuture = this.executorService.submit(renderTask);
		}
	}
	
	//TODO (tom) quand on fait juste un clic dans le color rect du color picker, juste après avoir lancé le programme
	//il y a du tearing dans la preview
	private void pushPreview(WorkerStateEvent event)
	{
		drawImage(this.rayTracer.getRenderedPixels(), WritablePixelFormat.getIntArgbInstance());
		previewPushed = true;
	}
}