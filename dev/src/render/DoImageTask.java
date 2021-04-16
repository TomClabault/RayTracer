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
	private Scene mainAppScene;
	private RayTracerSettings rayTracerSettings;
	private RayTracer rayTracer;

	DoImageTask(Scene mainAppScene, PixelWriter pw, WritablePixelFormat<IntBuffer> pixelFormat,RayTracer rayTracer, RayTracingScene rts, RayTracerSettings rayTracerSettings) {
		this.mainAppScene = mainAppScene;
		this.rayTracingScene = rts;
		this.rayTracer = rayTracer;
		this.rayTracerSettings = rayTracerSettings;
	}

	// public WindowTimer getWindowTimer() {
	// return this.windowTimer;
	// }

	@Override
	public IntBuffer call() {
		synchronized (mainAppScene) {
			pixelBuffer = rayTracer.renderImage(rayTracingScene, this.rayTracerSettings);
		}

		return pixelBuffer;
	}
}
