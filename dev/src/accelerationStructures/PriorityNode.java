package accelerationStructures;

public class PriorityNode implements Comparable
{
	private final double EPSILON = 0.000001;
	private OctreeNode node;
	
	private double tDistance;
	
	public PriorityNode(OctreeNode node, double tDistance) 
	{
		this.node = node;
		this.tDistance = tDistance;
	}
	
	@Override
	public int compareTo(Object o) 
	{
		PriorityNode object = (PriorityNode)o;
		
		if(Math.abs(object.tDistance - this.tDistance) < EPSILON)
			return 0;
		
		else
			return (int)Math.signum(this.tDistance - object.tDistance);
	}
	
	public OctreeNode getNode()
	{
		return this.node;
	}
	
	public double getTDistance() 
	{
		return tDistance;
	}
}
