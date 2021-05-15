package gui.materialChooser;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jfxtras.styles.jmetro.JMetroStyleClass;
import materials.observer.ObservableConcreteMaterial;

//TODO (tom) add change listener aux inputs pourvérifier avant que ce qui a été écrit dans l'input du coeff du matériau est correct
//si l'input n'est pas correct, le NumberStringConverter va planter
public class MaterialChooserControls extends GridPane 
{
	private ObservableConcreteMaterial materialChosen;
	
	private String[] labels;
	private TextField[] inputs;
	
	private boolean allowedToChangeInputs;
	
	private static final int NB_INPUT_PER_LINE = 4;
	
	public MaterialChooserControls(ObservableConcreteMaterial material)
	{
		super();
		super.setHgap(10);
		super.setVgap(10);
		
		this.allowedToChangeInputs = true;
		this.materialChosen = material;
		
		this.labels = new String[] {"Ambient : ", "Diffuse : ", "Reflection : ", "Specular intensity : ", "Specular size : ", "Refraction index : ", "Roughness : "};
		this.inputs = new TextField[labels.length];
		
		ArrayList<HBox> labelsAndInputs = new ArrayList<>();
			
		for(int i = 0; i < labels.length; i++)
		{
			final int finalI = i;
			
			inputs[i] = new TextField();
			inputs[i].setPrefWidth(64);
			inputs[i].textProperty().addListener((ov, oldV, newV) -> inputChangeCallback(inputs[finalI], oldV, newV, labels[finalI]));
			
			labelsAndInputs.add(new HBox());
			labelsAndInputs.get(i).getChildren().add(new Label(labels[i]));
			labelsAndInputs.get(i).getChildren().add(inputs[i]);
			labelsAndInputs.get(i).setAlignment(Pos.CENTER);
		}

		for (int i = 0; i < labels.length; i++)
			super.add(labelsAndInputs.get(i), i % NB_INPUT_PER_LINE, i / NB_INPUT_PER_LINE);
		
		setInputsFromMaterial(this.materialChosen);
		
		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private double getMaterialCoeffFromLabel(String label)
	{
		if(label.equals("Ambient : "))
			return this.materialChosen.getAmbientCoeff();
		else if(label.equals("Diffuse : "))
			return this.materialChosen.getDiffuseCoeff();
		else if(label.equals("Reflection : "))
			return this.materialChosen.getReflectiveCoeff();
		else if(label.equals("Specular intensity : "))
			return this.materialChosen.getSpecularCoeff();
		else if(label.equals("Specular size : "))
			return this.materialChosen.getShininess();
		else if(label.equals("Refraction index : "))
			return this.materialChosen.getRefractionIndex();
		else if(label.equals("Roughness : "))
			return this.materialChosen.getRoughness();
		else
			return 0;
	}
	
	private void setMaterialCoeffFromLabel(double value, String label)
	{
		if(label.equals("Ambient : "))
			this.materialChosen.setAmbientCoeff(value);
		else if(label.equals("Diffuse : "))
			this.materialChosen.setDiffuseCoeff(value);
		else if(label.equals("Reflection : "))
			this.materialChosen.setReflectiveCoeff(value);
		else if(label.equals("Specular intensity : "))
			this.materialChosen.setSpecularCoeff(value);
		else if(label.equals("Specular size : "))
			this.materialChosen.setShininess((int)value);
		else if(label.equals("Refraction index : "))
			this.materialChosen.setRefractionIndex(value);
		else if(label.equals("Roughness : "))
			this.materialChosen.setRoughness(value);
	}
	
	private void extendSelection(TextField input, int endSelection)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				input.extendSelection(endSelection);
			}
		});
	}
	
	private void inputChangeCallback(TextField sourceField, String oldValue, String newValue, String coeffLabel)
	{
		Double newValueD = null;
		
		try
		{
			newValueD = Double.parseDouble(newValue);
			
			this.allowedToChangeInputs = false;
			setMaterialCoeffFromLabel(newValueD, coeffLabel);
			this.allowedToChangeInputs = true;
		}
		catch(NumberFormatException e)
		{
			if(newValue.indexOf('.') != newValue.lastIndexOf('.'))//Il y a deux caractères '.' dans le nombre
			{
				sourceField.setText(oldValue);//On reset le texte d'avant, le nouveau n'est pas bon
				
				setCaretPosition(sourceField, oldValue.indexOf('.') + 1);//On positionne le curseur après le point
				extendSelection(sourceField, oldValue.length());//On sélectionne toutes les décimales
			}
			else if(newValue.equals(""))//L'utilssateur a effacé tous les caractères du champ
			{
				this.allowedToChangeInputs = false;
				setMaterialCoeffFromLabel(0, coeffLabel);
				this.allowedToChangeInputs = true;
			}
		}
	}
	
	private void setCaretPosition(TextField input, int position)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				input.positionCaret(position);
			}
		});
	}
	
	public void setInputsFromMaterial(ObservableConcreteMaterial material)
	{
		if(allowedToChangeInputs)
			for(int i = 0; i < this.inputs.length; i++)
				this.inputs[i].setText(Double.toString(getMaterialCoeffFromLabel(this.labels[i])));
	}
}
