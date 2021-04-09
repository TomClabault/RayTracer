package render;

import java.nio.IntBuffer;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import rayTracer.RayTracer;
import scene.RayTracingScene;

public class DoImageTask extends Task {
	
	IntBuffer pixelBuffer;
	PixelWriter pw;
	WritablePixelFormat<IntBuffer> pixelFormat;
	RayTracingScene rts;
	Scene mainAppScene;
	WindowTimer windowTimer;
	
	DoImageTask(Scene mainAppScene, PixelWriter pw, WritablePixelFormat<IntBuffer> pixelFormat, RayTracingScene rts) {
		this.mainAppScene = mainAppScene;
		this.pw = pw;
		this.pixelFormat = pixelFormat;
		this.rts = rts;
		this.windowTimer = new WindowTimer(rts, this.pw, new RayTracer(MainApp.WIDTH, MainApp.HEIGHT, 4, 1));
		
	}
	
	public WindowTimer getWindowTimer() {
		return this.windowTimer;
	}
	
	
	@Override
	public Object call() {
		RayTracer rayTracer = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT, 4, 1);
		

        CameraTimer cameraTimer = new CameraTimer(this.mainAppScene, rts);
		windowTimer.start();
        cameraTimer.start();
		pixelBuffer = rayTracer.renderImage(rts);
		ImageWriter.doImage(pixelBuffer, pw, pixelFormat);
			
		return null;
	}
		
	
}


