package parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;

import viewer.stepbystep.mxGraph;
import viewer.stepbystep.mxNode;

public class mxDotParser {
	Parser parser;
	public mxDotParser(String fileName) throws FileNotFoundException, ParseException {
		//boolean parse(Reader in) throws ParseException
		parser = new Parser();
		System.out.println("mxPARSING STARTED");
		boolean succeed = parser.parse(new FileReader(fileName));
		System.out.println("mxPARSING ENDED");
		if(!succeed) throw new ParseException();
	}
	
	public mxGraph getGraph() {
		System.out.println("mxGRAPH TRANSFORMATION STARTED");
		//Graph à renvoyer :
		mxGraph finalGraph = new mxGraph();
		//On récupère la liste des graphes générés par le parser.
		ArrayList<com.alexmerz.graphviz.objects.Graph> graphs = parser.getGraphs();
		
		//Pour chaque graphe :
		HashMap<String, mxNode> mapOfNodes = new HashMap<>();
		System.out.println("Le fichier dot contient "+graphs.size()+" graphes");
		for(com.alexmerz.graphviz.objects.Graph graph : graphs) {
			//On récupère tous les noeuds/arcs
			ArrayList<com.alexmerz.graphviz.objects.Node> nodes = graph.getNodes(false); //Pas de sous-graphe considéré.
			ArrayList<com.alexmerz.graphviz.objects.Edge> edges = graph.getEdges();
			//On transforme chaque noeud obtenu en Node et on le met dans une hashmap (id, value)
			System.out.println("number of nodes : "+nodes.size());
			System.out.println("number of edges : "+edges.size());
			for(com.alexmerz.graphviz.objects.Node node : nodes) {
				String label = node.getId().getId();
				mxNode n = new mxNode(label);
				mapOfNodes.put((String)n.getValue(), n);
				System.out.println("added node "+n.getValue());
			}
			//On relie nos Node selon les arcs obtenus avec le parser
			for(com.alexmerz.graphviz.objects.Edge edge : edges) {
				String src = edge.getSource().getNode().getId().getId();
				String dest = edge.getTarget().getNode().getId().getId();
				
				mxNode srcN = mapOfNodes.get(src);
				if(srcN==null)System.out.println("src is null");
				mxNode destN = mapOfNodes.get(dest);
				if(destN==null)System.out.println("dest is null");
				srcN.connect(destN);
				System.out.println("created edge between "+srcN.getValue()+" and "+destN.getValue());
			}
			//On détermine alors les sources et les sinks de notre Graph
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArrayList<mxNode> listOfNodes = new ArrayList(mapOfNodes.values());
			ArrayList<mxNode> listOfSources = new ArrayList<>();
			ArrayList<mxNode> listOfSinks = new ArrayList<>();
			for(mxNode node : listOfNodes) {
				if(node.getParents().isEmpty()) {
					listOfSources.add(node);
					System.out.println(node.getValue()+" is a source.");
				}
				if(node.getChildren().isEmpty()) {
					listOfSinks.add(node);
					System.out.println(node.getValue()+" is a sink.");
				}
			}
			//On crée alors notre graph et on le compose en parallèle au graph final
			mxGraph interGraph = new mxGraph(listOfSources, listOfSinks);
			finalGraph.parallelPlugIn(interGraph);
		}
		System.out.println("mxGRAPH TRANSFORMATION ENDED");
		return finalGraph;
	}

}
