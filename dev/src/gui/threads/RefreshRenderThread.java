package gui.threads;

import java.nio.IntBuffer;

import gui.windows.RenderWindowOld;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;

public class RefreshRenderThread extends Thread
{
	private RayTracer rayTracer;
	
	private PixelWriter pixelWriter;
	private WritablePixelFormat<IntBuffer> pixelFormat;
	
	public RefreshRenderThread(RayTracer rayTracer, PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelBufferFormat)
	{
		this.rayTracer = rayTracer;
		
		this.pixelWriter = pixelWriter;
		this.pixelFormat = pixelBufferFormat;
	}
	
	@Override
	public void run()
	{
		while(!this.rayTracer.isRenderDone())
			RenderWindowOld.doImage(rayTracer.getRenderedPixels(), this.pixelWriter, this.pixelFormat);
	}
}
