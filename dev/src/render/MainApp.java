package render;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static int HEIGHT;/*Définie par choiceWindowMain*/
    public static int WIDTH;
    //public static final Double DELTA_MOVE = 0.1;
    //public volatile MyScene globalScene = addObjectsToScene();

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        ChoiceWindow choiceWindow = new ChoiceWindow();
        choiceWindow.choiceWindowMain();

        //WritableImage writableImage = new WritableImage(800,600);
        //PixelWriter pw = writableImage.getPixelWriter();

        ImageWriter imageWriter = new ImageWriter();
        imageWriter.ImageWriterMain(HEIGHT, WIDTH);
        imageWriter.updateWindow();
        imageWriter.updateCamera();



    }

    /*public MyScene addObjectsToScene() {/*utilisé dans le constructeur*//*

        Camera c = new Camera(); c.setFOV(100);
        Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);
        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new SphereMaths(new Point(0, 0, -4), 1));
        shapeList.add(new Triangle(new Point(-1,-1,-1.5),new Point(1,-1,-1.5),new Point(0,2,-1.5)));
        MyScene s = new MyScene(c, l, shapeList, 0.5);
        return s;
    }


    public void updateWindow(final PixelWriter pw) {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    RayTracer r = new RayTracer(MainApp.HEIGHT, MainApp.WIDTH);
                    ImageWriter.doImage(r.computeImage(globalScene),pw);
                }

            }
        }).start();
    }

    public void updateCamera(final PixelWriter pw, final Scene scene) {
        new Thread(new Runnable() {
        @Override
            public void run() {
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case UP:    upGlobalCamera();
                            case DOWN:  downGlobalCamera();
                            //case LEFT:  goWest  = true; break;
                            //case RIGHT: goEast  = true; break;
                            //case SHIFT: running = true; break;
                        }
                    }
                });

                while(true){
                    Point cameraPosition = globalScene.getCamera().getPosition();


                }

            }
        }).start();
    }

    public void upGlobalCamera() {
        globalScene.getCamera().getPosition().setY(globalScene.getCamera().getPosition().getY() + DELTA_MOVE);
    }

    public void downGlobalCamera() {
        globalScene.getCamera().getPosition().setY(globalScene.getCamera().getPosition().getY() - DELTA_MOVE);
    }*/



}
