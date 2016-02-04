package viewer.stepbystep;

import controler.GraphControler;
import datastructure.graph.SerieParallelGraph;

public class mxGraphInstance {

	public static mxSerieParallelGraph one() {
		mxSerieParallelGraph graph;
		mxSerieParallelGraph tmp = mxNodeControler.newSerieGraph("b", "c", "g");
		graph = tmp;
		tmp = mxNodeControler.newSerieGraph("b", "d", "g");
		graph.parallelPlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("b", "e", "g");
		graph.parallelPlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("a", "b");
		tmp.seriePlugIn(graph);
		graph = tmp;
		tmp = mxNodeControler.newSerieGraph("g", "h");
		graph.seriePlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("a", "f", "h");
		graph.parallelPlugIn(tmp);
		
		mxSerieParallelGraph graph2;
		tmp = mxNodeControler.newSerieGraph("i", "j", "n");
		graph2 = tmp;
		graph2.parallelPlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("i", "k", "n");
		graph2.parallelPlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("i", "l", "n");
		graph2.parallelPlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("h", "i");
		tmp.seriePlugIn(graph2);
		graph2 = tmp;
		tmp = mxNodeControler.newSerieGraph("n", "o");
		graph2.seriePlugIn(tmp);
		tmp = mxNodeControler.newSerieGraph("h", "m", "o");
		graph2.parallelPlugIn(tmp);
		graph.seriePlugIn(graph2);
		
		tmp = mxNodeControler.newSerieGraph("a", "p", "q", "r", "o");
		graph.parallelPlugIn(tmp);
		return graph;
	}
}
