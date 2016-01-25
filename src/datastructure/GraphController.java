package datastructure;

public class GraphController {
	
	//Make Serie Graph parallel
	public static SerieParallelGraph newSerieGraph(String a, String b, String... c) {
		Node source = new Node(a);
		Node sink = new Node(b);
		SerieParallelGraph g = new SerieParallelGraph(source, sink);
		
		if(c.length>0)
			for(String label : c) {
				Node r = g.getSink();
				Node k = new Node(label);
				g.seriePlugIn(new SerieParallelGraph(r, k));
			}
		
		return g;
	}

}
