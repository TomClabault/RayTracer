package render;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.control.Label;

import scene.MyScene;
import rayTracer.RayTracer;

public class WindowTimer extends AnimationTimer {

    private MyScene myScene;
    private PixelWriter pixelWriter;
    private RayTracer rayTracer;
    private long oldFrameTime;
    private Label fpsLabel;

    public WindowTimer(MyScene myScene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;
        Label fpsLabel = new Label("");
        this.fpsLabel = fpsLabel;
        fpsLabel.setId("fpsLabel");
    }

    public Label getfpsLabel() {
    	return fpsLabel;
    }


    public void handle(long actualFrameTime){
        long dif = actualFrameTime - oldFrameTime;
        dif  = (long)1000000000.0 / dif;
        this.oldFrameTime = actualFrameTime;
        ImageWriter.doImage(rayTracer.renderImage(this.myScene,8),this.pixelWriter);
        fpsLabel.setText(String.format("FPS : %d", dif));
    }

}
