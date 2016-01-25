package datastructure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class SimpleViewer extends JFrame {
		private static final long serialVersionUID = -2707712944901661771L;
		
		//Pour JGraph
		private mxGraph graph = new mxGraph();
		private Object gParent = graph.getDefaultParent();
		private List<mxCell> vertices =  new ArrayList<>();
		
		//Pour les algos
		private int[] nextFreePlace = new int[50];
		private int[] offset;
		private int graphDepth=0;
		private SerieParallelGraph g;
		private int step = 5;
		public SimpleViewer(SerieParallelGraph g) {
			super("Graphe Serie-Parallel");
			this.g=g;
			graph.getModel().beginUpdate();
			try
			{
				//Trouver la profondeur
				findDepthMax(g.getSource(), 0);
				g.getSink().y=graphDepth*step;
				System.out.println(graphDepth);
				//Placer les pères et les ordonnées
				computeDown(g.getSource(), 0);
				System.out.println("Fin algo aller");
				//Placer les fils
				computeUp(g.getSink());
				System.out.println("Fin algo retour");
				//Puis dessiner
				draw(g.getSource(), null);
			}
			finally
			{
				graph.getModel().endUpdate();
			}

			mxGraphComponent graphComponent = new mxGraphComponent(graph);
			getContentPane().add(graphComponent);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(400, 320);
			setVisible(true);
		}
		
		/**
		 * En fonction des coordonnées des noeuds, on va dessiner le graphe
		 * à l'aide de JGraph, en prenant soin de ne pas dessiner plusieurs
		 * fois les noeuds.
		 * @param n : noeud source du graphe.
		 * @param parent : initialisé à null.
		 */
		private void draw(Node n, Object parent) {
			Object node = null;
			for(mxCell cell : vertices) { //On regarde si on a déjà dessiné un noeud ici, pour pas le dessiner 50 fois.
				if(cell.getValue()==n.label) { //On considère le label unique...
					node = cell;
				}
			}
			if(node==null) {
				node = graph.insertVertex(gParent, null, n.label, n.x*4*step, n.y*4*step, 20, 20);
				System.out.println(n.label+"= ["+n.x+","+n.y+"]");
				vertices.add((mxCell) node);
			}
			
			if(parent!=null)
				graph.insertEdge(parent, null, null, parent, node);

			if(n.getChildren()!=null)
				for(Node child : n.getChildren())
					draw(child, node);
		}
		/**
		 * On cherche ici à effectuer tous les chemins possibles de la source 
		 * vers la sink, afin de déterminer le chemin le plus long, c-a-d
		 * la profondeur.
		 * 
		 * @param n : noeud source du graphe
		 * @param depth : profondeur courante
		 */
		private void findDepthMax(Node n, int depth) {
			for(Node child : n.getChildren()) {
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
		private int computeDown(Node n, int depthLevel) {
			int lengthOfPath=depthLevel;

			for(Node child : n.getChildren()) {
				lengthOfPath = computeDown(child, depthLevel+1);
				n.y = Math.max(n.y, (graphDepth/lengthOfPath)*depthLevel*step);
			}
			

			if (n.getChildren().isEmpty() || n.getChildren().size()==1) {
				n.x = nextFreePlace[n.y/step]*step;
				nextFreePlace[n.y/step]++;
				
			} else {
				n.x = Math.max(((n.getChildren().get(0).x + n.getChildren().get(n.getChildren().size()-1).x) / 2), nextFreePlace[n.y/step]*step);
				nextFreePlace[n.y/step]++;
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
		private void computeUp(Node n) {
			System.out.println(n.label);
			if(n==g.getSource()) { System.out.println("on retourne car "+n.label+"=="+g.getSource().label);return;}
			if(!n.getParents().isEmpty())
				for(Node parent : n.getParents()) {
					computeUp(parent);
					System.out.println(n.label+" a pour parent "+parent.label+"("+n.getParents().size()+"parents)");
				}
			else System.out.println(n.label+" est orphelin..");

			if (n.getParents().isEmpty() || (n.getParents().size()==1) ) {
				//DO NOTHING
			} else {
				int newXMaybe = ((n.getParents().get(0).x + n.getParents().get(n.getParents().size()-1).x) / 2);
				if(newXMaybe < n.x) {
					HashSet<Node> descendants = n.getDescendants();
					for(Node e : descendants) {
						e.x += (newXMaybe - n.x);
					}
					n.x = newXMaybe;
				}
				
			}
			
		}
		
	}