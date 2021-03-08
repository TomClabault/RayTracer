package render;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;

import scene.MyScene;
import rayTracer.RayTracer;

public class WindowTimer extends AnimationTimer {

    private MyScene myScene;
    private PixelWriter pixelWriter;
    private RayTracer rayTracer;

    public WindowTimer(MyScene myScene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;
    }

    public void handle(long nanoTime){
        ImageWriter.doImage(rayTracer.renderImage(this.myScene,8),this.pixelWriter);
        System.out.println("A Image is printed");
        System.out.println(myScene.getCamera().getPosition());

    }
}
