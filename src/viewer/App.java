package viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import viewer.stepbystep.GraphComponent;
import viewer.stepbystep.mxGraphInstance;
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
		mxThread thread = new mxThread(mxGraphInstance.one());
		tabs.add("tab1",new GraphComponent(thread));
		
		//viewer = new SecondVersionViewer(GraphInstance.one());
		
		//viewer = new SimpleViewer(GraphInstance.one());
		
		//TreeControler tc= new TreeControler();
//		Tree tree = tc.randomTree(3, 3);
//		viewer = new TreeViewer(tree);
		
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