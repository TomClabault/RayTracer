package raytracer.tests;

import java.io.File;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import raytracer.geometry.ArbitraryTriangleShape;
import raytracer.gui.MainUtil;
import raytracer.materials.MatteMaterial;
import raytracer.maths.Vector;
import raytracer.parsers.plyParser.PlyParser;

public class PlyParserTest extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	public void start(Stage stage) 
	{
		PlyParser plyParser = new PlyParser(new MatteMaterial(Color.rgb(128, 0, 0)), 1, new Vector(0, 0, 0));
		
		File plyTestFile = MainUtil.chooseFile(stage, "PLY", "*.ply");
		
		ArbitraryTriangleShape shape = plyParser.parsePly(plyTestFile);
		
		System.out.println(shape);
	}
}
