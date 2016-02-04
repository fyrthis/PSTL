package viewer;

import javax.swing.JTabbedPane;

import viewer.stepbystep.GraphComponent;

public class Tab extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	public Tab() {
		super();
	}
	
	public void setTitle(String title) {
		addTab("", null); //Ici le panel à mettre dans le tab
		setTitleAt(getTabCount()-1, title+" n°"+(getTabCount()-1));
	}
	
	public void add(String title, GraphComponent graph) {
		addTab("", graph);
		setTitleAt(getTabCount()-1, title+" n°"+(getTabCount()-1));
	}
	
	public void event(boolean b) {
		((GraphComponent)getComponentAt(getSelectedIndex())).event(b);
	}

}