package gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import accelerationStructures.BVHAccelerationStructure;
import accelerationStructures.NoAccelerationStructure;
import exceptions.InvalidParallelepipedException;
import exceptions.InvalidSphereException;
import geometry.ArbitraryTriangleShape;
import geometry.Shape;
import geometry.shapes.Icosphere;
import geometry.shapes.Plane;
import geometry.shapes.Sphere;
import gui.threads.RefreshSimpleRenderThread;
import gui.threads.RenderTask;
import gui.toolbox.SimpleRenderToolbox;
import gui.toolbox.Toolbox;
import gui.windows.ChooseRenderSettingsWindow;
import gui.windows.RenderWindow;
import gui.windows.RenderWindowOld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import materials.GlassMaterial;
import materials.GlassyMaterial;
import materials.MatteMaterial;
import materials.MetallicMaterial;
import materials.MirrorMaterial;
import materials.RoughMaterial;
import materials.textures.ProceduralTextureCheckerboard;
import maths.ColorOperations;
import maths.Point;
import maths.Vector;
import parsers.plyParser.PlyParser;
import parsers.povParser.PovAutomat;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.Camera;
import scene.RayTracingScene;
import scene.lights.LightBulb;
import scene.lights.PositionnalLight;

/**
 * La classe contenant le Main qui gère la totalité de l'application
*/
public class MainApp extends Application {
    /**
     * La hauteur de la résolution de la fenêtre du rendu définie par {@link gui.windows.ChooseRenderSettingsWindow}
    */
    public static int HEIGHT;
    /**
     * La largeur de la résolution de la fenêtre du rendu définie par {@link gui.windows.ChooseRenderSettingsWindow}
    */
    public static int WIDTH;

    /**
     * Vaut true si le mode automatique est activé
     *
     * Le mode automatique maximize la fenêtre de rendu et étire le rendu, le rendu devient pixélisé si la résolution du rendu est inférieur à la taille de la fenêtre
     */
    public static boolean FULLSCREEN_MODE;

    /**
     * True si le ray tracer ne doit rendre qu'une image et arrêter les calculs. False si le RayTracer doit rendre les images en temps
     * réel (autorise ainsi les mouvements de caméra)
     */
    public static boolean SIMPLE_RENDER;
    
    /**
     * Ouvre un explorateur de fichier et demande à l'utilisateur de choisir un fichier d'une ou plusieurs
     * extensions autorisées par 'extensions' 
     * 
     * @param stage La fenêtre mère de l'explorateur de fichier
     * @param description La description des types de fichiers acceptés
     * @param extensions La liste des extensions de fichiers qui seront autorisées pour le choix du fichier
     * 
     * @return Le fichier choisi par l'utilisateur. Null si aucun fichier n'a été choisi
     */
    public static File chooseFile(Stage stage, String description, String... extensions)
    {
	   	ExtensionFilter filter = new ExtensionFilter(description, extensions);
    	FileChooser fileChooser = new FileChooser();
    	
    	fileChooser.setInitialDirectory(new File("."));
	   	fileChooser.setTitle("Selectionnez un fichier");
	   	fileChooser.getExtensionFilters().add(filter);
	   	
	   	File file = fileChooser.showOpenDialog(stage);
	   	
	   	return file;
    }
    
    /**
     * La méthode main de java
     * @param args
     */
    public static void main(String[] args) {

        Application.launch(args);

    }
    
    public static String getFileExtension(File file)
    {
    	String fileName = file.getName();
    	if(fileName.lastIndexOf('.') != -1)
    		return fileName.substring(fileName.lastIndexOf('.'));
    	
    	return "";
    }
    
    /**
     * Contient la méthode à Override de {@link javafx.application.Application}
     * Elle est exécutée dans le main
     * @param stage Le stage de la fenêtre de rendu
     */
    public void start(Stage stage) 
    {
    	//TODO (tom) clean le main avec des fonctions --> commence à être le bordel
    	File fileChosen = chooseFile(stage, "POV, PLY", "*.pov", "*.ply");
    
    	if(fileChosen == null)//L'utilisateur n'a pas choisi de fichier / a annulé
    	{
    		Platform.exit();
    		System.exit(0);
    	}

	   	RayTracingScene rayTracingScene = new RayTracingScene();
	   	try
	   	{
	   		String fileExtension = getFileExtension(fileChosen);
	   		
	   		if(fileExtension.equals(".pov"))
	   			rayTracingScene = PovAutomat.parsePov(fileChosen);
	   		else if(fileExtension.equals(".ply"))
	   		{
	   			rayTracingScene = createBaselineScene();
	   			
//	   			PlyParser plyParser = new PlyParser(new RoughMaterial(ColorOperations.sRGBGamma2_2ToLinear(Color.web("D4AF37")), 0.75), 4);
//	   			ArbitraryTriangleShape plyFileShape = plyParser.parsePly(fileChosen);
//	   			
//	   			rayTracingScene.addShape(plyFileShape);
	   		}
	   	}
	   	catch(InvalidParallelepipedException recExc)
	   	{
	   		System.out.println("Le rendu de la scène ne peut pas être effectué dû à un parallélépipède incorrect.");

	   		Platform.exit();
	   		System.exit(0);
	   	}
	   	catch(InvalidSphereException sphereExc)
	   	{
	   		System.out.println("Le rendu de la scène ne peut pas être effectué dû à une sphère incorrecte.");

	   		Platform.exit();
	   		System.exit(0);
	   	}

	   	
	   	
	   	
	   	
	   	if(!rayTracingScene.hasSkybox())
	   	{
	   		Image skybox = null;
		    URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
		    if(skyboxURL != null)
		    	skybox = new Image(skyboxURL.toExternalForm());

		    rayTracingScene.setSkybox(skybox);
	   	}
	   	rayTracingScene.setAccelerationStructure(new BVHAccelerationStructure(rayTracingScene.getSceneObjects()));
	   	//rayTracingScene.setAccelerationStructure(new NoAccelerationStructure(rayTracingScene.getSceneObjects()));
	   	
        RayTracerSettings rayTracerSettings = new RayTracerSettings(Runtime.getRuntime().availableProcessors(), 4, 9, 4);
       
	   	ChooseRenderSettingsWindow renderSettingsWindow = new ChooseRenderSettingsWindow(rayTracerSettings);
        renderSettingsWindow.execute();
        
	   	RayTracer rayTracer = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);

        if(!MainApp.SIMPLE_RENDER)//On lance le rendu en temps réel s'il est désiré
        {
        	//TODO (tom) clean render window old
        	RenderWindowOld renderWindow = new RenderWindowOld(stage, rayTracer, rayTracingScene, rayTracerSettings);
        	renderWindow.execute();

        	Toolbox toolbox = new Toolbox(renderWindow.getStatPane(), renderWindow.getProgressBar(), rayTracerSettings, renderWindow.getWritableImage());
        	toolbox.execute();
        }
        else
        {
        	RenderWindow renderWindow = new RenderWindow(stage);
        	
        	RefreshSimpleRenderThread refreshRenderThread = new RefreshSimpleRenderThread(rayTracer, renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance(), renderWindow.getStatsPane());
        	refreshRenderThread.start();
        	
        	SimpleRenderToolbox saveRenderWindow = new SimpleRenderToolbox(renderWindow.getWritableImage(), renderWindow.getStatsPane());
        	
        	ExecutorService executorService = Executors.newFixedThreadPool(1);
        	RenderTask renderTask = new RenderTask(renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance(), rayTracer, rayTracingScene, rayTracerSettings);
        	executorService.submit(renderTask);
        	
        	renderTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
        	{
        		@Override
        		public void handle(WorkerStateEvent arg0) 
        		{
        			RenderWindowOld.drawImage(rayTracer.getRenderedPixels(), renderWindow.getPixelWriter(), PixelFormat.getIntArgbInstance());
        		}
			});
        }


        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
        	@Override
        	public void handle(WindowEvent e) {
        		Platform.exit();
        		System.exit(0);
        	}
        });
    }

    public RayTracingScene createBaselineScene()
    {
    	Camera cameraRT = new Camera(new Point(0.5, 0.5, 2), 0, 0, 40);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));

        shapeList.add(new Icosphere(new Point(0, 0.5, -2), 1, 3, new MirrorMaterial(0.1)));
        
        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());

        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargée
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisée.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        //sceneRT.addLight(new LightBulb(new Point(-2, 2.5, 1.440), 1));
        return sceneRT;
    }
    
    public RayTracingScene createTestingScene()
    {
    	Camera cameraRT = new Camera(new Point(0, 0, 0), 0, 0, 45);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();

        shapeList.add(new Sphere(new Point(0, 0, -3), 1, new MatteMaterial(Color.rgb(0, 0, 0))));
        
        RayTracingScene sceneRT = null;
        
        sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        
        return sceneRT;
    }
    
    /**
     * La méthode de test pour ajouter des éléments 3d à une scène
     * @deprecated Les éléments doivent désormais être importés à l'aide d'un fichier POV
     * @return RayTracingScene contenant les éléments 3d
     */
    public RayTracingScene generateUsualScene()
    {
    	Camera cameraRT = new Camera(new Point(0.000, 0.5, 0.320), 0, 0, 40);//Magic camera
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));

        shapeList.add(new Sphere(new Point(-1.25, 0.5, -6), 1, new MirrorMaterial(0.75)));
        shapeList.add(new Sphere(new Point(0, 1.5, -6), 0.5, new RoughMaterial(ColorOperations.sRGBGamma2_2ToLinear(Color.web("D4AF37")), 0.75)));
        shapeList.add(new Sphere(new Point(1.25, 0.5, -6), 1, new GlassMaterial()));

        shapeList.add(new Sphere(Point.translateMul(new Point(-0.3, 0.5, -0.1), new Vector(1.250, 0.000, -4.500), 1.5625), 0.2, new GlassyMaterial(Color.GREEN)));
        shapeList.add(new Sphere(new Point(-2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.BLACK, Color.YELLOW, 12))));
        shapeList.add(new Sphere(new Point(2, -0.65, -5), 0.35, new MatteMaterial(Color.BLACK, new ProceduralTextureCheckerboard(Color.RED, Color.DARKRED.darker(), 12))));

        shapeList.add(new Sphere(new Point(0, -0.5, -6), 0.5, new GlassyMaterial(Color.RED)));
        shapeList.add(new Sphere(new Point(-0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));
        shapeList.add(new Sphere(new Point(0.75, -0.75, -6), 0.25, new GlassyMaterial(Color.rgb(255, 64, 0))));


        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());

        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargée
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisée.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        sceneRT.addLight(new LightBulb(new Point(-2, 2.5, 1.440), 1));
        return  sceneRT;
    }

    /**
     * Génère une 3d avec des éléments pour visualiser la roughness
     * @return RaytracingScene contenant des éléments utilisant roughness
     * @deprecated Les éléments doivent désormais être importés en utilisant un fichier POV
     */
    public RayTracingScene generateRoughnessDemoScene()
    {
    	Camera cameraRT = new Camera(new Point(-2.000, 4, -1), new Point(-2, 0, -8), 40);
        PositionnalLight l = new LightBulb(new Point(-2, 6, 0), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));

        double roughnessTab[] = new double[] {0.5, 0.75, 0.9, 1};
        for(int y = 0; y < 4; y++)
        {
        	for(int x = 0; x < 4; x++)
        	{
        		Color sphereColor = ColorOperations.sRGBGamma2_2ToLinear(Color.web("D4AF37").interpolate(Color.rgb(32, 32, 32), 1.0/4.0*x));

                shapeList.add(new Sphere(new Point(-5 + x * 2, -0.5, -15 + y * 3), 0.5, new RoughMaterial(sphereColor, roughnessTab[y])));
        	}
        }

        Image skybox = null;
        URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
        if(skyboxURL != null)
        		skybox = new Image(skyboxURL.toExternalForm());

        RayTracingScene sceneRT = null;
        try
        {
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1, skybox);
        }
        catch (IllegalArgumentException exception)//Skybox mal chargée
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisée.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        return  sceneRT;
    }
}
