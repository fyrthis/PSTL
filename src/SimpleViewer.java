import javax.swing.JFrame;

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

		public SimpleViewer(SerieParallelGraph g)
		{
			super("Hello, World!");

			

			graph.getModel().beginUpdate();
			try
			{
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
		
		private void draw(Node n, Object parent) {
			Object node = graph.insertVertex(gParent, null, n.label, n.x*20, n.y*20, 20, 20);
			if(parent!=null)
				graph.insertEdge(parent, null, null, parent, node);

			if(n.getChildren()!=null)
				for(Node child : n.getChildren())
					draw(child, node);
		}


	}