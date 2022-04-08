package raytracer.gui.materialChooser;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import raytracer.materials.GlassMaterial;
import raytracer.materials.GlassyMaterial;
import raytracer.materials.MatteMaterial;
import raytracer.materials.MetallicMaterial;
import raytracer.materials.MirrorMaterial;
import raytracer.materials.RoughMaterial;
import raytracer.materials.observer.ObservableConcreteMaterial;

public class MaterialChooserPresets extends HBox 
{
	private ObservableConcreteMaterial materialChosen;
	
	private MaterialChooserColorPicker colorPicker;
	
	public MaterialChooserPresets(ObservableConcreteMaterial materialChosen, MaterialChooserColorPicker colorPicker)
	{
		super();
		super.setSpacing(10);;
		
		this.materialChosen = materialChosen;
		this.colorPicker = colorPicker;
		
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
		
//		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private void matteButtonCallback(ActionEvent event) 
	{ 
		this.materialChosen.copyIn(new MatteMaterial(this.colorPicker.getCustomColor())); 
	}
	
	private void metallicButtonCallback(ActionEvent event) 
	{ 
		this.materialChosen.copyIn(new MetallicMaterial(this.colorPicker.getCustomColor())); 
	}
	
	private void mirrorButtonCallback(ActionEvent event)
	{ 
		this.materialChosen.copyIn(new MirrorMaterial(0.75)); 
	}
	
	private void glassButtonCallback(ActionEvent event) 
	{ 
		this.materialChosen.copyIn(new GlassMaterial()); 
	}
	
	private void glassyButtonCallback(ActionEvent event) 
	{ 
		this.materialChosen.copyIn(new GlassyMaterial(this.colorPicker.getCustomColor()));
	}
	
	private void roughButtonCallback(ActionEvent event) 
	{ 
		this.materialChosen.copyIn(new RoughMaterial(this.colorPicker.getCustomColor(), 0.75)); 
	}
}
