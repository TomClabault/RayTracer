package render;

import javafx.scene.Scene;

import scene.MyScene;

public class UpdateCamera implements Runnable {

    Scene scene;
    MyScene myScene;

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
