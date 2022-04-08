package raytracer.gui.threads;

import java.nio.IntBuffer;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import raytracer.gui.windows.RenderWindowOld;
import raytracer.gui.windows.StatsPane;
import raytracer.rayTracer.RayTracer;
import raytracer.rayTracer.RayTracerStats;

public class RefreshSimpleRenderThread extends AnimationTimer
{
	private RayTracer rayTracer;
	
	private PixelWriter pixelWriter;
	private WritablePixelFormat<IntBuffer> pixelFormat;
	
	private StatsPane statsPane;
	
	private long startRenderTimeNano;
	
	public RefreshSimpleRenderThread(RayTracer rayTracer, PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelBufferFormat, StatsPane statsPane)
	{
		this.rayTracer = rayTracer;
		
		this.pixelWriter = pixelWriter;
		this.pixelFormat = pixelBufferFormat;
		
		this.statsPane = statsPane;
		
		this.startRenderTimeNano = System.nanoTime();
	}

	@Override
	public void handle(long currentTimeNano) 
	{
		RayTracerStats stats = this.rayTracer.getStats();
		
		if(!this.rayTracer.isRenderDone()) 
		{
			RenderWindowOld.drawImage(rayTracer.getRenderedPixels(), this.pixelWriter, this.pixelFormat);
			
			this.statsPane.getRayTracerStatsLabel().setText(String.format("Rays cast: %,d\nIntersections tests: %,d\nCompletion percentage: %.5f", stats.getNbRaysShot(), stats.getIntersectionTestsDone(), ((float)stats.getNbPixelsComputed()/(float)stats.getTotalNbPixel())*100));
			this.statsPane.getRenderTimeLabel().setText(String.format("Render time: %ds", (currentTimeNano - this.startRenderTimeNano)/1000000000));
		}
	}
}