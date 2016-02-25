package viewer.stepbystep;

public class mxSerieParallelGraph {

	public mxNode source;
	public mxNode sink;
	
	public mxSerieParallelGraph(mxNode a, mxNode b) {
		source = a;
		sink = b;
		a.connect(b);
	}
	
	public mxSerieParallelGraph() {
		// TODO Auto-generated constructor stub
	}

	public void parallelPlugIn(mxSerieParallelGraph g) {
		for(mxNode child : g.getSource().getChildren()) {
			if(!this.getSource().getChildren().contains(child))
				this.source.getChildren().add(child);
			if(child.getParents().size() != 1) System.out.println("grave erreur");
			child.getParents().remove(0);
			child.getParents().add(this.source);
		}
		
		for(mxNode parent : g.getSink().getParents()) {
			if(!this.getSink().getParents().contains(parent))
				this.sink.getParents().add(parent);
			if(parent.getChildren().size() != 1) System.out.println("grave erreur");
			parent.getChildren().remove(0);
			parent.getChildren().add(this.sink);
		}
	}
	
	public void seriePlugIn(mxSerieParallelGraph g) {
		
			for(mxNode child : g.getSource().getChildren()) {
				if(!this.sink.getChildren().contains(child))
					this.sink.getChildren().add(child);
				child.getParents().remove(0);
				child.getParents().add(this.sink);
			}
		this.sink = g.getSink();
	}

	public mxNode getSink() {
		return sink;
	}

	public mxNode getSource() {
		return source;
	}
}
