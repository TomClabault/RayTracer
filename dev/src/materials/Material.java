package materials;

import javafx.scene.paint.Color;
import materials.textures.*;
import maths.ColorOperations;

/**
 * Classe permettant definir des materiaux a partir des differentes caracteristiques physiques que peut simuler le rayTracer<br>
 * <br>
 * Un materiau est definit par differentes caracteristiques:<br>
 * 
 * <ul>
 	 * <li> Une caracteristique ambiante: Defini le pourcentage de lumiere ambiante que reflechira le materiau. 0% signifie par exemple que l'objet ne sera eclaire que par la lumiere directe de la scene et pas par la luminosite ambiante 	
	 * <li> Une caracteristique diffuse: Plus un materiau est diffus, plus la lumiere sera renvoyee dans "toute les directions", donnant un aspect mat au materiau</li>
	 * <li> Une caracteristique speculaire: Donne de la brillance a l'objet</li>
	 * <li> Une caracteristique de "brillance" (shininess): Plus cette valeur est haute plus les tâches speculaires de l'objet seront petites</li>
	 * <li> Une caracteristique reflective: Plus un materiau est reflechissant plus il se comportera comme un miroir.</li> 
	 * <li> Une caracteristique de transparence: Si oui ou non le materiau est transparent. Un materiau transparent refractera la lumiere et aura donc besoin d'un indice de refraction (decrit ci-dessous) approprie</li>
	 * <li> Un indice de refraction: Caracterise a quel point les rayons de lumiere sont refractes par le materiau</li>
	 * <li> Une durete: Plus un materiau est dur, plus les rayons de lumieres incidents seront disperses a l'impact avec le materiau</li>
 * </ul>  
 */
public class Material 
{	
	private Color color;
	
	private double ambientCoeff;
	private double diffuseCoeff;
	private double reflectiveCoeff;
	private double specularCoeff;
	private int shininess;
	private boolean isTransparent;
	private double refractionIndex;
	private double roughness;
	
	private ProceduralTexture proceduralTexture;//Attribut special qui specifie la texture du materiau. Utilise pour le damier par exemple
	
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
	public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double roughness)
	{
		this.color = color;
		
		this.ambientCoeff = ambientCoeff;
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		this.isTransparent = isTransparent;
		this.refractionIndex = refractionIndex;
		this.roughness = roughness;
		
		this.proceduralTexture = null;//Pas de texture par defaut
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
	public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double roughness, ProceduralTexture proceduralTexture)
	{
		this.color = color;
		
		this.ambientCoeff = ambientCoeff;
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		this.isTransparent = isTransparent;
		this.refractionIndex = refractionIndex;
		this.roughness = roughness;
		
		this.proceduralTexture = proceduralTexture;
	}

	/**
	 * Retourne le couleur du materiau sous la forme d'un objet Color.RGB(r, g, b)
	 * 
	 * @return Color.rgb(r, g, b), la couleur de l'objet au format RGB
	 */
	public Color getColor()
	{
		return this.color;
	}
	
	/**
	 * Permet de recuperer le coefficient de reflexion de la lumiere ambiante du materiau
	 * 
	 * @return Retourne un reel entre 0 et 1 representant le coefficient dit 'ambiant' du materiau
	 */
	public double getAmbientCoeff()
	{
		return this.ambientCoeff;
	}
	
	/**
	 * Permet d'obtenir la composante diffuse du materiau
	 * Plus un materiau est diffus et plus il sera mat. La craie est par exemple tres diffuse alors que l'aluminium ne l'est pas
	 * 
	 * @return Retourne un reel entre 0 et 1 representant le pourcentage de diffusion de la lumiere par l'objet
	 */
	public double getDiffuseCoeff()
	{
		return this.diffuseCoeff;
	}
	
	/**
	 * Permet de savoir si un materiau est transparent et refracte la lumiere ou non
	 * 
	 * @return Retourne true si le materiau est transparent, false sinon
	 */
	public boolean getIsTransparent()
	{
		return this.isTransparent;
	}
	
	/**
	 * Permet d'obtenir la proportion de lumiere que reflechit le materiau.
	 * Un coefficient reflectif de 1 fera du materiau un miroir tandis qu'a 0, le materiau ne reflechira rien de ce qui l'entour
	 * 
	 * @return Retourne la proportion de lumiere que refelchit l'objet. Reel entre 0 et 1
	 */
	public double getReflectiveCoeff()
	{
		return this.reflectiveCoeff;
	}
	
	/**
	 * Permet d'obtenir l'indice de refraction du materiau
	 * 
	 * @return Un reel representant l'indice de refraction du materiau. Si le materiau n'est pas transparent, retournera 0
	 */
	public double getRefractionIndex()
	{
		return this.refractionIndex;
	}
	
	public double getRoughness() 
	{
		return roughness;
	}
	
	/**
	 * Permet d'obtenir la brillance du materiau
	 * Cette valeur influe sur les tâches specualaires de l'objet. Une shininess haute entraînera des tâches plus petite alors qu'une faible shininess produira des tâches de specularite larges
	 * 
	 * @return Retourne un entier positif representant la brillance du materiau. 
	 */
	public int getShininess()
	{
		return this.shininess;
	}
	
	/**
	 * Permet d'obtenir le coefficient de specularite du materiau.
	 * Ce coefficient definit l'intensite des tâches de specularite de l'objet.
	 * Avec une valeur de 0, le materiau n'exhibera aucune tâches speculaires.
	 * Pour une valeur de 0.5 les tâches seront moins marquees et visibles qu'avec une valeur de 1 
	 * 
	 * @return Un reel entre 0 et 1 representant le coefficient de specularite de l'objet
	 */
	public double getSpecularCoeff()
	{
		return this.specularCoeff;
	}
	
	/**
	 * Permet d'obtenir la texture procedurale de l'objet s'il en a une
	 * 
	 * @return L'instance de la texture procedurale de l'objet. Retourne null si l'objet n'a pas de texture procedurale.
	 */
	public ProceduralTexture getProceduralTexture()
	{
		return this.proceduralTexture;
	}
	
	/**
	 * Permet de determiner si le materiau a une texture procedurale appliquee ou non
	 * 
	 *  @return Retourne true si le materiau a une texture procedurale appliquee. false sinon
	 */
	public boolean hasProceduralTexture()
	{
		return !(this.proceduralTexture == null);
	}
	
	/**
	 * Permet de definir la couleur du materiau
	 * 
	 * @param color La nouvelle couleur du materiau
	 */
	public void setColor(Color color) 
	{
		this.color = color;
	}

	/**
	 * Permet de defenir un coefficient de reflexion de la lumiere ambiante pour le materiau
	 * 
	 * @param ambientCoeff Le nouveau coefficient ambiant du materiau
	 */
	public void setAmbientCoeff(double ambientCoeff)
	{
		this.ambientCoeff = ambientCoeff;
	}
	
	/**
	 * Permet de definir le coefficient de diffusion du materiau
	 * 
	 * @param diffuseCoeff Le nouveau coefficient de diffusion du materiau
	 */
	public void setDiffuseCoeff(double diffuseCoeff) 
	{
		this.diffuseCoeff = diffuseCoeff;
	}

	public void setProceduralTexture(ProceduralTexture proceduralTexture) 
	{
		this.proceduralTexture = proceduralTexture;
	}
	
	public void setReflectiveCoeff(double reflectiveCoeff) 
	{
		this.reflectiveCoeff = reflectiveCoeff;
	}

	public void setRefractionIndex(double refractionIndex) 
	{
		this.refractionIndex = refractionIndex;
	}
	
	public void setRoughness(double roughness) 
	{
		this.roughness = roughness;
	}
	
	public void setSpecularCoeff(double specularCoeff) 
	{
		this.specularCoeff = specularCoeff;
	}

	public void setShininess(int shininess) 
	{
		this.shininess = shininess;
	}

	public void setTransparent(boolean isTransparent) 
	{
		this.isTransparent = isTransparent;
	}

	@Override
	public String toString() 
	{
		String transparencyString = String.format("%b", isTransparent);
		
		return String.format("Color: %-15s | AmbientCoeff: %.3f | DiffuseCoeff: %.3f | ReflectiveCoeff: %.3f | SpecularCoeff: %.3f | Shininess: %-3d | Transparency: %-5s | RefractionIndex: %.3f | Roughness: %.3f | ProceduralTexture: %s", ColorOperations.colorToString(color), ambientCoeff, diffuseCoeff, reflectiveCoeff, specularCoeff, shininess, transparencyString, refractionIndex, roughness, proceduralTexture);
	}
}
