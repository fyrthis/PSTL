package datastructure;

public class Main {
	
	static Node a = new Node("a");
	static Node b = new Node("b");
	static Node c = new Node("c");
	static Node d = new Node("d");
	static Node e = new Node("e");
	static Node f = new Node("f");
	static Node g = new Node("g");
	static Node h = new Node("h");
	static Node i = new Node("i");
	static Node j = new Node("j");
	static Node k = new Node("k");
	static Node l = new Node("l");
	static Node m = new Node("m");
	static Node n = new Node("n");
	static Node o = new Node("o");
	static Node p = new Node("p");

	public static void main(String[] args) {
		/****************************************
		 * Ici, on représente quelque chose :   *
		 * 
		 *               a
		 *              / \
		 *             /   \
		 *            /     \
		 *           /       \
		 *          /         \
		 *         /           \
		 *        b             \
		 *      / | \            \
		 *     c  d  e            h
		 *      \ | /            /
		 *        f             /
		 *         \           /
		 *          \         /
		 *           \       /
		 *            \     /
		 *             \   /
		 *              \ /
		 *               g
		 *****************************************/
		
		SerieParallelGraph graph;
		SerieParallelGraph tmp = GraphController.newSerieGraph("b", "c", "g");
		graph = tmp;
		tmp = GraphController.newSerieGraph("b", "d", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphController.newSerieGraph("b", "e", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphController.newSerieGraph("a", "b");
		tmp.seriePlugIn(graph);
		graph = tmp;
		tmp = GraphController.newSerieGraph("g", "h");
		graph.seriePlugIn(tmp);
		tmp = GraphController.newSerieGraph("a", "f", "h");
		graph.parallelPlugIn(tmp);
		
		SerieParallelGraph graph2;
		tmp = GraphController.newSerieGraph("i", "j", "n");
		graph2 = tmp;
		graph2.parallelPlugIn(tmp);
		tmp = GraphController.newSerieGraph("i", "k", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphController.newSerieGraph("i", "l", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphController.newSerieGraph("h", "i");
		tmp.seriePlugIn(graph2);
		graph2 = tmp;
		tmp = GraphController.newSerieGraph("n", "o");
		graph2.seriePlugIn(tmp);
		tmp = GraphController.newSerieGraph("h", "m", "o");
		graph2.parallelPlugIn(tmp);
		graph.seriePlugIn(graph2);

		
		System.out.println("le graphe a pour source/sink : ["+graph.getSource().label+","+graph.getSink().label+"]");
		new SimpleViewer(graph);
		
		
		
	}
}
