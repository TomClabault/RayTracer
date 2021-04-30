package gui.threads;

import java.nio.IntBuffer;

import gui.panes.StatsPane;
import gui.windows.RenderWindowOld;
import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;
import rayTracer.RayTracerStats;

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
			
			this.statsPane.getRayTracerStatsLabel().setText(String.format("Rays cast: %,d\nIntersections tests: %,d", stats.getNbRaysShot(), stats.getIntersectionTestsDone()));
			this.statsPane.getRenderTimeLabel().setText(String.format("Render time: %ds", (currentTimeNano - this.startRenderTimeNano)/1000000000));
		}
	}
}
