package gui.toolbox;

import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimpleRenderToolbox 
{
	public SimpleRenderToolbox(WritableImage writableImage, Pane statsPane)
	{
		Stage saveRenderWindowStage = new Stage();
		saveRenderWindowStage.setTitle("Toolbox");
		saveRenderWindowStage.setResizable(false);
		
		GridPane mainPane = new GridPane();
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setPrefSize(192, 96);
		mainPane.setVgap(8);
		
		Label saveRenderLabel = new Label("Sauvegarde du rendu:");
		Button saveRenderButton = new ToolboxSaveButton("Sauvegarder le rendu", saveRenderWindowStage, writableImage);
		
		CheckBox showStatsCheckbox = new ShowStatsCheckbox("Afficher les stats", statsPane);
		
		mainPane.add(saveRenderLabel, 0, 0);
		mainPane.add(saveRenderButton, 0, 1);
		mainPane.add(new Separator(), 0, 2);
		mainPane.add(showStatsCheckbox, 0, 3);
		
		Scene windowScene = new Scene(mainPane);

		saveRenderWindowStage.setScene(windowScene);
		saveRenderWindowStage.show();
	}
}
