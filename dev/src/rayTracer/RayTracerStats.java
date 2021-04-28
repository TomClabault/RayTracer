package rayTracer;

public class RayTracerStats 
{
	private long nbRaysShot;
	private long nbIntersectionTestsDone;
	
	public RayTracerStats() 
	{
		this.nbRaysShot = 0;
		this.nbIntersectionTestsDone = 0;
	}
	
	public void incrementNbRaysShot()
	{
		this.nbRaysShot++;
	}
	
	public void incrementIntersectionTestsDone()
	{
		this.nbIntersectionTestsDone++;
	}
	
	public long getNbRaysShot()
	{
		return this.nbRaysShot;
	}
	
	public long getIntersectionTestsDone()
	{
		return this.nbIntersectionTestsDone;
	}
}
