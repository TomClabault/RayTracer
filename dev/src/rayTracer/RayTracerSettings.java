package rayTracer;

public class RayTracerSettings 
{
	private int nbCore;
	private int recursionDepth;
	private int antialiasingSampling;
	
	public RayTracerSettings(int maxRecursionDepth, int nbCore, int antialiasingSampling)
	{
		this.recursionDepth = maxRecursionDepth;
		this.nbCore = nbCore;
		this.antialiasingSampling = antialiasingSampling;
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
