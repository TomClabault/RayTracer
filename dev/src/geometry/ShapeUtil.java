package geometry;

import javafx.scene.paint.Color;
import materials.Material;
import maths.Point;

public abstract class ShapeUtil
{
	protected Material material;
	
	/*
	 * Permet d'obtenir le matériau de l'objet
	 * 
	 * @return Retourne le matériau actuel de l'objet
	 */
	public Material getMaterial()
	{
		return this.material;
	}
	
	/*
	 * @link {geometry.Shape#getUVCoords}
	 */
	public abstract Point getUVCoords(Point point);
	
	/*
	 * Permet de redéfinir le matériau de l'objet
	 * 
	 * @param material Le nouveau matériau de l'objet
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}
}
