package serieparallel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


import serieparallel.Graph;
import serieparallel.Node;
import viewer.stepbystep.mxGraph;
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
	public static mxGraph connectedComponnents (mxGraph spg){
		//create list of sources (find all nodes without parents by connected component)
		ArrayList<mxNode> listOfSources = new ArrayList<>();
		//create list of sinks (find all nodes without descendant by connected component)
		ArrayList<mxNode> listOfSinks = new ArrayList<>();
		//DFS that separates different components and creates SPGraph for each connected component
		mxGraph component = null;
		
		Iterator<mxNode> it = listOfSources.iterator();
		while (it.hasNext()) {
			mxNode u = it.next();
			DFScomponentVisit(u,listOfSources,listOfSinks);
			//
			spg.getSources().removeAll(listOfSources);
			spg.getSinks().removeAll(listOfSinks);

			if(component!=null){
				component.next = new mxGraph(listOfSources,listOfSinks);
			} else {
				component = new mxGraph(listOfSources,listOfSinks);
			}

			listOfSources.clear();
			listOfSinks.clear();
		}

		return component;
	}


	private static void DFScomponentVisit(mxNode u, ArrayList<mxNode> listOfSources, ArrayList<mxNode> listOfSinks) {
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

		for(mxNode v: u.getChildren()){
			DFScomponentVisit(v,listOfSources,listOfSinks);
		}
		for(mxNode v: u.getParents()){
			DFScomponentVisit(v,listOfSources,listOfSinks);
		}

	}

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
