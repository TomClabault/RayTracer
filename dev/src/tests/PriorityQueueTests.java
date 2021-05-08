package tests;

import java.util.PriorityQueue;

import accelerationStructures.PriorityNode;

public class PriorityQueueTests 
{
	public static void main(String[] args) 
	{
		PriorityQueue<PriorityNode> testQueue = new PriorityQueue<>();
		
		testQueue.add(new PriorityNode(null, 0));
		testQueue.add(new PriorityNode(null, 1));
		testQueue.add(new PriorityNode(null, -0.5));
		testQueue.add(new PriorityNode(null, 2));
		
		System.out.println(testQueue.peek().getTDistance() + " | Expected : " + -0.5);
	}
}
