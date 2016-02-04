package viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenu file;
	private JMenuItem loadFile, saveFile, exit;
	private JMenu samples; 
	private JMenuItem sample1, sample2, sample3, sample4, sample5;
	private JMenu preferences;
	private JMenuItem stepToStep;
	
	public Menu() {
		//Création menu
		file = new JMenu("File");
			loadFile = new JMenuItem("load file..");
			saveFile = new JMenuItem("save as..");
			exit = new JMenuItem("exit");
		samples = new JMenu("Samples");
			sample1 = new JMenuItem("sample 1");
			sample2 = new JMenuItem("sample 2");
			sample3 = new JMenuItem("sample 3");
			sample4 = new JMenuItem("sample 4");
			sample5 = new JMenuItem("sample 5");
		preferences = new JMenu("preferences");
		stepToStep = new JCheckBoxMenuItem("mode stepToStep");
		stepToStep.setSelected(false);

		
		//Ajout menu
		add(file);
		file.add(loadFile);
		file.add(saveFile);
		file.addSeparator();
		file.add(exit);
		add(samples);
		samples.add(sample1);
		samples.add(sample2);
		samples.add(sample3);
		samples.add(sample4);
		samples.add(sample5);
		add(preferences);
		preferences.add(stepToStep);
		
		//Ajout listeners
		Stream.of(file.getMenuComponents()).forEach(c->{
				if (c instanceof AbstractButton)
					((AbstractButton)c).addActionListener(this);
				});
		Stream.of(samples.getMenuComponents()).forEach(c->{
				if (c instanceof AbstractButton)
					((AbstractButton)c).addActionListener(this);
				});
		Stream.of(preferences.getMenuComponents()).forEach(c->{
			if (c instanceof AbstractButton)
				((AbstractButton)c).addActionListener(this);
			});
		//nextStep.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==loadFile) { System.out.println("catch loadFile event"); }
		if(e.getSource()==saveFile) { System.out.println("catch saveFile event"); }
		if(e.getSource()==exit) { Runtime.getRuntime().exit(0); }
		if(e.getSource()==sample1) { System.out.println("catch sample1 event"); }
		if(e.getSource()==sample2) { System.out.println("catch sample2 event"); }
		if(e.getSource()==sample3) { System.out.println("catch sample3 event"); }
		if(e.getSource()==sample4) { System.out.println("catch sample4 event"); }
		if(e.getSource()==sample5) { System.out.println("catch sample5 event"); }
		if(e.getSource()==preferences) { System.out.println("catch preferences event"); }
		//if(e.getSource()==nextStep) { notify(); }
	}
	
	public boolean isStepByStep() {
		return stepToStep.isSelected();
	}
}