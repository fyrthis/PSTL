package viewer.stepbystep;

import java.util.ArrayList;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class mxThread extends Thread {

	mxSerieParallelGraph sp;
	mxGraphComponent graphcp;
	private ArrayList<mxNode> vertices;
	private mxGraph graph = new mxGraph();
	private Object gParent = graph.getDefaultParent();
	int step = 50;
	private ArrayList<mxNode> nodeOnYaxis[];
	private int[] next;
	private int[] offset;
	private int graphDepth;
	private boolean stepByStep = true;
	private boolean myTurn = true;
	
	
	public mxThread(mxSerieParallelGraph sp) {
		this.sp=sp;
		//Trouver la profondeur
		findDepthMax(sp.getSource(), 0);
		next = new int[graphDepth+1];
		offset = new int[graphDepth+1];
		System.out.println(graphDepth);
		draw(sp.getSource(), null);
		nodeOnYaxis = new ArrayList[graphDepth+1];
		for(int i=0;i<nodeOnYaxis.length;i++)
			nodeOnYaxis[i] = new ArrayList<mxNode>();
		graphcp = new mxGraphComponent(graph);
// n		graph.getModel().beginUpdate();
//		try
//		{
//			graph.insertVertex(gParent, "hello", "heloo", 10, 10, 40, 40);
//		}
//		finally
//		{
//			graph.getModel().endUpdate();
//		}
		
	}
	
	public void run() {
		try {
			System.out.println("hello");
			placeFathers(sp.getSource(), 0);
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
		for(mxNode child : node.getChildren()) {
			placeFathers(child, depth+1);
		}
		
		//Calcul position parent par rapport fils
		int nbChildren = node.getChildren().size(); 
		int place = 0;
		if (nbChildren == 0) {
			place = next[depth];
			node.setX(place);
		} else {
			place = (node.getChildren().get(0).n.x + node.getChildren().get(nbChildren-1).n.x) / 2;
		}
		
		//calcul éventuel décalage engendré. Si on l'a décalé à cause de ses fils.
		offset[depth] = Math.max(offset[depth], next[depth]-place);
		
		//Application décalage profondeur.
		if (nbChildren != 0)
			node.setX(place + offset[depth]);
		
		myTurn=false;
		while(stepByStep && !myTurn) { wait(); }
		node.setVisible(true);
		graph.refresh();
		
		//maj prochaine place disponible à cette profondeur.
		next[depth] = node.n.x+1;
		
		// On mémorise le décalage à appliquer au sous-arbre lors de la deuxième passe.
		node.n.offset = offset[depth];
		
		
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
