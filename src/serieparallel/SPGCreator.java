package serieparallel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
	public static Graph connectedComponnents (Graph spg){
		//create list of sources (find all nodes without parents by connected component)
		ArrayList<Node<?>> listOfSources = new ArrayList<>();
		//create list of sinks (find all nodes without descendant by connected component)
		ArrayList<Node<?>> listOfSinks = new ArrayList<>();
		//DFS that separates different components and creates SPGraph for each connected component
		Graph component = null;

		Iterator<Node<?>> it = listOfSources.iterator();
		while (it.hasNext()) {
			Node<?> u = it.next();
			DFScomponentVisit(u,listOfSources,listOfSinks);
			//
			spg.getSources().removeAll(listOfSources);
			spg.getSinks().removeAll(listOfSinks);

			if(component!=null){
				component.next = new Graph(listOfSources,listOfSinks);
			} else {
				component = new Graph(listOfSources,listOfSinks);
			}

			listOfSources.clear();
			listOfSinks.clear();
		}

		return component;
	}


	private static void DFScomponentVisit(Node<?> u, ArrayList<Node<?>> listOfSources, ArrayList<Node<?>> listOfSinks) {
		//mark U
		if(u.getDetectionCycle()==2){
			return;
		}
		u.setDetectionCycle(2);
		if(u.getChildren().isEmpty()){
			listOfSinks.add(u);
		}
		if(u.getParents().isEmpty()){
			listOfSources.add(u);
		}

		for(Node<?> v: u.getChildren()){
			DFScomponentVisit(v,listOfSources,listOfSinks);
		}
		for(Node<?> v: u.getParents()){
			DFScomponentVisit(v,listOfSources,listOfSinks);
		}

	}

	/**
	 * finds cycles in graph
	 * a graph with cycles is not an SP graph
	 * @param nodes
	 */
	public static boolean cycleDetection(List<Node<?>> sources){
		System.out.println("enter cycle detection");

		for(Node<?> v: sources){
			DFSvisit(v);//this function throws error if cycle detected
		}
		//no cycle has been detceted
		return false;
	}

	public static boolean DFSvisit(Node<?> u){
		//color u-> grey, grey is -1
		if(u.getDetectionCycle()==1){
			return true;
		}
		u.setDetectionCycle(-1);
		System.out.println("start: "+u.getDetectionCycle());
		for(Node<?> v: u.getChildren()){
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
	/**
	 * Algorithm that detects if the graph is an SPGraph
	 * @param start
	 */
	public static void DetectionSPGraph(Node<?> start, Stack<Node<?>> forks ){
		//black =2 end of treatment 
		//grey = 1 in treatment
		//white = 0 before treatment
		if (start.getColor()==2){
			return;
		}
		if (start.getColor()==1){
			//a cycle is detected= problem
			throw new Error();
		}
		//checks if fork
		if(start.getChildren().size()>1){
			//push start to fork stack
			forks.push(start);
		}

		//checks if join
		if(start.getParents().size()>1){
			//stock couple <fork, join>, not yet implemented, with poped fork
			Node<?> n= forks.pop();
			/*
			 * this version is only compatible with a basic SP where each fork has one join
			 */
			if(n==null){
				//no fork is found for the join in treatment
				throw new Error();
			}
		}

		for(Node<?> child: start.getChildren()){
			DetectionSPGraph(child, forks);
		}

		// end of treatment
		start.setColor(2);

	}


}
