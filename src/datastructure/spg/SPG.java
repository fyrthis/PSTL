package datastructure.spg;

import java.util.ArrayList;

import datastructure.GraphNode;

public class SPG {
	ArrayList<SPGraph> componnentsInGraph;
	
	public SPG(ArrayList<SPGraph> graphs){
		this.componnentsInGraph = graphs;
	}

	public ArrayList<SPGraph> getComponnentsInGraph() {
		return componnentsInGraph;
	}
	
	/**
	 * parallel plugin with one component SPGraph
	 * @param SPGraph spGraph2
	 */
	public void parallelPlugIn(SPGraph spGraph2) {
		if(spGraph2.isEmpty()) {
			return;
		}
		this.componnentsInGraph.add(spGraph2);
	}
	/**
	 * parallel plugin with another SPG with a list of SPGraph that are added to the
	 * actual structure
	 * @param SPG spg2
	 */
	public void parallelPlugIn(SPG spg2) {
		if(spg2.isEmpty()) {
			return;
		}
		for(SPGraph spgraph: spg2.getComponnentsInGraph()){
			this.componnentsInGraph.add(spgraph);
		}
	}
	/**
	 * when componnentsInGraph is empty return true, 
	 * check every SPGraph if empty
	 * @return
	 */
	public boolean isEmpty() {
		int empty= 0;
		if(this.componnentsInGraph.isEmpty()) {
			return true;
		}
		for(SPGraph spg: componnentsInGraph){
			if(!spg.isEmpty()){
				empty++;
			}
		}
		if(empty>0){
			return false;
		}
		return true;
	}
	
	
	
}
