package viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.alexmerz.graphviz.ParseException;

import parser.mxDotParser;
import viewer.stepbystep.GraphComponent;
import viewer.stepbystep.mxGraph;
import viewer.stepbystep.mxThread;

public class App extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Menu menu;
	private JButton button;
	private Tab tabs;
	
	public App() {
		super("Projet STL : Visualisation de graphes série parallèle");
		//Create menu
		menu = new Menu();
		setJMenuBar(menu);
		setLayout(new BorderLayout());
		//Create tabs pane
		tabs = new Tab();
		add(tabs);
		//Create Panel
		mxDotParser parser;
		try {
			parser = new mxDotParser("samples/random-dag/g.50.8.graphml.dot");
			mxGraph graph = parser.getGraph();
			if(graph==null) System.out.println("graph is null");
			mxThread thread = new mxThread(graph);
			tabs.add("tab1",new GraphComponent(thread));
		} catch (FileNotFoundException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Create Window
		add(tabs, BorderLayout.CENTER);
		button = new JButton("next step");
		button.addActionListener(this);
		add(button, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 800);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) {
			tabs.event(menu.isStepByStep());
		}
		
	}
}