package rayTracer;

import java.util.ArrayList;

import geometry.Point;
import geometry.Ray;
import geometry.Shape;
import geometry.Vector;
import geometry.shapes.SphereMaths;
import javafx.scene.paint.Color;
import scene.Camera;
import scene.Scene;
import scene.lights.Light;
import scene.lights.LightBulb;

/*
 * Une instance de RayTracer créée à partir de la largeur et de la hauteur du rendu voulu. Permet de générer les 
 */
public class RayTracer 
{
	private int renderHeight;
	private int renderLength;
	
	private boolean imageRendered;
	
	Color[][] renderedPixels;
	
	public static void main(String[] args)
	{
		RayTracer r = new RayTracer(1920, 1080);
		
		Camera c = new Camera();
		Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)));
		ArrayList<Shape> sphere = new ArrayList<>();
		sphere.add(new SphereMaths(new Point(0, 0, -4), 1));
		
		Scene s = new Scene(c, l, sphere);
		
		r.convPxCoToWorldCoords(s.getCamera(), 1919, 0);
	}
	
	public RayTracer(int renderLength, int renderHeight)
	{
		this.renderLength = renderLength;
		this.renderHeight = renderHeight;
		
		renderedPixels = new Color[renderHeight][renderLength];
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
				Point pixelWorldCoords = this.convPxCoToWorldCoords(renderScene.getCamera(), x, y);
				
				Ray cameraRay = new Ray(renderScene.getCamera().getPosition(), pixelWorldCoords);
				cameraRay.normalize();
				
				this.renderedPixels[y][x] = this.computePixel(renderScene, cameraRay);
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
		double xWorld = (double)x;
		double yWorld = (double)y;
		
		double aspectRatio = (double)this.renderLength / (double)this.renderHeight;
		double demiHeightPlane = Math.tan(Math.toRadians(camera.getFOV())/2);
		
		xWorld = (xWorld + 0.5) / this.renderLength;//Normalisation des pixels. Maintenant dans [0, 1]
		xWorld = xWorld * 2 - 1;//Décalage des pixels dans [-1, 1]
		xWorld *= aspectRatio;//Prise en compte de l'aspect ratio. Maintenant dans [-aspectRatio; aspectRatio]
		xWorld *= demiHeightPlane;
		
		yWorld = (yWorld + 0.5) / this.renderHeight;//Normalisation des pixels. Maintenant dans [0, 1]
		yWorld = 1 - yWorld * 2;//Décalage des pixels dans [-1, 1]
		yWorld *= demiHeightPlane;
		
		return new Point(xWorld, yWorld, camera.getPosition().getZ());
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
