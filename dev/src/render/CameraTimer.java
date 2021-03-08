package render;

import scene.MyScene;
import maths.Point;

import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class CameraTimer extends AnimationTimer {

    private static final Double DELTA_MOVE = 0.1;

    private Scene scene;
    private MyScene myScene;

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
    /*TODO zqsd change la position, fleche change l'orientation de la caméra, space et shift pour changer la hauteur*/
    public void upGlobalCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0, DELTA_MOVE,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0, DELTA_MOVE,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void downGlobalCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0,- DELTA_MOVE,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0,- DELTA_MOVE,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);

    }
}
