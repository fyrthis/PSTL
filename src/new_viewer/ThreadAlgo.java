package new_viewer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import serieparallel.Graph;
import serieparallel.Node;

public class ThreadAlgo{

	Graph sp;
	private ArrayList<Node<?>> nodeOnYaxis[];
	private float[] next;
	private float[] offset;
	private int graphDepth;
	private Deque<Integer> forks;
	private Deque<Node<?>> associateNodes;
	
	
	public ThreadAlgo(Graph graph) {
		this.sp=graph;
		forks = new ArrayDeque<>();
		associateNodes = new ArrayDeque<>();
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
		for(int i = forks.size(); i > 0 ; i--) {
			System.out.println(associateNodes.pop()+" is a fork of "+forks.pop());
		}
	}
	
	
	
	public void run() {
		try {
			for(Node<?> node : sp.getSources()) {
				System.out.println("mxPLACE FATHERS STARTED");
				placeFathers(node, 0);
				System.out.println("mxPLACE FATHERS ENDED");
			}
			for(Node<?> node : sp.getSources()) {
				System.out.println("mxPLACE SONS STARTED");
				placeSons(node);
				System.out.println("mxPLACE SONS ENDED");
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
		System.out.println("(Fathers) Node : "+node.id);
		//Placer l'ordonnée
		if(depth>node.y) {
			if(!nodeOnYaxis[depth].contains(node)) 
				nodeOnYaxis[depth].add(node);
			node.y = depth;
		}
		for(Node<?> child : node.getChildren()) {
			placeFathers(child, depth+1);
		}
		
		//Calcul position parent par rapport fils
		int nbChildren = node.getChildren().size(); 
		float place = 0;
		if (nbChildren == 0) {
			place = next[depth];
		} else {
			//System.out.println(node.getValue()+" between "+node.getChildren().get(0).value+" and "+node.getChildren().get(nbChildren-1).value);
			place = (float) ((node.getChildren().get(0).x + node.getChildren().get(nbChildren-1).x) / 2.0);
		}

		//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
		offset[depth] = Math.max(offset[depth], next[depth]-place);
		
		//Application décalage profondeur.
		//if(place > node.x) {
			node.x = place + offset[depth];
		//}
		
		//maj prochaine place disponible à cette profondeur.
		
			next[depth] = Math.max(next[depth]+1, node.x+1);
		if(node.tag==0) {
			if(nbChildren>1) {
				forks.push(nbChildren);
				associateNodes.push(node);
			}
		}
		
		
		// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
		node.offset = offset[depth];	
		node.tag=1;
	}
	private void placeSons(Node<?> node) {				
		System.out.println("(Sons) Node : "+node.id);
		for(Node<?> child : node.getChildren()) {
			placeSons(child);
		}
		
		
		if(node.tag==1) {
			if(node.getParents().size() > 1) {
				forks.pop();
				node.x = associateNodes.pop().x;
			}
		}
		
		node.tag=0;

	}

	
}
