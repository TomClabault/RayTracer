package render;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import scene.RayTracingScene;

public class Toolbox{

	private RayTracingScene rayTracingScene;
	private Scene renderScene;
	private Pane statPane;

	public Toolbox(RayTracingScene rts, Scene renderScene, Pane statPane) {
		this.rayTracingScene = rts;
		this.renderScene = renderScene;
		this.statPane = statPane;
	}

	public RayTracingScene getRts() {
		return rayTracingScene;
	}

	public void setRts(RayTracingScene rts) {
		this.rayTracingScene = rts;
	}

	public void execute() {



		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(SetSizeWindow.class.getResource("style/window.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        CheckBox statOnOffCheckBox = new CheckBox("Affichage des stats");
        statOnOffCheckBox.setSelected(true);


        Button saveButton = new Button("Sauvegarder le rendu");

        Label resolutionLabel = new Label("Résolution de la scène");

        HBox resolutionHbox = new HBox();
        TextField widthSceneRes = new TextField("Width");
        TextField heightSceneRes = new TextField("Height");
        resolutionHbox.getChildren().addAll(widthSceneRes, heightSceneRes);

        Button applyResButton = new Button("Appliquer");

        Label depthLabel = new Label();
        Slider depthSlider = new Slider(0,10,1);

        root.getChildren().addAll(statOnOffCheckBox, saveButton, resolutionLabel, resolutionHbox, applyResButton, depthLabel, depthSlider);

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
        saveButton.setOnAction(new EventHandler<ActionEvent>()
    	{
            @Override
            public void handle(ActionEvent event) {
            	FileChooser fileChooser = new FileChooser();
            	 fileChooser.setTitle("Chemin d'enregistrement du rendu");
            	 ExtensionFilter filter = new ExtensionFilter("Image", "*.png");
            	 fileChooser.getExtensionFilters().add(filter);
            	 File file = fileChooser.showSaveDialog(stage);
            	 if (file != null) {
            		 try {
            			 util.ImageUtil.writeImageToDisk(renderScene, file);
            			 System.out.println("Image sauvegardée en : " + file);
					} catch (IOException e) {
						System.out.println("Impossible de sauvegarder l'image");
					}

            	 } else {
            		 System.out.println("Aucun dossier n'est sélectionner");
            	 }
            }
        });
        statOnOffCheckBox.setOnAction(new EventHandler<ActionEvent>()
        {
        	@Override
        	public void handle(ActionEvent event) {
        		statPane.setVisible(statOnOffCheckBox.isSelected());
        	}
        });

        stage.show();
	}
}
