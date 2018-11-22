/* Driver code for PERT algorithm (LP4)
 * @author
 */

// change package to your netid
package axc173730;

import rbk.Graph.Vertex;
import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	int criticalNodes;
	int criticalPath;
	public static class PERTVertex implements Factory {
		int ec;
		int lc;
		int slack;
		int duration;
		public PERTVertex(Vertex u) {
			ec=0;
			lc=0;
			slack=0;
			duration=0;
		}
		public PERTVertex make(Vertex u) { return new PERTVertex(u); }
	}

	public PERT(Graph g) {
		super(g, new PERTVertex(null));
	}

	public void setDuration(Vertex u, int d) {
		this.get(u).duration = d;
	}

	public int getDuration(Vertex u) {
		return get(u).duration;
	}
	public boolean pert() {
		int criticalNodes = 0;
		addSourceAndTarget(g);
		LinkedList<Vertex> topOrder = (LinkedList<Vertex>) DFS.topologicalOrder1(g);
		if(topOrder==null) {
			System.out.println("here");
			return true;
		}
		
		for(Vertex u : topOrder) {
			for(Edge edge: g.outEdges(u)) {
				Vertex v = edge.otherEnd(u);
				int ec = getDuration(v)+ec(u);
				if(ec > ec(v)) {
					setEC(v, ec);
				}
			}
		}
		Vertex t = g.getVertex(g.size());
		int maxTime = ec(t);
		for(Vertex u : g) {
			setLC(u, maxTime);
		}
		Iterator<Vertex> it = topOrder.descendingIterator();
		while(it.hasNext()) {
			Vertex u = it.next();
			for(Edge edge : g.outEdges(u)) {
				Vertex v = edge.otherEnd(u);
				int lc = lc(v)-getDuration(v);
				if(lc<lc(u)) {
					setLC(u, lc);
				}
			}
			int slack = lc(u)-ec(u);
			setSlack(u, slack);
			if(slack==0) {
				criticalNodes++;
			}
		}
		setCriticalNode(criticalNodes);
		setCriticalPath(ec(t));
		return false;
	}
	public int ec(Vertex u) {
		return get(u).ec;
	}

	public void setEC(Vertex u, int ec) {
		get(u).ec = ec;
	}
	public int lc(Vertex u) {
		return get(u).lc;
	}

	public void setLC(Vertex u, int lc) {
		get(u).lc = lc;
	}
	public int slack(Vertex u) {
		return get(u).slack;
	}

	public void setSlack(Vertex u, int slack) {
		get(u).slack = slack;
	}
	public int criticalPath() {
		return criticalPath;
	}

	public void setCriticalPath(int cp) {
		criticalPath = cp;
	}
	public boolean critical(Vertex u) {
		return slack(u)==0;
	}

	public int numCritical() {
		return criticalNodes;
	}

	public void setCriticalNode(int cn) {
		criticalNodes = cn;
	}

	// setDuration(u, duration[u.getIndex()]);
	public static PERT pert(Graph g, int[] duration) {

		PERT p = new PERT(g);
		for(int i=1; i<=g.size(); i++) {
			p.setDuration(g.getVertex(i), duration[i-1]);		
		}
		p.pert();
		return p;
	}

	private void addSourceAndTarget(Graph g) {
		
		Vertex s = g.getVertex(1);
		Vertex t = g.getVertex(g.size());
		int m = g.edgeSize();
		for(int i=2; i<g.size(); i++) {
			g.addEdge(s, g.getVertex(i), 1, ++m);
			g.addEdge(g.getVertex(i), t, 1, ++m);
		}
	}

//	public static void main(String[] args) throws Exception {
//		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
//		Scanner in;
//		// If there is a command line argument, use it as file from which
//		// input is read, otherwise use input from string.
//		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
//		Graph g = Graph.readDirectedGraph(in);
//		g.printGraph(false);
//
//		int arr[] = {1,2,3,4,5,6,7,8,9,10,11};
//		PERT p = PERT.pert(g, arr);
////		for(Vertex u: g) {
////			p.setDuration(u, in.nextInt());
////		}
//		// Run PERT algorithm.  Returns null if g is not a DAG
//		if(p.pert()) {
//			System.out.println("Invalid graph: not a DAG");
//		} else {
//			System.out.println("Number of critical vertices: " + p.numCritical());
//			System.out.println("u\tEC\tLC\tSlack\tCritical");
//			for(Vertex u: g) {
//				System.out.println(u +"\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
//			}
//		}
//	}
}