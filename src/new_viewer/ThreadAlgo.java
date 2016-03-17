package new_viewer;

import java.util.ArrayList;

import serieparallel.Graph;
import serieparallel.Node;

public class ThreadAlgo{

	Graph sp;
	private ArrayList<Node<?>> nodeOnYaxis[];
	private float[] next;
	private float[] offset;
	private int graphDepth;
	
	
	public ThreadAlgo(Graph graph) {
		this.sp=graph;
		//Trouver la profondeur
		System.out.println("mxFIND DEPTH STARTED");
		for(Node<?> node : sp.getSources()) {
			findDepthMax(node, 0);
		}
		System.out.println("depth :"+graphDepth);
		System.out.println("mxFIND DEPTH ENDED");
		next = new float[graphDepth+1];
		offset = new float[graphDepth+1];
		//draw(mxGraph.getSources().get(0), null);
		nodeOnYaxis = new ArrayList[graphDepth+1];
		for(int i=0;i<nodeOnYaxis.length;i++)
			nodeOnYaxis[i] = new ArrayList<Node<?>>();
		
		run();
	}
	
	
	
	public void run() {
		try {
			for(Node<?> node : sp.getSources()) {
				System.out.println("mxPLACE FATHERS STARTED");
				placeFathers(node, 0);
				System.out.println("mxPLACE FATHERS ENDED");
			}
			for(Node<?> node : sp.getSinks()) {
				System.out.println("mxPLACE FATHERS STARTED");
				//placeSons(node, graphDepth);
				System.out.println("mxPLACE FATHERS ENDED");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void findDepthMax(Node<?> Node, int depth) {
		for(Node<?> child : Node.getChildren()) {
			findDepthMax(child, depth+1);
		}
		if(graphDepth<depth) graphDepth=depth;
	}
	
	private synchronized void placeFathers(Node<?> node, int depth) throws InterruptedException {
		//Placer l'ordonnée
		if(depth>node.y) {
			if(!nodeOnYaxis[depth].contains(node)) 
				nodeOnYaxis[depth].add(node);
			node.y = depth;
		}
		
		//Récurrence sur les fils
		System.out.print(node.value+" = [");
		for(Node<?> child : node.getChildren()) {
			System.out.print(child.value+", ");
		}
		System.out.print("]\n");
		
		for(Node<?> child : node.getChildren()) {
			placeFathers(child, depth+1);
		}
		
		//Calcul position parent par rapport fils
		int nbChildren = node.getChildren().size(); 
		float place = 0;
		if (nbChildren == 0) {
			place = next[depth];
		} else {
			System.out.println(node.getValue()+" between "+node.getChildren().get(0).value+" and "+node.getChildren().get(nbChildren-1).value);
			place = (float) ((node.getChildren().get(0).x + node.getChildren().get(nbChildren-1).x) / 2.0);
		}

		//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
		offset[depth] = Math.max(offset[depth], next[depth]-place);
		
		//Application décalage profondeur.
		if(place > node.x) {
			node.x = place + offset[depth];
		}
		
		//maj prochaine place disponible à cette profondeur.
		if(node.tag==0) {
			next[depth] = Math.max(next[depth]+1, node.x+1);
		}
		
		// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
		node.offset = offset[depth];	
		node.tag=1;
	}
	private void placeSons(Node<?> node, int depth) {				

		//Récurrence sur les parents
		for(Node<?> parent : node.getParents())
			placeSons(parent, depth-1);

		//Calcul position fils par rapport parent
		int nbParents = node.getParents().size(); 
		float place = 0;
		if (nbParents == 0) {
			//DO NOTHING
		} else { //On trouve sa place dans les fils
			float leftParent = node.getParents().get(0).x;
			float rightParent = node.getParents().get(nbParents-1).x;
			int nbBrothersPlusHimself = 0;
			float nodePosWithBrothers = 1;
			for(Node<?> parent : node.getParents())
				for(Node<?> bro : parent.getChildren()) {
					if(bro==node) {
						nodePosWithBrothers = nbBrothersPlusHimself+1;
						break;
					}
					nbBrothersPlusHimself++;
				}
			node.x = (leftParent+rightParent)/nodePosWithBrothers;			
		}

		
		
		
		
	}

	
}
