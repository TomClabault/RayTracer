package gui.panes;

import java.net.URL;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class StatsPane extends GridPane
{
	private Label rayTracerStatsLabel;
	private Label renderTimeLabel;
	
	public StatsPane()
	{
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		RowConstraints row1 = new RowConstraints();
		RowConstraints row2 = new RowConstraints();
		column1.setPercentWidth(50);
		column2.setPercentWidth(50);
		row1.setPercentHeight(50);
		row2.setPercentHeight(50);
		
		this.getColumnConstraints().addAll(column1, column2);
		this.getRowConstraints().addAll(row1, row2);
		
		this.rayTracerStatsLabel = new Label("");
		this.renderTimeLabel = new Label("");
		
		this.add(rayTracerStatsLabel, 1, 0);
		this.add(renderTimeLabel, 0, 0);
		
		GridPane.setHalignment(rayTracerStatsLabel, HPos.RIGHT);
		GridPane.setValignment(rayTracerStatsLabel, VPos.TOP);
		GridPane.setValignment(renderTimeLabel, VPos.TOP);		
		
		URL cssHUDFile = StatsPane.class.getResource("../styles/HUDText.css");
		if(cssHUDFile == null)
		{
			System.out.println("Impossible de trouver le style HUDText.css");
			
			Platform.exit();
			System.exit(0);
		}
		
		this.getStylesheets().add(cssHUDFile.toExternalForm());
	}
	
	public Label getRayTracerStatsLabel()
	{
		return this.rayTracerStatsLabel;
	}
	
	public Label getRenderTimeLabel()
	{
		return this.renderTimeLabel;
	}
}
