package multithreading;

import rayTracer.RayTracer;
import scene.MyScene;

public class TileThread implements Runnable
{
	ThreadsTaskList taskList;
	
	RayTracer rayTracerInstance;
	MyScene renderScene;
	
	public TileThread(ThreadsTaskList taskList, RayTracer rayTracerInstance, MyScene renderScene) 
	{
		this.taskList = taskList;
		
		this.rayTracerInstance = rayTracerInstance;
		this.renderScene = renderScene;
	}
	
	@Override
	public void run() 
	{
		while(this.rayTracerInstance.computeTask(this.renderScene, this.taskList)) {}//On calcule des tiles tant qu'il y en a à calculer
	}

	public void startThread()
	{
		Thread thisThread = new Thread(this, "");
		thisThread.start();
	}
}
