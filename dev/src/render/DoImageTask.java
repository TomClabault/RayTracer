package render;

import java.nio.IntBuffer;
import java.util.concurrent.locks.ReentrantLock;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
 * 
 * Cette classe est une tâche de javafx, elle exécute les calculs de rendu.
 * Elle extends Task pour rendre le calcul indépendant de l'interface et permet de ne pas bloquer l'interface à cause des calculs.
 */
public class DoImageTask extends Task<IntBuffer> {

	private IntBuffer pixelBuffer;
	private RayTracingScene rayTracingScene;
	private Scene mainAppScene;
	private RayTracerSettings rayTracerSettings;
	private RayTracer rayTracer;

	/**
	 * @param mainAppScene 
	 * @param pixelWriter
	 * @param pixelFormat
	 * @param rayTracer
	 * @param rayTracingScene
	 * @param rayTracerSettings
	 */
	DoImageTask(Scene mainAppScene, PixelWriter pixelWriter, WritablePixelFormat<IntBuffer> pixelFormat,RayTracer rayTracer, RayTracingScene rayTracingScene, RayTracerSettings rayTracerSettings) {
		this.mainAppScene = mainAppScene;
		this.rayTracingScene = rayTracingScene;
		this.rayTracer = rayTracer;
		this.rayTracerSettings = rayTracerSettings;
	}

	@Override
	public IntBuffer call() {
		synchronized (mainAppScene) {
			pixelBuffer = rayTracer.renderImage(rayTracingScene, this.rayTracerSettings);
		}
		return pixelBuffer;
	}
}
