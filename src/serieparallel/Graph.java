package serieparallel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Graph implements Iterable<Graph> {

	public Graph next;
	List<Node<?>> sources;
	List<Node<?>> sinks;
	
	public Graph() {
		//Create an empty graph
		next=null;
		sources = new ArrayList<>();
		sinks = new ArrayList<>();
	}
	
	public Graph(List<Node<?>> sources, List<Node<?>> sinks) {
		next = null;
		this.sources = sources;
		this.sinks=sinks;
	}
	
	public Graph getNext() {
		return next;
	}

	public void setNext(Graph next) {
		this.next = next;
	}

	public List<Node<?>> getSources() {
		return sources;
	}

	public void setSources(List<Node<?>> sources) {
		this.sources = sources;
	}

	public List<Node<?>> getSinks() {
		return sinks;
	}

	public void setSinks(List<Node<?>> sinks) {
		this.sinks = sinks;
	}

	public void parallelPlugIn(Graph... graphs) {
		System.out.println(this.getSources().size()+"..."+graphs[0].getSources().size());
		if(graphs==null||graphs.length==0) return;
		Iterator<Graph> ite = iterator();
		Graph queue = this;
		while(ite.hasNext()) {
			 queue = ite.next();
			 System.out.println("hola que tal");
		}
		for(Graph g : graphs) {
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
		System.out.println(this.getSources().size()+"..."+graphs[0].getSources().size());
	}
	//TODO : Un graphe vide, ou composé d'un seul noeud, peut-il causer problème ? We must think about it :)
	public void seriePlugIn(Graph graph) {
		//CONNECT THIS AND mxGraph WITH AN INTERMEDIATE mxNode
				//Get all sinks of this
				Iterator<Graph> ite = iterator();
				HashSet<Node<?>> sinkSet = new HashSet<>(this.sinks);
				while(ite.hasNext()) 
					 sinkSet.addAll(ite.next().sinks);
				//Get all sources of mxGraph
				ite = graph.iterator();
				HashSet<Node<?>> sourceSet = new HashSet<>(graph.sources);
				while(ite.hasNext()) 
					sourceSet.addAll(ite.next().sources);
				//New inter-mxNode between this.sinks and mxGraph.sources
				Node<?> inter = new Node<String>("inter", "inter");
				for(Node<?> e : sinkSet)
					e.connect(inter);
				for(Node<?> e : sourceSet)
					inter.connect(e);
				
				//UPDATE SOURCES, SINKS AND NEXT OF THIS
				//Get all next sources of this and add them to this.sources
				ite = iterator();
				HashSet<Node<?>> set = new HashSet<>();
				while(ite.hasNext()) 
					set.addAll(ite.next().sources);
				sources.addAll(set);
				//Remove this.sinks, get all sinks of mxGraph and add them to this.sinks
				ite = graph.iterator();
				set = new HashSet<>();
				while(ite.hasNext()) 
					set.addAll(ite.next().sinks);
				this.sinks.clear();
				this.sinks.addAll(set);
				
				//SET NEXT TO NULL
				this.next=null;
	}

	@Override
	public Iterator<Graph> iterator() {
		return new GraphIterator();
	}

	private class GraphIterator implements Iterator<Graph> {
		private Graph cursor;

		public GraphIterator() {
			this.cursor = Graph.this;
		}

		public boolean hasNext() {
			return Graph.this.next != null;
		}

		public Graph next() {
			if(this.hasNext()) {
				Graph current = cursor;
				cursor = Graph.this.next;
				return current;
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		
	}
	
	public boolean isEmpty() {
		return (sources.isEmpty() && sinks.isEmpty());
	}

}
