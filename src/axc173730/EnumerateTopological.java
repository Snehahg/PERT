/**
 * Enumerate topological orderings of a graph
 * @author akash chand
 * @author pushpita panigrahi
 * @author sneha hullivan girisha
 * @author deeksha lakhshmeesh mestha
 */

package axc173730;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import axc173730.DFS.DFSVertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
	boolean print;  // Set to true to print array in visit
	long count;      // Number of permutations or combinations visited
	Selector sel;
	Vertex source; // start vertex, 1st vertex in graph list
	Vertex target; // end vertex, last vertex in graph list
	Vertex[] arr;
	DFS dfs;
	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();
		target = g.getVertex(g.size());
		source = g.getVertex(1);
		arr = new Vertex[this.g.size()];
		dfs = new DFS(g);
	}

	static class EnumVertex implements Factory {
		EnumVertex() { }
		public EnumVertex make(Vertex u) { return new EnumVertex();	}
	}

	class Selector extends Enumerate.Approver<Vertex> {
		Stack<Vertex> stack = new Stack<>();
		/**
		 * approve vertices which have inDegrees zero
		 * @param u graph vertex in consideration
		 * @return true if the vertex has zero indegree
		 */
		@Override
		public boolean select(Vertex u) { 
			if (dfs.get(u).indegree == 0) {
				for (Edge edge : g.outEdges(u)) {
					Vertex v = edge.otherEnd(u);
					DFSVertex vertex = dfs.get(v);
					vertex.indegree--;
				}
				return true;
			}else {
				return false;
			}
		}

		/**
		 * unselect the vertex from the topological ordering
		 * @param u graph vertex in consideration
		 */
		@Override
		public void unselect(Vertex u) { // reset inDegrees of vertices after processing
			if (dfs.get(u).indegree == 0) {
				for (Edge edge : g.outEdges(u)) {
					Vertex v = edge.otherEnd(u);
					DFSVertex vertex = dfs.get(v);
					vertex.indegree++;
				}
			}
		}

		/**
		 * confirms the topological ordering
		 * @param arr array containg the topological ordering
		 * @param k kth position of the array
		 */
		@Override
		public void visit(Vertex[] arr, int k) {
			count++;
			if(print) {
				for(Vertex u: arr) {
					System.out.print(u + " ");
				}
				System.out.println();
			}
		}
	}


	/**
	 * Make copies of the vertices, to set inDegrees and avoid modifying original graph
	 */
	private void initializeDFS() {
		Iterator<Vertex> git = g.iterator();
		while (git.hasNext()) {
			Vertex u = git.next();
			DFSVertex vertex = dfs.get(u);
			vertex.indegree = u.inDegree();
		}
	}


	/**
	 * Enumerate the topological orderings in a graph
	 * @param flag - print flag -  prints the possible orderings if true
	 * @return the number of orderings available
	 */
	public long enumerateTopological(boolean flag) {
		print = flag;
		initializeDFS();
		LinkedList<Vertex> list = (LinkedList<Vertex>) dfs.topologicalOrder1();
		int i = 0;
		for (Vertex vertex : list) {
			arr[i] = vertex;
			i++;
		}
		Enumerate<Vertex> enumTopological = new Enumerate<>(arr, sel);
		enumTopological.permute(g.size());
		return count;
	}

	//-------------------static methods----------------------

	/**
	 * Counts number of topological ordering of an graph
	 * @param g graph in consideration
	 * @return number of topological ordering in the input graph
	 */
	public static long countTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(false);
	}

	/**
	 * Enumerates the topological ordering of the graph
	 * @param g graph in consideration
	 * @return number of topological ordering in the input graph
	 */
	public static long enumerateTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		//String string = "7 6	1 2 2	1 7 7	2 3 3	2 4 4	7 5 5	7 6 6 0";
		//String string = "11 17	5 11 5	11 4 11	4 5 4	4 9 4	9 11 9	11 3 11	11 6 11		3 10 3	10 6 10	6 3 6	2 3 2	1 4 1	1 8 1	8 2 8	7 8 7	2 7 2";
		String string  = "6 7	1 2 2	1 4 4	2 3 3	4 3 3	4 5 5	3 6 6	5 6 6";
		int VERBOSE = 1;
		Scanner in;
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		Graph g = Graph.readDirectedGraph(in);
		// if(args.length > 0) { VERBOSE = 0; }
		// Graph g = Graph.readDirectedGraph(new java.util.Scanner(new
		// java.io.File(args[0])));
		g.printGraph(false);
		Graph.Timer t = new Graph.Timer();
		long result;
		if(VERBOSE > 0) {
			result = enumerateTopologicalOrders(g);
		} else {
			result = countTopologicalOrders(g);
		}
		System.out.println("\n" + result + "\n" + t.end());
	}

}
