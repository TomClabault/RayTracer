package render;

import scene.MyScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import java.util.logging.*;

public class UpdateCamera implements Runnable {

    public static final Double DELTA_MOVE = 0.1;
    Scene scene;
    MyScene myScene;

    public UpdateCamera(MyScene myScene, Scene scene) {
        this.myScene = myScene;
        this.scene = scene;
    }

    @Override
    public void run() {
        while(true) {
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {/*TODO code sale à revoir*/
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.UP) {
                        upGlobalCamera();
                        System.out.println("Camera up");
                    }else if (event.getCode() == KeyCode.DOWN) {
                        downGlobalCamera();
                        System.out.println("Camera down");
                    }
                }
            });
        }
    }

    public void upGlobalCamera() {
        this.myScene.getCamera().getPosition().setY(this.myScene.getCamera().getPosition().getY() + DELTA_MOVE);
    }

    public void downGlobalCamera() {
        this.myScene.getCamera().getPosition().setY(this.myScene.getCamera().getPosition().getY() - DELTA_MOVE);
    }
}
