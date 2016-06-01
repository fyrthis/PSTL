package new_viewer;

import java.util.Deque;
import java.util.LinkedList;

import serieparallel.Graph;
import serieparallel.Node;
import serieparallel.PairHandler;

public class PositionSerieParallelAlgo {

	Graph sp;
	private float[] next;
	private int graphDepth;

	private Deque<Integer> forks;
	private Deque<Node<?>> forkNodes;

	private PairHandler pairsList;
	boolean b = false;
	int nbInitialize =0;
	int nbComputeTree =0;
	int nbOffsets =0;
	int computeDiadmonds =0;

	public int getNbInitialize() { return nbInitialize; }
	public int getNbComputeTree() { return nbComputeTree; }
	public int getNbOffsets() { return nbOffsets; }
	public int getComputeDiadmonds() { return computeDiadmonds; }
	public int getNbRecursiveCalls() { return nbInitialize+nbComputeTree+nbOffsets+computeDiadmonds; }



	public PositionSerieParallelAlgo(Graph graph) {
		this.sp=graph;
		forks = new LinkedList<>();
		forkNodes = new LinkedList<>();
		pairsList = new PairHandler();

		run();
	}



	public void run() {
		try {
			for(Node<?> node : sp.getSources()) {
				findDepth(node,0);			
			}
			System.out.println("hauteur : "+graphDepth);
			next = new float[graphDepth+1];
			for(Node<?> node : sp.getSources()) {
				initialization(node,0);
			}
			for(Node<?> node : sp.getSources()) {
				applyOffsets(node, 0);
			}
			for(Node<?> node : sp.getSources()) {
				applyDiadmondsOffsets(node, 0);
			}
		} catch (InterruptedException e) { e.printStackTrace();	}

	}

	/**
	 * Complexité : O(n) * O(h) ? avec n nb de noeuds, h la hauteur
	 * On détermine l'ordonnée de chaque noeud
	 * On détermine la profondeur du graphe
	 * On compte le nombre de fois que nous tombons sur un même noeud N 
	 *    = nombre de chemins passant par N 
	 *    = nombre de parents de N
	 */
	private void findDepth(Node<?> node, int depth) {
		nbInitialize++;
		if(depth>node.y || node.findDepthTag == 0) {
			node.y = depth;

			if(graphDepth<depth) graphDepth=depth;

			node.findDepthTag=1;
			for(Node<?> child : node.getChildren()) {
				findDepth(child, depth+1);
			}
		}
	}

	private void initialization(Node<?> node, float maxOnLinesUpside) throws InterruptedException {
		nbComputeTree++;
		boolean parentsAreVisited = true;
		for(int i = 0; i < node.getParents().size() ; i++) {
			if(!node.getParents().get(i).visited) {
				parentsAreVisited = false;
				break;
			}
		}

		if(!parentsAreVisited && node.initializationTag==1)
			return;
		
		if(parentsAreVisited) {
			node.visited = true;
			updateKnownDiamonds(node);
		} 

		if(node.spaceTag==0) {
			for(int i=0;i<graphDepth;i++) 
			next[(int)node.y] = Math.max(next[(int)node.y], maxOnLinesUpside);
			//for(int i=(int)node.y+1;i<graphDepth-1;i++) next[(int)node.y] = Math.max(next[(int)node.y], next[i]);
			node.spaceTag=1;
		}
		
		for(Node<?> child : node.getChildren()) {
			initialization(child, (int)next[(int)node.y]);
		}
		
		
		if(node.initializationTag==0) { //Si première fois qu'on passe par ce noeud
			int nbChildren = node.getChildren().size(); 
			float placeSouhaite = 0;
			if (nbChildren == 0) {
				placeSouhaite = next[(int)node.y];
				node.x = placeSouhaite;
			}else {
				placeSouhaite = (float) ((node.getChildren().get(0).x + node.getChildren().get(nbChildren-1).x) / 2.0);
				node.x = Math.max(placeSouhaite, next[(int)node.y]);
			}

			//maj prochaine place disponible à cette profondeur.

			next[(int)node.y] = Math.max(next[(int)node.y], node.x+1);
			
			
			node.offset = Math.abs(node.x - placeSouhaite);//offset[(int)node.y];
			if(node.offset!=0)System.out.println("alert : "+node+" : "+node.offset );
			node.initializationTag = 1;

		}

	}

	private void applyOffsets(Node<?> node, float offsum) {
		nbOffsets++;
		if(node.applyOffsetsTag==0) {
			node.x += offsum;
			System.out.println("on a ajouté "+offsum);
			offsum += node.offset;
			
			node.offset = 0;
			node.applyOffsetsTag=1;
			for(Node<?> child : node.getChildren()) {
				applyOffsets(child, offsum);
			}
		}
	}

	private void updateKnownDiamonds(Node<?> node) {
		if(node.getParents().size() > 1) { //Si c'est un join
			int joinSize = node.getParents().size();
			while(joinSize > 0) {
				Node<?> fork = forkNodes.removeLast();
				int forkValue = forks.removeLast();
				if(joinSize == forkValue) {
					pairsList.add(node, fork);
					//pairsList.add(new Pair<Node<?>, Node<?>>(node, fork));
					//System.out.println("ajout de "+node+" avec "+fork);
					break;
				}
				else if(joinSize > forkValue) {
					joinSize = joinSize - forkValue + 1;
				}else {
					//System.out.println("join tout seul : "+node);
					forkNodes.addLast(fork);
					forks.addLast(forkValue - joinSize +1);
					pairsList.add(node, null);
					break;
				}
			}
		}

		if(node.getChildren().size() > 1) { //C'est un fork
			forkNodes.addLast(node);
			forks.addLast(node.getChildren().size());
		}

	}

	private void applyDiadmondsOffsets(Node<?> node, float offsum) {
		computeDiadmonds++;
		if(node.findDiadmondsTag==0) {
			if(pairsList.containsKey(node)) {
				if(pairsList.get(node)!=null) { //Join à associer avec son fork d'alignement
					double off = Math.abs(pairsList.get(node).x - node.x);
					if(off!=0)
						offsum = (float)off;
					System.out.println(node + " --- " + pairsList.get(node));
				} else {
					offsum += Math.abs((float) ((node.getParents().get(0).x + node.getParents().get(node.getParents().size()-1).x) / 2.0) - (node.x+offsum));
				} //La distance entre la position à atteindre et la position actuelle avec l'offset

			}
			node.x += offsum;
			node.findDiadmondsTag=1;
			for(Node<?> child : node.getChildren()) {
				applyDiadmondsOffsets(child, offsum);
			}
		}

	}


}
