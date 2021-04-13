package scene;

import java.util.ArrayList;

import geometry.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import scene.lights.PositionnalLight;
import util.ImageUtil;

/*
 * Permet de représenter une scène de rendu contenant une caméra, une lumière et une liste de formes
 */
public class RayTracingScene
{
	private Camera camera;
	private PositionnalLight light;

	private ArrayList<Shape> shapes;

	private Image skyboxTexture;
	private PixelReader skyboxPixelReader;
	private int skyboxWidth;
	private int skyboxHeight;
	
	private Color backgroundColor;
	private double ambientLightIntensity;
	
	/*
	 * Crée une scène vide
	 */
	public RayTracingScene()
	{
		this(null, null, new ArrayList<Shape>(), Color.rgb(0, 0, 0), 0.1);
	}


	/*
	 * Crée la scène à partir d'une caméra, d'une lumière et d'une liste de forme
	 * 
	 * @param camera La camera de la scène à travers laquelle le rendu sera fait
	 * @param light La lumière permettant d'illuminer la scène
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisée pour la scène. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scène
	 * @param ambientLightIntensity L'intensité de la luminosité ambiante de la scène. Défini l'intensité lumineuse minimale par laquelle seront éclairés tous les points de la scène
	 */
	public RayTracingScene(Camera camera, PositionnalLight light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity) 
	{
		this(camera, light, shapes, backgroundColor, ambientLightIntensity, (Image)null);
	}
	
	/*
	 * Crée la scène à partir d'une caméra, d'une lumière et d'une liste de forme
	 * 
	 * @param camera La camera de la scène à travers laquelle le rendu sera fait
	 * @param light La lumière permettant d'illuminer la scène
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisée pour la scène. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scène. Si le paramètre skyboxTexture est utilisé, le paramètre backgroundColor sera ignoré
	 * @param ambientLightIntensity L'intensité de la luminosité ambiante de la scène. Défini l'intensité lumineuse minimale par laquelle seront éclairés tous les points de la scène
	 * @param skyboxTexturePath Chemin vers la texture de la skybox a utiliser
	 */
	public RayTracingScene(Camera camera, PositionnalLight light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, String skyboxTexturePath) 
	{
		this(camera, light, shapes, backgroundColor, ambientLightIntensity, new Image(skyboxTexturePath));
	}
	
	/*
	 * Crée la scène à partir d'une caméra, d'une lumière et d'une liste de forme
	 * 
	 * @param camera La camera de la scène à travers laquelle le rendu sera fait
	 * @param light La lumière permettant d'illuminer la scène
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisée pour la scène. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scène. Si le paramètre skyboxTexture est utilisé, le paramètre backgroundColor sera ignoré
	 * @param ambientLightIntensity L'intensité de la luminosité ambiante de la scène. Défini l'intensité lumineuse minimale par laquelle seront éclairés tous les points de la scène
	 * @param skyboxTexture javafx.scene.image.Image chargé de la texture a utiliser pour la skybox de la scène. null si aucune skybox n'est voulue
	 * 
	 * @throws IllegalArgumentException quand l'argument skyboxTexture passé ne constitue pas une image correcte. i.e. l'image n'a peut être pas été ouverte correctement, introuvable, format non supporté, ...
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
		this.light = light;
		this.shapes = shapes;

		this.backgroundColor = backgroundColor;
		this.ambientLightIntensity = ambientLightIntensity;
	}
	
	/*
	 * Permet d'ajouter un objet à la scène
	 * 
	 * @param shape La forme à ajouter
	 */
	public void addShape(Shape shape)
	{
		this.shapes.add(shape);
	}
	
	/*
	 * Permet d'obtenir l'intensité de la lumière ambiante de la scène
	 * 
	 * @return L'intensité de la lumière ambiante de la scène, un réel entre 0 et 1
	 */
	public double getAmbientLightIntensity() 
	{
		return this.ambientLightIntensity;
	}

	/*
	 * Permet d'obtenir la couleur du fond de la scène
	 * 
	 * @return La couleur du fond de la scène sous la forme d'un objet Color
	 */
	public Color getBackgroundColor()
	{
		return this.backgroundColor;
	}
	
	/*
	 * Retourne la caméra de la scène
	 * 
	 * @return La caméra de la scène
	 */
	public Camera getCamera() 
	{
		return this.camera;
	}

	/*
	 * Retourne la source de lumière de la scène
	 * 
	 * @return La source de lumière de la scène
	 */
	public PositionnalLight getLight() 
	{
		return this.light;
	}

	/*
	 * Permet d'obtenir la liste des formes de la scène
	 * 
	 * @return Une ArrayList<Shape> contenant toutes les formes de la scène
	 */
	public ArrayList<Shape> getSceneObjects() 
	{
		return this.shapes;
	}
	
	/*
	 * Permet d'obtenir la hauteur de la texture de la skybox
	 * 
	 * @return Un entier représentant la hauteur de la texture de la skybox
	 */
	public int getSkyboxHeight()
	{
		return this.skyboxHeight;
	}
	
	/*
	 * Permet d'obtenir la largeur de la texture de la skybox
	 * 
	 * @return Un entier représentant la largeur de la texture de la skybox
	 */
	public int getSkyboxWidth()
	{
		return this.skyboxWidth;
	}
	
	/*
	 * Permet d'obtenir le pixelReader de la texture de la skybox. Utile pour récupérer la couleur d'un pixel donné de la texture
	 * 
	 * @return Retourne une nouvelle instance d'un PixelReader sur la texture de la skybox
	 */
	public PixelReader getSkyboxPixelReader()
	{
		return this.skyboxPixelReader;
	}
	
	/*
	 * Permet de déterminer si la scène possède une skybox ou non
	 * 
	 * @return Retourne true si la scène a une skybox, false sinon.
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

	public void setLight(PositionnalLight light) 
	{
		this.light = light;
	}

	public void setShapes(ArrayList<Shape> shapes) 
	{
		this.shapes = shapes;
	}
	
	/*
	 * Permet d'attribuer une skybox à la scène
	 * 
	 * @param skyboxTexture L'image de la skybox a utiliser pour le rendu de la scène
	 * 
	 * @throws IllegalArgumentException Si 'skyboxTexture' ne peut pas être chargé correctement
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
		
		output += ("Camera: " + camera.toString() + System.lineSeparator());
		output += ("Light: " + light.toString() + System.lineSeparator());
		output += ("Ambient light: " + ambientLightIntensity + System.lineSeparator());
		output += ("Formes: " + System.lineSeparator());
		for(Shape object : this.shapes)
			output += (object.toString() + System.lineSeparator());
		
		return output;
	}
}
