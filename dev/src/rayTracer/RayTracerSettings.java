package rayTracer;

public class RayTracerSettings 
{
	private int renderWidth;
	private int renderHeight;
	
	private int nbCore;
	private int recursionDepth;
	private int antialiasingSampling;
	
	public RayTracerSettings(int renderWidth, int renderHeight, int maxRecursionDepth, int nbCore, int antialiasingSampling)
	{
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		
		this.recursionDepth = maxRecursionDepth;
		this.nbCore = nbCore;
		this.antialiasingSampling = antialiasingSampling;
	}
	
	public int getRenderWidth() 
	{
		return renderWidth;
	}
	
	public void setRenderWidth(int renderWdith) 
	{
		this.renderWidth = renderWdith;
	}
	
	public int getRenderHeight() 
	{
		return renderHeight;
	}
	
	public void setRenderHeight(int renderHeight) 
	{
		this.renderHeight = renderHeight;
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
