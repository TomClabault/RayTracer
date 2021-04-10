package rayTracer;

import java.nio.IntBuffer;
import java.util.ArrayList;

import geometry.Shape;
import geometry.shapes.PlaneMaths;
import geometry.shapes.Sphere;
import materials.Material;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import maths.MatrixD;
import maths.Point;
import maths.ColorOperations;
import maths.Ray;
import maths.Vector;
import multithreading.ThreadsTaskList;
import multithreading.TileTask;
import multithreading.TileThread;
import scene.RayTracingScene;

/*
 * Une instance de RayTracer créée à partir de la largeur et de la hauteur du rendu voulu. Permet de générer les
 */
public class RayTracer
{
	/*
	 * Lorsque qu'une intersection est trouvée, cette classe collecte toutes les informations utiles à la suite des calculs 
	 * (normal au point d'intersection, point d'intersection lui même, l'objet intersecté, la direction du rayon réfléchi, ...) 
	 */
	private class RayTracerInterInfos
	{
		private Shape intersectedObject;
		
		private Material intersectedObjectMaterial;
		
		private Color currentPixelColor;
		private Color intersectedObjectColor;
		
		private Point intersectionPoint; 
		private Point intersectionPointShift;
		
		private Vector incidentRayDirection;
		private Vector normalAtIntersection;
		private Vector perfectlyReflectedVector;
		private Vector toLightVector;
		
		private Ray shadowRay;
		
		
		

		
		public Shape getIntObj() {return this.intersectedObject;}
		
		public Material getIntObjMat() {return this.intersectedObjectMaterial;}
		
		public Color getCurPixCol() {return this.currentPixelColor;}
		public Color getObjCol() {return this.intersectedObjectColor;}
		
		public Point getIntP() {return this.intersectionPoint;}
		public Point getIntPShift() {return this.intersectionPointShift;}
		
		public Vector getNormInt() {return this.normalAtIntersection;}
		public Vector getRayDir() {return this.incidentRayDirection;}
		public Vector getReflVec() {return this.perfectlyReflectedVector;}
		public Vector getToLightVec() {return this.toLightVector;}
		
		public Ray getShadowRay() {return this.shadowRay;}
		
		
		
		public void setIntO(Shape newIntersectedObject) {this.intersectedObject = newIntersectedObject; this.intersectedObjectMaterial = newIntersectedObject.getMaterial();}
		
		public void setCurPixCol(Color newPixelColor) {this.currentPixelColor = newPixelColor;}
		public void setObjCol(Color intersectedObjectColor) {this.intersectedObjectColor = intersectedObjectColor;}
		
		public void setIntP(Point newInterPoint) {this.intersectionPoint = newInterPoint;}
		public void setIntPShift(Point newInterPointShift) {this.intersectionPointShift = newInterPointShift;}
		
		public void setNormInt(Vector newNormalAtInter) {this.normalAtIntersection = newNormalAtInter;}
		public void setReflVec(Vector perfectlyReflectedVector) {this.perfectlyReflectedVector = perfectlyReflectedVector;}
		public void setToLightVec(Vector toLightVector) {this.toLightVector = toLightVector;}
		
		public void setRay(Ray newIncidentRay) {this.incidentRayDirection = newIncidentRay.getDirection();}
		public void setShadowRay(Ray newShadowRay) {this.shadowRay = newShadowRay;}
	}
	
	
	
	
	
	public static final double AIR_REFRACTION_INDEX = 1.000293;
	public static final double EPSILON_SHIFT = 0.0001;
	
	RayTracerSettings settings;

	private ThreadsTaskList threadTaskList;
	private IntBuffer renderedPixels;

	private PixelReader skyboxPixelReader = null;

	public RayTracer(RayTracerSettings settings)
	{
		this.settings = settings;
		
		this.renderedPixels = IntBuffer.allocate(settings.getRenderWidth()*settings.getRenderHeight());
//		for(int i = 0; i < renderWidth*renderHeight; i++)
//			this.renderedPixels.put(i, ColorOperations.aRGB2Int(Color.rgb(255, 0, 0)));
		
		this.threadTaskList = new ThreadsTaskList();
		this.threadTaskList.initTaskList(settings.getNbCore(), settings.getRenderWidth(), settings.getRenderHeight());
	}
	
	/*
	 * Calcule le premier point d'intersection du rayon passé en argument avec les objets de la scène
	 *
	 * @param objectList Liste des objets de la scène. Obtenable avec MyScene.getSceneObjects()
	 * @param ray Rayon duquel chercher les points d'intersection avec les objets de la scène
	 * @param outClosestInterPoint Ce paramètre reçoit les coordonnées du point d'intersection le plus proche de la caméra trouvé. Il est inchangé si aucun point d'intersection n'est trouvé
	 * @param outNormalAtInter 	   Ce paramètre reçoit la normale au point d'intersection trouvé. Inchangé si aucun point d'intersection n'a été trouvé. Si ce paramètre est null, la normale au point d'intersection ne sera pas automatiquement calculée
	 *
	 * @return Retourne l'objet avec lequel le rayon a fait son intersection. 'outClosestInterPoint' est un point de l'objet retourné
	 */
	protected Shape computeClosestInterPoint(ArrayList<Shape> objectList, Ray ray, RayTracerInterInfos intInfos, boolean getNormalAtInter, Point outClosestInterPoint)
	{
		Shape closestObjectIntersected = null;
		Double distanceMin = null;

		for(Shape object : objectList)
		{
			double distRayOriInter = -1;

			Vector newNormalAtInter = new Vector(0, 0, 0);//Ce vecteur va temporairement stocker la normale au point d'intersection trouvé (s'il existe). Si le point d'intersection trouvé et plus proche que les autres, c'est alors cette normale que l'on gardera
			Point intersection = object.intersect(ray, newNormalAtInter);
			if(intersection != null)
			{
				distRayOriInter = Point.distance(ray.getOrigin(), intersection);

				if(distanceMin == null || distRayOriInter < distanceMin)//Si c'est le premier point d'intersection qu'on trouve ou si on a trouvé un point d'intersection plus proche que celui qu'on avait avant
				{
					distanceMin = distRayOriInter;

					if(intInfos != null)
					{
						intInfos.setIntP(intersection);
						if(getNormalAtInter)//Si on souhaite récupérer la normale au point d'intersection
							intInfos.setNormInt(newNormalAtInter);;//On a trouvé un point plus proche donc on peut actualiser la normale avec la normale correpondant au point le plus proche
					}
					
					if(outClosestInterPoint != null)
						outClosestInterPoint.copyIn(intersection);
					
					closestObjectIntersected = object;
				}
			}
		}

		return closestObjectIntersected;
	}

	/*
	 * A partir d'une couleur donnée en entrée, calcule et renvoie la couleur après application de la composante diffuse de l'ombrage de Phong pour un matériau et des directions de rayons donnés
	 * Si l'objet n'est pas diffus, la couleur est renvoyée inchangée par la fonction
	 * 
	 * @param rayIntObjMaterial Le matériau qui va être utilisé pour calculer la composante diffuse
	 * @param finalColorBefore La couleur "sur" laquelle va être appliquée le calcul de la composante diffuse
	 * @param objectColor La couleur du matériau de l'objet
	 * @param toLightVector Vecteur indiquant la direction vers la source de lumière
	 * @param normalAtIntersection Le vecteur normal de la surface de l'objet au point d'intersection
	 * @param lightIntensity Intensité lumineuse de la source de lumière
	 *  
	 * @return La couleur passée en argument affectée de la composante diffuse de l'ombrage de Phong. Si l'objet n'est pas diffus, la couleur est renvoyée inchangée.
	 */
	protected Color computeDiffuseColor(RayTracerInterInfos intInfos, double lightIntensity)
	{
		Material intObjMat = intInfos.getIntObjMat();
		Color currentPixelColor = intInfos.getCurPixCol(); 
		
		if(intObjMat.getDiffuseCoeff() > 0)//Si le matériau est diffus
		{
			double diffuseComponent = computeDiffuse(intInfos.getToLightVec(), intInfos.getNormInt(), lightIntensity);
			return ColorOperations.mulColor(intInfos.getObjCol(), diffuseComponent * intObjMat.getDiffuseCoeff());
		}
		else//L'objet n'est pas diffus, on renvoie la couleur inchangée
			return Color.rgb(0, 0, 0);
	}
	
	/*
	 * A partir d'une couleur donnée en entrée, calcule et renvoie la couleur après application de la composante spéculaire de l'ombrage de Phong pour un matériau et des directions de rayons donnés
	 * Si l'objet n'est pas spéculaire, la couleur renvoyée est inchangée
	 * 
	 * @param rayIntObjMaterial Le matériau qui va être utilisé pour calculer la composante diffuse
	 * @param finalColorBefore La couleur "sur" laquelle va être appliquée le calcul de la composante diffuse
	 * @param incidentRay Le rayon incident au point d'intersection de l'objet
	 * @param toLightVector Vecteur indiquant la direction vers la source de lumière
	 * @param normalAtIntersection Le vecteur normal de la surface de l'objet au point d'intersection
	 * @param lightIntensity Intensité lumineuse de la source de lumière
	 *  
	 * @return La couleur passée en argument affectée de la composante spéculaire de l'ombrage de Phong. Si l'objet n'est pas spéculaire, la couleur est renvoyée inchangée
	 */
	protected Color computeSpecularColor(RayTracerInterInfos intInfos, double lightIntensity)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		Color specularColor = Color.rgb(255, 255, 255);//TODO bonus: ne pas faire une couleur fixe comme ça mais ajouter une couleur spéculaire pour chaque matériaux
		
		if(intObjMat.getSpecularCoeff() > 0)//Si le matériau est spéculaire
		{
			double specularTerm = computeSpecular(intInfos.getRayDir(), intInfos.getToLightVec(), intInfos.getNormInt(), lightIntensity, intObjMat.getShininess());
			return ColorOperations.mulColor(specularColor, specularTerm * intObjMat.getSpecularCoeff());
		}
		else//L'objet n'est pas spéculaire, on renvoie la couleur inchangée
			return Color.rgb(0, 0, 0);
	}
	
	/*
	 * A partir d'une couleur donnée en entrée, calcule et renvoie la couleur après application de la réflectivité du matériau.
	 * Si l'objet n'est pas réflexif, la couleur renvoyée est inchangée
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param rayIntObjMaterial Le matériau qui va être utilisé pour calculer la composante diffuse
	 * @param finalColorBefore La couleur "sur" laquelle va être appliquée le calcul de la composante diffuse
	 * @param objectColor La couleur du matériau de l'objet
	 * @param rayDirection Direction du rayon incident au point d'intersection de l'objet
	 * @param toLightVector Vecteur indiquant la direction vers la source de lumière
	 * @param normalAtIntersection Le vecteur normal de la surface de l'objet au point d'intersection
	 * @param interPointShift Le point d'intersection du rayon incident avec l'objet légèrement décalé dans le sens du rayon incident parfaitement réfléchi par la surface de l'objet
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 *  
	 * @return La couleur passée en argument affectée des couleurs qui se réfléchissent dans l'objet. Si l'objet n'est pas réflexif, la couleur est renvoyée inchangée
	 */
	protected Color computeReflectionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		//Si l'objet est réfléchissant
		if(intObjMat.getReflectiveCoeff() > 0)
		{
			Vector reflectDirection = intInfos.getReflVec();
			Color reflectionColor = computePixel(renderScene, new Ray(intInfos.getIntPShift(), Vector.normalizeV(reflectDirection)), depth - 1);

			return ColorOperations.mulColor(reflectionColor, intObjMat.getReflectiveCoeff());
		}
		else//Si l'objet n'est pas réfléchissant, on ne renvoie pas de couleur de réflexion --> noir
			return Color.rgb(0, 0, 0);
	}
	
	/*
	 * Calcule les couleurs venant des réfractions et des réflexions d'un objet transparent. 
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param rayIntObjMaterial Le matériau qui va être utilisé pour calculer la composante diffuse
	 * @param finalColorBefore La couleur "sur" laquelle va être appliquée le calcul de la composante diffuse
	 * @param incidentRay Le rayon incident au point d'intersection de l'objet
	 * @param normalAtIntersection Le vecteur normal de la surface de l'objet au point d'intersection
	 * @param rayInterPoint Le point d'intersection du rayon incident avec l'objet
	 * @param interPointShift Le point d'intersection du rayon incident avec l'objet légèrement décalé dans le sens du rayon incident parfaitement réfléchi par la surface de l'objet
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 * 
	 * @return Retourne la couleur donnée en argument "mixée" avec les couleurs des réfractions et des réflexions de l'objet. Si l'objet n'est pas transparent, la couleur retournée est inchangée par rapport à celle passée en argument
	 */
	protected Color computeRefractionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		Vector incidentRayDir = intInfos.getRayDir();
		Vector normalAtInter = intInfos.getNormInt();
		
		Point rayInterPoint = intInfos.getIntP();
		
		if (intObjMat.getRefractionIndex() != 0)//L'objet est réfractif 
		{
			double fr = fresnel(intInfos.getRayDir(), intInfos.getNormInt(), intObjMat.getRefractionIndex());
			double ft = 1 - fr;
			Vector refractedRayDir = computeRefractedVector(incidentRayDir, normalAtInter, intObjMat.getRefractionIndex());
			Ray reflectedRay = null;
			Ray refractedRay = null;
			if (Vector.dotProduct(incidentRayDir, normalAtInter) > 0) {
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
				reflectedRay = new Ray(intInfos.getIntPShift(), computeReflectionVector(normalAtInter, incidentRayDir));
			} else {
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
				reflectedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, -EPSILON_SHIFT), computeReflectionVector(normalAtInter, incidentRayDir));
			}
			
			Color refractedColor = Color.rgb(0,0,0);
			if(intObjMat.getIsTransparent())//L'objet est transparent, on va donc calculer les rayons réfractés à l'intérieur de l'objet
			{
				if (! refractedRayDir.equals(new Vector(0,0,0)) ) {
					refractedColor = computePixel(renderScene, refractedRay, depth -1);
				}
			}
			
			Color reflectedColor = computePixel(renderScene, reflectedRay, depth -1);
			
			Color finalColor = ColorOperations.mulColor(refractedColor, ft);
			return ColorOperations.addColors(finalColor, ColorOperations.mulColor(reflectedColor, fr));
		}
		else//L'objet n'est pas réfractif
			return Color.rgb(0, 0, 0);
	}
	
	/*
	 * Calcule la couleur d'un point de la scène ombragé 
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param rayIntObjMaterial Le matériau qui va être utilisé pour calculer la composante diffuse
	 * @param objectColor La couleur du matériau de l'objet
	 * @param normalAtIntersection Le vecteur normal de la surface de l'objet au point d'intersection
	 * @param rayDirection La direction du rayon incident
	 * @param rayInterPoint Le point d'intersection du rayon incident avec l'objet
	 * @param rayInterPointShift Le point d'intersection du rayon incident avec l'objet légèrement décalé dans le sens du rayon incident parfaitement réfléchi par la surface de l'objet
	 * @param ambientLighting L'intensité de la lumière ambiante de la scène
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 * 
	 * @return Retourne la couleur ombragée
	 */
	protected Color computeShadow(RayTracingScene renderScene, RayTracerInterInfos intInfos, double ambientLighting, int depth)
	{
		Color finalColor = null;

		finalColor = ColorOperations.mulColor(intInfos.getObjCol(), ambientLighting);
		finalColor = ColorOperations.addColors(finalColor, computeReflectionsColor(renderScene, intInfos, depth));
		finalColor = ColorOperations.addColors(finalColor, computeRefractionsColor(renderScene, intInfos, depth));
		
		return finalColor;
	}
	
	/*
	 * Calcule la luminosité ambiante de la scène à partir de l'intensité de la source de lumière et de l'intensité de la lumière ambiante
	 *
	 * @param ambientLightIntensity Intensité de la luminosité ambiante de la scène
	 * @param materialAmbientCoeff Coefficient de réflexion de la lumière ambiante du matériau
	 *
	 * @return Retourne la composante ambiante du matériau dont le coefficient ambiant a été passé en paramètre. Réel entre 0 et 1
	 */
	protected double computeAmbient(double ambientLightIntensity, double materialAmbientCoeff)
	{
		return ambientLightIntensity * materialAmbientCoeff;
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
	protected double computeDiffuse(Vector toLightDirection, Vector normalAtIntersection, double lightIntensity)
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
	protected double computeSpecular(Vector incidentRayDirection, Vector toLightDirection, Vector normalAtIntersection, double lightIntensity, double objectShininess)
	{
		double specularTerm = 0;
		Vector reflectVector = Vector.normalizeV(this.computeReflectionVector(normalAtIntersection, toLightDirection.getNegated()));

		double dotProdSpecular = Vector.dotProduct(reflectVector, incidentRayDirection.getNegated());
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
	protected void computePartialImage(RayTracingScene renderScene, int startX, int startY, int endX, int endY)
	{
		MatrixD ctwMatrix = renderScene.getCamera().getCTWMatrix();

		double FOV = renderScene.getCamera().getFOV();

		for(int y = startY; y < endY; y++)
		{
			for(int x = startX; x < endX; x++)
			{
				Point pixelWorldCoords = this.convPxCoToWorldCoords(FOV, x, y, ctwMatrix);

				Vector rayDirection = new Vector(renderScene.getCamera().getPosition(), pixelWorldCoords);
				Ray cameraRay = new Ray(MatrixD.mulPointP(new Vector(0, 0, 0), ctwMatrix), rayDirection);
				cameraRay.normalize();

				Color pixelColor = this.computePixel(renderScene, cameraRay, settings.getRecursionDepth());
				pixelColor = ColorOperations.linearTosRGBGamma2_2(pixelColor);
				
				this.renderedPixels.put(y*settings.getRenderWidth() + x, ColorOperations.aRGB2Int(pixelColor));
			}
		}
	}

	/*
	 * Calcule la couleur d'un pixel grâce à un rayon
	 *
	 * @param renderScene La scène utilisée pour le rendu
	 * @param ray Le camera ray passant par le pixel que l'on veut calculer
	 * @param depth La profondeur maximale de récursion de l'algorithme. Défini entre autre le nombre maximum de reflets consécutifs que l'on peut observer dans deux surface se réfléchissants l'une l'autre
	 *
	 * @return La couleur du pixel que traverse le rayon incident 'ray'
	 */
	protected Color computePixel(RayTracingScene renderScene, Ray ray, int depth)
	{
		if(depth == 0)
			return Color.BLACK;

		ArrayList<Shape> objectList = renderScene.getSceneObjects();
		RayTracerInterInfos interInfos = new RayTracerInterInfos();
		
		interInfos.setRay(ray);
		Shape intersectedObject = computeClosestInterPoint(objectList, ray, interInfos, true, null);//On détermine l'objet intersecté par le rayon et on stocke sa référence dans la intInfos

		if(intersectedObject != null)//Un objet a bien été intersecté
		{
			interInfos.setIntO(intersectedObject);//On set l'objet intersecté dans les informations d'intersection
			
			double lightIntensity = renderScene.getLight().getIntensity();
			double ambientLighting = computeAmbient(renderScene.getAmbientLightIntensity(), interInfos.getIntObjMat().getAmbientCoeff());

			interInfos.setToLightVec(Vector.normalizeV(new Vector(interInfos.getIntP(), renderScene.getLight().getCenter())));

			Color currentPixelColor = Color.rgb(0, 0, 0);
			Color objectColor = null;
			if(interInfos.getIntObjMat().hasProceduralTexture())
			{
				Point UVCoordsAtInterPoint = interInfos.getIntObj().getUVCoords(interInfos.getIntP());
				
				
				objectColor = interInfos.getIntObjMat().getProceduralTexture().getColorAt(UVCoordsAtInterPoint);
			}
			else
				objectColor = interInfos.getIntObjMat().getColor();
			
			if(interInfos.getIntObj() instanceof PlaneMaths && interInfos.getIntObj().getMaterial().hasProceduralTexture())//Si le plan est un checkerboard
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, 1));//Cas spécial pour notre application pour que le plan soit plus illuminé que le reste. Non réaliste mais meilleur aspect visuel. On applique une ambient lighting fixe de 1
			else
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, ambientLighting));

			interInfos.setCurPixCol(currentPixelColor);
			interInfos.setObjCol(objectColor);

			
			
			interInfos.setReflVec(computeReflectionVector(interInfos.getNormInt(), ray.getDirection()));
			interInfos.setIntPShift(Point.translateMul(interInfos.getIntP(), interInfos.getReflVec(), EPSILON_SHIFT));//On translate légèrement le point d'intersection dans la direction d'un  rayon parfaitement réfléchi pour ne pas directement réintersecter l'objet avec lequel nous avons déjà trouvé un point d'intersection
			
			Vector shadowRayDir = new Vector(interInfos.getIntPShift(), renderScene.getLight().getCenter());//On calcule la direction du rayon secondaire qui va droit dans la source de lumière
			double interToLightDist = shadowRayDir.length();//Distance qui sépare le point d'intersection du centre de la lumière
			shadowRayDir.normalize();
			
			interInfos.setShadowRay(new Ray(interInfos.getIntPShift(), shadowRayDir));//Création du rayon secondaire avec pour origine le premier point d'intersection décalé et avec comme direction le centre de la lampe

			//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
			Point shadowInterPoint = new Point(0, 0, 0);
			Shape shadowInterObject = computeClosestInterPoint(objectList, interInfos.getShadowRay(), null, false, shadowInterPoint);

			double interToShadowInterDist = 0;
			if(shadowInterObject != null)
				interToShadowInterDist = Point.distance(interInfos.getIntP(), shadowInterPoint);


			if(shadowInterObject == null || interToShadowInterDist > interToLightDist || shadowInterObject.getMaterial().getIsTransparent())//Aucune intersection trouvée pour aller jusqu'à la lumière, on peut calculer la couleur directe de l'objet
			{
				interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeDiffuseColor(interInfos, lightIntensity)));
				interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeSpecularColor(interInfos, lightIntensity)));
				interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeReflectionsColor(renderScene, interInfos, depth)));
				interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeRefractionsColor(renderScene, interInfos, depth)));
			}
			else//Une intersection a été trouvée et l'objet intersecté est entre la lumière et le départ du shadow ray. De plus, l'objet bloquant la vue à la lumière n'est pas transparent
				interInfos.setCurPixCol(computeShadow(renderScene, interInfos, ambientLighting, depth));

			return interInfos.getCurPixCol();
		}
		else//Le rayon n'a rien intersecté --> couleur du background / de la skybox
			if(renderScene.hasSkybox())
			{
				Point UVCoords = Sphere.getUVCoordsUnitSphere(ray.getDirection());
				
				double uD = UVCoords.getX();
				double vD = UVCoords.getY();
				
				int u = (int)Math.floor((renderScene.getSkyboxWidth()-1) * uD);
				int v = (int)Math.floor((renderScene.getSkyboxHeight()-1) * vD);
				
				Color skyboxPixelColor = this.skyboxPixelReader.getColor(u, v); 
				
				return skyboxPixelColor;
			}
			else
				return renderScene.getBackgroundColor();//Couleur du fond si on a pas de skybox
	}

	/*
	 * Permet de calculer la prochaine tâche de rendu de la taskList. 
	 * Cette méthode est exécutée par plusieurs threads en même temps.
	 * Elle est public afin que TileThread, la classe des threads puisse appeler cette méthode depuis run() des threads 
	 * 
	 *  @param renderScene La scène qui doit être rendue par le rayTracer
	 *  @param taskList La liste de tâche préalablement initialisée
	 *  
	 *  @return Retourne true si le thread à calculé une tâche avec succès. false sinon, i.e., le thread n'a pas calculé de tâche car il n'y en avait plus à calcuelr
	 */
	public boolean computeTask(RayTracingScene renderScene, ThreadsTaskList taskList)
	{
		Integer taskNumber = 0;
		TileTask currentTileTask = null;

		taskNumber = taskList.getTotalTaskGiven();
		if(taskNumber >= taskList.getTotalTaskCount())
			return false;

		currentTileTask = taskList.getTask(taskList.getTotalTaskGiven());
		taskList.incrementTaskGiven();
		
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
	protected Point convPxCoToWorldCoords(double FOV, int x, int y, MatrixD ctwMatrix)
	{
		double xWorld = (double)x;
		double yWorld = (double)y;

		double aspectRatio = (double)settings.getRenderWidth() / (double)settings.getRenderHeight();
		double demiHeightPlane = Math.tan(Math.toRadians(FOV/2));

		xWorld = (xWorld + 0.5) / settings.getRenderWidth();//Normalisation des pixels. Maintenant dans [0, 1]
		xWorld = xWorld * 2 - 1;//Décalage des pixels dans [-1, 1]
		xWorld *= aspectRatio;//Prise en compte de l'aspect ratio. Maintenant dans [-aspectRatio; aspectRatio]
		xWorld *= demiHeightPlane;

		yWorld = (yWorld + 0.5) / settings.getRenderHeight();//Normalisation des pixels. Maintenant dans [0, 1]
		yWorld = 1 - yWorld * 2;//Décalage des pixels dans [-1, 1]
		yWorld *= demiHeightPlane;

		Point pixelWorld = new Point(xWorld, yWorld, -1);
		Point pixelWorldConverted = MatrixD.mulPointP(pixelWorld, ctwMatrix);

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
	protected Vector computeReflectionVector(Vector normalToSurface, Vector rayDirection)
	{
		return Vector.sub(rayDirection, Vector.scalarMul(normalToSurface, Vector.dotProduct(normalToSurface, rayDirection)*2));
	}

	/*
	 * Calcule le rayon réfracté par un matériau transparent ayant un indice de réfraction différent de celui de l'air
	 * 
	 *  @param rayDirection La directio, du rayon incident au point d'intersection avec le matériau
	 *  @param normalAtIntersection Le vecteur normal de la surface au point d'intersection
	 *  @param specialMediumIndex L'indice de réfraction du matériau réfractif
	 *  
	 *  @return Retourne la direction du rayon réfracté
	 */
	protected Vector computeRefractedVector(Vector rayDirection, Vector normalAtIntersection, double specialMediumRefIndex)
	{
		double startRefractionIndex = AIR_REFRACTION_INDEX;
		Vector newNormal = new Vector(0,0,0);
		newNormal.copyIn(normalAtIntersection);
		
		if (Vector.dotProduct(rayDirection, normalAtIntersection) > 0)//Si on est à l'intérieur de l'objet, il faut échanger les deux indices de réfraction
		{
			newNormal = Vector.scalarMul(normalAtIntersection, -1); 
			startRefractionIndex = specialMediumRefIndex;
			specialMediumRefIndex = AIR_REFRACTION_INDEX;
		}
		double eta = startRefractionIndex / specialMediumRefIndex;
		double c1 = Vector.dotProduct(rayDirection, newNormal);
		if (c1 < 0) {
			c1 = -c1;
		}
		double thetaIncident = Math.acos(c1);
		
		double inRootC2 = 1-eta*eta*Math.sin(thetaIncident)*Math.sin(thetaIncident);
		if (inRootC2 < 0) {
			return new Vector(0,0,0);
		}
		double c2 = Math.sqrt(inRootC2);
		
		Vector leftPart = Vector.scalarMul(rayDirection, eta);
		Vector rightPart = Vector.scalarMul(newNormal,eta*c1 - c2);
		return  Vector.add(leftPart, rightPart);
	}
	
	/*
	 * Calcule la proportion de lumière réfléchie et réfractée par un objet réfractif
	 * 
	 * @param incidentRayDirection La direction du rayon incident au point d'intersection de l'objet
	 * @param normalAtIntersection Vecteur normal à la surface de l'objet au point d'intersection
	 * @param specialMediumRefractionIndex L'indice de réfraction du matériau réfractif
	 * 
	 * @return Retourne la proportion de lumière réfléchie par le matériau étant donne le rayon incident.
	 * La proportion de lumière réfractée peut être déduite: réfractée = 1 - réfléchie
	 */
	protected double fresnel(Vector incidentRayDirection, Vector normalAtIntersection, double specialMediumRefIndex) 
	{
		double incomingRefractionIndex = AIR_REFRACTION_INDEX;
		
		if (Vector.dotProduct(incidentRayDirection, normalAtIntersection) > 0)//Le rayon est à l'intérieur de l'objet
		{
			incomingRefractionIndex = specialMediumRefIndex;
			specialMediumRefIndex = AIR_REFRACTION_INDEX;
		}
		
		double cosThetaIncident = Vector.dotProduct(incidentRayDirection, normalAtIntersection);
		cosThetaIncident = Math.abs(cosThetaIncident);
		double sinThetaRefracted = Math.sin(Math.acos(cosThetaIncident)) * incomingRefractionIndex/specialMediumRefIndex;
		double thetaRefracted = Math.asin(sinThetaRefracted);
		double cosThetaRefracted = Math.cos(thetaRefracted);
		if (sinThetaRefracted >= 1) {
			return 1;
		}
		
		double sup = (specialMediumRefIndex*cosThetaIncident - incomingRefractionIndex*cosThetaRefracted);
		double inf = (specialMediumRefIndex*cosThetaIncident + incomingRefractionIndex*cosThetaRefracted);
		double fpl = Math.pow(sup/inf, 2);
		
		sup = (incomingRefractionIndex*cosThetaIncident - specialMediumRefIndex*cosThetaRefracted);
		inf = (incomingRefractionIndex*cosThetaIncident + specialMediumRefIndex*cosThetaRefracted);
		double fpr = Math.pow(sup/inf, 2);
		
		return 0.5*(fpl+fpr);
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

	public IntBuffer renderImage(RayTracingScene renderScene)
	{
		if(renderScene.hasSkybox())
			this.skyboxPixelReader = renderScene.getSkyboxPixelReader();

		for(int i = 1; i < settings.getNbCore(); i++)//Création des threads sauf 1, le thread principal, qui est déjà créé
			new Thread(new TileThread(threadTaskList, this, renderScene), String.format("RT-Thread %d", i)).start();

		while(threadTaskList.getTotalTaskFinished() < threadTaskList.getTotalTaskCount())
			this.computeTask(renderScene, threadTaskList);

		this.threadTaskList.resetTasksProgression();
		return this.getRenderedPixels();
	}
}
