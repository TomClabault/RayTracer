package gui.windows;

import gui.MainApp;
import gui.panes.StatsPane;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RenderWindow 
{
	private PixelWriter pixelWriter;
	private WritableImage writableImage;

	private StatsPane statsPane; 
	
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
		
		StackPane panes = new StackPane();
		
		
		Pane mainPane = new Pane(imageView);
		this.statsPane = new StatsPane();
		
		panes.getChildren().add(mainPane);
		panes.getChildren().add(this.statsPane);
		Scene renderScene = new Scene(panes);
		
		stage.setScene(renderScene);
		stage.setMaximized(MainApp.FULLSCREEN_MODE);
		stage.show();
	}
	
	public PixelWriter getPixelWriter()
	{
		return this.pixelWriter;
	}
	
	public StatsPane getStatsPane()
	{
		return this.statsPane;
	}
	
	public WritableImage getWritableImage()
	{
		return this.writableImage;
	}
}
