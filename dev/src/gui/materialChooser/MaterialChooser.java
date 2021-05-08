package gui.materialChooser;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import materials.Material;
import materials.MatteMaterial;
import materials.observer.MaterialObserver;
import materials.observer.ObservableConcreteMaterial;
import maths.ColorOperations;

public class MaterialChooser extends Stage
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
			this.chooserControls.setInputsFromMaterial(this.material);
			this.chooserPreview.updatePreview();
		}
	}
	
	public MaterialChooser()
	{
		super();
		this.setTitle("Choisissez votre matériau");
		this.setOnCloseRequest(this::gracefulExit);
		
		this.materialChosen = new ObservableConcreteMaterial();
		materialChosen.copyIn(new MatteMaterial(Color.RED));
		
		
		
		
		MaterialChooserControls chooserControls = new MaterialChooserControls(materialChosen);
		MaterialChooserPreview previewPane = new MaterialChooserPreview(materialChosen);
		MaterialChooserPresets presetsPane = new MaterialChooserPresets(this.materialChosen);
		colorPicker = new MaterialChooserColorPicker();
		
		Button validateButton = new Button("Valider");
		validateButton.setOnAction(this::validate);
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(presetsPane);
		mainPane.setCenter(chooserControls);
		mainPane.setRight(previewPane);
		mainPane.setLeft(colorPicker);
		mainPane.setBottom(validateButton);
		
		mainPane.setPadding(new Insets(10, 10, 10, 10));
		BorderPane.setMargin(previewPane, new Insets(10, 10, 10, 0));
		
		chooserControls.setAlignment(Pos.CENTER);
		presetsPane.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(chooserControls, Pos.CENTER);
		BorderPane.setAlignment(validateButton, Pos.CENTER);
		
		MaterialUpdateHandler materialUpdateHander = new MaterialUpdateHandler(chooserControls, previewPane, materialChosen);
		
		this.materialChosen.addListener(materialUpdateHander);
		materialUpdateHander.materialUpdated(this);
		
		Scene scene = new Scene(mainPane);
		
		this.setScene(scene);
		this.hide();
	}
	
	public Material chooseMaterial()
	{
		this.showAndWait();
		
		return this.materialChosen;
	}

	private void colorPickerCallback(ActionEvent event)
	{
		this.materialChosen.setColor(ColorOperations.sRGBGamma2_2ToLinear(this.colorPicker.getCurrentColor()));
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
