package gui.windows;

import gui.toolbox.ToolboxSaveButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SaveRenderWindow 
{
	public SaveRenderWindow(WritableImage writableImage)
	{
		Stage saveRenderWindowStage = new Stage();
		
		Pane mainPane = new Pane();
		
		Button saveRenderButton = new ToolboxSaveButton("Sauvegarder le rendu", saveRenderWindowStage, writableImage);
		
		mainPane.getChildren().add(saveRenderButton);

		Scene windowScene = new Scene(mainPane);

		saveRenderWindowStage.setScene(windowScene);
		saveRenderWindowStage.show();
	}
}
