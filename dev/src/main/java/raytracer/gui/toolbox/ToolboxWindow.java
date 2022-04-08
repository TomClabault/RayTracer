package raytracer.gui.toolbox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import raytracer.gui.windows.ChooseRenderSettingsWindow;
import raytracer.rayTracer.RayTracerSettings;

/**
 * La classe contenant le code de la toolbox, c'est-a-dire la fenetre contenant les parametres que l'ont peux manipuler pendant l'affichage du rendu.
 */
public class ToolboxWindow{

	private Pane statPane;
	private RayTracerSettings rayTracerSettings;
	
	private ProgressBar progressBar;
	private WritableImage writableImage;
	
	/**
	 * @param renderScene la scene javafx contenant le rendu.
	 * @param statPane le Pane contenant les statistiques du rendu (typiquement les fps).
	 * @param rayTracerSettings les parametres du rayTracer.
	 */
	public ToolboxWindow(Pane statPane, ProgressBar progressBar, RayTracerSettings rayTracerSettings, WritableImage writableImage) {
		this.statPane = statPane;
		this.rayTracerSettings = rayTracerSettings;
		this.progressBar = progressBar;
		this.writableImage = writableImage;
	}

	/**
	 * Methode affichant la toolbox.
	 */
	public void execute() {

		Stage stage = new Stage();
		VBox mainVBox = new VBox();
		Scene scene = new Scene(mainVBox);
//		JMetro jMetro = new JMetro(Style.DARK);
//		jMetro.setScene(scene);
		
        scene.getStylesheets().add(ChooseRenderSettingsWindow.class.getResource("../styles/windows.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ToolBox");

        CheckBox statOnOffCheckBox = new CheckBox("Affichage des stats");
        
        Label progressLabel = new Label("Avancement du rendu de l'image");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        GridPane slidersPane = new ToolboxSliders(rayTracerSettings);
        GridPane checkboxesPane = new ToolboxCheckboxes(rayTracerSettings);
        
//        slidersPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
//        checkboxesPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        
//        mainVBox.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        mainVBox.getChildren().addAll(statOnOffCheckBox, 
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
