package serieparallel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


import serieparallel.Graph;
import serieparallel.Node;
import viewer.stepbystep.mxNode;

public class SPGCreator {

	public static Graph createSPG(Graph spg){

		List<Node<?>> sources = spg.getSources();
		//1.detect cycle in spg
		/*
		if(!cycleDetection(sources)){
			throw new Error("there are cycles in this graph, can't create SPG");
		}
		 */

		//2.delete edges that are transitive 
		//create edge list from node list?? needed??
		//delete edges from list
		//update nodes
		//transitiveSupression(sources);

		//3.detection of connected components for each SPGraph
		//SPG spg = connectedComponnents(sources);

		return spg;

	}

	/**
	 * Finds connected components in a graph
	 * 
	 * @param nodes
	 */
	/**
	public static Graph connectedComponnents (Graph spg){
		spg.getSources();
		spg.getSinks();
		//DFS that separates different components and creates SPGraph by connected component
		for(int i=0; i< nodes.size();i++){
			//TODO
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		//for each component
			//create list of sources (find all nodes without parents by connected component)
			ArrayList<serieparallel.Node<?>> listOfSources = new ArrayList<>();
			//create list of sinks (find all nodes without descendant by connected component)
			ArrayList<serieparallel.Node<?>> listOfSinks = new ArrayList<>();
			for(serieparallel.Node<?> node : nodes) {
				if(node.getParents().isEmpty()) listOfSources.add(node);
				if(node.getChildren().isEmpty()) listOfSinks.add(node);
			}

			Stack<Node<?>> s = new Stack<>();
			ArrayList<Node<?>> marked = new ArrayList<Node<?>>();
			//DFS for each source in nodes
			for(Node<?> v: sources){
				marked.clear();
				s.push(v);
				while(!s.isEmpty()){
					v = s.pop();
					if (!marked.contains(v)){
						marked.add(v);
						for(Node<?> child: v.getChildren()){
							s.push(child);
						}
					} else{

					}
				}
			}

		//end for each

		//return spg;
		return null;
	}
	 **/
	/**
	 * finds cycles in graph
	 * a graph with cycles is not an SP graph
	 * @param nodes
	 */
	public static boolean cycleDetection(List<mxNode> sources){
		System.out.println("enter cycle detection");

		for(mxNode v: sources){
			DFSvisit(v);//this function throws error if cycle detected
		}
		
		//no cycle has been detceted
		return false;
	}
	
	public static boolean DFSvisit(mxNode u){
		//color u-> grey, grey is -1
		if(u.getDetectionCycle()==1){
			return true;
		}
		u.setDetectionCycle(-1);
		System.out.println("start: "+u.getDetectionCycle());
		for(mxNode v: u.getChildren()){
			System.out.println("child: "+v.getDetectionCycle());
			//white is 0
			if(v.getDetectionCycle()== 0){
				DFSvisit(v);
			}
			if(v.getDetectionCycle() == -1){
				throw new Error("cycle detected");
			}
		}
		//color u-> black, black is 1
		u.setDetectionCycle(1);
		System.out.println("end: "+u.getDetectionCycle());
		return true;
	}
	/**
	 * suppression transitive edges
	 * @param nodes
	 */
	public static void transitiveSupression(ArrayList<Node<?>> nodes){

	}


}
