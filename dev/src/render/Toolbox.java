package render;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scene.RayTracingScene;

public class Toolbox {
	
	public RayTracingScene rts;
	
	public Toolbox(RayTracingScene rts) {
		this.rts = rts;
	}

	public RayTracingScene getRts() {
		return rts;
	}

	public void setRts(RayTracingScene rts) {
		this.rts = rts;
	}
	
	public void execute() {
		Stage stage = new Stage();
		VBox root = new VBox();
		Scene scene = new Scene(root);
        //scene.getStylesheets().add(SetSizeWindow.class.getResource("style/setSizeWindow.css").toExternalForm()); // TODO ajouter css
        stage.setScene(scene);
        stage.setTitle("ToolBox");
        
        TextField saveTextField = new TextField("Chemin d'enregistrement du rendu");
        Button saveButton = new Button("Save");
        Label resolutionLabel = new Label("Résolution de la scène");
        HBox resolutionHbox = new HBox();
        
        
		
		
	}
}
