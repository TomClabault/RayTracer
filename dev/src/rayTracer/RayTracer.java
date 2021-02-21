package rayTracer;

import java.util.ArrayList;

import geometry.Point;
import geometry.Ray;
import geometry.Shape;
import geometry.Vector;
import geometry.shapes.SphereMaths;
import javafx.scene.paint.Color;
import scene.Camera;
import scene.MyScene;
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
	
	public RayTracer(int renderLength, int renderHeight)
	{
		this.renderLength = renderLength;
		this.renderHeight = renderHeight;
		
		renderedPixels = new Color[renderHeight][renderLength];
	}
	
//	public Shape computeClosestInterObj(ArrayList<Point> intersectionPointList, Point intersectionPoint, ArrayList<Shape> intersectedObjects)
//	{
//		for(int i = 0; i < intersectionPointList.size(); i++)
//			if(intersectionPointList.get(i).equals(intersectionPoint))
//				return intersectedObjects.get(i);
//		
//		return null;//Normalement impossible
//	}
//	
//	Point computeClosestIntersection(ArrayList<Point> intersectionPointList, Camera camera) 
//	{
//		Double min = null;
//		
//		Point closestPoint = null;
//		
//		for(Point point : intersectionPointList)
//		{
//			double cameraPointDist = Point.distance(point, camera.getPosition());
//			if(min == null || cameraPointDist < min)
//			{
//				min = cameraPointDist;
//				closestPoint = point;
//			}
//		}
//		
//		return closestPoint;
//	}
	
	/*
	 * Calcule tous les pixels de la scène donnée en argument et retourne un tableau de couleur RGB correspondant aux pixels
	 * 
	 * @param renderScene La scène de rendu contenant les informations pour rendre l'image
	 * 
	 * @return Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength
	 */
	public Color[][] computeImage(MyScene renderScene)
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
	public Color computePixel(MyScene renderScene, Ray ray)
	{
		ArrayList<Shape> objectsList = renderScene.getSceneObjects();
		
		ArrayList<Point> intersectionPoints = new ArrayList<>();
		ArrayList<Shape> intersectedObjects = new ArrayList<>();
		
		for(Shape object : objectsList)
		{
			Point inter = object.intersect(ray);//On calcule le point d'intersection
			if(inter != null)//Si il y a effectivement un point d'intersection
			{
				intersectionPoints.add(object.intersect(ray));//On l'ajoute à la liste des points d'intersection trouvés
				intersectedObjects.add(object);//On ajoute l'objet qui correspond au point d'intersection. i.e. l'objet qui a été intersecté
			}
		}
			
			
		if(intersectionPoints.size() > 0)//Il y a au moins un point d'intersection
		{
			Point closestIntersectionPoint = new Point(0, 0, 0);//On crée une instance de point qui va nous permettre de garder le point d'intersection le plus proche de la caméra parmis tous les objets intersectés
			Shape closestIntersectedObject = detClosestInterPointObj(renderScene, intersectionPoints, intersectedObjects, closestIntersectionPoint);
			
			
			
			
			Vector normalAtIntersection = closestIntersectedObject.getNormal(closestIntersectionPoint);//On calcule la normale au point d'intersection avec la forme
			Point interPointShift = Point.add(closestIntersectionPoint, Point.scalarMul(0.0001d, Vector.v2p(normalAtIntersection)));//On ajoute un très léger décalage au point d'intersection pour quand le retirant vers la lumière, il ne réintersecte
			
			Vector shadowRayDir = new Vector(interPointShift, renderScene.getLight().getCenter());//On calcule la direction du rayon secondaire
			Ray shadowRay = new Ray(interPointShift, shadowRayDir, true);//Création du rayon secondaire avec pour origine le premier point d'intersection décalé et avec comme direction le centre de la lampe
			Point shadowRayInter = null;
			
			//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
			for(Shape objectAgain : objectsList)
			{
				shadowRayInter = objectAgain.intersect(shadowRay);
				if(shadowRayInter != null)
					break;
			}

			if(shadowRayInter == null)//Pas d'intersection, on retourne la pleine lumière
			{
				int objectRed = (int)closestIntersectedObject.getColor().getRed()*255;
				int objectGreen = (int)closestIntersectedObject.getColor().getGreen()*255;
				int objectBlue = (int)closestIntersectedObject.getColor().getBlue()*255;
				
				
				
				double lightIntensity = renderScene.getLight().getIntensity();
			
				double ambientTerm = lightIntensity * renderScene.getAmbientLightIntensity();
				double diffuseTerm = lightIntensity*closestIntersectedObject.getDiffuse()*Vector.dotProduct(shadowRayDir, normalAtIntersection);
				
				Vector refVector = Vector.normalize(this.getReflectionVector(normalAtIntersection, shadowRayDir));
				double spec2 = Math.pow(Vector.dotProduct(ray.negate(), refVector), closestIntersectedObject.getSpecular());
				double specularTerm = lightIntensity*spec2;
				
				double phongShadingCoeff = ambientTerm + diffuseTerm + specularTerm;
				
				
				
				//On calcule la couleur de chacune des composantes en fonction de la couleur de l'objet et de l'ombrage de Phong. On ramène les valeurs à 255 si elles sont supérieures à 255.
				int pixelRed = (int)(objectRed * phongShadingCoeff); pixelRed = pixelRed > 255 ? 255 : pixelRed;
				int pixelGreen = (int)(objectGreen * phongShadingCoeff); pixelGreen = pixelGreen > 255 ? 255 : pixelGreen;
				int pixelBlue = (int)(objectBlue * phongShadingCoeff); pixelBlue = pixelBlue > 255 ? 255 : pixelBlue;
				return Color.rgb(pixelRed, pixelGreen, pixelBlue);
			}
			else//Une intersection a été trouvée, on retourne donc un pixel d'ombre sombre
				return closestIntersectedObject.getColor().darker();
		}
		else//Le rayon n'a rien intersecté --> noir
			return Color.rgb(0, 0, 0);//Couleur du fond, noir si on a pas de fond
	}
	
	/*
	 * Convertit les coordonnées d'un pixel sur l'image (1920x1080 par exemple) en coordonnées 3D dans la scène à rendre
	 * 
	 * @param camera La caméra selon laquelle on souhaite obtenir les coordonnées du pixel
	 * @param x Coordonnées x du pixel sur l'image (de 0 à 1919 pour une résolution de 1920 de large par exemple)
	 * @param y Coordonnées y du pixel sur l'image (de 0 à 1079 pour une résolution de 1080 de haut par exemple)
	 * 
	 * @return Un point de cooordonnées (x, y, z) tel que x, y et z représentent les coordonnées du pixel dans la scène
	 */
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
		
		return new Point(xWorld, yWorld, camera.getDirection().getZ());
	}
	
	public Shape detClosestInterPointObj(MyScene renderScene, ArrayList<Point> intersectionPoints, ArrayList<Shape> intersectedObjects, Point outClosestInterPoint)
	{
		Shape closestIntersectedObject = null;
		
		if(intersectionPoints.size() > 1)//Il y plus d'un point d'intersection, on va devoir calculer le quel est le plus proche
		{
			Double min = null;
			for(int i = 0; i < intersectionPoints.size(); i++)
			{
				Point point = intersectionPoints.get(i);
				double distPointCam = Point.distance(point,  renderScene.getCamera().getPosition());
				
				if(min == null || min > distPointCam)
				{
					min = distPointCam;
					
					outClosestInterPoint.copyIn(point);
					closestIntersectedObject = intersectedObjects.get(i);
				}
			}
		}
		else//Il n'y a qu'un seul point d'intersection donc on peut récupérer le premier élément de la liste des points d'intersection
		{
			outClosestInterPoint.copyIn(intersectionPoints.get(0));
			closestIntersectedObject = intersectedObjects.get(0);
		}
		
		return closestIntersectedObject;
	}
	
	/*
	 * Calcule le rayon réfléchi par la surface en fonction de la position de la lumière par rapport au point d'intersection
	 * 
	 * @param normalToSurface N, le vecteur normal normalisé de la surface au point d'intersection
	 * @param intersectToLightVec L, le vecteur normalisé d'origine le point d'intersection et de direction la source de lumière
	 * 
	 * @return R, le vecteur d'origine le point d'intersection et de direction la direction de réflexion calculée par cette méthode 
	 */
	public Vector getReflectionVector(Vector normalToSurface, Vector intersectToLightVec)
	{
		return Vector.sub(Vector.scalarMul(normalToSurface, 2*Vector.dotProduct(intersectToLightVec, normalToSurface)), intersectToLightVec.negate());//2*(L.N)*N-L
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
