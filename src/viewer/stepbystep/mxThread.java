package viewer.stepbystep;

import java.util.ArrayList;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class mxThread extends Thread {

	viewer.stepbystep.mxGraph sp;
	mxGraphComponent graphcp;
	private mxGraph graph = new mxGraph();
	private Object gParent = graph.getDefaultParent();
	int step = 50;
	private ArrayList<mxNode> nodeOnYaxis[];
	private int[] next;
	private int[] offset;
	private int graphDepth;
	private boolean stepByStep = true;
	private boolean myTurn = true;
	
	
	public mxThread(viewer.stepbystep.mxGraph mxGraph) {
		this.sp=mxGraph;
		//Trouver la profondeur
		System.out.println("mxFIND DEPTH STARTED");
		findDepthMax(mxGraph.getSources().get(0), 0);
		System.out.println("mxFIND DEPTH ENDED");
		next = new int[graphDepth+1];
		offset = new int[graphDepth+1];
		System.out.println("mxINITIALIZE GRAPHIC NODE STARTED");
		for(mxNode node : sp.getSources())
			draw(node, null);
		System.out.println("mxINITIALIZE GRAPHIC NODE ENDED");
		//draw(mxGraph.getSources().get(0), null);
		nodeOnYaxis = new ArrayList[graphDepth+1];
		for(int i=0;i<nodeOnYaxis.length;i++)
			nodeOnYaxis[i] = new ArrayList<mxNode>();
		graphcp = new mxGraphComponent(graph);
	}
	
	public void run() {
		try {
			for(mxNode node : sp.getSources()) {
				System.out.println("mxPLACE FATHERS STARTED");
				placeFathers(node, 0);
				System.out.println("mxPLACE FATHERS ENDED");
			}
			for(mxNode node : sp.getSources()) {
				System.out.println("mxPLACE FATHERS STARTED");
				placeSonsv2(node, graphDepth);
				System.out.println("mxPLACE FATHERS ENDED");
			}
			//placeSons(sp.getSinks().get(0));
			graph.refresh();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void findDepthMax(mxNode mxNode, int depth) {
		for(mxNode child : mxNode.getChildren()) {
			findDepthMax(child, depth+1);
		}
		if(graphDepth<depth) graphDepth=depth;
	}
	
	
	public void draw(mxNode node, Object parent) {
		//Vertex drawing
		graph.addCell(node);
		node.setVertex(true);
		node.setGeometry(new mxGeometry(node.n.x, node.n.y, 40, 40));
		graph.insertEdge(gParent, null, null, parent, node);
		for(mxNode child : node.getChildren()) {
			draw(child, node);
		}
	}
	
	private synchronized void placeFathers(mxNode node, int depth) throws InterruptedException {
		//Placer l'ordonnée
		if(depth>node.n.y) {
			if(!nodeOnYaxis[depth].contains(node)) 
				nodeOnYaxis[node.n.y].remove(node);
			nodeOnYaxis[depth].add(node);
			node.setY(depth);
		}
		
		//Récurrence sur les fils
		for(mxNode child : node.getChildren())
			placeFathers(child, depth+1);
		
		//Calcul position parent par rapport fils
		int nbChildren = node.getChildren().size(); 
		int place = 0;
		if (nbChildren == 0) {
			place = next[depth];
			node.setX(place);
		} else
			place = (node.getChildren().get(0).n.x + node.getChildren().get(nbChildren-1).n.x) / 2;
		
		
		//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
		offset[depth] = Math.max(offset[depth], next[depth]-place);
		
		//Application décalage profondeur.
		if (nbChildren != 0)
			node.setX(place + offset[depth]);
		
		
//		myTurn=false;
//		while(stepByStep && !myTurn)
//			wait();
		node.setVisible(true);
//		graph.refresh();
		
		//maj prochaine place disponible à cette profondeur.
		if(node.tag==0)
		next[depth] = node.n.x+1;
		
		// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
		node.n.offset = offset[depth];	
		node.tag=1;
	}
	private void placeSonsv2(mxNode node, int depth) {				

		//Récurrence sur les parents
		for(mxNode parent : node.getParents())
			placeSonsv2(parent, depth+1);

		//Calcul position fils par rapport parent
		int nbParents = node.getParents().size(); 
		int place = 0;
		if (nbParents == 0) {
			place = next[depth];
			node.setX(place);
		} else
			place = (node.getParents().get(0).n.x + node.getParents().get(nbParents-1).n.x) / 2;


		//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
		offset[depth] = Math.min(offset[depth], next[depth]-place);

		//Application décalage profondeur.
		if (nbParents != 0)
			node.setX(place + offset[depth]);


		//				myTurn=false;
		//				while(stepByStep && !myTurn)
		//					wait();
		node.setVisible(true);
		//				graph.refresh();

		//maj prochaine place disponible à cette profondeur.
		if(node.tag==1)
			next[depth] = node.n.x+1;

		// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
		node.n.offset = offset[depth];	
		node.tag=0;
	}
	private void placeSons(mxNode node) {
		if(node==sp.getSources().get(0)) return;
		
		for(mxNode parent : node.getParents())
			placeSons(parent);

		if (node.getParents().isEmpty()) {
			//IS ROOT, DO NOTHING
		} else if (node.getParents().size()==1) {
			node.n.x = node.getParents().get(0).n.x;
		} else {
			int newXMaybe = ((node.getParents().get(0).n.x + node.getParents().get(node.getParents().size()-1).n.x) / 2);
			if(newXMaybe < node.n.x) {
				System.out.println("newXMaybe = "+newXMaybe);
				System.out.println("n.x = "+node.n.x);

				int offset = (newXMaybe - node.n.x);
				System.out.println("décalage engendré par "+node.n.value+" : "+(node.n.y)+"   "+graphDepth);
				for(int i = (node.n.y+1); i<graphDepth+1; i++) {
					System.out.println("size of : "+nodeOnYaxis[i].size());
					//Peut-être qu'en regardant l'espace qui le sépare du prochain, on pourrait savoir si on a besoin de la déplacer ou non ?
					for(mxNode  k : nodeOnYaxis[i]) {
						System.out.println("on décale "+k.n.value+" de "+offset);
						k.n.x += offset;
					}
				}
				node.n.x = newXMaybe;
			}
			
		}
		
	}
	
	public mxGraph getGraph() {
		// TODO Auto-generated method stub
		return graph;
	}
	
	public synchronized void event(boolean b) {
		myTurn = true;
		stepByStep=b;
		notify();
	}
}
