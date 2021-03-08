package render;

import scene.MyScene;

import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class CameraTimer extends AnimationTimer {

    public static final Double DELTA_MOVE = 0.1;

    Scene scene;
    MyScene myScene;

    public CameraTimer(Scene scene, MyScene myScene) {
        this.scene = scene;
        this.myScene = myScene;
    }

    public void handle(long nanoTime){
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

    public void upGlobalCamera() {
        this.myScene.getCamera().getPosition().setY(this.myScene.getCamera().getPosition().getY() + DELTA_MOVE);
        //System.out.println(this.myScene.getCamera().getPosition());
        //Point tempPoint = this.myScene.getCamera().getPosition();
        //tempPoint.setY(tempPoint.getY() + DELTA_MOVE);
        //this.myScene.getCamera().setPosition(tempPoint);
    }

    public void downGlobalCamera() {
        this.myScene.getCamera().getPosition().setY(this.myScene.getCamera().getPosition().getY() - DELTA_MOVE);
    }
}
