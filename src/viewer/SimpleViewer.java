package viewer;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import datastructure.GraphNode;
import datastructure.graph.SerieParallelGraph;
import viewer.stepbystep.mxNode;

public class SimpleViewer implements IViewer {
		//Pour JGraph
		private mxGraph graph = new mxGraph();
		private Object gParent = graph.getDefaultParent();
		private List<mxCell> vertices =  new ArrayList<>();
		
		//Pour les algos
		private int[] nextFreePlace = new int[50];
		private ArrayList<GraphNode> nodeOnYaxis[];
		private int graphDepth=0;
		private SerieParallelGraph g;
		private int step = 5;
		mxGraphComponent graphcp;
		
		
		@SuppressWarnings("unchecked")
		public SimpleViewer(SerieParallelGraph g) {
			super();
			this.g=g;
			graph.getModel().beginUpdate();
			try
			{
				//Trouver la profondeur
				findDepthMax(g.getSource(), 0);
				nodeOnYaxis = new ArrayList[graphDepth+1];
				for(int i=0;i<nodeOnYaxis.length;i++)
					nodeOnYaxis[i] = new ArrayList<GraphNode>();
				g.getSink().y=graphDepth*step;
				nodeOnYaxis[graphDepth].add(g.getSink());
				System.out.println(graphDepth);
				//Placer les pères et les ordonnées
				computeDown(g.getSource(), 0);
				System.out.println("Fin algo aller");
				//Placer les fils
				//ScomputeUp(g.getSink());

				System.out.println("Fin algo retour");
				//Puis dessiner
				draw(g.getSource(), null);
			}
			finally
			{
				graph.getModel().endUpdate();
			}

			graphcp = new mxGraphComponent(graph);
			
		}
		/**
		 * En fonction des coordonnées des noeuds, on va dessiner le graphe
		 * à l'aide de JGraph, en prenant soin de ne pas dessiner plusieurs
		 * fois les noeuds.
		 * @param node : noeud source du graphe.
		 * @param parent : initialisé à null.
		 */
		public void draw(GraphNode node, Object parent) {
			//Vertex drawing
			mxCell vertex = null;
			for(mxCell cell : vertices) { //On regarde si on a déjà dessiné un noeud ici, pour pas le dessiner 50 fois.
				if(((GraphNode)cell.getValue()).getLabel()==node.label) //On considère le label unique...
					vertex = cell;
			}
			if(vertex==null) {
				vertex = (mxCell) graph.insertVertex(gParent, null, node, node.x*4*step, node.y*4*step, 20, 20);
				vertices.add((mxCell) vertex);
			}
			//Edge drawing
			mxCell edge = null;
			for(int i = 0; i<((mxCell)vertex).getEdgeCount() ;i++){
				mxCell edgeTmp = (mxCell)vertex.getEdgeAt(i);
				if(edgeTmp.getSource()==parent && edgeTmp.getTarget()==vertex)
					edge = edgeTmp;
			}
			if(edge==null && parent!=null)
				edge = (mxCell) graph.insertEdge(parent, null, null, parent, vertex);
			//Recursive call
			if(node.getChildren()!=null)
				for(GraphNode child : node.getChildren())
					draw(child, vertex);
		}
		/**
		 * On cherche ici à effectuer tous les chemins possibles de la source 
		 * vers la sink, afin de déterminer le chemin le plus long, c-a-d
		 * la profondeur.
		 * 
		 * @param n : noeud source du graphe
		 * @param depth : profondeur courante
		 */
		private void findDepthMax(GraphNode n, int depth) {
			for(GraphNode child : n.getChildren()) {
				findDepthMax(child, depth+1);
			}
			if(graphDepth<depth) graphDepth=depth;
		}

		/**
		 * On place les abscisses pères par rapport à leurs enfants et 
		 * les ordonnées par rapport à leur chemin le plus court au noeud terminal.
		 * On parcourt donc la structure de la source vers la sink :
		 *  "algorithme à la descente"
		 * 
		 * @param n : Noeud source du graphe qu'on veut dessiner
		 * @param depthLevel : initialisé à zéro
		 * @return profondeur courante
		 */
		private int computeDown(GraphNode n, int depthLevel) {
			int lengthOfPath=depthLevel;

			for(GraphNode child : n.getChildren()) {
				lengthOfPath = computeDown(child, depthLevel+1);
				int placeY = (graphDepth/lengthOfPath)*depthLevel*step;
				if(placeY > n.y) {
					if(nodeOnYaxis[n.y/step].contains(n))
						nodeOnYaxis[n.y/step].remove(n);
					nodeOnYaxis[placeY/step].add(n);
					n.y = placeY;
					System.out.println("ajout "+n.label+" en tab:"+placeY/step);
				}
			}
			

			if (n.getChildren().isEmpty() || n.getChildren().size()==1) {
				n.x = nextFreePlace[n.y/step]*step;
				nextFreePlace[n.y/step]++;
			} else {
				int placeX = ((n.getChildren().get(0).x + n.getChildren().get(n.getChildren().size()-1).x) / 2);
				if(placeX > n.x) {
					n.x = placeX;
					nextFreePlace[n.y/step]++;
				}
//				n.x = Math.max(((n.getChildren().get(0).x + n.getChildren().get(n.getChildren().size()-1).x) / 2), nextFreePlace[n.y/step]*step);
//				nextFreePlace[n.y/step]++;
			}

			return lengthOfPath;
			
		}
		
		/**
		 * On place les abscisses fils par rapport à leurs parents.
		 * On parcourt donc la structure de la sink vers la source :
		 *  "algorithme à la (re)montée"
		 * 
		 * @param n : Noeud sink du graphe qu'on veut dessiner
		 * @param depth : initialisé à la profondeur du graphe
		 */
		private void computeUp(GraphNode n) {
			if(n==g.getSource()) return;
			
			for(GraphNode parent : n.getParents())
				computeUp(parent);

			if (n.getParents().isEmpty() || (n.getParents().size()==1) ) {
				//DO NOTHING
			} else {
				int newXMaybe = ((n.getParents().get(0).x + n.getParents().get(n.getParents().size()-1).x) / 2);
				if(newXMaybe < n.x) {
					System.out.println("newXMaybe = "+newXMaybe);
					System.out.println("n.x = "+n.x);
					/*
					HashSet<Node> descendants = n.getDescendants(); //Erreur : On est susceptible de décaler des noeuds qui ne sont pas des descendants.
					// Pour corriger, faire un tableu de taille depthMax et y ranger au fur et à mesure les élements
					for(Node e : descendants) {
						e.x += (newXMaybe - n.x);
					}
					*/
					int offset = (newXMaybe - n.x);
					System.out.println("décalage engendré par "+n.label+" : "+(n.y/step)+"   "+graphDepth);
					for(int i = (n.y/step+1); i<graphDepth+1; i++) {
						System.out.println("size of : "+nodeOnYaxis[i].size());
						//Peut-être qu'en regardant l'espace qui le sépare du prochain, on pourrait savoir si on a besoin de la déplacer ou non ?
						for(GraphNode  k : nodeOnYaxis[i]) {
							System.out.println("on décale "+k.label+" de "+offset);
							k.x += offset;
						}
					}
					n.x = newXMaybe;
				}
				
			}
			
		}

		@Override
		public mxGraphComponent getpane() {
			return graphcp;
		}
		
	}