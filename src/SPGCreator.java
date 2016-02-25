import java.util.ArrayList;
import java.util.HashMap;

import datastructure.GraphNode;
import datastructure.spg.SPG;
import datastructure.spg.SPGraph;

public class SPGCreator {

	public static SPG nodeListToSPG(ArrayList<GraphNode> nodes){
		//1. starting with a list of GraphNode- detect cycle
		try{
			cycleDetection(nodes);
		}catch(Exception e){
			throw new Error("there are cycles in this graph, can't create SPG");
		}

		//2.delete edges that are transitive 
		//create edge list from node list?? needed??
		//delete edges from list
		//update nodes
		transitiveSupression(nodes);

		//3.detection of connected components for each SPGraph
		SPG spg = connectedComponnents(nodes);

		return spg;

	}

	/**
	 * Finds connected components in a graph
	 * 
	 * @param nodes
	 */
	public static SPG connectedComponnents (ArrayList<GraphNode> nodes){
		//DFS that separates different components and creates SPGraph by connected component
		for(int i=0; i< nodes.size();i++){
			//TODO
		}


		//for each component
		//create list of sources (find all nodes without parents by connected component)
		ArrayList<GraphNode> sources = new ArrayList<GraphNode>();
		//create list of sinks (find all nodes without descendant by connected component)
		ArrayList<GraphNode> sinks = new ArrayList<GraphNode>();
		for(GraphNode gn: nodes){
			if(gn.getParents().isEmpty()){
				sources.add(gn);
			}
			if(gn.getChildren().isEmpty()){
				sinks.add(gn);
			}
		}
		//create SPG
		try {
			SPGraph spGraph = new SPGraph(sources,sinks);
			//add spGraph to SPG to return
		} catch (Exception e) {
			e.printStackTrace();
		}
		//end for each
		
		//return spg;
		return null;
	}
	/**
	 * finds cycles in graph
	 * a graph with cycles is not an SP graph
	 * @param nodes
	 */
	public static void cycleDetection(ArrayList<GraphNode> nodes){

	}
	/**
	 * suppression transitive edges
	 * @param nodes
	 */
	public static void transitiveSupression(ArrayList<GraphNode> nodes){

	}


}
