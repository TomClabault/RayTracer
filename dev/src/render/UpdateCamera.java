package render;

import javafx.scene.Scene;

import scene.MyScene;

/**
 * Un thread qui gère les déplacements de la caméra
*/
public class UpdateCamera implements Runnable {

    private Scene scene;
    private MyScene myScene;

    public UpdateCamera(MyScene myScene, Scene scene) {
        this.myScene = myScene;
        this.scene = scene;
    }

    @Override
    public void run() {
        CameraTimer cameraTimer = new CameraTimer(this.scene, this.myScene);
        cameraTimer.start();
    }
}
