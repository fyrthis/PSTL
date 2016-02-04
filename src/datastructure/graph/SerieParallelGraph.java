package datastructure.graph;

import datastructure.GraphNode;

public class SerieParallelGraph {

	private GraphNode source;
	private GraphNode sink;
	
	public SerieParallelGraph(GraphNode a, GraphNode b) {
		source = a;
		sink = b;
		a.connect(b);
	}
	
	public void parallelPlugIn(SerieParallelGraph g) {
		for(GraphNode child : g.getSource().getChildren()) {
			if(!this.getSource().getChildren().contains(child))
				this.source.getChildren().add(child);
			if(child.getParents().size() != 1) System.out.println("grave erreur");
			child.getParents().remove(0);
			child.getParents().add(this.source);
		}
		
		for(GraphNode parent : g.getSink().getParents()) {
			if(!this.getSink().getParents().contains(parent))
				this.sink.getParents().add(parent);
			if(parent.getChildren().size() != 1) System.out.println("grave erreur");
			parent.getChildren().remove(0);
			parent.getChildren().add(this.sink);
		}
	}
	
	public void seriePlugIn(SerieParallelGraph g) {
		
			for(GraphNode child : g.getSource().getChildren()) {
				if(!this.sink.getChildren().contains(child))
					this.sink.getChildren().add(child);
				child.getParents().remove(0);
				child.getParents().add(this.sink);
			}
		this.sink = g.getSink();
	}

	public GraphNode getSink() {
		return sink;
	}

	public GraphNode getSource() {
		return source;
	}
}
