package datastructure.spg;

import java.util.ArrayList;
import java.util.List;

import datastructure.GraphNode;

public class SPG {
	List<GraphNode> sources;
	List<GraphNode> sinks;
	
	public SPG() {
		sources = new ArrayList<>();
		sinks = new ArrayList<>();
	}
	
	public SPG(GraphNode g) {
		sources = new ArrayList<>();
		sinks = new ArrayList<>();
		sources.add(g);
		sinks.add(g);
	}
	
	public SPG(List<GraphNode> sources, List<GraphNode> sinks) throws Exception {
		sources = new ArrayList<>(sources);
		sinks = new ArrayList<>(sinks);
		if(!this.isSPG()) {
			throw new Exception("Error : was not spg");
		}
	}

	public void seriePlugIn(SPG spg2) {
		GraphNode e = new GraphNode("plugIn");
		for(GraphNode sink : sinks) {
			sink.connect(e);
		}
		for(GraphNode source : spg2.getSources()) {
			e.connect(source);
		}
		if(this.isEmpty()) {
			sources.add(e);
		}
		if(spg2.isEmpty()) {
			sinks.add(e);
		} else {
			sinks = new ArrayList<>(spg2.getSinks());
		}
	}
	
	public void parallelPlugIn(SPG spg2) {
		if(spg2.isEmpty()) {
			return;
		}
		for(GraphNode source : spg2.getSources()) {
			sources.add(source);
		}
		for(GraphNode sink : spg2.getSinks()) {
			sinks.add(sink);
		}
	}

	public List<GraphNode> getSources() {
		return sources;
	}

	public void setSources(List<GraphNode> sources) {
		this.sources = sources;
	}

	public List<GraphNode> getSinks() {
		return sinks;
	}

	public void setSinks(List<GraphNode> sinks) {
		this.sinks = sinks;
	}
	
	public boolean isEmpty() {
		if(sources.isEmpty() && sinks.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean isSPG() {
		// TODO Auto-generated method stub
		return false;
	}
}
