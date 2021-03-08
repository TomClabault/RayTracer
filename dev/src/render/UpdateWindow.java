package render;

import javafx.scene.image.PixelWriter;

import scene.MyScene;
import rayTracer.RayTracer;

public class UpdateWindow implements Runnable {
    RayTracer rayTracer;
    MyScene myScene;
    PixelWriter pixelWriter;

    public UpdateWindow(RayTracer rayTracer, MyScene myScene, PixelWriter pixelWriter) {
        this.rayTracer = rayTracer;
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;
    }

    @Override
    public void run() {
        WindowTimer windowTimer = new WindowTimer(this.myScene, this.pixelWriter, this.rayTracer);
        windowTimer.start();
    }

}
