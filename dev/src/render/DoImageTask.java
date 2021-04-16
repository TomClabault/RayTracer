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

public class DoImageTask extends Task<IntBuffer> {
	
	private IntBuffer pixelBuffer;
	private RayTracingScene rayTracingScene;
	private RayTracer rayTracerInstance;
	private Scene mainAppScene;
	private RayTracerSettings rayTracerSettings;
	DoImageTask(Scene mainAppScene, PixelWriter pw, WritablePixelFormat<IntBuffer> pixelFormat, RayTracingScene rts, RayTracerSettings rayTracerSettings) {
		this.mainAppScene = mainAppScene;
		this.rayTracingScene = rts;
		this.rayTracerInstance = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);
		this.rayTracerSettings = rayTracerSettings;
	}
	
	//public WindowTimer getWindowTimer() {
	//	return this.windowTimer;
	//}
	
	
	@Override
	public IntBuffer call() {
		synchronized(mainAppScene)
		{
			pixelBuffer = rayTracerInstance.renderImage(rayTracingScene, this.rayTracerSettings);
		}
			
		return pixelBuffer;
	}
}
