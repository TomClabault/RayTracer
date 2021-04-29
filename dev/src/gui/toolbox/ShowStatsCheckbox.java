package gui.toolbox;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

public class ShowStatsCheckbox extends CheckBox 
{
	private Pane statsPane;
	
	public ShowStatsCheckbox(String text, Pane statPane)
	{
		super(text);
		
		this.selectedProperty().addListener(this::checkboxCallback);
		this.setSelected(true);
		
		this.statsPane = statPane;
	}
	
	public void checkboxCallback(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		statsPane.setVisible(newValue);
	}
}
