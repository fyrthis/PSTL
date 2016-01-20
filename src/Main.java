import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Main {

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
		Node a = new Node("a",10,0);
		Node b = new Node("b",3,6);
		Node c = new Node("c",0,8);
		Node d = new Node("d",3,8);
		Node e = new Node("e",6,8);
		Node f = new Node("f",3,10);
		Node g = new Node("g",10,17);
		Node h = new Node("h",19,8);
		
		SerieParallelGraph graph = new SerieParallelGraph(b, c); //b-c
		SerieParallelGraph graph2 = new SerieParallelGraph(c, f);//c-f
		graph.seriePlugIn(graph2);								 //b-c-f
		graph2 = new SerieParallelGraph(b, d);                   //b-d
		SerieParallelGraph graph3 = new SerieParallelGraph(d, f);//d-f
		graph2.seriePlugIn(graph3); 							 //b-d-f
		graph.parallelPlugIn(graph2); 							 //b-cd-f
		graph2 = new SerieParallelGraph(b, e);					 //b-e
		graph3 = new SerieParallelGraph(e, f);					 //e-f
		graph2.seriePlugIn(graph3);								 //b-e-f
		graph.parallelPlugIn(graph2); 							 //b-cde-f
		graph2 = new SerieParallelGraph(f, g);                   //f-g
		graph.seriePlugIn(graph2); 								 //b-cde-f-g
		graph2 = new SerieParallelGraph(a, b);                   //a-b
		graph2.seriePlugIn(graph);
		graph = graph2;											 //a-b-cde-f-g
		graph2 = new SerieParallelGraph(a, h);                   //a-h
		graph3 = new SerieParallelGraph(h, g);                   //h-g
		graph2.seriePlugIn(graph3);                              //a-h-g
		graph.parallelPlugIn(graph2);                            //FINI
		
		new SimpleViewer(graph);
		
	}

}
