package scene;

import java.net.URL;
import java.util.ArrayList;

import geometry.Shape;
import geometry.shapes.Icosphere;
import geometry.shapes.Plane;
import geometry.shapes.Sphere;
import geometry.shapes.Triangle;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import materials.GlassMaterial;
import materials.GlassyMaterial;
import materials.Material;
import materials.MatteMaterial;
import materials.MirrorMaterial;
import materials.RoughMaterial;
import materials.textures.ProceduralTextureCheckerboard;
import maths.ColorOperations;
import maths.Point;
import maths.Vector;
import scene.lights.LightBulb;
import scene.lights.PositionnalLight;

public class PredefinedScenes 
{
	/**
	 * @return Une scène disposant seulement d'un place, d'une caméra, d'une source de lumière et d'une skybox.
	 * Sert de scène de base à laquelle peuvent ensuite etre ajoutes des elements
	 */
	public static RayTracingScene createEmptyScene()
    {
    	Camera cameraRT = new Camera(new Point(0, 0, 2), 0, 0, 40);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));
        
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
    
	/**
	 * @return Une scène contenant une icosphere de subdivision et de matériau donne
	 */
    public static RayTracingScene createIcosphereScene(int subdivision, Material material)
    {
    	Camera cameraRT = new Camera(new Point(0.5, 0.5, 2), 0, 0, 40);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new Plane(new Vector(0, 1, 0), new Point(0, -1, 0), new MatteMaterial(Color.rgb(128, 128, 128), new ProceduralTextureCheckerboard(Color.rgb(32, 32, 32), Color.rgb(150, 150, 150), 1.0))));

        shapeList.add(new Icosphere(new Point(0, 0.5, -2), 1, subdivision, material));
        
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

        //sceneRT.addLight(new LightBulb(new Point(-2, 2.5, 1.440), 1));
        return sceneRT;
    }
    
    /**
     * @return Une scène composé de triangles disposés de façon à pouvoir débugguer la BVH
     */
    public static RayTracingScene createBVHTestingScene()
    {
    	Camera cameraRT = new Camera(new Point(3, -1, 3), 0, 0, 45);
        PositionnalLight l = new LightBulb(new Point(2, 2, 1), 1);

        ArrayList<Shape> shapeList = new ArrayList<>();

        shapeList.add(new Triangle(new Point(0, 0, -1), new Point(1, 0, -1), new Point(0, 1, -1), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(1.5, 0, -1), new Point(2.5, 0, -1), new Point(2.5, 1, -1), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(0, -1.5, -1), new Point(1, -1.5, -1), new Point(0, -0.5, -1), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(1.5, -1.5, -1), new Point(2.5, -1.5, -1), new Point(1.5, -0.5, -1), new MatteMaterial(Color.RED)));
        
        shapeList.add(new Triangle(new Point(0, 0, -2), new Point(1, 0, -2), new Point(0, 1, -2), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(1.5, 0, -2), new Point(2.5, 0, -2), new Point(2.5, 1, -2), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(0, -1.5, -2), new Point(1, -1.5, -2), new Point(0, -0.5, -2), new MatteMaterial(Color.RED)));
        shapeList.add(new Triangle(new Point(1.5, -1.5, -2), new Point(2.5, -1.5, -2), new Point(1.5, -0.5, -2), new MatteMaterial(Color.RED)));
        
        
        
        
        
        RayTracingScene sceneRT = null;
        
        sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        
        return sceneRT;
    }
    
    /**
     * La methode de test pour ajouter des elements 3d a une scene
     * @return RayTracingScene contenant les elements 3d
     */
    public static RayTracingScene generateUsualScene()
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
        catch (IllegalArgumentException exception)//Skybox mal chargee
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisee.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        sceneRT.addLight(new LightBulb(new Point(-2, 2.5, 1.440), 1));
        return  sceneRT;
    }

    /**
     * Genere une 3d avec des elements pour visualiser la roughness
     * 
     * @return RaytracingScene contenant des elements utilisant roughness
     */
    public static RayTracingScene generateRoughnessDemoScene()
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
        catch (IllegalArgumentException exception)//Skybox mal chargee
        {
        	System.err.println(exception.getMessage() + System.lineSeparator() + "Aucune skybox ne sera utilisee.");
        	sceneRT = new RayTracingScene(cameraRT, l, shapeList, Color.rgb(32, 32, 32), 0.1);
        }

        return  sceneRT;
    }
}
