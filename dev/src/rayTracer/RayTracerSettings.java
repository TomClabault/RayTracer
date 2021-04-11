package rayTracer;

public class RayTracerSettings 
{
	private int nbCore;//Détermine le nombre de thread sur lequel sera effectué le rendu des images
	private int recursionDepth;//Gère le nombre d'appel récursif maximum que peut faire le ray tracer. Détermine par exemple le nombre consécutifs de reflets que l'on peut observer dans deux surfaces se réfléchissant l'une l'autre  
	private int antialiasingSampling;//Détermine le nombre de sous-pixel calculé pour chaque pixel afin de réduire l'aliasing (effet d'escalier) de l'image
	
	private boolean enableAmbient;//Permet d'activer ou de désactiver l'effet de lumière ambiante de l'ombrage de Phong
	private boolean enableDiffuse;//Permet d'activer ou de désactiver l'effet de lumière diffuse de l'ombrage de Phong
	private boolean enableReflections;//Permet d'activer ou de désactiver les réflexions des matériaux du rendu
	private boolean enableRefractions;//Permet d'activer ou de désactiver les réfractions des matériaux du rendu
	private boolean enableSpecular;//Permet d'activer ou de désactiver l'effet de lumière spéculaire de l'ombrage de Phong
	private boolean enableFresnel;//Permet d'activer ou de désactiver les reflets aux bords des objets réfractifs
	private boolean enableAntialiasing;//Permet d'activer / désactiver l'antialiasing lors du rendu

	public RayTracerSettings()
	{
		this(8, 5, 0, true, true, true, true, true, true, true);
	}
	
	public RayTracerSettings(int nbCore, int maxRecursionDepth, int antialiasingSampling)
	{
		this(nbCore, maxRecursionDepth, antialiasingSampling, true, true, true, true, true, true, false);
	}

	/*
	 * Initialise les réglages qui seront utiliés pour le rendu de la scène
	 * 
	 * @param nbCore Le nombre de threads sur lequel sera effectué le rendu
	 * @param recursionDepth La profondeur de récursion maximale autorisée pour le rendu des réflexions / réfractions
	 * @param antialiasingSampleCount Le nombre d'échantillons par pixel qui sera utilisé pour l'antialiasing de la scène. Doit être le carré d'un entier >= 2
	 * @param enableAmbient Active ou désactive le calcule de la luminosité ambiante lors du rendu
	 * @param enableDiffuse Active ou désactive le calcule de l'illumination diffuse lors du rendu
	 * @param enableReflections Active ou désactive le calcule des réflexions lors du rendu
	 * @param enableRefractions Active ou désactive le calcule des réfractions lors du rendu. Désactivera par exemple le rendu des matériaux tels que le verre i.e. les matériaux transparents qui réfractent les rayons de lumière
	 * @param enableSpecular Active ou désactive le calcule de la spécularité lors du rendu
	 * @param enableFresnel Active ou désactive le calcule des réflexions de Fresnel lors du rendu. Ce sont les réflexions à la surface des objets réfractifs sans pour autant s'agir de réfractions à part entière.
	 * @param enableAntialiasing Active ou désactive l'antialiasing (anti-crénelage) du rendu. L'antialiasing évite les effets d'escaliers sur les bords des objets.
	 * 
	 * @throws IllegalArgumentException si "antialiasingSampleCount" n'est pas le carré d'un entier >= 2
	 */
	public RayTracerSettings(int nbCore, int recursionDepth, int antialiasingSampleCount, boolean enableAmbient, boolean enableDiffuse, boolean enableReflections, boolean enableRefractions, boolean enableSpecular, boolean enableFresnel, boolean enableAntialiasing) throws IllegalArgumentException 
	{
		if(!verifAntialiasingSampleCount(antialiasingSampleCount))
			throw new IllegalArgumentException("Nombre d'échantillons d'antialiasing incorrect. Doit être le carré d'un entier >= 2");
			
		this.nbCore = nbCore;
		this.recursionDepth = recursionDepth;
		this.antialiasingSampling = antialiasingSampleCount;
		
		this.enableAmbient = enableAmbient;
		this.enableDiffuse = enableDiffuse;
		this.enableReflections = enableReflections;
		this.enableRefractions = enableRefractions;
		this.enableSpecular = enableSpecular;
		this.enableFresnel = enableFresnel;
		this.enableAntialiasing = enableAntialiasing;
	}

	public boolean isEnableAmbient() 
	{
		return enableAmbient;
	}

	public void setEnableAmbient(boolean enableAmbient) 
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
	
	public boolean isEnableDiffuse() 
	{
		return enableDiffuse;
	}

	public void setEnableDiffuse(boolean enableDiffuse) 
	{
		this.enableDiffuse = enableDiffuse;
	}

	public boolean isEnableReflections() 
	{
		return enableReflections;
	}

	public void setEnableReflections(boolean enableReflections) 
	{
		this.enableReflections = enableReflections;
	}

	public boolean isEnableRefractions() 
	{
		return enableRefractions;
	}

	public void setEnableRefractions(boolean enableRefractions) 
	{
		this.enableRefractions = enableRefractions;
	}
	
	public boolean isEnableSpecular() 
	{
		return enableSpecular;
	}

	public void setEnableSpecular(boolean enableSpecular) 
	{
		this.enableSpecular = enableSpecular;
	}

	public boolean isEnableFresnel() 
	{
		return enableFresnel;
	}

	public void setEnableFresnel(boolean enableFresnel) 
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
	
	/*
	 * Permet de redéfinir le nombre d'échantillons calculés par pixels pour l'antiailiasing du rendu
	 * 
	 * @param sampleCount	Le nombre d'échantillons par pixel à utiliser pour l'antialiasing. Doit être le carré d'un entier >= 2.  
	 * 
	 * @throws IllegalArgumentException Si sampleCount n'est pas le carré d'un entier >= 2
	 */
	public void setAntialiasingSampling(int sampleCount) throws IllegalArgumentException
	{
		if(!verifAntialiasingSampleCount(sampleCount))
			throw new IllegalArgumentException("Nombre d'échantillons d'antialiasing incorrect. Doit être le carré d'un entier >= 2");
			
		this.antialiasingSampling = sampleCount;
	}
	
	/*
	 * Permet de vérifier si sampleCount est le carré d'un entier >= 2
	 * 
	 * @param sampleCount Le nombre d'échantillons par pixel à utliser pour l'antialiasing. Cette méthode détermine si c'est le carré d'un entier >= 2
	 * 
	 *  @return false si 'sampleCount' n'est pas le carré d'un entier >= 2, true si c'en est un
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
