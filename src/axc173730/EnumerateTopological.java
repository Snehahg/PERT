/* Starter code for enumerating topological orders of a DAG
 * @author
 */

package axc173730;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;

import java.util.Iterator;
import java.util.Stack;

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
		Iterator<Vertex> it = this.g.iterator();
		Vertex v = null;
		while(it.hasNext()) {
			 v = it.next();
			if(v.inDegree()==0 && v.outDegree()!=0 && source==null) {
				source = v;
				System.out.println("Source : "+source);
			}
			if(v.outDegree()==0 && v.inDegree()!=0 && target== null) {
				target= v;
				System.out.println("target : "+target);
			}
			if(source!=null && target!=null) {
				break;
			}
		}
		if(target==null) {
			target=v;
			System.out.println("target : "+target);
		}
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
		enumerateTopological(source,0);
		return count;
	}

	private void enumerateTopological(Vertex u, int i) {
		if(sel.select(u)) {
			arr[i] = u;
		
			if(u.equals(target)) {
				sel.visit(arr, i);
			}else {
				for(Edge edge : this.g.outEdges(u)) {
					enumerateTopological(edge.otherEnd(u), i+1);
				}
			}
		}
		
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

	public static void main(String[] args) {
		//String string = "7 6	1 2 2	1 7 7	2 3 3	2 4 4	7 5 5	7 6 6 0";
		//String string = "11 17	5 11 5	11 4 11	4 5 4	4 9 4	9 11 9	11 3 11	11 6 11		3 10 3	10 6 10	6 3 6	2 3 2	1 4 1	1 8 1	8 2 8	7 8 7	2 7 2";
		String string  = "6 7	1 2 2	1 4 4	2 3 3	4 3 3	4 5 5	3 6 6	5 6 6";
		int VERBOSE = 0;
		if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
		Graph g = Graph.readDirectedGraph(new java.util.Scanner(string));
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
