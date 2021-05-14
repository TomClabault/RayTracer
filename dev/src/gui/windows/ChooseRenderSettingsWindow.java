package gui.windows;

import javafx.scene.Scene;

import java.net.URL;

import gui.MainApp;
import gui.toolbox.ToolboxCheckboxes;
import gui.toolbox.ToolboxSliders;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import rayTracer.RayTracerSettings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

//TODO (tom) bien centrer les cases pour choisir la larguer/hauteur et les textes et les boutons et tout en fait
//#retravailler l'interface

/**
 * La classe contenant le code du choix de la taille du rendu.
 */
public class ChooseRenderSettingsWindow 
{
	private Stage windowStage;
	
	private GridPane mainGridPane;
	
	private TextField inputLargeur;
	private TextField inputHauteur;
    
	private CheckBox fullscreenCheckbox;
	private CheckBox simpleRenderCheckbox;
	
	private VBox settingsVBox;
	
	private RayTracerSettings rayTracerSettings;
	
	public ChooseRenderSettingsWindow(RayTracerSettings rayTracerSettings)
	{
		this.rayTracerSettings = rayTracerSettings;
	}
	
	/**
	 * Methode executant le code pour afficher la fenetre du choix de rendu.
	 */
	public void execute() {
		this.windowStage = new Stage();
        this.mainGridPane = new GridPane();
        mainGridPane.setHgap(10);
        mainGridPane.setVgap(10);
        mainGridPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        
        Scene scene = new Scene(mainGridPane);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(scene);
        
        this.windowStage.setScene(scene);
        this.windowStage.setTitle("Selection de la taille de rendu");

        
        
        
        
        Label textLargeur = new Label("Largeur de rendu:");
        Label textHauteur = new Label("Hauteur de rendu:");
        
        this.inputLargeur = new TextField("");
        this.inputLargeur.setPromptText("Largeur");
        this.inputHauteur = new TextField("");
        this.inputHauteur.setPromptText("Hauteur");
        
        
        
        this.fullscreenCheckbox = new CheckBox("Adapter a l'ecran");
        this.simpleRenderCheckbox = new CheckBox("Rendre une seule image");
        
        Button validateButton = new Button("Valider");
        Button cancelButton = new Button("Annuler");
        GridPane.setHalignment(validateButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        ToolboxCheckboxes toolboxCheckboxes = new ToolboxCheckboxes(rayTracerSettings);
        ToolboxSliders toolboxSliders = new ToolboxSliders(rayTracerSettings);
        
        Label chooseSettingsLabel = new Label("Choisissez les reglages de votre rendu:");
        
        Separator separatorSettingsLabelTop = new Separator();
        Separator separatorSettingsLabelBottom = new Separator();
        Separator separatorSlidersCheckboxes = new Separator();
        
        this.settingsVBox = new VBox();
        this.settingsVBox.setAlignment(Pos.CENTER);
        this.settingsVBox.getChildren().addAll(separatorSettingsLabelTop,
        								  chooseSettingsLabel,
        								  separatorSettingsLabelBottom,
        								  toolboxSliders,
        								  separatorSlidersCheckboxes,
        								  toolboxCheckboxes);
        VBox.setMargin(separatorSettingsLabelTop, new Insets(10, 0, 10, 0));
        VBox.setMargin(separatorSettingsLabelBottom, new Insets(10, 0, 10, 0));
        VBox.setMargin(separatorSlidersCheckboxes, new Insets(10, 0, 10, 0));
        VBox.setMargin(toolboxCheckboxes, new Insets(0, 10, 10, 10));
        VBox.setMargin(toolboxSliders, new Insets(0, 10, 0, 10));
        
        
        mainGridPane.add(textLargeur, 0, 0);
        mainGridPane.add(textHauteur, 0, 1);
        GridPane.setMargin(textLargeur, new Insets(10, 10, 0, 0));
        GridPane.setMargin(inputLargeur, new Insets(10, 0, 0, 10));
        
        mainGridPane.add(inputLargeur, 1, 0);
        mainGridPane.add(inputHauteur, 1, 1);
        
        mainGridPane.add(fullscreenCheckbox, 0, 2);
        mainGridPane.add(simpleRenderCheckbox, 1, 2);
        
        mainGridPane.add(validateButton, 0, 3);
        mainGridPane.add(cancelButton, 1, 3);
        
        
        
        
        fullscreenCheckbox.selectedProperty().addListener(this::toggleFullscreen);
        simpleRenderCheckbox.selectedProperty().addListener(this::toggleSimpleRender);
        
        validateButton.setOnAction(this::validate);
        cancelButton.setOnAction(this::exitApp);
        
        this.windowStage.setOnCloseRequest(this::exitApp);
        this.windowStage.showAndWait();
    }
	
	public void showRenderSettings(boolean bShow) 
	{
		if(bShow)
			this.mainGridPane.add(this.settingsVBox, 0, 4, 2, 1);
		else
			this.mainGridPane.getChildren().remove(this.settingsVBox);
		
		this.windowStage.sizeToScene();
	}
	
	public void toggleSimpleRender(ObservableValue<? extends Boolean> observale, Boolean oldValue, Boolean newValue)
	{
		MainApp.SIMPLE_RENDER = newValue;
		
		showRenderSettings(newValue);
	}
	
	public void toggleFullscreen(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		MainApp.FULLSCREEN_MODE = newValue;
	}
	
	public void validate(ActionEvent event)
	{
    	try {
			if (Integer.parseInt(inputHauteur.getText()) < 0 || Integer.parseInt(inputLargeur.getText()) < 0) {
				throw new NumberFormatException();
			}
    		MainApp.HEIGHT = Integer.parseInt(inputHauteur.getText());
            MainApp.WIDTH = Integer.parseInt(inputLargeur.getText());
    		
            this.windowStage.close();
		} catch (NumberFormatException e) {
			System.out.println("Les arguments doivent etre des entiers positifs");
		}
	}
	
	public void exitApp(Event event)
	{
        Platform.exit();
        System.exit(0);
	}
}
