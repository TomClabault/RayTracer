package render;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import rayTracer.RayTracer;
import rayTracer.RayTracerSettings;
import scene.RayTracingScene;

/**
 * La classe contenant le code de la toolbox, c'est-à-dire la fenêtre contenant les paramêtres que l'ont peux manipuler pendant l'affichage du rendu.
 */
public class Toolbox{

	private Scene renderScene;
	private Pane statPane;
	private RayTracerSettings rayTracerSettings;
	
	private Slider nbCoreSlider;//Attribut nécessaire pour pouvoir y accéder dans les méthodes Callback
	private Slider blurrySamplesSlider;
	private Slider antialiasingSlider;
	
	/**
	 * @param renderScene la scène javafx contenant le rendu.
	 * @param statPane le Pane contenant les statistiques du rendu (typiquement les fps).
	 * @param rayTracerSettings les paramêtres du rayTracer.
	 */
	public Toolbox(Scene renderScene, Pane statPane, RayTracerSettings rayTracerSettings) {
		this.renderScene = renderScene;
		this.statPane = statPane;
		this.rayTracerSettings = rayTracerSettings;
	}

	/**
	 * Méthode affichant la toolbox.
	 */
	public void execute() {

		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(SetSizeWindow.class.getResource("style/window.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        CheckBox statOnOffCheckBox = new CheckBox("Affichage des stats");
        
        Button saveButton = new Button("Sauvegarder le rendu");

        Label resolutionLabel = new Label("Résolution de la scène");
        
        Button applyResButton = new Button("Appliquer");

        /*
         * ------------------ Sliders ------------------ 
         */
        GridPane slidersPane = new GridPane();
        slidersPane.setHgap(20);
        
        Label depthSliderLabel = new Label("Profondeur maximale de récursion:");
        Slider depthSlider = new Slider(0,10,1);
        depthSlider.setShowTickLabels(true);
        depthSlider.setShowTickMarks(true);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setMinorTickCount(0);
        depthSlider.setValue(5);
        
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
        /*
         * ------------------ Sliders ------------------ 
         */
        
        /*
         * ------------------ Checkboxes ------------------ 
         */
        GridPane checkboxesPane = new GridPane();
        checkboxesPane.setHgap(10);
        checkboxesPane.setVgap(5);
        
        CheckBox ambiantCheckbox = new CheckBox("Ambiante");
        CheckBox diffuseCheckbox = new CheckBox("Diffuse");
        CheckBox reflectionsCheckbox = new CheckBox("Réflexions");
        CheckBox roughReflectionsCheckbox = new CheckBox("Rough réflexions");
        CheckBox refractionsCheckbox = new CheckBox("Réfractions"); 
        CheckBox specularCheckbox = new CheckBox("Spécularité"); 
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
        /*
         * ------------------ Checkboxes ------------------ 
         */
        
        root.getChildren().addAll(statOnOffCheckBox, 
        						  saveButton, 
        						  resolutionLabel, 
        						  applyResButton,
        						  new Separator(),
        						  slidersPane,
        						  new Separator(), 
        						  checkboxesPane);

        saveButton.setOnAction(new EventHandler<ActionEvent>()
    	{
            @Override
            public void handle(ActionEvent event) {
            	 FileChooser fileChooser = new FileChooser();
            	 fileChooser.setTitle("Chemin d'enregistrement du rendu");
            	 ExtensionFilter filter = new ExtensionFilter("Image", "*.png");
            	 fileChooser.getExtensionFilters().add(filter);
            	 File file = fileChooser.showSaveDialog(stage);
            	 if (file != null) {
            		 try {
            			 util.ImageUtil.writeSceneToDiskFile(renderScene, file);
            			 System.out.println("Image sauvegardée en : " + file);
					} catch (IOException e) {
						System.out.println("Impossible de sauvegarder l'image");
					}

            	 } else {
            		 System.out.println("Aucun dossier n'est sélectionner");
            	 }
            }
        });
        statOnOffCheckBox.setOnAction(new EventHandler<ActionEvent>()
        {
        	@Override
        	public void handle(ActionEvent event) {
        		statPane.setVisible(statOnOffCheckBox.isSelected());
        	}
        });
        depthSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
        	{
        		//Permet de ne faire se déplacer le curseur du slider que de valeur entière en valeur entière.
        		depthSlider.setValue(Math.round(new_val.doubleValue()));//On arrondi à chaque fois la valeur du curseur et on set la valeur arrondie
        		
                rayTracerSettings.setRecursionDepth((int)depthSlider.getValue());
            }
        });

        stage.show();
	}
	
	private void ambiantCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableAmbient(newValue); }
	private void diffuseCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableDiffuse(newValue); }
	private void reflectionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)		{ this.rayTracerSettings.enableReflections(newValue); }
	private void roughReflectionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)	{ this.rayTracerSettings.enableBlurryReflections(newValue); }
	private void refractionsCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)		{ this.rayTracerSettings.enableRefractions(newValue); }
	private void specularCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableSpecular(newValue); }
	private void fresnelCheckboxCallback(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue)			{ this.rayTracerSettings.enableFresnel(newValue); }
	
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
