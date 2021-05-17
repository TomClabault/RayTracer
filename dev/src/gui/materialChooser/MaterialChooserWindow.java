package gui.materialChooser;

import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import materials.Material;
import materials.MatteMaterial;
import materials.observer.MaterialObserver;
import materials.observer.ObservableConcreteMaterial;

//TODO (tom) finir l'interface du material chooser -> centrer la preview et faire quelque chose avec le bouton valider
//TODO (tom) faire le code hexa fonctionnel pour le color picker

public class MaterialChooserWindow extends Stage
{
	private ObservableConcreteMaterial materialChosen;
	
	private MaterialChooserColorPicker colorPicker;
	
	private class MaterialUpdateHandler implements MaterialObserver
	{
		private MaterialChooserControls chooserControls;
		private MaterialChooserPreview chooserPreview;
		
		private ObservableConcreteMaterial material;
		
		public MaterialUpdateHandler(MaterialChooserControls chooserControls, MaterialChooserPreview previewPane, ObservableConcreteMaterial material) 
		{
			this.chooserControls = chooserControls;
			this.chooserPreview = previewPane;
			
			this.material = material;
		}
		
		@Override
		public void materialUpdated(Object updater) 
		{
			this.chooserControls.setInputsFromMaterial(material);
			this.chooserPreview.updatePreview();
		}
	}
	
	public MaterialChooserWindow()
	{
		super();
		this.setResizable(false);
		this.setTitle("Choisissez votre matériau");
		this.setOnCloseRequest(this::gracefulExit);
		
		this.materialChosen = new ObservableConcreteMaterial();
		
		
		
		colorPicker = new MaterialChooserColorPicker(materialChosen);
		MaterialChooserControls inputFields = new MaterialChooserControls(materialChosen);
		MaterialChooserPreview previewPane = new MaterialChooserPreview(materialChosen);
		MaterialChooserPresets presetsPane = new MaterialChooserPresets(materialChosen, colorPicker);
		
		Label presetsLabel = new Label("Presets:");
		presetsLabel.setAlignment(Pos.CENTER);
		presetsLabel.setMaxWidth(Double.MAX_VALUE);
		
		Button validateButton = new Button("Valider");
		validateButton.setOnAction(this::validate);
		
		VBox controlsVBox = new VBox();
		VBox.setMargin(inputFields, new Insets(10, 10, 10, 10));
		VBox.setMargin(presetsPane, new Insets(10, 10, 10, 10));
		controlsVBox.setAlignment(Pos.CENTER_LEFT);
		controlsVBox.getChildren().addAll(new Separator(), presetsLabel, presetsPane, new Separator(), inputFields, new Separator());
		
		HBox previewValidateHBox = new HBox();
		previewValidateHBox.getChildren().add(previewPane);
		previewValidateHBox.getChildren().add(validateButton);
		
		
		
		
		
		GridPane mainPane = new GridPane();
		mainPane.add(colorPicker, 0, 0, 1, 2);
		mainPane.add(controlsVBox, 1, 0);
		mainPane.add(previewValidateHBox, 1, 1);
		
		mainPane.setPadding(new Insets(10, 10, 10, 10));
		GridPane.setMargin(previewPane, new Insets(10, 0, 10, 0));
		
		previewPane.setAlignment(Pos.CENTER);
		
		MaterialUpdateHandler materialUpdateHander = new MaterialUpdateHandler(inputFields, previewPane, materialChosen);
		
		this.materialChosen.addListener(materialUpdateHander);
		materialUpdateHander.materialUpdated(this);
		materialChosen.copyIn(new MatteMaterial(Color.RED));
		
		Scene scene = new Scene(mainPane);
		URL cssURL = getClass().getResource("../styles/colorPicker.css");
		if(cssURL != null)
			scene.getStylesheets().add(cssURL.toExternalForm());
		else
		{
			System.out.println("Impossible de trouver colorPicker.css");
		
			gracefulExit(null);
		}
		
		JMetro jMetro = new JMetro(Style.DARK);
		jMetro.setScene(scene);
		mainPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
		
		this.setScene(scene);
		this.sizeToScene();
		this.hide();
	}
	
	public Material chooseMaterial()
	{
		this.showAndWait();
		
		return this.materialChosen;
	}

	private void gracefulExit(WindowEvent event)
	{
		Platform.exit();
		System.exit(0);
	}
	
	private void validate(ActionEvent event)
	{
		this.close();
	}
}
