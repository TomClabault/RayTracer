package render;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

public class Toolbox{

	private RayTracingScene rayTracingScene;
	private Scene renderScene;
	private Pane statPane;
	private RayTracerSettings rayTracerSettings;
	private RayTracer rayTracer;

	public Toolbox(RayTracingScene rayTracingScene, Scene renderScene, Pane statPane, RayTracer rayTracer, RayTracerSettings rayTracerSettings) {
		this.rayTracingScene = rayTracingScene;
		this.rayTracer = rayTracer;
		this.renderScene = renderScene;
		this.statPane = statPane;
		this.rayTracerSettings = rayTracerSettings;
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
        statOnOffCheckBox.setSelected(false);
        
        //CheckBox skyboxCheckbox = new CheckBox("Affichage des stats");
        //statOnOffCheckBox.setSelected(true);

        Button saveButton = new Button("Sauvegarder le rendu");

        Label resolutionLabel = new Label("Résolution de la scène");
        
        Button applyResButton = new Button("Appliquer");

        Label depthLabel = new Label("Profondeur maximale de récursion");
        Slider depthSlider = new Slider(0,10,1);
        depthSlider.setShowTickLabels(true);
        depthSlider.setShowTickMarks(true);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setMinorTickCount(0);
        depthSlider.setBlockIncrement(10);
        depthSlider.setValue(5);
        depthSlider.setSnapToTicks(true);

        root.getChildren().addAll(statOnOffCheckBox, saveButton, resolutionLabel, applyResButton, depthLabel, depthSlider);

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
        depthSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
        	{
        		//Permet de ne faire se déplacer le curseur du slider que de valeur entière en valeur entière.
        		depthSlider.setValue(Math.round(new_val.doubleValue()));//On arrondi à chaque fois la valeur du curseur et on set la valeur arrondie
        		
                rayTracerSettings.setRecursionDepth((int)depthSlider.getValue());
            }
        });

        stage.show();
	}
	
	
}
