package multithreading;

import java.util.Random;

import rayTracer.RayTracer;
import scene.RayTracingScene;

/**
 * Thread s'occupant du rendu des tâches d'une instance de {@link multithreading.ThreadsTaskList}.<br>
 * Chaque TileThread dispose d'un generateur de nombre aleatoire qui lui est propre, permettant ainsi une consistence des nombres aleatoires
 * generes d'une image a l'autre (pourvu que la graîne des generateurs soit geree de façon appropriee).
 */
public class TileThread extends Thread
{
	private ThreadsTaskList taskList;
	
	private RayTracer rayTracerInstance;
	private RayTracingScene renderScene;
	
	private Random randomGenerator;//Generateur de nombre aleatoire local au thread pour eviter le surcout de synchronisation d'un seul generateur qui serait utilise pour tous les threads
	
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
		while(this.rayTracerInstance.computeTask(this.renderScene, this.taskList)) {}//On calcule des tiles tant qu'il y en a a calculer
	}
	
	/**
	 * Permet d'obtenir le generateur de nombre aleatoire local au thrad
	 */
	public Random getLocalRandomGenerator()
	{
		return this.randomGenerator;
	}
	
	/**
	 * Permet de reinitialiser le generateur de nombre aleatoire local au thread avec la graine passee en parametre  
	 * 
	 * @param seed La graîne avec laquelle le generateur sera reinitialise.
	 */
	public void setLocalRandomGenerator(long seed)
	{
		this.randomGenerator.setSeed(seed);
	}
}
