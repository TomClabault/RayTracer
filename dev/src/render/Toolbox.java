package render;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scene.RayTracingScene;

public class Toolbox{

	public RayTracingScene rts;

	public Toolbox(RayTracingScene rts) {
		this.rts = rts;
	}

	public RayTracingScene getRts() {
		return rts;
	}

	public void setRts(RayTracingScene rts) {
		this.rts = rts;
	}

	public void execute() {



		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(SetSizeWindow.class.getResource("style/window.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        TextField saveTextField = new TextField("Chemin d'enregistrement du rendu");
        Button saveButton = new Button("Save");

        Label resolutionLabel = new Label("Résolution de la scène");

        HBox resolutionHbox = new HBox();
        TextField widthSceneRes = new TextField("Width");
        TextField heightSceneRes = new TextField("Height");
        resolutionHbox.getChildren().addAll(widthSceneRes, heightSceneRes);

        Button applyResButton = new Button("Appliquer");


        Label depthLabel = new Label();
        Slider depthSlider = new Slider(0,10,1);

        root.getChildren().addAll(saveTextField, saveButton, resolutionLabel, resolutionHbox, applyResButton, depthLabel, depthSlider);

        applyResButton.setOnAction(new EventHandler<ActionEvent>()
    	{
            @Override
            public void handle(ActionEvent event) {
        		if (Integer.parseInt(widthSceneRes.getText()) < 0 || Integer.parseInt(heightSceneRes.getText()) < 0) {
					throw new NumberFormatException();
				}
        		MainApp.HEIGHT = Integer.parseInt(heightSceneRes.getText());
                MainApp.WIDTH = Integer.parseInt(widthSceneRes.getText());
            }
        });

        stage.show();
	}

	/*@Override
	protected RayTracingScene call() {
		System.out.println("Rentre dans call");
		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(SetSizeWindow.class.getResource("style/window.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        TextField saveTextField = new TextField("Chemin d'enregistrement du rendu");
        Button saveButton = new Button("Save");





        Label resolutionLabel = new Label("Résolution de la scène");

        HBox resolutionHbox = new HBox();
        TextField widthSceneRes = new TextField("Width");
        TextField heightSceneRes = new TextField("Height");
        resolutionHbox.getChildren().addAll(widthSceneRes, heightSceneRes);

        Button applyResButton = new Button("Appliquer");


        Label depthLabel = new Label();
        Slider depthSlider = new Slider(0,10,1);

        root.getChildren().addAll(saveTextField, saveButton, resolutionLabel, resolutionHbox, applyResButton, depthLabel, depthSlider);

        applyResButton.setOnAction(new EventHandler<ActionEvent>()
    	{
            @Override
            public void handle(ActionEvent event) {
        		if (Integer.parseInt(widthSceneRes.getText()) < 0 || Integer.parseInt(heightSceneRes.getText()) < 0) {
					throw new NumberFormatException();
				}

        		MainApp.HEIGHT = Integer.parseInt(heightSceneRes.getText());
                MainApp.WIDTH = Integer.parseInt(widthSceneRes.getText());
            }
        });

        stage.show();
        System.out.println("sort de call");
		return null;
	}*/
}
