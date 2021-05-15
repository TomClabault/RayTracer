package materials.observer;

import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.paint.Color;
import materials.Material;
import materials.textures.ProceduralTexture;

public class ObservableConcreteMaterial extends Material implements ObservableMaterial
{
	private ArrayList<MaterialObserver> observers;
	
	/**
	 * Crée un materiau noir.
	 */
	public ObservableConcreteMaterial()
	{
		this(Color.BLACK, 0, 0, 0, 0, 0, false, 0, 0, null);
	}
	
	/**
	 * Cree un materiau de A a Z
	 * 
	 * @param color Couleur du materiau
	 * @param ambientCoeff Reel entre 0 et 1. Coefficient representant la quantite de lumiere ambiante que le materiau reflechi
	 * @param diffuseCoeff Reel entre 0 et 1. Coefficient modulant la diffusion de lumiere du materiau
	 * @param reflectiveCoeff Reel entre 0 et 1. Coefficient qui gere la reflectivite du materiau. Plus ce coefficient se rapproche de 1 et plus le materiau se rapprochera d'un materiau "mirroir"
	 * @param specularCoeff Reel entre 0 et 1. Coefficient permettant de jouer sur l'intensite des tâches speculaires du materiau. Plus ce coefficient se rapproche de 1 plus les tâches speculaires seront marquees. A 0, le materiau n'est pas speculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches speculaires du materiau. Plus ce nombre est grand plus les tâches seront petites
	 * @param isTransparent True pour indiquer que le materiau est transparent, false pour indiquer qu'il n'est pas transparent
	 * @param refractionIndex Indice de refraction du materiau. Doit etre 0 pour indiquer que le materiau n'est pas refractif
	 * @param roughness Le coefficient de dispersion des rayons de lumiere a l'impact du materiau. Responsable de reflexions floues notamment
	 */
	public ObservableConcreteMaterial(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double roughness)
	{
		super(color, ambientCoeff, diffuseCoeff, reflectiveCoeff, specularCoeff, shininess, isTransparent, refractionIndex, roughness, null);
		
		this.observers = new ArrayList<>();
	}
	
	/**
	 * Cree un materiau de A a Z
	 * 
	 * @param color Couleur du materiau
	 * @param ambientCoeff Reel entre 0 et 1. Coefficient representant la quantite de lumiere ambiante que le materiau reflechi
	 * @param diffuseCoeff Reel entre 0 et 1. Coefficient modulant la diffusion de lumiere du materiau
	 * @param reflectiveCoeff Reel entre 0 et 1. Coefficient qui gere la reflectivite du materiau. Plus ce coefficient se rapproche de 1 et plus le materiau se rapprochera d'un materiau "mirroir"
	 * @param specularCoeff Reel entre 0 et 1. Coefficient permettant de jouer sur l'intensite des tâches speculaires du materiau. Plus ce coefficient se rapproche de 1 plus les tâches speculaires seront marquees. A 0, le materiau n'est pas speculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches speculaires du materiau. Plus ce nombre est grand plus les tâches seront petites
	 * @param isTransparent True pour indiquer que le materiau est transparent, false pour indiquer qu'il n'est pas transparent
	 * @param refractionIndex Indice de refraction du materiau. Doit etre 0 pour indiquer que le materiau n'est pas refractif
	 * @param roughness Le coefficient de dispersion des rayons de lumiere a l'impact du materiau. Responsable de reflexions floues notamment
	 * @param proceduralTexture La texture procedurale de l'objet
	 */
	public ObservableConcreteMaterial(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double roughness, ProceduralTexture proceduralTexture)
	{
		super(color, ambientCoeff, diffuseCoeff, reflectiveCoeff, specularCoeff, shininess, isTransparent, refractionIndex, roughness, proceduralTexture);
		
		this.observers = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyIn(Material materialToCopy) 
	{
		super.copyIn(materialToCopy);
		
		this.notifyListeners();
	}
	
	/**
	 * @return L'observableMaterial sous forme d'une instance de Material 
	 */
	public Material getMaterial()
	{
		return new Material(color, ambientCoeff, diffuseCoeff, reflectiveCoeff, specularCoeff, shininess, isTransparent, refractionIndex, roughness, proceduralTexture);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor(Color color) 
	{
		super.setColor(color);
		
		this.notifyListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAmbientCoeff(double ambientCoeff)
	{
		super.setAmbientCoeff(ambientCoeff);
		
		this.notifyListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDiffuseCoeff(double diffuseCoeff) 
	{
		super.setDiffuseCoeff(diffuseCoeff);
		
		this.notifyListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProceduralTexture(ProceduralTexture proceduralTexture) 
	{
		super.setProceduralTexture(proceduralTexture);
		
		this.notifyListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setReflectiveCoeff(double reflectiveCoeff) 
	{
		super.setReflectiveCoeff(reflectiveCoeff);
		
		this.notifyListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRefractionIndex(double refractionIndex) 
	{
		super.setRefractionIndex(refractionIndex);
		
		this.notifyListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setRoughness(double roughness) 
	{
		super.setRoughness(roughness);
		
		this.notifyListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSpecularCoeff(double specularCoeff) 
	{
		super.setSpecularCoeff(specularCoeff);
		
		this.notifyListeners();
	}

	public void setShininess(int shininess) 
	{
		super.setShininess(shininess);
		
		this.notifyListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTransparent(boolean isTransparent) 
	{
		super.setTransparent(isTransparent);
		
		this.notifyListeners();
	}
	
	@Override
	public void addListener(MaterialObserver listener) 
	{
		this.observers.add(listener);
	}

	@Override
	public void removeListener(MaterialObserver listener) 
	{
		this.observers.remove(listener);
	}
	
	private void notifyListeners()
	{
		for(MaterialObserver observer : this.observers)
			observer.materialUpdated(this);
	}
}
