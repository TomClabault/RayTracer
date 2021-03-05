package render;

import scene.MyScene;
import rayTracer.RayTracer;
import javafx.scene.image.PixelWriter;

public class UpdateWindow implements Runnable{
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
        while(true){
            RayTracer r = new RayTracer(MainApp.WIDTH, MainApp.HEIGHT);
            ImageWriter.doImage(r.renderImage(this.myScene,8),this.pixelWriter);
        }

    }


}
