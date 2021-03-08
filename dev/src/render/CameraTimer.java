package render;

import maths.MatrixD;
import scene.MyScene;
import maths.Point;
import maths.RotationMatrix;

import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class CameraTimer extends AnimationTimer {

    private static final Double DELTA_MOVE = 0.1;
    private static final Double DELTA_ANGLE = 1.0;

    private final RotationMatrix PLUS_Y_ANGLE = new RotationMatrix(1, DELTA_ANGLE);
    private final RotationMatrix MINUS_Y_ANGLE = new RotationMatrix(1, - DELTA_ANGLE);
    private final RotationMatrix PLUS_X_ANGLE = new RotationMatrix(0, DELTA_ANGLE);
    private final RotationMatrix MINUS_X_ANGLE = new RotationMatrix(0, - DELTA_ANGLE);

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
                if (event.getCode() == KeyCode.E) {
                    upCamera();
                    //System.out.println("Camera up");
                }else if (event.getCode() == KeyCode.A) {
                    downCamera();
                    //System.out.println("Camera down");
                } else if (event.getCode() == KeyCode.UP) {
                    turnUpCamera();
                } else if (event.getCode() == KeyCode.DOWN) {
                    turnDownCamera();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    turnRightCamera();
                } else if (event.getCode() == KeyCode.LEFT) {
                    turnLeftCamera();
                }
            }
        });

    }
    /*TODO zqsd change la position, fleche change l'orientation de la caméra, space et shift pour changer la hauteur*/
    public void upCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0, DELTA_MOVE,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0, DELTA_MOVE,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void downCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0,- DELTA_MOVE,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0,- DELTA_MOVE,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void turnUpCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
        Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), PLUS_X_ANGLE);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void turnDownCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
        Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), MINUS_X_ANGLE);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void turnRightCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
        Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), MINUS_Y_ANGLE);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void turnLeftCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
        Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), PLUS_Y_ANGLE);
        this.myScene.getCamera().setDirection(new_direction);
    }
}
