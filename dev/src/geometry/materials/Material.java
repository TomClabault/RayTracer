package geometry.materials;

import javafx.scene.paint.Color;

/*
 * Classe permettant définir des matériaux à partir des différentes caractéristiques physiques que peut simuler le rayTracer
 * 
 * Un matériau est définit par différentes caractéristiques 
 */
public interface Material 
{	
	/*
	 * Permet d'obtenir la quantité de lumière ambiante que le matériau est capable de renvoyer.
	 * 0 signifie que l'objet portant le matériau ne sera pas illuminé par la luminosité ambiante, 1 signifie qu'il "reçevra" 100% de la luminosité ambiante de la scène
	 * 
	 *  @return Retourne un réel entre 0 et 1 représentant le pourcentage de luminosité ambiante qui réfléchira l'objet 
	 */
	public abstract double getAmbientCoeff();
	
	/*
	 * Retourne le couleur du matériau sous la forme d'un objet Color.RGB(r, g, b)
	 * 
	 * @return Color.rgb(r, g, b), la couleur de l'objet au format RGB
	 */
	public abstract Color getColor();
	
	/*
	 * Permet d'obtenir la composante diffuse du matériau
	 * Plus un matériau est diffus et plus il sera mat. La craie est par exemple très diffuse alors que l'aluminium ne l'est pas
	 * 
	 * @return Retourne un réel entre 0 et 1 représentant le pourcentage de diffusion de la lumière par l'objet
	 */
	public abstract double getDiffuseCoeff();
	
	/*
	 * Permet d'obtenir la proportion de lumière que réfléchit le matériau.
	 * Un coefficient réflectif de 1 fera du matériau un miroir tandis qu'à 0, le matériau ne réfléchira rien de ce qui l'entour
	 * 
	 * @return Retourne la proportion de lumière que réfélchit l'objet. Réel entre 0 et 1
	 */
	public abstract double getReflectiveCoeff();
	
	/*
	 * Permet d'obtenir la brillance du matériau
	 * Cette valeur influe sur les tâches spécualaires de l'objet. Une shininess haute entraînera des tâches plus petite alors qu'une faible shininess produira des tâches de spécularité larges
	 * 
	 * @return Retourne un entier positif représentant la brillance du matériau. 
	 */
	public abstract int getShininess(); 
	
	/*
	 * Permet d'obtenir le coefficient de spécularité du matériau.
	 * Ce coefficient définit l'intensité des tâches de spécularité de l'objet.
	 * Avec une valeur de 0, le matériau n'exhibera aucune tâches spéculaires.
	 * Pour une valeur de 0.5 les tâches seront moins marquées et visibles qu'avec une valeur de 1 
	 * 
	 * @return Un réel entre 0 et 1 représentant le coefficient de spécularité de l'objet
	 */
	public abstract double getSpecularCoeff();
}
