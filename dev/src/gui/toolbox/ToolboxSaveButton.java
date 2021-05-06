package gui.toolbox;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ToolboxSaveButton extends Button
{
	private	Stage windowStage;
	private WritableImage writableImage;
	
	public ToolboxSaveButton(String text, Stage windowStage, WritableImage writableImage)
	{
		super(text);
		
		this.windowStage = windowStage;
		this.writableImage = writableImage;
		
		this.setOnAction(this::buttonCallback);
	}
	
	public void buttonCallback(ActionEvent event)
	{
       	 FileChooser fileChooser = new FileChooser();
       	 fileChooser.setTitle("Chemin d'enregistrement du rendu");
       	 ExtensionFilter filter = new ExtensionFilter("Image", "*.png");
       	 fileChooser.getExtensionFilters().add(filter);
       	 File file = fileChooser.showSaveDialog(this.windowStage);
       	 if (file != null) 
       	 {
       		 try 
       		 {
       			 util.ImageUtil.writeWritableImageToDisk(writableImage, file);
       			 System.out.println("Image sauvegardee en : " + file);
       		 } 
       		 catch (IOException e) 
       		 {
					System.out.println("Impossible de sauvegarder l'image : " + e.getMessage());
       		 }

       	 } 
       	 else 
       	 {
       		 System.out.println("Aucun dossier n'a ete selectionne.");
       	 }
	}
}
