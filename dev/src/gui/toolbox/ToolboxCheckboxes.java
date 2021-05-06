package gui.toolbox;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import rayTracer.RayTracerSettings;

//TODO (tom) extends GridPane
public class ToolboxCheckboxes 
{
	private RayTracerSettings rayTracerSettings;
	
	private GridPane checkboxesPane;
	
	public ToolboxCheckboxes(RayTracerSettings rayTracerSettings)
	{
		this.rayTracerSettings = rayTracerSettings;
		
		this.checkboxesPane = createCheckboxesPane();
	}
	
	private GridPane createCheckboxesPane()
	{
		GridPane checkboxesPane = new GridPane();
		checkboxesPane.setHgap(10);
		checkboxesPane.setVgap(5);
        
        CheckBox ambiantCheckbox = new CheckBox("Ambiante");
        CheckBox diffuseCheckbox = new CheckBox("Diffuse");
        CheckBox reflectionsCheckbox = new CheckBox("Reflexions");
        CheckBox roughReflectionsCheckbox = new CheckBox("Rough reflexions");
        CheckBox refractionsCheckbox = new CheckBox("Refractions"); 
        CheckBox specularCheckbox = new CheckBox("Specularite"); 
        CheckBox fresnelCheckbox = new CheckBox("Fresnel");  
        
        ambiantCheckbox.setSelected(true);
        diffuseCheckbox.setSelected(true);
        reflectionsCheckbox.setSelected(true);
        roughReflectionsCheckbox.setSelected(true);
        refractionsCheckbox.setSelected(true);
        specularCheckbox.setSelected(true);
        fresnelCheckbox.setSelected(true);
        
        ambiantCheckbox.selectedProperty().addListener(this::ambiantCheckboxCallback);
        diffuseCheckbox.selectedProperty().addListener(this::diffuseCheckboxCallback);
        reflectionsCheckbox.selectedProperty().addListener(this::reflectionsCheckboxCallback);
        roughReflectionsCheckbox.selectedProperty().addListener(this::roughReflectionsCheckboxCallback);
        refractionsCheckbox.selectedProperty().addListener(this::refractionsCheckboxCallback);
        specularCheckbox.selectedProperty().addListener(this::specularCheckboxCallback);
        fresnelCheckbox.selectedProperty().addListener(this::fresnelCheckboxCallback);
        
       
        
        GridPane.setConstraints(ambiantCheckbox, 0, 0);
        GridPane.setConstraints(diffuseCheckbox, 1, 0);
        GridPane.setConstraints(reflectionsCheckbox, 2, 0);
        GridPane.setConstraints(roughReflectionsCheckbox, 3, 0);
        GridPane.setConstraints(refractionsCheckbox, 0, 1);
        GridPane.setConstraints(specularCheckbox, 1, 1);
        GridPane.setConstraints(fresnelCheckbox, 2, 1);
        
        checkboxesPane.getChildren().addAll(ambiantCheckbox, 
        									diffuseCheckbox,
        									reflectionsCheckbox,
        									roughReflectionsCheckbox,
        									refractionsCheckbox,
        									specularCheckbox,
        									fresnelCheckbox);
        
        return checkboxesPane;
	}
	
	public GridPane getCheckboxesPane()
	{
		return this.checkboxesPane;
	}
	
	private void ambiantCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableAmbient(newValue); }
	private void diffuseCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableDiffuse(newValue); }
	private void reflectionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)		{ this.rayTracerSettings.enableReflections(newValue); }
	private void roughReflectionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)	{ this.rayTracerSettings.enableBlurryReflections(newValue); }
	private void refractionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)		{ this.rayTracerSettings.enableRefractions(newValue); }
	private void specularCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableSpecular(newValue); }
	private void fresnelCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableFresnel(newValue); }
}
