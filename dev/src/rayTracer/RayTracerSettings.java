package rayTracer;

/**
 * 
 * Represente les reglages de rendu qui seront utilises par le ray tracer. Ces reglages sont dynamiques et peuvent etre modifies pendant le rendu.
 * Le ray Tracer adaptera alors son rendu en consequence<br>
 * <br>
 * <strong>Parametres:</strong><br>
 * <ul>
	 * <li>nbCore: Determine le nombre de thread sur lequel sera effectue le rendu des images</li>
	 * <li>recursionDepth: Gere le nombre d'appel recursif maximum que peut faire le ray tracer. Determine par exemple le nombre consecutifs de reflets que l'on peut observer dans deux surfaces se reflechissant l'une l'autre</li>
	 * <li>antialiasingSampling: Determine le nombre de sous-pixel calcule pour chaque pixel afin de reduire l'aliasing (effet d'escalier) de l'image</li>
	 * <li>blurryReflectionsSampleCount: Combien d'echantillons seront utilises pour calculer les reflexion flous de certains materiaux</li>
	 * <li>enableAmbient: Permet d'activer ou de desactiver l'effet de lumiere ambiante de l'ombrage de Phong</li>
	 * <li>enableDiffuse: Permet d'activer ou de desactiver l'effet de lumiere diffuse de l'ombrage de Phong</li>
	 * <li>enableReflections: Permet d'activer ou de desactiver les reflexions des materiaux du rendu</li>
	 * <li>enableBlurryReflections: Permet d'activer / desactiver les reflexions floues des materiaux</li>
	 * <li>enableRefractions: Permet d'activer ou de desactiver les refractions des materiaux du rendu</li>
	 * <li>enableSpecular: Permet d'activer ou de desactiver l'effet de lumiere speculaire de l'ombrage de Phong</li>
	 * <li>enableFresnel: Permet d'activer ou de desactiver les reflets aux bords des objets refractifs</li>
	 * <li>enableAntialiasing: Permet d'activer / desactiver l'antialiasing lors du rendu</li>
 * </ul>
 */
public class RayTracerSettings 
{
	private int nbCore;//Determine le nombre de thread sur lequel sera effectue le rendu des images
	private int recursionDepth;//Gere le nombre d'appel recursif maximum que peut faire le ray tracer. Determine par exemple le nombre consecutifs de reflets que l'on peut observer dans deux surfaces se reflechissant l'une l'autre
	private int antialiasingSampling;//Determine le nombre de sous-pixel calcule pour chaque pixel afin de reduire l'aliasing (effet d'escalier) de l'image
	private int blurryReflectionsSampleCount;//Combien d'echantillons seront utilises pour calculer les reflexion flous de certains materiaux 
	
	private boolean enableAmbient;//Permet d'activer ou de desactiver l'effet de lumiere ambiante de l'ombrage de Phong
	private boolean enableDiffuse;//Permet d'activer ou de desactiver l'effet de lumiere diffuse de l'ombrage de Phong
	private boolean enableReflections;//Permet d'activer ou de desactiver les reflexions des materiaux du rendu
	private boolean enableBlurryReflections;//Permet d'activer / desactiver les reflexions floues des materiaux
	private boolean enableRefractions;//Permet d'activer ou de desactiver les refractions des materiaux du rendu
	private boolean enableSpecular;//Permet d'activer ou de desactiver l'effet de lumiere speculaire de l'ombrage de Phong
	private boolean enableFresnel;//Permet d'activer ou de desactiver les reflets aux bords des objets refractifs
	private boolean enableAntialiasing;//Permet d'activer / desactiver l'antialiasing lors du rendu
	
	public RayTracerSettings()
	{
		this(8, 5, 0, 4, true, true, true, true, true, true, true, false);
	}
	
	public RayTracerSettings(int nbCore, int maxRecursionDepth, int antialiasingSampling)
	{
		this(nbCore, maxRecursionDepth, antialiasingSampling, 4, true, true, true, true, true, true, true, false);
	}
	
	public RayTracerSettings(int nbCore, int maxRecursionDepth, int antialiasingSampling, int blurryReflectionsSampleCount)
	{
		this(nbCore, maxRecursionDepth, antialiasingSampling, blurryReflectionsSampleCount, true, true, true, true, true, true, true, false);
	}

	public RayTracerSettings(RayTracerSettings settingsToCopy)
	{
		this(settingsToCopy.getNbCore(),
			 settingsToCopy.getRecursionDepth(),
			 settingsToCopy.getAntialiasingSampling(),
			 settingsToCopy.getBlurryReflectionsSampleCount(),
			 settingsToCopy.isEnableAmbient(),
			 settingsToCopy.isEnableDiffuse(),
			 settingsToCopy.isEnableReflections(),
			 settingsToCopy.isEnableBlurryReflections(),
			 settingsToCopy.isEnableRefractions(),
			 settingsToCopy.isEnableSpecular(),
			 settingsToCopy.isEnableFresnel(),
			 settingsToCopy.isEnableAntialiasing());
	}
	
	/**
	 * Initialise les reglages qui seront utilies pour le rendu de la scene
	 * 
	 * @param nbCore Le nombre de threads sur lequel sera effectue le rendu
	 * @param recursionDepth La profondeur de recursion maximale autorisee pour le rendu des reflexions / refractions
	 * @param antialiasingSampleCount Le nombre d'echantillons par pixel qui sera utilise pour l'antialiasing de la scene. Doit etre le carre d'un entier >= 2
	 * @param enableAmbient Active ou desactive le calcule de la luminosite ambiante lors du rendu
	 * @param enableDiffuse Active ou desactive le calcule de l'illumination diffuse lors du rendu
	 * @param enableReflections Active ou desactive le calcule des reflexions lors du rendu
	 * @param enableRefractions Active ou desactive le calcule des refractions lors du rendu. Desactivera par exemple le rendu des materiaux tels que le verre i.e. les materiaux transparents qui refractent les rayons de lumiere
	 * @param enableSpecular Active ou desactive le calcule de la specularite lors du rendu
	 * @param enableFresnel Active ou desactive le calcule des reflexions de Fresnel lors du rendu. Ce sont les reflexions a la surface des objets refractifs sans pour autant s'agir de refractions a part entiere.
	 * @param enableAntialiasing Active ou desactive l'antialiasing (anti-crenelage) du rendu. L'antialiasing evite les effets d'escaliers sur les bords des objets.
	 * 
	 * @throws IllegalArgumentException si "antialiasingSampleCount" n'est pas le carre d'un entier >= 2
	 */
	public RayTracerSettings(int nbCore, int recursionDepth, int antialiasingSampleCount, int blurryReflectionsSampleCount, boolean enableAmbient, boolean enableDiffuse, boolean enableReflections, boolean enableBlurryReflections, boolean enableRefractions, boolean enableSpecular, boolean enableFresnel, boolean enableAntialiasing) throws IllegalArgumentException 
	{
		if(!verifAntialiasingSampleCount(antialiasingSampleCount))
			throw new IllegalArgumentException("Nombre d'echantillons d'antialiasing incorrect. Doit etre le carre d'un entier >= 2");
			
		this.nbCore = nbCore;
		this.recursionDepth = recursionDepth;
		this.antialiasingSampling = antialiasingSampleCount;
		this.blurryReflectionsSampleCount = blurryReflectionsSampleCount;
		
		this.enableAmbient = enableAmbient;
		this.enableDiffuse = enableDiffuse;
		this.enableReflections = enableReflections;
		this.enableBlurryReflections = enableBlurryReflections;
		this.enableRefractions = enableRefractions;
		this.enableSpecular = enableSpecular;
		this.enableFresnel = enableFresnel;
		this.enableAntialiasing = enableAntialiasing;
	}

	public boolean isEnableAmbient() 
	{
		return enableAmbient;
	}

	public void enableAmbient(boolean enableAmbient) 
	{
		this.enableAmbient = enableAmbient;
	}
	
	public boolean isEnableAntialiasing() 
	{
		return enableAntialiasing;
	}

	public void enableAntialiasing(boolean enableAntialiasing) 
	{
		this.enableAntialiasing = enableAntialiasing;
	}
	
	public int getBlurryReflectionsSampleCount() 
	{
		return blurryReflectionsSampleCount;
	}

	public void setBlurryReflectionsSampleCount(int blurryReflectionsSampleCount) 
	{
		this.blurryReflectionsSampleCount = blurryReflectionsSampleCount;
	}
	
	public boolean isEnableDiffuse() 
	{
		return enableDiffuse;
	}

	public void enableDiffuse(boolean enableDiffuse) 
	{
		this.enableDiffuse = enableDiffuse;
	}

	public boolean isEnableBlurryReflections() 
	{
		return enableBlurryReflections;
	}

	public void enableBlurryReflections(boolean enableBlurryReflections) 
	{
		this.enableBlurryReflections = enableBlurryReflections;
	}

	public boolean isEnableReflections() 
	{
		return enableReflections;
	}

	public void enableReflections(boolean enableReflections) 
	{
		this.enableReflections = enableReflections;
	}

	public boolean isEnableRefractions() 
	{
		return enableRefractions;
	}

	public void enableRefractions(boolean enableRefractions) 
	{
		this.enableRefractions = enableRefractions;
	}
	
	public boolean isEnableSpecular() 
	{
		return enableSpecular;
	}

	public void enableSpecular(boolean enableSpecular) 
	{
		this.enableSpecular = enableSpecular;
	}

	public boolean isEnableFresnel() 
	{
		return enableFresnel;
	}

	public void enableFresnel(boolean enableFresnel) 
	{
		this.enableFresnel = enableFresnel;
	}
	
	public int getNbCore() 
	{
		return nbCore;
	}
	
	public void setNbCore(int nbCore) 
	{
		this.nbCore = nbCore;
	}
	
	public int getRecursionDepth() 
	{
		return recursionDepth;
	}
	
	public void setRecursionDepth(int recursionDepth) 
	{
		this.recursionDepth = recursionDepth;
	}
	
	public int getAntialiasingSampling() 
	{
		return antialiasingSampling;
	}
	
	/**
	 * Permet de redefinir le nombre d'echantillons calcules par pixels pour l'antiailiasing du rendu
	 * 
	 * @param sampleCount	Le nombre d'echantillons par pixel a utiliser pour l'antialiasing. Doit etre le carre d'un entier >= 2.  
	 * 
	 * @throws IllegalArgumentException Si sampleCount n'est pas le carre d'un entier >= 2
	 */
	public void setAntialiasingSampling(int sampleCount) throws IllegalArgumentException
	{
		if(!verifAntialiasingSampleCount(sampleCount))
			throw new IllegalArgumentException("Nombre d'echantillons d'antialiasing incorrect. Doit etre le carre d'un entier >= 2");
			
		this.antialiasingSampling = sampleCount;
	}
	
	/**
	 * Permet de verifier si sampleCount est le carre d'un entier >= 2
	 * 
	 * @param sampleCount Le nombre d'echantillons par pixel a utliser pour l'antialiasing. Cette methode determine si c'est le carre d'un entier >= 2
	 * 
	 *  @return false si 'sampleCount' n'est pas le carre d'un entier >= 2, true si c'en est un
	 */
	private boolean verifAntialiasingSampleCount(double sampleCount)
	{
		double sqrtDouble = Math.sqrt(sampleCount);
		int sqrtInt = (int)sqrtDouble;
		
		if(sqrtDouble - sqrtInt != 0 || sampleCount < 4)
			return false;
		
		return true;
	}
}
