package controler;

import datastructure.GraphNode;
import datastructure.graph.SerieParallelGraph;

public class GraphControler {
	
	//Make Serie Graph parallel
	public static SerieParallelGraph newSerieGraph(String a, String b, String... c) {
		GraphNode source = new GraphNode(a);
		GraphNode sink = new GraphNode(b);
		SerieParallelGraph g = new SerieParallelGraph(source, sink);
		
		if(c.length>0)
			for(String label : c) {
				GraphNode r = g.getSink();
				GraphNode k = new GraphNode(label);
				g.seriePlugIn(new SerieParallelGraph(r, k));
			}
		
		return g;
	}

}
