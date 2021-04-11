package materials;

import javafx.scene.paint.Color;
import materials.textures.*;
import maths.ColorOperations;

/*
 * Classe permettant définir des matériaux à partir des différentes caractéristiques physiques que peut simuler le rayTracer
 * 
 * Un matériau est définit par différentes caractéristiques:
 * 
 *  - Une caractéristique diffuse: Plus un matériau est diffus, plus la lumière sera renvoyée dans "toute les directions", donnant un aspect mat au matériau
 *  - Une caractéristique spéculaire: Donne de la brillance à l'objet
 *  - Une caractéristique de "brillance" (shininess): Plus cette valeur est haute plus les tâches spéculaires de l'objet seront petites
 *  - Une caractéristique réflective: Plus un matériau est réfléchissant plus il se comportera comme un miroir. 
 *  - Une caractéristique de transparence: Si oui ou non le matériau est transparent. Un matériau transparent réfractera la lumière et aura donc besoin d'un indice de réfraction (décrit ci-dessous) approprié
 *  - Un indice de réfraction: Caractérise à quel point les rayons de lumière sont réfractés par le matériau  
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
	private double scatteringCoeff;
	
	private ProceduralTexture proceduralTexture;//Attribut special qui spécifie la texture du matériau. Utilisé pour le damier par exemple
	
	/*
	 * Crée un matériau de A à Z
	 * 
	 * @param color Couleur du matériau
	 * @param ambientCoeff Réel entre 0 et 1. Coefficient représentant la quantité de lumière ambiante que le matériau réfléchi
	 * @param diffuseCoeff Réel entre 0 et 1. Coefficient modulant la diffusion de lumière du matériau
	 * @param reflectiveCoeff Réel entre 0 et 1. Coefficient qui gère la réflectivité du matériau. Plus ce coefficient se rapproche de 1 et plus le matériau se rapprochera d'un matériau "mirroir"
	 * @param specularCoeff Réel entre 0 et 1. Coefficient permettant de jouer sur l'intensité des tâches spéculaires du matériau. Plus ce coefficient se rapproche de 1 plus les tâches spéculaires seront marquées. A 0, le matériau n'est pas spéculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches spéculaires du matériau. Plus ce nombre est grand plus les tâches seront petites
	 * @param isTransparent True pour indiquer que le matériau est transparent, false pour indiquer qu'il n'est pas transparent
	 * @param refractionIndex Indice de réfraction du matériau. Doit être 0 pour indiquer que le matériau n'est pas réfractif
	 * @param scaterringCoeff Le coefficient de dispersion des rayons de lumière à l'impact du matériau. Responsable de réflexions floues 
	 */
	public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double scatteringCoeff)
	{
		this.color = color;
		
		this.ambientCoeff = ambientCoeff;
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		this.isTransparent = isTransparent;
		this.refractionIndex = refractionIndex;
		this.scatteringCoeff = scatteringCoeff;
		
		this.proceduralTexture = null;//Pas de texture par défaut
	}
	
	/*
	 * Crée un matériau de A à Z
	 * 
	 * @param color Couleur du matériau
	 * @param ambientCoeff Réel entre 0 et 1. Coefficient représentant la quantité de lumière ambiante que le matériau réfléchi
	 * @param diffuseCoeff Réel entre 0 et 1. Coefficient modulant la diffusion de lumière du matériau
	 * @param reflectiveCoeff Réel entre 0 et 1. Coefficient qui gère la réflectivité du matériau. Plus ce coefficient se rapproche de 1 et plus le matériau se rapprochera d'un matériau "mirroir"
	 * @param specularCoeff Réel entre 0 et 1. Coefficient permettant de jouer sur l'intensité des tâches spéculaires du matériau. Plus ce coefficient se rapproche de 1 plus les tâches spéculaires seront marquées. A 0, le matériau n'est pas spéculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches spéculaires du matériau. Plus ce nombre est grand plus les tâches seront petites
	 * @param isTransparent True pour indiquer que le matériau est transparent, false pour indiquer qu'il n'est pas transparent
	 * @param refractionIndex Indice de réfraction du matériau. Doit être 0 pour indiquer que le matériau n'est pas réfractif
	 * @param scaterringCoeff Le coefficient de dispersion des rayons de lumière à l'impact du matériau. Responsable de réflexions floues 
	 * @param proceduralTexture La texture procédurale de l'objet
	 */
	public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, boolean isTransparent, double refractionIndex, double scatteringCoeff, ProceduralTexture proceduralTexture)
	{
		this.color = color;
		
		this.ambientCoeff = ambientCoeff;
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		this.isTransparent = isTransparent;
		this.refractionIndex = refractionIndex;
		this.scatteringCoeff = scatteringCoeff;
		
		this.proceduralTexture = proceduralTexture;
	}

	/*
	 * Retourne le couleur du matériau sous la forme d'un objet Color.RGB(r, g, b)
	 * 
	 * @return Color.rgb(r, g, b), la couleur de l'objet au format RGB
	 */
	public Color getColor()
	{
		return this.color;
	}
	
	/*
	 * Permet de récupérer le coefficient de réflexion de la lumière ambiante du matériau
	 * 
	 * @return Retourne un réel entre 0 et 1 représentant le coefficient dit 'ambiant' du matériau
	 */
	public double getAmbientCoeff()
	{
		return this.ambientCoeff;
	}
	
	/*
	 * Permet d'obtenir la composante diffuse du matériau
	 * Plus un matériau est diffus et plus il sera mat. La craie est par exemple très diffuse alors que l'aluminium ne l'est pas
	 * 
	 * @return Retourne un réel entre 0 et 1 représentant le pourcentage de diffusion de la lumière par l'objet
	 */
	public double getDiffuseCoeff()
	{
		return this.diffuseCoeff;
	}
	
	/*
	 * Permet de savoir si un matériau est transparent et réfracte la lumière ou non
	 * 
	 * @return Retourne true si le matériau est transparent, false sinon
	 */
	public boolean getIsTransparent()
	{
		return this.isTransparent;
	}
	
	/*
	 * Permet d'obtenir la proportion de lumière que réfléchit le matériau.
	 * Un coefficient réflectif de 1 fera du matériau un miroir tandis qu'à 0, le matériau ne réfléchira rien de ce qui l'entour
	 * 
	 * @return Retourne la proportion de lumière que réfélchit l'objet. Réel entre 0 et 1
	 */
	public double getReflectiveCoeff()
	{
		return this.reflectiveCoeff;
	}
	
	/*
	 * Permet d'obtenir l'indice de réfraction du matériau
	 * 
	 * @return Un réel représentant l'indice de réfraction du matériau. Si le matériau n'est pas transparent, retournera 0
	 */
	public double getRefractionIndex()
	{
		return this.refractionIndex;
	}
	
	public double getScatteringCoeff() 
	{
		return scatteringCoeff;
	}
	
	/*
	 * Permet d'obtenir la brillance du matériau
	 * Cette valeur influe sur les tâches spécualaires de l'objet. Une shininess haute entraînera des tâches plus petite alors qu'une faible shininess produira des tâches de spécularité larges
	 * 
	 * @return Retourne un entier positif représentant la brillance du matériau. 
	 */
	public int getShininess()
	{
		return this.shininess;
	}
	
	/*
	 * Permet d'obtenir le coefficient de spécularité du matériau.
	 * Ce coefficient définit l'intensité des tâches de spécularité de l'objet.
	 * Avec une valeur de 0, le matériau n'exhibera aucune tâches spéculaires.
	 * Pour une valeur de 0.5 les tâches seront moins marquées et visibles qu'avec une valeur de 1 
	 * 
	 * @return Un réel entre 0 et 1 représentant le coefficient de spécularité de l'objet
	 */
	public double getSpecularCoeff()
	{
		return this.specularCoeff;
	}
	
	/*
	 * Permet d'obtenir la texture procédurale de l'objet s'il en a une
	 * 
	 * @return L'instance de la texture procédurale de l'objet. Retourne null si l'objet n'a pas de texture procédurale.
	 */
	public ProceduralTexture getProceduralTexture()
	{
		return this.proceduralTexture;
	}
	
	/*
	 * Permet de déterminer si le matériau a une texture procédurale appliquée ou non
	 * 
	 *  @return Retourne true si le matériau a une texture procédurale appliquée. false sinon
	 */
	public boolean hasProceduralTexture()
	{
		return !(this.proceduralTexture == null);
	}
	
	/*
	 * Permet de définir la couleur du matériau
	 * 
	 * @param color La nouvelle couleur du matériau
	 */
	public void setColor(Color color) 
	{
		this.color = color;
	}

	/*
	 * Permet de defénir un coefficient de réflexion de la lumière ambiante pour le matériau
	 * 
	 * @param ambientCoeff Le nouveau coefficient ambiant du matériau
	 */
	public void setAmbientCoeff(double ambientCoeff)
	{
		this.ambientCoeff = ambientCoeff;
	}
	
	/*
	 * Permet de définir le coefficient de diffusion du matériau
	 * 
	 * @param diffuseCoeff Le nouveau coefficient de diffusion du matériau
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
	
	public void setScatteringCoeff(double scatteringCoeff) 
	{
		this.scatteringCoeff = scatteringCoeff;
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
		
		return String.format("Color: %-15s | AmbientCoeff: %.3f | DiffuseCoeff: %.3f | ReflectiveCoeff: %.3f | SpecularCoeff: %.3f | Shininess: %-3d | Transparency: %-5s | RefractionIndex: %.3f | ScaterringCoeff: %.3f | ProceduralTexture: %s", ColorOperations.colorToString(color), ambientCoeff, diffuseCoeff, reflectiveCoeff, specularCoeff, shininess, transparencyString, refractionIndex, scatteringCoeff, proceduralTexture);
	}
}
