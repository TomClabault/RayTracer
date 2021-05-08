package gui.materialChooser;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import materials.Material;
import materials.MatteMaterial;

public class MaterialChooser extends Stage
{
	private Material materialChosen;
	
	public MaterialChooser()
	{
		super();
		
		materialChosen = new MatteMaterial(Color.BLACK);
	}
	
	public Material chooseMaterial()
	{
		this.showAndWait();
		
		return this.materialChosen;
	}
}
