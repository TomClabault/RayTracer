package raytracer.gui;

import java.io.File;
import java.net.URL;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import raytracer.exceptions.InvalidParallelepipedException;
import raytracer.exceptions.InvalidSphereException;
import raytracer.geometry.ArbitraryTriangleShape;
import raytracer.geometry.shapes.Triangle;
import raytracer.gui.materialChooser.MaterialChooserWindow;
import raytracer.materials.Material;
import raytracer.maths.Vector;
import raytracer.parsers.plyParser.PlyParser;
import raytracer.parsers.povParser.PovAutomat;
import raytracer.scene.PredefinedScenes;
import raytracer.scene.RayTracingScene;

public class MainUtil 
{
	/**
	 * Ajoute la skybox par défaut à la scène et retourne la nouvelle scène ayant la skybox
	 * 
	 * @param rtScene La scène à laquelle ajouter la skybox par défaut
	 * 
	 * @return La scène avec la skybox
	 */
	public static RayTracingScene addSkybox(RayTracingScene rtScene)
	{
		if(!rtScene.hasSkybox())
		{
			Image skybox = null;
			URL skyboxURL = RayTracingScene.class.getResource("resources/skybox.jpg");
			if(skyboxURL != null)
				skybox = new Image(skyboxURL.toExternalForm());
			
			rtScene.setSkybox(skybox);
		}
		
		return rtScene;
	}
	
	/**
     * Ouvre un explorateur de fichier et demande a l'utilisateur de choisir un fichier d'une ou plusieurs
     * extensions autorisees par 'extensions' 
     * 
     * @param stage La fenetre mere de l'explorateur de fichier
     * @param description La description des types de fichiers acceptes
     * @param extensions La liste des extensions de fichiers qui seront autorisees pour le choix du fichier
     * 
     * @return Le fichier choisi par l'utilisateur. Null si aucun fichier n'a ete choisi
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
    
    public static String getFileExtension(File file)
    {
    	String fileName = file.getName();
    	if(fileName.lastIndexOf('.') != -1)
    		return fileName.substring(fileName.lastIndexOf('.'));
    	
    	return "";
    }
    
    
    /**
     * Cette méthode s'occupe de demander à l'utilisateur de choisir le fichier contenant la scène qu'il veut rendre
     * 
     * @param stage Le stage depuis lequel seront ouverts les explorateurs de fichier
     *  
     * @return La scène représentée par le fichier  
     */
    public static RayTracingScene getSceneFromFile(Stage stage)
    {
    	RayTracingScene rtScene = null;
    	
    	File fileChosen = chooseFile(stage, "POV, PLY", "*.pov", "*.ply");
        
    	if(fileChosen == null)//L'utilisateur n'a pas choisi de fichier / a annule
    	{
    		Platform.exit();
    		System.exit(0);
    	}
    	
	    try
	   	{
	   		String fileExtension = getFileExtension(fileChosen);
	   		
	   		if(fileExtension.equals(".pov"))
	   			rtScene = PovAutomat.parsePov(fileChosen);
	   		else if(fileExtension.equals(".ply"))
	   		{
	   			rtScene = PredefinedScenes.createEmptyScene();
	   			
	   			MaterialChooserWindow matChoose = new MaterialChooserWindow();
	   			Material material = matChoose.chooseMaterial();

	   			PlyParser plyParser = new PlyParser(material, 4, new Vector(0, -0.5, 0));
	   			ArbitraryTriangleShape plyFileShape = plyParser.parsePly(fileChosen);
	   			plyFileShape.getTriangleList().trimToSize();
	   			
	   			for(Triangle triangle : plyFileShape.getTriangleList())
	   				rtScene.addShape(triangle);
	   		}
	   	}
	   	catch(InvalidParallelepipedException recExc)
	   	{
	   		System.out.println("Le rendu de la scene ne peut pas etre effectue dû a un parallelepipede incorrect.");
	
	   		Platform.exit();
	   		System.exit(0);
	   	}
	   	catch(InvalidSphereException sphereExc)
	   	{
	   		System.out.println("Le rendu de la scene ne peut pas etre effectue dû a une sphere incorrecte.");
	
	   		Platform.exit();
	   		System.exit(0);
	   	}
	    
	    return rtScene;
    }
}