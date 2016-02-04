package viewer;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import datastructure.GraphNode;
import datastructure.TreeNode;
import datastructure.graph.SerieParallelGraph;

public class SecondVersionViewer  {
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
		private int[] next;
		private int[] offset;
		
		
		@SuppressWarnings("unchecked")
		public SecondVersionViewer(SerieParallelGraph g) {
			super();
			this.g=g;
			graph.getModel().beginUpdate();
			try
			{
				//Trouver la profondeur
				findDepthMax(g.getSource(), 0); //Update le champ graphDepth
				next = new int[graphDepth+1];
				offset = new int[graphDepth+1];
				
				
				nodeOnYaxis = new ArrayList[graphDepth+1];
				for(int i=0;i<nodeOnYaxis.length;i++)
					nodeOnYaxis[i] = new ArrayList<GraphNode>();

				//Placer les pères et les ordonnées
				placeFathers(g.getSource(), 0);
				//addOffsets(g.getSource(), 0);
				System.out.println("Fin algo aller");
				//Placer les fils
				//computeUp(g.getSink());

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
		
		public void draw(GraphNode node, Object parent) {
			//Vertex drawing
			mxCell vertex = null;
			for(mxCell cell : vertices) { //On regarde si on a déjà dessiné un noeud ici, pour pas le dessiner 50 fois.
				if(((GraphNode)cell.getValue()).getLabel()==node.label) //On considère le label unique...
					vertex = cell;
			}
			if(vertex==null) {
				vertex = (mxCell) graph.insertVertex(gParent, null, node, node.x*2*step*step, node.y*4*step*step, 40, 40);
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

		
		private void findDepthMax(GraphNode n, int depth) {
			for(GraphNode child : n.getChildren()) {
				findDepthMax(child, depth+1);
			}
			if(graphDepth<depth) graphDepth=depth;
		}

		private void placeFathers(GraphNode n, int depth) {
			//Placer l'ordonnée
			if(depth>n.y) {
				nodeOnYaxis[n.y].remove(n);
				nodeOnYaxis[depth].add(n);
				n.y=depth;
			}
			if(!nodeOnYaxis[depth].contains(n)) {
				nodeOnYaxis[depth].add(n);
			}
			
			//Récurrence sur les fils
			for(GraphNode child : n.getChildren()) {
				placeFathers(child, depth+1);
			}
			
			//Calcul position parent par rapport fils
			int nbChildren = n.getChildren().size(); 
			int place = 0;
			if (nbChildren == 0) {
				place = next[depth];
				n.x = place;
			} else {
				place = (n.getChildren().get(0).x + n.getChildren().get(nbChildren-1).x) / 2;
			}
			
			//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
			offset[depth] = Math.max(offset[depth], next[depth]-place);
			
			//Application décalage profondeur.
			if (nbChildren != 0)
				n.x = place + offset[depth];
			
			//maj prochaine place disponible à cette profondeur.
			next[depth] = n.x+1;
			
			// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
			n.offset = offset[depth];
			
		}
		private void placerSons() { }
		
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
		
		private void computeUp(GraphNode n) {
			if(n==g.getSource()) return;
			
			for(GraphNode parent : n.getParents())
				computeUp(parent);

			if (n.getParents().isEmpty() || (n.getParents().size()==1) ) {
				//DO NOTHING
			} else {
				int newXMaybe = ((n.getParents().get(0).x + n.getParents().get(n.getParents().size()-1).x) / 2);
				if(newXMaybe < n.x) {
					int offset = (newXMaybe - n.x);
					for(int i = (n.y/step+1); i<graphDepth+1; i++) {
						for(GraphNode  k : nodeOnYaxis[i]) {
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
		
		private void addOffsets(GraphNode n, int offsum) {
			n.x = n.x + offsum;
			offsum += n.offset;
			
			for(GraphNode child : n.getChildren()) {
				addOffsets(child, offsum);
			}
		}
	}