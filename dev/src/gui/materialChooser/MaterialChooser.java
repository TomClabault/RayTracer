package gui.materialChooser;

import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
import maths.ColorOperations;

/*
 * marge boule gauche
 * 
 * descendre preset (peut être au centre avec les valeurs) + texte indicatif
 * 
 * valider plus marqué et en bas à cdroite
 * 
 * centre sur plusieurs lignes
 * 
 * code rgb en dessous du color picker et possiblité de le modifier
 */

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
		
		
		
		MaterialChooserControls inputFields = new MaterialChooserControls(materialChosen);
		MaterialChooserPreview previewPane = new MaterialChooserPreview(materialChosen);
		MaterialChooserPresets presetsPane = new MaterialChooserPresets(this.materialChosen);
		colorPicker = new MaterialChooserColorPicker(materialChosen);

		VBox controlsVBox = new VBox();
		VBox.setMargin(inputFields, new Insets(10, 10, 10, 10));
		VBox.setMargin(presetsPane, new Insets(10, 10, 10, 10));
		controlsVBox.setAlignment(Pos.CENTER_LEFT);
		controlsVBox.getChildren().addAll(new Separator(), presetsPane, new Separator(), inputFields, new Separator());
		
		Button validateButton = new Button("Valider");
		validateButton.setOnAction(this::validate);
		
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(controlsVBox);
		mainPane.setRight(previewPane);
		mainPane.setLeft(colorPicker);
		mainPane.setBottom(validateButton);
		
		mainPane.setPadding(new Insets(10, 10, 10, 10));
		BorderPane.setMargin(previewPane, new Insets(10, 0, 10, 10));
		
		previewPane.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(validateButton, Pos.CENTER);
		
		MaterialUpdateHandler materialUpdateHander = new MaterialUpdateHandler(inputFields, previewPane, materialChosen);
		
		this.materialChosen.addListener(materialUpdateHander);
		materialUpdateHander.materialUpdated(this);
		
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
