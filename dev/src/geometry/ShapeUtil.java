package geometry;

import materials.Material;

public abstract class ShapeUtil extends ShapeTriangle
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
	 * Permet de redéfinir le matériau de l'objet
	 * 
	 * @param material Le nouveau matériau de l'objet
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}
}
