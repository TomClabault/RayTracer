package render;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static int HEIGHT;
    public static int WIDTH;

    public static void main(String[] args) {

        Application.launch(args);

    }
    public void start(Stage stage) {

        ChoiceWindow choiceWindow = new ChoiceWindow();
        choiceWindow.choiceWindowMain();

        ImageWriter imageWriter = new ImageWriter();
        imageWriter.ImageWriterMain(HEIGHT, WIDTH);
    public MyScene addObjectsToScene() {

        Camera c = new Camera(); c.setFOV(100);
        Light l = new LightBulb(Point.add(c.getPosition(), new Point(-1, 1, 0)), 1);
        ArrayList<Shape> shapeList = new ArrayList<>();
        shapeList.add(new SphereMaths(new Point(0, 0, -4), 1));
        shapeList.add(new Triangle(new Point(-1,-1,-1.5),new Point(1,-1,-1.5),new Point(0,2,-1.5)));
        MyScene s = new MyScene(c, l, shapeList, 0.5);
        return s;
    }
    public void updateWindow(final PixelWriter pw, final MyScene scene) {
        new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    RayTracer r = new RayTracer(MainApp.HEIGHT, MainApp.WIDTH);
                    ImageWriter.doImage(r.computeImage(scene),pw);
                }

            }
        }).start();
    }

}
