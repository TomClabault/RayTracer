package scene;

import java.util.ArrayList;

import geometry.Shape;
import scene.lights.Light;

/*
 * Permet de représenter une scène de rendu contenant une caméra, une lumière et une liste de formes
 */
public class MyScene 
{
	Camera camera;
	Light light;
	
	ArrayList<Shape> shapes;
	
	double ambientLightIntensity;
	
	/*
	 * Crée la scène à partir d'une caméra, d'une lumière et d'une liste de forme
	 * 
	 * @param camera La camera de la scène à travers laquelle le rendu sera fait
	 * @param light La lumière permettant d'illuminer la scène
	 * @param shapes Liste de forme qui seront rendues
	 */
	public MyScene(Camera camera, Light light, ArrayList<Shape> shapes, double ambientLightIntensity)
	{
		this.camera = camera;
		this.light = light;
		this.shapes = shapes;
		this.ambientLightIntensity = ambientLightIntensity;
	}
	
	/*
	 * Permet d'obtenir l'intensité de la lumière ambiante de la scène
	 * 
	 * @return L'intensité de la lumière ambiante de la scène, un réel entre 0 et 1
	 */
	public double getAmbientLightIntensity()
	{
		return this.ambientLightIntensity;
	}
	
	/*
	 * Retourne la caméra de la scène
	 * 
	 * @return La caméra de la scène
	 */
	public Camera getCamera()
	{
		return this.camera;
	}
	
	/*
	 * Retourne la source de lumière de la scène
	 * 
	 * @return La source de lumière de la scène
	 */
	public Light getLight()
	{
		return this.light;
	}
	
	/*
	 * Permet d'obtenir la liste des formes de la scène
	 * 
	 * @return Une ArrayList<Shape> contenant toutes les formes de la scène
	 */
	public ArrayList<Shape> getSceneObjects()
	{
		return this.shapes;
	}
}
