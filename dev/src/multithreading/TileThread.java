package multithreading;

import java.util.Random;

import rayTracer.RayTracer;
import scene.RayTracingScene;

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
