package render;

import java.nio.IntBuffer;
import java.util.concurrent.locks.ReentrantLock;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;
import scene.RayTracingScene;

public class DoImageTask extends Task<IntBuffer> {
	
	private IntBuffer pixelBuffer;
	private RayTracingScene rts;
	private RayTracer rayTracerInstance;
	private Scene mainAppScene;
	
	DoImageTask(Scene mainAppScene, PixelWriter pw, WritablePixelFormat<IntBuffer> pixelFormat, RayTracingScene rts) {
		this.mainAppScene = mainAppScene;
		this.rts = rts;
		
		this.rayTracerInstance = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT, 4, 8);
	}
	
	//public WindowTimer getWindowTimer() {
	//	return this.windowTimer;
	//}
	
	
	@Override
	protected IntBuffer call() {
		synchronized(mainAppScene)
		{
			pixelBuffer = rayTracerInstance.renderImage(rts);
		}
			
		return pixelBuffer;
	}
}
