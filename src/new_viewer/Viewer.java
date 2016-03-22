package new_viewer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.alexmerz.graphviz.ParseException;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import parser.DotParser;
import serieparallel.Graph;
import serieparallel.Node;

public class Viewer extends JFrame{
	private mxGraph mxGraph;
	private Object gParent;
	private mxGraphComponent graphComponent;
	int widthCell = 40;
	int heightCell = 40;
	private int scale = 80;
	List<mxCell> cells;
	
	private Viewer(Graph graph) {
		super("GraphViewer");
		cells = new ArrayList<>();
		mxGraph = new mxGraph(); 
		//graph.setCellsLocked(true); 
		gParent = mxGraph.getDefaultParent();

		mxGraph.getModel().beginUpdate();
		
		
		for(Node<?> node : graph.getSources())
			draw(node);
		
		mxGraph.getModel().endUpdate();
		
		
		graphComponent = new mxGraphComponent(mxGraph);	
		getContentPane().add(graphComponent);
	}

	private void draw(Node<?> node) {
		mxCell cell = (mxCell) mxGraph.insertVertex(gParent, null, node.value, node.x*scale, node.y*scale, widthCell, heightCell);
		mxGraph.addCell(cell);
		cells.add(cell);
		System.out.println(cell.getValue()+" created."+node.tag);
		for(Node<?> child : node.getChildren()) {
			draw(cell, child);
		}
		
		node.tag=1;
	}

	private void draw(mxCell parent, Node<?> child) {
		mxCell cell = null;
		System.out.println("child "+child.getValue()+" ["+child.x+","+child.y+"]"+child.tag);
		if(child.tag==0) { //On n'est jamais passé par child
			cell = (mxCell) mxGraph.insertVertex(gParent, null, child.value, child.x*scale, child.y*scale, widthCell, heightCell);
			cells.add(cell);
			System.out.println(cell.getValue()+" created.");
			child.tag=1;
		} else { //On a déjà fait une cellule pour child
			for(mxCell c : cells) {
				System.out.println(c.getValue()+" == "+child.getValue());
				if(c.getValue().equals((child.getValue()))) {
					cell = c;
					break;
				}
			}
		}
			
		System.out.println("add "+parent.getValue()+"->"+cell.getValue());
		mxGraph.insertEdge(gParent, null, null, parent, cell);
		
		for(Node<?> son : child.getChildren()) {
			draw(cell, son);
		}
		
	}
	
	public static void main(String[] args)
	{
		Viewer frame;
		try {
			//Graph g = new DotParser("samples/random-dag/g.50.8.graphml.dot").getGraph();
			Graph g = new DotParser("samples/serie02.dot").getGraph();
			new ThreadAlgo(g);
			frame = new Viewer(g);		
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 320);
			frame.setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
