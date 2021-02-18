package render;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
//from ww  w  . j  av  a2s  . c  om
public class ExempleImageWriter extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {

    WritableImage writableImage = new WritableImage(200,200);

    PixelWriter pw = writableImage.getPixelWriter();

    pw.setColor(2,2,Color.web("0x0000FF"));
    pw.setColor(2,3,Color.web("0x0000FF"));
    pw.setColor(3,2,Color.web("0x0000FF"));
    pw.setColor(3,3,Color.web("0x0000FF"));/*Color.rgb(0,0,255)*/
    /*Prend en argument un tableau de couleur*/

    ImageView imageView = new ImageView();
    imageView.setImage(writableImage);

    Pane root = new Pane();
    root.getChildren().add(imageView);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("");
    stage.show();

    pw.setColor(2,2,Color.web("0xFF0000"));
    pw.setColor(3,2,Color.web("0xFF0000"));
    pw.setColor(2,3,Color.web("0xFF0000"));
    pw.setColor(3,3,Color.web("0xFF0000"));

  }
}
