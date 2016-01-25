package datastructure;

public class SerieParallelGraph {

	private Node source;
	private Node sink;
	
	public SerieParallelGraph(Node a, Node b) {
		source = a;
		sink = b;
		a.connect(b);
	}
	
	public void parallelPlugIn(SerieParallelGraph g) {
		for(Node child : g.getSource().getChildren()) {
			if(!this.getSource().getChildren().contains(child))
				this.source.getChildren().add(child);
			if(child.getParents().size() != 1) System.out.println("grave erreur");
			child.getParents().remove(0);
			child.getParents().add(this.source);
		}
		
		for(Node parent : g.getSink().getParents()) {
			if(!this.getSink().getParents().contains(parent))
				this.sink.getParents().add(parent);
			if(parent.getChildren().size() != 1) System.out.println("grave erreur");
			parent.getChildren().remove(0);
			parent.getChildren().add(this.sink);
		}
	}
	
	public void seriePlugIn(SerieParallelGraph g) {
		
			for(Node child : g.getSource().getChildren()) {
				if(!this.sink.getChildren().contains(child))
					this.sink.getChildren().add(child);
				child.getParents().remove(0);
				child.getParents().add(this.sink);
			}
		this.sink = g.getSink();
	}

	public Node getSink() {
		return sink;
	}

	public Node getSource() {
		return source;
	}
}
