package new_viewer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alexmerz.graphviz.ParseException;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import parser.DotParser;
import serieparallel.Graph;
import serieparallel.Node;

public class Window extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6781845648070860613L;
	
	private JMenuBar menu;
	private JMenuItem open, saveAs, quit;
	
	
	private mxGraph mxGraph;
	private Object gParent;
	private mxGraphComponent graphComponent;
	int widthCell = 40;
	int heightCell = 40;
	private int scale = 80;
	List<mxCell> cells;

	public Window() {
		super();
		initializeMenu();
		

		mxGraph = new mxGraph(); 
		gParent = mxGraph.getDefaultParent();
		graphComponent = new mxGraphComponent(mxGraph);	
		getContentPane().add(graphComponent);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 320);
		setVisible(true);
		
		cells = new ArrayList<>();
	}
	
	private void initializeMenu() {
		menu = new JMenuBar();
		JMenu file = new JMenu("File");
		open = new JMenuItem("Open..");
		saveAs = new JMenuItem("Save as..");
		quit = new JMenuItem("Quit");
		add(file);
		file.add(open);
		file.add(saveAs);
		file.addSeparator();
		file.add(quit);
		//Ajout listeners
		Stream.of(file.getMenuComponents()).forEach(c->{
				if (c instanceof AbstractButton)
					((AbstractButton)c).addActionListener(this);
				});
		menu.add(file);
		setJMenuBar(menu);
	}

	public static void main(String[] argc) {
		new Window();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==open) { 
			JFileChooser chooser = new JFileChooser("/media/tanguinoche/Data/workspace/PSTL/samples");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("fichier dot", "dot");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            String file = chooser.getSelectedFile().getAbsolutePath();
	            initializeBody(file);
	        } else {
	            System.err.println("Open file cancelled by user.");
	        }
		}
		
		if(e.getSource()==saveAs) { 
			JFileChooser chooser = new JFileChooser("/media/tanguinoche/Data/workspace/PSTL");
		    FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG image", "png");
		    FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG image", "jpeg", "jpg");
		    FileNameExtensionFilter filterBMP = new FileNameExtensionFilter("BMP image", "bmp");
		    chooser.addChoosableFileFilter(filterPNG);
		    chooser.addChoosableFileFilter(filterJPG);
		    chooser.addChoosableFileFilter(filterBMP);
		    chooser.setAcceptAllFileFilterUsed(false);
		    int returnVal = chooser.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = chooser.getSelectedFile();
	            String ext = StringTools.getFileExtension(file);
	            BufferedImage image = mxCellRenderer.createBufferedImage(mxGraph, null, 1, Color.WHITE, true, null);
	            try {
	            	if(chooser.getFileFilter().equals(filterPNG)) {
	            		if(ext.compareToIgnoreCase("png")!=0)
	            			file = new File(file.getAbsolutePath()+".png");
	            		ImageIO.write(image, "PNG", file);
	            	} else if(chooser.getFileFilter().equals(filterJPG)) {
	            		if(ext.compareToIgnoreCase("jpg")!=0 && ext.compareToIgnoreCase("jpeg")!=0)
	            			file = new File(file.getAbsolutePath()+".jpg");
	            		ImageIO.write(image, "JPG", file);
	            	} else if(chooser.getFileFilter().equals(filterBMP)) {
	            		if(ext.compareToIgnoreCase("bmp")!=0)
	            			file = new File(file.getAbsolutePath()+".bmp");
	            		ImageIO.write(image, "BMP", file);
	            	} else 
	            		System.err.println("Erreur, choix de filtre non reconnu lors de l'exportation en image.");
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
	        } else {
	            System.err.println("Open file cancelled by user.");
	        }
		}
		
		if(e.getSource()==quit) { Runtime.getRuntime().exit(0); }
	}

	private void initializeBody(String file) {
		System.out.println("Parsing file...");
		DotParser parser = null;
		try {
			parser = new DotParser(file);
		} catch (FileNotFoundException | ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("Check graph structure & build it...");
		Graph graph = parser.getGraph();
		
		System.out.println("Running coordinates algorithm...");
		new PositionSerieParallelAlgo(graph);
		
		System.out.println("Building view...");
		
		cells.clear();
		
		mxGraph.removeCells(mxGraph.getChildVertices(mxGraph.getDefaultParent())); //remove previous graph
		mxGraph.getModel().beginUpdate();		
		for(Node<?> node : graph.getSources())
			draw(node);
		mxGraph.getModel().endUpdate();

	}
	
	private void draw(Node<?> node) {
		mxCell cell = (mxCell) mxGraph.insertVertex(gParent, null, node.value, node.x*scale, node.y*scale, widthCell, heightCell);
		mxGraph.addCell(cell);
		cells.add(cell);
		for(Node<?> child : node.getChildren()) {
			draw(cell, child);
		}
		
		node.tag=0;
	}

	private void draw(mxCell parent, Node<?> child) {
		mxCell cell = null;
		if(child.tag==1) { //On n'est jamais passé par child
			cell = (mxCell) mxGraph.insertVertex(gParent, null, child.value, child.x*scale, child.y*scale, widthCell, heightCell);
			cells.add(cell);
			//System.out.println("add cell "+cell.getValue());
			child.tag=0;
		} else { //On a déjà fait une cellule pour child
			for(mxCell c : cells) {
				if(c.getValue().equals((child.getValue()))) {
					cell = c;
					break;
				}
			}
		}
			

		boolean drawn = false;
		for(int i =0 ; i<cell.getEdgeCount(); i++) {
			mxCell edge = (mxCell) cell.getEdgeAt(i);
			if(edge.getSource().equals(parent) && edge.getTarget().equals(cell)) {
				drawn = true;
			}
		}
		if(!drawn) mxGraph.insertEdge(gParent, null, null, parent, cell);
		
		for(Node<?> son : child.getChildren()) {
			draw(cell, son);
		}
		
	}
}
