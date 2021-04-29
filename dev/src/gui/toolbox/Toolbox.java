package gui.toolbox;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;

import java.io.File;
import java.io.IOException;

import gui.windows.ChooseRenderSettingsWindow;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import rayTracer.RayTracerSettings;

/**
 * La classe contenant le code de la toolbox, c'est-à-dire la fenêtre contenant les paramêtres que l'ont peux manipuler pendant l'affichage du rendu.
 */
public class Toolbox{

	private Pane statPane;
	private RayTracerSettings rayTracerSettings;
	
	private ProgressBar progressBar;
	private WritableImage writableImage;
	
	/**
	 * @param renderScene la scène javafx contenant le rendu.
	 * @param statPane le Pane contenant les statistiques du rendu (typiquement les fps).
	 * @param rayTracerSettings les paramêtres du rayTracer.
	 */
	public Toolbox(Pane statPane, ProgressBar progressBar, RayTracerSettings rayTracerSettings, WritableImage writableImage) {
		this.statPane = statPane;
		this.rayTracerSettings = rayTracerSettings;
		this.progressBar = progressBar;
		this.writableImage = writableImage;
	}

	/**
	 * Méthode affichant la toolbox.
	 */
	public void execute() {

		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(ChooseRenderSettingsWindow.class.getResource("../styles/windows.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        CheckBox statOnOffCheckBox = new CheckBox("Affichage des stats");
        
        Label progressLabel = new Label("Avancement du rendu de l'image");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        GridPane slidersPane = new ToolboxSliders(rayTracerSettings).getSlidersPane();
        GridPane checkboxesPane = new ToolboxCheckboxes(rayTracerSettings).getCheckboxesPane();
        
        root.getChildren().addAll(statOnOffCheckBox, 
        						  new ToolboxSaveButton("Sauvegarder le rendu", stage, this.writableImage), 
        						  new Separator(),
        						  slidersPane,
        						  new Separator(), 
        						  checkboxesPane,
        						  new Separator(),
        						  progressLabel,
        						  this.progressBar);

        statOnOffCheckBox.setOnAction(new EventHandler<ActionEvent>()
        {
        	@Override
        	public void handle(ActionEvent event) {
        		statPane.setVisible(statOnOffCheckBox.isSelected());
        	}
        });
       
        stage.show();
	}
}
