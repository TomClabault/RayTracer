package gui.threads;

import java.nio.IntBuffer;

import javafx.concurrent.Task;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
 * 
 * Cette classe est une tâche de javafx, elle execute les calculs de rendu.
 * Elle extends Task pour rendre le calcul independant de l'interface et permet de ne pas bloquer l'interface a cause des calculs.
 */
public class RenderTask extends Task<IntBuffer> {

	private IntBuffer pixelBuffer;
	
	private RayTracingScene rayTracingScene;
	private RayTracerSettings rayTracerSettings;
	private RayTracer rayTracer;
	
	/**
	 * @param pixelWriter
	 * @param pixelFormat
	 * @param rayTracer
	 * @param rayTracingScene
	 * @param rayTracerSettings
	 */
	public RenderTask(PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelFormat,RayTracer rayTracer, RayTracingScene rayTracingScene, RayTracerSettings rayTracerSettings) {
		this.rayTracingScene = rayTracingScene;
		this.rayTracer = rayTracer;
		this.rayTracerSettings = rayTracerSettings;
	}

	@Override
	public IntBuffer call() 
	{
		synchronized(rayTracingScene)
		{
			pixelBuffer = rayTracer.renderImage(rayTracingScene, this.rayTracerSettings);
		}
		
		return pixelBuffer;
	}
}
