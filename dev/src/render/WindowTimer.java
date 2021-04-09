package render;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javafx.concurrent.Task;

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
    private ExecutorService es;
    Scene mainAppScene;

    private Future<?> futureRenderTask = null;

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
        this.mainAppScene = mainAppScene;
        this.pixelFormat = PixelFormat.getIntArgbPreInstance();
        //this.task = new DoImageTask(mainAppScene, pixelWriter, PixelFormat.getIntArgbPreInstance(), rayTracingScene);
        this.es = Executors.newFixedThreadPool(1);
        this.cameraTimer = new CameraTimer(mainAppScene, rayTracingScene);

    }

    public Label getfpsLabel() {
    	return fpsLabel;
    }


    public void handle(long actualFrameTime){
    	DoImageTask renderTask = new DoImageTask(mainAppScene, pixelWriter, PixelFormat.getIntArgbPreInstance(), rayTracingScene);

    	if(futureRenderTask == null || futureRenderTask.isDone())//Si aucune tâche n'a encore été donnée ou si la tâche est terminée
    		futureRenderTask = es.submit(renderTask);//On redonne une autre tâche de rendu à faire



        renderTask.setOnSucceeded((succeededEvent) -> {

        	IntBuffer pixelBuffer = renderTask.getValue();
        	ImageWriter.doImage(pixelBuffer, pixelWriter, pixelFormat);
        	long dif = actualFrameTime - oldFrameTime;
            dif  = (long)1000000000.0 / dif;
            this.oldFrameTime = actualFrameTime;
            fpsLabel.setText(String.format("FPS : %d\n%s\nH: %.2f°\nV: %.2f°", dif, this.rayTracingScene.getCamera().getPosition().toString(), this.rayTracingScene.getCamera().getAngleHori(), this.rayTracingScene.getCamera().getAngleVerti()));
        });


        /*try {
			es.awaitTermination(100,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

}
