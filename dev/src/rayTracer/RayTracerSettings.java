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
		this(nbCore, maxRecursionDepth, antialiasingSampling, true, true, true, true, true, true, true);
	}
	
	public RayTracerSettings(int nbCore, int recursionDepth, int antialiasingSampling, boolean enableAmbient, boolean enableDiffuse, boolean enableReflections, boolean enableRefractions, boolean enableSpecular, boolean enableFresnel, boolean enableAntialiasing) 
	{
		this.nbCore = nbCore;
		this.recursionDepth = recursionDepth;
		this.antialiasingSampling = antialiasingSampling;
		
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

	public void setEnableAntialiasing(boolean enableAntialiasing) 
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
	
	public void setAntialiasingSampling(int antialiasingSampling) 
	{
		this.antialiasingSampling = antialiasingSampling;
	}
}
