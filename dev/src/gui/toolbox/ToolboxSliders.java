package gui.toolbox;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import rayTracer.RayTracerSettings;

public class ToolboxSliders 
{
	private RayTracerSettings rayTracerSettings;
	
	private GridPane slidersPane;
	
	private Slider depthSlider;
	private Slider nbCoreSlider;
	private Slider blurrySamplesSlider;
	private Slider antialiasingSlider; 
	
	public ToolboxSliders(RayTracerSettings rayTracerSettings)
	{
		this.rayTracerSettings = rayTracerSettings;
		
		this.slidersPane = createSlidersPane();
	}
	
	private GridPane createSlidersPane()
	{
		GridPane slidersPane = new GridPane();
	    slidersPane.setHgap(20);
	    
	    Label depthSliderLabel = new Label("Profondeur maximale de récursion:");
	    this.depthSlider = new Slider(0,16,2);
	    this.depthSlider.setShowTickLabels(true);
	    this.depthSlider.setShowTickMarks(true);
	    this.depthSlider.setMajorTickUnit(1);
	    this.depthSlider.setMinorTickCount(0);
	    this.depthSlider.setValue(5);
	    this.depthSlider.valueProperty().addListener(this::depthSliderCallback);
	    
	    Label nbCoreSliderLabel = new Label("Nombre de thread utilisé pour le rendu:");
	    this.nbCoreSlider = new Slider(1, 8, 1);
	    this.nbCoreSlider.setShowTickLabels(true);
	    this.nbCoreSlider.setShowTickMarks(true);
	    this.nbCoreSlider.setMajorTickUnit(1);
	    this.nbCoreSlider.setMinorTickCount(0);
	    this.nbCoreSlider.setValue(8);
	    this.nbCoreSlider.valueProperty().addListener(this::nbCoreSliderCallback);
	    
	    Label blurrySamplesSliderLabel = new Label("Nombre d'échantillons rough réflexions:");
	    this.blurrySamplesSlider = new Slider(1, 16, 1);
	    this.blurrySamplesSlider.setShowTickLabels(true);
	    this.blurrySamplesSlider.setShowTickMarks(true);
	    this.blurrySamplesSlider.setMajorTickUnit(2);
	    this.blurrySamplesSlider.setMinorTickCount(0);
	    this.blurrySamplesSlider.setValue(4);
	    this.blurrySamplesSlider.valueProperty().addListener(this::blurrySamplesSliderCallback);
	    
	    Label antialiasingSliderLabel = new Label("Nombre d'échantillons d'anti-aliasing:");
	    CheckBox antialiasingCheckbox = new CheckBox("Anti-aliasing");
	    this.antialiasingSlider = new Slider(2, 8, 1);
	    this.antialiasingSlider.setShowTickLabels(true);
	    this.antialiasingSlider.setShowTickMarks(true);
	    this.antialiasingSlider.setMajorTickUnit(1);
	    this.antialiasingSlider.setMinorTickCount(0);
	    this.antialiasingSlider.setValue(3);
	    this.antialiasingSlider.valueProperty().addListener(this::antialiasingSliderCallback);
	    antialiasingCheckbox.selectedProperty().addListener(this::antialiasingCheckboxCallback);
	    
	    GridPane.setConstraints(depthSliderLabel, 0, 0, 2, 1);
	    GridPane.setConstraints(depthSlider, 0, 1, 2, 1);
	    
	    GridPane.setConstraints(nbCoreSliderLabel, 0, 2, 2, 1);
	    GridPane.setConstraints(nbCoreSlider, 0, 3, 2, 1);
	    
	    GridPane.setConstraints(blurrySamplesSliderLabel, 0, 4, 2, 1);
	    GridPane.setConstraints(blurrySamplesSlider, 0, 5, 2, 1);
	    
	    GridPane.setConstraints(antialiasingSliderLabel, 0, 6);
	    GridPane.setConstraints(this.antialiasingSlider, 0, 7);
	    GridPane.setConstraints(antialiasingCheckbox, 1, 7);
	    
	    slidersPane.getChildren().addAll(depthSliderLabel,
	    								 depthSlider,
	    								 nbCoreSliderLabel, 
	    								 nbCoreSlider, 
	    								 blurrySamplesSliderLabel,
	    								 blurrySamplesSlider,
	    								 antialiasingSliderLabel,
	    								 antialiasingSlider, 
	    								 antialiasingCheckbox);
	    
	    return slidersPane;
	}
	
	public GridPane getSlidersPane()
	{
		return this.slidersPane;
	}
	
	private void depthSliderCallback(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
	{
		this.depthSlider.setValue(Math.round(newValue.doubleValue()));//On arrondi à chaque fois la valeur du curseur et on set la valeur arrondie
	    		
        rayTracerSettings.setRecursionDepth((int)this.depthSlider.getValue());
	}
	
	/**
	 * Méthode gérant le slider javafx du nombre de Thread
	 * @param observableValue
	 * @param oldValue
	 * @param newValue
	 */
	private void nbCoreSliderCallback(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
	{
		this.nbCoreSlider.setValue(Math.round(newValue.doubleValue()));
		int roundedValue = (int)this.nbCoreSlider.getValue();
	
		this.rayTracerSettings.setNbCore(roundedValue);
	}
	
	/**
	 * Méthode gérant le slider d'échantillon rough reflexion.
	 * @param observableValue
	 * @param oldValue
	 * @param newValue
	 */
	private void blurrySamplesSliderCallback(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
	{
		this.blurrySamplesSlider.setValue(Math.round(newValue.doubleValue()));
		int roundedValue = (int)this.blurrySamplesSlider.getValue();
	
		this.rayTracerSettings.setBlurryReflectionsSampleCount(roundedValue);
	}
	
	/**
	 * Méthode gérant le slider d'antialiasing
	 * @param observableValue
	 * @param oldValue
	 * @param newValue
	 */
	private void antialiasingSliderCallback(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)		
	{
		this.antialiasingSlider.setValue(Math.round(newValue.doubleValue()));
		int roundedValue = (int)this.antialiasingSlider.getValue();
		
		this.rayTracerSettings.setAntialiasingSampling(roundedValue*roundedValue);
	}
	private void antialiasingCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)		{ this.rayTracerSettings.enableAntialiasing(newValue); }
}
