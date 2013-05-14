import java.util.LinkedList;

public interface SearchNode<E> {

	public boolean equals(Object nodeObj);
	public E getNodeObj();
	//public void addVisited(D<E> newVisited);
	public int getNumNodesVisited();
	public boolean hasVisited(E otherNode);
	//public LinkedList<D<E>> getNodesVisited();
	public LinkedList<E> getNodeObjsVisited();
	public int getDistanceTravelled();
	
}