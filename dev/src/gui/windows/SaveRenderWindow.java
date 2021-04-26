package gui.windows;

import gui.toolbox.ToolboxSaveButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SaveRenderWindow 
{
	public SaveRenderWindow(WritableImage writableImage)
	{
		Stage saveRenderWindowStage = new Stage();
		saveRenderWindowStage.setTitle("Sauvegarde du rendu");
		saveRenderWindowStage.setResizable(false);
		
		StackPane mainPane = new StackPane();
		mainPane.setPrefSize(240, 75);
		mainPane.setAlignment(Pos.CENTER);
		
		Button saveRenderButton = new ToolboxSaveButton("Sauvegarder le rendu", saveRenderWindowStage, writableImage);
		saveRenderButton.setAlignment(Pos.CENTER);
		
		mainPane.getChildren().add(saveRenderButton);
		
		Scene windowScene = new Scene(mainPane);

		saveRenderWindowStage.setScene(windowScene);
		saveRenderWindowStage.show();
	}
}
