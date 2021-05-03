package rayTracer;

import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import accelerationStructures.AccelerationStructure;
import geometry.ObjectContainer;
import geometry.Shape;
import geometry.shapes.Plane;
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
import scene.lights.PositionnalLight;

/**
 * Permet d'instancier un ray tracer capable de faire le rendu d'une scène donnée.
 * 
 */
public class RayTracer
{
	/**
	 * Lorsque qu'une intersection est trouvée, cette classe collecte toutes les informations utiles à la suite des calculs 
	 * (normal au point d'intersection, point d'intersection lui même, l'objet intersecté, la direction du rayon réfléchi, ...) 
	 */
	private class RayTracerInterInfos
	{
		private Double tCoeff;
		
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
		
		
		

		
		public Double getTCoeff() { return tCoeff; }
		
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
		
		
		
		public void setTCoeff(Double newT) { this.tCoeff = newT; }
		
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
	
	private int renderWidth;
	private int renderHeight;
	private boolean renderDone;
	
	/*
	 * Variables utilisées pour calculer la progression du rendu
	 */
	private AtomicInteger totalPixelComputed;
	private int totalPixelToRender;
	
	private RayTracerSettings settings;
	private RayTracerStats rtStats;
	
	private ThreadsTaskList threadTaskList;
	private IntBuffer renderedPixels;
	private PixelReader skyboxPixelReader = null;
	private Random randomGenerator;//Générateur de nombre aléatoire qui servira uniquement au thread principal
	
	public RayTracer(int renderWidth, int renderHeight)
	{
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		this.renderDone = false;
		
		this.rtStats = new RayTracerStats();
		
		this.totalPixelToRender = renderWidth * renderHeight;
		this.totalPixelComputed = new AtomicInteger();
		
		this.renderedPixels = IntBuffer.allocate(renderWidth*renderHeight);
		for(int i = 0; i < renderWidth * renderHeight; i++)
			this.renderedPixels.put(i, ColorOperations.aRGB2Int(Color.rgb(0, 0, 0)));
		
		this.threadTaskList = new ThreadsTaskList();
		this.randomGenerator= new Random();
	}
	
	/**
	 * Permet de changer la taille de rendu du ray tracer
	 * 
	 * @param newRenderWidth La nouvelle largeur de rendu
	 * @param newRenderHeight La nouvelle hauteur de rendu
	 */
	public void changeRenderSize(int newRenderWidth, int newRenderHeight)
	{
		this.renderWidth = newRenderWidth;
		this.renderHeight = newRenderHeight;
		
		this.renderedPixels = IntBuffer.allocate(renderWidth*renderHeight);
	}
	
	/**
	 * Calcule le premier point d'intersection du rayon passé en argument avec les objets de la scène
	 *
	 * @param accelStruct 			Structure d'accélération wrappant les objets de la scène. Le rayon passé en paramètre sera tester contre cette
	 * structure d'accélération
	 * @param ray 					Rayon duquel chercher les points d'intersection avec les objets de la scène
	 * @param intInfos 				Référence vers les informations sur le point d'intersection qui sera éventuellement trouvé par un appel à computeClosestInterPoint. Si un point d'intersection est trouvé, une partie de l'ensemble des informations relatives sera mise à jour 
	 * @param updateNormalAtInter 	True pour récupérer la normale au point d'intersection dans intInfos passé en argument. Si false, la normale ne sera pas récupérer et l'attribut 'normalAtIntersection' de intInfos restera inchangé
	 * @param outClosestInterPoint	Référence vers une instance de Point. Si cette instance est non nulle, elle sera mise à jour si computeClosestInterPoint trouve un point d'intersection entre le rayon et la scène. Si aucun point d'intersection n'est trouvé, l'instance restera inchangée
	 *
	 * @return Retourne l'objet avec lequel le rayon a fait son intersection. Si 'outClosestInterPoint' était non nulle à l'appel de la méthode alors outClosestInterPoint contient maintenant le point d'intersection entre le rayon et l'objet renvoyé par la méthode
	 */
	protected Shape computeClosestInterPoint(AccelerationStructure accelStruct, Ray ray, RayTracerStats interStats, RayTracerInterInfos intInfos, boolean updateNormalAtInter, Point outClosestInterPoint)
	{
		Vector outNormalAtIntersection = new Vector(0, 0, 0);;
		
		Point interPoint = new Point(0, 0, 0);
		ObjectContainer objectContainer = new ObjectContainer();
		Double t = accelStruct.intersect(interStats, ray, interPoint, outNormalAtIntersection, objectContainer);
		
		if(t != null)//On a trouvé une intersection
		{
			if(intInfos != null)
			{
				if(updateNormalAtInter)
					intInfos.setNormInt(outNormalAtIntersection);
				intInfos.setIntP(interPoint);
				intInfos.setTCoeff(t);
			}
			
			if(outClosestInterPoint != null)//On souhaite récupérer le point d'intersection
				outClosestInterPoint.copyIn(interPoint);
		}
		
		return objectContainer.getContainedShape();
	}

	/**
	 * A partir des informations sur le point d'intersection données en entrée, calcule et renvoie la couleur de la composante diffuse de l'ombrage de Phong au point d'intersection<br>
	 * Si le matériau de l'objet n'est pas diffus, la couleur noire rgb(0, 0, 0) est renvoyée
	 *
	 * @param intInfos 			Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur diffuse
	 * @param lightIntensity 	Intensité lumineuse de la source de lumière
	 *  
	 * @return La couleur de la composante diffuse de l'ombrage de Phong au point d'intersection contenu dans intInfos. Si l'objet n'est pas spéculaire, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeDiffuseColor(RayTracerInterInfos intInfos, double lightIntensity)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		if(intObjMat.getDiffuseCoeff() > 0)//Si le matériau est diffus
		{
			double diffuseComponent = computeDiffuse(intInfos.getToLightVec(), intInfos.getNormInt(), lightIntensity);
			return ColorOperations.mulColor(intInfos.getObjCol(), diffuseComponent * intObjMat.getDiffuseCoeff());
		}
		else//L'objet n'est pas diffus, on renvoie la couleur inchangée
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * A partir d'une couleur donnée en entrée, calcule et renvoie la couleur après application de la composante spéculaire de l'ombrage de Phong pour un matériau et des directions de rayons donnés<br>
	 * Si le matériau de l'objet n'est pas spéculaire, la couleur noire rgb(0, 0, 0) est renvoyée
	 * 
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur spéculaire
	 * @param lightIntensity Intensité lumineuse de la source de lumière
	 *  
	 * @return La couleur de la composante spéculaire de l'ombrage de Phong au point d'intersection contenu dans intInfos. Si l'objet n'est pas spéculaire, renvoie la couleur noire rgb(0, 0, 0)
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
	
	/**
	 * A partir d'une couleur donnée en entrée, calcule et renvoie la couleur après application de la réflectivité du matériau.<br>
	 * Si le matériau de l'objet n'est pas réflexif, la couleur noire rgb(0, 0, 0) est renvoyée
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur de réflexion
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 *  
	 * @return La couleur réfléchie par le matériau au point d'intersection contenu dans intInfos. Si l'objet n'est pas réflexif, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeReflectionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		//Si l'objet est réfléchissant
		if(intObjMat.getReflectiveCoeff() > 0)
		{
			Vector perfectReflectDirection = intInfos.getReflVec();
			perfectReflectDirection = Vector.normalizeV(perfectReflectDirection);
			
			int summedRed = 0;
			int summedGreen = 0;
			int summedBlue = 0;
			
			int blurSampleCount = (this.settings.isEnableBlurryReflections() && intInfos.getIntObjMat().getRoughness() < 1) ? this.settings.getBlurryReflectionsSampleCount() : 1;
			for(int blurSample = 0; blurSample < blurSampleCount; blurSample++)
			{
				Vector reflectDirection = null;
				
				if(!this.settings.isEnableBlurryReflections() || intInfos.getIntObjMat().getRoughness() == 0)
					reflectDirection = perfectReflectDirection;
				else
				{
					double randomX;
					double randomY;
					double randomZ;
					
					Thread currentThread = Thread.currentThread();
					Random localRandomGenerator = null;
					
					if(currentThread instanceof TileThread)//Si le thread courant n'est pas le thread principal mais un thread de calcul
						localRandomGenerator = ((TileThread)currentThread).getLocalRandomGenerator();//On récupère le générateur de nombre aléatoire local au thread de calcul
					else
						localRandomGenerator = this.randomGenerator;
					
					randomX = localRandomGenerator.nextDouble() * 2 - 1;
					randomY = localRandomGenerator.nextDouble() * 2 - 1;
					randomZ = localRandomGenerator.nextDouble() * 2 - 1;
					
					Vector randomBounce = Vector.normalizeV(Vector.add(Vector.normalizeV(new Vector(randomX, randomY, randomZ)), intInfos.getNormInt()));
					Vector randomBounceDirection = Vector.normalizeV(Vector.interpolate(perfectReflectDirection, randomBounce, intInfos.getIntObjMat().getRoughness()));
					
					reflectDirection = randomBounceDirection;
				}
					
				Color reflectionColor = traceRay(renderScene, new Ray(intInfos.getIntPShift(), reflectDirection), depth - 1);
				
				summedRed += (int)(reflectionColor.getRed()*255);
				summedGreen += (int)(reflectionColor.getGreen()*255);
				summedBlue += (int)(reflectionColor.getBlue()*255);
			}

			Color summedColor = Color.rgb(summedRed / blurSampleCount, summedGreen / blurSampleCount, summedBlue / blurSampleCount);
			return ColorOperations.mulColor(summedColor, intObjMat.getReflectiveCoeff());
		}
		else//Si l'objet n'est pas réfléchissant, on ne renvoie pas de couleur de réflexion --> noir
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * Calcule les couleurs venant des réfractions et des réflexions d'un objet transparent.<br>
	 * Si le matériau de l'objet n'est pas réfractif, la couleur noire rgb(0, 0, 0) est renvoyée 
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur de réfraction
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 * 
	 * @return Le mix de couleur réfléchie et réfractée par le matériau au point d'intersection contenu dans intInfos. Si l'objet n'est pas réfractif, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeRefractionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, double transmittedLightRatio, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		Vector incidentRayDir = intInfos.getRayDir();
		Vector normalAtInter = intInfos.getNormInt();
		
		Point rayInterPoint = intInfos.getIntP();
		
		//TODO (tom) absorption pour la spécularité ? cf shadertoy 
		if (intObjMat.getRefractionIndex() != 0)//L'objet est réfractif 
		{
			Vector refractedRayDir = computeRefractedVector(incidentRayDir, normalAtInter, intObjMat.getRefractionIndex());
			Ray refractedRay = null;
			if (Vector.dotProduct(incidentRayDir, normalAtInter) > 0)
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
			else
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
			
			Color refractedColor = Color.rgb(0,0,0);
			if(intObjMat.getIsTransparent())//L'objet est transparent, on va donc calculer les rayons réfractés à l'intérieur de l'objet
			{
				if (! refractedRayDir.equals(new Vector(0,0,0)) ) {
					refractedColor = traceRay(renderScene, refractedRay, depth -1);
				}
			}
			
			double absorbDistance = intInfos.getTCoeff();
			
			Vector materialAbsorption = intInfos.getIntObjMat().getAbsorption();
	        Vector absorbColor = new Vector(Math.exp(absorbDistance*materialAbsorption.getX()), 
	        								Math.exp(absorbDistance*materialAbsorption.getY()), 
	        								Math.exp(absorbDistance*materialAbsorption.getZ()));
	        
			return ColorOperations.mulColorVector(ColorOperations.mulColor(refractedColor, transmittedLightRatio), absorbColor);
		}
		else//L'objet n'est pas réfractif
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * Calcule la couleur des réflexions de Fresnel aux bords des objets réfractifs
	 * 
	 * @param renderScene La scène utilisée pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur de réfraction
	 * @param reflectedLightRatio Le ratio de lumière réfléchie par le matériau réfractif.
	 * @param depth La profondeur de récursion actuelle de l'algorithme
	 */
	protected Color computeFresnelColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, double reflectedLightRatio, int depth)
	{
		Vector incidentRayDir = intInfos.getRayDir();
		Vector normalAtInter = intInfos.getNormInt();
		
		Point rayInterPoint = intInfos.getIntP();
		
		Ray reflectedRay = null;
		if (Vector.dotProduct(incidentRayDir, normalAtInter) > 0)
			reflectedRay = new Ray(intInfos.getIntPShift(), computeReflectionVector(normalAtInter, incidentRayDir));
		else
			reflectedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, -EPSILON_SHIFT), computeReflectionVector(normalAtInter, incidentRayDir));
		
		Color reflectedColor = Color.rgb(0, 0, 0);
		reflectedColor = traceRay(renderScene, reflectedRay, depth -1);
		
		return ColorOperations.mulColor(reflectedColor, reflectedLightRatio);
	}
	
	/**
	 * Calcule la couleur d'un point de la scène ombragé 
	 * 
	 * @param renderScene 		La scène utilisée pour le rendu
	 * @param intInfos 			Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a été intersecté et dont on souhaite obtenir la couleur du point ombragé de l'objet
	 * @param ambientLighting 	L'intensité de la lumière ambiante de la scène
	 * @param depth 			La profondeur de récursion actuelle de l'algorithme
	 * 
	 * @return Retourne la couleur ombragée au point d'intersection contenu dans intInfos
	 */
	protected Color computeShadow(RayTracingScene renderScene, RayTracerInterInfos intInfos, double reflectedLightRatio, double transmittedLightRatio, double ambientLighting, int depth)
	{
		Color finalColor = Color.rgb(0, 0, 0);

		finalColor = ColorOperations.mulColor(intInfos.getObjCol(), ambientLighting);
		if(this.settings.isEnableReflections())
			finalColor = ColorOperations.addColors(finalColor, computeReflectionsColor(renderScene, intInfos, depth));
		if(this.settings.isEnableRefractions())
			finalColor = ColorOperations.addColors(finalColor, computeRefractionsColor(renderScene, intInfos, transmittedLightRatio, depth));
		if(this.settings.isEnableFresnel()) 
			finalColor = ColorOperations.addColors(finalColor, computeFresnelColor(renderScene, intInfos, reflectedLightRatio, depth));
		
		return finalColor;
	}
	
	/**
	 * Calcule la luminosité ambiante de la scène à partir de l'intensité de la source de lumière et du coefficient de réflexion ambiant du matériau de l'objet
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

	/**
	 * Calcule l'intensité lumineuse diffuse en un point donné de l'image à la surface d'un objet
	 *
	 *  @param toLightDirection 	Vecteur indiquant la direction de la source de lumière
	 *  @param normalAtIntersection Vecteur normal à la surface de l'objet
	 *  @param lightIntensity 		Intensité lumineuse de la source de lumière
	 *
	 *  @return La composante diffuse en un point donné de la scène vis à vis de la surface d'un objet. Réel entre 0 et 1
	 */
	protected double computeDiffuse(Vector toLightDirection, Vector normalAtIntersection, double lightIntensity)
	{
		double dotProdDiffuse = Vector.dotProduct(toLightDirection, normalAtIntersection);
		double diffuseTerm = dotProdDiffuse < 0 ? 0 : lightIntensity*dotProdDiffuse;//Si le dotProduct est négatif, on ne calcule pas le terme diffus --> = 0

		return diffuseTerm;
	}

	/**
	 * Calcule l'intensité lumineuse spéculaire en un point donné de l'image à la surface d'un objet
	 *
	 *  @param incidentRayDirection	Ray incident au point dont on souhaite l'intensité spéculaire
	 *  @param toLightDirection 	Vecteur indiquant la direction de la source de lumière
	 *  @param normalAtIntersection Vecteur normal à la surface de l'objet
	 *  @param lightIntensity 		Intensité lumineuse de la source de lumière
	 *  @param objectShininess 		Brillance de l'objet obtenable avec Shape.getShininess()
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
	
	/**
	 * Calcule un partie de la scène représentée par un pixel de départ X et Y et un pixel d'arrivée X et Y. Le rendu du rectangle de pixel définit par ces valeurs est alors effectué
	 *
	 * @param renderScene La scène de rendu contenant les informations pour rendre l'image
	 * @param startX Le pixel de départ horizontal de la zone de l'image qui doit être calculée. Entre 0 et renderWidth - 1 inclus
	 * @param startY Le pixel de départ vertical de la zone de l'image qui doit être calculée. Entre 0 et renderHeight- 1 inclus
	 * @param endX Le pixel de fin horizontal de la zone de l'image qui doit être calculée. Entre startX + 1 et renderWidth - 1 inclus
	 * @param endY Le pixel de fin vertical de la zone de l'image qui doit être calculée. Entre endY + 1 et renderHeight - 1 inclus
	 */
	protected void computePartialImage(RayTracingScene renderScene, int startX, int startY, int endX, int endY)
	{
		Thread currentThread = Thread.currentThread();
		Random localRandomGenerator = null;
		if(currentThread instanceof TileThread)
			localRandomGenerator = ((TileThread)currentThread).getLocalRandomGenerator();
		else
			localRandomGenerator = this.randomGenerator;
		localRandomGenerator.setSeed(Long.parseLong(String.format("%d%d", startX, startY)));//On réinitialise la graine du générateur pour la tuile à calculer avec un nombre
		//unique construit à partir des coordonnées de départ de la tuile
		
		MatrixD ctwMatrix = renderScene.getCamera().getCTWMatrix();

		double FOV = renderScene.getCamera().getFOV();

		for(int y = startY; y < endY; y++)
		{
			for(int x = startX; x < endX; x++)
			{
				double[][] subpixelTab;//Coordonnées de tous les sous pixels du pixel (x, y) actuel. subpixelTab[i][0] = CoordX, [i][1] = CoordY
				if(this.settings.isEnableAntialiasing() && this.settings.getAntialiasingSampling() > 1)
				{
					subpixelTab = new double[this.settings.getAntialiasingSampling()][2];
					generateSubpixelsCoords(subpixelTab, this.settings.getAntialiasingSampling());
				}
				else
				{
					subpixelTab = new double[1][2];//Un seul sous-pixel sera calculé pour chaque pixel
					subpixelTab[0][0] = 0;
					subpixelTab[0][1] = 0;
				}
					
				int summedRed = 0;
				int summedGreen = 0;
				int summedBlue = 0;
				
				int antialiasingSampleCount = this.settings.isEnableAntialiasing() ? this.settings.getAntialiasingSampling() : 1;
				for(int subpixel = 0; subpixel < antialiasingSampleCount; subpixel++)
				{
					Point pixelWorldCoords = this.convPxCoToWorldCoords(FOV, (double)x + subpixelTab[subpixel][0], (double)y + subpixelTab[subpixel][1], ctwMatrix);
	
					Vector rayDirection = new Vector(renderScene.getCamera().getPosition(), pixelWorldCoords);
					Ray cameraRay = new Ray(MatrixD.mulPointP(new Vector(0, 0, 0), ctwMatrix), rayDirection);
					cameraRay.normalize();
	
					Color subpixelColor = this.traceRay(renderScene, cameraRay, settings.getRecursionDepth());

					summedRed += (int)(subpixelColor.getRed()*255);
					summedGreen += (int)(subpixelColor.getGreen()*255);
					summedBlue += (int)(subpixelColor.getBlue()*255);
				}
				
				summedRed /= antialiasingSampleCount;
				summedGreen /= antialiasingSampleCount;
				summedBlue /= antialiasingSampleCount;
				
				Color pixelColor = Color.rgb(summedRed, summedGreen, summedBlue);
				pixelColor = ColorOperations.linearTosRGBGamma2_2(pixelColor);
				
				this.renderedPixels.put(y*this.renderWidth + x, ColorOperations.aRGB2Int(pixelColor));
				this.totalPixelComputed.incrementAndGet();
			}
		}
	}

	/**
	 * Calcule la couleur d'un pixel grâce à un rayon
	 *
	 * @param renderScene La scène utilisée pour le rendu
	 * @param ray Le camera ray passant par le pixel que l'on veut calculer
	 * @param depth La profondeur maximale de récursion de l'algorithme. Défini entre autre le nombre maximum de reflets consécutifs que l'on peut observer dans deux surface se réfléchissants l'une l'autre
	 *
	 * @return La couleur du pixel que traverse le rayon incident 'ray'
	 */
	protected Color traceRay(RayTracingScene renderScene, Ray ray, int depth)
	{
		this.rtStats.incrementNbRaysShot();
		
		if(depth == 0)
			return Color.BLACK;

		RayTracerInterInfos interInfos = new RayTracerInterInfos();
		
		interInfos.setRay(ray);
		Shape intersectedObject = computeClosestInterPoint(renderScene.getAccelerationStructure(), ray, rtStats, interInfos, true, null);//On détermine l'objet intersecté par le rayon et on stocke sa référence dans la intInfos

		if(intersectedObject != null)//Un objet a bien été intersecté
		{
//			try 
//			{
//				debugOutputFile.write(intersectedObject.toString() + System.lineSeparator());
//				debugOutputFile.write(interInfos.getIntP().toString() + System.lineSeparator());
//				debugOutputFile.write(interInfos.getNormInt().toString() + System.lineSeparator());
//				debugOutputFile.write(System.lineSeparator());
//			} 
//			catch (IOException e) 
//			{
//				e.printStackTrace();
//			}
			
			interInfos.setIntO(intersectedObject);//On set l'objet intersecté dans les informations d'intersection
			
			double lightIntensity = 0;
			for(PositionnalLight light : renderScene.getLights())
				lightIntensity = Math.max(lightIntensity, light.getIntensity());
			
			double ambientLighting = computeAmbient(renderScene.getAmbientLightIntensity(), interInfos.getIntObjMat().getAmbientCoeff());
			if(!this.settings.isEnableAmbient())//Si le calcul de l'ambient n'est pas activé
				ambientLighting = 0;//On définit l'ambient à 0

			Color currentPixelColor = Color.rgb(0, 0, 0);
			Color objectColor = null;
			if(interInfos.getIntObjMat().hasProceduralTexture())
			{
				Point UVCoordsAtInterPoint = interInfos.getIntObj().getUVCoords(interInfos.getIntP());
				
				
				objectColor = interInfos.getIntObjMat().getProceduralTexture().getColorAt(UVCoordsAtInterPoint);
			}
			else
				objectColor = interInfos.getIntObjMat().getColor();
			
			if(interInfos.getIntObj() instanceof Plane && interInfos.getIntObj().getMaterial().hasProceduralTexture())//Si le plan est un checkerboard
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, 1));//Cas spécial pour notre application pour que le plan soit plus illuminé que le reste. Non réaliste mais meilleur aspect visuel. On applique une ambient lighting fixe de 1
			else
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, ambientLighting));

			interInfos.setCurPixCol(currentPixelColor);
			interInfos.setObjCol(objectColor);

			
			
			interInfos.setReflVec(computeReflectionVector(interInfos.getNormInt(), ray.getDirection()));
			interInfos.setIntPShift(Point.translateMul(interInfos.getIntP(), interInfos.getReflVec(), EPSILON_SHIFT));//On translate légèrement le point d'intersection dans la direction d'un  rayon parfaitement réfléchi pour ne pas directement réintersecter l'objet avec lequel nous avons déjà trouvé un point d'intersection
			
			boolean accessToLight = false;//Si le point de la scène que l'on est en train de calculer a un accès direct à une (ou plusieures) source de lumière, alors on n'ombragera pas ce point et cette variable passera à true
			boolean reflectionsDone = false;//Permet de ne pas recalculer les reflexions pour chaque source de lumière
			boolean refractionsDone = false;//Permet de ne calculer les réfractions qu'une seule fois car elles ne dépendent pas des sources de lumière
			boolean fresnelDone = false;
			for(PositionnalLight light : renderScene.getLights())
			{
				interInfos.setToLightVec(Vector.normalizeV(new Vector(interInfos.getIntP(), light.getCenter())));
				
				Vector shadowRayDir = new Vector(interInfos.getIntPShift(), light.getCenter());//On calcule la direction du rayon secondaire qui va droit dans la source de lumière
				double interToLightDist = shadowRayDir.length();//Distance qui sépare le point d'intersection du centre de la lumière
				shadowRayDir.normalize();
				
				interInfos.setShadowRay(new Ray(interInfos.getIntPShift(), shadowRayDir));//Création du rayon secondaire avec pour origine le premier point d'intersection décalé et avec comme direction le centre de la lampe
	
				//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
				Point shadowInterPoint = new Point(0, 0, 0);
				Shape shadowInterObject = computeClosestInterPoint(renderScene.getAccelerationStructure(), interInfos.getShadowRay(), rtStats, null, false, shadowInterPoint);
	
				double interToShadowInterDist = 0;
				if(shadowInterObject != null)
					interToShadowInterDist = Point.distance(interInfos.getIntP(), shadowInterPoint);
	
	
				if(shadowInterObject == null || interToShadowInterDist > interToLightDist || shadowInterObject.getMaterial().getIsTransparent())//Aucune intersection trouvée pour aller jusqu'à la lumière, on peut calculer la couleur directe de l'objet
				{
					accessToLight = true;
					
					if(this.settings.isEnableDiffuse())
						interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeDiffuseColor(interInfos, lightIntensity)));
					if(this.settings.isEnableSpecular())
						interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeSpecularColor(interInfos, lightIntensity)));
					if(this.settings.isEnableReflections() && !reflectionsDone)
					{
						interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeReflectionsColor(renderScene, interInfos, depth)));
						reflectionsDone = true;
					}
					
					if(interInfos.getIntObjMat().getRefractionIndex() > 0)
					{
						double reflectedLightRatio = fresnel(interInfos.getRayDir(), interInfos.getNormInt(), interInfos.getIntObjMat().getRefractionIndex());
						double transmittedLightRatio = 1 - reflectedLightRatio;
						
						if(this.settings.isEnableRefractions() && !refractionsDone)
						{
							interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeRefractionsColor(renderScene, interInfos, transmittedLightRatio, depth)));
							refractionsDone = true;
						}
						if(this.settings.isEnableFresnel() && !fresnelDone)
						{
							interInfos.setCurPixCol(ColorOperations.addColors(interInfos.getCurPixCol(), computeFresnelColor(renderScene, interInfos, reflectedLightRatio, depth)));	
							fresnelDone = true;	
						}
					}
				}
			}
			
			if(!accessToLight)//On vérifie que le point de l'image n'a aucun accès à aucune des lumières de la scène avant de calculer l'ombre
			{
				double reflectedLightRatio = 0;
				double transmittedLightRatio = 0;
				
				if(interInfos.getIntObjMat().getRefractionIndex() > 0)
				{
					reflectedLightRatio = fresnel(interInfos.getRayDir(), interInfos.getNormInt(), interInfos.getIntObjMat().getRefractionIndex());
					transmittedLightRatio = 1 - reflectedLightRatio;
				}
				interInfos.setCurPixCol(computeShadow(renderScene, interInfos, reflectedLightRatio, transmittedLightRatio, ambientLighting, depth));
			}

			return interInfos.getCurPixCol();
		}
		else//Le rayon n'a rien intersecté --> couleur du background / de la skybox
		{
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
	}

	/**
	 * Permet de calculer la prochaine tâche de rendu de la taskList.<br> 
	 * Cette méthode est exécutée par plusieurs threads en même temps.<br>
	 * Elle est public afin que TileThread, la classe des threads puisse appeler cette méthode depuis run() des threads<br> 
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
		synchronized (taskNumber)
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

	/**
	 * Convertit les coordonnées d'un pixel sur le plan de la caméra en coordonnées 3D dans la scène à rendre
	 *
	 * @param FOV Champ de vision de la caméra. Entier entre 1 et 189
	 * @param x Coordonnées x du pixel sur l'image (de 0 à 1919 pour une résolution de 1920 de large par exemple)
	 * @param y Coordonnées y du pixel sur l'image (de 0 à 1079 pour une résolution de 1080 de haut par exemple)
	 * @param ctwMatrix La matrice de passage entre l'espace de la caméra et l'espace de la scène
	 *
	 * @return Un point de cooordonnées (x, y, z) tel que x, y et z représentent les coordonnées du pixel dans la scène
	 */
	protected Point convPxCoToWorldCoords(double FOV, double x, double y, MatrixD ctwMatrix)
	{
		double xWorld = x;
		double yWorld = y;

		double aspectRatio = (double)this.renderWidth / (double)this.renderHeight;
		double demiHeightPlane = Math.tan(Math.toRadians(FOV/2));

		xWorld = xWorld / this.renderWidth;//Normalisation des pixels. Maintenant dans [0, 1]
		xWorld = xWorld * 2 - 1;//Décalage des pixels dans [-1, 1]
		xWorld *= aspectRatio;//Prise en compte de l'aspect ratio. Maintenant dans [-aspectRatio; aspectRatio]
		xWorld *= demiHeightPlane;

		yWorld = yWorld / this.renderHeight;//Normalisation des pixels. Maintenant dans [0, 1]
		yWorld = 1 - yWorld * 2;//Décalage des pixels dans [-1, 1]
		yWorld *= demiHeightPlane;

		Point pixelWorld = new Point(xWorld, yWorld, -1);
		Point pixelWorldConverted = MatrixD.mulPointP(pixelWorld, ctwMatrix);

		return pixelWorldConverted;
	}

	/**
	 * Calcule le rayon réfléchi par la surface en fonction de la position de la lumière par rapport au point d'intersection
	 *
	 * @param normalToSurface Le vecteur normal normalisé de la surface au point d'intersection
	 * @param rayDirection Le vecteur normalisé indiquant la direction du rayon incident 
	 *
	 * @return Le vecteur indiquant la direction d'un rayon de lumière parfaitement réfléchi par la surface de l'objet
	 */
	protected Vector computeReflectionVector(Vector normalToSurface, Vector rayDirection)
	{
		return Vector.sub(rayDirection, Vector.scalarMul(normalToSurface, Vector.dotProduct(normalToSurface, rayDirection)*2));
	}

	/**
	 * Calcule le rayon réfracté par un matériau pour un indice de réfraction donné
	 * 
	 *  @param rayDirection La direction du rayon incident au point d'intersection avec l'objet
	 *  @param normalAtIntersection Le vecteur normal de la surface au point d'intersection
	 *  @param specialMediumRefIndex L'indice de réfraction du matériau réfractif
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
	
	/**
	 * Calcule la proportion de lumière réfléchie et réfractée par un objet réfractif
	 * 
	 * @param incidentRayDirection La direction du rayon incident au point d'intersection de l'objet
	 * @param normalAtIntersection Vecteur normal à la surface de l'objet au point d'intersection
	 * @param specialMediumRefIndex L'indice de réfraction du matériau réfractif
	 * 
	 * @return Retourne la proportion de lumière réfléchie par le matériau étant donne le rayon incident.
	 * La proportion de lumière réfractée peut être déduite comme suit: réfractée = 1 - réfléchie
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
	
	/**
	 * Génère des coordonnées aléatoires entre 0 et 1 et rempli 'subpixelTab' avec ces coordonnées
	 * 
	 * @param subpixelTab 	Le tableau qui va contenir toutes les coordonnées générées. Ce tableau est modifié par la fonction
	 * @param subpixelCount Le nombre de coordonnées de sous pixel à calculer
	 */
	protected void generateSubpixelsCoords(double[][] subpixelTab, int subpixelCount)
	{
		int sqrtSubpixelCount = (int)Math.sqrt(subpixelCount);//Le résultat est forcément entier. subpixelCount est un carré d'entier 
		double subPixelSize = 1.0 / (double)sqrtSubpixelCount;

		for(int subPixelY = 0; subPixelY < sqrtSubpixelCount; subPixelY++)
		{
			for(int subPixelX = 0; subPixelX < sqrtSubpixelCount; subPixelX++)
			{
				subpixelTab[sqrtSubpixelCount * subPixelY + subPixelX][0] = (double)subPixelX*subPixelSize + subPixelSize/2;
				subpixelTab[sqrtSubpixelCount * subPixelY + subPixelX][1] = (double)subPixelY*subPixelSize + subPixelSize/2;
			}
		}
	}
	
	/**
	 * Retourne la progression du rendu de l'image en cours.
	 * 
	 * @return Réel entre 0 et 1 représentant le pourcentage de pixels de l'image ayant été calculés jusqu'à présent 
	 */
	public double getProgression()
	{
		return this.totalPixelComputed.doubleValue() / (double)totalPixelToRender;
	}
	
	/**
	 * Permet d'obtenir le tableau de pixels correspondant à la dernière image rendue par le RayTracer<br>
	 * Si aucune image n'a été rendue au préalable, renvoie null
	 *
	 * @return Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength. Renvoie null si encore aucune image n'a été rendue
	 */
	public IntBuffer getRenderedPixels()
	{
		return this.renderedPixels;
	}
	
	/**
	 * @return Les statistiques du ray tracer. Notamment, combien de rayons ont été lancés, combien d'intersections ont été testées, ...
	 */
	public RayTracerStats getStats()
	{
		return this.rtStats;
	}
	
	/**
	 * @return True si le rendu de l'image actuelle est terminée. False sinon.
	 * Ne s'applique potentiellement que dans le cas où le rayTracer ne calcule qu'une image. En calculant plusieurs images, en temps
	 * réel, le rayTracer sera toujours en train de calculer un rendu et cette fonction renverra false la grande majorité du temps
	 */
	public boolean isRenderDone()
	{
		return this.renderDone;
	}
	
	/**
	 * Calcule le rendu de la scène donnée avec les réglages donnés
	 * 
	 * @param renderScene La scène à rendre
	 * @param renderSettings Les réglages techniques du rendu
	 * 
	 * @return Un IntBuffer de taille renderWidth*renderHeight (spécifiées à la construction du RayTracer) contenant les pixels de l'image rendue
	 */
	public IntBuffer renderImage(RayTracingScene renderScene, RayTracerSettings renderSettings)
	{
		if(!verifRenderScene(renderScene))
		{
			System.out.println("Scène de rendu invalide.");
			return this.getRenderedPixels();//Sera probablement noir puisque la scène n'est pas valide et donc aucun pixel n'a été calculé
		}
		
		this.renderDone = false;
		this.settings = new RayTracerSettings(renderSettings);//On crée une nouvelle instance de RayTracerSettings pour ne pas "lier dynamiquement" les réglages : cela pourrait causer des déchirement d'image lorsqu'on change les réglages pendant un rendu
		this.threadTaskList.initTaskList(renderWidth, renderHeight);
		this.randomGenerator = new Random(0);//On réinitialise le générateur de nombre avec la graine 0
		this.totalPixelComputed.set(0);

		
		
		if(renderScene.hasSkybox())
			this.skyboxPixelReader = renderScene.getSkyboxPixelReader();

		for(int i = 1; i < settings.getNbCore(); i++)//Création des threads sauf 1, le thread principal, qui est déjà créé
			new TileThread(threadTaskList, this, renderScene).start();

		while(threadTaskList.getTotalTaskFinished() < threadTaskList.getTotalTaskCount())
			this.computeTask(renderScene, threadTaskList);

		this.threadTaskList.resetTasksProgression();
		
		this.renderDone = true;
		return this.getRenderedPixels();
	}
	
	/**
	 * Permet de vérifier que la scène est valide et peut être rendue
	 * 
	 * @param renderScene La scène à vérifier
	 * 
	 * @eturn Retourne true si la scène passée en argument est correcte et prête à être rendue. False sinon
	 */
	private boolean verifRenderScene(RayTracingScene renderScene)
	{
		if(renderScene.getCamera() == null)//Pas de caméra
			return false;
		
		if(renderScene.getLights() == null || renderScene.getLights().size() == 0)//Pas de source de lumière
			return false;
		
		if(renderScene.getAccelerationStructure() == null)
			return false;
		
		return true;
	}
}
