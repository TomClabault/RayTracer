package gui.windows;

import gui.MainApp;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RenderWindow 
{
	private PixelWriter pixelWriter;
	
	private WritableImage writableImage;
	
	public RenderWindow(Stage stage)
	{
		this.writableImage = new WritableImage(MainApp.WIDTH, MainApp.HEIGHT);
		this.pixelWriter = this.writableImage.getPixelWriter();
		
		ImageView imageView = new ImageView(this.writableImage);
		if(MainApp.FULLSCREEN_MODE)
		{
			Rectangle2D screenDimensions = Screen.getPrimary().getVisualBounds();
			imageView.setFitWidth(screenDimensions.getWidth());
			imageView.setFitHeight(screenDimensions.getHeight());
		}
		
		Pane mainPane = new Pane(imageView);
		
		Scene renderScene = new Scene(mainPane);
		
		stage.setScene(renderScene);
		stage.setMaximized(MainApp.FULLSCREEN_MODE);
		stage.show();
	}
	
	public PixelWriter getPixelWriter()
	{
		return this.pixelWriter;
	}
	
	public WritableImage getWritableImage()
	{
		return this.writableImage;
	}
}
