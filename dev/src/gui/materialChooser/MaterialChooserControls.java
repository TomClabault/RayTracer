package gui.materialChooser;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import jfxtras.styles.jmetro.JMetroStyleClass;
import materials.observer.ObservableConcreteMaterial;

//TODO (tom) le matériau de la preview est toujours noir juste apres le lancement du material chooser
public class MaterialChooserControls extends GridPane 
{
	private interface MaterialGetter
	{
		public double getCoefficient();
	}
	
	private interface MaterialSetter
	{
		public void setCoefficient(double newCoeff);
	}
	
	private final MaterialGetter[] getters;
	private final MaterialSetter[] setters;
	
	private ObservableConcreteMaterial materialChosen;
	
	private final String labelsName[] = new String[] {"Ambient", "Diffuse", "Reflection", "Specular intensity", "Specular size", "Refraction index", "Roughness"};
	private ArrayList<String> labels;
	private TextField[] inputs;
	private Slider[] sliders;
	
	private boolean allowedToChangeInputs;
	
	private static final int NB_INPUT_PER_LINE = 4;
	
	public MaterialChooserControls(ObservableConcreteMaterial material)
	{
		super();
		super.setHgap(10);
		super.setVgap(10);
		
		this.allowedToChangeInputs = true;
		this.materialChosen = material;
		
		this.getters = new MaterialGetter[]
		{
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getAmbientCoeff(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getDiffuseCoeff(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getReflectiveCoeff(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getSpecularCoeff(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getShininess(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getRefractionIndex(); }},
			new MaterialGetter() { @Override public double getCoefficient() { return materialChosen.getRoughness(); }}
		};
		this.setters = new MaterialSetter[]
		{
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setAmbientCoeff(newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setDiffuseCoeff(newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setReflectiveCoeff(newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setSpecularCoeff(newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setShininess((int)newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setRefractionIndex(newCoeff);}},
			new MaterialSetter() {@Override public void setCoefficient(double newCoeff) {materialChosen.setRoughness(newCoeff);}}
		};
		
		this.labels = new ArrayList<>();
		createLabels();//Ajoute les labels à l'arrayList
		this.inputs = new TextField[labels.size()];
		this.sliders = new Slider[labels.size()];
		
		ArrayList<HBox> labelsAndInputs = new ArrayList<>();
			
		StringConverter<Number> stringToDoubleConverter = new NumberStringConverter()
		{
			@Override
			public Double fromString(String in)
			{
				in = in.replace(',', '.');
				
				try
				{
					return Double.parseDouble(in);
				}
				catch (NumberFormatException e)
				{
					return 0.0;
				}
			}
		};
		
		for(int i = 0; i < labels.size(); i++)
		{
			final int finalI = i;
			
			sliders[i] = new Slider();
			sliders[i].setMax(getMaxSliderValFromLabel(labels.get(i)));
			HBox.setMargin(sliders[i], new Insets(0, 5, 0, 5));
			
			inputs[i] = new TextField();
			inputs[i].setPrefWidth(64);
			inputs[i].textProperty().addListener((ov, oldV, newV) -> inputChangeCallback(inputs[finalI], oldV, newV, labels.get(finalI)));
			inputs[i].textProperty().bindBidirectional(sliders[i].valueProperty(), stringToDoubleConverter);
			
			labelsAndInputs.add(new HBox());
			labelsAndInputs.get(i).getChildren().add(new Label(labels.get(i) + " :"));
			labelsAndInputs.get(i).getChildren().add(sliders[i]);
			labelsAndInputs.get(i).getChildren().add(inputs[i]);
		}
		
		

		for (int i = 0; i < labels.size(); i++)
			super.add(labelsAndInputs.get(i), i / NB_INPUT_PER_LINE, i % NB_INPUT_PER_LINE);
		
		setInputsFromMaterial(this.materialChosen);
		
		this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
	}
	
	private void createLabels()
	{
		for(String label : labelsName)
			labels.add(label);
	}
	
	private String generateSpaces(int length)
	{
		return length == 0 ? "" : new String(new char[length]).replace('\0', ' ');
	}
	
	/**
	 * En fonction du label passé en argument, retourne le bon coefficient du matériau
	 * Ex: pour le label 'Ambient :', cette fonction retournera le coefficient ambiant du matériau.
	 * De même pour les coefficients diffus, reflexifs et autres
	 * 
	 * @param label Un des labels de {@link gui.materialChooser.MaterialChooserControls#labelsName}
	 * 
	 * @return Le coefficient approprie du materiau
	 */
	private double getMaterialCoeffFromLabel(String label)
	{
		return this.getters[labels.indexOf(label)].getCoefficient();
	}
	
	private int getMaxSliderValFromLabel(String label)
	{
		int[] maxValues = new int[] {1, 1, 1, 1, 256, 10, 1};
		
		return maxValues[labels.indexOf(label)];
	}
	
	/**
	 * Analogue à {@link gui.materialChooser.MaterialChooserControls#getMaterialCoeffFromLabel(String)} mais agit
	 * comme un setter pour le coefficient
	 * 
	 * @param value La nouvelle valeur pour le coefficient designe par 'label'
	 * 
	 * @param label Un des labels de {@link gui.materialChooser.MaterialChooserControls#labelsName}
	 */
	private void setMaterialCoeffFromLabel(double value, String label)
	{
		this.setters[labels.indexOf(label)].setCoefficient(value);
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
		newValue = newValue.replace(',', '.');
		
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
				this.inputs[i].setText(Double.toString(getMaterialCoeffFromLabel(this.labels.get(i))));
	}
}
