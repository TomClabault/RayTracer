package render;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//from www.java2s.com
public class ExempleCanva extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    Canvas canvas = new Canvas(300, 100);/*ou Canvas canvas = new Canvas();*/

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setLineWidth(1);
    gc.setFill(Color.BLUE);/*ou Color.web("0x0000FF")*/

    gc.strokeRoundRect(40, 40, 50, 50, 10, 10);

    gc.strokeRect(1,1,150,50);/*x origin, y origin, width, height*/


    gc.fillOval(70, 10, 50, 20);

    gc.strokeText("Vive le cafe", 150, 20);

    Pane root = new Pane();
    root.getChildren().add(canvas);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Titre");
    stage.show();
  }
}
