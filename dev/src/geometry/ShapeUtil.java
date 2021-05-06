package geometry;

import materials.Material;
import maths.Point;

public abstract class ShapeUtil
{
	protected Material material;
	
	/**
	 * Permet d'obtenir le materiau de l'objet
	 * 
	 * @return Retourne le materiau actuel de l'objet
	 */
	public Material getMaterial()
	{
		return this.material;
	}
	
	/**
	 * {@link geometry.Shape#getUVCoords}
	 */
	public abstract Point getUVCoords(Point point);
	
	/**
	 * Permet de redefinir le materiau de l'objet
	 * 
	 * @param material Le nouveau materiau de l'objet
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}
}
