package gui.materialChooser;

import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jfxtras.styles.jmetro.JMetroStyleClass;
import materials.observer.ObservableConcreteMaterial;

public class MaterialChooserControls extends GridPane 
{
	private ObservableConcreteMaterial materialChosen;
	
	private String[] labels;
	private TextField[] inputs;
	
	private static final int NB_INPUT_PER_LINE = 4;
	
	public MaterialChooserControls(ObservableConcreteMaterial material)
	{
		super();
		super.setHgap(10);
		super.setVgap(10);
		
		this.materialChosen = material;
		
		this.labels = new String[] {"Ambient : ", "Diffuse : ", "Reflection : ", "Specular intensity : ", "Specular size : ", "Refraction index : ", "Roughness : "};
		this.inputs = new TextField[labels.length];
		
		ArrayList<HBox> labelsAndInputs = new ArrayList<>();
			
		for(int i = 0; i < labels.length; i++)
		{
			inputs[i] = new TextField();
			inputs[i].setPrefWidth(45);
			inputs[i].selectedTextProperty().addListener(this::inputChangeCallback);
			
			labelsAndInputs.add(new HBox());
			labelsAndInputs.get(i).getChildren().add(new Label(labels[i]));
			labelsAndInputs.get(i).getChildren().add(inputs[i]);
			labelsAndInputs.get(i).setId(labels[i]);
			labelsAndInputs.get(i).setAlignment(Pos.CENTER);
		}

		for (int i = 0; i < labels.length; i++)
			super.add(labelsAndInputs.get(i), i % NB_INPUT_PER_LINE, i / NB_INPUT_PER_LINE);
		
		setInputsFromMaterial(this.materialChosen);
		
		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private double getInputPropertyFromLabel(ObservableConcreteMaterial material, String label)
	{
		if(label.equals("Ambient : "))
			return material.getAmbientCoeff();
		else if(label.equals("Diffuse : "))
			return material.getDiffuseCoeff();
		else if(label.equals("Reflection : "))
			return material.getReflectiveCoeff();
		else if(label.equals("Specular intensity : "))
			return material.getSpecularCoeff();
		else if(label.equals("Specular size : "))
			return material.getShininess();
		else if(label.equals("Refraction index : "))
			return material.getRefractionIndex();
		else if(label.equals("Roughness : "))
			return material.getRoughness();
		else
			return 0;
	}
	
	public void inputChangeCallback(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		Double inputValue = null;
		try
		{
			inputValue = Double.parseDouble(newValue);
		}
		catch (NumberFormatException e)
		{
			
		}
	}
	
	public void setInputsFromMaterial(ObservableConcreteMaterial material)
	{
		for(int i = 0; i < this.inputs.length; i++)
			this.inputs[i].setText(Double.toString(getInputPropertyFromLabel(material, this.labels[i])));
	}
}
