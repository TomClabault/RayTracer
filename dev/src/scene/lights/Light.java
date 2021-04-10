package scene.lights;

public interface Light 
{
	/*
	 * Permet de récupérer l'intensité lumineuse de la source de lumière
	 * 
	 * @return Un réel entre 0 et 1 représentant l'intensité lumineuse de la source lumineuse 
	 */
	public abstract double getIntensity();
}
