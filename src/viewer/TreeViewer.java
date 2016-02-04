package viewer;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import datastructure.TreeNode;
import datastructure.graph.Tree;

public class TreeViewer implements IViewer {
		//Pour JGraph
		private mxGraph graph = new mxGraph();
		private Object gParent = graph.getDefaultParent();
		private int treeDepth=0;
		private int step = 5;
		
		int[] next;
		int[] offset;
		
		mxGraphComponent graphcp;
		
		public TreeViewer(Tree tree) {
			super();
			graph.getModel().beginUpdate();
			try
			{
				//Trouver la profondeur
				findDepthMax(tree.getSource(), 0);
				next = new int[treeDepth+1];
				offset = new int[treeDepth+1];
				System.out.println("Fin algo depth = "+treeDepth);
				computeDown(tree.getSource(), 0);
				System.out.println("Fin algo setup");
				addOffsets(tree.getSource(), 0);
				System.out.println("Fin algo offsum");

				//Puis dessiner
				draw(tree.getSource(), null);
				System.out.println("Fin algo drawing");
			}
			finally
			{
				graph.getModel().endUpdate();
				System.out.println("Fin model update");
			}

			graphcp = new mxGraphComponent(graph);
			System.out.println("should be drawing..?");
			
		}
		
		public mxGraphComponent getpane() {
			return graphcp;
		}
		
		private void draw(TreeNode node, Object parent) {
			//Vertex drawing
			mxCell vertex = (mxCell) graph.insertVertex(gParent, null, node, node.x*4*step*step, node.y*4*step, 40, 40);
			//Edge drawing
			graph.insertEdge(parent, null, null, parent, vertex);
			//Recursive call
			if(node.getChildren()!=null)
				for(TreeNode child : node.getChildren())
					draw(child, vertex);
		}

		
		private void findDepthMax(TreeNode n, int depth) {
			for(TreeNode child : n.getChildren()) {
				findDepthMax(child, depth+1);
			}
			if(treeDepth<depth) treeDepth=depth;
		}
		

		private void computeDown(TreeNode n, int depth) {
			n.y = depth*step;
			
			//Récurrence sur les fils
			for(TreeNode child : n.getChildren()) {
				computeDown(child, depth+1);
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
		
		private void addOffsets(TreeNode n, int offsum) {
			n.x = n.x + offsum;
			offsum += n.offset;
			
			for(TreeNode child : n.getChildren()) {
				addOffsets(child, offsum);
			}
		}
			

		
	}