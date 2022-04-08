package raytracer.rayTracer;

import java.util.concurrent.atomic.AtomicLong;

public class RayTracerStats 
{
	private AtomicLong nbRaysShot;
	private AtomicLong nbIntersectionTestsDone;
	private AtomicLong nbPixelsComputed;
	
	private AtomicLong totalPixelCount;
	
	public RayTracerStats() 
	{
		this.nbRaysShot = new AtomicLong();
		this.nbIntersectionTestsDone = new AtomicLong();
		this.totalPixelCount = new AtomicLong();
		this.nbPixelsComputed = new AtomicLong();
		
		this.nbRaysShot.set(0);
		this.nbIntersectionTestsDone.set(0);
		this.totalPixelCount.set(0);
		this.nbPixelsComputed.set(0);
	}
	
	public long getIntersectionTestsDone()
	{
		return this.nbIntersectionTestsDone.get();
	}
	
	public long getNbPixelsComputed()
	{
		return this.nbPixelsComputed.get();
	}
	
	public long getNbRaysShot()
	{
		return this.nbRaysShot.get();
	}
	
	public long getTotalNbPixel()
	{
		return this.totalPixelCount.get();
	}
	
	public void incrementIntersectionTestsBy(long increment)
	{
		this.nbIntersectionTestsDone.addAndGet(increment);
	}
	
	public void incrementIntersectionTestsDone()
	{
		this.nbIntersectionTestsDone.incrementAndGet();
	}
	
	public void incrementNbPixelsComputed()
	{
		this.nbPixelsComputed.incrementAndGet();
	}
	
	public void incrementNbRaysShot()
	{
		this.nbRaysShot.incrementAndGet();
	}
	
	public void setTotalPixelCount(long pixelCount)
	{
		this.totalPixelCount.set(pixelCount);
	}
}
