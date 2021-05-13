package gui.materialChooser;


import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import jfxtras.styles.jmetro.JMetroStyleClass;
import materials.observer.ObservableConcreteMaterial;
import maths.ColorOperations;

//TODO(tom) bug de la hue qui revient à 0 quand on déplace la saturation à 0 à la souris / la brightness à 100 à la souris. Problème de valeurs
//maximales qui sont dépassées et donc le mouse handler set les valeurs comme de la merde ?
/**
 * ColorRect,  colorBar, colorRectIndicator de:
 * https://stackoverflow.com/questions/27171885/display-custom-color-dialog-directly-javafx-colorpicker
 */
public class MaterialChooserColorPicker extends VBox
{
    private final ObjectProperty<Color> currentColorProperty = 
        new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> customColorProperty = 
        new SimpleObjectProperty<>(Color.RED);

    private Pane colorRect;
    private final Pane colorBar;
    private final Pane colorRectOverlayOne;
    private final Pane colorRectOverlayTwo;
    private Region colorRectIndicator;
    private final Region colorBarIndicator;
    private Pane newColorRect;

    private GridPane colorControlsPane;
    
    private ToggleButton rgbButton;
    private ToggleButton hsbButton;
    private ToggleButton webButton;
    
    private final DoubleProperty hueComponent = new SimpleDoubleProperty(-1);
    private final DoubleProperty satComponent = new SimpleDoubleProperty(-1);
    private final DoubleProperty brightComponent = new SimpleDoubleProperty(-1);

    private final IntegerProperty redComponent = new SimpleIntegerProperty(255) 
    {
    	@Override
    	protected void invalidated() {updateColor();}
	};
	
    private final IntegerProperty greenComponent = new SimpleIntegerProperty(255) 
    {
    	@Override
    	protected void invalidated() {updateColor();}
	};
	
    private final IntegerProperty blueComponent = new SimpleIntegerProperty(255)
    {
    	@Override
    	protected void invalidated() {updateColor();}
	};
    
    private DoubleProperty alphaComponent = new SimpleDoubleProperty(100) {
        @Override protected void invalidated() {
            setCustomColor(new Color(getCustomColor().getRed(), getCustomColor().getGreen(), 
                    getCustomColor().getBlue(), clamp(alphaComponent.get() / 100)));
        }
    };

    private ObservableConcreteMaterial material;
    
    public MaterialChooserColorPicker(ObservableConcreteMaterial material) 
    {
    	this.material = material;
        this.getStyleClass().add("my-custom-color");

        VBox colorPickerBox = new VBox();

        Label materialColorLabel = new Label("Material color : ");
        materialColorLabel.setMaxWidth(Double.MAX_VALUE);
        materialColorLabel.setAlignment(Pos.CENTER);
        
        colorPickerBox.getStyleClass().add("color-rect-pane");
        customColorProperty().addListener((ov, t, t1) -> colorChanged());

        colorRectIndicator = new Region();
        colorRectIndicator.setId("color-rect-indicator");
        colorRectIndicator.setManaged(false);
        colorRectIndicator.setMouseTransparent(true);
        colorRectIndicator.setCache(true);

        final Pane colorRectOpacityContainer = new StackPane();

        colorRect = new StackPane();
        colorRect.getStyleClass().addAll("color-rect", "transparent-pattern");

        Pane colorRectHue = new Pane();
        colorRectHue.backgroundProperty().bind(new ObjectBinding<Background>() {

            {
                bind(hueComponent);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(
                        Color.hsb(hueComponent.getValue(), 1.0, 1.0), 
                        CornerRadii.EMPTY, Insets.EMPTY));

            }
        });            

        colorRectOverlayOne = new Pane();
        colorRectOverlayOne.getStyleClass().add("color-rect");
        colorRectOverlayOne.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, 
                new Stop(0, Color.rgb(255, 255, 255, 1)), 
                new Stop(1, Color.rgb(255, 255, 255, 0))), 
                CornerRadii.EMPTY, Insets.EMPTY)));

        EventHandler<MouseEvent> rectMouseHandler = event -> 
        {
            final double x = event.getX();
            final double y = event.getY();
            
            satComponent.set(clamp(x / colorRect.getWidth()) * 100);
            brightComponent.set(100 - (clamp(y / colorRect.getHeight()) * 100));
            
            //brightComponent.set(0);
            
            updateColor();
        };

        colorRectOverlayTwo = new Pane();
        colorRectOverlayTwo.getStyleClass().addAll("color-rect");
        colorRectOverlayTwo.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, 
                new Stop(0, Color.rgb(0, 0, 0, 0)), new Stop(1, Color.rgb(0, 0, 0, 1))), 
                CornerRadii.EMPTY, Insets.EMPTY)));
        colorRectOverlayTwo.setOnMouseDragged(rectMouseHandler);
        colorRectOverlayTwo.setOnMousePressed(rectMouseHandler);

        Pane colorRectBlackBorder = new Pane();
        colorRectBlackBorder.setMouseTransparent(true);
        colorRectBlackBorder.getStyleClass().addAll("color-rect", "color-rect-border");

        colorBar = new Pane();
        colorBar.getStyleClass().add("color-bar");
        colorBar.setBackground(new Background(new BackgroundFill(createHueGradient(), 
                CornerRadii.EMPTY, Insets.EMPTY)));

        colorBarIndicator = new Region();
        colorBarIndicator.setId("color-bar-indicator");
        colorBarIndicator.setMouseTransparent(true);
        colorBarIndicator.setCache(true);

        colorRectIndicator.layoutXProperty().bind(
            satComponent.divide(100).multiply(colorRect.widthProperty()));
        colorRectIndicator.layoutYProperty().bind(
            Bindings.subtract(1, brightComponent.divide(100)).multiply(colorRect.heightProperty()));
        colorBarIndicator.layoutXProperty().bind(
            hueComponent.divide(360).multiply(colorBar.widthProperty()));
        colorRectOpacityContainer.opacityProperty().bind(alphaComponent.divide(100));

        EventHandler<MouseEvent> barMouseHandler = event -> {
            final double x = event.getX();
            hueComponent.set(clamp(x / colorRect.getWidth()) * 360);
            updateColor();
        };

        colorBar.setOnMouseDragged(barMouseHandler);
        colorBar.setOnMousePressed(barMouseHandler);

        newColorRect = new Pane();
        newColorRect.getStyleClass().add("color-new-rect");
        newColorRect.setId("new-color");
        newColorRect.backgroundProperty().bind(new ObjectBinding<Background>() {
            {
                bind(customColorProperty);
            }
            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(customColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY));
            }
        });

        colorBar.getChildren().setAll(colorBarIndicator);
        colorRectOpacityContainer.getChildren().setAll(colorRectHue, colorRectOverlayOne, colorRectOverlayTwo);
        colorRect.getChildren().setAll(colorRectOpacityContainer, colorRectBlackBorder, colorRectIndicator);
        VBox.setVgrow(colorRect, Priority.SOMETIMES);
        
        
        colorPickerBox.getChildren().addAll(materialColorLabel, colorBar, colorRect, newColorRect);
        
        this.colorControlsPane = buildColorControls();
        this.getChildren().addAll(colorPickerBox, this.colorControlsPane);
        this.setMaxWidth(Double.MAX_VALUE);
        
        if (currentColorProperty.get() == null) {
            currentColorProperty.set(Color.TRANSPARENT);
        }
        
        this.borderProperty().bind(new ObjectBinding<Border>() 
        {
        	{
        		bind(customColorProperty);
        	}
        	
        	@Override
        	protected Border computeValue()
        	{
        		return new Border(new BorderStroke(customColorProperty.get(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY));
        	}
		});
        //this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        this.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        
        updateValues();
    }

    private GridPane buildColorControls()
    {
    	GridPane buttonsPane = new GridPane();
    	buttonsPane.setVgap(10);
    	
    	ToggleGroup buttonsGroup = new ToggleGroup();
    	
    	this.rgbButton = new ToggleButton("RGB");
    	this.hsbButton = new ToggleButton("HSB");
    	this.webButton = new ToggleButton("Web");
    	rgbButton.setToggleGroup(buttonsGroup);
    	rgbButton.setOnAction(this::createColorControlsCallback);
    	hsbButton.setOnAction(this::createColorControlsCallback);
    	hsbButton.setToggleGroup(buttonsGroup);
    	hsbButton.setSelected(true);
    	webButton.setOnAction(this::createColorControlsCallback);
    	webButton.setToggleGroup(buttonsGroup);
    	
    	
    	
    	HBox buttonsHBox = new HBox();
    	HBox.setMargin(hsbButton, new Insets(0, 5, 0, 5));
    	GridPane.setHalignment(buttonsHBox, HPos.CENTER);
    	
    	buttonsHBox.setPadding(new Insets(4));
    	buttonsHBox.setAlignment(Pos.CENTER);
    	buttonsHBox.getChildren().addAll(rgbButton, new Label(" -"), hsbButton, new Label("- "), webButton);
    	
    	buttonsPane.add(buttonsHBox, 0, 0);
    	buttonsPane.getChildren().add(createColorControls(1));
    	
    	return buttonsPane;
    }
    
    private void createColorControlsCallback(ActionEvent event)
    {
    	//Supprime les boutons existants car on va les remplacer par les bons 
    	//(rgb, hsb ou web selon sur quoi on a cliqué)
   		this.colorControlsPane.getChildren().remove(this.colorControlsPane.getChildren().size() - 1);
    	
    	if(event.getSource().equals(this.rgbButton))
    		this.colorControlsPane.getChildren().addAll(createColorControls(0));
    	else if(event.getSource().equals(this.hsbButton))
    		this.colorControlsPane.getChildren().addAll(createColorControls(1));
    	else if(event.getSource().equals(this.webButton))
    		this.colorControlsPane.getChildren().addAll(createColorControls(2));
    }
    
    private GridPane createColorControls(int index)
    {
    	String prompts[][] =
    	{
    		{"red", "green", "blue"},	
    		{"h", "s", "b"},
    		{"#FFFFFF", "", ""},
    	};
    	
    	String labels[][] = 
    	{
    		{"Red :", "Green :", "Blue :"},
    		{"Hue :", "Saturation :", "Brightness :"},
    		{"Hex", "", ""}
    	};
    	
    	String units[][] = 
    	{
    		{"", "", ""},
    		{"\u00B0", "%", "%"},
    		{"", "", ""}
    	};
    	
    	GridPane nodePane = new GridPane();
    	
    	
    	
    	Label label1 = new Label(labels[index][0]);
    	Label label2 = new Label(labels[index][1]);
    	Label label3 = new Label(labels[index][2]);
    	
    	Label unit1 = new Label(units[index][0]);
    	Label unit2 = new Label(units[index][1]);
    	Label unit3 = new Label(units[index][2]);
    	
    	Slider slider1 = new Slider();
    	Slider slider2 = new Slider();
    	Slider slider3 = new Slider();
    	
    	TextField input1 = new TextField();
    	TextField input2 = new TextField();
    	TextField input3 = new TextField();
    	input1.setPrefWidth(50);
    	input1.setPromptText(prompts[index][0]);
    	input2.setPrefWidth(50);
    	input2.setPromptText(prompts[index][1]);
    	input3.setPrefWidth(50);
    	input3.setPromptText(prompts[index][2]);
    	
    	StringConverter<Number> converter = new NumberStringConverter();
    	switch(index)
    	{
    	case 0:
    		input1.textProperty().bindBidirectional(this.redComponent, converter);
    		input2.textProperty().bindBidirectional(this.greenComponent, converter);
    		input3.textProperty().bindBidirectional(this.blueComponent, converter);
    		
    		slider1.valueProperty().bindBidirectional(this.redComponent);
    		slider2.valueProperty().bindBidirectional(this.greenComponent);
    		slider3.valueProperty().bindBidirectional(this.blueComponent);
    		
    		slider1.setMax(255);
    		slider2.setMax(255);
    		slider3.setMax(255);
    		break;
    		
    	case 1:
    		//TODO (tom) la hue retourne à zéro quand on positionne le curseur du color picker tout à gauche ?
    		input1.textProperty().bindBidirectional(this.hueComponent, converter);
    		input2.textProperty().bindBidirectional(this.satComponent, converter);
    		input3.textProperty().bindBidirectional(this.brightComponent, converter);
    		
    		slider1.valueProperty().bindBidirectional(this.hueComponent);
    		slider2.valueProperty().bindBidirectional(this.satComponent);
    		slider3.valueProperty().bindBidirectional(this.brightComponent);
    		
    		slider1.setMax(360);
    		slider2.setMax(100);
    		slider3.setMax(100);
    		break;
    	}
    	
    	Insets labelInsets = new Insets(0, 10, 0, 0);
    	GridPane.setMargin(label1, labelInsets);
    	GridPane.setMargin(label2, labelInsets);
    	GridPane.setMargin(label3, labelInsets);
    	GridPane.setConstraints(label1, 0, 0);
    	GridPane.setConstraints(label2, 0, 1);
    	GridPane.setConstraints(label3, 0, 2);

    	GridPane.setConstraints(slider1, 1, 0, 3, 1);
    	GridPane.setConstraints(slider2, 1, 1, 3, 1);
    	GridPane.setConstraints(slider3, 1, 2, 3, 1);
    	
    	GridPane.setConstraints(input1, 4, 0);
    	GridPane.setConstraints(input2, 4, 1);
    	GridPane.setConstraints(input3, 4, 2);
    	
    	Insets unitInsets = new Insets(0, 0, 0, 5);
    	GridPane.setMargin(unit1, unitInsets);
    	GridPane.setMargin(unit2, unitInsets);
    	GridPane.setMargin(unit3, unitInsets);
    	GridPane.setConstraints(unit1, 5, 0);
    	GridPane.setConstraints(unit2, 5, 1);
    	GridPane.setConstraints(unit3, 5, 2);
    	
    	
    	
    	nodePane.getChildren().add(label1);
    	if(index >= 0 && index <= 1)//rgb ou hsb --> pas de slider pour le web
    		nodePane.getChildren().add(slider1);
    	
    	nodePane.getChildren().addAll(input1, unit1);
    	
    	if(index >= 0 && index <= 1)//rgb ou hsb
    	{
    		nodePane.getChildren().addAll(label2, label3,
    									  slider2, slider3,
    									  input2, input3, 
    									  unit2, unit3);
    	}
    	
    	GridPane.setConstraints(nodePane, 0, 1);
    	return nodePane;
    }
    
    private void updateValues() 
    {
        hueComponent.set(getCurrentColor().getHue());
        satComponent.set(getCurrentColor().getSaturation()*100);
        brightComponent.set(getCurrentColor().getBrightness()*100);
        alphaComponent.set(getCurrentColor().getOpacity()*100);
        setCustomColor(Color.hsb(hueComponent.get(), clamp(satComponent.get() / 100), 
                clamp(brightComponent.get() / 100), clamp(alphaComponent.get()/100)));
    }

    private void colorChanged() 
    {
        hueComponent.set(getCustomColor().getHue());
        satComponent.set(getCustomColor().getSaturation() * 100);
        brightComponent.set(getCustomColor().getBrightness() * 100);
    }

    private void updateColor() 
    {
        Color newColor = Color.hsb(hueComponent.get(), 
        					 clamp(satComponent.get() / 100), 
        					 clamp(brightComponent.get() / 100),
        					 clamp(alphaComponent.get() / 100));
        
        this.redComponent.set((int)(newColor.getRed() * 255));
        this.greenComponent.set((int)(newColor.getGreen() * 255));
        this.blueComponent.set((int)(newColor.getBlue() * 255));
        
        setCustomColor(newColor);
    }
    
    @Override 
    protected void layoutChildren() {
        super.layoutChildren();            
        colorRectIndicator.autosize();
    }

    private double clamp(double value) {
        return value <= 0 ? 0 : value >= 1 ? 1 : value;
    }

    private LinearGradient createHueGradient() {
        double offset;
        Stop[] stops = new Stop[255];
        for (int x = 0; x < 255; x++) {
            offset = (double)((1.0 / 255) * x);
            int h = (int)((x / 255.0) * 360);
            stops[x] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
        }
        return new LinearGradient(0f, 0f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColorProperty.set(currentColor);
        updateValues();
    }

    private Color getCurrentColor() 
    {
        return currentColorProperty.get();
    }

    private ObjectProperty<Color> customColorProperty() {
        return customColorProperty;
    }

    private void setCustomColor(Color color) 
    {
        customColorProperty.set(color);
        
        this.material.setColor(ColorOperations.sRGBGamma2_2ToLinear(color));
    }

    Color getCustomColor() 
    {
        return customColorProperty.get();
    }
}
