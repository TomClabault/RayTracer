public boolean computeTask(RayTracingScene renderScene, 
						   ThreadsTaskList taskList)
{
	Integer taskNumber = 0;
	TileTask currentTileTask = null;
	
	//Bloc synchronise avec la variable taskNumber
	synchronized(taskNumber)
	{
		taskNumber = taskList.getTotalTaskGiven();
		if(taskNumber >= taskList.getTotalTaskCount())
			return false;
		
		//On recupere la tache a calculer
		currentTileTask = taskList.getTask(taskList.getTotalTaskGiven());
		taskList.incrementTaskGiven();
	}
	renderTile(renderScene, currentTileTask);
		
	Integer randomVariable = 0;
	synchronized(randomVariable)
	{
		taskList.incrementTaskFinished();
	}
	return true;//Encore des tuiles a calculer
}
