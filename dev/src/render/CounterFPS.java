package render;

import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CounterFPS {

    //private Scene scene;
    private Pane pane;

    public CounterFPS(Label fpsLabel) {
        Pane pane = new Pane();
        fpsLabel.setStyle("-fx-font: 64 arial;");
        fpsLabel.setTextFill(Color.WHITE);
        pane.getChildren().add(fpsLabel);
        this.pane = pane;
        /*Scene scene = new Scene(pane);
        this.scene = scene;*/
    }

    /**
     * @return the scene
     */
    /*public Scene getScene() {
    	return scene;
    }*/

    /**
     * @return the pane
     */
    public Pane getPane() {
    	return pane;
    }
}
