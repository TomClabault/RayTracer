package gui.windows;

import gui.MainApp;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RenderWindow 
{
	private PixelWriter pixelWriter;
	private WritableImage writableImage;

	private Label rayTracerStatsLabel;
	
	private Pane statsPane; 
	
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
		GridPane statsPane = new GridPane();
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		RowConstraints row1 = new RowConstraints();
		RowConstraints row2 = new RowConstraints();
		column1.setPercentWidth(50);
		column2.setPercentWidth(50);
		row1.setPercentHeight(50);
		row2.setPercentHeight(50);
		
		statsPane.getColumnConstraints().addAll(column1, column2);
		statsPane.getRowConstraints().addAll(row1, row2);
		statsPane.setAlignment(Pos.TOP_RIGHT);
		
		this.rayTracerStatsLabel = new Label("1");
		this.rayTracerStatsLabel.setAlignment(Pos.TOP_RIGHT);
		statsPane.add(new Label("0"), 0, 0);
		statsPane.add(new Label("2"), 0, 1);
		statsPane.add(new Label("3"), 1, 1);
		statsPane.add(rayTracerStatsLabel, 1, 0);
		
		
		
		panes.getChildren().add(mainPane);
		panes.getChildren().add(statsPane);
		Scene renderScene = new Scene(panes);
		
		stage.setScene(renderScene);
		stage.setMaximized(MainApp.FULLSCREEN_MODE);
		stage.show();
	}
	
	public PixelWriter getPixelWriter()
	{
		return this.pixelWriter;
	}
	
	public Pane getStatsPane()
	{
		return this.statsPane;
	}
	
	public WritableImage getWritableImage()
	{
		return this.writableImage;
	}
}
