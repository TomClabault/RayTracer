package gui.materialChooser;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import materials.Material;
import materials.MatteMaterial;
import materials.observer.MaterialObserver;
import materials.observer.ObservableConcreteMaterial;

public class MaterialChooser extends Stage
{
	private ObservableConcreteMaterial materialChosen;
	
	private class MaterialUpdateHandler implements MaterialObserver
	{
		private MaterialChooserControls chooserControls;
		private ObservableConcreteMaterial material;
		
		public MaterialUpdateHandler(MaterialChooserControls chooserControls, ObservableConcreteMaterial material) 
		{
			this.chooserControls = chooserControls;
			this.material = material;
		}
		
		@Override
		public void materialUpdated(Object updater) 
		{
			this.chooserControls.setInputsFromMaterial(this.material);
		}
	}
	
	public MaterialChooser()
	{
		super();
		this.setOnCloseRequest(this::gracefulExit);
		
		this.materialChosen = new ObservableConcreteMaterial();
		materialChosen.copyIn(new MatteMaterial(Color.RED));
		
		
		
		
		MaterialChooserControls chooserControls = new MaterialChooserControls(materialChosen, false);
		
		Button validateButton = new Button("Valider");
		validateButton.setOnAction(this::validate);
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(new MaterialChooserPresets(this.materialChosen, false));
		mainPane.setCenter(chooserControls);
		mainPane.setBottom(validateButton);
		BorderPane.setAlignment(validateButton, Pos.CENTER);
		
		MaterialUpdateHandler materialUpdateHander = new MaterialUpdateHandler(chooserControls, materialChosen);
		
		this.materialChosen.addListener(materialUpdateHander);
		
		Scene scene = new Scene(mainPane);
		
		this.setScene(scene);
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
