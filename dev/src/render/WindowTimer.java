package render;

import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import scene.RayTracingScene;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;

public class WindowTimer extends AnimationTimer {

    private RayTracingScene rayTracingScene;
    private RayTracerSettings rayTracingSettings;

    private PixelWriter pixelWriter;
    private RayTracer rayTracer;
    private long oldFrameTime;
    private Label fpsLabel;

    private WritablePixelFormat<IntBuffer> pixelFormat;
    private DoImageTask task;
    private ExecutorService es;
    private CameraTimer cameraTimer;

    public WindowTimer(Scene mainAppScene, RayTracingScene rayTracingScene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.rayTracingScene = rayTracingScene;
        this.rayTracingSettings = new RayTracerSettings(2, 4, 9, 4);
        this.rayTracingSettings.enableAntialiasing(false);
        this.rayTracingSettings.enableBlurryReflections(true);

        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;
        Label fpsLabel = new Label("");
        this.fpsLabel = fpsLabel;
        fpsLabel.setId("fpsLabel");

        this.pixelFormat = PixelFormat.getIntArgbPreInstance();
        this.task = new DoImageTask(mainAppScene, pixelWriter, PixelFormat.getIntArgbPreInstance(), rayTracingScene);
        this.es = Executors.newFixedThreadPool(1);
        this.cameraTimer = new CameraTimer(mainAppScene, rayTracingScene);

    }

    public Label getfpsLabel() {
    	return fpsLabel;
    }


    public void handle(long actualFrameTime){

        es.execute(task);

        task.setOnSucceeded((succeededEvent) -> {
        	System.out.println("I DO A FRAME");
        	long dif = actualFrameTime - oldFrameTime;
            dif  = (long)1000000000.0 / dif;
            this.oldFrameTime = actualFrameTime;
        	IntBuffer intBuffer = task.getValue();
        	ImageWriter.doImage(intBuffer, pixelWriter, pixelFormat);
            cameraTimer.start();
            fpsLabel.setText(String.format("FPS : %d\n%s\nH: %.2f°\nV: %.2f°", dif, this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti()));
         });

    }

}
