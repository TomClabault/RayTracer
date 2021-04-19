package render;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

/**
* Contient les composants javafx du compteur de FPS
*/
public class CounterFPS {

    private Pane pane;
    /**
    * @param fpsLabel le Label contenant le nombre de FPS
    */
    public CounterFPS(Label fpsLabel) {
        Pane pane = new Pane();
        pane.getChildren().add(fpsLabel);
        this.pane = pane;
    }

    public Pane getPane() {
    	return pane;
    }
}
