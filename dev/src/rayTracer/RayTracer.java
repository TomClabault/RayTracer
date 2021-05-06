package rayTracer;

import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import accelerationStructures.AccelerationStructure;
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
 * Permet d'instancier un ray tracer capable de faire le rendu d'une scene donnee.
 * 
 */
public class RayTracer
{
	/**
	 * Lorsque qu'une intersection est trouvee, cette classe collecte toutes les informations utiles a la suite des calculs 
	 * (normal au point d'intersection, point d'intersection lui meme, l'objet intersecte, la direction du rayon reflechi, ...) 
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
	
	private int renderWidth;
	private int renderHeight;
	private boolean renderDone;
	
	/*
	 * Variables utilisees pour calculer la progression du rendu
	 */
	private AtomicInteger totalPixelComputed;
	private int totalPixelToRender;
	
	private RayTracerSettings settings;
	private RayTracerStats rtStats;
	
	private ThreadsTaskList threadTaskList;
	private IntBuffer renderedPixels;
	private PixelReader skyboxPixelReader = null;
	private Random randomGenerator;//Generateur de nombre aleatoire qui servira uniquement au thread principal
	
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
	 * Calcule le premier point d'intersection du rayon passe en argument avec les objets de la scene
	 *
	 * @param accelStruct 			Structure d'acceleration wrappant les objets de la scene. Le rayon passe en parametre sera tester contre cette
	 * structure d'acceleration
	 * @param ray 					Rayon duquel chercher les points d'intersection avec les objets de la scene
	 * @param intInfos 				Reference vers les informations sur le point d'intersection qui sera eventuellement trouve par un appel a computeClosestInterPoint. Si un point d'intersection est trouve, une partie de l'ensemble des informations relatives sera mise a jour 
	 * @param updateNormalAtInter 	True pour recuperer la normale au point d'intersection dans intInfos passe en argument. Si false, la normale ne sera pas recuperer et l'attribut 'normalAtIntersection' de intInfos restera inchange
	 * @param outClosestInterPoint	Reference vers une instance de Point. Si cette instance est non nulle, elle sera mise a jour si computeClosestInterPoint trouve un point d'intersection entre le rayon et la scene. Si aucun point d'intersection n'est trouve, l'instance restera inchangee
	 *
	 * @return Retourne l'objet avec lequel le rayon a fait son intersection. Si 'outClosestInterPoint' etait non nulle a l'appel de la methode alors outClosestInterPoint contient maintenant le point d'intersection entre le rayon et l'objet renvoye par la methode
	 */
	protected Shape computeClosestInterPoint(AccelerationStructure accelStruct, Ray ray, RayTracerStats interStats, RayTracerInterInfos intInfos, boolean updateNormalAtInter, Point outClosestInterPoint)
	{
		Vector outNormalAtIntersection = new Vector(0, 0, 0);;
		
		Point interPoint = new Point(0, 0, 0);
		Shape closestObjectIntersected = accelStruct.intersect(interStats, ray, interPoint, outNormalAtIntersection);
		if(closestObjectIntersected != null)//On a trouve une intersection
		{
			if(intInfos != null)
			{
				if(updateNormalAtInter)
					intInfos.setNormInt(outNormalAtIntersection);
				intInfos.setIntP(interPoint);
			}
			
			if(outClosestInterPoint != null)//On souhaite recuperer le point d'intersection
				outClosestInterPoint.copyIn(interPoint);
		}
		
		return closestObjectIntersected;
	}

	/**
	 * A partir des informations sur le point d'intersection donnees en entree, calcule et renvoie la couleur de la composante diffuse de l'ombrage de Phong au point d'intersection<br>
	 * Si le materiau de l'objet n'est pas diffus, la couleur noire rgb(0, 0, 0) est renvoyee
	 *
	 * @param intInfos 			Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur diffuse
	 * @param lightIntensity 	Intensite lumineuse de la source de lumiere
	 *  
	 * @return La couleur de la composante diffuse de l'ombrage de Phong au point d'intersection contenu dans intInfos. Si l'objet n'est pas speculaire, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeDiffuseColor(RayTracerInterInfos intInfos, double lightIntensity)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		if(intObjMat.getDiffuseCoeff() > 0)//Si le materiau est diffus
		{
			double diffuseComponent = computeDiffuse(intInfos.getToLightVec(), intInfos.getNormInt(), lightIntensity);
			return ColorOperations.mulColor(intInfos.getObjCol(), diffuseComponent * intObjMat.getDiffuseCoeff());
		}
		else//L'objet n'est pas diffus, on renvoie la couleur inchangee
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * A partir d'une couleur donnee en entree, calcule et renvoie la couleur apres application de la composante speculaire de l'ombrage de Phong pour un materiau et des directions de rayons donnes<br>
	 * Si le materiau de l'objet n'est pas speculaire, la couleur noire rgb(0, 0, 0) est renvoyee
	 * 
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur speculaire
	 * @param lightIntensity Intensite lumineuse de la source de lumiere
	 *  
	 * @return La couleur de la composante speculaire de l'ombrage de Phong au point d'intersection contenu dans intInfos. Si l'objet n'est pas speculaire, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeSpecularColor(RayTracerInterInfos intInfos, double lightIntensity)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		Color specularColor = Color.rgb(255, 255, 255);//TODO bonus: ne pas faire une couleur fixe comme ça mais ajouter une couleur speculaire pour chaque materiaux
		
		if(intObjMat.getSpecularCoeff() > 0)//Si le materiau est speculaire
		{
			double specularTerm = computeSpecular(intInfos.getRayDir(), intInfos.getToLightVec(), intInfos.getNormInt(), lightIntensity, intObjMat.getShininess());
			return ColorOperations.mulColor(specularColor, specularTerm * intObjMat.getSpecularCoeff());
		}
		else//L'objet n'est pas speculaire, on renvoie la couleur inchangee
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * A partir d'une couleur donnee en entree, calcule et renvoie la couleur apres application de la reflectivite du materiau.<br>
	 * Si le materiau de l'objet n'est pas reflexif, la couleur noire rgb(0, 0, 0) est renvoyee
	 * 
	 * @param renderScene La scene utilisee pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur de reflexion
	 * @param depth La profondeur de recursion actuelle de l'algorithme
	 *  
	 * @return La couleur reflechie par le materiau au point d'intersection contenu dans intInfos. Si l'objet n'est pas reflexif, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeReflectionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		//Si l'objet est reflechissant
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
						localRandomGenerator = ((TileThread)currentThread).getLocalRandomGenerator();//On recupere le generateur de nombre aleatoire local au thread de calcul
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
		else//Si l'objet n'est pas reflechissant, on ne renvoie pas de couleur de reflexion --> noir
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * Calcule les couleurs venant des refractions et des reflexions d'un objet transparent.<br>
	 * Si le materiau de l'objet n'est pas refractif, la couleur noire rgb(0, 0, 0) est renvoyee 
	 * 
	 * @param renderScene La scene utilisee pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur de refraction
	 * @param depth La profondeur de recursion actuelle de l'algorithme
	 * 
	 * @return Le mix de couleur reflechie et refractee par le materiau au point d'intersection contenu dans intInfos. Si l'objet n'est pas refractif, renvoie la couleur noire rgb(0, 0, 0)
	 */
	protected Color computeRefractionsColor(RayTracingScene renderScene, RayTracerInterInfos intInfos, double transmittedLightRatio, int depth)
	{
		Material intObjMat = intInfos.getIntObjMat();
		
		Vector incidentRayDir = intInfos.getRayDir();
		Vector normalAtInter = intInfos.getNormInt();
		
		Point rayInterPoint = intInfos.getIntP();
		
		if (intObjMat.getRefractionIndex() != 0)//L'objet est refractif 
		{
			Vector refractedRayDir = computeRefractedVector(incidentRayDir, normalAtInter, intObjMat.getRefractionIndex());
			Ray refractedRay = null;
			if (Vector.dotProduct(incidentRayDir, normalAtInter) > 0)
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
			else
				refractedRay = new Ray(Point.translateMul(rayInterPoint, incidentRayDir, EPSILON_SHIFT), refractedRayDir);
			
			Color refractedColor = Color.rgb(0,0,0);
			if(intObjMat.getIsTransparent())//L'objet est transparent, on va donc calculer les rayons refractes a l'interieur de l'objet
			{
				if (! refractedRayDir.equals(new Vector(0,0,0)) ) {
					refractedColor = traceRay(renderScene, refractedRay, depth -1);
				}
			}
			
			return ColorOperations.mulColor(refractedColor, transmittedLightRatio);
		}
		else//L'objet n'est pas refractif
			return Color.rgb(0, 0, 0);
	}
	
	/**
	 * Calcule la couleur des reflexions de Fresnel aux bords des objets refractifs
	 * 
	 * @param renderScene La scene utilisee pour le rendu
	 * @param intInfos Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur de refraction
	 * @param reflectedLightRatio Le ratio de lumiere reflechie par le materiau refractif.
	 * @param depth La profondeur de recursion actuelle de l'algorithme
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
	 * Calcule la couleur d'un point de la scene ombrage 
	 * 
	 * @param renderScene 		La scene utilisee pour le rendu
	 * @param intInfos 			Ensemble des informations sur le point d'intersection entre le rayon incident et l'objet qui a ete intersecte et dont on souhaite obtenir la couleur du point ombrage de l'objet
	 * @param ambientLighting 	L'intensite de la lumiere ambiante de la scene
	 * @param depth 			La profondeur de recursion actuelle de l'algorithme
	 * 
	 * @return Retourne la couleur ombragee au point d'intersection contenu dans intInfos
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
	 * Calcule la luminosite ambiante de la scene a partir de l'intensite de la source de lumiere et du coefficient de reflexion ambiant du materiau de l'objet
	 *
	 * @param ambientLightIntensity Intensite de la luminosite ambiante de la scene
	 * @param materialAmbientCoeff Coefficient de reflexion de la lumiere ambiante du materiau
	 *
	 * @return Retourne la composante ambiante du materiau dont le coefficient ambiant a ete passe en parametre. Reel entre 0 et 1
	 */
	protected double computeAmbient(double ambientLightIntensity, double materialAmbientCoeff)
	{
		return ambientLightIntensity * materialAmbientCoeff;
	}

	/**
	 * Calcule l'intensite lumineuse diffuse en un point donne de l'image a la surface d'un objet
	 *
	 *  @param toLightDirection 	Vecteur indiquant la direction de la source de lumiere
	 *  @param normalAtIntersection Vecteur normal a la surface de l'objet
	 *  @param lightIntensity 		Intensite lumineuse de la source de lumiere
	 *
	 *  @return La composante diffuse en un point donne de la scene vis a vis de la surface d'un objet. Reel entre 0 et 1
	 */
	protected double computeDiffuse(Vector toLightDirection, Vector normalAtIntersection, double lightIntensity)
	{
		double dotProdDiffuse = Vector.dotProduct(toLightDirection, normalAtIntersection);
		double diffuseTerm = dotProdDiffuse < 0 ? 0 : lightIntensity*dotProdDiffuse;//Si le dotProduct est negatif, on ne calcule pas le terme diffus --> = 0

		return diffuseTerm;
	}

	/**
	 * Calcule l'intensite lumineuse speculaire en un point donne de l'image a la surface d'un objet
	 *
	 *  @param incidentRayDirection	Ray incident au point dont on souhaite l'intensite speculaire
	 *  @param toLightDirection 	Vecteur indiquant la direction de la source de lumiere
	 *  @param normalAtIntersection Vecteur normal a la surface de l'objet
	 *  @param lightIntensity 		Intensite lumineuse de la source de lumiere
	 *  @param objectShininess 		Brillance de l'objet obtenable avec Shape.getShininess()
	 *
	 *  @return La composante speculaire en un point donne de la scene vis a vis de la surface d'un objet. Reel entre 0 et 1.
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
	 * Calcule un partie de la scene representee par un pixel de depart X et Y et un pixel d'arrivee X et Y. Le rendu du rectangle de pixel definit par ces valeurs est alors effectue
	 *
	 * @param renderScene La scene de rendu contenant les informations pour rendre l'image
	 * @param startX Le pixel de depart horizontal de la zone de l'image qui doit etre calculee. Entre 0 et renderWidth - 1 inclus
	 * @param startY Le pixel de depart vertical de la zone de l'image qui doit etre calculee. Entre 0 et renderHeight- 1 inclus
	 * @param endX Le pixel de fin horizontal de la zone de l'image qui doit etre calculee. Entre startX + 1 et renderWidth - 1 inclus
	 * @param endY Le pixel de fin vertical de la zone de l'image qui doit etre calculee. Entre endY + 1 et renderHeight - 1 inclus
	 */
	protected void computePartialImage(RayTracingScene renderScene, int startX, int startY, int endX, int endY)
	{
		Thread currentThread = Thread.currentThread();
		Random localRandomGenerator = null;
		if(currentThread instanceof TileThread)
			localRandomGenerator = ((TileThread)currentThread).getLocalRandomGenerator();
		else
			localRandomGenerator = this.randomGenerator;
		localRandomGenerator.setSeed(Long.parseLong(String.format("%d%d", startX, startY)));//On reinitialise la graine du generateur pour la tuile a calculer avec un nombre
		//unique construit a partir des coordonnees de depart de la tuile
		
		MatrixD ctwMatrix = renderScene.getCamera().getCTWMatrix();

		double FOV = renderScene.getCamera().getFOV();

		for(int y = startY; y < endY; y++)
		{
			for(int x = startX; x < endX; x++)
			{
				double[][] subpixelTab;//Coordonnees de tous les sous pixels du pixel (x, y) actuel. subpixelTab[i][0] = CoordX, [i][1] = CoordY
				if(this.settings.isEnableAntialiasing() && this.settings.getAntialiasingSampling() > 1)
				{
					subpixelTab = new double[this.settings.getAntialiasingSampling()][2];
					generateSubpixelsCoords(subpixelTab, this.settings.getAntialiasingSampling());
				}
				else
				{
					subpixelTab = new double[1][2];//Un seul sous-pixel sera calcule pour chaque pixel
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
	 * Calcule la couleur d'un pixel grâce a un rayon
	 *
	 * @param renderScene La scene utilisee pour le rendu
	 * @param ray Le camera ray passant par le pixel que l'on veut calculer
	 * @param depth La profondeur maximale de recursion de l'algorithme. Defini entre autre le nombre maximum de reflets consecutifs que l'on peut observer dans deux surface se reflechissants l'une l'autre
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
		Shape intersectedObject = computeClosestInterPoint(renderScene.getAccelerationStructure(), ray, rtStats, interInfos, true, null);//On determine l'objet intersecte par le rayon et on stocke sa reference dans la intInfos

		if(intersectedObject != null)//Un objet a bien ete intersecte
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
			
			interInfos.setIntO(intersectedObject);//On set l'objet intersecte dans les informations d'intersection
			
			double lightIntensity = 0;
			for(PositionnalLight light : renderScene.getLights())
				lightIntensity = Math.max(lightIntensity, light.getIntensity());
			
			double ambientLighting = computeAmbient(renderScene.getAmbientLightIntensity(), interInfos.getIntObjMat().getAmbientCoeff());
			if(!this.settings.isEnableAmbient())//Si le calcul de l'ambient n'est pas active
				ambientLighting = 0;//On definit l'ambient a 0

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
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, 1));//Cas special pour notre application pour que le plan soit plus illumine que le reste. Non realiste mais meilleur aspect visuel. On applique une ambient lighting fixe de 1
			else
				currentPixelColor = ColorOperations.addColors(currentPixelColor, ColorOperations.mulColor(objectColor, ambientLighting));

			interInfos.setCurPixCol(currentPixelColor);
			interInfos.setObjCol(objectColor);

			
			
			interInfos.setReflVec(computeReflectionVector(interInfos.getNormInt(), ray.getDirection()));
			interInfos.setIntPShift(Point.translateMul(interInfos.getIntP(), interInfos.getReflVec(), EPSILON_SHIFT));//On translate legerement le point d'intersection dans la direction d'un  rayon parfaitement reflechi pour ne pas directement reintersecter l'objet avec lequel nous avons deja trouve un point d'intersection
			
			boolean accessToLight = false;//Si le point de la scene que l'on est en train de calculer a un acces direct a une (ou plusieures) source de lumiere, alors on n'ombragera pas ce point et cette variable passera a true
			boolean reflectionsDone = false;//Permet de ne pas recalculer les reflexions pour chaque source de lumiere
			boolean refractionsDone = false;//Permet de ne calculer les refractions qu'une seule fois car elles ne dependent pas des sources de lumiere
			boolean fresnelDone = false;
			for(PositionnalLight light : renderScene.getLights())
			{
				interInfos.setToLightVec(Vector.normalizeV(new Vector(interInfos.getIntP(), light.getCenter())));
				
				Vector shadowRayDir = new Vector(interInfos.getIntPShift(), light.getCenter());//On calcule la direction du rayon secondaire qui va droit dans la source de lumiere
				double interToLightDist = shadowRayDir.length();//Distance qui separe le point d'intersection du centre de la lumiere
				shadowRayDir.normalize();
				
				interInfos.setShadowRay(new Ray(interInfos.getIntPShift(), shadowRayDir));//Creation du rayon secondaire avec pour origine le premier point d'intersection decale et avec comme direction le centre de la lampe
	
				//On cherche une intersection avec un objet qui se trouverait entre la lampe et l'origine du shadow ray
				Point shadowInterPoint = new Point(0, 0, 0);
				Shape shadowInterObject = computeClosestInterPoint(renderScene.getAccelerationStructure(), interInfos.getShadowRay(), rtStats, null, false, shadowInterPoint);
	
				double interToShadowInterDist = 0;
				if(shadowInterObject != null)
					interToShadowInterDist = Point.distance(interInfos.getIntP(), shadowInterPoint);
	
	
				if(shadowInterObject == null || interToShadowInterDist > interToLightDist || shadowInterObject.getMaterial().getIsTransparent())//Aucune intersection trouvee pour aller jusqu'a la lumiere, on peut calculer la couleur directe de l'objet
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
			
			if(!accessToLight)//On verifie que le point de l'image n'a aucun acces a aucune des lumieres de la scene avant de calculer l'ombre
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
		else//Le rayon n'a rien intersecte --> couleur du background / de la skybox
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
	 * Cette methode est executee par plusieurs threads en meme temps.<br>
	 * Elle est public afin que TileThread, la classe des threads puisse appeler cette methode depuis run() des threads<br> 
	 * 
	 *  @param renderScene La scene qui doit etre rendue par le rayTracer
	 *  @param taskList La liste de tâche prealablement initialisee
	 *  
	 *  @return Retourne true si le thread a calcule une tâche avec succes. false sinon, i.e., le thread n'a pas calcule de tâche car il n'y en avait plus a calcuelr
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
		
		return true;//Encore des tuiles a calculer
	}

	/**
	 * Convertit les coordonnees d'un pixel sur le plan de la camera en coordonnees 3D dans la scene a rendre
	 *
	 * @param FOV Champ de vision de la camera. Entier entre 1 et 189
	 * @param x Coordonnees x du pixel sur l'image (de 0 a 1919 pour une resolution de 1920 de large par exemple)
	 * @param y Coordonnees y du pixel sur l'image (de 0 a 1079 pour une resolution de 1080 de haut par exemple)
	 * @param ctwMatrix La matrice de passage entre l'espace de la camera et l'espace de la scene
	 *
	 * @return Un point de cooordonnees (x, y, z) tel que x, y et z representent les coordonnees du pixel dans la scene
	 */
	protected Point convPxCoToWorldCoords(double FOV, double x, double y, MatrixD ctwMatrix)
	{
		double xWorld = x;
		double yWorld = y;

		double aspectRatio = (double)this.renderWidth / (double)this.renderHeight;
		double demiHeightPlane = Math.tan(Math.toRadians(FOV/2));

		xWorld = xWorld / this.renderWidth;//Normalisation des pixels. Maintenant dans [0, 1]
		xWorld = xWorld * 2 - 1;//Decalage des pixels dans [-1, 1]
		xWorld *= aspectRatio;//Prise en compte de l'aspect ratio. Maintenant dans [-aspectRatio; aspectRatio]
		xWorld *= demiHeightPlane;

		yWorld = yWorld / this.renderHeight;//Normalisation des pixels. Maintenant dans [0, 1]
		yWorld = 1 - yWorld * 2;//Decalage des pixels dans [-1, 1]
		yWorld *= demiHeightPlane;

		Point pixelWorld = new Point(xWorld, yWorld, -1);
		Point pixelWorldConverted = MatrixD.mulPointP(pixelWorld, ctwMatrix);

		return pixelWorldConverted;
	}

	/**
	 * Calcule le rayon reflechi par la surface en fonction de la position de la lumiere par rapport au point d'intersection
	 *
	 * @param normalToSurface Le vecteur normal normalise de la surface au point d'intersection
	 * @param rayDirection Le vecteur normalise indiquant la direction du rayon incident 
	 *
	 * @return Le vecteur indiquant la direction d'un rayon de lumiere parfaitement reflechi par la surface de l'objet
	 */
	protected Vector computeReflectionVector(Vector normalToSurface, Vector rayDirection)
	{
		return Vector.sub(rayDirection, Vector.scalarMul(normalToSurface, Vector.dotProduct(normalToSurface, rayDirection)*2));
	}

	/**
	 * Calcule le rayon refracte par un materiau pour un indice de refraction donne
	 * 
	 *  @param rayDirection La direction du rayon incident au point d'intersection avec l'objet
	 *  @param normalAtIntersection Le vecteur normal de la surface au point d'intersection
	 *  @param specialMediumRefIndex L'indice de refraction du materiau refractif
	 *  
	 *  @return Retourne la direction du rayon refracte
	 */
	protected Vector computeRefractedVector(Vector rayDirection, Vector normalAtIntersection, double specialMediumRefIndex)
	{
		double startRefractionIndex = AIR_REFRACTION_INDEX;
		Vector newNormal = new Vector(0,0,0);
		newNormal.copyIn(normalAtIntersection);
		
		if (Vector.dotProduct(rayDirection, normalAtIntersection) > 0)//Si on est a l'interieur de l'objet, il faut echanger les deux indices de refraction
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
	 * Calcule la proportion de lumiere reflechie et refractee par un objet refractif
	 * 
	 * @param incidentRayDirection La direction du rayon incident au point d'intersection de l'objet
	 * @param normalAtIntersection Vecteur normal a la surface de l'objet au point d'intersection
	 * @param specialMediumRefIndex L'indice de refraction du materiau refractif
	 * 
	 * @return Retourne la proportion de lumiere reflechie par le materiau etant donne le rayon incident.
	 * La proportion de lumiere refractee peut etre deduite comme suit: refractee = 1 - reflechie
	 */
	protected double fresnel(Vector incidentRayDirection, Vector normalAtIntersection, double specialMediumRefIndex) 
	{
		double incomingRefractionIndex = AIR_REFRACTION_INDEX;
		
		if (Vector.dotProduct(incidentRayDirection, normalAtIntersection) > 0)//Le rayon est a l'interieur de l'objet
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
	 * Genere des coordonnees aleatoires entre 0 et 1 et rempli 'subpixelTab' avec ces coordonnees
	 * 
	 * @param subpixelTab 	Le tableau qui va contenir toutes les coordonnees generees. Ce tableau est modifie par la fonction
	 * @param subpixelCount Le nombre de coordonnees de sous pixel a calculer
	 */
	protected void generateSubpixelsCoords(double[][] subpixelTab, int subpixelCount)
	{
		int sqrtSubpixelCount = (int)Math.sqrt(subpixelCount);//Le resultat est forcement entier. subpixelCount est un carre d'entier 
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
	 * @return Reel entre 0 et 1 representant le pourcentage de pixels de l'image ayant ete calcules jusqu'a present 
	 */
	public double getProgression()
	{
		return this.totalPixelComputed.doubleValue() / (double)totalPixelToRender;
	}
	
	/**
	 * Permet d'obtenir le tableau de pixels correspondant a la derniere image rendue par le RayTracer<br>
	 * Si aucune image n'a ete rendue au prealable, renvoie null
	 *
	 * @return Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength. Renvoie null si encore aucune image n'a ete rendue
	 */
	public IntBuffer getRenderedPixels()
	{
		return this.renderedPixels;
	}
	
	/**
	 * @return Les statistiques du ray tracer. Notamment, combien de rayons ont ete lances, combien d'intersections ont ete testees, ...
	 */
	public RayTracerStats getStats()
	{
		return this.rtStats;
	}
	
	/**
	 * @return True si le rendu de l'image actuelle est terminee. False sinon.
	 * Ne s'applique potentiellement que dans le cas où le rayTracer ne calcule qu'une image. En calculant plusieurs images, en temps
	 * reel, le rayTracer sera toujours en train de calculer un rendu et cette fonction renverra false la grande majorite du temps
	 */
	public boolean isRenderDone()
	{
		return this.renderDone;
	}
	
	/**
	 * Calcule le rendu de la scene donnee avec les reglages donnes
	 * 
	 * @param renderScene La scene a rendre
	 * @param renderSettings Les reglages techniques du rendu
	 * 
	 * @return Un IntBuffer de taille renderWidth*renderHeight (specifiees a la construction du RayTracer) contenant les pixels de l'image rendue
	 */
	public IntBuffer renderImage(RayTracingScene renderScene, RayTracerSettings renderSettings)
	{
		if(!verifRenderScene(renderScene))
		{
			System.out.println("Scene de rendu invalide.");
			return this.getRenderedPixels();//Sera probablement noir puisque la scene n'est pas valide et donc aucun pixel n'a ete calcule
		}
		
		this.renderDone = false;
		this.settings = new RayTracerSettings(renderSettings);//On cree une nouvelle instance de RayTracerSettings pour ne pas "lier dynamiquement" les reglages : cela pourrait causer des dechirement d'image lorsqu'on change les reglages pendant un rendu
		this.threadTaskList.initTaskList(renderWidth, renderHeight);
		this.randomGenerator = new Random(0);//On reinitialise le generateur de nombre avec la graine 0
		this.totalPixelComputed.set(0);

		
		
		if(renderScene.hasSkybox())
			this.skyboxPixelReader = renderScene.getSkyboxPixelReader();

		for(int i = 1; i < settings.getNbCore(); i++)//Creation des threads sauf 1, le thread principal, qui est deja cree
			new TileThread(threadTaskList, this, renderScene).start();

		while(threadTaskList.getTotalTaskFinished() < threadTaskList.getTotalTaskCount())
			this.computeTask(renderScene, threadTaskList);

		this.threadTaskList.resetTasksProgression();
		
		this.renderDone = true;
		return this.getRenderedPixels();
	}
	
	/**
	 * Permet de verifier que la scene est valide et peut etre rendue
	 * 
	 * @param renderScene La scene a verifier
	 * 
	 * @eturn Retourne true si la scene passee en argument est correcte et prete a etre rendue. False sinon
	 */
	private boolean verifRenderScene(RayTracingScene renderScene)
	{
		if(renderScene.getCamera() == null)//Pas de camera
			return false;
		
		if(renderScene.getLights() == null || renderScene.getLights().size() == 0)//Pas de source de lumiere
			return false;
		
		if(renderScene.getAccelerationStructure() == null)
			return false;
		
		return true;
	}
}
