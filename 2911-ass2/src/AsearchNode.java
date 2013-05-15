import java.util.LinkedList;

public class AsearchNode {

	public AsearchNode(DualPoint jobNode, int externalDistance)
	{
		this.nodeObj = jobNode;
		this.externalDistanceTravelled = externalDistance;
		this.visited = new LinkedList<AsearchNode>();
	}
	
	public boolean equals(Object obj)
	{
		boolean result = false;
		
		if (obj == this)
		{
			result =  true;
		}
		else if (obj.getClass() == AsearchNode.class)
		{
			if (this.getNodeObj().equals(((AsearchNode)obj).getNodeObj()))
			{
				result = true;
			}
		}
		return result;
	}
	
	public DualPoint getNodeObj()
	{
		return this.nodeObj;
	}
	
	public void addVisited(AsearchNode newVisited)
	{
		visited.addLast(newVisited);
	}
	
	public int getNumNodesVisited()
	{
		return visited.size();
	}
	
	public boolean hasVisited(DualPoint otherNode)
	{
		for (AsearchNode node : visited)
		{
			if (node.getNodeObj().equals(otherNode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public LinkedList<AsearchNode> getNodesVisited()
	{
		LinkedList<AsearchNode> path = new LinkedList<AsearchNode>();
		for (AsearchNode obj : visited)
		{
			path.add(obj);
		}
		return path;
	}
	
	public LinkedList<AsearchNode> getNodeObjsVisited()
	{
		LinkedList<AsearchNode> path = new LinkedList<AsearchNode>();
		for (AsearchNode obj : visited)
		{
			path.add(obj);
		}
		return path;
	}
	
	public String toString()
	{
		return this.getNodeObj().toString() + "{"+this.getExternalDistanceTravelled()+"}";
	}
	
	public int getExternalDistanceTravelled()
	{
		return this.externalDistanceTravelled;
	}
	
	public int getTotalDistanceTravelled()
	{
		int internalDistanceTravelled = 0;
		for (AsearchNode eachNode : visited)
		{
			internalDistanceTravelled += eachNode.getNodeObj().getInternalDistance();
		}
		
		return this.externalDistanceTravelled + internalDistanceTravelled;
	}

	private LinkedList<AsearchNode> visited;
	private DualPoint nodeObj;
	private int externalDistanceTravelled;
	
}