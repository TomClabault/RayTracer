package scene;

import java.util.ArrayList;

import geometry.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import scene.lights.Light;
import util.ImageUtil;

/*
 * Permet de représenter une scène de rendu contenant une caméra, une lumière et une liste de formes
 */
public class RayTracingScene
{
	private Camera camera;
	private Light light;

	private ArrayList<Shape> shapes;

	private Image skyboxTexture;
	private PixelReader skyboxPixelReader;
	private int skyboxWidth;
	private int skyboxHeight;
	
	private Color backgroundColor;
	private double ambientLightIntensity;
	
	/*
	 * Crée la scène à partir d'une caméra, d'une lumière et d'une liste de forme
	 * 
	 * @param camera La camera de la scène à travers laquelle le rendu sera fait
	 * @param light La lumière permettant d'illuminer la scène
	 * @param shapes Liste de forme qui seront rendues
	 * @param backgroundColor La couleur de fond qui sera utilisée pour la scène. Ce sera la couleur visible lorsqu'un rayon n'intersectera aucun objet de la scène
	 * @param ambientLightIntensity L'intensité de la luminosité ambiante de la scène. Défini l'intensité lumineuse minimale par laquelle seront éclairés tous les points de la scène
	 */
	public RayTracingScene(Camera camera, Light light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity) 
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
	public RayTracingScene(Camera camera, Light light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, String skyboxTexturePath) 
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
	 */
	public RayTracingScene(Camera camera, Light light, ArrayList<Shape> shapes, Color backgroundColor, double ambientLightIntensity, Image skyboxTexture) 
	{
		this.camera = camera;
		this.light = light;
		this.shapes = shapes;

		this.skyboxTexture = ImageUtil.sRGBImageToLinear(skyboxTexture, 2.2);
		this.backgroundColor = backgroundColor;
		this.ambientLightIntensity = ambientLightIntensity;
		
		if(skyboxTexture != null)
		{
			this.skyboxPixelReader = this.skyboxTexture.getPixelReader();
			this.skyboxWidth = (int)skyboxTexture.getWidth();
			this.skyboxHeight = (int)skyboxTexture.getHeight();
		}
		else
		{
			this.skyboxPixelReader = null;
			this.skyboxWidth = this.skyboxHeight = 0;
		}
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
	public Light getLight() 
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
}
