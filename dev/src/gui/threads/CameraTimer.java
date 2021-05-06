package gui.threads;

import maths.Point;
import maths.Vector;
import scene.RayTracingScene;

import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.event.EventHandler;


import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

/**
* Deplace la camera en fonction des touches pressees
*/
public class CameraTimer extends AnimationTimer {

    /**
    * Sensibilite du deplacement droite/gauche de la camera
    */
    private static final Double DELTA_MOVE = 0.16;
    /**
    * Sensibilite du deplacement haut/bas de la camera
    */
    private static final Double DELTA_MOVE_Y = 0.1;
    /**
    * Sensibilite de la rotation de la camera
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

    @Override
    public void handle(long actualFrameTime){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {//TODO ajouter liste keycode 
            @Override
            public void handle(KeyEvent event) 
            {
            	synchronized(rayTracingScene)
            	{
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
            }
        });
    }

    /**
    * Permet de deplacer la camera verticalement vers le haut
    */
    public void upCamera()
    {
    	Point new_position = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), new Vector(0, DELTA_MOVE_Y,0), 1);

        this.rayTracingScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de deplacer la camera verticalement vers le bas
    */
    public void downCamera()
    {
    	Point new_position = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), new Vector(0, -DELTA_MOVE_Y,0), 1);

        this.rayTracingScene.getCamera().setPosition(new_position);
    }

    /**
    * Permet de pivoter la camera vers le haut
    */
    public void turnUpCamera()
    {
    	this.rayTracingScene.getCamera().addAngleVerti(DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la camera vers le bas
    */
    public void turnDownCamera()
    {
    	this.rayTracingScene.getCamera().addAngleVerti(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la camera vers la droite
    */
    public void turnRightCamera()
    {
    	this.rayTracingScene.getCamera().addAngleHori(-DELTA_ANGLE);
    }

    /**
    * Permet de pivoter la camera vers le gauche
    */
    public void turnLeftCamera()
    {
    	this.rayTracingScene.getCamera().addAngleHori(DELTA_ANGLE);
    }

    /**
    * Permet de deplacer la camera vers l'avant
    */
    public void goForwardCamera()
    {
    	Vector axeZ = this.rayTracingScene.getCamera().getZAxis();
    	Point newPosition = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), axeZ, -DELTA_MOVE);

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de deplacer la camera verticalement vers l'arriere
    */
    public void goBackwardCamera()
    {
    	Vector axeZ = this.rayTracingScene.getCamera().getZAxis();
    	Point newPosition = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), axeZ, DELTA_MOVE);

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de deplacer la camera verticalement vers la gauche
    */
    public void goLeftCamera()
    {
    	Vector axeX = this.rayTracingScene.getCamera().getXAxis();
    	Point newPosition = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), axeX, -DELTA_MOVE);

    	this.rayTracingScene.getCamera().setPosition(newPosition);
    }

    /**
    * Permet de deplacer la camera verticalement vers la droite
    */
    public void goRightCamera()
    {
    	Vector axeX = this.rayTracingScene.getCamera().getXAxis();
    	Point newPosition = Point.translateMul(this.rayTracingScene.getCamera().getPosition(), axeX, DELTA_MOVE);
    	
    	this.rayTracingScene.getCamera().setPosition(newPosition);
   }
}
