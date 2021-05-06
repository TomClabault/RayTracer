package scene;

import java.util.ArrayList;

import accelerationStructures.AccelerationStructure;
import geometry.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import scene.lights.PositionnalLight;
import util.ImageUtil;

/**
 * Permet de representer une scene de rendu.<br>
 * Une scene doit contenir:
 * <ul>
 * 	<li> Une camera </li>
 * 	<li> Une ou plusieurs sources de lumiere </li>
 * 	<li> Une ou plusieurs formes </li>
 * </ul>
 * 
 * La scene peut optionellement contenir:
 * <ul>
 * 	<li> Une skybox </li>
 * </ul>
 */
public class RayTracingScene
{
	private Camera camera;
	private ArrayList<PositionnalLight> lights;

	private ArrayList<Shape> shapes;
	private AccelerationStructure accelerationStructure;
	
	private Image skyboxTexture;
	private PixelReader skyboxPixelReader;
	private int skyboxWidth;
	private int skyboxHeight;
	
	private Color backgroundColor;
	private double ambientLightIntensity;
	
	/*
	 * Cree une scene vide
	 */
	public RayTracingScene()
	{
		this(null, new ArrayList<PositionnalLight>(), new ArrayList<Shape>(), Color.rgb(0, 0, 0), 0.1);
	}


	/**
	 * Cree la scene a partir d'une camera, d'une lumiere et d'une liste de forme
	 * 
	 * @param camera La camera de la scene a travers laquelle le rendu sera fait
	 * @param light La lumiere permettant d'illuminer la scene
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisee pour la scene. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scene
	 * @param ambientLightIntensity L'intensite de la luminosite ambiante de la scene. Defini l'intensite lumineuse minimale par laquelle seront eclaires tous les points de la scene
	 */
	public RayTracingScene(Camera camera, PositionnalLight light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity) 
	{
		this(camera, light, shapes, backgroundColor, ambientLightIntensity, (Image)null);
	}
	
	/**
	 * Cree la scene a partir d'une camera, d'une lumiere et d'une liste de forme
	 * 
	 * @param camera La camera de la scene a travers laquelle le rendu sera fait
	 * @param lights Les sources de lumiere qui seront utilisees pour le rendu de la scene
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisee pour la scene. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scene
	 * @param ambientLightIntensity L'intensite de la luminosite ambiante de la scene. Defini l'intensite lumineuse minimale par laquelle seront eclaires tous les points de la scene
	 */
	public RayTracingScene(Camera camera, ArrayList<PositionnalLight> lights, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity) 
	{
		this(camera, lights, shapes, backgroundColor, ambientLightIntensity, (Image)null);
	}
	
	/**
	 * Cree la scene a partir d'une camera, d'une lumiere et d'une liste de forme
	 * 
	 * @param camera La camera de la scene a travers laquelle le rendu sera fait
	 * @param light La lumiere permettant d'illuminer la scene
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisee pour la scene. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scene. Si le parametre skyboxTexture est utilise, le parametre backgroundColor sera ignore
	 * @param ambientLightIntensity L'intensite de la luminosite ambiante de la scene. Defini l'intensite lumineuse minimale par laquelle seront eclaires tous les points de la scene
	 * @param skyboxTexturePath Chemin vers la texture de la skybox a utiliser
	 */
	public RayTracingScene(Camera camera, PositionnalLight light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, String skyboxTexturePath) 
	{
		this(camera, light, shapes, backgroundColor, ambientLightIntensity, new Image(skyboxTexturePath));
	}
	
	/**
	 * Cree la scene a partir d'une camera, d'une lumiere et d'une liste de forme
	 * 
	 * @param camera La camera de la scene a travers laquelle le rendu sera fait
	 * @param lights Liste des sources de lumiere qui illumineront la scene
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisee pour la scene. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scene. Si le parametre skyboxTexture est utilise, le parametre backgroundColor sera ignore
	 * @param ambientLightIntensity L'intensite de la luminosite ambiante de la scene. Defini l'intensite lumineuse minimale par laquelle seront eclaires tous les points de la scene
	 * @param skyboxTexture javafx.scene.image.Image charge de la texture a utiliser pour la skybox de la scene. null si aucune skybox n'est voulue
	 */
	public RayTracingScene(Camera camera, ArrayList<PositionnalLight> lights, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, Image skyboxTexture) 
	{
		this(camera, (PositionnalLight)null, shapes, backgroundColor, ambientLightIntensity, skyboxTexture);
		
		for(PositionnalLight light : lights)
			this.lights.add(light);
	}
	
	/**
	 * Cree la scene a partir d'une camera, d'une lumiere et d'une liste de forme
	 * 
	 * @param camera La camera de la scene a travers laquelle le rendu sera fait
	 * @param light La lumiere permettant d'illuminer la scene
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisee pour la scene. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scene. Si le parametre skyboxTexture est utilise, le parametre backgroundColor sera ignore
	 * @param ambientLightIntensity L'intensite de la luminosite ambiante de la scene. Defini l'intensite lumineuse minimale par laquelle seront eclaires tous les points de la scene
	 * @param skyboxTexture javafx.scene.image.Image charge de la texture a utiliser pour la skybox de la scene. null si aucune skybox n'est voulue
	 * 
	 * @throws IllegalArgumentException quand l'argument skyboxTexture passe ne constitue pas une image correcte. i.e. l'image n'a peut etre pas ete ouverte correctement, introuvable, format non supporte, ...
	 */
	public RayTracingScene(Camera camera, PositionnalLight light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, Image skyboxTexture) throws IllegalArgumentException
	{
		try
		{
			setSkybox(skyboxTexture);
		}
		catch (IllegalArgumentException e)
		{
			throw e;
		}
		
		this.camera = camera;
		this.lights = new ArrayList<PositionnalLight>();
		if(light != null)
			this.lights.add(light);
		this.shapes = shapes;

		this.backgroundColor = backgroundColor;
		this.ambientLightIntensity = ambientLightIntensity;
	}
	
	public AccelerationStructure getAccelerationStructure()
	{
		return this.accelerationStructure;
	}
	
	public void setAccelerationStructure(AccelerationStructure structure)
	{
		this.accelerationStructure = structure;
	}
	
	/**
	 * Permet d'ajouter une source de lumiere a la scene
	 * 
	 * @param light La source de lumiere a ajouter
	 */
	public void addLight(PositionnalLight light)
	{
		this.lights.add(light);
	}
	
	/**
	 * Permet d'ajouter un objet a la scene
	 * 
	 * @param shape La forme a ajouter
	 */
	public void addShape(Shape shape)
	{
		this.shapes.add(shape);
	}
	
	/**
	 * Permet d'obtenir l'intensite de la lumiere ambiante de la scene
	 * 
	 * @return L'intensite de la lumiere ambiante de la scene, un reel entre 0 et 1
	 */
	public double getAmbientLightIntensity() 
	{
		return this.ambientLightIntensity;
	}

	/**
	 * Permet d'obtenir la couleur du fond de la scene
	 * 
	 * @return La couleur du fond de la scene sous la forme d'un objet Color
	 */
	public Color getBackgroundColor()
	{
		return this.backgroundColor;
	}
	
	/**
	 * Retourne la camera de la scene
	 * 
	 * @return La camera de la scene
	 */
	public Camera getCamera() 
	{
		return this.camera;
	}

	/**
	 * Retourne la source de lumiere de la scene numero i
	 * 
	 * @param i l'indice de la source de lumiere que l'on veut recuperer
	 * 
	 * @return La source de lumiere de la scene
	 */
	public PositionnalLight getLight(int i) 
	{
		return this.lights.get(i);
	}
	
	/**
	 * Retourne la liste des sources de lumiere de la scene
	 * 
	 * @return Les sources de lumiere de la scene
	 */
	public ArrayList<PositionnalLight> getLights() 
	{
		return this.lights;
	}

	/**
	 * Permet d'obtenir la liste des formes de la scene
	 * 
	 * @return Une {@link java.util.ArrayList} of {@link geometry.Shape} contenant toutes les formes de la scene
	 */
	public ArrayList<Shape> getSceneObjects() 
	{
		return this.shapes;
	}
	
	/**
	 * Permet d'obtenir la hauteur de la texture de la skybox
	 * 
	 * @return Un entier representant la hauteur de la texture de la skybox
	 */
	public int getSkyboxHeight()
	{
		return this.skyboxHeight;
	}
	
	/**
	 * Permet d'obtenir la largeur de la texture de la skybox
	 * 
	 * @return Un entier representant la largeur de la texture de la skybox
	 */
	public int getSkyboxWidth()
	{
		return this.skyboxWidth;
	}
	
	/**
	 * Permet d'obtenir le pixelReader de la texture de la skybox. Utile pour recuperer la couleur d'un pixel donne de la texture
	 * 
	 * @return Retourne une nouvelle instance d'un PixelReader sur la texture de la skybox
	 */
	public PixelReader getSkyboxPixelReader()
	{
		return this.skyboxPixelReader;
	}
	
	/**
	 * Permet de determiner si la scene possede une skybox ou non
	 * 
	 * @return Retourne true si la scene a une skybox, false sinon.
	 */
	public boolean hasSkybox()
	{
		return !(this.skyboxTexture == null);
	}
	
	public void setAmbientLightIntensity(double ambientLightIntensity) 
	{
		this.ambientLightIntensity = ambientLightIntensity;
	}
	
	public void setBackgroundColor(Color backgroundColor) 
	{
		this.backgroundColor = backgroundColor;
	}
	
	public void setCamera(Camera camera) 
	{
		this.camera = camera;
	}

	public void setLights(ArrayList<PositionnalLight> lights) 
	{
		this.lights = lights;
	}

	public void setShapes(ArrayList<Shape> shapes) 
	{
		this.shapes = shapes;
	}
	
	/**
	 * Permet d'attribuer une skybox a la scene
	 * 
	 * @param skyboxTexture L'image de la skybox a utiliser pour le rendu de la scene
	 * 
	 * @throws IllegalArgumentException Si 'skyboxTexture' ne peut pas etre charge correctement
	 */
	public void setSkybox(Image skyboxTexture) throws IllegalArgumentException
	{
		if(skyboxTexture != null)
		{
			if(skyboxTexture.isError())
				throw new IllegalArgumentException("Erreur durant le chargement de la skybox : " + skyboxTexture.getException().getMessage());
			else
			{
				this.skyboxTexture = ImageUtil.sRGBImageToLinear(skyboxTexture);
				this.skyboxPixelReader = this.skyboxTexture.getPixelReader();
				
				this.skyboxWidth = (int)skyboxTexture.getWidth();
				this.skyboxHeight = (int)skyboxTexture.getHeight();
			}
		}
		else
		{
			this.skyboxPixelReader = null;
			
			this.skyboxWidth = 0;
			this.skyboxHeight = 0;	
		}
	}
	
	@Override
	public String toString()
	{
		String output = "";
		
		output += ("Camera: " + (camera == null ? "null" : camera.toString()) + System.lineSeparator());
		output += ("Light: " + lights.toString() + System.lineSeparator());
		output += ("Ambient light: " + ambientLightIntensity + System.lineSeparator());
		output += ("Formes: " + System.lineSeparator());
		for(Shape object : this.shapes)
			output += (object.toString() + System.lineSeparator());
		
		return output;
	}
}
