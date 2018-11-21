/** Starter code for SP8
 *  @author Akash Chand
 *  @author Arpita Agarwal
 */

// change to your netid
package axc173730;

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
//import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	boolean cycle; //cycle flag to determine if cycle is present
	int max; // for holding the number of connected components of the graph
	public static class DFSVertex implements Factory {
		int cno;
		boolean seen;
		Vertex parent;
		int top;
		int indegree;
		public DFSVertex(Vertex u) {
			seen= false;
			parent = null;
		}
		public DFSVertex make(Vertex u) { return new DFSVertex(u); }
	}

	public DFS(Graph g) {
		super(g, new DFSVertex(null));
	}

	public static DFS depthFirstSearch(Graph g) {
		return null;
	}

	/**
	 * Topological ordering using DFS
	 * @return the linked list of vertices in topological order
	 */
	public List<Vertex> topologicalOrder1() {
		LinkedList<Vertex> finishList = dfsCall();
		
		if(cycle==true || !this.g.isDirected())
			return null;
		return finishList;
	}
	/**
	 * DFS traversal of vertices
	 * @param git - iterator to iterate through the vertices of the graph
	 * @return list in order of their finish times. Vertex at 0th index gets finished first
	 */
	private LinkedList<Vertex> dfsCall() {
		LinkedList<Vertex> finishList =  new LinkedList<>() ;
		Integer topNum = this.g.size();
		Integer cnum =0;
		Iterator<Vertex> it = this.g.iterator();
		while(it.hasNext()) {
			Vertex u = it.next();
			DFSVertex vertex = this.get(u);
			vertex.seen =false;
			vertex.parent =null;
			vertex.top= 0;
		}
		it =this.g.iterator();
		while(it.hasNext()) {
			Vertex u = it.next();
			DFSVertex vertex = this.get(u);
			boolean seenFlag = vertex.seen;
			if(!seenFlag) {
				DFS_Visit(u, finishList, topNum, ++cnum);
				
			}
		}

		max=cnum;
		return finishList;
	}

	/**
	 *  Recursively visits the children of the node- DFS
	 * @param u - vertex in consideration
	 * @param finishList - Linked list of vertices in topological order
	 * @param topNum - 
	 * @param cnum - Component number
	 */
	private void DFS_Visit(Vertex u, LinkedList<Vertex> finishList, Integer topNum, Integer cnum) {
		
		DFSVertex vertex = this.get(u);
		vertex.seen=true;
		for(Edge edgeOfU: this.g.outEdges(u)) {
			Vertex v = edgeOfU.otherEnd(u);
			DFSVertex dfsv = this.get(v);
			if(vertex.parent!=null && dfsv.parent!=null && vertex.parent.equals(v))
				continue;
			dfsv.parent = u;
			boolean seenFlag = dfsv.seen;
			if(!seenFlag) {
				DFS_Visit(v, finishList, topNum, cnum);
			}
			else if(vertex.top>=dfsv.top) {
				cycle=true;
			}
		}
		vertex.top = topNum--;
		vertex.cno = cnum;
		finishList.addFirst(u);
	}
	// Find the number of connected components of the graph g by running dfs.
	// Enter the component number of each vertex u in u.cno.
	// Note that the graph g is available as a class field via GraphAlgorithm.
	/**
	 * Finds the number of components in the graph by calling topological order
	 * @return the number of components in the graph
	 */
	public int connectedComponents() {
		dfsCall();
		return max;
	}

	/**
	 * For finding the strongly connectedcomponents of the graph
	 * @param g - graph object
	 * @return - returns the dfs object having number of strongly connected components in the max instance variable
	 */
	public static DFS stronglyConnectedComponents(Graph g) {
		
		DFS d = new DFS(g);
		d.dfsCall();
		g.reverseGraph();
		d.dfsCall();
		g.reverseGraph();
		return d;
	}
	// After running the onnected components algorithm, the component no of each vertex can be queried.
	public int cno(Vertex u) {
		return get(u).cno;
	}

	// Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder1(Graph g) {
		if(!g.isDirected()) // if undirected graph returns null
			return null;
		DFS d = new DFS(g);
		return d.topologicalOrder1();
	}

	// Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder2(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder2();
	}

	/**
	 * Topological order of the vertices using removing vertices method
	 * @return - Linked List of vertices in topological order
	 */
	private List<Vertex> topologicalOrder2() {
		Graph gcopy = new Graph(this.g);
		Iterator<Vertex> git = gcopy.iterator();
		Queue<Vertex> zeroQ = new LinkedList<Vertex>();
		LinkedList<Vertex> finalList = new LinkedList<>();
		while(git.hasNext()) {
			Vertex u = git.next();
			DFSVertex vertex = this.get(u);
			vertex.indegree = u.inDegree();
			if(u.inDegree()==0) {
				zeroQ.add(u);
			}
			
		}
		while(!zeroQ.isEmpty()) {
			Vertex u = zeroQ.poll();
			DFSVertex dfsu = this.get(u);
			//dfsu.col=Color.Gray;
			dfsu.seen =true;
			finalList.add(u);
			for(Edge edgeU: gcopy.outEdges(u)) {
				Vertex v = edgeU.toVertex();
				DFSVertex vertex = this.get(v);
				if(!vertex.seen) {
					vertex.indegree--;
					if(vertex.indegree==0)
						zeroQ.add(v);
				}
				else {
					return null;
				}
				
			}
		}
		return finalList;
	}
	public static void main(String[] args) throws Exception {
		//String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";
		String string = "7 6	1 2 2	1 7 7	2 3 3	2 4 4	7 5 5	7 6 6 0";
		//String string = "7 6   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   6 7 1 0";
		//String string = "11 17	5 11 5	11 4 11	4 5 4	4 9 4	9 11 9	11 3 11	11 6 11		3 10 3	10 6 10	6 3 6	2 3 2	1 4 1	1 8 1	1 7 1	8 2 8	7 8 7	2 7 2";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

		//in1 = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		// Read graph from input
		Graph g = Graph.readDirectedGraph(in);
		LinkedList<Vertex> top1 = (LinkedList<Vertex>)DFS.topologicalOrder1(g);
		System.out.println("Topological ordering 1 :");
		if(top1!=null) {
			for(Vertex vertex : top1) {
				System.out.print(vertex+"  ");
			}
		}
		System.out.println();
		System.out.println("BEFORE REVERSING : ");
		g.printGraph(false);
		System.out.println();
		DFS d = new DFS(g);
		d.connectedComponents();
		int numcc = d.max;
		System.out.println("Number of components: " + numcc + "\nu\tcno");
		for(Vertex u: g) {
			System.out.println(u + "\t" + d.cno(u));
		}
	}
}