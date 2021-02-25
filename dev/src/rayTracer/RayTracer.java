package rayTracer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import geometry.Shape;
import javafx.scene.paint.Color;
import maths.MatrixD;
import maths.ColorOperations;
import maths.Point;
import maths.Ray;
import maths.Vector;
import multithreading.ThreadsTaskList;
import multithreading.TileTask;
import multithreading.TileThread;
import scene.MyScene;

/*
 * Lumière dans la sphère: traverse
 * Lumière en dessous du plan, traverse
 * Classe matériaux --> couleur, specular, diffuse
 */

/*
 * Une instance de RayTracer créée à partir de la largeur et de la hauteur du rendu voulu. Permet de générer les 
 */
public class RayTracer 
{
	private int renderHeight;
	private int renderWidth;
	
	AtomicReferenceArray<Color> renderedPixels;
	
	public RayTracer(int renderWidth, int renderHeight)
	{
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		
		this.renderedPixels = new AtomicReferenceArray<>(renderWidth*renderHeight);
		for(int i = 0; i < renderWidth*renderHeight; i++)
			this.renderedPixels.set(i, Color.rgb(255, 0, 0));
	}
	
	/*
	 * Calcule le premier point d'intersection du rayon passé en argument avec les objets de la scène
	 * 
	 * @param objectList Liste des objets de la scène. Obtenable avec MyScene.getSceneObjects()
	 * @param ray Rayon duquel chercher les points d'intersection avec les objets de la scène
	 * @param outClosestInterPoint
	 * 
	 * @return Retourne l'objet avec lequel le rayon a fait son intersection. 'outClosestInterPoint' est un point de l'objet retourné 
	 */
	public Shape computeClosestInterPoint(ArrayList<Shape> objectList, Ray ray, Point outClosestInterPoint)
	{
		Shape closestObjectIntersected = null;
		Double distanceMin = null;
		
		for(Shape object : objectList)
		{
			double distRayOriInter = -1;
			Point intersection = object.intersect(ray);
			if(intersection != null)
			{
				distRayOriInter = Point.distance(ray.getOrigin(), intersection);
			
				if(distanceMin == null || distRayOriInter < distanceMin)
				{
					distanceMin = distRayOriInter;
					
					outClosestInterPoint.copyIn(intersection);
					closestObjectIntersected = object;
				}
			}
		}
		
		return closestObjectIntersected;
	}
	
	/*
	 * Calcul l'intensité lumineuse diffuse en un point donné de l'image 
	 */
	public double computeDiffuse(Vector shadowRayDir, Vector normalAtIntersection, double lightIntensity)
	{
		double dotProdDiffuse = Vector.dotProduct(shadowRayDir, normalAtIntersection);
		double diffuseTerm = dotProdDiffuse < 0 ? 0 : lightIntensity*dotProdDiffuse;//Si le dotProduct est négatif, on ne calcule pas le terme diffus --> = 0
		
		return diffuseTerm;
	}
	
	public double computeSpecular(Ray incidentRay, Vector shadowRayDir, Vector normalAtIntersection, double lightIntensity, double objectShininess)
	{
		//Composante spéculaire
		double specularTerm = 0;
		Vector reflectVector = Vector.normalize(this.getReflectionVector(normalAtIntersection, shadowRayDir));
		
		double dotProdSpecular = Vector.dotProduct(reflectVector, incidentRay.negate());
		specularTerm = lightIntensity*Math.pow(Math.max(dotProdSpecular, 0), objectShininess);
		
		return specularTerm;
	}
	
	/*
	 * Calcule un partie de la scène représentée par un pixel de départ X et Y et un pixel d'arrivée X et Y. Le rectangle de pixel définit par ces valeurs est alors calculé
	 * 
	 * @param renderScene La scène de rendu contenant les informations pour rendre l'image
	 * @param startX Le pixel de départ horizontal de la zone de l'image qui doit être calculée. Entre 0 et renderWidth - 1 inclus
	 * @param startY Le pixel de départ vertical de la zone de l'image qui doit être calculée. Entre 0 et renderHeight- 1 inclus
	 * @param endX Le pixel de fin horizontal de la zone de l'image qui doit être calculée. Entre startX + 1 et renderWidth - 1 inclus
	 * @param endY Le pixel de fin vertical de la zone de l'image qui doit être calculée. Entre endY + 1 et renderHeight - 1 inclus
	 */
	public void computePartialImage(MyScene renderScene, int startX, int startY, int endX, int endY)
	{
		//RotationMatrix rotMatrix = new RotationMatrix(RotationMatrix.yAxis, -30);
		//MatrixD transformMatrix = MatrixD.mulMatrix(ctwMatrix, rotMatrix);
		MatrixD ctwMatrix = renderScene.getCamera().getCTWMatrix();
		
		double FOV = renderScene.getCamera().getFOV();
		
		for(int y = startY; y < endY; y++)
		{		
			for(int x = startX; x < endX; x++)
			{
				Point pixelWorldCoords = this.convPxCoToWorldCoords(FOV, x, y, ctwMatrix);
				
				Ray cameraRay = new Ray(MatrixD.mulPoint(new Point(0, 0, 0), ctwMatrix), pixelWorldCoords);
				cameraRay.normalize();
				
				Color pixelColor = this.computePixel(x, y, renderScene, cameraRay, 35);
				this.renderedPixels.set(y*renderWidth + x, pixelColor);
			}
		}
	}
	
//	/*
//	 * Calcule et renvoie la couleur et l'illumination d'un point à l'écran en fonction
//	 */
//	public Color computePhongShading(MyScene renderScene, Ray ray, Vector shadowRayDir, Shape intersectedObject, Vector normalAtIntersection)
//	{
//		double lightIntensity = renderScene.getLight().getIntensity();
//		
//		//Composante ambiante
//		double ambientTerm = lightIntensity * renderScene.getAmbientLightIntensity();
//
//		//Composante spéculaire
//		double specularTerm = 0;
//		if(dotProdDiffuse >= 0)//On ne calcule la specular que si le dotProduct du diffus n'est pas négatif
//		{
//			Vector reflectVector = Vector.normalize(this.getReflectionVector(normalAtIntersection, shadowRayDir));
//			
//			double dotProdSpecular = Vector.dotProduct(reflectVector, ray.negate());
//			specularTerm = lightIntensity*Math.pow(Math.max(dotProdSpecular, 0), intersectedObject.getShininess());
//		}
//		
//		double diffuseCoeff = intersectedObject.getDiffuse();
//		double specularCoeff = intersectedObject.getSpecularCoeff();
//		
//		Color initialColor = intersectedObject.getColor();
//		Color finalColor = intersectedObject.getColor();
//		
//		finalColor = ColorOperations.mulColor(finalColor, ambientTerm);
//		finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(initialColor, diffuseCoeff * diffuseTerm));
//		finalColor = ColorOperations.addToColor(finalColor, specularTerm * specularCoeff);
//		
//		return finalColor;
//	}
	
	/*
	 * Calcule la couleur d'un pixel grâce à un rayon
	 * 
	 * @param ray Le rayon qu'on veut tirer et dont on déduira la couleur d'un pixel
	 * 
	 * @return Une instance de Color.RGB(r, g, b)
	 */
	public Color computePixel(int x, int y, MyScene renderScene, Ray ray, int maxBounceCount)
	{
		if(maxBounceCount == 0)
			return renderScene.getBackgroundColor();
		
		ArrayList<Shape> objectList = renderScene.getSceneObjects();
		
		Point rayInterPoint = new Point(0, 0, 0);
		Shape rayInterObject = computeClosestInterPoint(objectList, ray, rayInterPoint);
		
		if(rayInterObject != null)//Il y a un point d'intersection
		{
			double lightIntensity = renderScene.getLight().getIntensity();
			double ambientLighting = renderScene.getAmbientLightIntensity() * lightIntensity;
			
			Color finalColor = Color.rgb(0, 0, 0);
			finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(rayInterObject.getColor(), ambientLighting));
			
			

			Vector reflectionVector = getReflectionVector(rayInterObject.getNormal(rayInterPoint), new Vector(rayInterPoint, renderScene.getLight().getCenter()));

			Point interPointShift = Point.add(rayInterPoint, Point.scalarMul(0.0001d, Vector.v2p(reflectionVector)));//On ajoute un très léger décalage au point d'intersection pour quand le retirant vers la lumière, il ne réintersecte
			
			Vector shadowRayDir = new Vector(interPointShift, renderScene.getLight().getCenter());//On calcule la direction du rayon secondaire qui va droit dans la source de lumière
			Ray shadowRay = new Ray(interPointShift, shadowRayDir);//Création du rayon secondaire avec pour origine le premier point d'intersection décalé et avec comme direction le centre de la lampe
			double interToLightDist = shadowRayDir.length();//Distance qui sépare le point d'intersection du centre de la lumière
			shadowRay.normalize();
			shadowRayDir.normalize();
			
			//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
			Point shadowInterPoint = new Point(0, 0, 0);
			Shape shadowInterObject = computeClosestInterPoint(objectList, shadowRay, shadowInterPoint);

			double interToShadowInterDist = 0;
			if(shadowInterObject != null)
				interToShadowInterDist = Point.distance(rayInterPoint, shadowInterPoint);

			
			if(shadowInterObject == null || interToShadowInterDist > interToLightDist)//Aucune intersection trouvée pour aller jusqu'à la lumière, on peut calculer la couleur directe de l'objet
			{
				Vector normalAtIntersection = rayInterObject.getNormal(rayInterPoint);//Normale au point d'intersection avec l'objet
				
				if(rayInterObject.getDiffuse() > 0)//Si le matériau est diffus
				{
					double diffuseComponent = computeDiffuse(shadowRayDir, normalAtIntersection, lightIntensity);
					finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(rayInterObject.getColor(), diffuseComponent * rayInterObject.getDiffuse()));
				}
				
				if(rayInterObject.getSpecularCoeff() > 0)//Si le matériau est spéculaire
				{
					double specularTerm = computeSpecular(ray, shadowRayDir, normalAtIntersection, lightIntensity, rayInterObject.getShininess());
					finalColor = ColorOperations.addToColor(finalColor, specularTerm * rayInterObject.getSpecularCoeff());
				}
				
				//finalColor = computePhongShading(renderScene, ray, shadowRayDir, rayInterObject, normalAtIntersection);
			}
			else//Une intersection a été trouvée et l'objet intersecté est entre la lumière et le départ du shadow ray, le pixel est dans l'ombre. On retourne la couleur * la luminosité ambiante
			{
				double ambientTerm = renderScene.getLight().getIntensity() * renderScene.getAmbientLightIntensity();
				
				int pixelRed = (int)(rayInterObject.getColor().getRed() * ambientTerm * 255); pixelRed = pixelRed > 255 ? 255 : pixelRed;
				int pixelGreen = (int)(rayInterObject.getColor().getGreen() * ambientTerm * 255); pixelGreen = pixelGreen > 255 ? 255 : pixelGreen;
				int pixelBlue = (int)(rayInterObject.getColor().getBlue() * ambientTerm * 255); pixelBlue = pixelBlue > 255 ? 255 : pixelBlue;
				
				finalColor = Color.rgb(pixelRed, pixelGreen, pixelBlue);//L'objet n'est pas réfléxif, on ne renvoie que la partie ambiante
			}
			
//			if(rayInterObject.getIsReflective())//Si l'objet réfléchi la lumière
//			{
//				Color reflectionColor = computePixel(x, y, renderScene, new Ray(interPointShift, reflectionVector), maxBounceCount - 1);
//				
//				double reflectionCoeff = 0.5;
//				
//				double lightIntensity = renderScene.getLight().getIntensity();
//				
//				//Composante diffuse
//				double dotProdDiffuse = Vector.dotProduct(shadowRayDir, rayInterObject.getNormal(rayInterPoint));
//				double diffuseTerm = dotProdDiffuse < 0 ? 0 : lightIntensity*dotProdDiffuse;//Si le dotProduct est négatif, on n'inclus pas le terme diffus dans le calcul, on le met donc à 0
//				
//				//Composante spéculaire
//				double specularTerm = 0;
//				if(dotProdDiffuse >= 0)//On ne calcule la specular que si le dotProduct du diffus n'est pas négatif
//				{
//					Vector reflectVector = Vector.normalize(this.getReflectionVector(rayInterObject.getNormal(rayInterPoint), shadowRayDir));
//					
//					double dotProdSpecular = Vector.dotProduct(reflectVector, ray.negate());
//					specularTerm = lightIntensity*Math.pow(Math.max(dotProdSpecular, 0), rayInterObject.getShininess());
//				}
//				
//				double objectRed = rayInterObject.getColor().getRed();
//				double objectGreen = rayInterObject.getColor().getGreen();
//				double objectBlue = rayInterObject.getColor().getBlue();
//				double diffuseCoeff = rayInterObject.getDiffuse();
//				
//				int refleAmbMixRed = (int)((objectRed*diffuseTerm*diffuseCoeff*(1-reflectionCoeff) + reflectionColor.getRed()*reflectionCoeff + lightIntensity*renderScene.getAmbientLightIntensity() * objectRed + specularTerm * rayInterObject.getSpecularCoeff())*255); refleAmbMixRed = refleAmbMixRed > 255 ? 255 : refleAmbMixRed; 
//				int refleAmbMixGreen = (int)((objectGreen*diffuseTerm*diffuseCoeff*(1-reflectionCoeff) + reflectionColor.getGreen()*reflectionCoeff + lightIntensity*renderScene.getAmbientLightIntensity() * objectGreen + specularTerm * rayInterObject.getSpecularCoeff())*255); refleAmbMixGreen = refleAmbMixGreen > 255 ? 255 : refleAmbMixGreen;
//				int refleAmbMixBlue = (int)((objectBlue*diffuseTerm*diffuseCoeff*(1-reflectionCoeff) + reflectionColor.getBlue()*reflectionCoeff + lightIntensity*renderScene.getAmbientLightIntensity() * objectBlue + specularTerm * rayInterObject.getSpecularCoeff())*255); refleAmbMixBlue = refleAmbMixBlue > 255 ? 255 : refleAmbMixBlue;
//				
//				finalColor = Color.rgb(refleAmbMixRed, refleAmbMixGreen, refleAmbMixBlue);
//			}
			
			return finalColor;
		}
		else//Le rayon n'a rien intersecté --> noir
			return renderScene.getBackgroundColor();//Couleur du fond, noir si on a pas de fond
	}
	
	public boolean computeTask(MyScene renderScene, ThreadsTaskList taskList)
	{
		Integer taskNumber = 0;
		TileTask currentTileTask = null;
		
		synchronized(taskNumber)
		{
			taskNumber = taskList.getTotalTaskGiven();
			if(taskNumber >= taskList.getTotalTaskCount())
				return false;
		
			currentTileTask = taskList.getTask(taskList.getTotalTaskGiven());
			taskList.incrementTaskGiven();
		}
		this.computePartialImage(renderScene, currentTileTask.getStartX(), currentTileTask.getStartY(), currentTileTask.getEndX(), currentTileTask.getEndY());
		
		taskList.incrementTaskFinished();
		return true;//Encore des tuiles à calculer
	}
	
	/*
	 * Convertit les coordonnées d'un pixel sur l'image (un pixel de l'image 1920x1080 par exemple) en coordonnées 3D dans la scène à rendre
	 * 
	 * @param camera La caméra selon laquelle on souhaite obtenir les coordonnées du pixel
	 * @param x Coordonnées x du pixel sur l'image (de 0 à 1919 pour une résolution de 1920 de large par exemple)
	 * @param y Coordonnées y du pixel sur l'image (de 0 à 1079 pour une résolution de 1080 de haut par exemple)
	 * 
	 * @return Un point de cooordonnées (x, y, z) tel que x, y et z représentent les coordonnées du pixel dans la scène
	 */
	public Point convPxCoToWorldCoords(double FOV, int x, int y, MatrixD ctwMatrix)
	{
		double xWorld = (double)x;
		double yWorld = (double)y;
		
		double aspectRatio = (double)this.renderWidth / (double)this.renderHeight;
		double demiHeightPlane = Math.tan(Math.toRadians(FOV/2));
		
		xWorld = (xWorld + 0.5) / this.renderWidth;//Normalisation des pixels. Maintenant dans [0, 1]
		xWorld = xWorld * 2 - 1;//Décalage des pixels dans [-1, 1]
		xWorld *= aspectRatio;//Prise en compte de l'aspect ratio. Maintenant dans [-aspectRatio; aspectRatio]
		xWorld *= demiHeightPlane;
		
		yWorld = (yWorld + 0.5) / this.renderHeight;//Normalisation des pixels. Maintenant dans [0, 1]
		yWorld = 1 - yWorld * 2;//Décalage des pixels dans [-1, 1]
		yWorld *= demiHeightPlane;
		
		Point pixelWorld = new Point(xWorld, yWorld, -1);
		Point pixelWorldConverted = MatrixD.mulPoint(pixelWorld, ctwMatrix);
		
		return pixelWorldConverted;
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
		return Vector.sub(Vector.scalarMul(normalToSurface, 2*Vector.dotProduct(intersectToLightVec, normalToSurface)), intersectToLightVec);//2*(L.N)*N-L
	}
	
	/*
	 * Permet d'obtenir le tableau de pixels correspondant à la dernière image rendue par le RayTracer
	 * Si aucune image n'a été rendue, renvoie null
	 * 
	 * @param Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength. Renvoie null si encore aucune image n'a été rendue
	 */
	public AtomicReferenceArray<Color> getRenderedPixels()
	{
		return this.renderedPixels;
	}
	
	public AtomicReferenceArray<Color> renderImage(MyScene renderScene, int nbCore)
	{
		ThreadsTaskList threadTaskList = new ThreadsTaskList();
		threadTaskList.initTaskList(nbCore, this.renderWidth, this.renderHeight);
		
		for(int i = 1; i < nbCore; i++)//Création des threads sauf 1, le thread principal, qui est déjà créé
			new Thread(new TileThread(threadTaskList, this, renderScene), String.format("RT-Thread %d", i)).start();
			
		while(threadTaskList.getTotalTaskFinished() < threadTaskList.getTotalTaskCount())
			this.computeTask(renderScene, threadTaskList);
		
		return this.getRenderedPixels();
	}
}
