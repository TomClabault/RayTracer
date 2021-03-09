package render;

import javafx.scene.image.PixelWriter;

import scene.MyScene;
import rayTracer.RayTracer;

public class UpdateWindow implements Runnable {
    private RayTracer rayTracer;
    private MyScene myScene;
    private PixelWriter pixelWriter;
    private WindowTimer windowTimer;

    public UpdateWindow(RayTracer rayTracer, MyScene myScene, PixelWriter pixelWriter) {
        this.rayTracer = rayTracer;
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;

        WindowTimer windowTimer = new WindowTimer(this.myScene, this.pixelWriter, this.rayTracer);
        this.windowTimer = windowTimer;
    }

    /**
     * @return the windowTimer
     */
    public WindowTimer getWindowTimer() {
    	return this.windowTimer;
    }

    @Override
    public void run() {
        windowTimer.start();
    }

}
