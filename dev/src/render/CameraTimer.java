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

/**
* Déplace la caméra en fonction des touches pressées
*/
public class CameraTimer extends AnimationTimer {

    /**
    * Sensibilité du déplacement droite/gauche de la caméra
    */
    private static final Double DELTA_MOVE = 0.16;
    /**
    * Sensibilité du déplacement haut/bas de la caméra
    */
    private static final Double DELTA_MOVE_Y = 0.1;
    /**
    * Sensibilité de la rotation de la caméra
    */
    private static final Double DELTA_ANGLE = 1.5;

    private Scene scene;
    private MyScene myScene;

    /**
    * @param scene scene javafx
    * @param myScene scene {@link MyScene}
    */
    public CameraTimer(Scene scene, MyScene myScene) {
        this.scene = scene;
        this.myScene = myScene;
    }

    public void handle(long nanoTime){
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
    public void upCamera() 
    {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0, DELTA_MOVE_Y,0));

        this.myScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de déplacer la caméra verticalement vers le bas
    */
    public void downCamera() 
    {
        Point new_position = Point.add(this.myScene.getCamera().getPosition(),new Point(0,- DELTA_MOVE_Y,0));

        this.myScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de pivoter la caméra vers le haut
    */
    public void turnUpCamera() 
    {
    	this.myScene.getCamera().addAngleVerti(DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers le bas
    */
    public void turnDownCamera() 
    {
    	this.myScene.getCamera().addAngleVerti(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers la droite
    */
    public void turnRightCamera() 
    {
    	this.myScene.getCamera().addAngleHori(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers le gauche
    */
    public void turnLeftCamera() 
    {
    	this.myScene.getCamera().addAngleHori(DELTA_ANGLE);
    }

    /**
    * Permet de déplacer la caméra vers l'avant
    */
    public void goForwardCamera() 
    {
    	Vector axeZ = this.myScene.getCamera().getZAxis();
    	Point newPosition = Point.add(Vector.v2p(Vector.scalarMul(axeZ, -DELTA_MOVE)), myScene.getCamera().getPosition());
    	
    	this.myScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers l'arrière
    */
    public void goBackwardCamera() 
    {
    	Vector axeZ = this.myScene.getCamera().getZAxis();
    	Point newPosition = Point.add(Vector.v2p(Vector.scalarMul(axeZ, DELTA_MOVE)), myScene.getCamera().getPosition());
    	
    	this.myScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers la gauche
    */
    public void goLeftCamera() 
    {
    	Vector axeX = this.myScene.getCamera().getXAxis();
    	Point newPosition = Point.add(Vector.v2p(Vector.scalarMul(axeX, -DELTA_MOVE)), myScene.getCamera().getPosition());
    	
    	this.myScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers la droite
    */
    public void goRightCamera() 
    {
    	Vector axeX = this.myScene.getCamera().getXAxis();
    	Point newPosition = Point.add(Vector.v2p(Vector.scalarMul(axeX, DELTA_MOVE)), myScene.getCamera().getPosition());
    	this.myScene.getCamera().setPosition(newPosition);
   }
}
