package viewer.stepbystep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class mxGraph implements Iterable<mxGraph> {
	public List<mxNode> getSources() {
		return sources;
	}

	public List<mxNode> getSinks() {
		return sinks;
	}

	public mxGraph next;
	List<mxNode> sources;
	List<mxNode> sinks;
	
	public mxGraph() {
		//Create an empty mxGraph
		next=null;
		sources = new ArrayList<>();
		sinks = new ArrayList<>();
	}
	
	public mxGraph(List<mxNode> sources, List<mxNode> sinks) {
		next = null;
		this.sources = sources;
		this.sinks=sinks;
	}

	public void parallelPlugIn(mxGraph... mxGraphs) {
		System.out.println(this.getSources().size()+"..."+mxGraphs[0].getSources().size());
		if(mxGraphs==null||mxGraphs.length==0) return;
		Iterator<mxGraph> ite = iterator();
		mxGraph queue = this;
		while(ite.hasNext()) {
			 queue = ite.next();
			 System.out.println("hola que tal");
		}
		for(mxGraph g : mxGraphs) {
			System.out.println("g :"+g.getSources().size());
			if(queue.isEmpty()) {
				System.out.println("queue is empty :)");
				queue.sources = g.getSources();
				queue.sinks = g.getSinks();
			}else if(g.isEmpty()) {
					/*DO NOTHING*/
			}else {
				queue.next = g;
				queue = queue.next;
			}
		}
		System.out.println(this.getSources().size()+"..."+mxGraphs[0].getSources().size());
	}
	public boolean isEmpty() {
		return (sources.isEmpty() && sinks.isEmpty());
	}

	//TODO : Un mxGraphe vide, ou composé d'un seul noeud, peut-il causer problème ? We must think about it :)
	public void seriePlugIn(mxGraph mxGraph) {
		//CONNECT THIS AND mxGraph WITH AN INTERMEDIATE mxNode
		//Get all sinks of this
		Iterator<mxGraph> ite = iterator();
		HashSet<mxNode> sinkSet = new HashSet<>(this.sinks);
		while(ite.hasNext()) 
			 sinkSet.addAll(ite.next().sinks);
		//Get all sources of mxGraph
		ite = mxGraph.iterator();
		HashSet<mxNode> sourceSet = new HashSet<>(mxGraph.sources);
		while(ite.hasNext()) 
			sourceSet.addAll(ite.next().sources);
		//New inter-mxNode between this.sinks and mxGraph.sources
		mxNode inter = new mxNode("inter");
		for(mxNode e : sinkSet)
			e.connect(inter);
		for(mxNode e : sourceSet)
			inter.connect(e);
		
		//UPDATE SOURCES, SINKS AND NEXT OF THIS
		//Get all next sources of this and add them to this.sources
		ite = iterator();
		HashSet<mxNode> set = new HashSet<>();
		while(ite.hasNext()) 
			set.addAll(ite.next().sources);
		sources.addAll(set);
		//Remove this.sinks, get all sinks of mxGraph and add them to this.sinks
		ite = mxGraph.iterator();
		set = new HashSet<>();
		while(ite.hasNext()) 
			set.addAll(ite.next().sinks);
		this.sinks.clear();
		this.sinks.addAll(set);
		
		//SET NEXT TO NULL
		this.next=null;
	}

	@Override
	public Iterator<mxGraph> iterator() {
		return new mxGraphIterator();
	}

	private class mxGraphIterator implements Iterator<mxGraph> {
		private mxGraph cursor;

		public mxGraphIterator() {
			this.cursor = mxGraph.this;
		}

		public boolean hasNext() {
			return mxGraph.this.next != null;
		}

		public mxGraph next() {
			if(this.hasNext()) {
				mxGraph current = cursor;
				cursor = mxGraph.this.next;
				return current;
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
