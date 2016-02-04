package datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class TreeNode implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	TreeNode parent;
	ArrayList<TreeNode> children;
	public String label;
	public int offset;
	public int height;
	public int width;
	
	public TreeNode(String label) {
		this.label = label;
		x=0;
		y=0;
		offset=0;
		height = 0;
		width = 0;
		children = new ArrayList<TreeNode>();
	}
	public TreeNode(int x, int y){
		this.x = x;
		this.y = y;
		offset=0;
		height = 0;
		width = 0;
		children = new ArrayList<TreeNode>();
	}
	public TreeNode(String label, int x, int y){
		this.x = x;
		this.y = y;
		this.label = label;
		offset=0;
		height = 0;
		width = 0;
		children = new ArrayList<TreeNode>();
	}
	
	public ArrayList<TreeNode> getChildren() { 
		return children;
	}
	public TreeNode getParent() {
		return parent;
	}
	
	public void connect(TreeNode node) {
		this.children.add(node);
		node.setParent(this);
	}
	
	private void setParent(TreeNode node) {
		this.parent=node;
	}
	
	public String toString() {
		if(label!=null)
			return label;
		else 
			return new String(label+"= ["+x+";"+y+"]");
	}
	public String getLabel() {
		return label;
	}
	@Override
	public TreeNode clone() throws CloneNotSupportedException {
		//Risque de boucler parents => children => parents  => children ...
		return (TreeNode) super.clone();
	}
	
	public HashSet<TreeNode> getDescendants() {
		HashSet<TreeNode> descendants =  new HashSet<>();
		for(TreeNode child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}
}
