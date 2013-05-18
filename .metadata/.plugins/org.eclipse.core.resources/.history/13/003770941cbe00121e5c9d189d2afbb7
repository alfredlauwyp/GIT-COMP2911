import java.util.ArrayList;

public class AdjListGraph<E> implements Graph<E> {

	public AdjListGraph()
	{
		nodes = new ArrayList<ArrayList<E>>();
	}
	
	@Override
	public void addNode(E e) {
		ArrayList<E> adjList = new ArrayList<E>();
		adjList.add(e);
		nodes.add(adjList);
	}

	@Override
	public void removeNode(E e) {
		for (ArrayList<E> v : nodes)
		{
			if (v.get(0).equals(e))
			{
				nodes.remove(v);
				break;
			}
		}
		for (ArrayList<E> v : nodes)
		{
			for (E item : v)
			{
				if (item.equals(e))
				{
					v.remove(item);
					break;
				}
			}
		}
	}

	@Override
	public void addEdge(E from, E to) {
		for (ArrayList<E> v : nodes)
		{
			if (v.get(0).equals(from))
			{
				v.add(to);
			}
		}		
	}

	@Override
	public boolean isConnected(E from, E to) {
		for (ArrayList<E> v : nodes)
		{
			if (v.get(0).equals(from))
			{
				return v.contains(to);
			}
		}
		return false;
	}

	@Override
	public void removeEdge(E from, E to) {
		for (ArrayList<E> v : nodes)
		{
			if (v.get(0).equals(from))
			{
				v.remove(to);
			}
		}
	}

	@Override
	public void printGraph() {
		int vertice = 0;
		for (ArrayList<E> internal : nodes)
		{
			System.out.print("Vertice "+(vertice++)+" (" + internal.get(0) + "): ");
			int listCounter = 0;
			for (E item : internal)
			{
				System.out.print("(" + (listCounter++) + " " + item + ")");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}

	@Override
	public boolean isInGraph(E e) {
		for (ArrayList<E> vertice : nodes)
		{
			if (vertice.get(0).equals(e))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<E> getNeighbours(E e) {
		ArrayList<E> neighbours = new ArrayList<E>();
		for (ArrayList<E> v : nodes)
		{
			if (v.get(0).equals(e))
			{
				for (int i = 1; i < v.size(); i++)
				{
					neighbours.add(v.get(i));
				}
			}
		}
		return neighbours;
	}

	@Override
	public int getNumVertices() {
		return nodes.size();
	}

	@Override
	public int getNumEdges() {
		int totalEdges = 0;
		for (ArrayList<E> vertice : nodes)
		{
			totalEdges += vertice.size();
			totalEdges -= 1; // Remove initial item
		}
		return totalEdges;
	}
	
	public ArrayList<ArrayList<E>> getNodes()
	{
		return new ArrayList<ArrayList<E>>(nodes);
	}
	
	private ArrayList<ArrayList<E>> nodes;
	
}
