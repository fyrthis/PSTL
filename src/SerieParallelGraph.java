import java.util.ArrayList;

public class SerieParallelGraph {

	private Node source;
	private Node sink;
	
	public SerieParallelGraph(Node a, Node b) {
		source = a;
		sink = b;
		a.connect(b);
	}
	
	public void parallelPlugIn(SerieParallelGraph g) {
		for(Node child : g.getSource().getChildren())
			if(!g.getSource().getChildren().contains(child))
				this.source.getChildren().add(child);
		for(Node parent : g.getSink().getParents())
			if(!g.getSink().getParents().contains(parent))
				this.sink.getParents().add(parent);
	}
	
	public void seriePlugIn(SerieParallelGraph g) {
		for(Node child : g.getSource().getChildren())
			this.sink.getParents().add(child);
		this.sink = g.getSink();
	}

	public Node getSink() {
		return sink;
	}

	public Node getSource() {
		return source;
	}
}
