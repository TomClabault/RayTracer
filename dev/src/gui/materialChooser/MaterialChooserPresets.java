package gui.materialChooser;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import materials.GlassMaterial;
import materials.GlassyMaterial;
import materials.MatteMaterial;
import materials.MetallicMaterial;
import materials.MirrorMaterial;
import materials.RoughMaterial;
import materials.observer.ObservableConcreteMaterial;

public class MaterialChooserPresets extends GridPane 
{
	private ObservableConcreteMaterial materialChosen;
	
	private boolean colorChosen;
	
	//TODO (tom) ne modifier la couleur que si elle n'a pas déjà été modifié et donc que l'utilisateur a pas déjà choisir la couleur qu'il voulait
	public MaterialChooserPresets(ObservableConcreteMaterial materialChosen, boolean colorChosen)
	{
		super();
		super.setHgap(10);
		super.setVgap(10);
		
		this.materialChosen = materialChosen;
		this.colorChosen = colorChosen;
		
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
		
		this.add(matteButton, 0, 0);
		this.add(metallicButton, 1, 0);
		this.add(mirrorButton, 2, 0);
		this.add(glassButton, 0, 1);
		this.add(glassyButton, 1, 1);
		this.add(roughButton, 2, 1);
		this.add(new Separator(), 0, 2, 3, 1);
	}
	
	private void matteButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MatteMaterial(Color.RED)); }
	private void metallicButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MetallicMaterial(Color.RED)); }
	private void mirrorButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new MirrorMaterial(0.75)); }
	private void glassButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new GlassMaterial()); }
	private void glassyButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new GlassyMaterial(Color.RED)); }
	private void roughButtonCallback(ActionEvent event) { this.materialChosen.copyIn(new RoughMaterial(Color.RED, 0.75)); }
}
