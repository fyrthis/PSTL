package datastructure.graph;

import controler.GraphControler;

public class GraphInstance {

	public static SerieParallelGraph one() {
		SerieParallelGraph graph;
		SerieParallelGraph tmp = GraphControler.newSerieGraph("b", "c", "g");
		graph = tmp;
		tmp = GraphControler.newSerieGraph("b", "d", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("b", "e", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("a", "b");
		tmp.seriePlugIn(graph);
		graph = tmp;
		tmp = GraphControler.newSerieGraph("g", "h");
		graph.seriePlugIn(tmp);
		tmp = GraphControler.newSerieGraph("a", "f", "h");
		graph.parallelPlugIn(tmp);
		
		SerieParallelGraph graph2;
		tmp = GraphControler.newSerieGraph("i", "j", "n");
		graph2 = tmp;
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("i", "k", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("i", "l", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("h", "i");
		tmp.seriePlugIn(graph2);
		graph2 = tmp;
		tmp = GraphControler.newSerieGraph("n", "o");
		graph2.seriePlugIn(tmp);
		tmp = GraphControler.newSerieGraph("h", "m", "o");
		graph2.parallelPlugIn(tmp);
		graph.seriePlugIn(graph2);
		
		tmp = GraphControler.newSerieGraph("a", "p", "q", "r", "o");
		graph.parallelPlugIn(tmp);
		return graph;
	}
	
	public static SerieParallelGraph two() {
		SerieParallelGraph graph = GraphControler.newSerieGraph("source", "a", "sink");
		SerieParallelGraph tmp = GraphControler.newSerieGraph("source", "b", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "c", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "d", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "e", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "f", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "g", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "h", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "i", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "j", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "k", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "l", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "m", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "n", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "o", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "p", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "q", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "r", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "s", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "t", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "u", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "v", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "w", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "x", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "y", "sink");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("source", "z", "sink");
		graph.parallelPlugIn(tmp);
		return graph;
	}
	
	public static SerieParallelGraph three() {
		SerieParallelGraph graph;
		SerieParallelGraph tmp = GraphControler.newSerieGraph("b", "c", "g");
		graph = tmp;
		tmp = GraphControler.newSerieGraph("b", "d", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("b", "e", "g");
		graph.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("a", "b");
		tmp.seriePlugIn(graph);
		graph = tmp;
		tmp = GraphControler.newSerieGraph("g", "h");
		graph.seriePlugIn(tmp);
		tmp = GraphControler.newSerieGraph("a", "f", "h");
		graph.parallelPlugIn(tmp);
		
		SerieParallelGraph graph2;
		tmp = GraphControler.newSerieGraph("i", "j", "n");
		graph2 = tmp;
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("i", "k", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("i", "l", "n");
		graph2.parallelPlugIn(tmp);
		tmp = GraphControler.newSerieGraph("h", "i");
		tmp.seriePlugIn(graph2);
		graph2 = tmp;
		tmp = GraphControler.newSerieGraph("n", "o");
		graph2.seriePlugIn(tmp);
		tmp = GraphControler.newSerieGraph("h", "m", "o");
		graph2.parallelPlugIn(tmp);
		graph.seriePlugIn(graph2);
		
		tmp = GraphControler.newSerieGraph("a", "p", "q", "r", "o");
		graph.parallelPlugIn(tmp);
		
		tmp = GraphControler.newSerieGraph("a", "s", "t", "u", "v", "w", "o");
		graph.parallelPlugIn(tmp);
		
		tmp = GraphControler.newSerieGraph("a", "x", "y", "z", "o");
		graph.parallelPlugIn(tmp);
		
		tmp = GraphControler.newSerieGraph("a", "a1", "b1", "c1", "d1", "o");
		graph.parallelPlugIn(tmp);
		return graph;
	}
}
