package multithreading;

import java.util.Random;

import rayTracer.RayTracer;
import scene.RayTracingScene;

/**
 * Thread s'occupant du rendu des tâches d'une instance de {@link multithreading.ThreadsTaskList}.<br>
 * Chaque TileThread dispose d'un générateur de nombre aléatoire qui lui est propre, permettant ainsi une consistence des nombres aléatoires
 * générés d'une image à l'autre (pourvu que la graîne des générateurs soit gérée de façon appropriée).
 */
public class TileThread extends Thread
{
	private ThreadsTaskList taskList;
	
	private RayTracer rayTracerInstance;
	private RayTracingScene renderScene;
	
	private Random randomGenerator;//Générateur de nombre aléatoire local au thread pour éviter le surcôut de synchronisation d'un seul générateur qui serait utilisé pour tous les threads
	
	public TileThread(ThreadsTaskList taskList, RayTracer rayTracerInstance, RayTracingScene renderScene) 
	{
		super(String.format("RT-Thread"));
		
		this.taskList = taskList;
		
		this.rayTracerInstance = rayTracerInstance;
		this.renderScene = renderScene;
		this.randomGenerator = new Random(0);
	}
	
	@Override
	public void run() 
	{
		while(this.rayTracerInstance.computeTask(this.renderScene, this.taskList)) {}//On calcule des tiles tant qu'il y en a à calculer
	}
	
	/**
	 * Permet d'obtenir le générateur de nombre aléatoire local au thrad
	 */
	public Random getLocalRandomGenerator()
	{
		return this.randomGenerator;
	}
	
	/**
	 * Permet de réinitialiser le générateur de nombre aléatoire local au thread avec la graine passée en paramètre  
	 * 
	 * @param seed La graîne avec laquelle le générateur sera réinitialisé.
	 */
	public void setLocalRandomGenerator(long seed)
	{
		this.randomGenerator.setSeed(seed);
	}
}
