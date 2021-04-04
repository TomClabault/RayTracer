package render;

import java.io.IOException;
import java.nio.IntBuffer;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import scene.RayTracingScene;
import util.ImageUtil;
import rayTracer.RayTracer;

public class WindowTimer extends AnimationTimer {

    private RayTracingScene rayTracingScene;
    private PixelWriter pixelWriter;
    private RayTracer rayTracer;
    private long oldFrameTime;
    private Label fpsLabel;

    private WritablePixelFormat<IntBuffer> pixelFormat;

    private Scene scene;
    
    public WindowTimer(Scene scene, RayTracingScene rayTracingScene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.rayTracingScene = rayTracingScene;
        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;
        Label fpsLabel = new Label("");
        this.fpsLabel = fpsLabel;
        fpsLabel.setId("fpsLabel");

        this.pixelFormat = PixelFormat.getIntArgbPreInstance();
        
        
        this.scene = scene;
    }

    public Label getfpsLabel() {
    	return fpsLabel;
    }


    public void handle(long actualFrameTime){
        long dif = actualFrameTime - oldFrameTime;
        dif  = (long)1000000000.0 / dif;
        this.oldFrameTime = actualFrameTime;
        ImageWriter.doImage(rayTracer.renderImage(this.rayTracingScene), this.pixelWriter, this.pixelFormat);
        try {
			ImageUtil.writeImageToDisk(this.scene, "Output 4k.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.exit(0);
        fpsLabel.setText(String.format("FPS : %d\n%s\nH: %.2f°\nV: %.2f°", dif, this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti()));
    }

}
