package datastructure;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class SimpleViewer extends JFrame

	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -2707712944901661771L;
		mxGraph graph = new mxGraph();
		Object gParent = graph.getDefaultParent();
		protected List<mxCell> vertices =  new ArrayList<>();
		private static double x=0;
		private List<Integer> offset =  new ArrayList<>();
		public SimpleViewer(SerieParallelGraph g)
		{
			super("Hello, World!");

			

			graph.getModel().beginUpdate();
			try
			{
				//draw(g.getSource(), null);
				drawEssai(g.getSource(), 0);
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
		
		private void draw(Node n, Object parent) {
			Object node = graph.insertVertex(gParent, null, n.label, n.x*20, n.y*20, 20, 20);
			if(parent!=null)
				graph.insertEdge(parent, null, null, parent, node);

			if(n.getChildren()!=null)
				for(Node child : n.getChildren())
					draw(child, node);
		}
		
		private com.mxgraph.model.mxICell drawEssai(Node n, int depth) {
			ArrayList<Node> children = n.getChildren();
			if(children.isEmpty()) { x+=70; }
			double box = 20.0;
			ArrayList<Node> before = new ArrayList<>();
			for(int i=0; i<children.size()%2; i++) {
				before.add(children.get(i));
			}
			ArrayList<Node> after = new ArrayList<>();
			for(int i=children.size()%2; i<children.size(); i++) {
				after.add(children.get(i));
			}
			double y = 70.0*depth;
			
			ArrayList<Object> Obefore = new ArrayList<>();
			for(Node b : before) {
				Obefore.add(drawEssai(b, depth+1));
			}
			
			mxCell v=null;
			//Si le noeud existe déjà
			for(mxCell c : vertices) {
				System.out.println(c.getValue()+"   "+n.label);
				if(c.getValue().equals(n.label)) {
					v = c;
				}
			}
			if(v==null) {
				v = (mxCell) graph.insertVertex(gParent, null, n.label ,x,  y, box, box); //Construction d'un noeud
				vertices.add(v);
			}
			
			
			ArrayList<Object> Oafter = new ArrayList<>();
			for(Node b : after) {
				Oafter.add(drawEssai(b, depth+1));
			}
			for(Object b : Obefore) {
				graph.insertEdge(gParent, null, null, v, b);
			}
			for(Object b : Oafter) {
				graph.insertEdge(gParent, null, null, v, b);
			}

			return v;
		}

		private void drawEssaiAlgo(Node n, int depth) {
			n.y = depth;
			for(Node child : n.getChildren()) {
				drawEssaiAlgo(child, depth+1);
			}
			
			if (n.getChildren().size() == 0) {
				if(depth >= offset.size()) offset.add(0);
				Integer placeX = offset.get(depth);
				placeX += 1;
				n.x = placeX;
			} else {
				int placeX = (n.getChildren().get(0).x + n.getChildren().get(n.getChildren().size()-1).x) / 2;
			}
		}
	}