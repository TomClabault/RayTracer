package rayTracer;

import java.util.ArrayList;

import geometry.Point;
import geometry.Ray;
import geometry.Shape;
import geometry.Vector;
import javafx.scene.paint.Color;
import scene.Camera;
import scene.Scene;

/*
 * Une instance de RayTracer créée à partir de la largeur et de la hauteur du rendu voulu. Permet de générer les 
 */
public class RayTracer 
{
	private int renderHeight;
	private int renderLength;
	
	private boolean imageRendered;
	
	Color[][] renderedPixels;
	
	public RayTracer(int height, int length)
	{
		this.renderLength = length;
		this.renderHeight = height;
		
		renderedPixels = new Color[height][length];
	}
	
	/*
	 * Calcule tous les pixels de la scène donnée en argument et retourne un tableau de couleur RGB correspondant aux pixels
	 * 
	 * @param renderScene La scène de rendu contenant les informations pour rendre l'image
	 * 
	 * @return Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength
	 */
	public Color[][] computeImage(Scene renderScene)
	{
		for(int y = 0; y < this.renderHeight; y++)
		{
			for(int x = 0; x < this.renderLength; x++)
			{
				Point this.convPxCoToWorldCoords(renderScene.getCamera(), x, y);
			}
		}
		
		return renderedPixels;
	}
	
	/*
	 * Calcule la couleur d'un pixel grâce à un rayon
	 * 
	 * @param ray Le rayon qu'on veut tirer et dont on déduira la couleur d'un pixel
	 * 
	 * @return Une instance de Color.RGB(r, g, b)
	 */
	public Color computePixel(Scene renderScene, Ray ray)
	{
		Color pixel = Color.rgb(0, 0, 0);//On initialise le pixel à noir
		
		ArrayList<Shape> objectsList = renderScene.getSceneObjects();
		
		for(Shape object : objectsList)
		{
			Point intersectionPoint = object.intersect(ray);
			
			if(intersectionPoint != null)//Il y a un point d'intersection
			{
				Vector normalAtIntersection = object.getNormal(intersectionPoint);//On calcule la normale au point d'intersection avec la forme
				Point interPointShift = Point.add(intersectionPoint, Point.scalarMul(0.0001d, Vector.v2p(normalAtIntersection)));//On ajoute un très léger décalage au point d'intersection pour quand le retirant vers la lumière, il ne réintersecte
				
				Vector shadowRayDir = new Vector(interPointShift, renderScene.getLight().getCenter());//On calcule la direction du rayon secondaire
				
				Ray shadowRay = new Ray(shadowRayDir, interPointShift);//Création du rayon secondaire avec pour origine le premier point d'intersection décalé et avec comme direction le centre de la lampe
				
				//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
				for(Shape objectAgain : objectsList)
				{
					Point shadowRayInter = objectAgain.intersect(shadowRay);
					if(shadowRayInter == null)//Pas d'intersection, on retourne la pleine lumière
						return object.getColor(); 
					else//Une intersection a été trouvée, on retourne donc un pixel d'ombre tout noir
						return Color.rgb(0, 0, 0);
							
				}
			}
			else//Le rayon n'a rien intersecté --> noir
				return Color.rgb(0, 0, 0);//Couleur du fond, noir si on a pas de fond
		}
		
		return pixel;
	}
	
	public Point convPxCoToWorldCoords(Camera camera, int x, int y)
	{
		Point worldCoords;
		
		double xWorld = (double)x;
		double yWorld = (double)y;
		
		
		return worldCoords;
	}
	
	/*
	 * Permet d'obtenir le tableau de pixels correspondant à la dernière image rendue par le RayTracer
	 * Si aucune image n'a été rendue, renvoie null
	 * 
	 * @param Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength. Renvoie null si encore aucune image n'a été rendue
	 */
	public Color[][] getRenderedPixels()
	{
		if(this.imageRendered == false)
			return null;
		
		return this.renderedPixels;
	}
}
