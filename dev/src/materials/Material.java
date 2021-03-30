package materials;

import javafx.scene.paint.Color;

/*
 * Classe permettant définir des matériaux à partir des différentes caractéristiques physiques que peut simuler le rayTracer
 * 
 * Un matériau est définit par différentes caractéristiques 
 */
public class Material 
{	
	public static final int NORMAL = 0;
	public static final int CHECKERBOARD = 1;
	
	private Color color;
	
	private double diffuseCoeff;
	private double reflectiveCoeff;
	private int shininess;
	private double specularCoeff;
	
	private int special;//Attribut special qui spécifie la texture du matériau. Utilisé pour le damier par exemple
	
	/*
	 * Crée un matériau de A à Z
	 * 
	 * @param color Couleur du matériau
	 * @param diffuseCoeff Réel entre 0 et 1. Coefficient modulant la diffusion de lumière du matériau
	 * @param reflectiveCoeff Réel entre 0 et 1. Coefficient qui gère la réflectivité du matériau. Plus ce coefficient se rapproche de 1 et plus le matériau se rapprochera d'un matériau "mirroir"
	 * @param specularCoeff Réel entre 0 et 1. Coefficient permettant de jouer sur l'intensité des tâches spéculaires du matériau. Plus ce coefficient se rapproche de 1 plus les tâches spéculaires seront marquées. A 0, le matériau n'est pas spéculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches spéculaires du matériau. Plus ce nombre est grand plus les tâches seront petites
	 */
	public Material(Color color, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess)
	{
		this.color = color;
		
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		
		this.special = Material.NORMAL;
	}
	
	/*
	 * Crée un matériau de A à Z
	 * 
	 * @param color Couleur du matériau
	 * @param diffuseCoeff Réel entre 0 et 1. Coefficient modulant la diffusion de lumière du matériau
	 * @param reflectiveCoeff Réel entre 0 et 1. Coefficient qui gère la réflectivité du matériau. Plus ce coefficient se rapproche de 1 et plus le matériau se rapprochera d'un matériau "mirroir"
	 * @param specularCoeff Réel entre 0 et 1. Coefficient permettant de jouer sur l'intensité des tâches spéculaires du matériau. Plus ce coefficient se rapproche de 1 plus les tâches spéculaires seront marquées. A 0, le matériau n'est pas spéculaire
	 * @param shininess Entier positif non nul. Permet de jouer sur la taille des tâches spéculaires du matériau. Plus ce nombre est grand plus les tâches seront petites
	 */
	public Material(Color color, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess, int specialTexture)
	{
		this.color = color;
		
		this.diffuseCoeff = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
		this.specularCoeff = specularCoeff;
		this.shininess = shininess;
		
		this.special = specialTexture;
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
}
