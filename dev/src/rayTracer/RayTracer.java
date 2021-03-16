package rayTracer;

import java.nio.IntBuffer;
import java.util.ArrayList;

import geometry.Shape;
import geometry.materials.Material;
import javafx.scene.paint.Color;
import maths.MatrixD;
import maths.ColorOperations;
import maths.Point;
import maths.Ray;
import maths.Vector;
import multithreading.ThreadsTaskList;
import multithreading.TileTask;
import multithreading.TileThread;
import scene.RayTracingScene;

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

	IntBuffer renderedPixels;

	public RayTracer(int renderWidth, int renderHeight)
	{
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;

		this.renderedPixels = IntBuffer.allocate(renderWidth*renderHeight);
		for(int i = 0; i < renderWidth*renderHeight; i++)
			this.renderedPixels.put(i, ColorOperations.aRGB2Int(Color.rgb(255, 0, 0)));
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
	 * Calcule la luminosité ambiante de la scène à partir de l'intensité de la source de lumière et de l'intensité de la lumière ambiante
	 *
	 * @param ambientLightIntensity Intensité de la luminosité ambiante de la scène
	 * @param lightIntensity Intensité de la source de lumière
	 *
	 * @return Retourne la composante ambiante de l'illumination de la scène. Réel entre 0 et 1
	 */
	public double computeAmbient(double ambientLightIntensity, double lightIntensity)
	{
		return ambientLightIntensity * lightIntensity;
	}

	/*
	 * Calcule l'intensité lumineuse diffuse en un point donné de l'image à la surface d'un objet
	 *
	 *  @param toLightDirection Vecteur indiquant la direction de la source de lumière
	 *  @param normalAtIntersection Vecteur normal à la surface de l'objet
	 *  @param lightIntensity Intensité lumineuse de la source de lumière
	 *
	 *  @return La composante diffuse en un point donné de la scène vis à vis de la surface d'un objet. Réel entre 0 et 1
	 */
	public double computeDiffuse(Vector toLightDirection, Vector normalAtIntersection, double lightIntensity)
	{
		double dotProdDiffuse = Vector.dotProduct(toLightDirection, normalAtIntersection);
		double diffuseTerm = dotProdDiffuse < 0 ? 0 : lightIntensity*dotProdDiffuse;//Si le dotProduct est négatif, on ne calcule pas le terme diffus --> = 0

		return diffuseTerm;
	}

	/*
	 * Calcule l'intensité lumineuse spéculaire en un point donné de l'image à la surface d'un objet
	 *
	 *  @param incidentRay Ray incident au point dont on souhaite l'intensité spéculaire
	 *  @param toLightDirection Vecteur indiquant la direction de la source de lumière
	 *  @param normalAtIntersection Vecteur normal à la surface de l'objet
	 *  @param lightIntensity Intensité lumineuse de la source de lumière
	 *  @param objectShininess Brillance de l'objet obtenable avec Shape.getShininess()
	 *
	 *  @return La composante spéculaire en un point donné de la scène vis à vis de la surface d'un objet. Réel entre 0 et 1.
	 */
	public double computeSpecular(Ray incidentRay, Vector toLightDirection, Vector normalAtIntersection, double lightIntensity, double objectShininess)
	{
		//Composante spéculaire
		double specularTerm = 0;
		Vector reflectVector = Vector.normalize(this.getReflectionVector(normalAtIntersection, toLightDirection.negate()));

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
	public void computePartialImage(RayTracingScene renderScene, int startX, int startY, int endX, int endY)
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

				Color pixelColor = this.computePixel(x, y, renderScene, cameraRay, 3);
				this.renderedPixels.put(y*renderWidth + x, ColorOperations.aRGB2Int(pixelColor));
			}
		}
	}

	/*
	 * Calcule la couleur d'un pixel grâce à un rayon
	 *
	 * @param ray Le rayon qu'on veut tirer et dont on déduira la couleur d'un pixel
	 *
	 * @return Une instance de Color.RGB(r, g, b)
	 */
	public Color computePixel(int x, int y, RayTracingScene renderScene, Ray ray, int depth)
	{
		if(depth == 0)
			return renderScene.getBackgroundColor();

		ArrayList<Shape> objectList = renderScene.getSceneObjects();

		Point rayInterPoint = new Point(0, 0, 0);
		Shape rayInterObject = computeClosestInterPoint(objectList, ray, rayInterPoint);

		if(rayInterObject != null)//Il y a un point d'intersection
		{
			Material rayIntObjMaterial = rayInterObject.getMaterial();

			double lightIntensity = renderScene.getLight().getIntensity();
			double ambientLighting = computeAmbient(renderScene.getAmbientLightIntensity(), lightIntensity);

			Color finalColor = Color.rgb(0, 0, 0);
			finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(rayIntObjMaterial.getColor(), ambientLighting*rayIntObjMaterial.getAmbientCoeff()));



			Vector normalAtIntersection = rayInterObject.getNormal(rayInterPoint);
			//Vector reflectionVector = getReflectionVector(normalAtIntersection, new Vector(rayInterPoint, renderScene.getLight().getCenter()));
			Vector reflectionVector = getReflectionVector(normalAtIntersection, ray.getDirection());

			Point interPointShift = Point.add(rayInterPoint, Point.scalarMul(0.0001d, Vector.v2p(Vector.normalize(reflectionVector))));//On ajoute un très léger décalage au point d'intersection pour quand le retirant vers la lumière, il ne réintersecte

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
				if(rayIntObjMaterial.getDiffuseCoeff() > 0)//Si le matériau est diffus
				{
					double diffuseComponent = computeDiffuse(shadowRayDir, normalAtIntersection, lightIntensity);
					finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(rayIntObjMaterial.getColor(), diffuseComponent * rayIntObjMaterial.getDiffuseCoeff()));
				}

				if(rayIntObjMaterial.getSpecularCoeff() > 0)//Si le matériau est spéculaire
				{
					double specularTerm = computeSpecular(ray, shadowRayDir, normalAtIntersection, lightIntensity, rayIntObjMaterial.getShininess());
					finalColor = ColorOperations.addToColor(finalColor, specularTerm * rayIntObjMaterial.getSpecularCoeff());
				}

				if(rayIntObjMaterial.getReflectiveCoeff() > 0)
				{
					Color reflectionColor = computePixel(x, y, renderScene, new Ray(interPointShift, Vector.normalize(getReflectionVector(normalAtIntersection, ray.getDirection()))), depth - 1);

					//finalColor = ColorOperations.mulColor(reflectionColor, rayInterObject.getReflectiveCoeff());
					finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(reflectionColor, rayIntObjMaterial.getReflectiveCoeff()));
				}
			}
			else//Une intersection a été trouvée et l'objet intersecté est entre la lumière et le départ du shadow ray
			{
				if(rayIntObjMaterial.getReflectiveCoeff() > 0)//Si l'objet est réflectif, on va calculer le reflet de l'objet qui bloque le chemin à la lumière plutôt que de l'ombre
				{
					Color reflectionColor = computePixel(x, y, renderScene, new Ray(interPointShift, Vector.normalize(getReflectionVector(normalAtIntersection, ray.getDirection()))), depth - 1);

					finalColor = ColorOperations.mulColor(reflectionColor, rayIntObjMaterial.getReflectiveCoeff());
					finalColor = ColorOperations.addColors(finalColor, ColorOperations.mulColor(rayIntObjMaterial.getColor(), ambientLighting*rayIntObjMaterial.getAmbientCoeff()));
				}
				else
					finalColor = ColorOperations.mulColor(rayIntObjMaterial.getColor(), ambientLighting*rayIntObjMaterial.getAmbientCoeff());//L'objet n'est pas réfléxif, on ne renvoie que la partie ambiante
			}

			return finalColor;
		}
		else//Le rayon n'a rien intersecté --> couleur du background
			return renderScene.getBackgroundColor();//Couleur du fond, noir si on a pas de fond
	}

	public boolean computeTask(RayTracingScene renderScene, ThreadsTaskList taskList)
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
		//return Vector.sub(Vector.scalarMul(normalToSurface, 2*Vector.dotProduct(intersectToLightVec, normalToSurface)), intersectToLightVec);//2*(L.N)*N-L
		return Vector.sub(intersectToLightVec, Vector.scalarMul(normalToSurface, Vector.dotProduct(normalToSurface, intersectToLightVec)*2));
	}

	/*
	 * Permet d'obtenir le tableau de pixels correspondant à la dernière image rendue par le RayTracer
	 * Si aucune image n'a été rendue, renvoie null
	 *
	 * @param Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength. Renvoie null si encore aucune image n'a été rendue
	 */
	public IntBuffer getRenderedPixels()
	{
		return this.renderedPixels;
	}

	public IntBuffer renderImage(RayTracingScene renderScene, int nbCore)
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
