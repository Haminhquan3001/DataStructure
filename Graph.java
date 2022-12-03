
/**
 * Graph.java
 * CIS 22C, Final Project
 * @author Kia Anooshirvani
 * @author Jared Soliven
 * @author Ivan Louize Winoto
 * @author Hamzah Zaza
 * @author Kenneth Dannlyson
 * @author Quan Ha
 */

import java.util.ArrayList;

public class Graph {
	private int vertices;
	private int edges;
	private ArrayList<List<Integer>> adj;
	private ArrayList<Character> color;
	private ArrayList<Integer> distance;
	private ArrayList<Integer> parent;

	/** Constructors */

	/**
	 * initializes an empty graph, with n vertices and 0 edges
	 * 
	 * @param n the number of vertices in the graph
	 */
	public Graph(int n) {
		vertices = n;
		edges = 0;
		color = new ArrayList<>(n);
		distance = new ArrayList<>(n);
		parent = new ArrayList<>(n);
		adj = new ArrayList<List<Integer>>(n);
		for (int x = 0; x <= n; x++) {
			color.add(x, 'W');
			distance.add(x, -1);
			parent.add(x, null);
			adj.add(new List<Integer>());
		}
	}

	/*** Accessors ***/

	/**
	 * Returns the number of edges in the graph
	 * 
	 * @return the number of edges
	 */
	public int getNumEdges() {
		return edges;
	}

	/**
	 * Returns the number of vertices in the graph
	 * 
	 * @return the number of vertices
	 */
	public int getNumVertices() {
		return vertices;
	}

	/**
	 * returns whether the graph is empty (no edges)
	 * 
	 * @return whether the graph is empty
	 */
	public boolean isEmpty() {
		return (edges == 0);
	}

	/**
	 * Returns the value of the distance[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition 0 < v <= vertices
	 * @return the distance of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
		if (v <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException("getDistance: " + "cannot get the distance, v is out of bounds");
		} else {
			return distance.get(v);
		}
	}

	/**
	 * Returns the value of the parent[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition v <= vertices
	 * @return the parent of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Integer getParent(Integer v) throws IndexOutOfBoundsException {
		if (v > vertices) {
			throw new IndexOutOfBoundsException("getDistance: " + "cannot get the parent, vertex is out of bounds");
		} else {
			return parent.get(v);
		}

	}

	/**
	 * Returns the value of the color[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition 0 < v <= vertices
	 * @return the color of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Character getColor(Integer v) throws IndexOutOfBoundsException {
		if (v <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException("getColor: " + "cannot get the color, vertex is out of bounds");
		} else {
			return color.get(v);
		}

	}

	/*** Mutators ***/

	/**
	 * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into the
	 * list at index u) @precondition, 0 < u, v <= vertices
	 * 
	 * @throws IndexOutOfBounds exception when the precondition is violated
	 */
	public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
		if (u <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException(
					"addDirectedEdge: " + "Cannot add directed edges,  vertex is out of bounds");
		} else {
			adj.get(u).addLast(v);
			edges++;
		}
	}

//    public void removeDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
// 		if (u <= 0 || v > vertices) {
// 			throw new IndexOutOfBoundsException("removeDirectedEdge: " 
// 		+ "Cannot remove directed edges,  vertex is out of bounds");
// 		} else {
// 			
// 			adj.get(u).removeIterator();
// 			edges--;
// 		}
//     }

	/**
	 * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into the
	 * list at index u) and inserts u into the adjacent vertex list of
	 * v @precondition, 0 < u, v <= vertices
	 * 
	 */
	public void addUndirectedEdge(Integer u, Integer v) {
		if (u <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException(
					"addUndirectedEdge: " + "Cannot add undirected edges,  vertices is out of bounds");
		} else {
			adj.get(u).addLast(v);
			adj.get(v).addLast(u);
			edges++;
		}
	}

	/**
	 * Remove vertex v from the adjacency list of vertex u (i.e. remove v from the
	 * list at index u) @precondition, 0 < u, v <= vertices
	 */
	public void removeDirectedEdge(Integer u, Integer v) {
		if (u <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException(
					"removeDirectedEdge: " + "Cannot remove directed edges,  vertices is out of bounds");
		}
		adj.get(u).placeIterator();
		while (!(adj.get(u).getIterator().equals(v))) {
			adj.get(u).advanceIterator();
		}
		adj.get(u).removeIterator();
		edges--;
	}

	/**
	 * Remove vertex v from the adjacency list of vertex u (i.e. remove v from the
	 * list at index u) and removes u from the adjacent vertex list of
	 * v @precondition, 0 < u, v <= vertices
	 */
	public void removeUndirectedEdge(Integer u, Integer v) {
		if (u <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException(
					"removeDirectedEdge: " + "Cannot remove directed edges,  vertices is out of bounds");
		} else {
			adj.get(u).placeIterator();
			while (!(adj.get(u).getIterator().equals(v))) {
				adj.get(u).advanceIterator();
			}
			adj.get(u).removeIterator();

			adj.get(v).placeIterator();
			while (!(adj.get(v).getIterator().equals(u))) {
				adj.get(v).advanceIterator();
			}
			adj.get(v).removeIterator();
			edges--;
		}
	}

	/*** Additional Operations ***/

	/**
	 * Creates a String representation of the Graph Prints the adjacency list of
	 * each vertex in the graph, vertex: <space separated list of adjacent vertices>
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 1; i < vertices; i++) {
			result += adj.get(i).toString();
		}
		return result;
	}

	/**
	 * Prints the current values in the parallel ArrayLists after executing BFS.
	 * First prints the heading: v <tab> c <tab> p <tab> d Then, prints out this
	 * information for each vertex in the graph Note that this method is intended
	 * purely to help you debug your code
	 */
	public void printBFS() {
		System.out.println("vertex\tcolor\tparent\tdistance");

		for (int i = 1; i <= vertices; i++) {
			System.out.println((i) + "\t" + color.get(i) + "\t" + parent.get(i) + "\t" + distance.get(i));
		}

	}

	/**
	 * Performs breath first search on this Graph give a source vertex
	 * 
	 * @param source
	 * @precondition graph must not be empty
	 * @precondition source is a vertex in the graph
	 * @throws IllegalStateException     if the graph is empty
	 * @throws IndexOutOfBoundsException when the source vertex is not a vertex in
	 *                                   the graph
	 */

	public void BFS(Integer source) throws IllegalStateException, IndexOutOfBoundsException {
		if (isEmpty()) {
			throw new IllegalStateException("BFS:" + "Cannot called BFS, graph is empty");
		} else if (source > vertices) {
			throw new IndexOutOfBoundsException("BFS:" + "Cannot called BFS, source is not a vertex in the graph");
		} else {
			for (int x = 0; x <= vertices; x++) {
				color.set(x, 'W');
				distance.set(x, -1);
				parent.set(x, null);
			}

			color.set(source, 'G');
			distance.set(source, 0);
			List<Integer> L = new List<>();
			L.addLast(source);

			while (!(L.isEmpty())) {
				int i = L.getFirst();
				L.removeFirst();
				adj.get(i).placeIterator();
				for (int j = 0; j < adj.get(i).getLength(); j++) {
					int temp = adj.get(i).getIterator();
					if (color.get(temp).equals('W')) {
						color.set(temp, 'G');
						distance.set(temp, distance.get(i) + 1);
						parent.set(temp, i);
						L.addLast(temp);
					}
					adj.get(i).advanceIterator();

				}
				color.set(i, 'B');
			}

		}
	}
}