package parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;

public class DotParser {
	Parser parser;
	public DotParser(String fileName) throws FileNotFoundException, ParseException {
		//boolean parse(Reader in) throws ParseException
		parser = new Parser();
		System.out.println("PARSING STARTED");
		boolean succeed = parser.parse(new FileReader(fileName));
		System.out.println("PARSING ENDED");
		if(!succeed) throw new ParseException();
	}
	
	public serieparallel.Graph getGraph() {
		System.out.println("GRAPH TRANSFORMATION STARTED");
		//Graph à renvoyer :
		serieparallel.Graph finalGraph = new serieparallel.Graph();
		//On récupère la liste des graphes générés par le parser.
		ArrayList<com.alexmerz.graphviz.objects.Graph> graphs = parser.getGraphs();
		
		//Pour chaque graphe :
		HashMap<String, serieparallel.Node<?>> mapOfNodes = new HashMap<>();
		for(com.alexmerz.graphviz.objects.Graph graph : graphs) {
			//On récupère tous les noeuds/arcs
			ArrayList<com.alexmerz.graphviz.objects.Node> nodes = graph.getNodes(false); //Pas de sous-graphe considéré.
			ArrayList<com.alexmerz.graphviz.objects.Edge> edges = graph.getEdges();
			//On transforme chaque noeud obtenu en Node et on le met dans une hashmap (id, value)
			for(com.alexmerz.graphviz.objects.Node node : nodes) {
				String label = node.getId().getLabel();
				@SuppressWarnings({ "unchecked", "rawtypes" })
				serieparallel.Node<?> n = new serieparallel.Node(node.getId().getId(), label);
				mapOfNodes.put(n.getId(), n);
			}
			//On relie nos Node selon les arcs obtenus avec le parser
			for(com.alexmerz.graphviz.objects.Edge edge : edges) {
				String src = edge.getSource().getNode().getId().getId();
				String dest = edge.getTarget().getNode().getId().getId();
				mapOfNodes.get(src).connect(mapOfNodes.get(dest));
			}
			//On détermine alors les sources et les sinks de notre Graph
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArrayList<serieparallel.Node<?>> listOfNodes = new ArrayList(mapOfNodes.values());
			ArrayList<serieparallel.Node<?>> listOfSources = new ArrayList<>();
			ArrayList<serieparallel.Node<?>> listOfSinks = new ArrayList<>();
			for(serieparallel.Node<?> node : listOfNodes) {
				if(node.getParents().isEmpty()) listOfSources.add(node);
				if(node.getChildren().isEmpty()) listOfSinks.add(node);
			}
			//On crée alors notre graph et on le compose en parallèle au graph final
			serieparallel.Graph interGraph = new serieparallel.Graph(listOfSources, listOfSinks);
			//TODO : test si interGraph est connexe, sinon faire autant de sous-graphes que nécessaire.
			//TODO : pour chaque graph obtenu, faire un test de cycle
			//Si on trouve un cycle : exception
			//Sinon :
			finalGraph.parallelPlugIn(interGraph);
		}
		System.out.println("GRAPH TRANSFORMATION ENDED");
		return finalGraph;
	}

}
