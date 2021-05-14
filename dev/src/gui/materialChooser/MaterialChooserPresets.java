package gui.materialChooser;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import jfxtras.styles.jmetro.JMetroStyleClass;
import materials.GlassMaterial;
import materials.GlassyMaterial;
import materials.MatteMaterial;
import materials.MetallicMaterial;
import materials.MirrorMaterial;
import materials.RoughMaterial;
import materials.observer.ObservableConcreteMaterial;

public class MaterialChooserPresets extends HBox 
{
	private ObservableConcreteMaterial materialChosen;
	
	//TODO (tom) quand on choisit du verre ou un material mirror, l'ancienne couleur qu'on avait disparait
	public MaterialChooserPresets(ObservableConcreteMaterial materialChosen)
	{
		super();
		super.setSpacing(10);;
		
		this.materialChosen = materialChosen;
		
		Button matteButton = new Button("Matte");
		Button metallicButton = new Button("Metallic");
		Button mirrorButton = new Button("Mirror");
		Button glassButton = new Button("Glass");
		Button glassyButton = new Button("Glassy");
		Button roughButton = new Button("Rough");

		matteButton.setOnAction(this::matteButtonCallback);
		metallicButton.setOnAction(this::metallicButtonCallback);
		mirrorButton.setOnAction(this::mirrorButtonCallback);
		glassButton.setOnAction(this::glassButtonCallback);
		glassyButton.setOnAction(this::glassyButtonCallback);
		roughButton.setOnAction(this::roughButtonCallback);
		
		matteButton.setPrefSize(96, 48);
		metallicButton.setPrefSize(96, 48);
		mirrorButton.setPrefSize(96, 48);
		glassButton.setPrefSize(96, 48);
		glassyButton.setPrefSize(96, 48);
		roughButton.setPrefSize(96, 48);
		
		super.getChildren().addAll(matteButton, metallicButton, mirrorButton, glassButton, glassyButton, roughButton);
		super.setAlignment(Pos.CENTER);
		
		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private void matteButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MatteMaterial(this.materialChosen.getColor())); }
	private void metallicButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MetallicMaterial(this.materialChosen.getColor())); }
	private void mirrorButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MirrorMaterial(0.75)); }
	private void glassButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new GlassMaterial()); }
	private void glassyButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new GlassyMaterial(this.materialChosen.getColor())); }
	private void roughButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new RoughMaterial(this.materialChosen.getColor(), 0.75)); }
}
