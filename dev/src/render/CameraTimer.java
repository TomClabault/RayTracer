package render;

import maths.Vector;
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

    private static final Double DELTA_MOVE = 0.02;
    private static final Double DELTA_MOVE_Y = 0.1;
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
                } else if (event.getCode() == KeyCode.A) {
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
                } else if (event.getCode() == KeyCode.Z) {
                    goForwardCamera();
                } else if (event.getCode() == KeyCode.S) {
                    goBackwardCamera();
                } else if (event.getCode() == KeyCode.Q) {
                    goLeftCamera();
                } else if (event.getCode() == KeyCode.D) {
                    goRightCamera();
                }
            }
        });

    }
    /*TODO zqsd change la position, fleche change l'orientation de la caméra, space et shift pour changer la hauteur*/
    public void upCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0, DELTA_MOVE_Y,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0, DELTA_MOVE_Y,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    public void downCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0,- DELTA_MOVE_Y,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0,- DELTA_MOVE_Y,0));
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

    public void goForwardCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getPosition(), this.myScene.getCamera().getDirection());
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        this.myScene.getCamera().setDirection(Point.add(this.myScene.getCamera().getDirection(), Vector.v2p(dir)));
        this.myScene.getCamera().setPosition(Point.add(this.myScene.getCamera().getPosition(), Vector.v2p(dir)));
    }

    public void goBackwardCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getDirection() , this.myScene.getCamera().getPosition());/*Inversion des deux points pour aller en arrière*/
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        this.myScene.getCamera().setDirection(Point.add(this.myScene.getCamera().getDirection(), Vector.v2p(dir)));
        this.myScene.getCamera().setPosition(Point.add(this.myScene.getCamera().getPosition(), Vector.v2p(dir)));
    }

    public void goLeftCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getPosition(), this.myScene.getCamera().getDirection());
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        dir = new Vector(new Point(dir.getX(),0, dir.getZ()));/*Permet déplacement horizontal de la caméra*/
        Point point_dir = MatrixD.mulPoint(Vector.v2p(dir), new RotationMatrix(1,90));

        Point new_position = this.myScene.getCamera().getPosition();
        Point new_direction = this.myScene.getCamera().getDirection();

        this.myScene.getCamera().setDirection(Point.add(new_direction, point_dir));
        this.myScene.getCamera().setPosition(Point.add(new_position, point_dir));
    }

    public void goRightCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getPosition(), this.myScene.getCamera().getDirection());
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        dir = new Vector(new Point(dir.getX(),0, dir.getZ()));
        Point point_dir = MatrixD.mulPoint(Vector.v2p(dir), new RotationMatrix(1,-90));

        Point new_position = this.myScene.getCamera().getPosition();
        Point new_direction = this.myScene.getCamera().getDirection();

        this.myScene.getCamera().setDirection(Point.add(new_direction, point_dir));
        this.myScene.getCamera().setPosition(Point.add(new_position, point_dir));
    }
}
