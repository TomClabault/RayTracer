package render;

import maths.Vector3D;
import scene.RayTracingScene;

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
    private RayTracingScene rayTracingScene;

    /**
    * @param scene scene javafx
    * @param rayTracingScene scene {@link RayTracingScene}
    */
    public CameraTimer(Scene scene, RayTracingScene rayTracingScene) {
        this.scene = scene;
        this.rayTracingScene = rayTracingScene;
    }

    public void handle(long nanoTime){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {//TODO ajouter liste keycode 
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
        Vector3D new_position = Vector3D.add(this.rayTracingScene.getCamera().getPosition(),new Vector3D(0, DELTA_MOVE_Y,0));

        this.rayTracingScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de déplacer la caméra verticalement vers le bas
    */
    public void downCamera()
    {
        Vector3D new_position = Vector3D.add(this.rayTracingScene.getCamera().getPosition(),new Vector3D(0,- DELTA_MOVE_Y,0));

        this.rayTracingScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de pivoter la caméra vers le haut
    */
    public void turnUpCamera()
    {
    	this.rayTracingScene.getCamera().addAngleVerti(DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers le bas
    */
    public void turnDownCamera()
    {
    	this.rayTracingScene.getCamera().addAngleVerti(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers la droite
    */
    public void turnRightCamera()
    {
    	this.rayTracingScene.getCamera().addAngleHori(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la caméra vers le gauche
    */
    public void turnLeftCamera()
    {
    	this.rayTracingScene.getCamera().addAngleHori(DELTA_ANGLE);
    }

    /**
    * Permet de déplacer la caméra vers l'avant
    */
    public void goForwardCamera()
    {
    	Vector3D axeZ = this.rayTracingScene.getCamera().getZAxis();
    	Vector3D newPosition = Vector3D.add(Vector3D.scalarMul(axeZ, -DELTA_MOVE), rayTracingScene.getCamera().getPosition());

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers l'arrière
    */
    public void goBackwardCamera()
    {
    	Vector3D axeZ = this.rayTracingScene.getCamera().getZAxis();
    	Vector3D newPosition = Vector3D.add(Vector3D.scalarMul(axeZ, DELTA_MOVE), rayTracingScene.getCamera().getPosition());

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers la gauche
    */
    public void goLeftCamera()
    {
    	Vector3D axeX = this.rayTracingScene.getCamera().getXAxis();
    	Vector3D newPosition = Vector3D.add(Vector3D.scalarMul(axeX, -DELTA_MOVE), rayTracingScene.getCamera().getPosition());

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de déplacer la caméra verticalement vers la droite
    */
    public void goRightCamera()
    {
    	Vector3D axeX = this.rayTracingScene.getCamera().getXAxis();
    	Vector3D newPosition = Vector3D.add(Vector3D.scalarMul(axeX, DELTA_MOVE), rayTracingScene.getCamera().getPosition());
    	this.rayTracingScene.getCamera().setPosition(newPosition);
   }
}
