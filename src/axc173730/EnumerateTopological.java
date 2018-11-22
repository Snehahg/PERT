/* Starter code for enumerating topological orders of a DAG
 * @author
 */

package axc173730;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import axc173730.DFS.DFSVertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
	boolean print;  // Set to true to print array in visit
	long count;      // Number of permutations or combinations visited
	Selector sel;
	Vertex source;
	Vertex target;
	Vertex[] arr;
	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();
		target = g.getVertex(g.size());
		source = g.getVertex(1);
		arr = new Vertex[this.g.size()];
	}

	static class EnumVertex implements Factory {
		EnumVertex() { }
		public EnumVertex make(Vertex u) { return new EnumVertex();	}
	}

	class Selector extends Enumerate.Approver<Vertex> {
		Stack<Vertex> stack = new Stack<>();
		@Override
		public boolean select(Vertex u) {
			if(stack.isEmpty()) {
				if(u.equals(source)) {
					stack.push(source);
					return true;
				}
				else {
					return false;
				}
			}else {
				Vertex head = stack.peek();
				for(Edge edge : g.outEdges(head)) {
					if(edge.otherEnd(head).equals(u)) {
						stack.push(u);
						return true;
					}
					else {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public void unselect(Vertex u) {
			if(!stack.isEmpty()) {
				stack.pop();
			}
		}

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


	// To do: LP4; return the number of topological orders of g
	public long enumerateTopological(boolean flag) {
		print = flag;
		DFS dfs = new DFS(g);
		initializeDFS(dfs);
		LinkedList<Vertex> zeroList = checkZeroIndegree(dfs);
		for(Vertex source : zeroList) {
			this.source = source;
			enumerateTopological(dfs, source,0);
		}
		return count;
	}

	private void initializeDFS(DFS dfs) {
		Iterator<Vertex> git = g.iterator();
		while(git.hasNext()) {
			Vertex u = git.next();
			DFSVertex vertex = dfs.get(u);
			vertex.indegree = u.inDegree();
			
		}
		
	}

	private void enumerateTopological(DFS dfs, Vertex u, int index) {
		
		arr[index]=u;
		if(index==g.size()-1) {
			sel.visit(arr, index);
			return;
		}
		else {
			
			dfs.get(u).seen =true;
			for(Edge edge: g.outEdges(u)) {
				Vertex v = edge.otherEnd(u);
				DFSVertex vertex = dfs.get(v);
				if(!vertex.seen) {
					vertex.indegree--;
				}
			}
			LinkedList<Vertex> zeroList = checkZeroIndegree(dfs);
			for(Vertex vertex : zeroList) {
				enumerateTopological(dfs,vertex, index+1);
			}
			dfs.get(u).seen = false;
			for(Edge edge: g.outEdges(u)) {
				Vertex v = edge.otherEnd(u);
				DFSVertex vertex = dfs.get(v);
				if(!vertex.seen) {
					vertex.indegree++;
				}
			}
		}
	}
	
	private LinkedList<Vertex> checkZeroIndegree(DFS dfs){
		Iterator<Vertex> git = g.iterator();
		LinkedList<Vertex> zeroList = new LinkedList<Vertex>();
		while(git.hasNext()) {
			Vertex u = git.next();
			DFSVertex vertex = dfs.get(u);
			if(vertex.indegree==0 && !dfs.get(u).seen) {
				zeroList.add(u);
			}
			
		}
		return zeroList;
	}
	//-------------------static methods----------------------

	

	public static long countTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(false);
	}

	public static long enumerateTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		//String string = "7 6	1 2 2	1 7 7	2 3 3	2 4 4	7 5 5	7 6 6 0";
		//String string = "11 17	5 11 5	11 4 11	4 5 4	4 9 4	9 11 9	11 3 11	11 6 11		3 10 3	10 6 10	6 3 6	2 3 2	1 4 1	1 8 1	8 2 8	7 8 7	2 7 2";
		String string  = "6 7	1 2 2	1 4 4	2 3 3	4 3 3	4 5 5	3 6 6	5 6 6";
		int VERBOSE = 0;
		if(args.length > 0) { VERBOSE = 0; }
		Graph g = Graph.readDirectedGraph(new java.util.Scanner(new java.io.File(args[0])));
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
