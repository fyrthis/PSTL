/*package new_viewer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import serieparallel.Graph;
import serieparallel.Node;
import serieparallel.PairHandler;

public class PositionSerieParallelAlgo {

	Graph sp;
	private ArrayList<Node<?>> nodeOnYaxis[];
	private float[] next;
	private int graphDepth;

	private Deque<Integer> forks;
	private Deque<Node<?>> forkNodes;

	private PairHandler pairsList;
	boolean b = false;

	
	@SuppressWarnings("unchecked")
	public PositionSerieParallelAlgo(Graph graph) {
		this.sp=graph;
		forks = new LinkedList<>();
		forkNodes = new LinkedList<>();
		pairsList = new PairHandler();
		for(Node<?> node : sp.getSources()) {
			findDepthMax(node, 0);
		}
		next = new float[graphDepth+1];
		
		nodeOnYaxis = new ArrayList[graphDepth+1];
		for(int i=0;i<nodeOnYaxis.length;i++)
			nodeOnYaxis[i] = new ArrayList<Node<?>>();

		run();
		for(int i = forks.size(); i > 0 ; i--) {
			System.out.println(forkNodes.pop()+" is a fork of "+forks.pop());
		}
	}



	public void run() {
		try {
			for(Node<?> node : sp.getSources()) {
				setYCoordinates(node,0);
				computeTree(node);
			}
			for(Node<?> node : sp.getSources()) {
				applyOffsets(node, 0);
			}
			for(Node<?> node : sp.getSources()) {
				computeDiamonds(node, 0);
			}
		} catch (InterruptedException e) { e.printStackTrace();	}

	}

	private void applyOffsets(Node<?> node, float offsum) {
		if(node.tag==1) {
			node.x += offsum;
			offsum += node.offset;

			node.offset = 0;
			node.tag=0;

		}
		for(Node<?> child : node.getChildren()) {
			applyOffsets(child, offsum);
		}
	}



	private void findDepthMax(Node<?> Node, int depth) {
		for(Node<?> child : Node.getChildren()) {
			findDepthMax(child, depth+1);
		}
		if(graphDepth<depth) graphDepth=depth;
	}


	private void setYCoordinates(Node<?> node, int depth) {
		if(depth>node.y) {
			nodeOnYaxis[(int)node.y].remove(node);
			nodeOnYaxis[depth].add(node);
			node.y = depth;
		}

		for(Node<?> child : node.getChildren()) {
			setYCoordinates(child, depth+1);
		}
	}

	private void computeTree(Node<?> node) throws InterruptedException {
		boolean parentsAreVisited = true;
		for(int i = 0; i < node.getParents().size() ; i++) {
			if(node.getParents().get(i).visited == false) {
				parentsAreVisited = false;
				break;
			}
		}
		if(parentsAreVisited) {
			System.out.println("parcours de "+node.id+" : "+node.x);
			node.visited = true;
			updateStacks(node);
		}


		for(Node<?> child : node.getChildren()) {
			computeTree(child);
		}

		if(node.tag==0) {


			int nbChildren = node.getChildren().size(); 
			float placeSouhaite = 0;
			if (nbChildren == 0) {
				placeSouhaite = next[(int)node.y];
			}else {
				//System.out.println(node.getValue()+" between "+node.getChildren().get(0).value+" and "+node.getChildren().get(nbChildren-1).value);
				placeSouhaite = (float) ((node.getChildren().get(0).x + node.getChildren().get(nbChildren-1).x) / 2.0);
			}

			node.x = Math.max(placeSouhaite, next[(int)node.y]);// + offset[(int)node.y];

			//maj prochaine place disponible à cette profondeur.

			next[(int)node.y] = Math.max(next[(int)node.y], node.x+1);
			node.offset = Math.abs(node.x - placeSouhaite);//offset[(int)node.y];
			node.tag = 1;

		}
	}



	private void updateStacks(Node<?> node) {
		if(node.getParents().size() > 1) { //Si c'est un join
			int joinSize = node.getParents().size();
			while(joinSize > 0) {
				Node<?> fork = forkNodes.removeLast();
				int forkValue = forks.removeLast();
				if(joinSize == forkValue) {
					pairsList.add(node, fork);
					//pairsList.add(new Pair<Node<?>, Node<?>>(node, fork));
					System.out.println("ajout de "+node+" avec "+fork);
					break;
				}
				else if(joinSize > forkValue) {
					joinSize = joinSize - forkValue + 1;
				}else {
					System.out.println("join tout seul : "+node);
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

	private void computeDiamonds(Node<?> node, float offsum) {
		//System.out.println(node + "is computing avec tag "+node.tag);
		if(node.tag==0) {
			if(pairsList.containsKey(node)) {
				if(pairsList.get(node)!=null) { //Join à associer avec son fork d'alignement
					double off = Math.abs(pairsList.get(node).x - node.x);
					if(off!=0)
						offsum = (float)off;
					System.out.println(node + " --- " + pairsList.get(node));
				} else {
					offsum += Math.abs((float) ((node.getParents().get(0).x + node.getParents().get(node.getParents().size()-1).x) / 2.0) - (node.x+offsum));
				} //La distance entre la position à atteindre et la position avec l'offset

			}
			node.x += offsum;
			
			node.tag=1;
			for(Node<?> child : node.getChildren()) {
				computeDiamonds(child, offsum);
			}
		}

	}


}
*/