package viewer.stepbystep;

public class mxNodeControler {
	
	//Make Serie Graph parallel
	public static mxSerieParallelGraph newSerieGraph(String a, String b, String... c) {
		mxNode source = new mxNode(a);
		mxNode sink = new mxNode(b);
		mxSerieParallelGraph g = new mxSerieParallelGraph(source, sink);
		
		if(c.length>0)
			for(String label : c) {
				mxNode r = g.getSink();
				mxNode k = new mxNode(label);
				g.seriePlugIn(new mxSerieParallelGraph(r, k));
			}
		
		return g;
	}

}