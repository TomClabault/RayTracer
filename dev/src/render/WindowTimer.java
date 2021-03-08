package render;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import scene.MyScene;
import rayTracer.RayTracer;

public class WindowTimer extends AnimationTimer {

    private MyScene myScene;
    private PixelWriter pixelWriter;
    private RayTracer rayTracer;
    private long oldFrameTime;
    private Label fpsString;

    public WindowTimer(MyScene myScene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;

        Pane root = new Pane();
        Label fpsString = new Label("this is text");
        fpsString.setStyle("-fx-font: 128 arial;");
        this.fpsString = fpsString;
        root.getChildren().add(fpsString);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Number of fps");
        stage.setScene(scene);
        stage.show();
    }

    public void handle(long actualFrameTime){
        long dif = actualFrameTime - oldFrameTime;
        dif  = (long)1000000000.0 / dif;
        this.oldFrameTime = actualFrameTime;
        ImageWriter.doImage(rayTracer.renderImage(this.myScene,8),this.pixelWriter);
        fpsString.setText(String.format("FPS : %d", dif));
        //System.out.println("A Image is printed");
        //System.out.println(myScene.getCamera().getPosition());

    }

}
