package render;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

public class CounterFPS {

    private Pane pane;

    public CounterFPS(Label fpsLabel) {
        Pane pane = new Pane();
        pane.getChildren().add(fpsLabel);
        this.pane = pane;
    }

    /**
     * @return the pane
     */
    public Pane getPane() {
    	return pane;
    }
}
