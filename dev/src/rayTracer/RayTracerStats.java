package rayTracer;

import java.util.concurrent.atomic.AtomicLong;

public class RayTracerStats 
{
	private AtomicLong nbRaysShot;
	private AtomicLong nbIntersectionTestsDone;
	
	public RayTracerStats() 
	{
		this.nbRaysShot = new AtomicLong();
		this.nbIntersectionTestsDone = new AtomicLong();
		
		this.nbRaysShot.set(0);
		this.nbIntersectionTestsDone.set(0);
	}
	
	public void incrementNbRaysShot()
	{
		this.nbRaysShot.incrementAndGet();
	}
	
	public void incrementIntersectionTestsDone()
	{
		this.nbIntersectionTestsDone.incrementAndGet();
	}
	
	public void incrementIntersectionTestsBy(long increment)
	{
		this.nbIntersectionTestsDone.addAndGet(increment);
	}
	
	public long getNbRaysShot()
	{
		return this.nbRaysShot.get();
	}
	
	public long getIntersectionTestsDone()
	{
		return this.nbIntersectionTestsDone.get();
	}
}
