package viewer;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
	
	public static String getFileChooser(Component component) {
		JFileChooser chooser = new JFileChooser("D:\\workspace\\TMEALGAV");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("fichier dot, xml", "dot", "xml");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(component);
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	       return  chooser.getSelectedFile().getName();
	    else
	    	return null;
	}

}
