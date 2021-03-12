package render;

import javafx.scene.input.KeyEvent;
import maths.MatrixD;
import javafx.scene.Scene;
import maths.RotationMatrix;
import rayTracer.RayTracer;
import scene.MyScene;
import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import maths.Point;
import javafx.scene.control.Label;

import maths.Vector;
import scene.MyScene;
import maths.RotationMatrix;

import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class GlobalTimer extends AnimationTimer{

    private MyScene myScene;
    private PixelWriter pixelWriter;
    private RayTracer rayTracer;
    private long oldFrameTime;
    private Label fpsLabel;

    /**
    * Sensibilité du déplacement droite/gauche de la caméra
    */
    private static final Double DELTA_MOVE = 0.02;
    /**
    * Sensibilité du déplacement haut/bas de la caméra
    */
    private static final Double DELTA_MOVE_Y = 0.1;
    /**
    * Sensibilité de la rotation de la caméra
    */
    private static final Double DELTA_ANGLE = 1.0;

    private static final RotationMatrix PLUS_Y_ANGLE = new RotationMatrix(1, DELTA_ANGLE);
    private static final RotationMatrix MINUS_Y_ANGLE = new RotationMatrix(1, - DELTA_ANGLE);
    private static final RotationMatrix PLUS_X_ANGLE = new RotationMatrix(0, DELTA_ANGLE);
    private static final RotationMatrix MINUS_X_ANGLE = new RotationMatrix(0, - DELTA_ANGLE);

    private Scene scene;

    public GlobalTimer(MyScene myScene, Scene scene, PixelWriter pixelWriter, RayTracer rayTracer) {
        this.myScene = myScene;
        this.pixelWriter = pixelWriter;
        this.rayTracer = rayTracer;
        Label fpsLabel = new Label("");
        this.fpsLabel = fpsLabel;
        fpsLabel.setId("fpsLabel");
        this.scene = scene;
        this.myScene = myScene;
    }

    public Label getfpsLabel() {
    	return fpsLabel;
    }


    public void handle(long actualFrameTime){
        long dif = actualFrameTime - oldFrameTime;
        dif  = (long)1000000000.0 / dif;
        this.oldFrameTime = actualFrameTime;
        //System.out.println(myScene.getCamera().getXAxis().toString() + " " + myScene.getCamera().getYAxis().toString() + " "+ myScene.getCamera().getZAxis().toString());
        //System.out.println(myScene.getCamera().getPosition().toString() + " " + myScene.getCamera().getDirection().toString());
        //System.out.println(Point.distance(myScene.getCamera().getPosition(), myScene.getCamera().getDirection()));
        //System.out.println(myScene.getCamera().getCTWMatrix().toString());
        ImageWriter.doImage(rayTracer.renderImage(this.myScene,8),this.pixelWriter);
        fpsLabel.setText(String.format("FPS : %d", dif));

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.E) {
                    upCamera();
                } else if (event.getCode() == KeyCode.A) {
                    downCamera();
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

    /**
    * Permet de déplacer la caméra verticalement vers le haut
    */
    public void upCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0, DELTA_MOVE_Y,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0, DELTA_MOVE_Y,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de déplacer la caméra verticalement vers le bas
    */
    public void downCamera() {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0,- DELTA_MOVE_Y,0));
        Point new_direction = Point.add(this.myScene.getCamera().getDirection(),new Point(0,- DELTA_MOVE_Y,0));
        this.myScene.getCamera().setPosition(new_position);
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de pivoter la caméra vers le haut
    */
    public void turnUpCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
    	Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), new RotationMatrix(myScene.getCamera().getXAxis(), -DELTA_ANGLE));
        //Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), PLUS_X_ANGLE);//Ancienne ligne
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de pivoter la caméra vers le bas
    */
    public void turnDownCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
        //Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), MINUS_X_ANGLE);//Ancienne ligne
    	Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), new RotationMatrix(myScene.getCamera().getXAxis(), DELTA_ANGLE));
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de pivoter la caméra vers la droite
    */
    public void turnRightCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
    	//Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), MINUS_Y_ANGLE);//Ancienne ligne
    	Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), new RotationMatrix(myScene.getCamera().getYAxis(), DELTA_ANGLE));
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de pivoter la caméra vers le gauche
    */
    public void turnLeftCamera() {/*0 pour l'axe x, 1 pour y et 2 pour z*/
    	Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), new RotationMatrix(myScene.getCamera().getYAxis(), -DELTA_ANGLE));
        //Point new_direction = MatrixD.mulPoint(this.myScene.getCamera().getDirection(), PLUS_Y_ANGLE);//Ancienne ligne
        this.myScene.getCamera().setDirection(new_direction);
    }

    /**
    * Permet de déplacer la caméra vers l'avant
    */
    public void goForwardCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getPosition(), this.myScene.getCamera().getDirection());
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        this.myScene.getCamera().setDirection(Point.add(this.myScene.getCamera().getDirection(), Vector.v2p(dir)));
        this.myScene.getCamera().setPosition(Point.add(this.myScene.getCamera().getPosition(), Vector.v2p(dir)));
    }

    /**
    * Permet de déplacer la caméra verticalement vers l'arrière
    */
    public void goBackwardCamera() {
        Vector dir = new Vector(this.myScene.getCamera().getDirection() , this.myScene.getCamera().getPosition());/*Inversion des deux points pour aller en arrière*/
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        this.myScene.getCamera().setDirection(Point.add(this.myScene.getCamera().getDirection(), Vector.v2p(dir)));
        this.myScene.getCamera().setPosition(Point.add(this.myScene.getCamera().getPosition(), Vector.v2p(dir)));
    }

    /**
    * Permet de déplacer la caméra verticalement vers la gauche
    */
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

    /**
    * Permet de déplacer la caméra verticalement vers la droite
    */
    public void goRightCamera()
    {
        Vector dir = new Vector(this.myScene.getCamera().getPosition(), this.myScene.getCamera().getDirection());
        dir = Vector.scalarMul(dir, DELTA_MOVE);
        dir = new Vector(new Point(dir.getX(),0, dir.getZ()));
        Point point_dir = MatrixD.mulPoint(Vector.v2p(dir), new RotationMatrix(1,-90));

        Point new_position = this.myScene.getCamera().getPosition();
        Point new_direction = this.myScene.getCamera().getDirection();

        this.myScene.getCamera().setDirection(Point.add(new_direction, point_dir));
        this.myScene.getCamera().setPosition(Point.add(new_position, point_dir));

//    	Vector axeX = this.myScene.getCamera().getXAxis();
//
//    	Point newPosition = Point.add(a, b)
//    	this.myScene.getCamera().setPosition(newPosition);
   }
}
