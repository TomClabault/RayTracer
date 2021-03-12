package render;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import maths.Point;
import maths.Vector;
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
        System.out.println("Axe X : " + myScene.getCamera().getXAxis().toString() + System.lineSeparator() + "Axe Y : " + myScene.getCamera().getYAxis().toString() + System.lineSeparator() + "Axe Z : " + myScene.getCamera().getZAxis().toString());
        System.out.println("Position/direction: " + myScene.getCamera().getPosition().toString() + " " + myScene.getCamera().getDirection().toString());
        System.out.println("Distance position/direciton : " + Point.distance(myScene.getCamera().getPosition(), Vector.v2p(myScene.getCamera().getDirection())));
        System.out.println("Matrix :\n" + myScene.getCamera().getCTWMatrix().toString());
        ImageWriter.doImage(rayTracer.renderImage(this.myScene,8),this.pixelWriter);
        fpsLabel.setText(String.format("FPS : %d", dif));
    }

}
